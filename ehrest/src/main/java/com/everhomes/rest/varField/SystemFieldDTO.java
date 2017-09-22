package com.everhomes.rest.varField;

/**
 * <ul>
 *     <li>id: 字段在系统中的id</li>
 *     <li>moduleName: 所属模块名</li>
 *     <li>name: 字段逻辑名</li>
 *     <li>displayName: 显示名</li>
 *     <li>fieldType: 字段类型</li>
 *     <li>groupId: 字段所属组</li>
 *     <li>groupPath: 字段所属组路径</li>
 *     <li>mandatoryFlag: 是否必选</li>
 *     <li>defaultOrder: 显示顺序</li>
 * </ul>
 * Created by ying.xiong on 2017/9/21.
 */
public class SystemFieldDTO {
    private Long id;
    private String moduleName;
    private String name;
    private String displayName;
    private String fieldType;
    private Long groupId;
    private String groupPath;
    private Byte mandatoryFlag;
    private Integer defaultOrder;
    private String fieldParam;

    public String getFieldParam() {
        return fieldParam;
    }

    public void setFieldParam(String fieldParam) {
        this.fieldParam = fieldParam;
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

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupPath() {
        return groupPath;
    }

    public void setGroupPath(String groupPath) {
        this.groupPath = groupPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getMandatoryFlag() {
        return mandatoryFlag;
    }

    public void setMandatoryFlag(Byte mandatoryFlag) {
        this.mandatoryFlag = mandatoryFlag;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
