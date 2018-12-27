package com.everhomes.techpark.punch.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.organization.AddPersonnelsToGroup;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.techpark.punch.AddPunchLogShouldPunchTimeCommand;
import com.everhomes.rest.techpark.punch.GetPunchQRCodeCommand;
import com.everhomes.rest.techpark.punch.ListOrganizationPunchLogsCommand;
import com.everhomes.rest.techpark.punch.ListOrganizationPunchLogsResponse;
import com.everhomes.rest.techpark.punch.ListPunchCountCommand;
import com.everhomes.rest.techpark.punch.ListPunchCountCommandResponse;
import com.everhomes.rest.techpark.punch.ListPunchLogsCommand;
import com.everhomes.rest.techpark.punch.ListPunchLogsResponse;
import com.everhomes.rest.techpark.punch.PunchDayLogInitializeCommand;
import com.everhomes.rest.techpark.punch.PunchDayLogInitializeMonthlyCommand;
import com.everhomes.rest.techpark.punch.admin.AddPersonnelsToPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.BatchUpdateVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.DeleteCommonCommand;
import com.everhomes.rest.techpark.punch.admin.ExportVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupsCountCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupsCountResponse;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleResponse;
import com.everhomes.rest.techpark.punch.admin.GetUserPunchRuleInfoCommand;
import com.everhomes.rest.techpark.punch.admin.GetUserPunchRuleInfoResponse;
import com.everhomes.rest.techpark.punch.admin.ImportVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ListAllSimplePunchGroupsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchGroupsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchGroupsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchMonthLogsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchMonthLogsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchSchedulingMonthResponse;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalanceLogsCommand;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalanceLogsResponse;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalancesResponse;
import com.everhomes.rest.techpark.punch.admin.PunchGroupDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO;
import com.everhomes.rest.techpark.punch.admin.TestPunchDayRefreshCommand;
import com.everhomes.rest.techpark.punch.admin.TransforSceneTokenCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.UpdateTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.UpdateVacationBalancesCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.techpark.punch.PunchVacationBalanceService;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.text.ParseException;

@RestDoc(value = "Punch controller", site = "ehccore")
@RestController
@RequestMapping("/punch")
public class PunchAdminController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(PunchAdminController.class);

    @Autowired
    private PunchService punchService;
    @Autowired
    private UserService userService;
    @Autowired
    private PunchVacationBalanceService punchVacationBalanceService;
//	/**
//	 * <b>URL: /punch/addPunchTimeRule</b>
//	 * <p>
//	 * 添加公司时间考勤规则
//	 * </p>
//	 */
//	@RequestMapping("addPunchTimeRule")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchTimeRule(@Valid AddPunchTimeRuleCommand cmd) {
//		punchService.addPunchTimeRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	

//	/**
//	 * <b>URL: /punch/updatePunchTimeRule</b>
//	 * <p>
//	 * 更新公司时间考勤规则
//	 * </p>
//	 */
//	@RequestMapping("updatePunchTimeRule")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchTimeRule(@Valid UpdatePunchTimeRuleCommand cmd) {
//		punchService.updatePunchTimeRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//
//	/**
//	 * <b>URL: /punch/deletePunchTimeRule</b>
//	 * <p>
//	 * 删除公司时间考勤规则
//	 * </p>
//	 */
//	@RequestMapping("deletePunchTimeRule")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchTimeRule(@Valid DeleteCommonCommand cmd) {
//		punchService.deletePunchTimeRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}

