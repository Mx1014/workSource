package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>joinUrl: 加入会议地址</li>
 *  <li>confId: 会议号</li>
 * </ul>
 *
 */
public class JoinVideoConfResponse {
	
	private String joinUrl;
	
	private Integer condId;
	
	public String getJoinUrl() {
		return joinUrl;
	}

	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}

	public Integer getCondId() {
		return condId;
	}

	public void setCondId(Integer condId) {
		this.condId = condId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
