// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>参数:
 * <li>salaryGroupId: 薪酬批次id</li>
 * <li>salaryGroupEntity: 批次项目，参考{@link com.everhomes.rest.salary.SalaryGroupEntityDTO}</li>
 * </ul>
 */
public class UpdateSalaryGroupCommand {

	private Long salaryGroupId;

	@ItemType(SalaryGroupEntityDTO.class)
	private List<SalaryGroupEntityDTO> salaryGroupEntity;

	public UpdateSalaryGroupCommand() {

	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	public List<SalaryGroupEntityDTO> getSalaryGroupEntity() {
		return salaryGroupEntity;
	}

	public void setSalaryGroupEntity(List<SalaryGroupEntityDTO> salaryGroupEntity) {
		this.salaryGroupEntity = salaryGroupEntity;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
