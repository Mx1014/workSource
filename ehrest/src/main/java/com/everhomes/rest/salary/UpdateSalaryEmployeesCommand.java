// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>参数:
 * <li>employeeOriginVal: 个人批次设定</li>
 * <li>userId: 用户id</li>
 * <li>detailId: 用户档案id</li>
 * <li>ownerType: 'organization'</li>
 * <li>owernId: organizationId</li>
 * </ul>
 */
public class UpdateSalaryEmployeesCommand {

    @ItemType(SalaryEmployeeOriginValDTO.class)
	private List<SalaryEmployeeOriginValDTO> employeeOriginVal;

    private Long userId;

    private Long detailId;

    private String ownerType;

    private Long ownerId;

	public UpdateSalaryEmployeesCommand() {

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
