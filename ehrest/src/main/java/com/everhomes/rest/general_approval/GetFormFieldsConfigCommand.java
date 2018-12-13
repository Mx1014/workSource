package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>formFieldsConfigId：表单字段配置ID</li>
 *     <li>sourceId: 资源ID</li>
 *     <li>sourceType: 资源类型</li>
 * </ul>
 * @author  huqi
 */
public class GetFormFieldsConfigCommand {
    private Long formFieldsConfigId;
    private Long sourceId;
    private String sourceType;

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

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}
