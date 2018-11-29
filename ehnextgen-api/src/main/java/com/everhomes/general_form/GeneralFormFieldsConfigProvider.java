package com.everhomes.general_form;

/**
 * @author huqi
 */
public interface GeneralFormFieldsConfigProvider {

    /**
     * 新建表单字段配置
     */
    Long createFormFieldsConfig(GeneralFormFieldsConfig formFieldsConfig);

    /**
     * 更新表单字段配置
     */
    void updateFormFieldsConfig(GeneralFormFieldsConfig formFieldsConfig);

    /**
     * 获取表单字段配置
     */
    GeneralFormFieldsConfig getFormFieldsConfig(Long formFieldsConfigId);


}
