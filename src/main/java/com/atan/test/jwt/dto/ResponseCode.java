package com.atan.test.jwt.dto;

public enum ResponseCode {
	SUCCESS(200), FAILED(400);
		
	private Integer code;

	private ResponseCode(int code) {
        this.code = code;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
}
