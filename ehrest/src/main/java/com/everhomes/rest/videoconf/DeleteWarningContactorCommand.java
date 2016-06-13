package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * warningContactorId: 预警联系人主键id
 *
 */
public class DeleteWarningContactorCommand {

	private Long warningContactorId;

	public Long getWarningContactorId() {
		return warningContactorId;
	}

	public void setWarningContactorId(Long warningContactorId) {
		this.warningContactorId = warningContactorId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
