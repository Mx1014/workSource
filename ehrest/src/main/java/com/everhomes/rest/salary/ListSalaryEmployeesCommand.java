// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>keywords: 搜索关键词</li>
 * <li>organizationId: 部门id</li>
 * <li>salaryGroupId: 薪酬批次id</li>
 * <li>isException: 是否筛选异常员工: 0-否 1-是</li>
 * <li>ownerType: 'organization'</li>
 * <li>ownerId: 公司organizationId</li>
 * </ul>
 */
public class ListSalaryEmployeesCommand {

	private String keywords;

	private Long departmentId;

	private Long salaryGroupId;

	private Byte isException;

	private String ownerType;

	private Long ownerId;

	public ListSalaryEmployeesCommand() {

	}

	public ListSalaryEmployeesCommand(String keywords, Long departmentId, Long salaryGroupId, Byte isException) {
		super();
		this.keywords = keywords;
		this.departmentId = departmentId;
		this.salaryGroupId = salaryGroupId;
		this.isException = isException;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public Byte getIsException() {
		return isException;
	}

	public void setIsException(Byte isException) {
		this.isException = isException;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
