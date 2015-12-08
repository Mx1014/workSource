package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>confHostId: 会议主持人id</li>
 *  <li>confHostName: 会议主持人name</li>
 *  <li>maxCount: 最大与会人数</li>
 *  <li>token: token</li>
 * </ul>
 *
 */
public class StartVideoConfResponse {

	private String confHostId;
	
	private String confHostName;
	
	private Integer maxCount;
	
	private String token;
	
	public String getConfHostId() {
		return confHostId;
	}

	public void setConfHostId(String confHostId) {
		this.confHostId = confHostId;
	}

	public String getConfHostName() {
		return confHostName;
	}

	public void setConfHostName(String confHostName) {
		this.confHostName = confHostName;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
