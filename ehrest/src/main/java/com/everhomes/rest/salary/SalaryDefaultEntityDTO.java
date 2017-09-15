package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>defaultFlag: 是否是缺省参数:0-否 1-是</li>
 * <li>type: 字段类型:0-文本类 1-数值类</li>
 * <li>categoryId: 项目标签(统计分类) id</li>
 * <li>categoryName: 项目标签(统计分类)名称 example:基础,应发,应收,合计</li>
 * <li>name: 项目字段名称</li>
 * <li>editableFlag: 是否可编辑(对文本类):0-否 1-是</li>
 * <li>templeteName: 模板名称</li>
 * <li>defaultOrder: 默认顺序</li>
 * </ul>
 */
public class SalaryDefaultEntityDTO {


    private Long id;

    private Byte defaultFlag;

    private Byte type;

    private Long categoryId;

    private String categoryName;

    private String name;

    private Byte editableFlag;

    private String templateName;

    private Integer defaultOrder;

    public SalaryDefaultEntityDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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
