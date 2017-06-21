package com.everhomes.techpark.expansion;

import com.everhomes.controller.ControllerBase;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.techpark.expansion.InitTestFlowDataCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/techpark/entry/test")
public class EnterpriseApplyEntryTestDataController extends ControllerBase {

    @Autowired
    public FlowProvider flowProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowButtonProvider flowButtonProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private DbProvider dbProvider;


    private User testUser1;
    private Integer namespaceId = 1000000;
    private Long moduleId = 40100L;
    private Long orgId = 1000001L;
    private String identifierToken = "13632650699";

    private String flowName = "园区入驻工作流";


    /**
     * <p>初始化工作流数据</p>
     * <b>URL: /techpark/entry/test/initData</b>
     */
    @RequestMapping("initData")
    @RestReturn(String.class)
    public RestResponse initData(InitTestFlowDataCommand cmd) {
        RestResponse response = new RestResponse();
        try {
            initFlowData(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseObject(Arrays.asList(e.getMessage(), e.getStackTrace()).toString());
        }
        return response;
    }

    /**
     * <p>获取初始化工作流数据</p>
     * <b>URL: /techpark/entry/test/listData</b>
     */
    @RequestMapping("listData")
    @RestReturn(value = String.class)
    public RestResponse listData(InitTestFlowDataCommand cmd) {
        RestResponse response = new RestResponse();
        try {
            List<Flow> flows = flowProvider.queryFlows(new ListingLocator(), 1000, (locator, query) -> {
                query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
                query.addConditions(Tables.EH_FLOWS.FLOW_NAME.eq(flowName));
                return query;
            });
            response.setResponseObject(StringHelper.toJsonString(flows));
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseObject(Arrays.asList(e.getMessage(), e.getStackTrace()).toString());
        }
        return response;
    }

    /**
     * <p>删除初始化工作流数据</p>
     * <b>URL: /techpark/entry/test/deleteData</b>
     */
    @RequestMapping("deleteData")
    @RestReturn(String.class)
    public RestResponse deleteData(InitTestFlowDataCommand cmd) {
        RestResponse response = new RestResponse();
        try {
            List<Flow> flows = flowProvider.queryFlows(new ListingLocator(), 1000, (locator, query) -> {
                query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
                query.addConditions(Tables.EH_FLOWS.FLOW_NAME.eq(flowName));
                return query;
            });
            if (flows != null) {
                for (Flow flow : flows) {
                    flowProvider.deleteFlow(flow);
                }
            } else {
                response.setErrorDescription("Flows list is empty!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseObject(Arrays.asList(e.getMessage(), e.getStackTrace()).toString());
        }
        return response;
    }

    public void init() {
        testUser1 = userService.findUserByIndentifier(namespaceId, identifierToken);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> communityIds = context.select(Tables.EH_COMMUNITIES.ID).from(Tables.EH_COMMUNITIES)
                .where(Tables.EH_COMMUNITIES.NAMESPACE_ID.eq(namespaceId)).fetchInto(Long.class);
        if (communityIds != null) {
            communityIds.forEach(this::createWorkFlowData);
        }
    }

    private void initFlowData(InitTestFlowDataCommand cmd){
        if (cmd.getNamespaceId() != null) {
            this.namespaceId = cmd.getNamespaceId();
        }
        if (cmd.getIdentifierToken() != null) {
            this.identifierToken = cmd.getIdentifierToken();
        }
        if (cmd.getModuleId() != null) {
            this.moduleId = cmd.getModuleId();
        }
        if (cmd.getOrganizationId() != null) {
            this.orgId = cmd.getOrganizationId();
        }
        dbProvider.execute(status -> {
            init();
            return true;
        });
    }

    private void createWorkFlowData(Long communityId) {
        FlowDTO flowDTO = createFlow(communityId);

        FlowNodeDTO node1 = createFlowNode(flowDTO.getId(), "待处理", 1);
        FlowNodeDTO node2 = createFlowNode(flowDTO.getId(), "处理中", 2);
        FlowNodeDTO node3 = createFlowNode(flowDTO.getId(), "已联系", 3);
        FlowNodeDTO node4 = createFlowNode(flowDTO.getId(), "已处理", 4);

        updateFlowNode(node4);

        updateFlowEvaluate(flowDTO);

        addNodeProcessor(node1);
        addNodeProcessor(node2);
        addNodeProcessor(node3);
        addNodeProcessor(node4);

        FlowActionInfo reminderActionInfo = createReminderProcessorActionInfo("您有一条园区入驻任务需要处理, 申请人:${applierName}");
        FlowActionInfo promptActionInfo   = createPromptApplierActionInfo("您的园区入驻任务有新的进展了,处理人:${currProcessor}");
        FlowActionInfo completeActionInfo = createPromptApplierActionInfo("您的园区入驻任务已完成,处理人:${currProcessor}");

        FlowButton node1ApproveStepButton   = createButton(node1, "受理", FlowStepType.APPROVE_STEP, FlowUserType.PROCESSOR, promptActionInfo,  1);
        FlowButton node1ApplierRemindButton = createButton(node1, "催办", FlowStepType.REMINDER_STEP, FlowUserType.APPLIER, reminderActionInfo, 0);
        FlowButton node1ApplierAbortButton  = createButton(node1, "取消", FlowStepType.ABSORT_STEP, FlowUserType.APPLIER, null, 1);

        FlowButton node2ApproveStepButton   = createButton(node2, "下一步", FlowStepType.APPROVE_STEP, FlowUserType.PROCESSOR, promptActionInfo, 1);
        FlowButton node2ApplierRemindButton = createButton(node2, "催办", FlowStepType.REMINDER_STEP, FlowUserType.APPLIER, reminderActionInfo, 0);
        FlowButton node2ApplierAbortButton  = createButton(node2, "取消", FlowStepType.ABSORT_STEP, FlowUserType.APPLIER, null, 1);

        FlowButton node3ApproveStepButton   = createButton(node3, "完成", FlowStepType.APPROVE_STEP, FlowUserType.PROCESSOR, completeActionInfo, 1);
        FlowButton node3ApplierRemindButton = createButton(node3, "催办", FlowStepType.REMINDER_STEP, FlowUserType.APPLIER, reminderActionInfo, 0);
        FlowButton node3ApplierAbortButton  = createButton(node3, "取消", FlowStepType.ABSORT_STEP, FlowUserType.APPLIER, null, 1);

        List<FlowButton> flowButtons = flowButtonProvider.queryFlowButtons(new ListingLocator(), 100, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_BUTTONS.FLOW_NODE_ID.in(node1.getId(), node2.getId(), node3.getId()));
            return query;
        });

        List<Long> needButtonIdList = Arrays.asList(
                node1ApplierAbortButton.getId(),
                node1ApplierRemindButton.getId(),
                node1ApproveStepButton.getId(),
                node2ApplierAbortButton.getId(),
                node2ApplierRemindButton.getId(),
                node2ApproveStepButton.getId(),
                node3ApproveStepButton.getId(),
                node3ApplierRemindButton.getId(),
                node3ApplierAbortButton.getId()
        );

        // 把不需要的按钮关掉
        List<FlowButton> updateButtonIdList = flowButtons.stream().filter(r -> !needButtonIdList.contains(r.getId())).collect(Collectors.toList());
        for (FlowButton flowButton : updateButtonIdList) {
            flowButton.setStatus(Byte.valueOf("1"));
            flowButtonProvider.updateFlowButton(flowButton);
        }

        flowService.enableFlow(flowDTO.getId());
    }

    private void updateFlowEvaluate(FlowDTO flowDTO) {
        UpdateFlowEvaluateCommand cmd = new UpdateFlowEvaluateCommand();
        cmd.setNeedEvaluate((byte)1);
        cmd.setFlowId(flowDTO.getId());
        cmd.setEvaluateStart(4L);
        cmd.setEvaluateEnd(4L);
        cmd.setEvaluateStep(FlowStepType.NO_STEP.getCode());
        // cmd.setItems(Collections.singletonList("入驻评价"));
        flowService.updateFlowEvaluate(cmd);
    }

    private FlowButton createButton(FlowNodeDTO node1, String buttonName, FlowStepType stepType, FlowUserType userType, FlowActionInfo actionInfo, int needSubject) {
        FlowButton button = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER,
                stepType.getCode(), userType.getCode());
        UpdateFlowButtonCommand buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setButtonName(buttonName);
        buttonCmd.setDescription(buttonName + "描述");
        buttonCmd.setFlowButtonId(button.getId());
        buttonCmd.setNeedSubject((byte)needSubject);
        buttonCmd.setMessageAction(actionInfo);
        if (stepType == FlowStepType.REMINDER_STEP) {
            buttonCmd.setRemindCount(2);
        }
        flowService.updateFlowButton(buttonCmd);
        return button;
    }

