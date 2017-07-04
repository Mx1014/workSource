package com.everhomes.rest.salary;
/**
 * 工资条对应人员的字段项DTO
 * <ul>参数:
 * <li>salaryEmployeeId: salaryEmployeeId 确定是哪个员工哪一期的数据</li>
 * <li>groupEntityId: 字段id</li>
 * <li>groupEntityName: 字段项名称</li>
 * <li>salaryValue: 字段项值</li>
 * <li>isFormula: 是否为公式 0-否 1-是</li>
 * <li>entityType: entity类型 0-文本 1-数值</li>
 * <li>editableFlag: 是否可编辑(对文本类):0-否   1-是</li>
 * <li>numberType: 数值类型:0-普通数值 1-计算公式</li>
 * <li>needCheck: 是否需要核算:0-否 1-是</li>
 * <li>defaultOrder: 排序</li>
 * <li>visibleFlag: 是否展示到工资条:0-否 1-是</li>
 * </ul>
 * */
public class SalaryPeriodEmployeeEntityDTO {

    private Long salaryEmployeeId;
    private Long groupEntityId;
    private String groupEntityName;
    private String salaryValue;
	private Byte isFormula;
	private Byte entityType;
	private Byte editableFlag;
	private Byte numberType;
	private Byte needCheck;
	private Integer defaultOrder;
	private Byte visibleFlag;

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public Byte getEditableFlag() {
		return editableFlag;
	}

	public void setEditableFlag(Byte editableFlag) {
		this.editableFlag = editableFlag;
	}

	public String getGroupEntityName() {
		return groupEntityName;
	}

	public void setGroupEntityName(String groupEntityName) {
		this.groupEntityName = groupEntityName;
	}

	public Byte getNeedCheck() {
		return needCheck;
	}

	public void setNeedCheck(Byte needCheck) {
		this.needCheck = needCheck;
	}

	public Byte getNumberType() {
		return numberType;
	}

	public void setNumberType(Byte numberType) {
		this.numberType = numberType;
	}

	public Byte getVisibleFlag() {
		return visibleFlag;
	}

	public void setVisibleFlag(Byte visibleFlag) {
		this.visibleFlag = visibleFlag;
	}

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

	public Byte getEntityType() {
		return entityType;
	}

	public void setEntityType(Byte entityType) {
		this.entityType = entityType;
	}
}
