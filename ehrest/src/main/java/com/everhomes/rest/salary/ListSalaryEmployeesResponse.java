// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryEmployeeDTO: 基础字段参考{@link com.everhomes.rest.salary.SalaryEmployeeDTO}</li>
 * <li>exceptionCount: 异常人数</li>
 * </ul>
 */
public class ListSalaryEmployeesResponse {

	@ItemType(SalaryEmployeeDTO.class)
	private List<SalaryEmployeeDTO> salaryEmployeeDTO;

	private Integer exceptionCount;

	public ListSalaryEmployeesResponse() {

	}

	public ListSalaryEmployeesResponse(List<SalaryEmployeeDTO> salaryEmployeeDTO) {
		super();
		this.salaryEmployeeDTO = salaryEmployeeDTO;
	}

	public List<SalaryEmployeeDTO> getSalaryEmployeeDTO() {
		return salaryEmployeeDTO;
	}

	public void setSalaryEmployeeDTO(List<SalaryEmployeeDTO> salaryEmployeeDTO) {
		this.salaryEmployeeDTO = salaryEmployeeDTO;
	}

	public Integer getExceptionCount() {
		return exceptionCount;
	}

	public void setExceptionCount(Integer exceptionCount) {
		this.exceptionCount = exceptionCount;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
