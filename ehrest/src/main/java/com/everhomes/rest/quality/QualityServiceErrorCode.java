package com.everhomes.rest.quality;

public interface QualityServiceErrorCode {

	static final String SCOPE = "quality";

    static final int ERROR_HECHA_MEMBER_EMPTY = 10001;  //业务组核查成员为空
    static final int ERROR_TASK_NOT_EXIST = 10002;  //任务为空
    static final int ERROR_STANDARD_NOT_EXIST = 10003;  //标准为空
    static final int ERROR_CATEGORY_NOT_EXIST = 10004;  //category为空
    static final int ERROR_FACTOR_NOT_EXIST = 10005;  //factor为空
    
    static final int ATTACHMENT_TEXT = 10006;  //附件文字
    static final int ERROR_CATEGORY_HAS_STANDARD = 10007;  //类型包含active的标准
    static final int ERROR_TASK_IS_CLOSED = 10008;  //任务已关闭
    
    static final int ERROR_CREATE_EXCEL = 10009;  //生成excel信息有问题
    static final int ERROR_DOWNLOAD_EXCEL = 10010;  //下载excel信息有问题
    
    static final int ERROR_ASSIGN_TO_ONESELF = 10011;  //把任务转给自己
    static final int ERROR_TEMPLATE_NOT_EXIST = 10012;  //任务模板不存在
    static final int ERROR_TEMPLATE_CREATOR = 10013;  //任务模板创建者非当前用户

    static final int ERROR_SAMPLE_HAS_TASK = 10014;  //检查已产生关联任务 不可删除
    static final int ERROR_SAMPLE_NOT_EXIST = 10015;  //检查已删除或不存在
    static final int ERROR_SAMPLE_START = 10016;  //已经进入开始时间的检查，不可进行编辑
    static final int ERROR_SAMPLE_CANNOT_CREATE_TASK = 10017;  //检查不存在或已过截止日期
}
