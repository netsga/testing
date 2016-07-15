package com.lge.hems.device.exceptions;

public class RestRequestException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -643849935766083732L;
	private String url;
	private String statusCode;
	
	public RestRequestException(String msg, String url) {
		super(msg);
		this.url = url;
	}
	
	public RestRequestException(String msg, String url, String statusCode) {
		super(msg);
		this.url = url;
		this.statusCode = statusCode;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getStatusCode() {
		return this.statusCode;
	}
}
