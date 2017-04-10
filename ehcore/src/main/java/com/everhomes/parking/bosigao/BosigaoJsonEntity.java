package com.everhomes.parking.bosigao;

import java.util.List;

public class BosigaoJsonEntity<T> {
	
	private String result;
	private List<T> data;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return "0".equals(this.result);
	}
}
