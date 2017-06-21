package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * 
 * <ul>参数:
 * <li>id: salaryEmployeeId</li>
 * <li>userId: 用户 id</li>
 * <li>salaryPeriod: 所属类型:Organization</li>
 * <li>checkFlag: 核算标准0-未核算 1-核算</li>
 * <li>periodEmployeeEntitys: 批次档期的字段列表 参考{@link com.everhomes.rest.salary.SalaryPeriodEmployeeEntityDTO}</li>
 * </ul>
 */
public class SalaryPeriodEmployeeDTO {

	@ItemType(SalaryPeriodEmployeeEntityDTO.class)
	private List<SalaryPeriodEmployeeEntityDTO> periodEmployeeEntitys;
	private Long id ;
    private String salaryPeriod;
    private Long userId;
	private Byte checkFlag;
	public List<SalaryPeriodEmployeeEntityDTO> getPeriodEmployeeEntitys() {
		return periodEmployeeEntitys;
	}
	public void setPeriodEmployeeEntitys(List<SalaryPeriodEmployeeEntityDTO> periodEmployeeEntitys) {
		this.periodEmployeeEntitys = periodEmployeeEntitys;
	}
	public String getSalaryPeriod() {
		return salaryPeriod;
	}
	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Byte getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(Byte checkFlag) {
		this.checkFlag = checkFlag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
