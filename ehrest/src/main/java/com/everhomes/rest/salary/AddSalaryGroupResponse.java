// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>返回值:
 * <li>salaryGroupEntity: 薪酬组选项</li>
 * </ul>
 */
public class AddSalaryGroupResponse {

	@ItemType(SalaryGroupEntityDTO.class)
	private List<SalaryGroupEntityDTO> salaryGroupEntity;

	public AddSalaryGroupResponse() {

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
