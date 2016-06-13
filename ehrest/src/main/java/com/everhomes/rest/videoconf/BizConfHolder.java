package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

public class BizConfHolder {
	
	private Object data;
	
	private Integer status;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
