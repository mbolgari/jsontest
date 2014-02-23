package com.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.spi.container.servlet.WebComponent;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly2.web.GrizzlyWebTestContainerFactory;

public class MyTestResourceTest extends JerseyTest {

    private final static String VALID_JSON_VIEW = "{\"key\":\"KEY\",\"value\":\"VALUE\"}";
    private final static String INVALID_JSON_VIEW = "{\"keyUNRECOGNIZED\":\"KEY\",\"value\":\"VALUE\"}";

    public MyTestResourceTest() {
	super("com.test;org.codehaus.jackson.jaxrs"); // this one does NOT work
	// super("com.test"); // this one works
    }

    @Override
    public WebAppDescriptor configure() {
	return new WebAppDescriptor.Builder().initParam(WebComponent.RESOURCE_CONFIG_CLASS, ClassNamesResourceConfig.class.getName()).build();
    }

    @Override
    public TestContainerFactory getTestContainerFactory() {
	return new GrizzlyWebTestContainerFactory();
    }

    @Test
    public void testSuccessfulPut() {
	Client client = new Client();
	String result = client.resource(getBaseURI().toString()).path("/test").header("Content-Type", "application/json")
		.put(String.class, VALID_JSON_VIEW);
	assertEquals(TestResource.PUT_RESULT, result);
    }

    /**
     * This is the important test. <br/>
     * In case the package "org.codehaus.jackson.jaxrs" is passed as provider package to scan via the constructor, then
     * the test will not pass, due to UnrecognizedPropertyException is not being intercepted via the ExceptionMapper and
     * swallowed. <br/>
     * In case the package "org.codehaus.jackson.jaxrs" is omitted then the test will pass because the
     * UnrecognizedPropertyException will be intercepted and swallowed.
     */
    @Test
    public void testPutUnrecognizedPropertyErrorNeedsToBeIntercepted() {
	Client client = new Client();
	client.resource(getBaseURI().toString()).path("/test").header("Content-Type", "application/json").put(String.class, INVALID_JSON_VIEW);
    }

    @Provider
    public static class MyExceptionMapper implements ExceptionMapper<Exception> {
	@Override
	public Response toResponse(Exception e) {
	    if (e instanceof com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
		    || e instanceof org.codehaus.jackson.map.exc.UnrecognizedPropertyException) {
		// This is the error that needs to be intercepted and swallowed.
		return Response.ok().build();
	    }
	    return Response.serverError().build();
	}
    }

}