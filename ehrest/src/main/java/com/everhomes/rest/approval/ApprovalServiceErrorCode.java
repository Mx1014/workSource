package com.everhomes.rest.approval;

public interface ApprovalServiceErrorCode {
	String SCOPE = "approval";
	
	int ABSENCE_EMPTY_CATEGORY = 10000;  //请选择请假类型
	int ABSENCE_EMPTY_REASON = 10001;  //请输入请假理由
	int ABSENCE_EMPTY_TIME = 10002;  //请选择请假时间
	int ABSENCE_FROM_TIME_MUST_LT_END_TIME = 10003;  //请假开始时间必须小于请假结束时间
	int ABSENCE_TIME_CONTAINS_REQUESTED_TIME = 10004;  //所选时间包含已请假时间，请重新选择
	int ABSENCE_NOT_WORK_TIME = 10005;  //请假时间是非工作时间，请重新选择
	int ABSENCE_TIME_TOTAL_EQUEAL_ZERO = 10006;  //请假时长为0
	int EXCEPTION_EMPTY_REASON = 10007;  //请输入申请理由
	
	int CATEGORY_EMPTY_NAME = 10008;  //类型名称不能为空
	int CATEGORY_EXIST_NAME = 10009;  //类型名称已存在
	
	int APPROVAL_FLOW_EXIST_APPROVAL_RULE_WHEN_DELETE = 10010;  //该审批人设置关联了审批规则，请先删除对应的审批规则，再删除该审批人设置
	int APPROVAL_FLOW_EMPTY_NAME = 10011;  //审批名称不能为空
	int APPROVAL_FLOW_NAME_LENGTH_GREATER_EIGHT = 10012;  //审批名称超过8字
	int APPROVAL_FLOW_EXIST_NAME = 10013;  //审批名称已存在
	
	int APPROVAL_RULE_EMPTY_NAME = 10014;  //审批规则名称不能为空
	int APPROVAL_RULE_NAME_LENGTH_GREATER_EIGHT = 10015;  //审批规则名称超过8字
	int APPROVAL_RULE_EXIST_NAME = 10016;  //审批规则名称已存在
	int APPROVAL_RULE_EMPTY_ABSENCE = 10017;  //请假审批不能选择无
	int APPROVAL_RULE_EMPTH_EXCEPTION = 10018;  //忘打卡审批不能为无
	
	int APPROVE_OR_REJECT_EMPTY_REASON = 10019;  //请输入驳回理由
	int APPROVE_OR_REJECT_EMPTY_REQUEST = 10020;  //请选择需要审批的申请单

	int CATEGORY_NAME_LENGTH_GREATER_EIGHT = 10021;  //类型名称不能超过8字
	
	int EFFECTIVE_DATE_HAS_REQUEST = 10022;  //所选日期已提交过加班申请，请重新选择
	
	int APPROVAL_LEVEL_APPROVED = 10023;  //申请单已经被审批
	
}
