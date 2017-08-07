package com.everhomes.rest.techpark.expansion;


public interface ApplyEntryErrorCodes {

    String SCOPE = "expansion";
    String SCOPE_APPLY_TYPE = "expansion.applyType";

    int FLOW_BRIEF_CONTENT_CODE = 1;// 工作流列表摘要
    int FLOW_DETAIL_CONTENT_CODE = 2;// 工作流详情
//    int FLOW_DETAIL_CONTENT_NOAREA_CODE = 3;// 工作流详情

    int LEASE_ISSUER_EXIST = 5; //已存在

    int ERROR_UPDATE_STATUS = 6; //状态不可修改

    int ERROR_BUILDING_NAME_EXIST = 7; //你要添加的楼栋已存在！
}
