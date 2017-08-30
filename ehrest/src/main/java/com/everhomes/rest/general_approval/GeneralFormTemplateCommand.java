package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formModule: 表单所属模板名称</li>
 * <li>formName: 表单名称</li>
 * </ul>
 */
public class GeneralFormTemplateCommand {

    private String formModule;

    private String formName;

    public GeneralFormTemplateCommand() {
    }

    public String getFormModule() {
        return formModule;
    }

    public void setFormModule(String formModule) {
        this.formModule = formModule;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
