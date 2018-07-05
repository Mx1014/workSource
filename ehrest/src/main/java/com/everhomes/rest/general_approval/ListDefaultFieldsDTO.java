package com.everhomes.rest.general_approval;


/**
 * <ul>
 *     <li>fieldName: 字段名字，对应的 form 表单里面的名字</li>
 *     <li>fieldDisplayName: 显示的字段名字</li>
 *     <li>fieldType: 字段类型 {@link com.everhomes.rest.general_approval.GeneralFormFieldType}</li>
 *     <li>fieldContentType: fieldContentType</li>
 *     <li>fieldDesc: 提示文案</li>
 *     <li>requiredFlag: 是否必填 1必填，0不填</li>
 *     <li>dynamicFlag: 是否动态获取数据</li>
 *     <li>visibleType: 显示风格，比如：隐藏，只读，可以修改 {@link com.everhomes.rest.general_approval.GeneralFormDataVisibleType}</li>
 *     <li>renderType: 渲染类型，{@link com.everhomes.rest.general_approval.GeneralFormRenderType}</li>
 *     <li>dataSourceType: 数据源类型 {@link com.everhomes.rest.general_approval.GeneralFormDataSourceType}</li>
 *     <li>validatorType: 校验方式 {@link com.everhomes.rest.general_approval.GeneralFormValidatorType}</li>
 *     <li>fieldExtra: fieldExtra</li>
 *     <li>fieldValue: 字段值</li>
 *     <li>fieldGroupName: 字段组名称</li>
 *     <li>fieldAttribute: 字段属性 比如：DEFAULT-系统字段 {@link GeneralFormFieldAttribute}</li>
 *     <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改</li>
 *     <li>deleteFlag: 是否可修改 0-不可删除 1-可以删除</li>
 *     <li>filterFlag: 是否设置为筛选条件 0-不 1-是</li>
 * </ul>
 */
public class ListDefaultFieldsDTO {
    private String fieldName;
    private String fieldDisplayName;
    private GeneralFormFieldType fieldType;
    private String fieldContentType;
    private String fieldDesc;

    private Byte requiredFlag;
    private Byte dynamicFlag;
    private GeneralFormDataVisibleType visibleType;
    private GeneralFormRenderType renderType;
    private GeneralFormDataSourceType dataSourceType;
    private GeneralFormValidatorType validatorType;
    private String fieldExtra;
    private String fieldValue;
    private String fieldGroupName;
    private String formAttribute;

    private Byte filterFlag;
    private Byte modifyFlag;
    private Byte deleteFlag;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    public GeneralFormFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(GeneralFormFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldContentType() {
        return fieldContentType;
    }

    public void setFieldContentType(String fieldContentType) {
        this.fieldContentType = fieldContentType;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public Byte getRequiredFlag() {
        return requiredFlag;
    }

    public void setRequiredFlag(Byte requiredFlag) {
        this.requiredFlag = requiredFlag;
    }

    public Byte getDynamicFlag() {
        return dynamicFlag;
    }

    public void setDynamicFlag(Byte dynamicFlag) {
        this.dynamicFlag = dynamicFlag;
    }

    public GeneralFormDataVisibleType getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(GeneralFormDataVisibleType visibleType) {
        this.visibleType = visibleType;
    }

    public GeneralFormRenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(GeneralFormRenderType renderType) {
        this.renderType = renderType;
    }

    public GeneralFormDataSourceType getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(GeneralFormDataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public GeneralFormValidatorType getValidatorType() {
        return validatorType;
    }

    public void setValidatorType(GeneralFormValidatorType validatorType) {
        this.validatorType = validatorType;
    }

    public String getFieldExtra() {
        return fieldExtra;
    }

    public void setFieldExtra(String fieldExtra) {
        this.fieldExtra = fieldExtra;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldGroupName() {
        return fieldGroupName;
    }

    public void setFieldGroupName(String fieldGroupName) {
        this.fieldGroupName = fieldGroupName;
    }

    public String getFormAttribute() {
        return formAttribute;
    }

    public void setFormAttribute(String formAttribute) {
        this.formAttribute = formAttribute;
    }

    public Byte getFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(Byte filterFlag) {
        this.filterFlag = filterFlag;
    }

    public Byte getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Byte modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "ListDefaultFieldsDTO{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldDisplayName='" + fieldDisplayName + '\'' +
                ", fieldType=" + fieldType +
                ", fieldContentType='" + fieldContentType + '\'' +
                ", fieldDesc='" + fieldDesc + '\'' +
                ", requiredFlag=" + requiredFlag +
                ", dynamicFlag=" + dynamicFlag +
                ", visibleType=" + visibleType +
                ", renderType=" + renderType +
                ", dataSourceType=" + dataSourceType +
                ", validatorType=" + validatorType +
                ", fieldExtra='" + fieldExtra + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                ", fieldGroupName='" + fieldGroupName + '\'' +
                ", formAttribute='" + formAttribute + '\'' +
                ", filterFlag=" + filterFlag +
                ", modifyFlag=" + modifyFlag +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
