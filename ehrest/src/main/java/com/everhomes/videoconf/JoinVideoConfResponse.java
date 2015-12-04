package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>joinUrl: 加入会议地址</li>
 * </ul>
 *
 */
public class JoinVideoConfResponse {
	
	private String joinUrl;
	
	public String getJoinUrl() {
		return joinUrl;
	}

	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
