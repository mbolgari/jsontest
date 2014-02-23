package com.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class TestResource {

    public final static String GET_RESULT = "GET";
    public final static String PUT_RESULT = "PUT";

    @GET
    @Produces("text/plain")
    public String get() {
	return "TEST RESOURCE RESPONSE";
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces("text/plain")
    public String put(View view) {
	System.out.println(view);
	return "PUT";
    }

}
