package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>confHostId: 会议主持人id</li>
 *  <li>confHostName: 会议主持人name</li>
 *  <li>maxCount: 最大与会人数</li>
 *  <li>token: token</li>
 *  <li>meetingNo: 会议id</li>
 * </ul>
 *
 */
public class StartVideoConfResponse {

	private String confHostId;
	
	private String confHostName;
	
	private Integer maxCount;
	
	private String token;
	
	private String meetingNo;
	
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

	public String getMeetingNo() {
		return meetingNo;
	}

	public void setMeetingNo(String meetingNo) {
		this.meetingNo = meetingNo;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
