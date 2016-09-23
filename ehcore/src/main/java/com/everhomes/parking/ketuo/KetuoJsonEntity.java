package com.everhomes.parking.ketuo;

import java.util.List;

public class KetuoJsonEntity<T> {
	private Integer resCode;
	private String resMsg;
	private List<T> data;
	
	public Integer getResCode() {
		return resCode;
	}
	public void setResCode(Integer resCode) {
		this.resCode = resCode;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	
	public Boolean isSuccess() {
		if(null != resCode && resCode.intValue() == 0)
			return true;
		return false;
	}
	
}