    private FlowDTO createFlow(Long communityId) {
        Flow flow = flowProvider.findFlowByName(namespaceId, moduleId, null, orgId, FlowOwnerType.PARKING.getCode(), flowName);
        if(flow != null) {
            flowService.disableFlow(flow.getId());
            flowService.deleteFlow(flow.getId());
        }

        CreateFlowCommand flowCmd = new CreateFlowCommand();
        flowCmd.setFlowName(flowName);
        flowCmd.setModuleId(moduleId);
        flowCmd.setNamespaceId(namespaceId);
        flowCmd.setOrgId(orgId);
        flowCmd.setOwnerId(communityId);
        flowCmd.setOwnerType(FlowOwnerType.COMMUNITY.getCode());
        flowCmd.setProjectId(communityId);
        flowCmd.setProjectType(EntityType.COMMUNITY.getCode());
        return flowService.createFlow(flowCmd);
    }

    private void updateFlowNode(FlowNodeDTO node) {
        UpdateFlowNodeCommand cmd = new UpdateFlowNodeCommand();
        cmd.setAllowTimeoutAction((byte)1);
        cmd.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
        cmd.setFlowNodeId(node.getId());
        flowService.updateFlowNode(cmd);
    }

    private FlowNodeDTO createFlowNode(Long flowId, String nodeName, int nodeLevel) {
        CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
        nodeCmd.setFlowMainId(flowId);
        nodeCmd.setNamespaceId(namespaceId);
        nodeCmd.setNodeLevel(nodeLevel);
        nodeCmd.setNodeName(nodeName);
        return flowService.createFlowNode(nodeCmd);
    }

