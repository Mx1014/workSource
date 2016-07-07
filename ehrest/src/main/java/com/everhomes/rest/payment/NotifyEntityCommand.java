package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

public class NotifyEntityCommand {
	private String token;
	private String sign;
	private String msg;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
