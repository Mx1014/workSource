// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>id: 薪酬批次id(salaryGroup表)</li>
 * <li>salaryGroupId: 薪酬批次id(Organization表)</li>
 * <li>organizationId: 公司id</li>
 * <li>attachment: 文件</li>
 * <li>ownerType: 'organization'</li>
 * <li>owernId: organizationId</li>
 * </ul>
 */
public class ImportSalaryInfoCommand {

	private Long id;

	private Long salaryGroupId;

	private Long organizationId;

	private String ownerType;

	private Long ownerId;

	public ImportSalaryInfoCommand() {

	}

	public ImportSalaryInfoCommand(Long salaryGroupId, Long organizationId) {
		super();
		this.salaryGroupId = salaryGroupId;
		this.organizationId = organizationId;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
