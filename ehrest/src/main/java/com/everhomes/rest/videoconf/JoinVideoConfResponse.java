package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>joinUrl: 加入会议地址</li>
 *  <li>confId: 会议号</li>
 *  <li>password: 会议密码</li>
 * </ul>
 *
 */
public class JoinVideoConfResponse {
	
	private String joinUrl;
	
	private String condId;
	
	private String password;
	
	public String getJoinUrl() {
		return joinUrl;
	}

	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}

	public String getCondId() {
		return condId;
	}

	public void setCondId(String condId) {
		this.condId = condId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
