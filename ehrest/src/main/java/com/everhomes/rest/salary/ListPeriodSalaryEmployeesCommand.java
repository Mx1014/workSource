// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryPeriodGroupId: 某期薪酬批次id</li>
 * <li>organizationId : 筛选的部门id </li>
 * <li>keyWords: 搜索关键字</li>
 * <li>checkFlag: 0-未核算 1-已核算 不传null 不过滤</li>
 * </ul>
 */
public class ListPeriodSalaryEmployeesCommand {

	private String ownerType;

	private Long ownerId;

	private Long salaryPeriodGroupId;

	private Long organizationId;

	private String keyWords;

	private Byte checkFlag;

	public ListPeriodSalaryEmployeesCommand() {

	}

	public ListPeriodSalaryEmployeesCommand(String ownerType, Long ownerId, Long salaryPeriodGroupId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.salaryPeriodGroupId = salaryPeriodGroupId;
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

	public Long getSalaryPeriodGroupId() {
		return salaryPeriodGroupId;
	}

	public void setSalaryPeriodGroupId(Long salaryPeriodGroupId) {
		this.salaryPeriodGroupId = salaryPeriodGroupId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public Byte getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Byte checkFlag) {
		this.checkFlag = checkFlag;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
}
