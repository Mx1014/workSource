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
    Long updateFormFieldsConfig(GeneralFormFieldsConfig formFieldsConfig);

    /**
     * 删除表单字段配置
     */
    void deleteFormFieldsConfig(Long formFieldsConfigId);

    /**
     * 获取表单字段配置
     */
    GeneralFormFieldsConfig getFormFieldsConfig(Long formFieldsConfigId);


}
