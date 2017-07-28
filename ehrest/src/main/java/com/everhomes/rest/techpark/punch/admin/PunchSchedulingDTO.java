package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
/**
 * <ul>
 * 
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * <li>targetType: 填organization/user</li>
 * <li>targetId：对应设置目标的id比如机构比如人的id</li>
 * <li>month：月份时间戳</li>
 * <li>punchOriganizationId：考勤组id</li> 
 * <li>employees：每一个人的排班{@link PunchSchedulingEmployeeDTO}</li> 
 * 
 * 
 * </ul>
 */
public class PunchSchedulingDTO {

	private String ownerType;
	private Long ownerId;
	private Long month;
	private Long punchOriganizationId; 
	private List<PunchSchedulingEmployeeDTO> employees;
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
	public Long getMonth() {
		return month;
	}
	public void setMonth(Long month) {
		this.month = month;
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
