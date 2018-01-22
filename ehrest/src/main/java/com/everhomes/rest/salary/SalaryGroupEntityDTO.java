package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 工资条的字段项DTO
 * 
 * <ul> 
 * <li>id: id --更新的时候id为null的会新增,id不为空的就会去更新</li>
 * <li>defaultFlag: 是否为缺省参数:0-否 1-是</li>
 * <li>editableFlag: 是否可编辑(对文本类):0-否   1-是</li>
 * <li>deleteFlag: 是否可删除:0-否 1-是</li>
 * <li>dataPolicy: 数据策略:0-次月沿用 1-次月清空</li>
 * <li>grantPolicy: 发放策略 0-税前 1-税后</li>
 * <li>taxPolicy: 纳税策略 0-工资 1-年终</li>
 * <li>status: 状态:0-不开启 2-开启</li>
 * <li>type: 字段的类型 0-发放项 1-扣款项 2-成本项 3-冗余项</li>
 * <li>name: 项目字段名称</li>
 * <li>description: 批注描述</li>
 * <li>categoryId: 薪酬结构类型 id</li>
 * <li>categoryName: 薪酬结构类型名</li>
 * </ul>
 * */
public class SalaryGroupEntityDTO {
	private Long id;
	private Long originEntityId;
	private Byte defaultFlag;
	private Byte editableFlag;
	private Byte deleteFlag;
	private Byte type;
	private Byte dataPolicy;
	private Byte grantPolicy;
	private Byte taxPolicy;
	private Long categoryId;
	private String categoryName;
	private String name;
	private String description;
	private Byte status;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Byte getDataPolicy() {
		return dataPolicy;
	}

	public void setDataPolicy(Byte dataPolicy) {
		this.dataPolicy = dataPolicy;
	}

	public Byte getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(Byte defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getEditableFlag() {
		return editableFlag;
	}

	public void setEditableFlag(Byte editableFlag) {
		this.editableFlag = editableFlag;
	}

	public Byte getGrantPolicy() {
		return grantPolicy;
	}

	public void setGrantPolicy(Byte grantPolicy) {
		this.grantPolicy = grantPolicy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOriginEntityId() {
		return originEntityId;
	}

	public void setOriginEntityId(Long originEntityId) {
		this.originEntityId = originEntityId;
	}

	public Byte getTaxPolicy() {
		return taxPolicy;
	}

	public void setTaxPolicy(Byte taxPolicy) {
		this.taxPolicy = taxPolicy;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}
}
