package com.everhomes.parking.clearance;

import com.everhomes.controller.ControllerBase;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.flow.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.group.GetClubPlaceholderNameCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestDoc(value = "Parking clearance test data controller", site = "core")
@RestController
@RequestMapping("/clearance/test")
public class ParkingClearanceTestDataController extends ControllerBase {

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

    /**
     * <p>初始化工作流数据</p>
     * <b>URL: /clearance/test/initData</b>
     */
    @RequestMapping("initData")
    @RestReturn(String.class)
    public RestResponse initData(GetClubPlaceholderNameCommand cmd) {
        RestResponse response = new RestResponse();
        try {
            initFlowData(cmd.getNamespaceId());
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseObject(e);
        }
        return response;
    }

    private User testUser1;
    private Integer namespaceId = 999984;// 999984
    private Long moduleId = 20900L;
    private Long orgId = 1008218L;// 1008218L
    private List<Long> parkingLotIds;

    public void init() {
        String u1 = "13600161256";
        testUser1 = userService.findUserByIndentifier(namespaceId, u1);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        parkingLotIds = context.select(Tables.EH_PARKING_LOTS.ID).from(Tables.EH_PARKING_LOTS)
                .where(Tables.EH_PARKING_LOTS.NAMESPACE_ID.eq(namespaceId)).fetchInto(Long.class);
    }

    private void initFlowData(Integer namespaceId){
        if (namespaceId != null) {
            this.namespaceId = namespaceId;
        }
        init();
        if (parkingLotIds != null) {
            parkingLotIds.forEach(this::flowData);
        }
    }

    private void flowData(Long parkingLotId) {
        String flowName = "车辆放行工作流";

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
        flowCmd.setOwnerId(parkingLotId);
        flowCmd.setOwnerType(FlowOwnerType.PARKING.getCode());
        FlowDTO flowDTO = flowService.createFlow(flowCmd);

        CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
        nodeCmd.setFlowMainId(flowDTO.getId());
        nodeCmd.setNamespaceId(namespaceId);
        nodeCmd.setNodeLevel(1);
        nodeCmd.setNodeName("等待处理");
        FlowNodeDTO node1 = flowService.createFlowNode(nodeCmd);

        UpdateFlowNodeCommand updateFlowNodeCommand = new UpdateFlowNodeCommand();
        updateFlowNodeCommand.setParams("{\"code\":1, \"status\":\"PROCESSING\"}");
        updateFlowNodeCommand.setFlowNodeId(node1.getId());
        flowService.updateFlowNode(updateFlowNodeCommand);

        nodeCmd = new CreateFlowNodeCommand();
        nodeCmd.setFlowMainId(flowDTO.getId());
        nodeCmd.setNamespaceId(namespaceId);
        nodeCmd.setNodeLevel(2);
        nodeCmd.setNodeName("正在处理");
        FlowNodeDTO node2 = flowService.createFlowNode(nodeCmd);

        updateFlowNodeCommand = new UpdateFlowNodeCommand();
        updateFlowNodeCommand.setParams("{\"code\":1, \"status\":\"PROCESSING\"}");
        updateFlowNodeCommand.setFlowNodeId(node2.getId());
        flowService.updateFlowNode(updateFlowNodeCommand);

        // addNodeProcessor(node1, orgId);
        // addNodeProcessor(node2, orgId);

        // updateNodeReminder(node1, orgId);
        // updateNodeReminder(node2, orgId);

        // updateNodeTracker(node1, orgId);
        // updateNodeTracker(node2, orgId);

        FlowButton node1ApproveStepButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.APPROVE_STEP.getCode(), FlowUserType.PROCESSOR.getCode());
        UpdateFlowButtonCommand buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setButtonName("受理");
        buttonCmd.setDescription("我要处理");
        buttonCmd.setFlowButtonId(node1ApproveStepButton.getId());
        buttonCmd.setNeedProcessor((byte)1);
        buttonCmd.setNeedSubject((byte)1);
        flowService.updateFlowButton(buttonCmd);

        FlowButton node1ApplierRemindButton = flowButtonProvider.findFlowButtonByStepType(node1.getId(), FlowConstants.FLOW_CONFIG_VER
                , FlowStepType.REMINDER_STEP.getCode(), FlowUserType.APPLIER.getCode());
        buttonCmd = new UpdateFlowButtonCommand();
        buttonCmd.setFlowButtonId(node1ApplierRemindButton.getId());
        buttonCmd.setButtonName("催办");
        buttonCmd.setDescription("催办描述");
        buttonCmd.setRemindCount(2);

        FlowActionInfo buttonAction = createActionInfo("您有一条车辆放行任务需要处理, 申请人:${applierName} ", orgId);
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

        buttonAction = createActionInfo("您有一条车辆放行任务需要处理, 申请人:${applierName} ", orgId);
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

        // action = createActionInfo("催办:tick:节点id:" + dto.getId(), orgId);
        // action.setReminderTickMinute(0L);
        // remindCmd.setTickMessageAction(action);
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

    /*private FlowActionInfo createApplierActionInfo(String text, Long orgId) {
        FlowActionInfo action = new FlowActionInfo();
        action.setRenderText(text);

        CreateFlowUserSelectionCommand seleCmd = new CreateFlowUserSelectionCommand();
        seleCmd.setFlowEntityType(FlowEntityType.FLOW_ACTION.getCode());
        seleCmd.setFlowUserType(FlowUserType.APPLIER.getCode());

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
    }*/

    private List<Long> getOrgUsers(Long id) {
        List<Long> users = new ArrayList<>();
        users.add(testUser1.getId());
        return users;
    }
}
