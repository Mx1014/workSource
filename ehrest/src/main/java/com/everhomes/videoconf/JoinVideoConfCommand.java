package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * confId: 会议id或主持人手机号
 *
 */
public class JoinVideoConfCommand {
	
	private String confId;

	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
