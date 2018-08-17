package com.everhomes.flow.event;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;

import java.util.*;
import java.util.stream.Collectors;

public class FlowGraphAutoStepEvent extends AbstractFlowGraphEvent {

    private FlowAutoStepDTO stepDTO;
    private Long firedUserId;
    private FlowSubject subject;

    private FlowEventLogProvider flowEventLogProvider;
    private UserService userService;
    private FlowService flowService;
    private FlowStateProcessor flowStateProcessor;

    public FlowGraphAutoStepEvent(FlowAutoStepDTO o) {
        firedUserId = User.SYSTEM_UID;
        if (o.getOperatorId() != null) {
            firedUserId = o.getOperatorId();
        }
        this.stepDTO = o;
        flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
        userService = PlatformContext.getComponent(UserService.class);
        flowService = PlatformContext.getComponent(FlowService.class);
        flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
    }

    @Override
    public FlowUserType getUserType() {
        return FlowUserType.PROCESSOR;
    }

    @Override
    public FlowEventType getEventType() {
        return FlowEventType.fromCode(stepDTO.getEventType());
    }

    public void setFiredUserId(Long firedUserId) {
        this.firedUserId = firedUserId;
    }

    @Override
    public Long getFiredUserId() {
        return this.firedUserId;
    }

    @Override
    public Long getFiredButtonId() {
        return null;
    }

    @Override
    public List<FlowEntitySel> getEntitySel() {
        return new ArrayList<>();
    }

    @Override
    public void fire(FlowCaseState ctx) {
        FlowGraph flowGraph = ctx.getFlowGraph();

        FlowEventLog log;
        FlowEventLog tracker = null;

        FlowStepType stepType = ctx.getStepType();
        FlowCase flowCase = ctx.getFlowCase();
        Long oldStepCount = flowCase.getStepCount();

        Integer goToLevel = 0;
        FlowGraphNode targetNode = flowGraph.getGraphNode(stepDTO.getTargetNodeId());
        if (targetNode != null) {
            goToLevel = targetNode.getFlowNode().getNodeLevel();
        }

        //currentNode state change to next step
        FlowGraphNode currentNode = ctx.getCurrentNode();
        FlowGraphNode next = null;

        FlowGraphLane currentLane = ctx.getCurrentLane();

        UserInfo applier = userService.getUserSnapshotInfo(flowCase.getApplyUserId());

        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("nodeName", currentNode.getFlowNode().getNodeName());
        templateMap.put("laneName", currentLane.getFlowLane().getDisplayName());
        templateMap.put("applierName", applier.getNickName());

        switch (stepType) {
            case NO_STEP:
                break;
            case APPROVE_STEP:
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

                tracker = new FlowEventLog();
                tracker.setLogContent(flowService.getStepMessageTemplate(stepType, next.getExpectStatus(), ctx.getCurrentEvent(), templateMap));
                tracker.setStepCount(ctx.getFlowCase().getStepCount());
                if (next.getExpectStatus() == FlowCaseStatus.FINISHED && subject == null) {
                    //显示任务跟踪语句
                    subject = new FlowSubject();
                }
                break;
            case REJECT_STEP:
                if (currentNode.getFlowNode().getNodeLevel() < 1) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR, "flow node step error");
                }

                flowService.fixupUserInfoInContext(ctx, ctx.getOperator());
                templateMap.put("processorName", ctx.getOperator().getNickName());

                tracker = new FlowEventLog();
                tracker.setLogContent(flowService.getFireButtonTemplate(stepType, templateMap));
                tracker.setStepCount(ctx.getFlowCase().getStepCount());

                flowStateProcessor.rejectToNode(ctx, goToLevel, currentNode);

                boolean notFindNextNode = ctx.getAllFlowState().stream().allMatch(r -> r.getNextNode() == null);
                if (notFindNextNode) {
                    throw RuntimeErrorException.errorWith("", 1, "reject node not found");
                }

                if (subject == null) {
                    subject = new FlowSubject();
                }

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
                // flowCase.setStepCount(flowCase.getStepCount() + 1);

