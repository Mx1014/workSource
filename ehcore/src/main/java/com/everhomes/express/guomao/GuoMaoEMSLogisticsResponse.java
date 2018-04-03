// @formatter:off
package com.everhomes.express.guomao;

import com.everhomes.util.StringHelper;

public class GuoMaoEMSLogisticsResponse<T> {
	private T traces;
	private String success;
	
	public T getTraces() {
		return traces;
	}

	public void setTraces(T traces) {
		this.traces = traces;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
	
	public boolean isSuccess(){
		return "T".equals(success);
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
