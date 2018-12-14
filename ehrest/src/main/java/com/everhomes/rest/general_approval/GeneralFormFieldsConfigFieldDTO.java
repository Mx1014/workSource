package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>fieldType: 字段类型 {@link com.everhomes.rest.general_approval.GeneralFormFieldType}</li>
 * <li>fieldName: 字段名字，对应的 form 表单里面的名字</li>
 * <li>fieldDisplayName: 显示的字段名字</li>
 * <li>fieldDesc: 提示文案</li>
 * <li>fieldExtra: 不同的字段类型，还有额外的数据信息。
 * 数字类型：{@link com.everhomes.rest.general_approval.GeneralFormNumDTO}
 * 文本类型：{@link com.everhomes.rest.general_approval.GeneralFormTextDTO}
 * 图片类型：{@link com.everhomes.rest.general_approval.GeneralFormImageDTO}
 * 文件类型：{@link com.everhomes.rest.general_approval.GeneralFormFileDTO}
 * 日期类型：{@link com.everhomes.rest.general_approval.GeneralFormDateDTO}
 * 下拉框类型：{@link com.everhomes.rest.general_approval.GeneralFormDropBoxDTO}
 * 子表单类型：{@link com.everhomes.rest.general_approval.GeneralFormSubformDTO}
 * 企业联系人类型：{@link com.everhomes.rest.general_approval.GeneralFormContactDTO}
 * </li>
 * <li>requiredFlag: 是否必填 1必填，0不填 </li>
 * <li>dynamicFlag: 是否动态获取数据</li>
 * <li>visibleType: 显示风格，比如：隐藏，只读，可以修改 {@link com.everhomes.rest.general_approval.GeneralFormDataVisibleType}</li>
 * <li>validatorType: 校验方式 {@link com.everhomes.rest.general_approval.GeneralFormValidatorType}</li>
 * <li>dataSourceType: 数据源类型 {@link com.everhomes.rest.general_approval.GeneralFormDataSourceType}</li>
 * <li>renderType: 渲染类型，{@link com.everhomes.rest.general_approval.GeneralFormRenderType}</li>
 * <li>fieldValue: 字段值</li>
 * <li>fieldGroupName: 字段组名称</li>
 * <li>fieldAttribute: 字段属性 比如：DEFAULT-系统字段 {@link GeneralFormFieldAttribute}</li>
 * <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改</li>
 * <li>deleteFlag: 是否可修改 0-不可删除 1-可以删除</li>
 * <li>filterFlag: 是否被筛选 0-不被筛选 1-被筛选</li>
 * </ul>
 * @author huqi
 */
public class GeneralFormFieldsConfigFieldDTO {
    private String fieldName;
    private String fieldDisplayName;
    private String fieldType;
    private String fieldContentType;
    private String fieldDesc;
    private Byte requiredFlag;
    private Byte dynamicFlag;
    private String visibleType;
    private String renderType;
    private String dataSourceType;
    private String validatorType;
    private String fieldExtra;
    private String fieldValue;
    private String fieldGroupName;
    private String fieldAttribute;
    private Byte modifyFlag;
    private Byte deleteFlag;
    private Byte filterFlag;
    private String remark;
    private Byte disabled;

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }

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

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
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

    public String getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(String visibleType) {
        this.visibleType = visibleType;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getValidatorType() {
        return validatorType;
    }

    public void setValidatorType(String validatorType) {
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

    public String getFieldAttribute() {
        return fieldAttribute;
    }

    public void setFieldAttribute(String fieldAttribute) {
        this.fieldAttribute = fieldAttribute;
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

    public Byte getFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(Byte filterFlag) {
        this.filterFlag = filterFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getDisabled() {
        return disabled;
    }

    public void setDisabled(Byte disabled) {
        this.disabled = disabled;
    }
}
