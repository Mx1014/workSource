package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>formFieldsConfigId：表单字段配置ID</li>
 * </ul>
 * @author  huqi
 */
public class GetFormFieldsConfigCommand {
    private Long formFieldsConfigId;

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
}
