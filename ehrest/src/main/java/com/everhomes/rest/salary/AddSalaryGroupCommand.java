// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>type: 字段类型0-文本类 1-数值类</li>
 * <li>categoryId: 项目标签id</li>
 * <li>categoryName: 项目标签名称</li>
 * <li>name: 字段项名</li>
 * <li>editableFlag: 是否可编辑(对文本类):0-否   1-是</li>
 * <li>numberType: 数值类型:0-普通数值 1-计算公式</li>
 * <li>defaultValue: 默认值/默认数值/计算公式</li>
 * <li>needCheck: 是否需要核算0-否 1-是</li>
 * <li>defaultOrder: 默认排序</li>
 * </ul>
 */
public class AddSalaryGroupCommand {

	private Byte type;

	private Long categoryId;

	private String categoryName;

	private String name;

	private Byte editableFlag;

	private Byte numberType;

	private String defaultValue;

	private Byte needCheck;

	private Integer defaultOrder;

	public AddSalaryGroupCommand() {

	}

	public AddSalaryGroupCommand(Byte type, Long categoryId, String categoryName, String name, Byte editableFlag,
                                 Byte numberType, String defaultValue, Byte needCheck, Integer defaultOrder) {
		super();
		this.type = type;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.name = name;
		this.editableFlag = editableFlag;
		this.numberType = numberType;
		this.defaultValue = defaultValue;
		this.needCheck = needCheck;
		this.defaultOrder = defaultOrder;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getEditableFlag() {
		return editableFlag;
	}

	public void setEditableFlag(Byte editableFlag) {
		this.editableFlag = editableFlag;
	}

	public Byte getNumberType() {
		return numberType;
	}

	public void setNumberType(Byte numberType) {
		this.numberType = numberType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Byte getNeedCheck() {
		return needCheck;
	}

	public void setNeedCheck(Byte needCheck) {
		this.needCheck = needCheck;
	}

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
