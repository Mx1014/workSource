package com.everhomes.techpark.punch;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.group.GroupDTO;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserContext;

/**
 * <ul>
 * 打卡管理：
 * <li>先调用查询是否可以打卡的接口，返回给客户端</li>
 * <li>可以打卡后，客户端调用打卡接口，进行打卡，数据库新增打卡记录</li>
 * <li>返回打卡记录list</li>
 * </ul>
 */
@RestDoc(value = "Punch controller", site = "ehccore")
@RestController
@RequestMapping("/techpark/punch")
public class PunchController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchController.class);

	@Autowired
	private PunchService punchService;
	
	
	
	/**
	 * <b>URL: /techpark/punch/punchExceptionRequest</b>
	 * <p>
	 * 增加打卡异常申报
	 * </p>
	 */
	@RequestMapping("punchExceptionRequest")
	@RestReturn(value = String.class)
	public RestResponse punchExceptionRequest(
			@Valid PunchExceptionRequestCommand cmd) {
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
	 * <b>URL: /techpark/punch/listYearPunchLogs</b>
	 * <p>
	 * 根据请求 companyid和日期 取一年的打卡记录
	 * </p>
	 */
	@RequestMapping("listYearPunchLogs")
	@RestReturn(value = PunchLogsYearListResponse.class, collection = true)
	public RestResponse listPunchLogs(@Valid ListPunchLogsCommand cmd) {

		RestResponse res = new RestResponse();
		PunchLogsYearListResponse punchLogsYearListResponse = punchService
				.getlistPunchLogs(cmd);
		res.setErrorCode(ErrorCodes.SUCCESS);
		res.setErrorDescription("OK");
		res.setResponseObject(punchLogsYearListResponse);
		return res;
	}
	
	/**
	 * <b>URL: /techpark/punch/addPunchRule</b>
	 * <p>
	 * 添加公司打卡规则
	 * </p>
	 */
	@RequestMapping("addPunchRule")
	@RestReturn(value = String.class)
	public RestResponse addPunchRule(@Valid CreatePunchRuleCommand cmd) {
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
	@RequestMapping("deletePunchRule")
	@RestReturn(value = String.class)
	public RestResponse deletePunchRule(@Valid PunchCompanyIdCommand cmd) {
		punchService.deletePunchRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /techpark/punch/getPunchRule</b>
	 * <p>
	 * 查询
	 * </p>
	 */
	@RequestMapping("getPunchRule")
	@RestReturn(value = PunchRuleResponse.class)
	public RestResponse getPunchRule(@Valid PunchCompanyIdCommand cmd) {
		PunchRuleResponse commandResponse = punchService.getPunchRuleByCompanyId(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
