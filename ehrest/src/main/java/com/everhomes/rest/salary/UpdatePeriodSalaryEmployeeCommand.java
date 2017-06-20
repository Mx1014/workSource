// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * <li>salaryEmployeeId: 所属id</li>
 * <li>periodEmployeeEntitys: 批次档期的字段列表</li>
 * <li>checkFlag: 核算标准0-未核算 1-核算</li>
 * </ul>
 */
public class UpdatePeriodSalaryEmployeeCommand {

	private String ownerType;

	private Long ownerId;

	private Long salaryEmployeeId;

	@ItemType(SalaryPeriodEmployeeEntityDTO.class)
	private List<SalaryPeriodEmployeeEntityDTO> periodEmployeeEntitys;

	private Byte checkFlag;

	public UpdatePeriodSalaryEmployeeCommand() {

	}

	public UpdatePeriodSalaryEmployeeCommand(String ownerType, Long ownerId, Long salaryEmployeeId, List<SalaryPeriodEmployeeEntityDTO> periodEmployeeEntitys, Byte checkFlag) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.salaryEmployeeId = salaryEmployeeId;
		this.periodEmployeeEntitys = periodEmployeeEntitys;
		this.checkFlag = checkFlag;
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

	public Long getSalaryEmployeeId() {
		return salaryEmployeeId;
	}

	public void setSalaryEmployeeId(Long salaryEmployeeId) {
		this.salaryEmployeeId = salaryEmployeeId;
	}

	public List<SalaryPeriodEmployeeEntityDTO> getPeriodEmployeeEntitys() {
		return periodEmployeeEntitys;
	}

	public void setPeriodEmployeeEntitys(List<SalaryPeriodEmployeeEntityDTO> periodEmployeeEntitys) {
		this.periodEmployeeEntitys = periodEmployeeEntitys;
	}

	public Byte getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Byte checkFlag) {
		this.checkFlag = checkFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
