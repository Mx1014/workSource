package com.everhomes.rest.contract;

/**
 * Created by ying.xiong on 2017/8/15.
 */
public interface ContractErrorCode {
    String SCOPE = "contract";

    int ERROR_CONTRACT_NOT_EXIST = 10001;  //合同不存在
    int ERROR_CONTRACTNUMBER_EXIST = 10002;  //合同编号已存在
    int ERROR_ENABLE_FLOW = 10003;  //请启用工作流
    int ERROR_CONTRACT_NOT_APPROVE_QUALITIED = 10004;  //合同没有审批通过
    int ERROR_CONTRACT_PARAM_NOT_EXIST = 10005;  //合同参数已存在
    int ERROR_CONTRACT_NOT_WAITING_FOR_LAUNCH = 10006;  //只有待发起的合同可以发起
}
