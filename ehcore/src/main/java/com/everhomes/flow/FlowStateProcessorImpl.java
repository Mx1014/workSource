package com.everhomes.flow;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.flow.event.FlowGraphAutoStepEvent;
import com.everhomes.flow.event.FlowGraphButtonEvent;
import com.everhomes.flow.event.FlowGraphNoStepEvent;
import com.everhomes.flow.event.FlowGraphStartEvent;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.tables.pojos.EhFlowAttachments;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private FlowUserSelectionProvider flowUserSelectionProvider;

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
        flowService.createSnapshotSupervisors(ctx);

        flowListenerManager.onFlowCaseStart(ctx);

        return ctx;
    }

    @Override
    public FlowCaseState prepareSubFlowCaseStart(UserInfo logonUser, FlowCase flowCase) {
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

            ctx.setFlowCase(flowCase);
            ctx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));

            FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
            ctx.setFlowGraph(flowGraph);

            FlowGraphNode node = flowGraph.getGraphNode(flowCase.getCurrentNodeId());
            if (node == null) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "flownode noexists");
            }
            ctx.setCurrentNode(node);

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

            return ctx;
        }
        return null;
    }

    @Override
    public FlowCaseState prepareNoStep(FlowAutoStepDTO stepDTO) {
        FlowCaseState ctx = new FlowCaseState();
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(stepDTO.getFlowCaseId());

        User user = null;
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

        if (stepDTO.getFlowTargetId() != null) {
            FlowGraphNode targetNode = flowGraph.getGraphNode(stepDTO.getFlowTargetId());
            ctx.setNextNode(targetNode);
        }

        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(user.getId());
        ctx.setOperator(userInfo);
        FlowGraphNoStepEvent event = new FlowGraphNoStepEvent(stepDTO);
        ctx.setCurrentEvent(event);
        ctx.setStepType(FlowStepType.NO_STEP);

        return ctx;
    }

    @Override
    public FlowGraphNode rejectToNode(FlowCaseState ctx, Integer gotoLevel, FlowGraphNode current) {

        if (current.getLinksIn().size() > 1) {
            for (FlowCaseState flowCaseState : ctx.getChildStates()) {
                FlowGraphLink link = current.getFlowLink(ctx.getFlowCase().getEndLinkId());
                FlowGraphNode node = findPrefixNode(flowCaseState, link, gotoLevel);
                if (node != null) {
                    return node;
                }
            }
        } else {
            return findPrefixNode(ctx, current.getLinksIn().get(0), gotoLevel);
        }
        return null;
    }

    /*private FlowGraphNode findPrefixNodes(FlowCaseState ctx, FlowGraphNode current, Integer gotoLevel) {


        FlowGraphNode prefixNode = link.getFromNode(ctx, null);

        List<FlowEventLog> enterLogs = flowEventLogProvider.findCurrentNodeEnterLogs(prefixNode.getFlowNode().getId(),
                ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount() - 1);
        if (enterLogs.isEmpty()) {
            continue;
        }

        if (prefixNode.getFlowNode().getNodeLevel().equals(gotoLevel)) {
            return prefixNode;
        } else if (prefixNode.getFlowNode().getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {

            ctx = ctx.getParentState();
        }
        return rejectToNode(ctx, gotoLevel, prefixNode);
    }*/

    private FlowGraphNode findPrefixNode(FlowCaseState ctx, FlowGraphLink link, Integer gotoLevel) {
        FlowGraphNode prefixNode = link.getFromNode(ctx, null);

        List<FlowEventLog> enterLogs = flowEventLogProvider.findCurrentNodeEnterLogs(prefixNode.getFlowNode().getId(),
                ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount() - 1);
        if (enterLogs.isEmpty()) {
            return null;
        }

        if (prefixNode.getFlowNode().getNodeLevel().equals(gotoLevel)) {
            return prefixNode;
        } else if (prefixNode.getFlowNode().getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {

            ctx = ctx.getParentState();
        }
        return rejectToNode(ctx, gotoLevel, prefixNode);
    }

    @Override
    public FlowCaseState prepareButtonFire(UserInfo logonUser, FlowFireButtonCommand cmd) {
        FlowCaseState ctx = new FlowCaseState();
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
        if (flowCase == null
                || flowCase.getStatus().equals(FlowCaseStatus.INVALID.getCode())
                || flowCase.getStatus().equals(FlowCaseStatus.FINISHED.getCode())
                || flowCase.getStatus().equals(FlowCaseStatus.ABSORTED.getCode())) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                    "flowcase noexists, flowCaseId=" + cmd.getFlowCaseId());
        }

        if (cmd.getStepCount() != null && !cmd.getStepCount().equals(flowCase.getStepCount())) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                    "step busy");
        }

        if (flowCase.getParentId() != 0) {
            prepareParentState(ctx, logonUser, flowCase);
        }

        ctx.setFlowCase(flowCase);
        ctx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));
        ctx.setOperator(logonUser);
        ctx.setListenerManager(flowListenerManager);

        FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        ctx.setFlowGraph(flowGraph);

        FlowGraphLane currentLane = flowGraph.getGraphLane(flowCase.getCurrentLaneId());
        if (currentLane == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_LANE_NOEXISTS,
                    "flowLane not exists, flowLaneId=" + flowCase.getCurrentLaneId());
        }
        ctx.setCurrentLane(currentLane);

        FlowGraphNode node = flowGraph.getGraphNode(flowCase.getCurrentNodeId());
        if (node == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS,
                    "flownode noexists, flowNodeId=" + flowCase.getCurrentNodeId());
        }
        ctx.setCurrentNode(node);

        FlowGraphButton button = flowGraph.getGraphButton(cmd.getButtonId());
        FlowGraphButtonEvent event = new FlowGraphButtonEvent();

        if ((cmd.getContent() != null /* && !cmd.getContent().isEmpty() */)
                || (null != cmd.getImages() && cmd.getImages().size() > 0)) {
            FlowSubject subject = new FlowSubject();
            subject.setBelongEntity(FlowEntityType.FLOW_BUTTON.getCode());
            subject.setBelongTo(cmd.getButtonId());
            subject.setContent(cmd.getContent());
            subject.setNamespaceId(button.getFlowButton().getNamespaceId());
            subject.setStatus(FlowStatusType.VALID.getCode());
            subject.setTitle(cmd.getTitle());
            flowSubjectProvider.createFlowSubject(subject);

            if (null != cmd.getImages() && cmd.getImages().size() > 0) {
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
            }

            event.setSubject(subject);
        }

        event.setUserType(FlowUserType.fromCode(button.getFlowButton().getFlowUserType()));
        event.setCmd(cmd);
        event.setFiredUser(ctx.getOperator());
        ctx.setCurrentEvent(event);

        //fire button actions
        FlowGraphButton btn = flowGraph.getGraphButton(cmd.getButtonId());
        if (btn != null) {
            FlowStepType stepType = FlowStepType.fromCode(btn.getFlowButton().getFlowStepType());
            ctx.setStepType(stepType);
        }

        flowListenerManager.onFlowButtonFired(ctx);

        return ctx;
    }

    private void prepareParentState(FlowCaseState ctx, UserInfo logonUser, FlowCase flowCase) {
        FlowCase parentFlowCase = flowCaseProvider.getFlowCaseById(flowCase.getParentId());
        if (parentFlowCase != null) {
            FlowCaseState parentCtx = new FlowCaseState();
            parentCtx.setFlowCase(parentFlowCase);
            parentCtx.setModule(flowListenerManager.getModule(flowCase.getModuleId()));
            parentCtx.setOperator(logonUser);
            parentCtx.setListenerManager(flowListenerManager);
            FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
            parentCtx.setFlowGraph(flowGraph);
            parentCtx.setParentState(parentCtx);
            ctx.setParentState(parentCtx);
            if (parentFlowCase.getParentId() != 0) {
                prepareParentState(parentCtx, logonUser, parentFlowCase);
            }
        }
    }

    @Override
    public void step(FlowCaseState ctx, FlowGraphEvent event) {
        boolean stepOK = true;
        FlowGraphNode currentNode = ctx.getCurrentNode();
        if (event != null) {
            event.fire(ctx);
        }
        FlowGraphNode nextNode = ctx.getNextNode();
        if (isValidNextFlowNode(ctx, currentNode, nextNode)) {
            try {
                //Only leave once time
                if (currentNode != null) {
                    currentNode.stepLeave(ctx, nextNode);
                }

                // if reject step, prefixNode is currentNode ?
                ctx.setPrefixNode(currentNode);
                ctx.setCurrentNode(nextNode);
                ctx.setNextNode(null);

                // create processor's
                // flowService.createSnapshotNodeProcessors(ctx, );

                // 普通进入
                if (nextNode != null) {
                    nextNode.stepEnter(ctx, currentNode);
                }

                // 分支进入
                for (FlowCaseState subCtx : ctx.getChildStates()) {
                    FlowGraphNode subNextNode = subCtx.getNextNode();
                    if (subNextNode != null) {
                        subCtx.setPrefixNode(currentNode);
                        subCtx.setCurrentNode(subNextNode);
                        subCtx.setNextNode(null);
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
        FlowStepType fromStep = ctx.getStepType();
        FlowGraphNode currentNode = ctx.getCurrentNode();

//		if(currentNode.getFlowNode().getNodeLevel() > 1) {
//			ctx.getFlowCase().setStatus(FlowCaseStatus.PROCESS.getCode());
//		}
        ctx.getFlowCase().setCurrentNodeId(currentNode.getFlowNode().getId());
        boolean logStep = false;

        FlowEventLog log = null;
//		log = flowEventLogProvider.getStepEvent(ctx.getFlowCase().getId(), currentNode.getFlowNode().getId(), ctx.getFlowCase().getStepCount(), fromStep);
//		if(log != null && fromStep != FlowStepType.COMMENT_STEP) {
//			return;
//		}

        FlowGraphBranch branch = ctx.getFlowGraph().getBranchByConvergenceNode(currentNode.getFlowNode().getId());
        if (branch != null && ctx.getParentState() != null) {
            ctx.getFlowCase().setStatus(FlowCaseStatus.FINISHED.getCode());
            List<FlowCaseState> childStates = ctx.getParentState().getChildStates();
            boolean allFinish = childStates.stream().allMatch(r -> FlowCaseStatus.fromCode(r.getFlowCase().getStatus()) == FlowCaseStatus.FINISHED);
            // 在汇总节点，只有当所有流程都走完才进入当前节点，不管是concurrent或者single
            if (!allFinish) {
                ctx.getFlowCase().setStatus(FlowCaseStatus.PROCESS.getCode());
                return;
            }
        }

        flowListenerManager.onFlowCaseStateChanged(ctx);

        //create processor's
        flowService.createSnapshotNodeProcessors(ctx, currentNode);

        ctx.getGrantParentState().setCurrentLane(ctx.getFlowGraph().getGraphLane(currentNode.getFlowNode().getFlowLaneId()));
        ctx.getGrantParentState().getFlowCase().setCurrentLaneId(currentNode.getFlowNode().getFlowLaneId());

        //TODO use schedule ? scheduler.execute();

        //TODO do build in a delay thread
        if (currentNode.getMessageAction() != null) {
            Byte status = currentNode.getMessageAction().getFlowAction().getStatus();
            if (FlowActionStatus.fromCode(status) == FlowActionStatus.ENABLED) {
                currentNode.getMessageAction().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (currentNode.getSmsAction() != null) {
            Byte status = currentNode.getSmsAction().getFlowAction().getStatus();
            if (FlowActionStatus.fromCode(status) == FlowActionStatus.ENABLED) {
                currentNode.getSmsAction().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (currentNode.getTickMessageAction() != null) {
            Byte status = currentNode.getTickMessageAction().getFlowAction().getStatus();
            if (FlowActionStatus.fromCode(status) == FlowActionStatus.ENABLED) {
                currentNode.getTickMessageAction().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (currentNode.getTickSMSAction() != null) {
            Byte status = currentNode.getTickSMSAction().getFlowAction().getStatus();
            if (FlowActionStatus.fromCode(status) == FlowActionStatus.ENABLED) {
                currentNode.getTickSMSAction().fireAction(ctx, ctx.getCurrentEvent());
            }
        }

        switch (fromStep) {
            case NO_STEP:
                break;
            case APPROVE_STEP:
                logStep = true;
                if (currentNode.getTrackApproveEnter() != null) {
                    currentNode.getTrackApproveEnter().fireAction(ctx, null);
                }
                break;
            case REJECT_STEP:
                logStep = true;
                if (currentNode.getTrackRejectEnter() != null) {
                    currentNode.getTrackRejectEnter().fireAction(ctx, null);
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

        //create step timeout
        if (!currentNode.getFlowNode().getAllowTimeoutAction().equals((byte) 0)) {
            FlowTimeout ft = new FlowTimeout();
            ft.setBelongEntity(FlowEntityType.FLOW_NODE.getCode());
            ft.setBelongTo(currentNode.getFlowNode().getId());
            ft.setTimeoutType(FlowTimeoutType.STEP_TIMEOUT.getCode());
            ft.setStatus(FlowStatusType.VALID.getCode());

            FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
            stepDTO.setFlowCaseId(ctx.getFlowCase().getId());
            stepDTO.setFlowMainId(ctx.getFlowCase().getFlowMainId());
            stepDTO.setFlowVersion(ctx.getFlowCase().getFlowVersion());
            stepDTO.setStepCount(ctx.getFlowCase().getStepCount());
            stepDTO.setFlowNodeId(currentNode.getFlowNode().getId());
            stepDTO.setAutoStepType(currentNode.getFlowNode().getAutoStepType());
            stepDTO.setOperatorId(User.SYSTEM_UID);
            ft.setJson(stepDTO.toString());

            Long timeoutTick = DateHelper.currentGMTTime().getTime() + currentNode.getFlowNode().getAutoStepMinute() * 60 * 1000L;
//			Long timeoutTick = DateHelper.currentGMTTime().getTime() + 6000;
            ft.setTimeoutTick(new Timestamp(timeoutTick));

//			flowTimeoutService.pushTimeout(ft);
            ctx.getTimeouts().add(ft);
        }

        if (logStep && log == null) {
            UserInfo firedUser = ctx.getOperator();
            log = new FlowEventLog();
            log.setId(flowEventLogProvider.getNextId());
            log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
            log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
            log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
            log.setFlowNodeId(currentNode.getFlowNode().getId());
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
        FlowStepType fromStep = ctx.getStepType();
        FlowGraphNode curr = ctx.getCurrentNode();
        ctx.getFlowCase().setCurrentNodeId(curr.getFlowNode().getId());
        boolean logStep = false;
        flowListenerManager.onFlowCaseStateChanged(ctx);
        switch (fromStep) {
            case NO_STEP:
                break;
            case APPROVE_STEP:
            case END_STEP:
                ctx.getFlowCase().setStatus(FlowCaseStatus.FINISHED.getCode());
                break;
            case EVALUATE_STEP:
                break;
            case ABSORT_STEP:
                logStep = true;
                ctx.getFlowCase().setStatus(FlowCaseStatus.ABSORTED.getCode());

                flowListenerManager.onFlowCaseAbsorted(ctx);
                break;
            default:
                break;
        }

        flowListenerManager.onFlowCaseEnd(ctx);

        if (logStep) {
            UserInfo firedUser = ctx.getOperator();
            FlowEventLog log = new FlowEventLog();
            log.setId(flowEventLogProvider.getNextId());
            log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
            log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
            log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
            log.setFlowNodeId(curr.getFlowNode().getId());
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
}
