package com.everhomes.rest.salary;
/**
 * 工资条对应人员的字段项DTO
 * <ul>参数:
 * <li>salaryEmployeeId: salaryEmployeeId 确定是哪个员工哪一期的数据</li>
 * <li>groupEntityId: 字段id</li>
 * <li>groupEntityName: 字段项名称</li>
 * <li>salaryValue: 字段项值</li>
 * <li>isFormula: 是否为公式 0-否 1-是</li>
 * </ul>
 * */
public class SalaryPeriodEmployeeEntityDTO {

    private Long salaryEmployeeId;
    private Long groupEntityId;
    private String groupEntityName;
    private String salaryValue;
	private Byte isFormula;
	public Long getSalaryEmployeeId() {
		return salaryEmployeeId;
	}
	public void setSalaryEmployeeId(Long salaryEmployeeId) {
		this.salaryEmployeeId = salaryEmployeeId;
	}
	public String getGroupEntryName() {
		return groupEntityName;
	}
	public void setGroupEntryName(String groupEntryName) {
		this.groupEntityName = groupEntryName;
	}
	public String getSalaryValue() {
		return salaryValue;
	}
	public void setSalaryValue(String salaryValue) {
		this.salaryValue = salaryValue;
	}

	public Byte getIsFormula() {
		return isFormula;
	}

	public void setIsFormula(Byte isFormula) {
		this.isFormula = isFormula;
	}

	public Long getGroupEntityId() {
		return groupEntityId;
	}

	public void setGroupEntityId(Long groupEntityId) {
		this.groupEntityId = groupEntityId;
	}
}
