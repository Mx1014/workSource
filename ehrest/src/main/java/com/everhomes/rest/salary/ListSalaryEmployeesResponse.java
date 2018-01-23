// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryEmployeeDTO: 基础字段参考{@link com.everhomes.rest.salary.SalaryEmployeeDTO}</li>
 * <li>month: 月份 YYYYMM</li>
 * <li>nextPageAnchor</li>
 * </ul>
 */
public class ListSalaryEmployeesResponse {

	@ItemType(SalaryEmployeeDTO.class)
	private List<SalaryEmployeeDTO> salaryEmployeeDTO;

	private String month;

	private Integer nextPageOffset;

	private Long nextPageAnchor;

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

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}
