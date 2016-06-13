package com.everhomes.rest.techpark.punch;


import java.sql.Date;
import java.sql.Time;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 * <li>userId：打卡人id</li>
 * <li>punchTime：打卡时间</li>
 * <li>status：打卡状态</li>
 * <li>approvalStatus:异常审批状态</li>
 * </ul>
 */
public class PunchDayLogDTO {
	private Long id;
	private Long userId;
    private Date punchTime;
	private Long    workTime;
    private Byte status;
    private Byte approvalStatus;
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
	public Date getPunchTime() {
		return punchTime;
	}
	public void setPunchTime(Date punchTime) {
		this.punchTime = punchTime;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Byte getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Long getWorkTime() {
		return workTime;
	}
	public void setWorkTime(Long workTime) {
		this.workTime = workTime;
	}
}
