package com.everhomes.techpark.punch.admin;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.techpark.punch.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.ListPunchCountCommand;
import com.everhomes.rest.techpark.punch.ListPunchCountCommandResponse;
import com.everhomes.rest.techpark.punch.PunchRuleDTO;
import com.everhomes.rest.techpark.punch.PunchRuleMapDTO;
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
	 * <b>URL: /punch/addPunchPoint</b>
	 * <p>
	 * 给对应的某个公司/人增加考勤点
	 * </p>
	 */
	@RequestMapping("addPunchPoint")
	@RestReturn(value = String.class)
	public RestResponse addPunchPoint(@Valid AddPunchPointCommand cmd) {
		punchService.addPunchPoint(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	
	
	/**
	 * <b>URL: /punch/updatePunchPoint</b>
	 * <p>
	 * 更新考勤点
	 * </p>
	 */
	@RequestMapping("updatePunchPoint")
	@RestReturn(value = String.class)
	public RestResponse updatePunchPoint(@Valid UpdatePunchPointCommand cmd) {
		punchService.updatePunchPoint(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	

	/**
	 * <b>URL: /punch/listPunchPoints</b>
	 * <p>
	 * 查询公司地点考勤规则列表
	 * </p>
	 */
	@RequestMapping("listPunchPoints")
	@RestReturn(value = ListPunchPointsResponse.class)
	public RestResponse listPunchPoints(@Valid ListPunchPointsCommand cmd) {
		ListPunchPointsResponse resp = punchService.listPunchPoints(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /punch/deletePunchPoint</b>
	 * <p>
	 * 删除公司考勤地点
	 * </p>
	 */
	@RequestMapping("deletePunchPoint")
	@RestReturn(value = String.class)
	public RestResponse deletePunchPoint(@Valid DeleteCommonCommand cmd) {
		punchService.deletePunchPoint(cmd);
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
	 * <b>URL: /punch/addPunchWiFi</b>
	 * <p>
	 * 添加target对象的WIFI考勤点
	 * </p>
	 */
	@RequestMapping("addPunchWiFi")
	@RestReturn(value = String.class)
	public RestResponse addPunchWiFi(@Valid AddPunchWiFiCommand cmd) {
		punchService.addPunchWiFi(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}	
	
	/**
	 * <b>URL: /punch/updatePunchWiFi</b>
	 * <p>
	 * 修改target对象的WIFI考勤点
	 * </p>
	 */
	@RequestMapping("updatePunchWiFi")
	@RestReturn(value = String.class)
	public RestResponse updatePunchWiFi(@Valid PunchWiFiDTO cmd) {
		punchService.updatePunchWiFi(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /punch/deletePunchWiFi</b>
	 * <p>
	 * 删除target对象的WIFI考勤点
	 * </p>
	 */
	@RequestMapping("deletePunchWiFi")
	@RestReturn(value = String.class)
	public RestResponse deletePunchWiFi(@Valid PunchWiFiDTO cmd) {
		punchService.deletePunchWiFi(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

	/**
	 * <b>URL: /punch/listPunchWiFis</b>
	 * <p>
	 * 查询公司WIFI考勤规则列表
	 * </p>
	 */
	@RequestMapping("listPunchWiFis")
	@RestReturn(value = ListPunchWiFisResponse.class)
	public RestResponse listPunchWiFis(@Valid ListPunchRulesCommonCommand cmd) {
		ListPunchWiFisResponse resp = punchService.listPunchWiFis(cmd);
		RestResponse response = new RestResponse(resp);
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
	 * 
	 * 删除公司WIFI考勤规则
	 * </p>
	 */
	@Deprecated
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
	 * <b>URL: /punch/listPunchScheduling</b>
	 * <p>
	 * 查询某个月的班次
	 * </p>
	 */
	@RequestMapping("listPunchScheduling")
	@RestReturn(value = ListPunchSchedulingMonthResponse.class)
	public RestResponse listPunchScheduling(@Valid ListPunchSchedulingMonthCommand cmd) {
		ListPunchSchedulingMonthResponse resp = punchService.listPunchScheduling(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /punch/exportPunchScheduling</b>
	 * <p>
	 * 导出公司某个月的班次
	 * </p>
	 */
	@RequestMapping("exportPunchScheduling")
	public  HttpServletResponse exportPunchScheduling(@Valid ListPunchSchedulingMonthCommand cmd,HttpServletResponse response ) {
		HttpServletResponse commandResponse = punchService.exportPunchScheduling(cmd, response );
//		RestResponse response = new RestResponse(commandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
		return commandResponse;
	}

    /**
     * <b>URL: /punch/importPunchScheduling</b>
     * <p>导入某个月的班次</p>
     */
    @RequestMapping("importPunchScheduling")
    @RestReturn(value = String.class)
    public RestResponse importPunchScheduling(@Valid ListPunchRulesCommonCommand cmd , @RequestParam(value = "attachment") MultipartFile[] files) {
    	punchService.importPunchScheduling(cmd , files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /punch/testimportPunchScheduling</b>
     * <p>导入某个月的班次</p>
     */
    @RequestMapping("testimportPunchScheduling")
    @RestReturn(value = String.class)
    public RestResponse testimportPunchScheduling(@Valid ListPunchRulesCommonCommand cmd , @RequestParam(value = "_attachment_file") MultipartFile[] files) {
    	punchService.importPunchScheduling(cmd , files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /punch/testimportPunchLogs</b>
     * <p>给登录用户通过excel导入打卡记录--测试用</p>
     */
    @RequestMapping("testimportPunchLogs")
    @RestReturn(value = String.class)
    public RestResponse testimportPunchLogs(  @RequestParam(value = "_attachment_file") MultipartFile[] files) {
    	punchService.importPunchLogs(files);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	/**
	 * <b>URL: /punch/updatePunchSchedulings</b>
	 * <p>
	 * 更新某个月的班次
	 * </p>
	 */
	@RequestMapping("updatePunchSchedulings")
	@RestReturn(value = String.class)
	public RestResponse updatePunchSchedulings(@Valid UpdatePunchSchedulingMonthCommand cmd) {
		punchService.updatePunchSchedulings(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	
	
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
	 * <b>URL: /punch/addPunchRuleMap</b>
	 * <p>
	 * 更新考勤规则映射 -特殊个人设置
	 * </p>
	 */
	@RequestMapping("updatePunchRuleMap")
	@RestReturn(value = String.class)
	public RestResponse updatePunchRuleMap(@Valid PunchRuleMapDTO cmd) {
		punchService.updatePunchRuleMap(cmd);
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
	public RestResponse deletePunchRuleMap(@Valid DeletePunchRuleMapCommand cmd) {
		punchService.deletePunchRuleMap(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /punch/listPunchRuleMaps</b>
	 * <p>
	 * 查询公司考勤规则列表
	 * 列出设置了特殊规则的人
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
	
	
	//统计
	
	/**
	 * <b>URL: punch/listPunchStatistics</b>
	 * <p>
	 * 查询公司考勤的统计结果
	 * </p>
	 */
	@RequestMapping("listPunchStatistics")
	@RestReturn(value = ListPunchCountCommandResponse.class)
	public RestResponse listPunchCount(@Valid ListPunchCountCommand cmd) {
		ListPunchCountCommandResponse commandResponse = punchService.listPunchCount(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: punch/exportPunchStatistics</b>
	 * <p>
	 * 导出公司打卡的统计结果
	 * </p>
	 */
	@RequestMapping("exportPunchStatistics")
	public  RestResponse exportPunchStatistics(@Valid ListPunchCountCommand cmd,HttpServletResponse response ) {
		HttpServletResponse commandResponse = punchService.exportPunchStatistics(cmd, response ); 
		return new RestResponse();
	}
	/**
	 * <b>URL: punch/listPunchMonthLogs</b>
	 * <p>
	 * 查询公司某月的考勤明细-具体到每人每天
	 * </p>
	 */
	@RequestMapping("listPunchMonthLogs")
	@RestReturn(value = ListPunchMonthLogsResponse.class)
	public RestResponse listPunchMonthLogs(@Valid ListPunchMonthLogsCommand cmd) {
		ListPunchMonthLogsResponse commandResponse = punchService.listPunchMonthLogs(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	

	/**
	 * <b>URL: punch/listPunchDetails</b>
	 * <p>
	 * 查询 打卡详情
	 * </p>
	 */
	@RequestMapping("listPunchDetails")
	@RestReturn(value = ListPunchDetailsResponse.class)
	public RestResponse listPunchDetails(@Valid ListPunchDetailsCommand cmd) {
		ListPunchDetailsResponse commandResponse = punchService.listPunchDetails(cmd);
		RestResponse response = new RestResponse(commandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: punch/exportPunchDetails</b>
	 * <p>
	 * 导出公司打卡的详情
	 * </p>
	 */
	@RequestMapping("exportPunchDetails")
	public  RestResponse exportPunchDetails(@Valid ListPunchDetailsCommand cmd,HttpServletResponse response ) {
		HttpServletResponse commandResponse = punchService.exportPunchDetails(cmd, response );
//		RestResponse response = new RestResponse(commandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
		return new RestResponse();
	}

	/**
	 * <b>URL: /punch/refreshDayStatistics</b>
	 * <p>
	 * 刷新统计结果 前一天的
	 * </p>
	 */
	@RequestMapping("refreshDayStatistics")
	@RestReturn(value = String.class)
	public RestResponse refreshDayStatistics() {
		punchService.dayRefreshLogScheduled();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	/**
	 * <b>URL: /punch/refreshMonthDayLogs</b>
	 * <p>
	 * 刷新统计结果 前一天的
	 * </p>
	 */
	@RequestMapping("refreshMonthDayLogs")
	@RestReturn(value = String.class)
	public RestResponse refreshMonthDayLogs(@Valid ListPunchMonthLogsCommand cmd) {
		punchService.refreshMonthDayLogs(cmd.getPunchMonth());
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>
	 * 查询公司某部门/某人 考勤规则 
	 * 已经废弃-by 2.5
	 * </p>
	 * <b>URL: /punch/getTargetPunchAllRule</b>
	 */
	@Deprecated
	@RequestMapping("getTargetPunchAllRule")
	@RestReturn(value = GetTargetPunchAllRuleResponse.class)
	public RestResponse getTargetPunchAllRule(@Valid GetTargetPunchAllRuleCommand cmd) {
		GetTargetPunchAllRuleResponse resp = punchService.getTargetPunchAllRule(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>
	 * 设置公司某部门/某人 考勤规则 
	 * 已经废弃-by2.5
	 * </p>
	 * <b>URL: /punch/updateTargetPunchAllRule</b>
	 */

	@Deprecated
	@RequestMapping("updateTargetPunchAllRule")
	@RestReturn(value = String.class)
	public RestResponse updateTargetPunchAllRule(@Valid UpdateTargetPunchAllRuleCommand cmd) {
		punchService.updateTargetPunchAllRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <p>
	 * 清空公司某部门/某人 考勤规则
	 * </p>
	 * <b>URL: /punch/deleteTargetPunchAllRule</b>
	 */
	@RequestMapping("deleteTargetPunchAllRule")
	@RestReturn(value = String.class)
	public RestResponse deleteTargetPunchAllRule(@Valid GetTargetPunchAllRuleCommand cmd) {
		punchService.deleteTargetPunchAllRule(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <p>
	 * 清空公司某部门/某人 考勤规则
	 * </p>
	 * <b>URL: /punch/testPunchDayRefresh</b>
	 */
	@RequestMapping("testPunchDayRefresh")
	@RestReturn(value = String.class)
	public RestResponse testPunchDayRefresh(@Valid TestPunchDayRefreshCommand cmd) {
		punchService.testDayRefreshLogs(cmd.getRunDate());
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: punch/refreshPunchDayLogs</b>
	 * <p>
	 * 刷新打卡详情
	 * </p>
	 */
	@RequestMapping("refreshPunchDayLogs")
	@RestReturn(value = ListPunchDetailsResponse.class)
	public RestResponse refreshPunchDayLogs(@Valid ListPunchDetailsCommand cmd) {
		punchService.refreshPunchDayLogs(cmd);
		RestResponse response = new RestResponse( );
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
