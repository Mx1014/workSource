package com.everhomes.contentserver;

import com.everhomes.util.Name;

@Name(value = "contentstorage.response.config")
public class MessageHandleResponse extends ContentServerBase {
	private int errCode;

	private String errMsg;

	private String message;

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
