package com.everhomes.rest.salary;
/**
 * 工资条对应人员的字段项DTO
 * <ul>参数:
 * <li>salaryEmployeeId: salaryEmployeeId 确定是哪个员工哪一期的数据</li>
 * <li>groupEntryId: 字段id</li>
 * <li>groupEntryName: 字段项名称</li>
 * <li>salaryValue: 字段项值</li> 
 * </ul>
 * */
public class SalaryPeriodEmployeeEntityDTO {

    private Long salaryEmployeeId;
    private Long groupEntryId;
    private String groupEntryName;
    private String salaryValue;
	public Long getSalaryEmployeeId() {
		return salaryEmployeeId;
	}
	public void setSalaryEmployeeId(Long salaryEmployeeId) {
		this.salaryEmployeeId = salaryEmployeeId;
	}
	public Long getGroupEntryId() {
		return groupEntryId;
	}
	public void setGroupEntryId(Long groupEntryId) {
		this.groupEntryId = groupEntryId;
	}
	public String getGroupEntryName() {
		return groupEntryName;
	}
	public void setGroupEntryName(String groupEntryName) {
		this.groupEntryName = groupEntryName;
	}
	public String getSalaryValue() {
		return salaryValue;
	}
	public void setSalaryValue(String salaryValue) {
		this.salaryValue = salaryValue;
	}
    
}