                break;
            case TRANSFER_STEP:
                FlowCaseStatus status = FlowCaseStatus.fromCode(flowCase.getStatus());
                if (status == FlowCaseStatus.ABSORTED || status == FlowCaseStatus.FINISHED) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                            "flowCase already absort or finished, flowCaseId=" + flowCase.getId());
                }

                next = currentNode;
                ctx.setNextNode(next);

                if (currentNode.getTrackTransferLeave() != null) {
                    currentNode.getTrackTransferLeave().fireAction(ctx, ctx.getCurrentEvent());
                }

                if (stepDTO instanceof FlowAutoStepTransferDTO) {
                    FlowAutoStepTransferDTO stepDTO = (FlowAutoStepTransferDTO) this.stepDTO;

                    // 转入
                    if (stepDTO.getTransferIn() != null) {
                        for (FlowEntitySel sel : stepDTO.getTransferIn()) {
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
                            log.setFlowUserId(sel.getEntityId());
                            log.setStepCount(ctx.getFlowCase().getStepCount());
                            log.setLogType(FlowLogType.NODE_ENTER.getCode());
                            log.setLogTitle("");
                            log.setButtonFiredStep(stepType.getCode());//mark as transfer log
                            ctx.getLogs().add(log);
                        }
                        log = null;
                    }

                    // 转出
                    if (stepDTO.getTransferOut() != null) {
                        for (FlowEntitySel sel : stepDTO.getTransferOut()) {
                            //Remove the old logs
                            log = flowEventLogProvider.getValidEnterStep(sel.getEntityId(), ctx.getFlowCase());
                            if (null != log) {
                                log.setStepCount(-1L); // mark as invalid
                                ctx.getUpdateLogs().add(log);
                            }
                        }
                        log = null;
                    }
                }
                break;
            case END_STEP:
            case ABSORT_STEP:
                status = FlowCaseStatus.fromCode(flowCase.getStatus());
                if (status == FlowCaseStatus.ABSORTED || status == FlowCaseStatus.FINISHED) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                            "flowCase already absort or finished, flowCaseId=" + flowCase.getId());
                }

                tracker = new FlowEventLog();
                if (ctx.getOperator() != null) {
                    templateMap.put("applierName", ctx.getOperator().getNickName());
                }

                next = flowGraph.getEndNode();

                tracker.setLogContent(flowService.getStepMessageTemplate(stepType, next.getExpectStatus(), ctx.getCurrentEvent(), templateMap));
                tracker.setStepCount(ctx.getFlowCase().getStepCount());
                if (subject == null) {
                    //显示任务跟踪语句
                    subject = new FlowSubject();
                }

                for (FlowCaseState flowCaseState : ctx.getAllFlowState()) {
                    flowCaseState.setNextNode(next);
                    flowCaseState.setStepType(stepType);
                    flowCaseState.getFlowCase().setStepCount(flowCaseState.getFlowCase().getStepCount() + 1);
                }

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
                break;
            case SUB_FLOW_REJECT_STEP:
                if (currentNode.getFlowNode().getNodeLevel() < 1) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR, "flow node step error");
                }

                flowService.fixupUserInfoInContext(ctx, ctx.getOperator());

                flowStateProcessor.rejectToNode(ctx, goToLevel, currentNode);

                notFindNextNode = ctx.getAllFlowState().stream().allMatch(r -> r.getNextNode() == null);
                if (notFindNextNode) {
                    throw RuntimeErrorException.errorWith("", 1, "reject node not found");
                }

                nextNodes = ctx.getAllFlowState().stream()
                        .map(FlowCaseState::getNextNode).filter(Objects::nonNull).collect(Collectors.toList());
                if (nextNodes.size() == 0) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_REJECT_NODE_NOT_ENTER,
                            "reject node not found");
                }

                rejectToNode = nextNodes.get(0);

                // 驳回需要一个特殊的日志类型，用于在跟踪上特殊显示
                rejectLog = new FlowEventLog();
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
                rejectLog.setButtonFiredStep(stepType.getCode());
                if (!Objects.equals(rejectToNode.getFlowNode().getFlowLaneId(), currentLane.getFlowLane().getId())) {
                    rejectLog.setCrossLaneRejectFlag(1L);
                }
                ctx.getLogs().add(rejectLog);

                flowCase.setRejectNodeId(currentNode.getFlowNode().getId());
                flowCase.setRejectCount(flowCase.getRejectCount() + 1);
                // flowCase.setStepCount(flowCase.getStepCount() + 1);

                break;
            default:
                break;
        }

        if (tracker != null && subject != null) {
            tracker.setId(flowEventLogProvider.getNextId());
            tracker.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
            tracker.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
            tracker.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
            tracker.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
            tracker.setParentId(0L);
            tracker.setFlowCaseId(ctx.getFlowCase().getId());
            tracker.setFlowUserId(ctx.getOperator().getId());
            tracker.setFlowUserName(ctx.getOperator().getNickName());
            if (subject.getContent() != null && !subject.getContent().isEmpty()) {
                tracker.setSubjectId(subject.getId());
            } else {
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
        log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
        log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
        log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
        if (next != null) {
            log.setFlowNodeId(next.getFlowNode().getId());
        }
        log.setParentId(0L);
        log.setFlowCaseId(ctx.getFlowCase().getId());
        log.setFlowUserId(ctx.getOperator().getId());
        log.setLogType(FlowLogType.AUTO_STEP.getCode());
        log.setButtonFiredStep(stepType.getCode());
        log.setButtonFiredFromNode(currentNode.getFlowNode().getId());
        ctx.getLogs().add(log);    //added but not save to database now.
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
    public FlowSubject getSubject() {
        return subject;
    }

    public void setSubject(FlowSubject subject) {
        this.subject = subject;
    }
}
