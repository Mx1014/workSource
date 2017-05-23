package com.everhomes.rest.parking;

/**
 * Created by xq.tian on 2016/12/6.
 */
public interface ParkingLocalStringCode {

    //------------------- template --------------------
    String SCOPE_TEMPLATE = "parking.clearance";

    int CLEARANCE_FLOW_CASE_DETAIL_CONTENT_APPLICANT = 1;
    int CLEARANCE_FLOW_CASE_DETAIL_CONTENT_PROCESSOR = 2;
    int CLEARANCE_FLOW_CASE_BRIEF_CONTENT = 3;

    //------------------- String -----------------------
    String SCOPE_STRING = "parking.clearance";

    String NONE_CODE = "1";// 无
    String INSUFFICIENT_PRIVILEGE_CLEARANCE_MESSAGE_CODE = "2";// 没有申请放行权限
    String INSUFFICIENT_PRIVILEGE_CLEARANCE_TASK_MESSAGE_CODE = "3";// 没有处理放行任务权限
    String NO_DATA = "10011";// 没有数据

    String SCOPE_STRING_STATUS = "parking.clearance.log.status";
}
