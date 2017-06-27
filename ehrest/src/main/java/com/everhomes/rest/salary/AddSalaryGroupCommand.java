// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>
 * <li>salaryGroupEntity: 批次项目，参考{@link com.everhomes.rest.salary.SalaryGroupEntityDTO}</li>
 * </ul>
 */
public class AddSalaryGroupCommand {

    @ItemType(SalaryGroupEntityDTO.class)
    private List<SalaryGroupEntityDTO> salaryGroupEntity;

    private String ownerType;

    private Long ownerId;

	public AddSalaryGroupCommand() {

	}

    public List<SalaryGroupEntityDTO> getSalaryGroupEntity() {
        return salaryGroupEntity;
    }

    public void setSalaryGroupEntity(List<SalaryGroupEntityDTO> salaryGroupEntity) {
        this.salaryGroupEntity = salaryGroupEntity;
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
