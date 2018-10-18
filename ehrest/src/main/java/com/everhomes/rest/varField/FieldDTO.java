package com.everhomes.rest.varField;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 域下的字段id，新加进去的没有</li>
 * <li>namespaceId: 域空间id</li>
 * <li>moduleName: 字段所属的模块类型名</li>
 * <li>groupId: 在系统里所属的字段组id</li>
 * <li>groupPath: 在系统里所属的字段组path</li>
 * <li>fieldId: 在系统里的字段id</li>
 * <li>fieldDisplayName: 字段显示名</li>
 * <li>fieldName: 字段名，数据库中的名字</li>
 * <li>fieldType: 字段类型枚举型，参考{@link com.everhomes.rest.varField.FieldType}</li>
 * <li>fieldParam: 字段描述，json 如：{fieldParamType: "file", length: 9}，字段组件类型枚举型，参考{@link FieldParamType}</li>
 * <li>mandatoryFlag: 是否必填 0: 否; 1: 是</li>
 * <li>defaultOrder: 顺序</li>
 * <li>items: 字段选择项 参考{@link FieldItemDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class FieldDTO {

    private Long id;

    private Integer namespaceId;

    private String moduleName;

    private Long groupId;

    private String groupPath;

    private Long fieldId;

    private String fieldDisplayName;

    private String fieldName;

    private String fieldType;

    private String fieldParam;

    private Byte mandatoryFlag;

    private Integer defaultOrder;

    @ItemType(FieldItemDTO.class)
    private List<FieldItemDTO> items;

    private String dateFormat;

    private Long ownerId;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<FieldItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FieldItemDTO> items) {
        this.items = items;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldParam() {
        return fieldParam;
    }

    public void setFieldParam(String fieldParam) {
        this.fieldParam = fieldParam;
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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
