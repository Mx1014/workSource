package com.everhomes.techpark.punch.admin;

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
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.PunchRuleDTO;
import com.everhomes.rest.techpark.punch.PunchRuleMapDTO;
import com.everhomes.rest.techpark.punch.admin.AddPunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.DeleteCommonCommand;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.QryPunchLocationRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesCommonCommand;
import com.everhomes.rest.techpark.punch.admin.listPunchTimeRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWiFiRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWorkdayRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchTimeRuleCommand;
import com.everhomes.techpark.punch.PunchService;
@RestDoc(value = "Punch controller", site = "ehccore")
@RestController
@RequestMapping("/punch")
public class PunchAdminController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(PunchAdminController.class);

	@Autowired
	private PunchService punchService;
	
	/**
	 * <b>URL: /punch/addPunchTimeRule</b>
	 * <p>
	 * 添加公司时间考勤规则
	 * </p>
	 */
	@RequestMapping("addPunchTimeRule")
	@RestReturn(value = String.class)
	public RestResponse addPunchTimeRule(@Valid AddPunchTimeRuleCommand cmd) {
		punchService.addPunchTimeRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	
	
	/**
	 * <b>URL: /punch/updatePunchTimeRule</b>
	 * <p>
	 * 更新公司时间考勤规则
	 * </p>
	 */
	@RequestMapping("updatePunchTimeRule")
	@RestReturn(value = String.class)
	public RestResponse updatePunchTimeRule(@Valid UpdatePunchTimeRuleCommand cmd) {
		punchService.updatePunchTimeRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /punch/deletePunchTimeRule</b>
	 * <p>
	 * 删除公司时间考勤规则
	 * </p>
	 */
	@RequestMapping("deletePunchTimeRule")
	@RestReturn(value = String.class)
	public RestResponse deletePunchTimeRule(@Valid DeleteCommonCommand cmd) {
		punchService.deletePunchTimeRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /punch/listPunchTimeRuleList</b>
	 * <p>
	 * 查询公司时间考勤规则列表
	 * </p>
	 */
	@RequestMapping("listPunchTimeRules")
	@RestReturn(value = listPunchTimeRuleListResponse.class)
	public RestResponse listPunchTimeRules(@Valid ListPunchRulesCommonCommand cmd) {
		listPunchTimeRuleListResponse resp = punchService.listPunchTimeRuleList(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	//地点考勤

	/**
	 * <b>URL: /punch/addPunchLocationRule</b>
	 * <p>
	 * 添加公司地点考勤规则
	 * </p>
	 */
	@RequestMapping("addPunchLocationRule")
	@RestReturn(value = String.class)
	public RestResponse addPunchLocationRule(@Valid PunchLocationRuleDTO cmd) {
		punchService.addPunchLocationRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	
	
	/**
	 * <b>URL: /punch/updatePunchLocationRule</b>
	 * <p>
	 * 更新公司地点考勤规则
	 * </p>
	 */
	@RequestMapping("updatePunchLocationRule")
	@RestReturn(value = String.class)
	public RestResponse updatePunchTimeRule(@Valid PunchLocationRuleDTO cmd) {
		punchService.updatePunchLocationRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /punch/deletePunchLocationRule</b>
	 * <p>
	 * 删除公司地点考勤规则
	 * </p>
	 */
	@RequestMapping("deletePunchLocationRule")
	@RestReturn(value = String.class)
	public RestResponse deletePunchLocationRule(@Valid DeleteCommonCommand cmd) {
		punchService.deletePunchLocationRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /punch/listPunchLocationRules</b>
	 * <p>
	 * 查询公司地点考勤规则列表
	 * </p>
	 */
	@RequestMapping("listPunchLocationRules")
	@RestReturn(value = QryPunchLocationRuleListResponse.class)
	public RestResponse listPunchLocationRules(@Valid ListPunchRulesCommonCommand cmd) {
		QryPunchLocationRuleListResponse resp = punchService.listPunchLocationRules(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	//wifi规则

	/**
	 * <b>URL: /punch/addPunchWiFiRule</b>
	 * <p>
	 * 添加公司WIFI考勤规则
	 * </p>
	 */
	@RequestMapping("addPunchWiFiRule")
	@RestReturn(value = String.class)
	public RestResponse addPunchWiFiRule(@Valid PunchWiFiRuleDTO cmd) {
		punchService.addPunchWiFiRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	
	
	/**
	 * <b>URL: /punch/updatePunchWiFiRule</b>
	 * <p>
	 * 更新公司WIFI考勤规则
	 * </p>
	 */
	@RequestMapping("updatePunchWiFiRule")
	@RestReturn(value = String.class)
	public RestResponse updatePunchWiFiRule(@Valid PunchWiFiRuleDTO cmd) {
		punchService.updatePunchWiFiRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /punch/deletePunchWiFiRule</b>
	 * <p>
	 * 删除公司WIFI考勤规则
	 * </p>
	 */
	@RequestMapping("deletePunchWiFiRule")
	@RestReturn(value = String.class)
	public RestResponse deletePunchWiFiRule(@Valid DeleteCommonCommand cmd) {
		punchService.deletePunchWiFiRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /punch/listPunchWiFiRules</b>
	 * <p>
	 * 查询公司WIFI考勤规则列表
	 * </p>
	 */
	@RequestMapping("listPunchWiFiRules")
	@RestReturn(value = ListPunchWiFiRuleListResponse.class)
	public RestResponse listPunchWiFiRules(@Valid ListPunchRulesCommonCommand cmd) {
		ListPunchWiFiRuleListResponse resp = punchService.listPunchWiFiRule(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

	//排班规则

	/**
	 * <b>URL: /punch/addPunchWorkdayRule</b>
	 * <p>
	 * 添加公司排班考勤规则
	 * </p>
	 */
	@RequestMapping("addPunchWorkdayRule")
	@RestReturn(value = String.class)
	public RestResponse addPunchWorkdayRule(@Valid PunchWorkdayRuleDTO cmd) {
		punchService.addPunchWorkdayRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	
	
	/**
	 * <b>URL: /punch/updatePunchWorkdayRule</b>
	 * <p>
	 * 设置公司排班考勤规则
	 * </p>
	 */
	@RequestMapping("updatePunchWorkdayRule")
	@RestReturn(value = String.class)
	public RestResponse updatePunchWorkdayRule(@Valid PunchWorkdayRuleDTO cmd) {
		punchService.updatePunchWorkdayRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /punch/deletePunchWorkdayRule</b>
	 * <p>
	 * 删除公司排班考勤规则
	 * </p>
	 */
	@RequestMapping("deletePunchWorkdayRule")
	@RestReturn(value = String.class)
	public RestResponse deletePunchWorkdayRule(@Valid DeleteCommonCommand cmd) {
		punchService.deletePunchWorkdayRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /punch/listPunchWorkdayRules</b>
	 * <p>
	 * 查询公司排班考勤规则列表
	 * </p>
	 */
	@RequestMapping("listPunchWorkdayRules")
	@RestReturn(value = ListPunchWorkdayRuleListResponse.class)
	public RestResponse listPunchWorkdayRules(@Valid ListPunchRulesCommonCommand cmd) {
		ListPunchWorkdayRuleListResponse resp = punchService.listPunchWorkdayRule(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	//总规则

	/**
	 * <b>URL: /punch/addPunchRule</b>
	 * <p>
	 * 添加公司考勤规则
	 * </p>
	 */
	@RequestMapping("addPunchRule")
	@RestReturn(value = String.class)
	public RestResponse addPunchRule(@Valid PunchRuleDTO cmd) {
		punchService.addPunchRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	
	
	/**
	 * <b>URL: /punch/updatePunchRule</b>
	 * <p>
	 * 设置公司考勤规则
	 * </p>
	 */
	@RequestMapping("updatePunchRule")
	@RestReturn(value = String.class)
	public RestResponse updatePunchRule(@Valid PunchRuleDTO cmd) {
		punchService.updatePunchRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /punch/deletePunchRule</b>
	 * <p>
	 * 删除公司考勤规则
	 * </p>
	 */
	@RequestMapping("deletePunchRule")
	@RestReturn(value = String.class)
	public RestResponse deletePunchRule(@Valid DeleteCommonCommand cmd) {
		punchService.deletePunchRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /punch/listPunchRules</b>
	 * <p>
	 * 查询公司考勤规则列表
	 * </p>
	 */
	@RequestMapping("listPunchRules")
	@RestReturn(value = ListPunchRulesResponse.class)
	public RestResponse listPunchRules(@Valid ListPunchRulesCommonCommand cmd) {
		ListPunchRulesResponse resp = punchService.listPunchRules(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

	//映射

	/**
	 * <b>URL: /punch/addPunchRuleMap</b>
	 * <p>
	 * 添加考勤规则映射
	 * </p>
	 */
	@RequestMapping("addPunchRuleMap")
	@RestReturn(value = String.class)
	public RestResponse addPunchRuleMap(@Valid PunchRuleMapDTO cmd) {
		punchService.addPunchRuleMap(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	
	 

	/**
	 * <b>URL: /punch/deletePunchRuleMap</b>
	 * <p>
	 * 删除公司考勤规则
	 * (目前只有设置没有删除)
	 * </p>
	 */
	@RequestMapping("deletePunchRuleMap")
	@RestReturn(value = String.class)
	public RestResponse deletePunchRuleMap(@Valid DeleteCommonCommand cmd) {
//		punchService.deletePunchRuleMap(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /punch/listPunchRuleMaps</b>
	 * <p>
	 * 查询公司考勤规则列表
	 * </p>
	 */
	@RequestMapping("listPunchRuleMaps")
	@RestReturn(value = ListPunchRuleMapsResponse.class)
	public RestResponse listPunchRuleMaps(@Valid ListPunchRuleMapsCommand cmd) {
		ListPunchRuleMapsResponse resp = punchService.listPunchRuleMaps(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
}
