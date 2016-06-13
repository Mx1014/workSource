package com.everhomes.rest.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>subject: 会议主题</li>
 *  <li>timeZone: 时区</li>
 *  <li>startTime: 开始时间</li>
 *  <li>duration: 会议时长 单位：分钟</li>
 *  <li>description: 会议描述</li>
 *  <li>hostKey: 会议密码</li>
 * </ul>
 */
public class ConfReservationsDTO {
	
	private Long id;
	
	private String subject;
	
	private String description;
	
	private String timeZone;
	
	private Timestamp startTime;
	
	private Integer duration;
	
	private String   confHostKey;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getConfHostKey() {
		return confHostKey;
	}

	public void setConfHostKey(String confHostKey) {
		this.confHostKey = confHostKey;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
