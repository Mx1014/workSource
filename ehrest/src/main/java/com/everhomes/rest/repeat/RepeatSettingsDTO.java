package com.everhomes.rest.repeat;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>id:  循环周期id</li>
 *  <li>ownerId:  循环周期所属的主体id</li>
 *  <li>ownerType: 循环周期所属的主体，如QA</li>
 *  <li>foreverFlag: 是否永不过期，0-会过期 1-永不过期</li>
 *  <li>repeatCount: 重复次数</li>
 *  <li>startDate: 开始日期</li>
 *  <li>endDate: 结束日期</li>
 *  <li>timeRanges: 一天多次时的重复细节 json格式：{"ranges":[{"startTime":"08:00:00","endTime":"09:30:00"},{"startTime":"18:30:00","endTime":"19:30:00"}]}</li>
 *  <li>repeatType: 重复单位 0-不重复 1-天 2-周 3-月 4-年</li>
 *  <li>repeatInterval: 每N天/周/月/年 </li>
 *  <li>workDayFlag: 是否工作日</li>
 *  <li>expression: 重复细节 json格式</li>
 *  <li>status: 重复状态</li>
 * </ul>
 */
public class RepeatSettingsDTO {

	private Long id;
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Byte foreverFlag;
	
	private Integer repeatCount;
	
	private Long startDate;
	
	private Long endDate;
	
	private String timeRanges;
	
	private Byte repeatType;
	
	private Integer repeatInterval;
	
	private Byte workDayFlag;
	
	private String expression;
	
	private Byte status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Byte getForeverFlag() {
		return foreverFlag;
	}

	public void setForeverFlag(Byte foreverFlag) {
		this.foreverFlag = foreverFlag;
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getTimeRanges() {
		return timeRanges;
	}

	public void setTimeRanges(String timeRanges) {
		this.timeRanges = timeRanges;
	}

	public Byte getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(Byte repeatType) {
		this.repeatType = repeatType;
	}

	public Integer getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(Integer repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public Byte getWorkDayFlag() {
		return workDayFlag;
	}

	public void setWorkDayFlag(Byte workDayFlag) {
		this.workDayFlag = workDayFlag;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
