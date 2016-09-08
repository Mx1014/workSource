package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>subject: 会议主题</li>
 *  <li>timeZone: 时区</li>
 *  <li>startTime: 开始时间</li>
 *  <li>endTime: 结束时间</li>
 *  <li>description: 会议描述</li>
 *  <li>hostKey: 会议密码</li>
 *  <li>enterpriseId: 公司id</li>
 * </ul>
 */
public class ReserveVideoConfCommand {
	
	private Long id;
	
	private String subject;
	
	private String timeZone;
	
	private Long startTime;
	
	private Long endTime;
	
	private String description;
	
	private String hostKey;
	
	private Long enterpriseId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHostKey() {
		return hostKey;
	}

	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}
	
	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
