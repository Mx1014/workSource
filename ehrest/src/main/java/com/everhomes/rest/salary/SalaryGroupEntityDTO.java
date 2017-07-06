package com.everhomes.rest.salary;
/**
 * 工资条的字段项DTO
 * 
 * <ul> 
 * <li>id: id</li>
 * <li>groupId: 批次id</li>
 * <li>defaultFlag: 是否为缺省参数:0-否 1-是</li>
 * <li>type: 字段类型:0-文本类 1-数值类</li>
 * <li>categoryId: 项目标签(统计分类) id</li>
 * <li>categoryName: 项目标签(统计分类)名称 example:基础,应发,应收,合计</li>
 * <li>originEntityId: 项目字段原始id</li>
 * <li>name: 项目字段名称</li>
 * <li>editableFlag: 是否可编辑(对文本类):0-否   1-是</li>
 * <li>numberType: 数值类型:0-普通数值 1-计算公式</li>
 * <li>defaultValue: 默认值/默认数值/计算公式</li>
 * <li>needCheck: 是否需要核算:0-否 1-是</li>
 * <li>defaultOrder: 排序</li>
 * <li>visibleFlag: 是否展示到工资条:0-否 1-是</li>
 * <li>templateName: 模板名称</li>
 * </ul>
 * */
public class SalaryGroupEntityDTO {
	private Long id;
	private Long groupId;
	private Long originEntityId;
	private Byte defaultFlag;
    private Byte type;
    private Long categoryId;
    private String categoryName;
	private String name;
	private Byte editableFlag;
	private Byte numberType;
    private String defaultValue;
    private Byte needCheck;
    private Integer defaultOrder;
    private Byte visibleFlag;
    private String templateName;
    
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

    public Long getOriginEntityId() {
        return originEntityId;
    }

    public void setOriginEntityId(Long originEntityId) {
        this.originEntityId = originEntityId;
    }

	public Byte getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(Byte defaultFlag) {
		this.defaultFlag = defaultFlag;
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
	public Byte getVisibleFlag() {
		return visibleFlag;
	}
	public void setVisibleFlag(Byte visibleFlag) {
		this.visibleFlag = visibleFlag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
