package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id:id</li>
 * <li>categoryName: 类型名</li>
 * <li>description: 提示批注(有就有没有就不展示)</li>
 * <li>customFlag: 是否可以添加自定义字段 1-是 0-否</li>
 * <li>customType: 自定义字段的类型 0-发放项 1-扣款项 2-成本项 3-冗余项</li>
 * <li>entities: 字段项 {@link com.everhomes.rest.salary.SalaryPeriodEmployeeEntityDTO}</li>
 * </ul>
 */
public class EmployeeCategoryDTO {

    private Long id;
    private String categoryName;
    private String description;
    private Byte customFlag;
    private Byte customType;
    private Byte status;
    @ItemType(SalaryPeriodEmployeeEntityDTO.class)
    List<SalaryPeriodEmployeeEntityDTO> entities;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Byte getCustomFlag() {
        return customFlag;
    }

    public void setCustomFlag(Byte customFlag) {
        this.customFlag = customFlag;
    }

    public Byte getCustomType() {
        return customType;
    }

    public void setCustomType(Byte customType) {
        this.customType = customType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SalaryPeriodEmployeeEntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<SalaryPeriodEmployeeEntityDTO> entities) {
        this.entities = entities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
