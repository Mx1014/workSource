package com.everhomes.rest.videoconf;

public class BizConfDTO {

	private String userId;
	
	private String token;
	
	private Integer maxCount;
	
	private String meetingNo;
	
	private String id;
	
	private String meetingName;
	
	private String hostKey;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public String getMeetingNo() {
		return meetingNo;
	}

	public void setMeetingNo(String meetingNo) {
		this.meetingNo = meetingNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeetingName() {
		return meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public String getHostKey() {
		return hostKey;
	}

	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}
	
	
}
