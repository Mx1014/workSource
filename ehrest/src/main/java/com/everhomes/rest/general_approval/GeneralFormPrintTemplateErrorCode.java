package com.everhomes.rest.general_approval;

public interface GeneralFormPrintTemplateErrorCode {
    static final String SCOPE = "general_form_print";

    static final Integer ERROR_FORM_PRINT_TEMPLATE_NOT_FOUND = 10001;  //打印模板不存在
    static final Integer ERROR_FORM_PRINT_TEMPLATE_IS_EXISTS  = 10002;  //打印模板已经存在
    static final Integer ERROR_FORM_VALUE_IS_NULL = 10003; //表单值不存在
    static final Integer ERROR_FORM_IS_NOT_EXISTS = 10004; //表单不存在
}
