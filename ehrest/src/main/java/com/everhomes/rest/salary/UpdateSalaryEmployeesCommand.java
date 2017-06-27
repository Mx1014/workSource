// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>参数:
 * <li>employeeOriginVal: 个人批次设定</li>
 * </ul>
 */
public class UpdateSalaryEmployeesCommand {

    @ItemType(SalaryEmployeeOriginValDTO.class)
	private List<SalaryEmployeeOriginValDTO> employeeOriginVal;

    private String ownerType;

    private Long ownerId;

	public UpdateSalaryEmployeesCommand() {

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

	public List<SalaryEmployeeOriginValDTO> getEmployeeOriginVal() {
		return employeeOriginVal;
	}

	public void setEmployeeOriginVal(List<SalaryEmployeeOriginValDTO> employeeOriginVal) {
		this.employeeOriginVal = employeeOriginVal;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
