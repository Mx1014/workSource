// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>type: 字段类型0-文本类 1-数值类</li>
 * <li>categoryId: 标签id</li>
 * <li>categoryName: 标签名称</li>
 * <li>name: 标签自定义名称</li>
 * <li>editableFlag: 是否可编辑(对文本类)0-否 1-是</li>
 * <li>sumFlag: 是否可合计(对数值类)0-否 1-是</li>
 * <li>numberType: 数值类型0-普通数值 1-计算公式</li>
 * <li>defaultValue: 默认值/默认数值/计算公式</li>
 * <li>needCheck: 是否需要核算0-否 1-是</li>
 * </ul>
 */
public class AddSalaryGroupCommand {

	private Byte type;

	private Long categoryId;

	private String categoryName;

	private String name;

	private Byte editableFlag;

	private Byte sumFlag;

	private Byte numberType;

	private String defaultValue;

	private Byte needCheck;

	public AddSalaryGroupCommand() {

	}

	public AddSalaryGroupCommand(Byte type, Long categoryId, String categoryName, String name, Byte editableFlag, Byte sumFlag, Byte numberType, String defaultValue, Byte needCheck) {
		super();
		this.type = type;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.name = name;
		this.editableFlag = editableFlag;
		this.sumFlag = sumFlag;
		this.numberType = numberType;
		this.defaultValue = defaultValue;
		this.needCheck = needCheck;
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

	public Byte getSumFlag() {
		return sumFlag;
	}

	public void setSumFlag(Byte sumFlag) {
		this.sumFlag = sumFlag;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
