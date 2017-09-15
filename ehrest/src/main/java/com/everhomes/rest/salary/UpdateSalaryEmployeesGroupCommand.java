// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>userId: 用户id</li>
 * <li>detailId: 用户档案id</li>
 * <li>name: 用户名称</li>
 * <li>salaryGroupId: 批次id</li>
 * <li>ownerType: 'organization'</li>
 * <li>owernId: organizationId</li>
 * </ul>
 */
public class UpdateSalaryEmployeesGroupCommand {

	private Long userId;

	private Long detailId;

	private String name;

	private Long salaryGroupId;

	private String ownerType;

	private Long ownerId;


	public UpdateSalaryEmployeesGroupCommand() {

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
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
