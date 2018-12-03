package com.everhomes.general_form;

/**
 * @author huqi
 */
public interface GeneralFormFieldsConfigProvider {

    /**
     * 新建表单字段配置
     */
    GeneralFormFieldsConfig createFormFieldsConfig(GeneralFormFieldsConfig formFieldsConfig);

    /**
     * 更新表单字段配置
     */
    GeneralFormFieldsConfig updateFormFieldsConfig(GeneralFormFieldsConfig formFieldsConfig);

    /**
     * 获取表单字段配置
     */
    GeneralFormFieldsConfig getFormFieldsConfig(Long formFieldsConfigId);

    /**
     * 获取有效状态的表单字段配置
     */
    GeneralFormFieldsConfig getActiveFormFieldsConfig(Long formFieldsConfigId);


}
