package com.everhomes.techpark.punch;

import javax.validation.Valid;

import com.everhomes.rest.techpark.punch.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

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
	
	
	
	/**
	 * <b>URL: /techpark/punch/addPunchExceptionRequest</b>
	 * <p>
	 * 增加打卡异常申报
	 * </p>
	 */
	@Deprecated
	@RequestMapping("addPunchExceptionRequest")
	@RestReturn(value = String.class)
	public RestResponse addPunchExceptionRequest(
			@Valid AddPunchExceptionRequestCommand cmd) {
		punchService.createPunchExceptionRequest(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

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
	 * <b>URL: /techpark/punch/getPunchType</b>
	 * <p>
	 * 获取现在打卡类型--上班或者下班
	 * </p>
	 */
	@RequestMapping("getPunchType")
	@RestReturn(value = GetPunchTypeResponse.class)
	public RestResponse getPunchType(@Valid GetPunchTypeCommand cmd) {
		// 打卡返回打卡时间
		GetPunchTypeResponse resp = punchService.getPunchType(cmd);
		RestResponse response = new RestResponse(resp);
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
	 * <b>URL: /techpark/punch/addPunchRule</b>
	 * <p>
	 * 添加公司打卡规则
	 * </p>
	 */

	@Deprecated
	@RequestMapping("addPunchRule")
	@RestReturn(value = String.class)
	public RestResponse addPunchRule(@Valid AddPunchRuleCommand cmd) {
		punchService.createPunchRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/punch/updatePunchRule</b>
	 * <p>
	 * 修改公司打卡规则
	 * </p>
	 */

	@Deprecated
	@RequestMapping("updatePunchRule")
	@RestReturn(value = String.class)
	public RestResponse updatePunchRule(@Valid UpdatePunchRuleCommand cmd) {
		punchService.updatePunchRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/punch/deletePunchRule</b>
	 * <p>
	 * 删除公司打卡规则
	 * </p>
	 */

	@Deprecated
	@RequestMapping("deletePunchRule")
	@RestReturn(value = String.class)
	public RestResponse deletePunchRule(@Valid DeletePunchRuleCommand cmd) {
		punchService.deletePunchRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/punch/getPunchRule</b>
	 * <p>
	 * 查询公司打卡规则
	 * </p>
	 */

	@Deprecated
	@RequestMapping("getPunchRule")
	@RestReturn(value = GetPunchRuleCommandResponse.class)
	public RestResponse getPunchRule(@Valid GetPunchRuleCommand cmd) {
		GetPunchRuleCommandResponse commandResponse = punchService.getPunchRuleByCompanyId(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/punch/listPunchExceptionRequest</b>
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
    public RestResponse listPunchCount(@Valid ListPunchSupportiveAddressCommand cmd) {
        ListPunchSupportiveAddressCommandResponse res = punchService.listPunchSupportiveAddress(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
