package com.everhomes.rest.pmtask;

public interface PmTaskErrorCode {

	String SCOPE = "pmtask";

	int ERROR_CATEGORY_EXIST = 10001;  //任务分类已存在
	
	int ERROR_CATEGORY_NOT_EXIST = 10002;  //任务分类不存在

    int ERROR_USER_NOT_EXIST = 10003; //目标用户不存在
    
    int ERROR_CONTENT_NULL = 10004; //内容不能为空
    
    int ERROR_SERVICE_CATEGORY_EXIST = 10005;  //服务类型已存在
    
    int ERROR_SERVICE_CATEGORY_NULL = 10006;  //服务类型不存在
    
    int ERROR_TASK_PROCCESING = 10007;  //该单已被其他人处理，请返回主界面刷新任务
    
    int ERROR_ENABLE_FLOW = 10008;  //请启用工作流
    
    int ERROR_USER_PRIVILEGE_EXIST = 10009;  //用户已有该权限，不能重复添加!

    int ERROR_CATEGORY_NULL = 10010;  //分类不能为空！
    
    int ERROR_CANCEL_TASK = 10011;  //任务已在处理中，不能取消！

    int ERROR_CREATE_TASK_PRIVILEGE = 10012; //没有代发权限！

    int ERROR_USER_INFO = 10013; //查不到该用户信息！
    int ERROR_APP_KEY = 10014; //查不到对应的app key！
    int ERROR_DOWNLOAD = 10015; //下载错误！
    int ERROR_ORDER_ID = 10016; //不存在的orderId
    int ERROR_STATE_ID = 10017;//非法状态

    int ERROR_CREATE_USER_ACCOUNT = 10018;//创建支付账号问题

    int PAYMENT_ORDER_NOT_EXIST = 10019;//订单系统找不到订单

//  ------------------------- 修改应用开小差 -------------------------
    int ERROR_SYNC_ES_FAIL = 10020;//同步搜索引擎失败
    int ERROR_QUERY_ES_FAIL = 10021;//查询失败
    int ERROR_FLIE_EXPORT_FAIL = 10022;//文件导出失败
    int ERROR_NOT_EXIST = 10023;//对象不存在
    int ERROR_REMOTE_INVOKE_FAIL = 10024;//第三方返回失败
    int ERROR_INVALD_PARAMS = 10025;//接口参数异常

    int ERROR_USER_NOT_FOUND = 10026;
}
