package com.everhomes.rest.address;

public enum AddressType {
	COMMUNITY_ADDRESS((byte)1,"小区地址"),
	SERVICE_ADDRESS((byte)2,"服务地址");
	
	private byte code;
	private String msg;
	
	private AddressType(byte code,String msg){
		this.code=code;
		this.msg=msg;
	}
	
	public byte getCode() {
		return code;
	}
	public void setCode(byte code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
