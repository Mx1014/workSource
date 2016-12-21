package com.everhomes.techpark.expansion;

import com.everhomes.controller.ControllerBase;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.RestReturn;
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

    // @Autowired
    // private FlowNodeProvider flowNodeProvider;

    @Autowired
    private FlowButtonProvider flowButtonProvider;

    // @Autowired
    // private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    // @Autowired
    // private UserProvider userProvider;

    // @Autowired
    // private ParkingProvider parkingProvider;

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
            communityIds.forEach(this::createFlow);
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

    private void createFlow(Long communityId) {
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
        FlowDTO flowDTO = flowService.createFlow(flowCmd);

        CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
        nodeCmd.setFlowMainId(flowDTO.getId());
        nodeCmd.setNamespaceId(namespaceId);
        nodeCmd.setNodeLevel(1);
        nodeCmd.setNodeName("待受理");
        FlowNodeDTO node1 = flowService.createFlowNode(nodeCmd);

        nodeCmd = new CreateFlowNodeCommand();
        nodeCmd.setFlowMainId(flowDTO.getId());
        nodeCmd.setNamespaceId(namespaceId);
        nodeCmd.setNodeLevel(2);
        nodeCmd.setNodeName("处理中");
        FlowNodeDTO node2 = flowService.createFlowNode(nodeCmd);

        addNodeProcessor(node1, orgId);
        addNodeProcessor(node2, orgId);

        // updateNodeReminder(node1, orgId);
        // updateNodeReminder(node2, orgId);

        // updateNodeTracker(node1, orgId);
        // updateNodeTracker(node2, orgId);

        FlowButton node1ApproveStepButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
        UpdateFlowButtonCommand buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setButtonName("处理");
        buttonCmd.setDescription("处理人处理");
        buttonCmd.setFlowButtonId(node1ApproveStepButton.getId());
        buttonCmd.setNeedSubject((byte)1);
        flowService.updateFlowButton(buttonCmd);

        FlowButton node1ApplierRemindButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.REMINDER_STEP.getCode(), FlowUserType.APPLIER.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node1ApplierRemindButton.getId());
        buttonCmd.setButtonName("催办");
        buttonCmd.setDescription("催办描述");
        buttonCmd.setRemindCount(2);

        FlowActionInfo buttonAction = createActionInfo("您有一条园区入驻任务需要处理, 申请人:${applierName} ", orgId);
        buttonCmd.setMessageAction(buttonAction);
        flowService.updateFlowButton(buttonCmd);

        FlowButton node1ApplierAbsortButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.ABSORT_STEP.getCode(), FlowUserType.APPLIER.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node1ApplierAbsortButton.getId());
        buttonCmd.setButtonName("取消");
        buttonCmd.setDescription("取消描述");
        buttonCmd.setNeedSubject((byte)1);
        flowService.updateFlowButton(buttonCmd);

        FlowButton node2ApproveStepButton = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node2ApproveStepButton.getId());
        buttonCmd.setButtonName("完成");
        buttonCmd.setDescription("完成描述");
        buttonCmd.setNeedSubject((byte)1);
        flowService.updateFlowButton(buttonCmd);

        FlowButton node2ApplierRemindButton = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.REMINDER_STEP.getCode(), FlowUserType.APPLIER.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node2ApplierRemindButton.getId());
        buttonCmd.setButtonName("催办");
        buttonCmd.setDescription("催办描述");
        buttonCmd.setRemindCount(2);

        buttonAction = createActionInfo("您有一条园区入驻任务需要处理, 申请人:${applierName} ", orgId);
        buttonCmd.setMessageAction(buttonAction);
        flowService.updateFlowButton(buttonCmd);

        FlowButton node2ApplierAbsortButton = flowButtonProvider.findFlowButtonByStepType(node2.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.ABSORT_STEP.getCode(), FlowUserType.APPLIER.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node2ApplierAbsortButton.getId());
        buttonCmd.setButtonName("取消");
        buttonCmd.setDescription("取消描述");
        buttonCmd.setNeedSubject((byte)1);
        flowService.updateFlowButton(buttonCmd);

        List<FlowButton> flowButtons = flowButtonProvider.queryFlowButtons(new ListingLocator(), 100, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_BUTTONS.FLOW_NODE_ID.in(node1.getId(), node2.getId()));
            return query;
        });

        List<Long> needButtonIdList = Arrays.asList(
                node1ApplierAbsortButton.getId(),
                node1ApplierRemindButton.getId(),
                node1ApproveStepButton.getId(),
                node2ApplierAbsortButton.getId(),
                node2ApplierRemindButton.getId(),
                node2ApproveStepButton.getId()
        );

        // 把不需要的按钮关掉
        List<FlowButton> updateButtonIdList = flowButtons.stream().filter(r -> !needButtonIdList.contains(r.getId())).collect(Collectors.toList());
        for (FlowButton flowButton : updateButtonIdList) {
            flowButton.setStatus(Byte.valueOf("1"));
            flowButtonProvider.updateFlowButton(flowButton);
        }

        flowService.enableFlow(flowDTO.getId());
    }

    private void addNodeProcessor(FlowNodeDTO dto, Long orgId) {
        CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
        seleCmd.setBelongTo(dto.getId());
        seleCmd.setFlowEntityType(FlowEntityType.FLOW_NODE.getCode());
        seleCmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());

        List<FlowSingleUserSelectionCommand> sels = new ArrayList<>();
        List<Long> users = getOrgUsers(orgId);
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

    private void updateNodeReminder(FlowNodeDTO dto, Long orgId) {
        UpdateFlowNodeReminderCommand remindCmd = new UpdateFlowNodeReminderCommand();
        remindCmd.setFlowNodeId(dto.getId());
        FlowActionInfo action = createActionInfo("您收到一条车辆放行任务", orgId);
        action.setReminderAfterMinute(0L);
        remindCmd.setMessageAction(action);

        flowService.updateFlowNodeReminder(remindCmd);
    }

    private FlowActionInfo createActionInfo(String text, Long orgId) {
        FlowActionInfo action = new FlowActionInfo();
        action.setRenderText(text);

        CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
        seleCmd.setFlowEntityType(FlowEntityType.FLOW_ACTION.getCode());
        seleCmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());

        List<FlowSingleUserSelectionCommand> sels = new ArrayList<>();
        List<Long> users = this.getOrgUsers(orgId);
        for(Long u : users) {
            FlowSingleUserSelectionCommand singCmd = new FlowSingleUserSelectionCommand();
            singCmd.setSourceIdA(u);
            singCmd.setFlowUserSelectionType(FlowUserSelectionType.DEPARTMENT.getCode());
            singCmd.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
            sels.add(singCmd);
        }
        seleCmd.setSelections(sels);
        action.setUserSelections(seleCmd);

        return action;
    }

    private List<Long> getOrgUsers(Long id) {
        List<Long> users = new ArrayList<>();
        users.add(testUser1.getId());
        return users;
    }
}
