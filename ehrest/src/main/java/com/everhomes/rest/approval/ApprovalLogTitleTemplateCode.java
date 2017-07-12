package com.everhomes.rest.approval;

public interface ApprovalLogTitleTemplateCode {
	String SCOPE = "approval.title";
	
	public int WAITING_FOR_APPROVING = 1; // ${nickName}提交了${approvalType}申请
	public int APPROVING_FLOW = 2; // 等待${nickNames}审批
	
	public int OVERTIME_TITLE = 3; //'${nickName}申请${date}加班${hour}小时'
	public int ABSENCE_TITLE = 4; //'${nickName}申请${category}(共${day}${hour}${min})\n${beginDate}到${endDate}'
	public int EXCEPTION_TITLE = 5; //'${nickName}对${date}提交异常申请'
	/**'${date}加班${hour}小时\n打卡记录:${punchLog}'	*/
	public int OVERTIME_MAIN_TITLE = 6; //
	/**'${category}(共${day}${hour}${min})\n${beginDate}到${endDate}'*/
	public int ABSENCE_MAIN_TITLE = 7; //
	/**'${date}\n打卡记录:${punchLog}\n打卡状态:${punchStatus}'*/
	public int EXCEPTION_MAIN_TITLE = 8; //'${date}\n打卡记录:${punchLog}\n打卡状态:${punchStatus}'
	
	
}
