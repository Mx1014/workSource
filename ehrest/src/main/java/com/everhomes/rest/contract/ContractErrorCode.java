package com.everhomes.rest.contract;

/**
 * Created by ying.xiong on 2017/8/15.
 */
public interface ContractErrorCode {
    String SCOPE = "contract";

    int ERROR_CONTRACT_NOT_EXIST = 10001;  //合同不存在
    int ERROR_CONTRACTNUMBER_EXIST = 10002;  //合同编号已存在
}
