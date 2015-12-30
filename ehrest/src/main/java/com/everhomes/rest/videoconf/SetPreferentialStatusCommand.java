package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * status: 优惠状态设置
 *
 */
public class SetPreferentialStatusCommand {
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
