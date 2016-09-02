package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

public class BizConfHolder {
	
	private BizConfDTO data;
	
	private Integer status;

	public BizConfDTO getData() {
		return data;
	}

	public void setData(BizConfDTO data) {
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
