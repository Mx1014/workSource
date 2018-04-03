// @formatter:off
package com.everhomes.parking.zhongbaichang;

import com.everhomes.util.StringHelper;

public class ZhongBaiChangCardInfo<T> {
	private String result;
	private String errorCode;
	private String errorMsg;
	private T data;
	
	public boolean isSuccess(){
		return "success".equals(result);
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
