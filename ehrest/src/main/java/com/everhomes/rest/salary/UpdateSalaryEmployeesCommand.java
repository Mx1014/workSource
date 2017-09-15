// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>参数:
 * <li>employeeOriginVal: 个人批次设定,参考{@link com.everhomes.rest.salary.SalaryEmployeeOriginValDTO}</li>
 * <li>salaryGroupId: 批次id</li>
 * <li>userId: 用户id</li>
 * <li>userDetailId: 用户档案id</li>
 * <li>name: 用户名称</li>
 * <li>ownerType: 'organization'</li>
 * <li>owernId: organizationId</li>
 * </ul>
 */
public class UpdateSalaryEmployeesCommand {

    @ItemType(SalaryEmployeeOriginValDTO.class)
	private List<SalaryEmployeeOriginValDTO> employeeOriginVal;

    private Long salaryGroupId;

    private Long userId;

    private Long userDetailId;

    private String name;

    private String ownerType;

    private Long ownerId;

	public UpdateSalaryEmployeesCommand() {

	}

    public Long getSalaryGroupId() {
        return salaryGroupId;
    }

    public void setSalaryGroupId(Long salaryGroupId) {
        this.salaryGroupId = salaryGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserDetailId() {
        return userDetailId;
    }

    public void setUserDetailId(Long userDetailId) {
        this.userDetailId = userDetailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
