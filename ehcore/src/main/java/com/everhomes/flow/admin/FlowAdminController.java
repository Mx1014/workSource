package com.everhomes.flow.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.flow.FlowService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow.*;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value="Flow Admin controller", site="core")
@RestController
@RequestMapping("/admin/flow")
public class FlowAdminController extends ControllerBase {
	
	@Autowired
	private FlowService flowService;
	
    /**
     * <b>URL: /admin/flow/searchFlowCases</b>
     * <p> 管理员 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("searchFlowCases")
    @RestReturn(value=SearchFlowCaseResponse.class)
    public RestResponse searchFlowCases(@Valid SearchFlowCaseCommand cmd) {
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	Byte admin = FlowCaseSearchType.ADMIN.getCode();
    	cmd.setFlowCaseSearchType(admin);
    	
    	response.setResponseObject(flowService.searchFlowCases(cmd));
    	
    	return response;
    }
    
    /**
     * <b>URL: /admin/flow/getFlowCaseDetailById</b>
     * <p> 显示用户所有的 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("getFlowCaseDetailById")
    @RestReturn(value=FlowCaseDetailDTO.class)
    public RestResponse getFlowCaseDetailById(@Valid GetFlowCaseDetailByIdCommand cmd) {
    	Long userId = UserContext.current().getUser().getId();
        RestResponse response = new RestResponse(flowService.getFlowCaseDetail(cmd.getFlowCaseId()
        		, userId
        		, FlowUserType.fromCode(cmd.getFlowUserType())
        		, false));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/createFlow</b>
     * <p> 创建一个新 Flow，一个业务模块，名字不能重复 </p>
     * @return Flow 的详细信息
     */
    @RequestMapping("createFlow")
    @RestReturn(value=FlowDTO.class)
    public RestResponse createFlow(@Valid CreateFlowCommand cmd) {
        RestResponse response = new RestResponse(flowService.createFlow(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/getFlowById</b>
     * <p> 创建一个新 Flow，一个业务模块，名字不能重复 </p>
     * @return Flow 的详细信息
     */
    @RequestMapping("getFlowById")
    @RestReturn(value=FlowDTO.class)
    public RestResponse getFlowById(@Valid FlowIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.getFlowById(cmd.getFlowId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/listFlows</b>
     * <p> 显示业务模块下的所有 Flow </p>
     * @return Flow 的列表
     */
    @RequestMapping("listFlows")
    @RestReturn(value=ListFlowBriefResponse.class)
    public RestResponse listFlows(@Valid ListFlowCommand cmd) {
        RestResponse response = new RestResponse(flowService.listBriefFlows(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/enableFlow</b>
     * <p> 启用某一个业务模块下的工作流，会自动把之前的工作流禁用 </p>
     * @return
     */
    @RequestMapping("enableFlow")
    @RestReturn(value=String.class)
    public RestResponse enableFlow(@Valid FlowIdCommand cmd) {
    		flowService.enableFlow(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/disableFlow</b>
     * <p> 禁用某一个业务模块下的工作流 </p>
     * @return
     */
    @RequestMapping("disableFlow")
    @RestReturn(value=String.class)
    public RestResponse disableFlow(@Valid FlowIdCommand cmd) {
        flowService.disableFlow(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/deleteFlow</b>
     * <p> 删除某一个工作流 </p>
     * @return
     */
    @RequestMapping("deleteFlow")
    @RestReturn(value=String.class)
    public RestResponse deleteFlow(@Valid FlowIdCommand cmd) {
    		flowService.deleteFlow(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/updateFlowName</b>
     * <p> 修改工作流的名字 </p>
     * @return
     */
    @RequestMapping("updateFlowName")
    @RestReturn(value=FlowDTO.class)
    public RestResponse updateFlowName(@Valid UpdateFlowNameCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowName(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/createFlowNode</b>
     * <p> 修改工作流的名字 </p>
     * @return
     */
    @RequestMapping("createFlowNode")
    @RestReturn(value=FlowNodeDTO.class)
    public RestResponse createFlowNode(@Valid CreateFlowNodeCommand cmd) {
        RestResponse response = new RestResponse(flowService.createFlowNode(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/deleteFlowNode</b>
     * <p> 修改工作流的名字 </p>
     * @return
     */
    @RequestMapping("deleteFlowNode")
    @RestReturn(value=FlowNodeDTO.class)
    public RestResponse deleteFlowNode(@Valid DeleteFlowNodeCommand cmd) {
        RestResponse response = new RestResponse(flowService.deleteFlowNode(cmd.getFlowNodeId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/listFlowNodes</b>
     * <p> 显示工作流下的所有节点 </p>
     * @return
     */
    @RequestMapping("listFlowNodes")
    @RestReturn(value=ListBriefFlowNodeResponse.class)
    public RestResponse listFlowNodes(@Valid FlowIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.listBriefFlowNodes(cmd.getFlowId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/updateNodePriority</b>
     * <p> 重新修改工作流下的优先级 </p>
     * @return
     */
    @RequestMapping("updateFlowNodePriority")
    @RestReturn(value=ListBriefFlowNodeResponse.class)
    public RestResponse updateNodePriority(@Valid UpdateFlowNodePriorityCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateNodePriority(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/updateFlowNode</b>
     * <p> 修改工作节点的名字 </p>
     * @return
     */
    @RequestMapping("updateFlowNode")
    @RestReturn(value=FlowNodeDTO.class)
    public RestResponse updateFlowNode(@Valid UpdateFlowNodeCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowNode(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/createFlowUserSelection</b>
     * <p> 创建一波用户选择项 </p>
     */
    @RequestMapping("createFlowUserSelection")
    @RestReturn(value=ListFlowUserSelectionResponse.class)
    public RestResponse createFlowUserSelection(@Valid CreateFlowUserSelectionCommand cmd) {
        RestResponse response = new RestResponse(flowService.createFlowUserSelection(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowNodeButtons</b>
     * <p> 获取 FlowNode 节点的详细信息，buttons 信息不包含在内 </p>
     * @return
     */
    @RequestMapping("listFlowNodeButtons")
    @RestReturn(value=ListFlowButtonResponse.class)
    public RestResponse listFlowNodeButtons(@Valid GetFlowNodeDetailCommand cmd) {
        RestResponse response = new RestResponse(flowService.listFlowNodeButtons(cmd.getFlowNodeId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/getFlowNodeDetail</b>
     * <p> 获取 FlowNode 节点的详细信息，buttons 信息不包含在内 </p>
     * @return
     */
    @RequestMapping("getFlowNodeDetail")
    @RestReturn(value=FlowNodeDetailDTO.class)
    public RestResponse getFlowNodeDetail(@Valid GetFlowNodeDetailCommand cmd) {
        RestResponse response = new RestResponse(flowService.getFlowNodeDetail(cmd.getFlowNodeId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/updateFlowNodeReminder</b>
     * <p>设置用户提醒</p>
     */
    @RequestMapping("updateFlowNodeReminder")
    @RestReturn(value=FlowNodeDetailDTO.class)
    public RestResponse updateFlowNodeReminder(@Valid UpdateFlowNodeReminderCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowNodeReminder(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    
    
    /**
     * <b>URL: /admin/flow/updateFlowNodeTracker</b>
     * <p> 创建一波用户选择项 </p>
     * @return
     */
    @RequestMapping("updateFlowNodeTracker")
    @RestReturn(value=FlowNodeDetailDTO.class)
    public RestResponse updateFlowNodeTracker(@Valid UpdateFlowNodeTrackerCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowNodeTracker(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    
    
    /**
     * <b>URL: /admin/flow/listFlowUserSelection</b>
     * <p> 列出所有用户的选择 </p>
     * @return
     */
    @RequestMapping("listFlowUserSelection")
    @RestReturn(value=ListFlowUserSelectionResponse.class)
    public RestResponse listFlowUserSelection(@Valid ListFlowUserSelectionCommand cmd) {
        RestResponse response = new RestResponse(flowService.listFlowUserSelection(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/deleteUserSelection</b>
     * <p> 删除用户选择项 </p>
     * @return
     */
    @RequestMapping("deleteUserSelection")
    @RestReturn(value=FlowUserSelectionDTO.class)
    public RestResponse deleteUserSelection(@Valid DeleteFlowUserSelectionCommand cmd) {
        RestResponse response = new RestResponse(flowService.deleteUserSelection(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/getFlowButtonDetail</b>
     * <p> 删除用户选择项 </p>
     * @return
     */
    @RequestMapping("getFlowButtonDetail")
    @RestReturn(value=FlowButtonDetailDTO.class)
    public RestResponse getFlowButtonDetail(@Valid GetFlowButtonDetailByIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.getFlowButtonDetail(cmd.getFlowButtonId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/updateFlowButton</b>
     * <p> 修改节点按钮信息 </p>
     * @return
     */
    @RequestMapping("updateFlowButton")
    @RestReturn(value=FlowButtonDetailDTO.class)
    public RestResponse updateFlowButton(@Valid UpdateFlowButtonCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowButton(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/disableFlowButton</b>
     * <p> 禁用节点中的某一个按钮应用 </p>
     * @return
     */
    @RequestMapping("enableFlowButton")
    @RestReturn(value=FlowButtonDTO.class)
    public RestResponse enableFlowButton(@Valid DisableFlowButtonCommand cmd) {
        RestResponse response = new RestResponse(flowService.enableFlowButton(cmd.getFlowButtonId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/disableFlowButton</b>
     * <p> 禁用节点中的某一个按钮应用 </p>
     * @return
     */
    @RequestMapping("disableFlowButton")
    @RestReturn(value=FlowButtonDTO.class)
    public RestResponse disableFlowButton(@Valid DisableFlowButtonCommand cmd) {
        RestResponse response = new RestResponse(flowService.disableFlowButton(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/listVariables</b>
     * <p> 显示某个位置的变量 </p>
     * @return
     */
    @RequestMapping("listVariables")
    @RestReturn(value=FlowVariableResponse.class)
    public RestResponse listFlowVariables(@Valid ListFlowVariablesCommand cmd) {
        RestResponse response = new RestResponse(flowService.listFlowVariables(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/getFlowGraphDetail</b>
     * <p> 创建一个新 Flow，一个业务模块，名字不能重复 </p>
     * @return Flow 的详细信息
     */
    @RequestMapping("getFlowGraphDetail")
    @RestReturn(value=FlowGraphDetailDTO.class)
    public RestResponse getFlowGraphDetail(@Valid GetFlowGraphDetailCommand cmd) {
        RestResponse response = new RestResponse(flowService.getFlowGraphDetail(cmd.getFlowId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/clearFlowCache</b>
     * <p> 创建一个新 Flow，一个业务模块，名字不能重复 </p>
     * @return Flow 的详细信息
     */
    @RequestMapping("clearFlowCache")
    @RestReturn(value=String.class)
    public RestResponse clearFlowCache(@Valid FlowIdCommand cmd) {
    	flowService.clearFlowGraphCache(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/updateFlowEvaluate</b>
     * <p> 修改工作流的评分属性 </p>
     */
    @RequestMapping("updateFlowEvaluate")
    @RestReturn(value=FlowEvaluateDetailDTO.class)
    public RestResponse updateFlowEvaluate(@Valid UpdateFlowEvaluateCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowEvaluate(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/getFlowEvaluate</b>
     * <p> 获取工作流的评分信息 </p>
     * @return
     */
    @RequestMapping("getFlowEvaluate")
    @RestReturn(value=FlowEvaluateDetailDTO.class)
    public RestResponse getFlowEvaluate(@Valid FlowIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.getFlowEvaluate(cmd.getFlowId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/listScripts</b>
     * <p> 获取工作流的评分信息 </p>
     * @return
     */
    @RequestMapping("listScripts")
    @RestReturn(value=ListScriptsResponse.class)
    public RestResponse listScripts(@Valid ListScriptsCommand cmd) {
        RestResponse response = new RestResponse(flowService.listScripts(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/listSMSTemplates</b>
     * <p>获取消息提醒里的短信模板</p>
     */
    @RequestMapping("listSMSTemplates")
    @RestReturn(value=FlowSMSTemplateResponse.class)
    public RestResponse listSMSTemplates(@Valid ListSMSTemplateCommand cmd) {
        RestResponse response = new RestResponse(flowService.listSMSTemplates(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/resolveUsers</b>
     * <p> 获取工作流的评分信息 </p>
     * @return
     */
    @RequestMapping("resolveUsers")
    @RestReturn(value=FlowResolveUsersResponse.class)
    public RestResponse resolveUsers(@Valid FlowResolveUsersCommand cmd) {
        RestResponse response = new RestResponse(flowService.resolveSelectionUsers(cmd.getFlowId(), cmd.getSelectionId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/createFlowGraph</b>
     * <p>保存工作流流程图</p>
     */
    @RequestMapping("createFlowGraph")
    @RestReturn(value=String.class)
    public RestResponse createFlowGraph(@Valid CreateFlowGraphCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/getFlowGraph</b>
     * <p>获取工作流流程图</p>
     */
    @RequestMapping("getFlowGraph")
    @RestReturn(value=FlowGraphDTO.class)
    public RestResponse getFlowGraph(@Valid GetFlowGraphCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
