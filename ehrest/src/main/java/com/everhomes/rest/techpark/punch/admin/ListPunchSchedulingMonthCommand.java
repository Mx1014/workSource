package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * <li>targetType: 填organization/user</li>
 * <li>targetId：对应设置目标的id比如机构比如人的id</li>
 * <li>punchOriganizationId：打卡考勤组id</li>
 * <li>queryTime: 查询时间 数字时间戳</li>
 * <li>employees：每一个人的排班{@link PunchSchedulingEmployeeDTO}</li> 
 * </ul>
 */
public class ListPunchSchedulingMonthCommand {

	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	private String targetType;
	private Long targetId; 
	private Long punchOriganizationId;
	private Long queryTime; 
	private List<PunchSchedulingEmployeeDTO> employees;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
 
	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Long queryTime) {
		this.queryTime = queryTime;
	}

	public Long getPunchOriganizationId() {
		return punchOriganizationId;
	}

	public void setPunchOriganizationId(Long punchOriganizationId) {
		this.punchOriganizationId = punchOriganizationId;
	}

	public List<PunchSchedulingEmployeeDTO> getEmployees() {
		return employees;
	}

	public void setEmployees(List<PunchSchedulingEmployeeDTO> employees) {
		this.employees = employees;
	}

}
