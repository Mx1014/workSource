package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

public class BizConfStatus {

	private int confStatus;

	public int getConfStatus() {
		return confStatus;
	}

	public void setConfStatus(int confStatus) {
		this.confStatus = confStatus;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
