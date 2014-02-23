package com.test;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class View {

    private String key;
    private String value;

    public View() {
    }

    public View(String key, String value) {
	super();
	this.key = key;
	this.value = value;
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