//	/**
//	 * <b>URL: /punch/listPunchTimeRuleList</b>
//	 * <p>
//	 * 查询公司时间考勤规则列表
//	 * </p>
//	 */
//	@RequestMapping("listPunchTimeRules")
//	@RestReturn(value = listPunchTimeRuleListResponse.class)
//	public RestResponse listPunchTimeRules(@Valid ListPunchRulesCommonCommand cmd) {
//		listPunchTimeRuleListResponse resp = punchService.listPunchTimeRuleList(cmd);
//		RestResponse response = new RestResponse(resp);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	//地点考勤
//
//	/**
//	 * <b>URL: /punch/addPunchLocationRule</b>
//	 * <p>
//	 * 添加公司地点考勤规则
//	 * </p>
//	 */
//	@RequestMapping("addPunchLocationRule")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchLocationRule(@Valid PunchLocationRuleDTO cmd) {
//		punchService.addPunchLocationRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//	/**
//	 * <b>URL: /punch/addPunchPoint</b>
//	 * <p>
//	 * 给对应的某个公司/人增加考勤点
//	 * </p>
//	 */
//	@RequestMapping("addPunchPoint")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchPoint(@Valid AddPunchPointCommand cmd) {
//		punchService.addPunchPoint(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//	
//	/**
//	 * <b>URL: /punch/updatePunchPoint</b>
//	 * <p>
//	 * 更新考勤点
//	 * </p>
//	 */
//	@RequestMapping("updatePunchPoint")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchPoint(@Valid UpdatePunchPointCommand cmd) {
//		punchService.updatePunchPoint(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//
//	/**
//	 * <b>URL: /punch/listPunchPoints</b>
//	 * <p>
//	 * 查询公司地点考勤规则列表
//	 * </p>
//	 */
//	@RequestMapping("listPunchPoints")
//	@RestReturn(value = ListPunchPointsResponse.class)
//	public RestResponse listPunchPoints(@Valid ListPunchPointsCommand cmd) {
//		ListPunchPointsResponse resp = punchService.listPunchPoints(cmd);
//		RestResponse response = new RestResponse(resp);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	/**
//	 * <b>URL: /punch/deletePunchPoint</b>
//	 * <p>
//	 * 删除公司考勤地点
//	 * </p>
//	 */
//	@RequestMapping("deletePunchPoint")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchPoint(@Valid DeleteCommonCommand cmd) {
//		punchService.deletePunchPoint(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	
//	
//	/**
//	 * <b>URL: /punch/updatePunchLocationRule</b>
//	 * <p>
//	 * 更新公司地点考勤规则
//	 * </p>
//	 */
//	@RequestMapping("updatePunchLocationRule")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchTimeRule(@Valid PunchLocationRuleDTO cmd) {
//		punchService.updatePunchLocationRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//
//	/**
//	 * <b>URL: /punch/deletePunchLocationRule</b>
//	 * <p>
//	 * 删除公司地点考勤规则
//	 * </p>
//	 */
//	@RequestMapping("deletePunchLocationRule")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchLocationRule(@Valid DeleteCommonCommand cmd) {
//		punchService.deletePunchLocationRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /punch/listPunchLocationRules</b>
//	 * <p>
//	 * 查询公司地点考勤规则列表
//	 * </p>
//	 */
//	@RequestMapping("listPunchLocationRules")
//	@RestReturn(value = QryPunchLocationRuleListResponse.class)
//	public RestResponse listPunchLocationRules(@Valid ListPunchRulesCommonCommand cmd) {
//		QryPunchLocationRuleListResponse resp = punchService.listPunchLocationRules(cmd);
//		RestResponse response = new RestResponse(resp);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	//wifi规则
//
//	/**
//	 * <b>URL: /punch/addPunchWiFiRule</b>
//	 * <p>
//	 * 添加公司WIFI考勤规则
//	 * </p>
//	 */
//	@RequestMapping("addPunchWiFiRule")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchWiFiRule(@Valid PunchWiFiRuleDTO cmd) {
//		punchService.addPunchWiFiRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//
//	/**
//	 * <b>URL: /punch/addPunchWiFi</b>
//	 * <p>
//	 * 添加target对象的WIFI考勤点
//	 * </p>
//	 */
//	@RequestMapping("addPunchWiFi")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchWiFi(@Valid AddPunchWiFiCommand cmd) {
//		punchService.addPunchWiFi(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//	
//	/**
//	 * <b>URL: /punch/updatePunchWiFi</b>
//	 * <p>
//	 * 修改target对象的WIFI考勤点
//	 * </p>
//	 */
//	@RequestMapping("updatePunchWiFi")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchWiFi(@Valid PunchWiFiDTO cmd) {
//		punchService.updatePunchWiFi(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	/**
//	 * <b>URL: /punch/deletePunchWiFi</b>
//	 * <p>
//	 * 删除target对象的WIFI考勤点
//	 * </p>
//	 */
//	@RequestMapping("deletePunchWiFi")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchWiFi(@Valid PunchWiFiDTO cmd) {
//		punchService.deletePunchWiFi(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//
//	/**
//	 * <b>URL: /punch/listPunchWiFis</b>
//	 * <p>
//	 * 查询公司WIFI考勤规则列表
//	 * </p>
//	 */
//	@RequestMapping("listPunchWiFis")
//	@RestReturn(value = ListPunchWiFisResponse.class)
//	public RestResponse listPunchWiFis(@Valid ListPunchRulesCommonCommand cmd) {
//		ListPunchWiFisResponse resp = punchService.listPunchWiFis(cmd);
//		RestResponse response = new RestResponse(resp);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /punch/updatePunchWiFiRule</b>
//	 * <p>
//	 * 更新公司WIFI考勤规则
//	 * </p>
//	 */
//	@RequestMapping("updatePunchWiFiRule")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchWiFiRule(@Valid PunchWiFiRuleDTO cmd) {
//		punchService.updatePunchWiFiRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//
//	/**
//	 * <b>URL: /punch/deletePunchWiFiRule</b>
//	 * <p>
//	 * 
//	 * 删除公司WIFI考勤规则
//	 * </p>
//	 */
//	@Deprecated
//	@RequestMapping("deletePunchWiFiRule")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchWiFiRule(@Valid DeleteCommonCommand cmd) {
//		punchService.deletePunchWiFiRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /punch/listPunchWiFiRules</b>
//	 * <p>
//	 * 查询公司WIFI考勤规则列表
//	 * </p>
//	 */
//	@RequestMapping("listPunchWiFiRules")
//	@RestReturn(value = ListPunchWiFiRuleListResponse.class)
//	public RestResponse listPunchWiFiRules(@Valid ListPunchRulesCommonCommand cmd) {
//		ListPunchWiFiRuleListResponse resp = punchService.listPunchWiFiRule(cmd);
//		RestResponse response = new RestResponse(resp);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	

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
     * 导出排班表
     * </p>
     */
    @RequestMapping("exportPunchScheduling")
    public RestResponse exportPunchScheduling(@Valid ListPunchSchedulingMonthCommand cmd, HttpServletResponse response) {
        punchService.exportPunchScheduling(cmd, response);
//		RestResponse response = new RestResponse(commandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
        return new RestResponse();
    }


    /**
     * <b>URL: /punch/exportPunchSchedulingTemplate</b>
     * <p>
     * 导出排班表模板
     * </p>
     */
    @RequestMapping("exportPunchSchedulingTemplate")
    public HttpServletResponse exportPunchSchedulingTemplate(@Valid ListPunchSchedulingMonthCommand cmd, HttpServletResponse response) {
        HttpServletResponse commandResponse = punchService.exportPunchSchedulingTemplate(cmd, response);
//		RestResponse response = new RestResponse(commandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
        return commandResponse;
    }


    /**
     * <b>URL: /punch/importPunchScheduling</b>
     * <p>导入排班表</p>
     */
    @RequestMapping("importPunchScheduling")
    @RestReturn(value = PunchSchedulingDTO.class)
    public RestResponse importPunchScheduling(@RequestParam(value = "attachment") MultipartFile[] files) {
        PunchSchedulingDTO result = punchService.importPunchScheduling(files);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /punch/testimportPunchScheduling</b>
     * <p>导入某个月的班次</p>
     */
    @RequestMapping("testimportPunchScheduling")
    @RestReturn(value = PunchSchedulingDTO.class)
    public RestResponse testimportPunchScheduling(@RequestParam(value = "_attachment_file") MultipartFile[] files) {
        return importPunchScheduling(files);
    }

    /**
     * <b>URL: /punch/testimportPunchLogs</b>
     * <p>给登录用户通过excel导入打卡记录--测试用</p>
     */
    @RequestMapping("testimportPunchLogs")
    @RestReturn(value = String.class)
    public RestResponse testimportPunchLogs(@RequestParam(value = "_attachment_file") MultipartFile[] files) {
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

//	
//	
//	
//	/**
//	 * <b>URL: /punch/addPunchWorkdayRule</b>
//	 * <p>
//	 * 添加公司排班考勤规则
//	 * </p>
//	 */
//	@RequestMapping("addPunchWorkdayRule")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchWorkdayRule(@Valid PunchWorkdayRuleDTO cmd) {
//		punchService.addPunchWorkdayRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//	
//	/**
//	 * <b>URL: /punch/updatePunchWorkdayRule</b>
//	 * <p>
//	 * 设置公司排班考勤规则
//	 * </p>
//	 */
//	@RequestMapping("updatePunchWorkdayRule")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchWorkdayRule(@Valid PunchWorkdayRuleDTO cmd) {
//		punchService.updatePunchWorkdayRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//
//	/**
//	 * <b>URL: /punch/deletePunchWorkdayRule</b>
//	 * <p>
//	 * 删除公司排班考勤规则
//	 * </p>
//	 */
//	@RequestMapping("deletePunchWorkdayRule")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchWorkdayRule(@Valid DeleteCommonCommand cmd) {
//		punchService.deletePunchWorkdayRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /punch/listPunchWorkdayRules</b>
//	 * <p>
//	 * 查询公司排班考勤规则列表
//	 * </p>
//	 */
//	@RequestMapping("listPunchWorkdayRules")
//	@RestReturn(value = ListPunchWorkdayRuleListResponse.class)
//	public RestResponse listPunchWorkdayRules(@Valid ListPunchRulesCommonCommand cmd) {
//		ListPunchWorkdayRuleListResponse resp = punchService.listPunchWorkdayRule(cmd);
//		RestResponse response = new RestResponse(resp);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//
//
//	//总规则
//
//	/**
//	 * <b>URL: /punch/addPunchRule</b>
//	 * <p>
//	 * 添加公司考勤规则
//	 * </p>
//	 */
//	@RequestMapping("addPunchRule")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchRule(@Valid PunchRuleDTO cmd) {
//		punchService.addPunchRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//	
//	/**
//	 * <b>URL: /punch/updatePunchRule</b>
//	 * <p>
//	 * 设置公司考勤规则
//	 * </p>
//	 */
//	@RequestMapping("updatePunchRule")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchRule(@Valid PunchRuleDTO cmd) {
//		punchService.updatePunchRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//
//	/**
//	 * <b>URL: /punch/deletePunchRule</b>
//	 * <p>
//	 * 删除公司考勤规则
//	 * </p>
//	 */
//	@RequestMapping("deletePunchRule")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchRule(@Valid DeleteCommonCommand cmd) {
//		punchService.deletePunchRule(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /punch/listPunchRules</b>
//	 * <p>
//	 * 查询公司考勤规则列表
//	 * </p>
//	 */
//	@RequestMapping("listPunchRules")
//	@RestReturn(value = ListPunchRulesResponse.class)
//	public RestResponse listPunchRules(@Valid ListPunchRulesCommonCommand cmd) {
//		ListPunchRulesResponse resp = punchService.listPunchRules(cmd);
//		RestResponse response = new RestResponse(resp);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//
//	//映射
//
//	/**
//	 * <b>URL: /punch/addPunchRuleMap</b>
//	 * <p>
//	 * 添加考勤规则映射
//	 * </p>
//	 */
//	@RequestMapping("addPunchRuleMap")
//	@RestReturn(value = String.class)
//	public RestResponse addPunchRuleMap(@Valid PunchRuleMapDTO cmd) {
//		punchService.addPunchRuleMap(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//	/**
//	 * <b>URL: /punch/addPunchRuleMap</b>
//	 * <p>
//	 * 更新考勤规则映射 -特殊个人设置
//	 * </p>
//	 */
//	@RequestMapping("updatePunchRuleMap")
//	@RestReturn(value = String.class)
//	public RestResponse updatePunchRuleMap(@Valid PunchRuleMapDTO cmd) {
//		punchService.updatePunchRuleMap(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}	
//	
//
//	/**
//	 * <b>URL: /punch/deletePunchRuleMap</b>
//	 * <p>
//	 * 删除公司考勤规则
//	 * (目前只有设置没有删除)
//	 * </p>
//	 */
//	@RequestMapping("deletePunchRuleMap")
//	@RestReturn(value = String.class)
//	public RestResponse deletePunchRuleMap(@Valid DeletePunchRuleMapCommand cmd) {
//		punchService.deletePunchRuleMap(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	
//	/**
//	 * <b>URL: /punch/listPunchRuleMaps</b>
//	 * <p>
//	 * 查询公司考勤规则列表
//	 * 列出设置了特殊规则的人
//	 * </p>
//	 */
//	@RequestMapping("listPunchRuleMaps")
//	@RestReturn(value = ListPunchRuleMapsResponse.class)
//	public RestResponse listPunchRuleMaps(@Valid ListPunchRuleMapsCommand cmd) {
//		ListPunchRuleMapsResponse resp = punchService.listPunchRuleMaps(cmd);
//		RestResponse response = new RestResponse(resp);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//	

    //设置打卡规则

    /**
     * <b>URL: punch/addPunchGroup</b>
     * <p>
     * 新增打卡规则(考勤组)
     * </p>
     */
    @RequestMapping("addPunchGroup")
    @RestReturn(value = PunchGroupDTO.class)
    public RestResponse addPunchGroup(@Valid AddPunchGroupCommand cmd) {
        PunchGroupDTO commandResponse = punchService.addPunchGroup(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/getPunchGroup</b>
     * <p>
     * 列出打卡规则(考勤组)
     * </p>
     */
    @RequestMapping("getPunchGroup")
    @RestReturn(value = PunchGroupDTO.class)
    public RestResponse getPunchGroup(@Valid GetPunchGroupCommand cmd) {
        PunchGroupDTO commandResponse = punchService.getPunchGroup(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/listPunchGroups</b>
     * <p>
     * 列出打卡规则(考勤组)
     * </p>
     */
    @RequestMapping("listPunchGroups")
    @RestReturn(value = ListPunchGroupsResponse.class)
    public RestResponse listPunchGroups(@Valid ListPunchGroupsCommand cmd) {
        ListPunchGroupsResponse commandResponse = punchService.listPunchGroups(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/listAllSimplePunchGroups</b>
     * <p>
     * 列出全部考勤组的基本 信息(给设置未安排班次的规则使用)
     * </p>
     */
    @RequestMapping("listAllSimplePunchGroups")
    @RestReturn(value = ListAllSimplePunchGroupsResponse.class)
    public RestResponse listAllSimplePunchGroups(@Valid ListPunchGroupsCommand cmd) {
        ListAllSimplePunchGroupsResponse commandResponse = punchService.listAllSimplePunchGroups(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: punch/getPunchGroupsCount</b>
     * <p>
     * 获取某公司总人数和关联人数
     * </p>
     */
    @RequestMapping("getPunchGroupsCount")
    @RestReturn(value = GetPunchGroupsCountResponse.class)
    public RestResponse getPunchGroupsCount(@Valid GetPunchGroupsCountCommand cmd) {
        GetPunchGroupsCountResponse commandResponse = punchService.getPunchGroupsCount(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: punch/updatePunchGroup</b>
     * <p>
     * 更新打卡规则(考勤组)
     * </p>
     */
    @RequestMapping("updatePunchGroup")
    @RestReturn(value = PunchGroupDTO.class)
    public RestResponse updatePunchGroup(@Valid PunchGroupDTO cmd) {
        PunchGroupDTO commandResponse = punchService.updatePunchGroup(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/addPersonnelsToPunchGroup</b>
     * <p>
     * 更新打卡规则(考勤组)
     * </p>
     * @return 
     */
    @RequestMapping("addPersonnelsToPunchGroup")
    @RestReturn(value = String.class)
    public RestResponse addPersonnelsToPunchGroup (@Valid AddPersonnelsToPunchGroupCommand cmd) {
        punchService.addPersonnelsToPunchGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/deletePunchGroup</b>
     * <p>
     * 删除打卡规则(考勤组)
     * </p>
     */
    @RequestMapping("deletePunchGroup")
    @RestReturn(value = String.class)
    public RestResponse deletePunchGroup(@Valid DeleteCommonCommand cmd) {
        punchService.deletePunchGroup(cmd);
        RestResponse response = new RestResponse();
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
        Long ownerId = punchService.getTopEnterpriseId(cmd.getOwnerId());
        punchService.checkAppPrivilege(ownerId, cmd.getOwnerId(), PrivilegeConstants.PUNCH_STATISTIC_QUERY);
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
    public RestResponse exportPunchStatistics(@Valid ListPunchCountCommand cmd, HttpServletResponse response) {
        Long ownerId = punchService.getTopEnterpriseId(cmd.getOwnerId());
        punchService.checkAppPrivilege(ownerId, cmd.getOwnerId(), PrivilegeConstants.PUNCH_STATISTIC_EXPORT);
        HttpServletResponse commandResponse = punchService.exportPunchStatistics(cmd, response);
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
        Long ownerId = punchService.getTopEnterpriseId(cmd.getOwnerId());
        punchService.checkAppPrivilege(ownerId, cmd.getOwnerId(), PrivilegeConstants.PUNCH_STATISTIC_QUERY);
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
    public RestResponse exportPunchDetails(@Valid ListPunchDetailsCommand cmd, HttpServletResponse response) {
        Long ownerId = punchService.getTopEnterpriseId(cmd.getOwnerId());
        punchService.checkAppPrivilege(ownerId, cmd.getOwnerId(), PrivilegeConstants.PUNCH_STATISTIC_EXPORT);
        HttpServletResponse commandResponse = punchService.exportPunchDetails(cmd, response);
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
        punchService.testDayRefreshLogs(cmd.getRunDate(), cmd.getOrgId());
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
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/refreshPunchGroupScheduled</b>
     * <p>
     * 刷新次日更新
     * </p>
     */
    @RequestMapping("refreshPunchGroupScheduled")
    @RestReturn(value = String.class)
    public RestResponse refreshPunchGroupScheduled() {
        punchService.dayRefreshPunchGroupScheduled();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/transforSceneToken</b>
     * <p>
     * <p>
     * </p>
     */
    @RequestMapping("transforSceneToken")
    @RequireAuthentication(false)
    @RestReturn(value = SceneTokenDTO.class)
    public RestResponse transforSceneToken(@Valid TransforSceneTokenCommand cmd) {
        SceneTokenDTO sceneToken = userService.checkSceneToken(cmd.getUserId(), cmd.getSceneToken());
        RestResponse response = new RestResponse(sceneToken);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/getPunchQRCode</b>
     * <p>
     * 获取二维码图片 返回二进制流
     * </p>
     */
    @RequestMapping("getPunchQRCode")
    @RestReturn(value = String.class)
    public RestResponse getPunchQRCode(@Valid GetPunchQRCodeCommand cmd, HttpServletResponse response) {
        String resp = punchService.getPunchQRCode(cmd, response);
        return new RestResponse(resp);
    }

    /**
     * <b>URL: punch/getPunchQRCodeResult</b>
     * <p>web调用,长轮询得到扫码结果</p>
     */
    @RequestMapping("getPunchQRCodeResult")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    public DeferredResult<RestResponse> getPunchQRCodeResult(@Valid GetPunchQRCodeCommand cmd) {
        return punchService.getPunchQRCodeResult(cmd);
    }


    /**
     * <b>URL: punch/invalidPunchQRCode</b>
     * <p>web调用,让二维码过期</p>
     */
    @RequestMapping("invalidPunchQRCode")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    public RestResponse invalidPunchQRCode(@Valid GetPunchQRCodeCommand cmd) {
        punchService.invalidPunchQRCode(cmd);
        return new RestResponse();
    }

    /**
     * <b>URL: punch/listPunchLogs</b>
     * <p>
     * 查询某人某天的真实打卡记录
     * </p>
     */
    @RequestMapping("listPunchLogs")
    @RestReturn(value = ListPunchLogsResponse.class)
    public RestResponse listPunchLogs(@Valid ListPunchLogsCommand cmd) {
        ListPunchLogsResponse commandResponse = punchService.listPunchLogs(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>查询假期余额列表</p>
     * <b>URL: /punch/listVacationBalances</b>
     */
    @RequestMapping("listVacationBalances")
    @RestReturn(ListVacationBalancesResponse.class)
    public RestResponse listVacationBalances(ListVacationBalancesCommand cmd) {
        return new RestResponse(punchVacationBalanceService.listVacationBalances(cmd));
    }

    /**
     * <p>修改余额</p>
     * <b>URL: /punch/updateVacationBalances</b>
     */
    @RequestMapping("updateVacationBalances")
    @RestReturn(String.class)
    public RestResponse updateVacationBalances(UpdateVacationBalancesCommand cmd) {
        punchVacationBalanceService.updateVacationBalancesAndSendMessage(cmd);
        return new RestResponse();
    }

    /**
     * <p>批量修改余额</p>
     * <b>URL: /punch/batchUpdateVacationBalances</b>
     */
    @RequestMapping("batchUpdateVacationBalances")
    @RestReturn(String.class)
    public RestResponse batchUpdateVacationBalances(BatchUpdateVacationBalancesCommand cmd) {
        punchVacationBalanceService.batchUpdateVacationBalances(cmd);
        return new RestResponse();
    }

    /**
     * <p>分页查询操作日志</p>
     * <b>URL: /punch/listVacationBalanceLogs</b>
     */
    @RequestMapping("listVacationBalanceLogs")
    @RestReturn(ListVacationBalanceLogsResponse.class)
    public RestResponse listVacationBalanceLogs(ListVacationBalanceLogsCommand cmd) {
        return new RestResponse(punchVacationBalanceService.listVacationBalanceLogs(cmd));
    }

    /**
     * <p>批量导出</p>
     * <b>URL: /punch/exportVacationBalances</b>
     */
    @RequestMapping("exportVacationBalances")
    @RestReturn(String.class)
    public RestResponse exportVacationBalances(ExportVacationBalancesCommand cmd) {
        punchVacationBalanceService.exportVacationBalances(cmd);
        return new RestResponse();
    }

    /**
     * <p>批量导入</p>
     * <b>URL: /punch/importVacationBalances</b>
     */
    @RequestMapping("importVacationBalances")
    @RestReturn(ImportFileTaskDTO.class)
    public RestResponse importVacationBalances(@RequestParam(value = "attachment") MultipartFile[] files, ImportVacationBalancesCommand cmd) {
        ImportFileTaskDTO dto = punchVacationBalanceService.importVacationBalances(files, cmd);
        return new RestResponse(dto);
    }
    
    /**
     * <p>刷新punchLog的应打卡时间</p>
     * <b>URL: /punch/addPunchLogShouldPunchTime</b>
     */
    @RequestMapping("addPunchLogShouldPunchTime")
    @RestReturn(String.class)
    public RestResponse addPunchLogShouldPunchTime(AddPunchLogShouldPunchTimeCommand cmd) {
        punchService.addPunchLogShouldPunchTime( cmd);
        return new RestResponse();
    }

    /**
     * <b>URL: punch/listOrganizationPunchLogs</b>
     * <p>
     * 查询某人某天的真实打卡记录
     * </p>
     */
    @RequestMapping("listOrganizationPunchLogs")
    @RestReturn(value = ListOrganizationPunchLogsResponse.class)
    public RestResponse listOrganizationPunchLogs(@Valid ListOrganizationPunchLogsCommand cmd) {
    	ListOrganizationPunchLogsResponse commandResponse = punchService.listOrganizationPunchLogs(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/getUserPunchRuleInfo</b>
     * <p>
     * 列出用户的考勤规则详情
     * </p>
     */
    @RequestMapping("getUserPunchRuleInfo")
    @RestReturn(value = GetUserPunchRuleInfoResponse.class)
    public RestResponse getUserPunchRuleInfo(@Valid GetUserPunchRuleInfoCommand cmd) {
        RestResponse response = new RestResponse(punchService.getUserPunchRuleInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: punch/punchDayLogInitialize</b>
     * <p>手动初始化考勤每日统计数据
     * </p>
     */
    @RequestMapping("punchDayLogInitialize")
    @RestReturn(value = String.class)
    public RestResponse punchDayLogInitialize(PunchDayLogInitializeCommand cmd) {
        punchService.punchDayLogInitialize(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /punch/punchDayLogInitializeByMonth</b>
     * <p>
     * 初始化某个月的每日统计数据,上线时手动调用进行初始化
     * </p>
     */
    @RequestMapping("punchDayLogInitializeByMonth")
    @RestReturn(value = String.class)
    public RestResponse punchDayLogInitializeByMonth(@Valid PunchDayLogInitializeMonthlyCommand cmd) throws ParseException {
        punchService.punchDayLogInitializeByMonth(cmd.getInitialMonth());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
