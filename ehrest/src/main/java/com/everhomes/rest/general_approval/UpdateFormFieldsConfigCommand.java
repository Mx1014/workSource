package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>formFieldsConfigId: 表单字段配置ID</li>
 *     <li>formOriginId: 表单原始ID</li>
 *     <li>formVersion: 表单版本</li>
 *     <li>configType：配置类型，默认"flowNode-visible"</li>
 *     <li>formFields：表单字段</li>
 * </ul>
 * @author huqi
 */
public class UpdateFormFieldsConfigCommand {
    private Long formFieldsConfigId;
    private Long formOriginId;
    private Long formVersion;
    private String configType;
    private String formFields;
    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }

    public Long getFormFieldsConfigId() {
        return formFieldsConfigId;
    }

    public void setFormFieldsConfigId(Long formFieldsConfigId) {
        this.formFieldsConfigId = formFieldsConfigId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getFormFields() {
        return formFields;
    }

    public void setFormFields(String formFields) {
        this.formFields = formFields;
    }
}
