package com.everhomes.rest.techpark.punch.admin;

import java.util.List;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li> 
 * <li>month：月份时间戳</li>
 * <li>punchOriganizationId：考勤组id</li> 
 * <li>employees：每一个人的排班{@link com.everhomes.rest.techpark.punch.admin.PunchSchedulingEmployeeDTO}</li>
 * 
 * 
 * </ul>
 */
public class PunchSchedulingDTO {

	private String ownerType;
	private Long ownerId;
	private Long month;
	private Long punchOriganizationId; 
	@ItemType(PunchSchedulingEmployeeDTO.class)
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
