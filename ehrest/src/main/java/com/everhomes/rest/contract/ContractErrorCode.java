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
    int ERROR_CONTRACT_APARTMENT_IS_NOT_FREE = 10007;  //合同关联的资产不是待租状态

    int ERROR_NO_DATA = 800000;

    int ERROR_ORGIDORCOMMUNITYID_IS_EMPTY = 10008;  //合同查询参数错误
    int ERROR_CONTRACT_TRACKING_NOT_EXIST = 10009; //合同日志项不存在

    int ERROR_CONTRACTTEMPLATENAME_EXIST = 10010; //合同模板名称已存在
    int ERROR_CONTRACTGOGSFILENOTEXIST_NOTEXIST = 10011; //合同模板不存在

    int ERROR_CONTRACT_BILL_NOT_EXIST = 10012;//合同关联的账单不存在

    int ERROR_BILLINGCYCLE_IS_EMPTY = 10013;  //合同计价条款计费周期不存在
    int ERROR_ADDRESS_PROPERTIES_IS_EXIST = 10014;  //该房源信息已经存在
    int ERROR_APARTMENTS_NOT_FREE_ERROR = 10015;//存在已占用房源
    int ERROR_ADJUST_CHANGEPERIOD_IS_ERROR = 10016;//调租 调整周期 不能为0

    int ERROR_CONTRACT_SYNC_UNKNOW_ERROR = 20001;//同步数据失败
    int ERROR_CONTRACT_SYNC_CUSTOMER_ERROR = 20002;//同步数据失败因为同步客户
    int ERROR_CONTRACT_SYNC_CONTRACT_ERROR = 20003;//同步数据失败因为同步合同
    int ERROR_CONTRACT_SYNC_BILL_ERROR = 20004;//同步数据失败因为账单

	int ERROR_CONTRACT_DOCUMENT_NAME_EXIST = 10201;//合同文档已存在
    //合同报表模块
    int CONTRACT_STATICS_TIME_DIMENSION_ERROR = 30001;//合同报表查询时间错误
    int CONTRACT_STATICS_COMMUNITYS_ERROR = 30002;//合同报表查询园区错误

}
