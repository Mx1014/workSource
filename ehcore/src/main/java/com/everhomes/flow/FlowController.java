package com.everhomes.flow;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.discover.SuppressDiscover;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow.*;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestDoc(value="Flow controller", site="core")
@RestController
@RequestMapping("/flow")
public class FlowController extends ControllerBase {
	
	@Autowired
	private FlowService flowService;
	
    /**
     * <b>URL: /flow/searchFlowCases</b>
     * <p> 搜索某一个用户的 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("searchFlowCases")
    @RestReturn(value=SearchFlowCaseResponse.class)
    public RestResponse searchFlowCases(@Valid SearchFlowCaseCommand cmd) {
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	Byte admin = FlowCaseSearchType.ADMIN.getCode();
    	if(admin.equals(cmd.getFlowCaseSearchType())) {
    		//never admin here
    		return response;
    	}
    	
    	response.setResponseObject(flowService.searchFlowCases(cmd));
    	
    	return response;
    }

    /**
     * <b>URL: /flow/getFlowCaseCount</b>
     * <p> 统计任务数量 </p>
     */
    @RequestMapping("getFlowCaseCount")
    @RestReturn(value=GetFlowCaseCountResponse.class)
    public RestResponse getFlowCaseCount(@Valid SearchFlowCaseCommand cmd) {
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");

    	Byte admin = FlowCaseSearchType.ADMIN.getCode();
    	if(admin.equals(cmd.getFlowCaseSearchType())) {
    		//never admin here
    		return response;
    	}
    	response.setResponseObject(flowService.getFlowCaseCount(cmd));
    	return response;
    }

