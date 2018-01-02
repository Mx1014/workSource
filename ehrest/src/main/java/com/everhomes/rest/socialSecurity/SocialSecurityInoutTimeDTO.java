package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>detailId: 用户detailId</li>
 * <li>timeType: 类型：</li>
 * <li>startTime: startTime</li>
 * <li>endTime: endTime</li>
 * </ul>
 */
public class SocialSecurityInoutTimeDTO {

	private Long id;

	private Long userId;

	private Long detailId;

	private String timeType;

	private Timestamp startTime;

	private Timestamp endTime;

	public SocialSecurityInoutTimeDTO() {
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
}
