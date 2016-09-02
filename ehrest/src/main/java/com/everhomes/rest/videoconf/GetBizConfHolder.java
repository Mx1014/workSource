package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

public class GetBizConfHolder {

	private BizConfStatus data;
	
	private Integer status;

	public BizConfStatus getData() {
		return data;
	}

	public void setData(BizConfStatus data) {
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