    /**
     * <b>URL: /flow/getFlowCaseDetailById</b>
     * <p> flowCase详细信息 </p>
     */
    @RequestMapping("getFlowCaseDetailById")
    @RestReturn(value=FlowCaseDetailDTO.class)
    public RestResponse getFlowCaseDetailById(@Valid GetFlowCaseDetailByIdCommand cmd) {
    	Long userId = UserContext.current().getUser().getId();
        RestResponse response = new RestResponse(flowService.getFlowCaseDetail(cmd.getFlowCaseId()
        		, userId
        		, FlowUserType.fromCode(cmd.getFlowUserType())
        		, true));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/getFlowCaseDetailByIdV2</b>
     * <p> flowCase详细信息(替换旧版本的getFlowCaseDetailById)</p>
     */
    @RequestMapping("getFlowCaseDetailByIdV2")
    @RestReturn(value=FlowCaseDetailDTOV2.class)
    public RestResponse getFlowCaseDetailByIdV2(@Valid GetFlowCaseDetailByIdV2Command cmd) {
        Long userId = UserContext.current().getUser().getId();
        FlowCaseDetailDTOV2 flowCaseDetail = flowService.getFlowCaseDetailByIdV2(
                cmd.getFlowCaseId(), userId, FlowUserType.fromCode(cmd.getFlowUserType()), true,
                true);
        RestResponse response = new RestResponse(flowCaseDetail);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/getFlowCaseBrief</b>
     * <p> 获取flowCase的简单详情 </p>
     */
    @RequestMapping("getFlowCaseBrief")
    @RestReturn(value=FlowCaseBriefDTO.class)
    public RestResponse getFlowCaseBrief(@Valid GetFlowCaseBriefCommand cmd) {
        FlowCaseBriefDTO dto = flowService.getFlowCaseBrief(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/getFlowCaseTrack</b>
     * <p> 获取flowCase的跟踪信息 </p>
     */
    @RequestMapping("getFlowCaseTrack")
    @RestReturn(value=FlowCaseTrackDTO.class)
    public RestResponse getFlowCaseTrack(@Valid GetFlowCaseTrackCommand cmd) {
        FlowCaseTrackDTO dto = flowService.getFlowCaseTrack(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/listFlowServiceTypes</b>
     * <p> 获取服务类别列表 </p>
     */
    @RequestMapping("listFlowServiceTypes")
    @RestReturn(value=ListFlowServiceTypeResponse.class)
    public RestResponse listFlowServiceTypes(@Valid /*ListFlowServiceTypesCommand cmd*/SearchFlowCaseCommand cmd) {
        ListFlowServiceTypeResponse resp = flowService.listFlowServiceTypes(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/listNextBranches</b>
     * <p> 获取下一个分支列表 </p>
     */
    @RequestMapping("listNextBranches")
    @RestReturn(value=ListNextBranchesResponse.class)
    public RestResponse listNextBranches(@Valid ListNextBranchesCommand cmd) {
        ListNextBranchesResponse resp = flowService.listNextBranches(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/searchFlowOperateLogs</b>
     * <p> 搜索操作日志 </p>
     */
    @RequestMapping("searchFlowOperateLogs")
    @RestReturn(value=SearchFlowOperateLogResponse.class)
    public RestResponse searchFlowOperateLogs(@Valid SearchFlowOperateLogsCommand cmd) {
        SearchFlowOperateLogResponse resp = flowService.searchFlowOperateLogs(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/postSubject</b>
     * <p> 附言 </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("postSubject")
    @RestReturn(value=FlowPostSubjectDTO.class)
    public RestResponse postSubject(@Valid FlowPostSubjectCommand cmd) {
        RestResponse response = new RestResponse(flowService.postSubject(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/getSubjectById</b>
     * <p> 显示某一个附言对应的详细信息 </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("getSubjectById")
    @RestReturn(value=FlowSubjectDTO.class)
    public RestResponse postSubject(@Valid FlowGetSubjectDetailById cmd) {
        RestResponse response = new RestResponse(flowService.getSubectById(cmd.getSubjectId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/fireButton</b>
     * <p>按钮触发操作</p>
     */
    @RequestMapping("fireButton")
    @RestReturn(value=FlowButtonDTO.class)
    public RestResponse fireButton(@Valid FlowFireButtonCommand cmd) {
        RestResponse response = new RestResponse(flowService.fireButton(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/listButtonSelections</b>
     * <p> 列出按钮对应的用户列表 </p>
     * @return 返回评价信息
     */
    @RequestMapping("listButtonProcessorSelections")
    @RestReturn(value=ListFlowUserSelectionResponse.class)
    public RestResponse listButtonSelections(@Valid ListButtonProcessorSelectionsCommand cmd) {
        RestResponse response = new RestResponse(flowService.listButtonProcessorSelections(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/listSelectUsers</b>
     * <p> 获取选择的用户列表 </p>
     * @return 返回用户选择的信息
     */
    @RequestMapping("listSelectUsers")
    @RestReturn(value=ListSelectUsersResponse.class)
    public RestResponse listSelectUsers(@Valid ListSelectUsersCommand cmd) {
        RestResponse response = new RestResponse(flowService.listUserSelections(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/postEvaluate</b>
     * <p> 对节点进行评价 </p>
     */
    @RequestMapping("postEvaluate")
    @RestReturn(value=FlowEvaluateDTO.class)
    public RestResponse postEvaluate(@Valid FlowPostEvaluateCommand cmd) {
        RestResponse response = new RestResponse(flowService.postEvaluate(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/getEvaluateInfo</b>
     * <p> 获取评价信息 </p>
     * @return 返回评价信息
     */
    @RequestMapping("getEvaluateInfo")
    @RestReturn(value=FlowEvaluateDTO.class)
    public RestResponse getEvaluateInfo(@Valid GetEvaluateInfoCommand cmd) {
        RestResponse response = new RestResponse(flowService.getEvaluateInfo(cmd.getFlowCaseId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/listModules</b>
     * <p> 获取模块列表 </p>
     * @return 返回评价信息
     */
    @RequestMapping("listModules")
    @RestReturn(value=ListFlowModulesResponse.class)
    public RestResponse listModules(@Valid ListFlowModulesCommand cmd) {
        RestResponse response = new RestResponse(flowService.listModules(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/deleteFlowCase</b>
     * <p> 删除flowCase </p>
     */
    @RequestMapping("deleteFlowCase")
    @RestReturn(value=String.class)
    public RestResponse deleteFlowCase(@Valid DeleteFlowCaseCommand cmd) {
        flowService.deleteFlowCase(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/testCase</b>
     * <p> 仅仅用于测试 </p>
     */
    @RequestMapping("testCase")
    @RestReturn(value=String.class)
    public RestResponse testCase() {
    	flowService.testFlowCase();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/mapping/{mode}/{id1}/{id2}/{functionName}</b>
     * <p> 第三方回调 </p>
     */
    @SuppressDiscover
    @RequestMapping("mapping/{mode}/{id1}/{id2}/{functionName}")
    @RestReturn(value=Object.class)
    public DeferredResult<Object> mapping(@PathVariable Byte mode, @PathVariable Long id1,
                                                @PathVariable Long id2, @PathVariable String functionName,
                                                HttpServletRequest request) {
        return flowService.flowScriptMappingCall(mode, id1, id2, functionName, request);
    }
}