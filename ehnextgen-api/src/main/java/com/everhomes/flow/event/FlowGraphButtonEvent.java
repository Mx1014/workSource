package com.everhomes.flow.event;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlowGraphButtonEvent extends AbstractFlowGraphEvent {

    private FlowUserType userType;
    private UserInfo firedUser;
    private FlowFireButtonCommand cmd;
    private FlowSubject subject;

    private FlowEventLogProvider flowEventLogProvider;
    private UserService userService;
    private FlowService flowService;
    private FlowStateProcessor flowStateProcessor;

    public FlowGraphButtonEvent() {
        flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
        userService = PlatformContext.getComponent(UserService.class);
        flowService = PlatformContext.getComponent(FlowService.class);
        flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
    }

    @Override
    public void fire(FlowCaseState ctx) {
        FlowGraph flowGraph = ctx.getFlowGraph();
        FlowGraphButton btn = flowGraph.getGraphButton(cmd.getButtonId());

        Integer gotoLevel = btn.getFlowButton().getGotoLevel();

        FlowLogType logType = FlowLogType.BUTTON_FIRED;
        FlowEventLog log = null;
        FlowEventLog tracker = null;

        FlowStepType stepType = ctx.getStepType();
        FlowCase flowCase = ctx.getFlowCase();
        Long oldStepCount = flowCase.getStepCount();

        //currentNode state change to next step
        FlowGraphNode currentNode = ctx.getCurrentNode();
        FlowGraphNode next = null;

        FlowGraphLane currentLane = ctx.getCurrentLane();

        UserInfo applier = userService.getUserSnapshotInfo(flowCase.getApplyUserId());

        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("nodeName", currentNode.getFlowNode().getNodeName());
        templateMap.put("laneName", currentLane.getFlowLane().getDisplayName());
        templateMap.put("applierName", applier.getNickName());

        Map<String, Object> buttonFireEventContentMap = new HashMap<>();
        buttonFireEventContentMap.put("nodeName", currentNode.getFlowNode().getNodeName());
        buttonFireEventContentMap.put("buttonName", btn.getFlowButton().getButtonName());

        //button actions
        btn.fireAction(ctx);

        switch(stepType) {
            case NO_STEP:
                break;
            case APPROVE_STEP:
                // 节点会签判断
                if (Objects.equals(currentNode.getFlowNode().getNeedAllProcessorComplete(), TrueOrFalseFlag.TRUE.getCode())) {
                    boolean allProcessorComplete = flowStateProcessor.allProcessorCompleteInCurrentNode(ctx, currentNode, firedUser);
                    if (!allProcessorComplete) {
                        //Remove the old enter logs
                        log = flowEventLogProvider.getValidEnterStep(firedUser.getId(), ctx.getFlowCase());
                        if (null != log) {
                            log.setEnterLogCompleteFlag(TrueOrFalseFlag.TRUE.getCode()); // mark as complete
                            ctx.getUpdateLogs().add(log);
                            log = null;
                            break;
                        }
                    }
                }

                next = currentNode.getLinksOut().get(0).getToNode(ctx, this);

                boolean isConditionNode = FlowNodeType.fromCode(next.getFlowNode().getNodeType()) == FlowNodeType.CONDITION_FRONT;
                // 条件节点
                if (isConditionNode) {
                    next.stepEnter(ctx, currentNode);
                } else {
                    final FlowGraphNode tempNext = next;
                    FlowGraphBranch thisBranch = flowGraph.getBranchByOriginalAndConvNode(flowCase.getStartNodeId(), next.getFlowNode().getId());
                    // 先把当前分支判断是否完成
                    if (thisBranch != null) {
                        flowCase.flushCurrentNode(next.getFlowNode());

                        List<FlowCase> siblingFlowCase = ctx.getSiblingFlowCase();
                        boolean allFinish = siblingFlowCase.stream().allMatch(
                                r -> Objects.equals(r.getCurrentNodeId(), tempNext.getFlowNode().getId()));
                        if (allFinish) {
                            // 如果父的flowCase的结束节点也是当前节点，则一起处理掉
                            stepParentState(ctx, next);
                        }
                    }

                    // 再判断是否有其他的分支,这里的分支包含上面的当前分支
                    List<FlowGraphBranch> branches = flowGraph.getBranchByConvNode(next.getFlowNode().getId());
                    if (branches != null && branches.size() > 0 && ctx.getParentState() != null) {
                        // 再判断其他所有分支是否都已经完成
                        int finishedBranch = 0;
                        for (FlowGraphBranch branch : branches) {
                            List<FlowCase> flowCaseList = ctx.getFlowCaseByBranch(branch.getFlowBranch());
                            boolean allFinish = flowCaseList.stream().allMatch(
                                    r -> Objects.equals(r.getCurrentNodeId(), tempNext.getFlowNode().getId()));
                            if (allFinish) {
                                finishedBranch++;
                                // 如果父的flowCase的结束节点也是当前节点，则一起处理掉
                                stepParentState(ctx, next);
                            }
                        }

                        // 当前节点的所有分支并没有全部完成，不能进入当前节点
                        if (finishedBranch < branches.size()) {
                            log = flowEventLogProvider.getValidEnterStep(ctx.getOperator().getId(), ctx.getFlowCase());
                            if (null != log) {
                                log.setEnterLogCompleteFlag(TrueOrFalseFlag.TRUE.getCode());
                                ctx.getUpdateLogs().add(log);
                                log = null;
                            }
                        } else {
                            // 全部分支完成，进入汇总节点
                            // flowCase.setCurrentLaneId(next.getFlowNode().getFlowLaneId());
                            List<FlowCase> siblingFlowCase = ctx.getSiblingFlowCase();
                            for (FlowCase aCase : siblingFlowCase) {
                                aCase.setCurrentLaneId(next.getFlowNode().getFlowLaneId());
                            }
                            ctx.getParentState().incrStepCount();
                            ctx.getParentState().setNextNode(next);
                            ctx.getParentState().setStepType(stepType);
                        }
                    } else {
                        // 下个节点不是分支汇总节点，正常进入
                        ctx.setNextNode(next);
                    }
                    flowCase.incrStepCount();
                }

                if (next.getExpectStatus() == FlowCaseStatus.FINISHED && subject == null) {
                    //显示任务跟踪语句
                    subject = new FlowSubject();
                }
                break;
            case REJECT_STEP:
                if (currentNode.getFlowNode().getNodeLevel() < 1) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                            "flow node step error");
                }

                flowService.fixupUserInfoInContext(ctx, firedUser);
                templateMap.put("processorName", firedUser.getNickName());

                flowStateProcessor.rejectToNode(ctx, gotoLevel, currentNode);
                // next = ctx.getNextNode();

                List<FlowGraphNode> nextNodes = ctx.getAllFlowState().stream()
                        .map(FlowCaseState::getNextNode).filter(Objects::nonNull).collect(Collectors.toList());
                if (nextNodes.size() == 0) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_REJECT_NODE_NOT_ENTER,
                            "reject node not found");
                }

                FlowGraphNode rejectToNode = nextNodes.get(0);

                // 驳回需要一个特殊的日志类型，用于在跟踪上特殊显示
                FlowEventLog rejectLog = new FlowEventLog();
                rejectLog.setId(flowEventLogProvider.getNextId());
                rejectLog.setFlowMainId(flowGraph.getFlow().getFlowMainId());
                rejectLog.setFlowVersion(flowGraph.getFlow().getFlowVersion());
                rejectLog.setNamespaceId(flowGraph.getFlow().getNamespaceId());
                rejectLog.setFlowButtonId(this.getFiredButtonId());
                rejectLog.setFlowNodeId(currentNode.getFlowNode().getId());
                rejectLog.setParentId(0L);
                rejectLog.setFlowCaseId(flowCase.getId());
                rejectLog.setStepCount(oldStepCount);
                rejectLog.setLogType(FlowLogType.REJECT_TRACKER.getCode());
                rejectLog.setLogTitle("");
                rejectLog.setButtonFiredStep(stepType.getCode());//mark as transfer log
                if (!Objects.equals(rejectToNode.getFlowNode().getFlowLaneId(), currentLane.getFlowLane().getId())) {
                    rejectLog.setCrossLaneRejectFlag(1L);
                }
                ctx.getLogs().add(rejectLog);

                flowCase.setRejectNodeId(currentNode.getFlowNode().getId());
                flowCase.setRejectCount(flowCase.getRejectCount() + 1);
                flowCase.setStepCount(flowCase.getStepCount() + 1);

                tracker = null;

                break;
            case TRANSFER_STEP:
                next = currentNode;
                ctx.setNextNode(next);

                if (currentNode.getTrackTransferLeave() != null) {
                    currentNode.getTrackTransferLeave().fireAction(ctx, ctx.getCurrentEvent());
                }

                log = new FlowEventLog();
                log.setId(flowEventLogProvider.getNextId());
                log.setFlowMainId(flowGraph.getFlow().getFlowMainId());
                log.setFlowVersion(flowGraph.getFlow().getFlowVersion());//get real version
                log.setNamespaceId(flowGraph.getFlow().getNamespaceId());
                if (ctx.getCurrentEvent() != null) {
                    log.setFlowButtonId(ctx.getCurrentEvent().getFiredButtonId());
                }
                log.setFlowNodeId(next.getFlowNode().getId());
                log.setParentId(0L);
                log.setFlowCaseId(ctx.getFlowCase().getId());
                if (cmd.getEntitySel() != null && cmd.getEntitySel().size() == 1) {
                    log.setFlowUserId(cmd.getEntitySel().get(0).getEntityId());
                }
                log.setStepCount(ctx.getFlowCase().getStepCount());
                log.setLogType(FlowLogType.NODE_ENTER.getCode());
                log.setLogTitle("");
                log.setButtonFiredStep(stepType.getCode());//mark as transfer log
                ctx.getLogs().add(log);
                log = null;

                //Remove the old logs
                log = flowEventLogProvider.getValidEnterStep(firedUser.getId(), ctx.getFlowCase());
                if (null != log) {
                    log.setStepCount(-1L); // mark as invalid
                    ctx.getUpdateLogs().add(log);
                    log = null;
                }

                break;
            case COMMENT_STEP:
                next = currentNode;
                ctx.setNextNode(next);

                if (subject == null) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR,
                            "flow node step param error");
                }

                tracker = new FlowEventLog();

                if (FlowUserType.fromCode(btn.getFlowButton().getFlowUserType()) != FlowUserType.APPLIER) {
                    flowService.fixupUserInfoInContext(ctx, firedUser);
                    templateMap.put("processorName", firedUser.getNickName());
                }
                if (subject.getContent() != null && subject.getContent().trim().length() > 0) {
                    templateMap.put("content", subject.getContent().trim());
                    tracker.setSubjectId(subject.getId());
                } else if (cmd.getImages() != null && cmd.getImages().size() > 0) {
                    tracker.setSubjectId(subject.getId());
                    templateMap.put("imageCount", String.valueOf(cmd.getImages().size()));
                }

                String template = flowService.getFireButtonTemplate(stepType, templateMap);
                tracker.setLogContent(template);
                tracker.setStepCount(ctx.getFlowCase().getStepCount());

                break;
            case ABSORT_STEP:
                next = flowGraph.getEndNode();

                for (FlowCaseState flowCaseState : ctx.getAllFlowState()) {
                    flowCaseState.setNextNode(next);
                    flowCaseState.setStepType(stepType);
                    flowCaseState.incrStepCount();
                }
                break;
            case REMINDER_STEP:
                next = currentNode;
                ctx.setNextNode(next);

                logType = FlowLogType.NODE_REMIND;
                log = new FlowEventLog();
                log.setFlowMainId(flowGraph.getFlow().getFlowMainId());
                log.setFlowVersion(flowGraph.getFlow().getFlowVersion());
                log.setNamespaceId(flowGraph.getFlow().getNamespaceId());
                log.setFlowCaseId(ctx.getFlowCase().getId());
                log.setFlowUserId(firedUser.getId());
                log.setLogType(logType.getCode());
                log.setFlowNodeId(next.getFlowNode().getId());
                List<FlowEventLog> remindLogs = flowEventLogProvider.findFiredEventsByLog(log);

                Integer remindCount = btn.getFlowButton().getRemindCount();
                if (remindCount == null || remindCount == 0) {
                    remindCount = 1;
                    btn.getFlowButton().setRemindCount(1);
                }
                if (remindLogs != null && remindLogs.size() >= remindCount) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_REMIND_ERROR,
                            "remind count overflow");
                }

                tracker = null;

                break;
            case SUPERVISE:
                next = currentNode;
                ctx.setNextNode(next);

                logType = FlowLogType.NODE_SUPERVISE;
                log = new FlowEventLog();
                log.setFlowMainId(flowGraph.getFlow().getFlowMainId());
                log.setFlowVersion(flowGraph.getFlow().getFlowVersion());
                log.setNamespaceId(flowGraph.getFlow().getNamespaceId());
                log.setFlowCaseId(ctx.getFlowCase().getId());
                log.setFlowUserId(firedUser.getId());
                log.setLogType(logType.getCode());
                log.setFlowNodeId(currentNode.getFlowNode().getId());
                List<FlowEventLog> superviseLogs = flowEventLogProvider.findFiredEventsByLog(log);

                Integer superviseCount = btn.getFlowButton().getRemindCount();
                if (superviseCount == null || superviseCount == 0) {
                    superviseCount = 1;
                    btn.getFlowButton().setRemindCount(1);
                }
                if (superviseLogs != null && superviseLogs.size() >= superviseCount) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_REMIND_ERROR,
                            "supervise count overflow");
                }
                tracker = null;

                break;
            case EVALUATE_STEP:
                break;
            case SUSPEND_STEP:
                next = currentNode;
                ctx.setNextNode(next);

                tracker = new FlowEventLog();
                tracker.setStepCount(ctx.getFlowCase().getStepCount());

                for (FlowCase aCase : ctx.getAllFlowCases()) {
                    aCase.setStatus(FlowCaseStatus.SUSPEND.getCode());
                }
                break;
            case ABORT_SUSPEND_STEP:
                next = currentNode;
                ctx.setNextNode(next);

                tracker = new FlowEventLog();
                tracker.setStepCount(ctx.getFlowCase().getStepCount());

                for (FlowCase aCase : ctx.getAllFlowCases()) {
                    aCase.setStatus(FlowCaseStatus.PROCESS.getCode());
                }
                // 重启定时消息及短信
                if (currentNode.getTickMessageAction() != null) {
                    FlowActionStatus status = FlowActionStatus.fromCode(currentNode.getTickMessageAction().getFlowAction().getStatus());
                    if (status == FlowActionStatus.ENABLED) {
                        currentNode.getTickMessageAction().fireAction(ctx, this);
                    }
                }
                if (currentNode.getTickSMSAction() != null) {
                    FlowActionStatus status = FlowActionStatus.fromCode(currentNode.getTickSMSAction().getFlowAction().getStatus());
                    if (status == FlowActionStatus.ENABLED) {
                        currentNode.getTickSMSAction().fireAction(ctx, this);
                    }
                }
                if (!next.getFlowNode().getAllowTimeoutAction().equals((byte) 0)) {
                    flowStateProcessor.createStepTimeout(ctx, next.getFlowNode());
                }
                break;
            default:
                break;
        }

        if (tracker != null && subject != null) {
            tracker.setId(flowEventLogProvider.getNextId());
            tracker.setFlowMainId(flowGraph.getFlow().getFlowMainId());
            tracker.setFlowVersion(flowGraph.getFlow().getFlowVersion());
            tracker.setNamespaceId(flowGraph.getFlow().getNamespaceId());
            tracker.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
            tracker.setParentId(0L);
            tracker.setFlowCaseId(ctx.getFlowCase().getId());
            tracker.setFlowUserId(ctx.getOperator().getId());
            tracker.setFlowUserName(ctx.getOperator().getNickName());
            if (isValidSubject(subject)) {
                tracker.setSubjectId(subject.getId());
            } else if (tracker.getSubjectId() == null) {
                tracker.setSubjectId(0L);// BUG #5431
            }

            tracker.setLogType(FlowLogType.NODE_TRACKER.getCode());

            tracker.setButtonFiredStep(stepType.getCode());
            tracker.setTrackerApplier(1L);
            tracker.setTrackerProcessor(1L);
            ctx.getLogs().add(tracker);
        }

        log = new FlowEventLog();
        log.setId(flowEventLogProvider.getNextId());
        log.setFlowMainId(flowGraph.getFlow().getFlowMainId());
        log.setFlowVersion(flowGraph.getFlow().getFlowVersion());
        log.setNamespaceId(flowGraph.getFlow().getNamespaceId());
        log.setFlowCaseId(ctx.getFlowCase().getId());
        log.setFlowUserId(firedUser.getId());
        log.setLogType(logType.getCode());
        log.setButtonFiredCount(0L);
        log.setLogContent(flowService.getButtonFireEventContentTemplate(stepType, buttonFireEventContentMap));

        //Important!!! not change this order
        if (logType == FlowLogType.BUTTON_FIRED) {
            //记录某一个节点的按钮被执行的次数
            List<FlowEventLog> likeLogs = flowEventLogProvider.findFiredEventsByLog(log);
            if (likeLogs != null && likeLogs.size() > 0) {
                log.setButtonFiredCount((long) likeLogs.size());
            }
        }

        log.setFlowButtonId(btn.getFlowButton().getId());
        if (next != null) {
            log.setFlowNodeId(next.getFlowNode().getId());
        }

        log.setParentId(0L);
        log.setFlowUserName(firedUser.getNickName());

        if (subject != null) {
            log.setSubjectId(subject.getId());
        }

        log.setButtonFiredStep(stepType.getCode());
        log.setButtonFiredFromNode(currentNode.getFlowNode().getId());
        log.setStepCount(oldStepCount);

        ctx.getLogs().add(log);    //added but not save to database now.
    }

    private boolean isValidSubject(FlowSubject subject) {
        return (subject.getContent() != null && subject.getContent().length() > 0)
                || (subject.getAttachments() != null && subject.getAttachments().size() > 0);
    }

    private void stepParentState(FlowCaseState ctx, FlowGraphNode nextNode) {
        FlowGraph flowGraph = ctx.getFlowGraph();
        FlowCaseState parentState = ctx.getParentState();
        if (parentState != null && parentState.getFlowCase().getEndNodeId().equals(nextNode.getFlowNodeId())) {
            parentState.flushCurrentNode(nextNode);
            parentState.flushCurrentLane(flowGraph.getGraphLane(nextNode.getFlowLaneId()));
            stepParentState(parentState, nextNode);
        }
    }

    @Override
    public FlowUserType getUserType() {
        return userType;
    }

    @Override
    public FlowEventType getEventType() {
        return FlowEventType.BUTTON_FIRED;
    }

    @Override
    public Long getFiredUserId() {
        return firedUser.getId();
    }

    public UserInfo getFiredUser() {
        return firedUser;
    }

    public void setFiredUser(UserInfo firedUser) {
        this.firedUser = firedUser;
    }

    public void setUserType(FlowUserType userType) {
        this.userType = userType;
    }

    public FlowFireButtonCommand getCmd() {
        return cmd;
    }

    public void setCmd(FlowFireButtonCommand cmd) {
        this.cmd = cmd;
    }

    @Override
    public FlowSubject getSubject() {
        return subject;
    }

    public void setSubject(FlowSubject subject) {
        this.subject = subject;
    }

    @Override
    public Long getFiredButtonId() {
        return cmd.getButtonId();
    }

    @Override
    public Long getNextNodeId() {
        return cmd.getNextNodeId();
    }

    @Override
    public List<FlowEntitySel> getEntitySel() {
        return cmd.getEntitySel();
    }
}
