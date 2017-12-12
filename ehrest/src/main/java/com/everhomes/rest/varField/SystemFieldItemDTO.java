package com.everhomes.rest.varField;

/**
 * <ul>
 *     <li>id: 选项在系统中的id</li>
 *     <li>moduleName: 所属模块名</li>
 *     <li>fieldId: 所属字段在系统中的id</li>
 *     <li>displayName: 显示名</li>
 *     <li>defaultOrder: 显示顺序</li>
 *     <li>businessValue: 在业务模块中定义的值，没有则为空</li>
 * </ul>
 * Created by ying.xiong on 2017/9/21.
 */
public class SystemFieldItemDTO {
    private Long id;
    private String moduleName;
    private Long fieldId;
    private String displayName;
    private Integer defaultOrder;
    private Byte businessValue;

    public Byte getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(Byte businessValue) {
        this.businessValue = businessValue;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
