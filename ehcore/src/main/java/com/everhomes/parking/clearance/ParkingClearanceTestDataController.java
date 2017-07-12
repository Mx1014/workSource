package com.everhomes.parking.clearance;

import com.everhomes.controller.ControllerBase;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.parking.ParkingLot;
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

@RestDoc(value = "Parking clearance test data controller", site = "core")
@RestController
@RequestMapping("/clearance/test")
public class ParkingClearanceTestDataController extends ControllerBase {

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

    /**
     * <p>初始化工作流数据</p>
     * <b>URL: /clearance/test/initData</b>
     */
    @RequestMapping("initData")
    @RestReturn(String.class)
    public RestResponse initData(InitTestFlowDataCommand cmd) {
        RestResponse response = new RestResponse();
        try {
            initFlowData(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseObject(e);
        }
        return response;
    }

    /**
     * <p>获取初始化工作流数据</p>
     * <b>URL: /clearance/test/listData</b>
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
     * <b>URL: /clearance/test/deleteData</b>
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

    private User testUser1;
    private Integer namespaceId = 999984;// 999984
    private Long moduleId = 20900L;
    private Long orgId = 1008218L;// 1008218L
    private String flowName = "车辆放行工作流";
    private String phoneNumber = "13600161256";


    public void init() {
        testUser1 = userService.findUserByIndentifier(namespaceId, phoneNumber);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<ParkingLot> parkingLots = context.select().from(Tables.EH_PARKING_LOTS)
                .where(Tables.EH_PARKING_LOTS.NAMESPACE_ID.eq(namespaceId)).fetchInto(ParkingLot.class);
        if (parkingLots != null) {
            parkingLots.forEach(this::flowData);
        }
    }

    private void initFlowData(InitTestFlowDataCommand cmd){
        if (cmd.getNamespaceId() != null) {
            this.namespaceId = cmd.getNamespaceId();
        }
        if (cmd.getIdentifierToken() != null) {
            this.phoneNumber = cmd.getIdentifierToken();
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

    private void flowData(ParkingLot parkingLot) {
        FlowDTO flowDTO = createFlow(parkingLot);

        FlowNodeDTO node1 = createFlowNode(flowDTO.getId(), "待处理", "{\"node\":\"PENDING\"}", 1);
        FlowNodeDTO node2 = createFlowNode(flowDTO.getId(), "处理中", null, 2);
        FlowNodeDTO node3 = createFlowNode(flowDTO.getId(), "处理完成", null, 3);

        updateFlowNode(node3);

        addNodeProcessor(node1);
        addNodeProcessor(node2);
        addNodeProcessor(node3);

        FlowActionInfo promptActionInfo   = createPromptApplierActionInfo("您的车辆放行任务已被处理, 处理人${currProcessor}");
        FlowActionInfo completeActionInfo = createPromptApplierActionInfo("您的车辆放行任务已完成, 处理人${currProcessor}");

        FlowButton node1ApproveStepButton   = createButton(node1, "受理", FlowStepType.APPROVE_STEP, FlowUserType.PROCESSOR, promptActionInfo, 1);
        FlowButton node1ApplierAbortButton  = createButton(node1, "取消", FlowStepType.ABSORT_STEP, FlowUserType.APPLIER, null, 1);

        FlowButton node2ApproveStepButton   = createButton(node2, "完成", FlowStepType.APPROVE_STEP, FlowUserType.PROCESSOR, completeActionInfo, 1);
        FlowButton node2ApplierAbortButton  = createButton(node2, "取消", FlowStepType.ABSORT_STEP, FlowUserType.APPLIER, null, 1);

        List<FlowButton> flowButtons = flowButtonProvider.queryFlowButtons(new ListingLocator(), 100, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_BUTTONS.FLOW_NODE_ID.in(node1.getId(), node2.getId()));
            return query;
        });

        List<Long> needButtonIdList = Arrays.asList(
                node1ApplierAbortButton.getId(),
                node1ApproveStepButton.getId(),
                node2ApplierAbortButton.getId(),
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

    private void updateFlowNode(FlowNodeDTO node) {
        UpdateFlowNodeCommand cmd = new UpdateFlowNodeCommand();
        cmd.setAllowTimeoutAction((byte)1);
        cmd.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
        cmd.setFlowNodeId(node.getId());
        flowService.updateFlowNode(cmd);
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

    private FlowNodeDTO createFlowNode(Long flowId, String nodeName, String params, int nodeLevel) {
        CreateFlowNodeCommand nodeCmd = new CreateFlowNodeCommand();
        nodeCmd.setFlowMainId(flowId);
        nodeCmd.setNamespaceId(namespaceId);
        nodeCmd.setNodeLevel(nodeLevel);
        nodeCmd.setNodeName(nodeName);
        FlowNodeDTO flowNode = flowService.createFlowNode(nodeCmd);

        if (params != null) {
            UpdateFlowNodeCommand updateFlowNodeCommand = new UpdateFlowNodeCommand();
            updateFlowNodeCommand.setParams(params);
            updateFlowNodeCommand.setFlowNodeId(flowNode.getId());
            flowService.updateFlowNode(updateFlowNodeCommand);
        }
        return flowNode;
    }

    private FlowDTO createFlow(ParkingLot parkingLot) {
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
        flowCmd.setOwnerId(parkingLot.getId());
        flowCmd.setOwnerType(FlowOwnerType.PARKING.getCode());
        flowCmd.setProjectType(EntityType.COMMUNITY.getCode());
        flowCmd.setProjectId(parkingLot.getOwnerId());
        return flowService.createFlow(flowCmd);
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
