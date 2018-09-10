package com.everhomes.rest.general_approval;

public class SubGeneralFormFieldDTO {
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

    //	added by R 20170825
    private String fieldGroupName;
    private String fieldAttribute;
    private Byte modifyFlag;
    private Byte deleteFlag;

    // added by hpy 20180711
    private Byte filterFlag;
}
