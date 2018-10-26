package com.everhomes.techpark.punch;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.techpark.punch.*;
import com.everhomes.rest.techpark.punch.admin.ListApprovalCategoriesResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <ul>
 * 打卡管理：
 * <li>先调用查询是否可以打卡的接口，返回给客户端</li>
 * <li>可以打卡后，客户端调用打卡接口，进行打卡，数据库新增打卡记录</li>
 * <li>返回打卡记录list</li>
 * </ul>
 */
@RestDoc(value = "Punch controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/punch")
public class PunchController extends ControllerBase {

	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchController.class);

	@Autowired
	private PunchService punchService;
	
	
	
//	/**
//	 * <b>URL: /techpark/punch/addPunchExceptionRequest</b>
//	 * <p>
//	 * 增加打卡异常申报
//	 * </p>
//	 */
//	@Deprecated
//	@RequestMapping("addPunchExceptionRequest")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchExceptionRequest(
//			@Valid AddPunchExceptionRequestCommand cmd) {
//		punchService.createPunchExceptionRequest(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}

	/**
	 * <b>URL: /techpark/punch/punchClock</b>
	 * <p>
	 * 根据请求 user和company的ID完成打卡功能
	 * </p>
	 */
	@RequestMapping("punchClock")
	@RestReturn(value = String.class)
	public RestResponse punchClock(@Valid PunchClockCommand cmd) {
		// 打卡返回打卡时间
		RestResponse response = new RestResponse(
				punchService.createPunchLog(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/punch/getPunchDayStatus</b>
	 * <p>
	 * 获取获取当前/某日 的打卡状态(以及将要打卡的状态)
	 * </p>
	 */
	@RequestMapping("getPunchDayStatus")
	@RestReturn(value = GetPunchDayStatusResponse.class)
	public RestResponse getPunchDayStatus(@Valid GetPunchDayStatusCommand cmd) {
		// 打卡返回打卡时间
		GetPunchDayStatusResponse resp = punchService.getPunchDayStatus(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/punch/listPunchMonthStatus</b>
	 * <p>
	 * 查询某月的打卡日历展示信息
	 * </p>
	 */
	@RequestMapping("listPunchMonthStatus")
	@RestReturn(value = ListPunchMonthStatusResponse.class)
	public RestResponse listPunchMonthStatus(@Valid ListPunchMonthStatusCommand cmd) {
		// 打卡返回打卡时间
		ListPunchMonthStatusResponse resp = punchService.listPunchMonthStatus(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}



	/**
	 * <b>URL: /techpark/punch/checkAbnormalStatus</b>
	 * <p>
	 * 查询企业是否开启了异常审批
	 * </p>
	 */
	@RequestMapping("checkAbnormalStatus")
	@RestReturn(value = CheckAbnormalStatusResponse.class)
	public RestResponse checkAbnormalStatus(@Valid CheckPunchAdminCommand cmd) {
		// 打卡返回打卡时间
		CheckAbnormalStatusResponse res = punchService.checkAbnormalStatus(cmd);
		RestResponse response = new RestResponse(res); 
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /techpark/punch/checkPunchAdmin</b>
	 * <p>
	 * 查询当前用户是否为该公司考勤的管理员(目前是企业管理员)
	 * </p>
	 */
	@RequestMapping("checkPunchAdmin")
	@RestReturn(value = CheckPunchAdminResponse.class)
	public RestResponse checkPunchAdmin(@Valid CheckPunchAdminCommand cmd) {
		// 打卡返回打卡时间
		CheckPunchAdminResponse res = punchService.checkPunchAdmin(cmd);
		RestResponse response = new RestResponse(res); 
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/punch/getPunchNewException</b>
	 * <p>
	 * 查询某人在本月/打卡第一天 到昨天是否有异常
	 * </p>
	 */
	@RequestMapping("getPunchNewException")
	@RestReturn(value = GetPunchNewExceptionCommandResponse.class)
	public RestResponse getPunchNewException(@Valid GetPunchNewExceptionCommand cmd) {
		// 打卡返回打卡时间
		GetPunchNewExceptionCommandResponse res = punchService.getPunchNewException(cmd);
		RestResponse response = new RestResponse(res); 
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	
	/**
	 * <b>URL: /techpark/punch/listYearPunchLogs</b>
	 * <p>
	 * 根据请求 companyid和日期 取一年的打卡记录
	 * </p>
	 */
	@RequestMapping("listYearPunchLogs")
	@RestReturn(value = ListYearPunchLogsCommandResponse.class)
	public RestResponse listYearPunchLogs(@Valid ListYearPunchLogsCommand cmd) {

		RestResponse res = new RestResponse();
		ListYearPunchLogsCommandResponse punchLogsYearListResponse = punchService
				.getlistPunchLogs(cmd);
		res.setErrorCode(ErrorCodes.SUCCESS);
		res.setErrorDescription("OK");
		res.setResponseObject(punchLogsYearListResponse);
		return res;
	}
	
	/**
	 * <b>URL: /techpark/punch/listMonthPunchLogs</b>
	 * <p>
	 * 根据请求 companyid和日期 取一个月的打卡记录
	 * </p>
	 */
	@RequestMapping("listMonthPunchLogs")
	@RestReturn(value = ListMonthPunchLogsCommandResponse.class)
	public RestResponse listMonthPunchLogs(@Valid ListMonthPunchLogsCommand cmd) {

		RestResponse res = new RestResponse();
		ListMonthPunchLogsCommandResponse punchLogsYearListResponse = punchService
				.listMonthPunchLogs(cmd);
		res.setErrorCode(ErrorCodes.SUCCESS);
		res.setErrorDescription("OK");
		res.setResponseObject(punchLogsYearListResponse);
		return res;
	}
	
	/**
	 * <b>URL: /techpark/punch/getDayPunchLogs</b>
	 * <p>
	 * 根据请求 companyid和日期 取一年的打卡记录
	 * </p>
	 */
	@RequestMapping("getDayPunchLogs")
	@RestReturn(value = PunchLogsDay.class)
	public RestResponse getDayPunchLogs(@Valid GetDayPunchLogsCommand cmd) {

		RestResponse res = new RestResponse();
		PunchLogsDay pdl = punchService.getDayPunchLogs(cmd);
		res.setErrorCode(ErrorCodes.SUCCESS);
		res.setErrorDescription("OK");
		res.setResponseObject(pdl);
		return res;
	}


	/**
	 * <b>URL: /techpark/punch/listApprovalCategories</b>
	 * <p>获取请假类型</p>
	 */
	@RequestMapping("listApprovalCategories")
	@RestReturn(value = ListApprovalCategoriesResponse.class)
	public RestResponse listApprovalCategories(ListApprovalCategoriesCommand cmd, HttpServletRequest request) {
		RestResponse res = new RestResponse(punchService.listApprovalCategories(cmd, request));
		res.setErrorCode(ErrorCodes.SUCCESS);
		res.setErrorDescription("OK");
		return res;
	}

//	/**
//	 * <b>URL: /techpark/punch/addPunchRule</b>
//	 * <p>
//	 * 添加公司打卡规则
//	 * </p>
//	 */
//
//	@Deprecated
//	@RequestMapping("addPunchRule")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchRule(@Valid AddPunchRuleCommand cmd) {
//		punchService.createPunchRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
//	/**
//	 * <b>URL: /techpark/punch/updatePunchRule</b>
//	 * <p>
//	 * 修改公司打卡规则
//	 * </p>
//	 */
//
//	@Deprecated
//	@RequestMapping("updatePunchRule")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchRule(@Valid UpdatePunchRuleCommand cmd) {
//		punchService.updatePunchRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /techpark/punch/deletePunchRule</b>
//	 * <p>
//	 * 删除公司打卡规则
//	 * </p>
//	 */
//
//	@Deprecated
//	@RequestMapping("deletePunchRule")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchRule(@Valid DeletePunchRuleCommand cmd) {
//		punchService.deletePunchRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /techpark/punch/getPunchRule</b>
//	 * <p>
//	 * 查询公司打卡规则
//	 * </p>
//	 */
//
//	@Deprecated
//	@RequestMapping("getPunchRule")
//	@RestReturn(value = GetPunchRuleCommandResponse.class)
//	public RestResponse getPunchRule(@Valid GetPunchRuleCommand cmd) {
//		GetPunchRuleCommandResponse commandResponse = punchService.getPunchRuleByCompanyId(cmd);
//		RestResponse response = new RestResponse(commandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /techpark/punch/listPunchExceptionRequestBetweenBeginAndEndTime</b>
	 * <p>
	 * 查询公司打卡异常申请列表
	 * </p>
	 */
	@RequestMapping("listPunchExceptionRequest")
	@RestReturn(value = ListPunchExceptionRequestCommandResponse.class)
	public RestResponse listPunchExceptionRequest(@Valid ListPunchExceptionRequestCommand cmd) {
		ListPunchExceptionRequestCommandResponse commandResponse = punchService.listExceptionRequests(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /techpark/punch/listPunchExceptionApproval</b>
	 * <p>
	 * 查询公司打卡异常审批-某人某日的申请的审批
	 * </p>
	 */
	@RequestMapping("listPunchExceptionApproval")
	@RestReturn(value = ListPunchExceptionRequestCommandResponse.class)
	public RestResponse listPunchExceptionApproval(@Valid ListPunchExceptionApprovalCommand cmd) {
		ListPunchExceptionRequestCommandResponse commandResponse = punchService.listExceptionApprovals(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/punch/PunchExceptionApproval</b>
	 * <p>
	 *审批异常申报
	 * </p>
	 */
	@RequestMapping("approvalPunchException")
	@RestReturn(value = String.class)
	public RestResponse approvalPunchException(@Valid ApprovalPunchExceptionCommand cmd) {
		punchService.punchExceptionApproval(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
 
//	/**
//	 * <b>URL: /techpark/punch/listPunchStatistics</b>
//	 * <p>
//	 * 查询公司打卡的统计结果
//	 * </p>
//	 */
//	@RequestMapping("listPunchStatistics")
//	@RestReturn(value = ListPunchStatisticsCommandResponse.class)
//	public RestResponse listPunchStatistics(@Valid ListPunchStatisticsCommand cmd) {
//		ListPunchStatisticsCommandResponse commandResponse = punchService.listPunchStatistics(cmd);
//		RestResponse response = new RestResponse(commandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /techpark/punch/listPunchCount</b>
	 * <p>
	 * 查询公司考勤的统计结果
	 * </p>
	 */
	@RequestMapping("listPunchCount")
	@RestReturn(value = ListPunchCountCommandResponse.class)
	public RestResponse listPunchCount(@Valid ListPunchCountCommand cmd) {
		Long ownerId = punchService.getTopEnterpriseId(cmd.getOwnerId());
		punchService.checkAppPrivilege(ownerId,cmd.getOwnerId(), PrivilegeConstants.PUNCH_STATISTIC_QUERY);
		ListPunchCountCommandResponse commandResponse = punchService.listPunchCount(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

//	/**
//	 * <b>URL: /techpark/punch/exportPunchStatistics</b>
//	 * <p>
//	 * 导出公司打卡的统计结果
//	 * </p>
//	 */
//	@RequestMapping("exportPunchStatistics")
//	public  HttpServletResponse exportPunchStatistics(@Valid ExportPunchStatisticsCommand cmd,HttpServletResponse response ) {
//		HttpServletResponse commandResponse = punchService.exportPunchDetails(cmd, response );
////		RestResponse response = new RestResponse(commandResponse);
////		response.setErrorCode(ErrorCodes.SUCCESS);
////		response.setErrorDescription("OK");
//		return commandResponse;
//	}

    /**
     * <b>URL: /techpark/punch/listPunchSupportiveAddress</b>
     * <p>
     * 获取用户对应的打卡wifi mac地址及经纬度信息
     * </p>
     */
    @RequestMapping("listPunchSupportiveAddress")
    @RestReturn(value = ListPunchSupportiveAddressCommandResponse.class)
    public RestResponse listPunchSupportiveAddress(@Valid ListPunchSupportiveAddressCommand cmd) {
        ListPunchSupportiveAddressCommandResponse res = punchService.listPunchSupportiveAddress(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /techpark/punch/addPunchPoints</b>
     * <p>
     * 上报经纬度坐标信息
     * </p>
     */
    @RequestMapping("addPunchPoints")
    @RestReturn(value = String.class)
    public RestResponse addPunchPoints(@Valid AddPunchPointsCommand cmd) {
        punchService.addPunchPoints(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /techpark/punch/addPunchWifis</b>
     * <p>
     * 上报wifi信息
     * </p>
     */
    @RequestMapping("addPunchWifis")
    @RestReturn(value = String.class)
    public RestResponse addPunchWifis(@Valid AddPunchWifisCommand cmd) {
        punchService.addPunchWifis(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

     
	/**
	 * <p>查看月报列表</p>
	 * <b>URL: /techpark/punch/listPunchMonthReports</b>
	 */
	@RequestMapping("listPunchMonthReports")
	@RestReturn(ListPunchMonthReportsResponse.class)
	public RestResponse listPunchMonthReports(ListPunchMonthReportsCommand cmd){
		return new RestResponse(punchService.listPunchMonthReports(cmd));
	}

	/**
	 * <p>刷新月报</p>
	 * <b>URL: /techpark/punch/updateMonthReport</b>
	 */
	@RequestMapping("updateMonthReport")
	@RestReturn(String.class)
	public RestResponse updateMonthReport(UpdateMonthReportCommand cmd){
		punchService.updateMonthReport(cmd);
		return new RestResponse();
	}

	/**
	 * <p>获取月报进度</p>
	 * <b>URL: /techpark/punch/getMonthReportProcess</b>
	 */
	@RequestMapping("getMonthReportProcess")
	@RestReturn(GetMonthReportProcessResponse.class)
	public RestResponse getMonthReportProcess(GetMonthReportProcessCommand cmd){
		return new RestResponse(punchService.getMonthReportProcess(cmd));
	}

	/**
	 * <p>归档月报</p>
	 * <b>URL: /techpark/punch/fileMonthReport</b>
	 */
	@RequestMapping("fileMonthReport")
	@RestReturn(String.class)
	public RestResponse fileMonthReport(FileMonthReportCommand cmd){
		punchService.fileMonthReport(cmd);
		return new RestResponse();
	}
	/**
	 * <p>刷新月报-每月1日的模拟</p>
	 * <b>URL: /techpark/punch/refreshMonthReport</b>
	 */
	@RequestMapping("refreshMonthReport")
	@RestReturn(String.class)
	public RestResponse refreshMonthReport(RefreshMonthReportCommand cmd){
		punchService.refreshMonthReport(cmd.getMonth());
		return new RestResponse();
	}
	/**
	 * <p>获取加班文案信息</p>
	 * <b>URL: /techpark/punch/getOvertimeInfo</b>
	 */
	@RequestMapping("getOvertimeInfo")
	@RestReturn(GetOvertimeInfoResponse.class)
	public RestResponse getOvertimeInfo(GetOvertimeInfoCommand cmd){
		GetOvertimeInfoResponse resp = punchService.getOvertimeInfo(cmd);
		return new RestResponse(resp);
	}

	/**
	 * <p>获取部门成员的考勤状态按日的统计数据</p>
	 * <b>URL: /techpark/punch/dailyStatisticsByDepartment</b>
	 */
	@RequestMapping("dailyStatisticsByDepartment")
	@RestReturn(PunchDailyStatisticsByDepartmentResponse.class)
	public RestResponse dailyStatisticsByDepartment(PunchDailyStatisticsByDepartmentCommand cmd) {
		RestResponse response = new RestResponse(punchService.dailyStatisticsByDepartment(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取部门成员的考勤状态按月的统计数据</p>
	 * <b>URL: /techpark/punch/monthlyStatisticsByDepartment</b>
	 */
	@RequestMapping("monthlyStatisticsByDepartment")
	@RestReturn(PunchMonthlyStatisticsByDepartmentResponse.class)
	public RestResponse monthlyStatisticsByDepartment(PunchMonthlyStatisticsByDepartmentCommand cmd) {
		RestResponse response = new RestResponse(punchService.monthlyStatisticsByDepartment(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取部门某日或某月的成员列表</p>
	 * <b>URL: /techpark/punch/listMembersOfDepartment</b>
	 */
	@RequestMapping("listMembersOfDepartment")
	@RestReturn(ListPunchMembersResponse.class)
	public RestResponse listMembersOfDepartment(ListPunchMembersCommand cmd) {
		RestResponse response = new RestResponse(punchService.listMembersOfDepartment(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取部门特定考勤状态的某日或某月的成员列表（出勤明细）</p>
	 * <b>URL: /techpark/punch/listMembersOfAPunchStatus</b>
	 */
	@RequestMapping("listMembersOfAPunchStatus")
	@RestReturn(ListPunchStatusMembersResponse.class)
	public RestResponse listMembersOfAPunchStatus(ListPunchStatusMembersCommand cmd) {
		RestResponse response = new RestResponse(punchService.listMembersOfAPunchStatus(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取部门特定考勤申请类型的某日或某月的成员类表（申请明细）</p>
	 * <b>URL: /techpark/punch/listMembersOfAPunchExceptionRequest</b>
	 */
	@RequestMapping("listMembersOfAPunchExceptionRequest")
	@RestReturn(ListPunchExceptionRequestMembersResponse.class)
	public RestResponse listMembersOfAPunchExceptionRequest(ListPunchExceptionRequestMembersCommand cmd) {
		RestResponse response = new RestResponse(punchService.listMembersOfAPunchExceptionRequest(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取用户某月特定考勤状态的日期明细</p>
	 * <b>URL: /techpark/punch/listItemDetailsOfAPunchStatus</b>
	 */
	@RequestMapping("listItemDetailsOfAPunchStatus")
	@RestReturn(ListPunchStatusItemDetailResponse.class)
	public RestResponse listItemDetailsOfAPunchStatus(ListPunchStatusItemDetailCommand cmd) {
		RestResponse response = new RestResponse(punchService.listItemDetailsOfAPunchStatus(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取用户某月特定考勤异常申请类型的申请明细</p>
	 * <b>URL: /techpark/punch/listItemDetailsOfAPunchExceptionRequest</b>
	 */
	@RequestMapping("listItemDetailsOfAPunchExceptionRequest")
	@RestReturn(ListPunchExceptionRequestItemDetailResponse.class)
	public RestResponse listItemDetailsOfAPunchExceptionRequest(ListPunchExceptionRequestItemDetailCommand cmd) {
		RestResponse response = new RestResponse(punchService.listItemDetailsOfAPunchExceptionRequest(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取员工个人的考勤状态按月的统计数据</p>
	 * <b>URL: /techpark/punch/monthlyStatisticsByMember</b>
	 */
	@RequestMapping("monthlyStatisticsByMember")
	@RestReturn(PunchMonthlyStatisticsByMemberResponse.class)
	public RestResponse monthlyStatisticsByMember(PunchMonthlyStatisticsByMemberCommand cmd) {
		RestResponse response = new RestResponse(punchService.monthlyStatisticsByMember(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>校验用户的统计权限</p>
	 * <b>URL: /techpark/punch/checkUserStatisticPrivilege</b>
	 */
	@RequestMapping("checkUserStatisticPrivilege")
	@RestReturn(CheckUserStatisticPrivilegeResponse.class)
	public RestResponse checkUserStatisticPrivilege(CheckUserStatisticPrivilegeCommand cmd) {
		RestResponse response = new RestResponse(punchService.checkUserStatisticPrivilege(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>获取规则文案URL</p>
	 * <b>URL: /techpark/punch/getAdjustRuleUrl</b>
	 */
	@RequestMapping("getAdjustRuleUrl")
	@RestReturn(GetAdjustRuleUrlResponse.class)
	public RestResponse getAdjustRuleUrl(HttpServletRequest request){
		return new RestResponse(new GetAdjustRuleUrlResponse(punchService.getAdjustRuleUrl(request)));
	}

	/**
	 * <p>获取规则文案URL</p>
	 * <b>URL: /techpark/punch/getUserPunchRuleInfoUrl</b>
	 */
	@RequestMapping("getUserPunchRuleInfoUrl")
	@RestReturn(GetUserPunchRuleInfoUrlResponse.class)
	public RestResponse getUserPunchRuleInfoUrl(GetUserPunchRuleInfoUrlCommand cmd, HttpServletRequest request){
		GetUserPunchRuleInfoUrlResponse resp = punchService.getUserPunchRuleInfoUrl(cmd, request);
		return new RestResponse(resp);
	}
	

	/**
	 * <b>URL: /techpark/punch/thirdPartPunchClock</b>
	 * <p>
	 * 第三方调用的打卡服务
	 * </p>
	 */
	@RequestMapping("thirdPartPunchClock")
	@RestReturn(value = String.class)
	public RestResponse thirdPartPunchClock(@Valid ThirdPartPunchClockCommand cmd) {
		// 打卡返回打卡时间
		RestResponse response = new RestResponse(punchService.thirdPartPunchClock(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/punch/goOutPunchClock</b>
	 * <p>
	 * 外出打卡
	 * </p>
	 */
	@RequestMapping("goOutPunchClock")
	@RestReturn(GoOutPunchLogDTO.class)
	public RestResponse goOutPunchClock(@Valid GoOutPunchClockCommand cmd) {
		// 打卡返回打卡时间
		RestResponse response = new RestResponse(
				punchService.goOutPunchClock(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/punch/updateGoOutPunchLog</b>
	 * <p>
	 * 更新外出打卡备注
	 * </p>
	 */
	@RequestMapping("updateGoOutPunchLog")
	@RestReturn(GoOutPunchLogDTO.class)
	public RestResponse updateGoOutPunchLog(@Valid UpdateGoOutPunchLogCommand cmd) {
		// 打卡返回打卡时间
		RestResponse response = new RestResponse(
				punchService.updateGoOutPunchLog(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/punch/getGoOutPunchLog</b>
	 * <p>
	 * 查看外出打卡详情
	 * </p>
	 */
	@RequestMapping("getGoOutPunchLog")
	@RestReturn(GoOutPunchLogDTO.class)
	public RestResponse getGoOutPunchLog(@Valid GetGoOutPunchLogCommand cmd) {
		// 打卡返回打卡时间
		RestResponse response = new RestResponse(
				punchService.getGoOutPunchLog(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}