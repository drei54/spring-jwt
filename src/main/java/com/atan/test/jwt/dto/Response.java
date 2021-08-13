package com.atan.test.jwt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Response<T> {
	private String message;
	private Integer code;
	private T data;
	
	public Response() {}
	
	public Response(ResponseCode type) {
		this.code =type.getCode();
		this.message = type.toString();
	}
	
	public Response(ResponseCode type, T data) {
		this.code =type.getCode();
		this.message = type.toString();
		this.data = data;
	}

	public Response(T data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
}