    private void addNodeProcessor(FlowNodeDTO dto) {
        CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
        seleCmd.setBelongTo(dto.getId());
        seleCmd.setFlowEntityType(FlowEntityType.FLOW_NODE.getCode());
        seleCmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());

        List<FlowSingleUserSelectionCommand> sels = new ArrayList<>();
        List<Long> users = getOrgUsers();
        for(Long u : users) {
            FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
            singCmd.setSourceIdA(u);
            singCmd.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
            singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
            sels.add(singCmd);
        }
        seleCmd.setSelections(sels);
        flowService.createFlowUserSelection(seleCmd);
    }

    private FlowActionInfo createReminderProcessorActionInfo(String text) {
        FlowActionInfo action = new FlowActionInfo();
        action.setRenderText(text);

        CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
        seleCmd.setFlowEntityType(FlowEntityType.FLOW_BUTTON.getCode());
        seleCmd.setFlowUserType(FlowUserType.APPLIER.getCode());

        List<FlowSingleUserSelectionCommand> sels = new ArrayList<>();
        FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
        singCmd.setSourceIdA(2002L);
        singCmd.setFlowUserSelectionType(FlowUserSelectionType.VARIABLE.getCode());
        singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_VARIABLE.getCode());
        sels.add(singCmd);
        seleCmd.setSelections(sels);
        action.setUserSelections(seleCmd);
        return action;
    }

    private FlowActionInfo createPromptApplierActionInfo(String text) {
        FlowActionInfo action = new FlowActionInfo();
        action.setRenderText(text);

        CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
        seleCmd.setFlowEntityType(FlowEntityType.FLOW_BUTTON.getCode());
        seleCmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());

        List<FlowSingleUserSelectionCommand> sels = new ArrayList<>();
        FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
        singCmd.setSourceIdA(2000L);
        singCmd.setFlowUserSelectionType(FlowUserSelectionType.VARIABLE.getCode());
        singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_VARIABLE.getCode());
        sels.add(singCmd);
        seleCmd.setSelections(sels);
        action.setUserSelections(seleCmd);
        return action;
    }

    private List<Long> getOrgUsers() {
        List<Long> users = new ArrayList<>();
        users.add(testUser1.getId());
        return users;
    }
}
