package com.everhomes.flow.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.XssExclude;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.flow.FlowService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.*;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestDoc(value = "Flow Admin controller", site = "core")
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
    @RestReturn(value = SearchFlowCaseResponse.class)
    public RestResponse searchFlowCases(@Valid SearchFlowCaseCommand cmd) {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        if (cmd.getFlowCaseSearchType() == null) {
            Byte admin = FlowCaseSearchType.ADMIN.getCode();
            cmd.setFlowCaseSearchType(admin);
        }
        response.setResponseObject(flowService.searchFlowCases(cmd));

        return response;
    }

    /**
     * <b>URL: /admin/flow/getFlowCaseDetailById</b>
     * <p> 获取 FlowCase 详情</p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("getFlowCaseDetailById")
    @RestReturn(value = FlowCaseDetailDTOV2.class)
    public RestResponse getFlowCaseDetailById(@Valid GetFlowCaseDetailByIdCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
        boolean needFlowButton = TrueOrFalseFlag.fromCode(cmd.getNeedFlowButton()) == TrueOrFalseFlag.TRUE;
        RestResponse response = new RestResponse(flowService.getFlowCaseDetailByIdV2(cmd.getFlowCaseId()
                , userId
                , FlowUserType.fromCode(cmd.getFlowUserType())
                , true, needFlowButton));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/getFlowCaseDetailByRefer</b>
     * <p> 根据refer获取FlowCase详情</p>
     */
    @RequestMapping("getFlowCaseDetailByRefer")
    @RestReturn(value = FlowCaseDetailDTOV2.class)
    public RestResponse getFlowCaseDetailByRefer(@Valid GetFlowCaseDetailByReferCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
        boolean needFlowButton = TrueOrFalseFlag.fromCode(cmd.getNeedFlowButton()) == TrueOrFalseFlag.TRUE;

        FlowCaseDetailDTOV2 detailDTOV2 = flowService.getFlowCaseDetailByRefer(
                cmd.getModuleId(),
                FlowUserType.fromCode(cmd.getFlowUserType()),
                userId,
                cmd.getReferType(),
                cmd.getReferId(),
                needFlowButton);

        RestResponse response = new RestResponse(detailDTOV2);
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
    @RestReturn(value = FlowDTO.class)
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
    @RestReturn(value = FlowDTO.class)
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
    @RestReturn(value = ListFlowBriefResponse.class)
    public RestResponse listFlows(@Valid ListFlowCommand cmd) {
        RestResponse response = new RestResponse(flowService.listBriefFlows(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/enableFlow</b>
     * <p> 启用某一个业务模块下的工作流，会自动把之前的工作流禁用 </p>
     */
    @RequestMapping("enableFlow")
    @RestReturn(value = String.class)
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
     */
    @RequestMapping("disableFlow")
    @RestReturn(value = String.class)
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
     */
    @RequestMapping("deleteFlow")
    @RestReturn(value = String.class)
    public RestResponse deleteFlow(@Valid FlowIdCommand cmd) {
        flowService.deleteFlow(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowName</b>
     * <p> 修改工作流的属性 </p>
     */
    @RequestMapping("updateFlowName")
    @RestReturn(value = FlowDTO.class)
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
    @Deprecated
    @RequestMapping("createFlowNode")
    @RestReturn(value = FlowNodeDTO.class)
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
    @Deprecated
    @RequestMapping("deleteFlowNode")
    @RestReturn(value = FlowNodeDTO.class)
    public RestResponse deleteFlowNode(@Valid DeleteFlowNodeCommand cmd) {
        RestResponse response = new RestResponse(flowService.deleteFlowNode(cmd.getFlowNodeId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowNodes</b>
     * <p> 显示工作流下的所有节点 </p>
     */
    @Deprecated
    @RequestMapping("listFlowNodes")
    @RestReturn(value = ListBriefFlowNodeResponse.class)
    public RestResponse listFlowNodes(@Valid FlowIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.listBriefFlowNodes(cmd.getFlowId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateNodePriority</b>
     * <p> 重新修改工作流下的优先级 </p>
     */
    @Deprecated
    @RequestMapping("updateFlowNodePriority")
    @RestReturn(value = ListBriefFlowNodeResponse.class)
    public RestResponse updateNodePriority(@Valid UpdateFlowNodePriorityCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateNodePriority(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowNode</b>
     * <p>修改节点设置属性</p>
     */
    @RequestMapping("updateFlowNode")
    @RestReturn(value = FlowNodeDTO.class)
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
    @RestReturn(value = ListFlowUserSelectionResponse.class)
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
    @RestReturn(value = ListFlowButtonResponse.class)
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
    @RestReturn(value = FlowNodeDetailDTO.class)
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
    @RestReturn(value = FlowNodeDetailDTO.class)
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
    @RestReturn(value = FlowNodeDetailDTO.class)
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
    @RestReturn(value = ListFlowUserSelectionResponse.class)
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
    @RestReturn(value = FlowUserSelectionDTO.class)
    public RestResponse deleteUserSelection(@Valid DeleteFlowUserSelectionCommand cmd) {
        RestResponse response = new RestResponse(flowService.deleteUserSelection(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/getFlowButtonDetail</b>
     * <p> 获取按钮详情 </p>
     */
    @RequestMapping("getFlowButtonDetail")
    @RestReturn(value = FlowButtonDetailDTO.class)
    public RestResponse getFlowButtonDetail(@Valid GetFlowButtonDetailByIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.getFlowButtonDetail(cmd.getFlowButtonId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowButton</b>
     * <p> 修改节点按钮信息 </p>
     */
    @RequestMapping("updateFlowButton")
    @RestReturn(value = FlowButtonDetailDTO.class)
    public RestResponse updateFlowButton(@Valid UpdateFlowButtonCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowButton(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/deleteFlowButton</b>
     * <p> 删除按钮 </p>
     */
    @RequestMapping("deleteFlowButton")
    @RestReturn(value = String.class)
    public RestResponse deleteFlowButton(@Valid DeleteFlowButtonCommand cmd) {
        flowService.deleteFlowButton(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/enableFlowButton</b>
     * <p> 启用节点中的某一个按钮应用 </p>
     */
    @RequestMapping("enableFlowButton")
    @RestReturn(value = FlowButtonDTO.class)
    public RestResponse enableFlowButton(@Valid DisableFlowButtonCommand cmd) {
        RestResponse response = new RestResponse(flowService.enableFlowButton(cmd.getFlowButtonId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/disableFlowButton</b>
     * <p> 禁用节点中的某一个按钮应用 </p>
     */
    @RequestMapping("disableFlowButton")
    @RestReturn(value = FlowButtonDTO.class)
    public RestResponse disableFlowButton(@Valid DisableFlowButtonCommand cmd) {
        RestResponse response = new RestResponse(flowService.disableFlowButton(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listVariables</b>
     * <p> 显示某个位置的变量 </p>
     */
    @RequestMapping("listVariables")
    @RestReturn(value = FlowVariableResponse.class)
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
    @RestReturn(value = FlowGraphDetailDTO.class)
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
    @RestReturn(value = String.class)
    public RestResponse clearFlowCache(@Valid FlowIdCommand cmd) {
        flowService.clearFlowGraphCache(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowEvaluate</b>
     * <p> 修改工作流评分项 </p>
     */
    @RequestMapping("updateFlowEvaluate")
    @RestReturn(value = String.class)
    public RestResponse updateFlowEvaluate(@Valid UpdateFlowEvaluateCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowEvaluate(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/createFlowEvaluateItem</b>
     * <p> 新增评价项 </p>
     */
    @RequestMapping("createFlowEvaluateItem")
    @RestReturn(value = FlowEvaluateItemDTO.class)
    public RestResponse createFlowEvaluateItem(@Valid CreateFlowEvaluateItemCommand cmd) {
        FlowEvaluateItemDTO dto = flowService.createFlowEvaluateItem(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowEvaluateItem</b>
     * <p> 修改评价项 </p>
     */
    @RequestMapping("updateFlowEvaluateItem")
    @RestReturn(value = FlowEvaluateItemDTO.class)
    public RestResponse updateFlowEvaluateItem(@Valid CreateFlowEvaluateItemCommand cmd) {
        FlowEvaluateItemDTO dto = flowService.updateFlowEvaluateItem(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/deleteFlowEvaluateItem</b>
     * <p> 删除评价项 </p>
     */
    @RequestMapping("deleteFlowEvaluateItem")
    @RestReturn(value = String.class)
    public RestResponse deleteFlowEvaluateItem(@Valid DeleteFlowEvaluateItemCommand cmd) {
        flowService.deleteFlowEvaluateItem(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/getFlowEvaluate</b>
     * <p> 获取工作流的评分信息 </p>
     */
    @RequestMapping("getFlowEvaluate")
    @RestReturn(value = FlowEvaluateDetailDTO.class)
    public RestResponse getFlowEvaluate(@Valid FlowIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.getFlowEvaluate(cmd.getFlowId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listSMSTemplates</b>
     * <p>获取消息提醒里的短信模板</p>
     */
    @RequestMapping("listSMSTemplates")
    @RestReturn(value = FlowSMSTemplateResponse.class)
    public RestResponse listSMSTemplates(@Valid ListSMSTemplateCommand cmd) {
        RestResponse response = new RestResponse(flowService.listSMSTemplates(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/resolveUsers</b>
     * <p> 获取工作流的评分信息 </p>
     */
    @RequestMapping("resolveUsers")
    @RestReturn(value = FlowResolveUsersResponse.class)
    public RestResponse resolveUsers(@Valid FlowResolveUsersCommand cmd) {
        RestResponse response = new RestResponse(flowService.resolveSelectionUsers(cmd.getFlowId(), cmd.getSelectionId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/createOrUpdateFlowGraph</b>
     * <p>保存或修改流程图</p>
     */
    @RequestMapping("createOrUpdateFlowGraph")
    @RestReturn(value = FlowGraphDTO.class)
    public RestResponse createOrUpdateFlowGraph(@Valid CreateFlowGraphCommand cmd) {
        FlowGraphDTO flowGraphDTO = flowService.createOrUpdateFlowGraph(cmd);
        RestResponse response = new RestResponse(flowGraphDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/createOrUpdateFlowCondition</b>
     * <p>保存或修改条件</p>
     */
    @RequestMapping("createOrUpdateFlowCondition")
    @RestReturn(value = FlowGraphDTO.class)
    public RestResponse createOrUpdateFlowCondition(@Valid CreateFlowConditionCommand cmd) {
        FlowGraphDTO flowGraphDTO = flowService.createOrUpdateFlowCondition(cmd);
        RestResponse response = new RestResponse(flowGraphDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowValidationStatus</b>
     * <p>修改工作流校验状态</p>
     */
    @RequestMapping("updateFlowValidationStatus")
    @RestReturn(value = String.class)
    public RestResponse updateFlowValidationStatus(@Valid UpdateFlowValidationStatusCommand cmd) {
        flowService.updateFlowValidationStatus(cmd);
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
    @RestReturn(value = FlowGraphDTO.class)
    public RestResponse getFlowGraph(@Valid FlowIdCommand cmd) {
        FlowGraphDTO flowGraphDTO = flowService.getFlowGraphNew(cmd);
        RestResponse response = new RestResponse(flowGraphDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/createFlowButton</b>
     * <p>新增按钮</p>
     */
    @RequestMapping("createFlowButton")
    @RestReturn(value = FlowButtonDTO.class)
    public RestResponse createFlowButton(@Valid CreateFlowButtonCommand cmd) {
        FlowButtonDTO buttonDTO = flowService.createFlowButton(cmd);
        RestResponse response = new RestResponse(buttonDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowButtonOrder</b>
     * <p>调整按钮的顺序</p>
     */
    @RequestMapping("updateFlowButtonOrder")
    @RestReturn(value = String.class)
    public RestResponse updateFlowButtonOrder(@Valid UpdateFlowButtonOrderCommand cmd) {
        flowService.updateFlowButtonOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowLane</b>
     * <p>修改泳道属性</p>
     */
    @RequestMapping("updateFlowLane")
    @RestReturn(value = FlowLaneDTO.class)
    public RestResponse updateFlowLane(@Valid UpdateFlowLaneCommand cmd) {
        FlowLaneDTO laneDTO = flowService.updateFlowLane(cmd);
        RestResponse response = new RestResponse(laneDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listPredefinedParam</b>
     * <p>预定义的参数列表</p>
     */
    @RequestMapping("listPredefinedParam")
    @RestReturn(value = ListFlowPredefinedParamResponse.class)
    public RestResponse listPredefinedParam(@Valid ListPredefinedParamCommand cmd) {
        ListFlowPredefinedParamResponse resp = flowService.listPredefinedParam(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowConditionVariables</b>
     * <p>获取条件变量</p>
     */
    @RequestMapping("listFlowConditionVariables")
    @RestReturn(value = ListFlowConditionVariablesResponse.class)
    public RestResponse listFlowConditionVariables(@Valid ListFlowConditionVariablesCommand cmd) {
        ListFlowConditionVariablesResponse resp = flowService.listFlowConditionVariables(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowForms</b>
     * <p>表单列表</p>
     */
    @RequestMapping("listFlowForms")
    @RestReturn(value = ListFlowFormsResponse.class)
    public RestResponse listFlowForms(@Valid ListFlowFormsCommand cmd) {
        ListFlowFormsResponse resp = flowService.listFlowForms(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowFormVersion</b>
     * <p>更新表单版本号</p>
     */
    @RequestMapping("updateFlowFormVersion")
    @RestReturn(value = FlowFormDTO.class)
    public RestResponse updateFlowFormVersion(@Valid UpdateFlowFormCommand cmd) {
        FlowFormDTO resp = flowService.updateFlowFormVersion(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowFormStatus</b>
     * <p>表单开关</p>
     */
    @RequestMapping("updateFlowFormStatus")
    @RestReturn(value = String.class)
    public RestResponse updateFlowFormStatus(@Valid UpdateFlowFormStatusCommand cmd) {
        flowService.updateFlowFormStatus(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/createFlowForm</b>
     * <p>建立表单关联</p>
     */
    @RequestMapping("createFlowForm")
    @RestReturn(value = FlowFormDTO.class)
    public RestResponse createFlowForm(@Valid UpdateFlowFormCommand cmd) {
        FlowFormDTO resp = flowService.createFlowForm(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/deleteFlowForm</b>
     * <p>取消表单关联</p>
     */
    @RequestMapping("deleteFlowForm")
    @RestReturn(value = String.class)
    public RestResponse deleteFlowForm(@Valid UpdateFlowFormCommand cmd) {
        flowService.deleteFlowForm(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/getFlowForm</b>
     * <p>获取工作流关联的表单</p>
     */
    @RequestMapping("getFlowForm")
    @RestReturn(value = FlowFormDTO.class)
    public RestResponse getFlowForm(@Valid GetFlowFormCommand cmd) {
        FlowFormDTO flowFormDTO = flowService.getFlowForm(cmd);
        RestResponse response = new RestResponse(flowFormDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowScripts</b>
     * <p>获取脚本列表</p>
     */
    @RequestMapping("listFlowScripts")
    @RestReturn(value = ListFlowScriptsResponse.class)
    public RestResponse listFlowScripts(@Valid ListFlowScriptsCommand cmd) {
        ListFlowScriptsResponse res = flowService.listFlowScripts(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/createFlowScript</b>
     * <p>创建脚本</p>
     */
    @XssExclude
    @RequestMapping("createFlowScript")
    @RestReturn(value = FlowScriptDTO.class)
    public RestResponse createFlowScript(@Valid CreateFlowScriptCommand cmd) {
        FlowScriptDTO dto = flowService.createFlowScript(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/deleteFlowScript</b>
     * <p>删除脚本</p>
     */
    @RequestMapping("deleteFlowScript")
    @RestReturn(value = String.class)
    public RestResponse deleteFlowScript(@Valid DeleteFlowScriptCommand cmd) {
        flowService.deleteFlowScript(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateFlowScript</b>
     * <p>修改脚本</p>
     */
    @XssExclude
    @RequestMapping("updateFlowScript")
    @RestReturn(value = FlowScriptDTO.class)
    public RestResponse updateFlowScript(@Valid UpdateFlowScriptCommand cmd) {
        FlowScriptDTO dto = flowService.updateFlowScript(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowScriptConfigs</b>
     * <p>获取脚本配置列表</p>
     */
    /*@RequestMapping("listFlowScriptConfigs")
    @RestReturn(ListFlowScriptConfigsResponse.class)
    public DeferredResult<RestResponse> listFlowScriptConfigs(@Valid ListFlowScriptConfigsCommand cmd) {
        return flowService.listFlowScriptConfigs(cmd);
    }*/

    /**
     * <b>URL: /admin/flow/updateNeedAllProcessorComplete</b>
     * <p>修改会签开关</p>
     */
    @RequestMapping("updateNeedAllProcessorComplete")
    @RestReturn(value = String.class)
    public RestResponse updateNeedAllProcessorComplete(@Valid UpdateNeedAllProcessorCompleteCommand cmd) {
        flowService.updateNeedAllProcessorComplete(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowModuleApps</b>
     * <p>获取对接了工作流的应用</p>
     */
    @RequestMapping("listFlowModuleApps")
    @RestReturn(value = ListFlowModuleAppsResponse.class)
    public RestResponse listFlowModuleApps(@Valid ListFlowModuleAppsCommand cmd) {
        ListFlowModuleAppsResponse resp = flowService.listFlowModuleApps(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowModuleAppServiceTypes</b>
     * <p>根据应用获取业务类型</p>
     */
    @RequestMapping("listFlowModuleAppServiceTypes")
    @RestReturn(value = ListFlowModuleAppServiceTypesResponse.class)
    public RestResponse listFlowModuleAppServiceTypes(@Valid ListFlowModuleAppServiceTypesCommand cmd) {
        ListFlowModuleAppServiceTypesResponse resp = flowService.listFlowModuleAppServiceTypes(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/doFlowSnapshot</b>
     * <p>发布新版本的工作流</p>
     */
    @RequestMapping("doFlowSnapshot")
    @RestReturn(value = String.class)
    public RestResponse doFlowSnapshot(@Valid FlowIdCommand cmd) {
        flowService.doFlowSnapshot(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/createFlowServiceMapping</b>
     * <p>新增工作流与业务的关联</p>
     */
    @RequestMapping("createFlowServiceMapping")
    @RestReturn(value = String.class)
    public RestResponse createFlowServiceMapping(@Valid CreateFlowServiceMappingCommand cmd) {
        flowService.createFlowServiceMapping(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/getFlowServiceMapping</b>
     * <p>获取工作流与业务关联数据</p>
     */
    @RequestMapping("getFlowServiceMapping")
    @RestReturn(FlowServiceMappingDTO.class)
    public RestResponse getFlowServiceMapping(@Valid GetFlowServiceMappingCommand cmd) {
        FlowServiceMappingDTO dto = flowService.getFlowServiceMapping(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/listFlowServiceMappings</b>
     * <p>工作流与业务的关联列表</p>
     */
    @RequestMapping("listFlowServiceMappings")
    @RestReturn(value = FlowServiceMappingDTO.class, collection = true)
    public RestResponse listFlowServiceMappings(@Valid ListFlowServiceMappingsCommand cmd) {
        List<FlowServiceMappingDTO> list = flowService.listFlowServiceMappings(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/updateSubFlowInfo</b>
     * <p>设置子流程的信息</p>
     */
    @RequestMapping("updateSubFlowInfo")
    @RestReturn(String.class)
    public RestResponse updateSubFlowInfo(@Valid UpdateSubFlowInfoCommand cmd) {
        flowService.updateSubFlowInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/enableProjectCustomize</b>
     * <p> 启用基于园区的自定义配置 </p>
     */
    @RequestMapping("enableProjectCustomize")
    @RestReturn(value = String.class)
    public RestResponse enableProjectCustomize(@Valid EnableProjectCustomizeCommand cmd) {
        flowService.enableProjectCustomize(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/disableProjectCustomize</b>
     * <p> 禁用基于园区的自定义配置 </p>
     */
    @RequestMapping("disableProjectCustomize")
    @RestReturn(value = String.class)
    public RestResponse disableProjectCustomize(@Valid DisableProjectCustomizeCommand cmd) {
        flowService.disableProjectCustomize(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/getProjectCustomize</b>
     * <p> 获取自定义配置属性 </p>
     */
    @RequestMapping("getProjectCustomize")
    @RestReturn(value = String.class)
    public RestResponse getProjectCustomize(@Valid GetProjectCustomizeCommand cmd) {
        Byte flag = flowService.getProjectCustomize(cmd);
        RestResponse response = new RestResponse(flag);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/flow/doFlowMirror</b>
     * <p> 复制工作流 </p>
     */
    @RequestMapping("doFlowMirror")
    @RestReturn(value = String.class)
    public RestResponse doFlowMirror(@Valid DoFlowMirrorCommand cmd) {
        flowService.doFlowMirror(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
