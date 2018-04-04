package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>detailId: 用户detailId</li>
 * <li>timeType: 类型：0-社保，1-公积金</li>
 * <li>startTime: startTime</li>
 * <li>endTime: endTime</li>
 * </ul>
 */
public class SocialSecurityInoutTimeDTO {

	private Long id;

	private Long userId;

	private Long detailId;

	private Byte inOutType;

	private Date startTime;

	private Date endTime;

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

	public Byte getInOutType() {
		return inOutType;
	}

	public void setInOutType(Byte inOutType) {
		this.inOutType = inOutType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
