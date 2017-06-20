// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryEmployeeDTO: 基础字段参考{@link com.everhomes.rest.salaryEmployeeDTO}</li>
 * </ul>
 */
public class ListSalaryStaffsResponse {

	@ItemType(salaryEmployeeDTO.class)
	private List<salaryEmployeeDTO> salaryEmployeeDTO;

	public ListSalaryStaffsResponse() {

	}

	public ListSalaryStaffsResponse(List<salaryEmployeeDTO> salaryEmployeeDTO) {
		super();
		this.salaryEmployeeDTO = salaryEmployeeDTO;
	}

	public List<salaryEmployeeDTO> getSalaryEmployeeDTO() {
		return salaryEmployeeDTO;
	}

	public void setSalaryEmployeeDTO(List<salaryEmployeeDTO> salaryEmployeeDTO) {
		this.salaryEmployeeDTO = salaryEmployeeDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
