package com.everhomes.flow;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.flow.event.FlowGraphAutoStepEvent;
import com.everhomes.flow.event.FlowGraphButtonEvent;
import com.everhomes.flow.event.FlowGraphNoStepEvent;
import com.everhomes.flow.event.FlowGraphStartEvent;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.FlowCaseDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.tables.pojos.EhFlowAttachments;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FlowStateProcessorImpl implements FlowStateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowStateProcessorImpl.class);

    @Autowired
    private FlowListenerManager flowListenerManager;

    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowNodeProvider flowNodeProvider;

    @Autowired
    private FlowSubjectProvider flowSubjectProvider;

    @Autowired
    private AttachmentProvider attachmentProvider;

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private FlowEventLogProvider flowEventLogProvider;

    @Autowired
    private FlowServiceMappingProvider flowServiceMappingProvider;

    @Autowired
    private FlowProvider flowProvider;

    private ThreadPoolTaskScheduler scheduler;

    public FlowStateProcessorImpl() {
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(3);
    }

//	private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public FlowCaseState prepareStart(UserInfo logonUser, FlowCase flowCase) {
        FlowCaseState ctx = new FlowCaseState();
        if (flowCase == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS, "flowcase noexists");
        }

        ctx.setFlowCase(flowCase);
        ctx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));

        FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        ctx.setFlowGraph(flowGraph);

        FlowGraphStartEvent event = new FlowGraphStartEvent();
        ctx.setCurrentEvent(event);
        ctx.setOperator(logonUser);

        if (flowGraph.getLanes().size() > 0) {
            FlowGraphLane currentLane = flowGraph.getLanes().get(0);
            ctx.setCurrentLane(currentLane);
        } else {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_LANE_NOT_EXISTS,
                    "flowLane not exists, flowLaneId=" + flowCase.getCurrentLaneId());
        }
        flowService.createSnapshotSupervisors(ctx);
        flowListenerManager.onFlowCaseStart(ctx);

        return ctx;
    }

    @Override
    public FlowCaseState prepareBranchFlowCaseStart(UserInfo logonUser, FlowCase flowCase) {
        FlowCaseState ctx = new FlowCaseState();
        if (flowCase == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS, "flowcase noexists");
        }

        ctx.setFlowCase(flowCase);
        ctx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));

        FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        ctx.setFlowGraph(flowGraph);

        FlowGraphStartEvent event = new FlowGraphStartEvent();
        ctx.setCurrentEvent(event);
        ctx.setOperator(logonUser);
        return ctx;
    }

    @Override
    public FlowCaseState prepareStepTimeout(FlowTimeout ft) {
        FlowAutoStepDTO stepDTO = (FlowAutoStepDTO) StringHelper.fromJsonString(ft.getJson(), FlowAutoStepDTO.class);
        return prepareAutoStep(stepDTO);
    }

    @Override
    public FlowCaseState prepareAutoStep(FlowAutoStepDTO stepDTO) {
        FlowCaseState ctx = new FlowCaseState();

        FlowCase flowCase = flowCaseProvider.getFlowCaseById(stepDTO.getFlowCaseId());
        if (flowCase.getStepCount().equals(stepDTO.getStepCount())
                && stepDTO.getFlowNodeId().equals(flowCase.getCurrentNodeId())) {

            User user = null;
            if (stepDTO.getOperatorId() != null) {
                //force to set context user
                user = userProvider.findUserById(stepDTO.getOperatorId());
                UserContext.current().setUser(user);
                UserContext.current().setNamespaceId(flowCase.getNamespaceId());
            } else if (UserContext.current().getUser() != null && UserContext.current().getUser().getId() != User.ANNONYMOUS_UID) {
                user = UserContext.current().getUser();
            } else {
                user = userProvider.findUserById(User.SYSTEM_UID);
                UserContext.current().setUser(user);
                UserContext.current().setNamespaceId(flowCase.getNamespaceId());
            }

            if (stepDTO.getEventLogs() != null) {
                ctx.getLogs().addAll(stepDTO.getEventLogs());
            }

            ctx.setFlowCase(flowCase);
            ctx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));

            FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
            ctx.setFlowGraph(flowGraph);

            FlowGraphNode node = flowGraph.getGraphNode(flowCase.getCurrentNodeId());
            if (node == null) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "flownode noexists");
            }
            ctx.setCurrentNode(node);

            FlowGraphLane currentLane = flowGraph.getGraphLane(flowCase.getCurrentLaneId());
            if (currentLane == null) {
                currentLane = flowGraph.getGraphLane(ctx.getCurrentNode().getFlowNode().getNodeLevel());
            }
            if (currentLane == null) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_LANE_NOT_EXISTS,
                        "flowLane not exists, flowLaneId=" + flowCase.getCurrentLaneId());
            }
            ctx.setCurrentLane(currentLane);

            UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(user.getId());//current user
            ctx.setOperator(userInfo);
            FlowGraphAutoStepEvent event = new FlowGraphAutoStepEvent(stepDTO);
            event.setFiredUserId(user.getId());
            if (stepDTO.getSubjectId() != null) {
                event.setSubject(flowSubjectProvider.getFlowSubjectById(stepDTO.getSubjectId()));
            }
            ctx.setCurrentEvent(event);

            FlowStepType stepType = FlowStepType.fromCode(stepDTO.getAutoStepType());
            if (stepType != null) {
                ctx.setStepType(stepType);
            }

            // parent ctx
            if (flowCase.getParentId() != 0) {
                prepareParentState(ctx, userInfo, flowCase, event);
            }
            // child ctx
            prepareChildState(ctx.getRootState(), userInfo, flowCase, event);

            return ctx;
        }
        return null;
    }

    @Override
    public FlowCaseState prepareNoStep(FlowAutoStepDTO stepDTO) {
        FlowCaseState ctx = new FlowCaseState();
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(stepDTO.getFlowCaseId());

        if (stepDTO.getEventLogs() != null) {
            ctx.getLogs().addAll(stepDTO.getEventLogs());
        }

        User user;
        if (stepDTO.getOperatorId() != null) {
            user = userProvider.findUserById(stepDTO.getOperatorId());
            //force to set context user
            UserContext.current().setUser(user);
            UserContext.current().setNamespaceId(flowCase.getNamespaceId());
        } else if (UserContext.current().getUser() != null) {
            user = UserContext.current().getUser();
        } else {
            user = userProvider.findUserById(User.SYSTEM_UID);
            UserContext.current().setUser(user);
            UserContext.current().setNamespaceId(flowCase.getNamespaceId());
        }

        //Important, never update this to database!!!
        flowCase.setCurrentNodeId(stepDTO.getFlowNodeId());
        flowCase.setStepCount(stepDTO.getStepCount());

        ctx.setFlowCase(flowCase);
        ctx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));

        FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        ctx.setFlowGraph(flowGraph);
        FlowGraphNode node = null;
        if (flowCase.getCurrentNodeId() == null) {
            node = flowGraph.getNodes().get(0);
        } else {
            node = flowGraph.getGraphNode(flowCase.getCurrentNodeId());
        }
        if (node == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "flownode noexists");
        }
        ctx.setCurrentNode(node);

        FlowGraphLane currentLane = flowGraph.getGraphLane(flowCase.getCurrentLaneId());
        if (currentLane == null) {
            currentLane = flowGraph.getGraphLane(ctx.getCurrentNode().getFlowNode().getNodeLevel());
        }
        if (currentLane == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_LANE_NOT_EXISTS,
                    "flowLane not exists, flowLaneId=" + flowCase.getCurrentLaneId());
        }
        ctx.setCurrentLane(currentLane);

        if (stepDTO.getTargetNodeId() != null) {
            FlowGraphNode targetNode = flowGraph.getGraphNode(stepDTO.getTargetNodeId());
            ctx.setNextNode(targetNode);
        }

        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(user.getId());
        flowService.fixupUserInfoInContext(ctx, userInfo);
        ctx.setOperator(userInfo);
        FlowGraphNoStepEvent event = new FlowGraphNoStepEvent(stepDTO);
        ctx.setCurrentEvent(event);
        ctx.setStepType(FlowStepType.NO_STEP);

        // parent ctx
        if (flowCase.getParentId() != 0) {
            prepareParentState(ctx, userInfo, flowCase, event);
        }
        // child ctx
        prepareChildState(ctx.getRootState(), userInfo, flowCase, event);

        return ctx;
    }

    @Override
    public void rejectToNode(FlowCaseState ctx, Integer gotoLevel, FlowGraphNode current) {
        List<FlowGraphLink> linksIn = current.getLinksIn();
        for (FlowGraphLink link : linksIn) {
            Map<FlowEventLog, FlowCaseState> logToFlowCaseStateMap = getFlowEventLog(ctx, link);
            for (Map.Entry<FlowEventLog, FlowCaseState> entry : logToFlowCaseStateMap.entrySet()) {
                FlowEventLog log = entry.getKey();
                FlowCaseState logCtx = entry.getValue();
                FlowCase flowCase = logCtx.getFlowCase();

                current = logCtx.getFlowGraph().getGraphNode(log.getFlowNodeId());

                if (FlowNodeType.fromCode(current.getFlowNode().getNodeType()) == FlowNodeType.CONDITION_FRONT) {
                    List<FlowCase> allFlowCases = logCtx.getAllFlowCases();
                    for (FlowGraphLink graphLink : current.getLinksOut()) {
                        if (!Objects.equals(flowCase.getStartLinkId(), graphLink.getFlowLink().getId())) {
                            final FlowGraphNode tmpCurrent = current;
                            allFlowCases.stream()
                                    .filter(r -> r.getStartNodeId().equals(tmpCurrent.getFlowNode().getId()))
                                    .filter(r -> r.getStartLinkId().equals(graphLink.getFlowLink().getId()))
                                    .findFirst()
                                    .ifPresent(r -> r.setStepCount(r.getStepCount() + 1));
                        }
                    }
                }
                if ((current.getFlowNode().getNodeLevel().equals(gotoLevel) || gotoLevel == 0)
                        && FlowNodeType.fromCode(current.getFlowNode().getNodeType()) != FlowNodeType.CONDITION_FRONT) {
                    logCtx.setNextNode(current);
                    logCtx.setStepType(FlowStepType.REJECT_STEP);
                    flowCase.incrStepCount();
                } else {
                    rejectToNode(logCtx, gotoLevel, current);
                }
            }
        }
    }

    @Override
    public boolean allProcessorCompleteInCurrentNode(FlowCaseState ctx, FlowGraphNode currentNode, UserInfo firedUser) {
        List<FlowEventLog> enterSteps = flowEventLogProvider.findCurrentNodeEnterLogs(currentNode.getFlowNode().getId(),
                ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
        boolean allComplete = true;
        for (FlowEventLog enterStep : enterSteps) {
            if (!Objects.equals(enterStep.getFlowUserId(), firedUser.getId())
                    && Objects.equals(enterStep.getEnterLogCompleteFlag(), TrueOrFalseFlag.FALSE.getCode())) {
                allComplete = false;
                break;
            }
        }
        return allComplete;
    }

    @Override
    public void startStepEnter(FlowCaseState ctx, FlowGraphNode from) {
        // 子流程进入的时候, 添加任务跟踪
        Long subFlowParentId = ctx.getFlowCase().getSubFlowParentId();
        if (!Objects.equals(subFlowParentId, 0L)) {
            FlowEventLog log = new FlowEventLog();
            log.setId(flowEventLogProvider.getNextId());
            Flow flow = ctx.getFlowGraph().getFlow();
            log.setFlowMainId(flow.getFlowMainId());
            log.setFlowVersion(flow.getFlowVersion());
            log.setNamespaceId(flow.getNamespaceId());
            log.setFlowNodeId(ctx.getCurrentNode().getFlowNodeId());
            log.setParentId(0L);
            log.setFlowCaseId(ctx.getFlowCase().getId());
            log.setFlowUserId(ctx.getOperator().getId());
            log.setFlowUserName(ctx.getOperator().getNickName());
            ctx.pushProcessType(FlowCaseStateStackType.TRACKER_ACTION);

            Map<String, Object> map = new HashMap<>(1);
            // map.put("serviceName", mapping.getDisplayName());

            log.setLogContent(flowService.templateRender(FlowTemplateCode.DEFAULT_SUB_FLOW_START_TRACKER_TEXT, map));
            ctx.popProcessType();
            log.setStepCount(ctx.getFlowCase().getStepCount());
            log.setLogType(FlowLogType.NODE_TRACKER.getCode());
            log.setTrackerApplier(1L);
            log.setTrackerProcessor(1L);
            log.setSubjectId(0L);

            FlowCaseDetailActionData actionData = new FlowCaseDetailActionData();
            actionData.setFlowCaseId(subFlowParentId);
            actionData.setModuleId(ctx.getModuleId());
            actionData.setFlowUserType(FlowUserType.PROCESSOR.getCode());

            String uri = RouterBuilder.build(Router.WORKFLOW_DETAIL, actionData);
            log.setExtra(String.format("{\"route\":\"%s\", \"subFlowParentId\":%s}", uri, subFlowParentId));
            ctx.getLogs().add(log);
        }

        normalStepEnter(ctx, from);
    }

    @Override
    public void subflowStepEnter(FlowCaseState ctx, FlowGraphNode from) {
        FlowGraphNode currentNode = ctx.getCurrentNode();
        FlowNode flowNode = currentNode.getFlowNode();

        FlowServiceMapping mapping = flowServiceMappingProvider.findConfigMapping(flowNode.getNamespaceId(),
                flowNode.getSubFlowProjectType(), flowNode.getSubFlowProjectId(), flowNode.getSubFlowModuleType(),
                flowNode.getSubFlowModuleId(), flowNode.getSubFlowOwnerType(), flowNode.getSubFlowOwnerId());
        if (mapping == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE,
                    FlowServiceErrorCode.ERROR_SUB_FLOW_INVALID, "could not found sub flow service association flow");
        }

        Flow snapshot = flowProvider.getSnapshotFlowById(mapping.getFlowMainId());
        if (snapshot == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE,
                    FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot flow not exist");
        }

        FlowCase flowCase = ctx.getFlowCase();
        if (flowCase.getSubFlowPath() != null && flowCase.getSubFlowPath().split("/").length > 5) {
            FlowSubFlowEndDTO endDTO = new FlowSubFlowEndDTO();
            endDTO.setStepType(FlowStepType.ABSORT_STEP.getCode());
            endDTO.setParentFlowCaseId(ctx.getFlowCase().getId());

            LOGGER.warn("sub flow over 5, abort it");

            FlowTimeout ft = new FlowTimeout();
            ft.setBelongEntity(FlowEntityType.FLOW_NODE.getCode());
            ft.setBelongTo(flowNode.getId());
            ft.setTimeoutType(FlowTimeoutType.SUBFLOW_TIMEOUT.getCode());
            ft.setStatus(FlowStatusType.VALID.getCode());

            // 日志
            FlowEventLog log = new FlowEventLog();
            log.setId(flowEventLogProvider.getNextId());
            Flow flow = ctx.getFlowGraph().getFlow();
            log.setFlowMainId(flow.getFlowMainId());
            log.setFlowVersion(flow.getFlowVersion());
            log.setNamespaceId(flow.getNamespaceId());
            log.setFlowNodeId(flowNode.getId());
            log.setParentId(0L);
            log.setFlowCaseId(flowCase.getId());
            log.setFlowUserId(ctx.getOperator().getId());
            log.setFlowUserName(ctx.getOperator().getNickName());

            log.setLogContent(flowService.templateRender(FlowTemplateCode.DEFAULT_SUB_FLOW_TOO_DEEP_TEXT, null));
            log.setStepCount(flowCase.getStepCount());
            log.setLogType(FlowLogType.NODE_TRACKER.getCode());
            log.setTrackerApplier(1L);
            log.setTrackerProcessor(1L);
            log.setSubjectId(0L);
            endDTO.addLog(log);

            ft.setJson(endDTO.toString());

            Long timeoutTick = System.currentTimeMillis() + 1000L;
            ft.setTimeoutTick(new Timestamp(timeoutTick));
            ctx.getTimeouts().add(ft);

            normalStepEnter(ctx, from);
            return;
        }

        // 创建 flowCase
        CreateFlowCaseCommand cmd = new CreateFlowCaseCommand();
        cmd.setTitle(mapping.getDisplayName());
        cmd.setContent(flowCase.getContent());
        cmd.setFlowCaseId(flowService.getNextFlowCaseId());
        cmd.setFlowMainId(snapshot.getTopId());
        cmd.setFlowVersion(snapshot.getFlowVersion());
        cmd.setProjectType(flowCase.getProjectType());
        cmd.setProjectId(flowCase.getProjectId());
        cmd.setProjectTypeA(flowCase.getProjectTypeA());
        cmd.setProjectIdA(flowCase.getProjectIdA());
        cmd.setRouteUri(cmd.getRouteUri());
        cmd.setApplierOrganizationId(flowCase.getApplierOrganizationId());
        cmd.setCurrentOrganizationId(flowCase.getApplierOrganizationId());
        cmd.setApplyUserId(flowCase.getApplyUserId());
        cmd.setReferId(flowCase.getReferId());
        cmd.setReferType(flowCase.getReferType());
        cmd.setReferType(flowCase.getReferType());
        cmd.setServiceType(mapping.getDisplayName());
        cmd.setSubFlowParentId(flowCase.getId());

        flowListenerManager.onSubFlowEnter(ctx, mapping, snapshot, cmd);
        flowService.createFlowCase(cmd);

        // 添加一条日志跟踪
        FlowEventLog log = new FlowEventLog();
        log.setId(flowEventLogProvider.getNextId());
        Flow flow = ctx.getFlowGraph().getFlow();
        log.setFlowMainId(flow.getFlowMainId());
        log.setFlowVersion(flow.getFlowVersion());
        log.setNamespaceId(flow.getNamespaceId());
        log.setFlowNodeId(flowNode.getId());
        log.setParentId(0L);
        log.setFlowCaseId(flowCase.getId());
        log.setFlowUserId(ctx.getOperator().getId());
        log.setFlowUserName(ctx.getOperator().getNickName());
        ctx.pushProcessType(FlowCaseStateStackType.TRACKER_ACTION);

        Map<String, Object> map = new HashMap<>(1);
        map.put("serviceName", mapping.getDisplayName());

        log.setLogContent(flowService.templateRender(FlowTemplateCode.DEFAULT_SUB_FLOW_ENTER_TEXT, map));
        ctx.popProcessType();
        log.setStepCount(flowCase.getStepCount());
        log.setLogType(FlowLogType.NODE_TRACKER.getCode());
        log.setTrackerApplier(1L);
        log.setTrackerProcessor(1L);
        log.setSubjectId(0L);

        ctx.getLogs().add(log);

        normalStepEnter(ctx, from);
    }

    @Override
    public void subflowStepLeave(FlowCaseState ctx, FlowGraphNode current, FlowGraphNode to) {
        FlowStepType stepType = ctx.getStepType();
        FlowCase flowCase = ctx.getFlowCase();

        FlowCase subFlowCase = flowCaseProvider.getFlowCaseBySubFlowParentId(flowCase.getId());
        switch (stepType) {
            case ABSORT_STEP:
                // 子流程的父流程被终止了, 子流程应该也终止
                FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
                stepDTO.setAutoStepType(stepType.getCode());
                stepDTO.setFlowCaseId(subFlowCase.getId());
                stepDTO.setStepCount(subFlowCase.getStepCount());
                stepDTO.setFlowMainId(subFlowCase.getFlowMainId());
                stepDTO.setFlowVersion(subFlowCase.getFlowVersion());
                stepDTO.setFlowNodeId(subFlowCase.getCurrentNodeId());
                stepDTO.setEventType(FlowEventType.STEP_FLOW.getCode());
                flowService.processAutoStep(stepDTO);
                break;
            default:
                break;
        }

        FlowNode flowNode = current.getFlowNode();
        // 添加一条日志跟踪
        FlowEventLog log = new FlowEventLog();
        log.setId(flowEventLogProvider.getNextId());
        Flow flow = ctx.getFlowGraph().getFlow();
        log.setFlowMainId(flow.getFlowMainId());
        log.setFlowVersion(flow.getFlowVersion());
        log.setNamespaceId(flow.getNamespaceId());
        log.setFlowNodeId(current.getFlowNodeId());
        log.setParentId(0L);
        log.setFlowCaseId(flowCase.getId());
        log.setFlowUserId(ctx.getOperator().getId());
        log.setFlowUserName(ctx.getOperator().getNickName());
        ctx.pushProcessType(FlowCaseStateStackType.TRACKER_ACTION);

        FlowServiceMapping mapping = flowServiceMappingProvider.findConfigMapping(flowNode.getNamespaceId(),
                flowNode.getSubFlowProjectType(), flowNode.getSubFlowProjectId(), flowNode.getSubFlowModuleType(),
                flowNode.getSubFlowModuleId(), flowNode.getSubFlowOwnerType(), flowNode.getSubFlowOwnerId());
        if (mapping == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE,
                    FlowServiceErrorCode.ERROR_SUB_FLOW_INVALID, "could not found sub flow service association flow");
        }

        Map<String, Object> map = new HashMap<>(1);
        map.put("serviceName", mapping.getDisplayName());

        String originalStepTypeStr = ctx.getExtra("originalStepType");
        FlowStepType originalStepType = FlowStepType.fromCode(originalStepTypeStr);

        if (originalStepType == FlowStepType.ABSORT_STEP) {
            log.setLogContent(flowService.templateRender(FlowTemplateCode.DEFAULT_SUB_FLOW_ABORT_TEXT, map));
        } else {
            log.setLogContent(flowService.templateRender(FlowTemplateCode.DEFAULT_SUB_FLOW_END_TEXT, map));
        }

        ctx.popProcessType();
        log.setStepCount(flowCase.getStepCount()-1);
        log.setLogType(FlowLogType.NODE_TRACKER.getCode());
        log.setTrackerApplier(1L);
        log.setTrackerProcessor(1L);
        log.setSubjectId(0L);

        ctx.getLogs().add(log);
    }

    private Map<FlowEventLog, FlowCaseState> getFlowEventLog(FlowCaseState ctx, FlowGraphLink link) {
        Map<FlowEventLog, FlowCaseState> map = new HashMap<>();
        FlowEventLog log = flowEventLogProvider.findNodeLastStepTrackerLog(link.getFlowLink().getFromNodeId(), ctx.getFlowCase().getId());
        if (log != null) {
            map.put(log, ctx);
        }
        FlowCaseState parentState = ctx.getParentState();
        if (parentState != null) {
            log = flowEventLogProvider.findNodeLastStepTrackerLog(link.getFlowLink().getFromNodeId(), parentState.getFlowCase().getId());
            if (log != null) {
                map.put(log, parentState);
            }
        }
        List<FlowCaseState> childStates = ctx.getChildStates();
        for (FlowCaseState childState : childStates) {
            log = flowEventLogProvider.findNodeLastStepTrackerLog(link.getFlowLink().getFromNodeId(), childState.getFlowCase().getId());
            if (log != null) {
                map.put(log, childState);
            }
        }
        return map;
    }

    @Override
    public FlowCaseState prepareButtonFire(UserInfo logonUser, FlowFireButtonCommand cmd) {
        FlowCaseState ctx = new FlowCaseState();
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());

        if (flowCase == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                    "flowcase noexists, flowCaseId=" + cmd.getFlowCaseId());
        }
        if (cmd.getStepCount() == null || !cmd.getStepCount().equals(flowCase.getStepCount())) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                    "step busy");
        }
        FlowCaseStatus status = FlowCaseStatus.fromCode(flowCase.getStatus());
        if (status == FlowCaseStatus.INVALID || status == FlowCaseStatus.FINISHED || status == FlowCaseStatus.ABSORTED) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_STATUS_MISMATCH_ERROR,
                    "flow case status mismatch, flowCaseId=" + cmd.getFlowCaseId() + ", status=" + flowCase.getStatus());
        }

        FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        ctx.setFlowGraph(flowGraph);

        // 暂缓状态下只允许取消暂缓一个操作
        FlowGraphButton button = flowGraph.getGraphButton(cmd.getButtonId());
        if (status == FlowCaseStatus.SUSPEND && !FlowStepType.ABORT_SUSPEND_STEP.getCode().equals(button.getFlowButton().getFlowStepType())) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_STATUS_MISMATCH_ERROR,
                    "flow case status mismatch, flowCaseId=" + cmd.getFlowCaseId() + ", status=" + flowCase.getStatus());
        }

        ctx.setFlowCase(flowCase);
        ctx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));
        ctx.setOperator(logonUser);

        FlowGraphNode node = flowGraph.getGraphNode(flowCase.getCurrentNodeId());
        if (node == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS,
                    "flownode noexists, flowNodeId=" + flowCase.getCurrentNodeId());
        }
        ctx.setCurrentNode(node);

        FlowGraphLane currentLane = flowGraph.getGraphLane(flowCase.getCurrentLaneId());
        if (currentLane == null) {
            currentLane = flowGraph.getGraphLane(ctx.getCurrentNode().getFlowNode().getNodeLevel());
        }
        if (currentLane == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_LANE_NOT_EXISTS,
                    "flowLane not exists, flowLaneId=" + flowCase.getCurrentLaneId());
        }
        ctx.setCurrentLane(currentLane);

        FlowGraphButtonEvent event = new FlowGraphButtonEvent();

        Integer namespaceId = button.getFlowButton().getNamespaceId();

        FlowSubject subject = null;
        if (cmd.getContent() != null && cmd.getContent().trim().length() > 0) {
            subject = createFlowSubject(cmd, namespaceId);
            event.setSubject(subject);
        }
        if (cmd.getImages() != null && cmd.getImages().size() > 0) {
            if (subject == null) {
                subject = createFlowSubject(cmd, namespaceId);
                event.setSubject(subject);
            }

            List<Attachment> attachments = new ArrayList<>();
            for (String image : cmd.getImages()) {
                Attachment attach = new Attachment();
                attach.setContentType(NewsCommentContentType.IMAGE.getCode());
                attach.setContentUri(image);
                attach.setCreatorUid(logonUser.getId());
                attach.setOwnerId(subject.getId());
                attachments.add(attach);
            }
            attachmentProvider.createAttachments(EhFlowAttachments.class, attachments);
            subject.setAttachments(attachments);
        }

        event.setUserType(FlowUserType.fromCode(button.getFlowButton().getFlowUserType()));
        event.setCmd(cmd);
        event.setFiredUser(ctx.getOperator());
        ctx.setCurrentEvent(event);

        // parent ctx
        if (flowCase.getParentId() != 0) {
            prepareParentState(ctx, logonUser, flowCase, event);
        }
        // child ctx
        prepareChildState(ctx.getRootState(), logonUser, flowCase, event);

        FlowGraphButton btn = flowGraph.getGraphButton(cmd.getButtonId());
        if (btn != null) {
            FlowStepType stepType = FlowStepType.fromCode(btn.getFlowButton().getFlowStepType());
            ctx.setStepType(stepType);
        }

        flowListenerManager.onFlowButtonFired(ctx);

        return ctx;
    }

    private FlowSubject createFlowSubject(FlowFireButtonCommand cmd, Integer namespaceId) {
        FlowSubject subject;
        subject = new FlowSubject();
        subject.setBelongEntity(FlowEntityType.FLOW_BUTTON.getCode());
        subject.setBelongTo(cmd.getButtonId());
        if (cmd.getContent() != null && cmd.getContent().trim().length() > 0) {
            subject.setContent(cmd.getContent());
        }
        subject.setNamespaceId(namespaceId);
        subject.setStatus(FlowStatusType.VALID.getCode());
        subject.setTitle(cmd.getTitle());
        flowSubjectProvider.createFlowSubject(subject);
        return subject;
    }

    private void prepareChildState(FlowCaseState ctx, UserInfo logonUser, FlowCase flowCase, FlowGraphEvent event) {
        List<FlowCase> childFlowCases = flowCaseProvider.listFlowCaseByParentId(ctx.getFlowCase().getId());
        Map<Long, FlowCaseState> flowCaseIdToStateMap = ctx.getChildStates().stream().collect(Collectors.toMap(r -> r.getFlowCase().getId(), r -> r));
        for (FlowCase childFlowCase : childFlowCases) {
            if (flowCaseIdToStateMap.containsKey(childFlowCase.getId())) {
                prepareChildState(flowCaseIdToStateMap.get(childFlowCase.getId()), logonUser, childFlowCase, event);
                continue;
            }
            FlowCaseState childCtx = new FlowCaseState();
            childCtx.setCurrentEvent(event);
            childCtx.setFlowCase(childFlowCase);
            childCtx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));
            childCtx.setOperator(logonUser);
            FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
            childCtx.setFlowGraph(flowGraph);
            childCtx.setParentState(ctx);
            childCtx.setCurrentNode(ctx.getFlowGraph().getGraphNode(childFlowCase.getCurrentNodeId()));
            ctx.getChildStates().add(childCtx);
            prepareChildState(childCtx, logonUser, childFlowCase, event);
        }
    }

    private void prepareParentState(FlowCaseState ctx, UserInfo logonUser, FlowCase flowCase, FlowGraphEvent event) {
        FlowCase parentFlowCase = flowCaseProvider.getFlowCaseById(flowCase.getParentId());
        if (parentFlowCase != null) {
            FlowCaseState parentCtx = new FlowCaseState();
            parentCtx.setFlowCase(parentFlowCase);
            parentCtx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));
            parentCtx.setOperator(logonUser);
            parentCtx.setFlowGraph(ctx.getFlowGraph());
            parentCtx.setCurrentEvent(event);
            parentCtx.setCurrentNode(ctx.getFlowGraph().getGraphNode(parentFlowCase.getCurrentNodeId()));
            ctx.setParentState(parentCtx);
            parentCtx.getChildStates().add(ctx);
            if (parentFlowCase.getParentId() != 0) {
                prepareParentState(parentCtx, logonUser, parentFlowCase, event);
            }
        }
    }

    @Override
    public void step(FlowCaseState ctx, FlowGraphEvent event) {
        boolean stepOK = true;
        if (event != null) {
            event.fire(ctx);
        }
        FlowGraphNode nextNode = ctx.getNextNode();
        FlowGraphNode currentNode = ctx.getCurrentNode();
        if (isValidNextFlowNode(ctx, currentNode, nextNode)) {
            try {
                //Only leave once time
                if (currentNode != null) {
                    currentNode.stepLeave(ctx, nextNode);
                }

                // 节点进入
                for (FlowCaseState subCtx : ctx.getAllFlowState()) {
                    FlowGraphNode subNextNode = subCtx.getNextNode();
                    subCtx.setPrefixNode(currentNode);
                    subCtx.setCurrentNode(subNextNode);
                    subCtx.setNextNode(null);
                    if (subNextNode != null) {
                        subNextNode.stepEnter(subCtx, currentNode);
                    }
                }
                stepOK = true;
            } catch (FlowStepErrorException ex) {
                stepOK = false;
            }
        }

        //Now save info to databases here
        if (stepOK) {
            flowService.flushState(ctx);
        } else {
            LOGGER.warn("step error flowCaseId=" + ctx.getFlowCase().getId());
        }
    }

    private boolean isValidNextFlowNode(FlowCaseState ctx, FlowGraphNode currentNode, FlowGraphNode nextNode) {
        return ctx.getStepType() != null
                && ctx.getStepType() != FlowStepType.NO_STEP
                && !currentNode.equals(nextNode);
    }

    //normal step functions
    @Override
    public void normalStepEnter(FlowCaseState ctx, FlowGraphNode from) throws FlowStepErrorException {
        FlowGraph flowGraph = ctx.getFlowGraph();
        FlowStepType fromStep = ctx.getStepType();
        FlowGraphNode currentNode = ctx.getCurrentNode();

        boolean logStep = false;
        FlowEventLog log = null;

        //create processor's
        flowService.createSnapshotNodeProcessors(ctx, currentNode);

        switch (fromStep) {
            case NO_STEP:
                break;
            case APPROVE_STEP:
                logStep = true;
                if (currentNode.getTrackApproveEnter() != null) {
                    FlowActionStatus status = FlowActionStatus.fromCode(currentNode.getTrackApproveEnter().getFlowAction().getStatus());
                    if (status == FlowActionStatus.ENABLED) {
                        currentNode.getTrackApproveEnter().fireAction(ctx, null);
                    }
                }
                break;
            case REJECT_STEP:
                logStep = true;
                if (currentNode.getTrackRejectEnter() != null) {
                    FlowActionStatus status = FlowActionStatus.fromCode(currentNode.getTrackRejectEnter().getFlowAction().getStatus());
                    if (status == FlowActionStatus.ENABLED) {
                        currentNode.getTrackRejectEnter().fireAction(ctx, null);
                    }
                }
                break;
            case TRANSFER_STEP:
                break;
            case COMMENT_STEP:
                break;
            case ABSORT_STEP:
                logStep = true;//Never enter here
                break;
            case REMINDER_STEP:
                break;
            case EVALUATE_STEP:
                break;
            default:
                break;
        }

        FlowNode currentRawNode = currentNode.getFlowNode();
        FlowGraphLane currentLane = flowGraph.getGraphLane(currentRawNode.getFlowLaneId());

        ctx.getRootState().flushCurrentLane(currentLane);
        ctx.flushCurrentNode(currentNode);
        ctx.flushCurrentLane(currentLane);

        flowListenerManager.onFlowCaseStateChanged(ctx);

        currentNode.fireAction(ctx);

        //create step timeout
        if (!currentRawNode.getAllowTimeoutAction().equals((byte) 0)) {
            createStepTimeout(ctx, currentRawNode);
        }

        if (logStep && log == null) {
            UserInfo firedUser = ctx.getOperator();
            log = new FlowEventLog();
            log.setId(flowEventLogProvider.getNextId());
            log.setFlowMainId(flowGraph.getFlow().getFlowMainId());
            log.setFlowVersion(flowGraph.getFlow().getFlowVersion());
            log.setNamespaceId(flowGraph.getFlow().getNamespaceId());
            log.setFlowNodeId(currentRawNode.getId());
            log.setParentId(0L);
            log.setFlowCaseId(ctx.getFlowCase().getId());
            if (firedUser != null) {
                log.setFlowUserId(firedUser.getId());
                log.setFlowUserName(firedUser.getNickName());
            }
            log.setButtonFiredStep(fromStep.getCode());
            log.setLogType(FlowLogType.STEP_TRACKER.getCode());
            log.setStepCount(ctx.getFlowCase().getStepCount());
            ctx.getLogs().add(log);    //added but not save to database now.
        }
    }

    @Override
    public void createStepTimeout(FlowCaseState ctx, FlowNode flowNode) {
        FlowTimeout ft = new FlowTimeout();
        ft.setBelongEntity(FlowEntityType.FLOW_NODE.getCode());
        ft.setBelongTo(flowNode.getId());
        ft.setTimeoutType(FlowTimeoutType.STEP_TIMEOUT.getCode());
        ft.setStatus(FlowStatusType.VALID.getCode());

        FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
        stepDTO.setFlowCaseId(ctx.getFlowCase().getId());
        stepDTO.setFlowMainId(ctx.getFlowCase().getFlowMainId());
        stepDTO.setFlowVersion(ctx.getFlowCase().getFlowVersion());
        stepDTO.setStepCount(ctx.getFlowCase().getStepCount());
        stepDTO.setFlowNodeId(flowNode.getId());
        stepDTO.setAutoStepType(flowNode.getAutoStepType());
        stepDTO.setOperatorId(User.SYSTEM_UID);
        stepDTO.setEventType(FlowEventType.STEP_TIMEOUT.getCode());
        ft.setJson(stepDTO.toString());

        Long timeoutTick = DateHelper.currentGMTTime().getTime() + flowNode.getAutoStepMinute() * 60 * 1000L;
        ft.setTimeoutTick(new Timestamp(timeoutTick));
        ctx.getTimeouts().add(ft);
    }

    @Override
    public void normalStepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException {
        FlowStepType fromStep = ctx.getStepType();
        switch (fromStep) {
            case NO_STEP:
                break;
            case APPROVE_STEP:
                break;
            case REJECT_STEP:
                break;
            case TRANSFER_STEP:
                break;
            case COMMENT_STEP:
                break;
            case ABSORT_STEP:
                break;
            case REMINDER_STEP:
                break;
            case EVALUATE_STEP:
                break;
            default:
                break;
        }
    }

    @Override
    public void endStepEnter(FlowCaseState ctx, FlowGraphNode from) {
        FlowGraph flowGraph = ctx.getFlowGraph();
        FlowStepType fromStep = ctx.getStepType();
        FlowGraphNode currentNode = ctx.getCurrentNode();
        FlowNode currentRowNode = currentNode.getFlowNode();
        FlowGraphLane currentLane = flowGraph.getGraphLane(currentRowNode.getFlowLaneId());

        ctx.flushCurrentNode(currentNode);
        ctx.flushCurrentLane(currentLane);

        // ctx.getFlowCase().setCurrentNodeId(curr.getFlowNode().getId());
        // ctx.getFlowCase().setCurrentLaneId(currentLane.getFlowLane().getId());
        // ctx.getFlowCase().setCurrentLane(currentLane.getFlowLane().getDisplayName());
        // ctx.setCurrentLane(currentLane);

        FlowEventLog tracker = null;

        boolean logStep = false;
        switch (fromStep) {
            case NO_STEP:
                break;
            case APPROVE_STEP:
            case END_STEP:
                logStep = true;
                ctx.setFlowCaseStatus(FlowCaseStatus.FINISHED);
                for (FlowCaseState subCtx : ctx.getAllFlowState()) {
                    subCtx.flushCurrentLane(currentLane);
                    subCtx.flushCurrentNode(currentNode);
                    subCtx.setFlowCaseStatus(FlowCaseStatus.FINISHED);
                }

                tracker = new FlowEventLog();
                tracker.setLogContent(flowService.getStepMessageTemplate(fromStep, FlowCaseStatus.FINISHED, ctx.getCurrentEvent(), null));
                tracker.setStepCount(ctx.getFlowCase().getStepCount());

                flowListenerManager.onFlowCaseEnd(ctx);
                break;
            case EVALUATE_STEP:
                break;
            case ABSORT_STEP:
                logStep = true;
                ctx.setFlowCaseStatus(FlowCaseStatus.ABSORTED);
                for (FlowCaseState subCtx : ctx.getAllFlowState()) {
                    subCtx.flushCurrentLane(currentLane);
                    subCtx.flushCurrentNode(currentNode);
                    subCtx.setFlowCaseStatus(FlowCaseStatus.ABSORTED);
                }

                tracker = new FlowEventLog();
                Map<String, Object> templateMap = new HashMap<>();
                UserInfo operator = ctx.getOperator();
                if (operator != null) {
                    flowService.fixupUserInfoInContext(ctx, operator);
                    templateMap.put("applierName", operator.getNickName());
                }

                tracker.setLogContent(flowService.getStepMessageTemplate(fromStep, FlowCaseStatus.ABSORTED, ctx.getCurrentEvent(), templateMap));
                tracker.setStepCount(ctx.getFlowCase().getStepCount());
                FlowSubject subject = ctx.getCurrentEvent().getSubject();
                if (subject != null) {
                    tracker.setSubjectId(subject.getId());
                }

                flowListenerManager.onFlowCaseAbsorted(ctx);
                break;
            default:
                break;
        }

        flowListenerManager.onFlowCaseStateChanged(ctx);

        if (tracker != null) {
            tracker.setId(flowEventLogProvider.getNextId());
            tracker.setFlowMainId(flowGraph.getFlow().getFlowMainId());
            tracker.setFlowVersion(flowGraph.getFlow().getFlowVersion());
            tracker.setNamespaceId(flowGraph.getFlow().getNamespaceId());
            tracker.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
            tracker.setParentId(0L);
            tracker.setFlowCaseId(ctx.getFlowCase().getId());
            tracker.setFlowUserId(ctx.getOperator().getId());
            tracker.setFlowUserName(ctx.getOperator().getNickName());
            if (tracker.getSubjectId() == null) {
                tracker.setSubjectId(0L);// BUG #5431
            }

            tracker.setLogType(FlowLogType.NODE_TRACKER.getCode());

            tracker.setButtonFiredStep(fromStep.getCode());
            tracker.setTrackerApplier(1L);
            tracker.setTrackerProcessor(1L);
            ctx.getLogs().add(tracker);
        }

        if (logStep) {
            UserInfo firedUser = ctx.getOperator();
            FlowEventLog log = new FlowEventLog();
            log.setId(flowEventLogProvider.getNextId());
            log.setFlowMainId(flowGraph.getFlow().getFlowMainId());
            log.setFlowVersion(flowGraph.getFlow().getFlowVersion());
            log.setNamespaceId(flowGraph.getFlow().getNamespaceId());
            log.setFlowNodeId(currentRowNode.getId());
            log.setParentId(0L);
            log.setFlowCaseId(ctx.getFlowCase().getId());
            if (firedUser != null) {
                log.setFlowUserId(firedUser.getId());
                log.setFlowUserName(firedUser.getNickName());
            }
            log.setButtonFiredStep(fromStep.getCode());
            log.setLogType(FlowLogType.STEP_TRACKER.getCode());
            log.setStepCount(ctx.getFlowCase().getStepCount());
            ctx.getLogs().add(log);    //added but not save to database now.
        }

        // 子流程处理
        if (!Objects.equals(ctx.getFlowCase().getSubFlowParentId(), 0L)) {
            FlowSubFlowEndDTO endDTO = new FlowSubFlowEndDTO();
            endDTO.setParentFlowCaseId(ctx.getFlowCase().getSubFlowParentId());
            endDTO.setStepType(fromStep.getCode());

            FlowTimeout ft = new FlowTimeout();
            ft.setBelongEntity(FlowEntityType.FLOW.getCode());
            ft.setBelongTo(ctx.getFlowCase().getFlowMainId());
            ft.setTimeoutType(FlowTimeoutType.SUBFLOW_TIMEOUT.getCode());
            ft.setStatus(FlowStatusType.VALID.getCode());
            ft.setTimeoutTick(new Timestamp(System.currentTimeMillis() + 1000L));
            ft.setJson(endDTO.toString());

            ctx.getTimeouts().add(ft);
        }
    }
}
