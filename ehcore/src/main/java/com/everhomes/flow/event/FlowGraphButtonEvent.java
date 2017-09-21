package com.everhomes.flow.event;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowGraphButtonEvent extends AbstractFlowGraphEvent {

    private FlowUserType userType;
    private UserInfo firedUser;
    private FlowFireButtonCommand cmd;
    private FlowSubject subject;

    private FlowEventLogProvider flowEventLogProvider;
    private UserService userService;
    private FlowService flowService;
    private FlowCaseProvider flowCaseProvider;
    private FlowStateProcessor flowStateProcessor;

    public FlowGraphButtonEvent() {
        flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
        userService = PlatformContext.getComponent(UserService.class);
        flowService = PlatformContext.getComponent(FlowService.class);
        flowCaseProvider = PlatformContext.getComponent(FlowCaseProvider.class);
        flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
    }

    @Override
    public void fire(FlowCaseState ctx) {
        FlowGraph flowGraph = ctx.getFlowGraph();
        FlowGraphButton btn = flowGraph.getGraphButton(cmd.getButtonId());
        Integer gotoLevel = btn.getFlowButton().getGotoLevel();

        // FlowStepType nextStep = FlowStepType.fromCode(btn.getFlowButton().getFlowStepType());
        // ctx.setStepType(nextStep);

        FlowLogType logType = FlowLogType.BUTTON_FIRED;
        FlowEventLog log = null;
        FlowEventLog tracker = null;

        FlowStepType stepType = ctx.getStepType();
        FlowCase flowCase = ctx.getFlowCase();
        Long oldStepCount = flowCase.getStepCount();

        //current state change to next step
        FlowGraphNode current = ctx.getCurrentNode();
        FlowGraphNode next = null;

        FlowGraphLane currentLane = ctx.getCurrentLane();

        UserInfo applier = userService.getUserSnapshotInfo(flowCase.getApplyUserId());

        Map<String, Object> templateMap = new HashMap<>();
        // templateMap.put("nodeName", current.getFlowNode().getNodeName());
        templateMap.put("laneName", currentLane.getFlowLane().getDisplayName());
        templateMap.put("applierName", applier.getNickName());

        switch (stepType) {
            case NO_STEP:
                break;
            case APPROVE_STEP:
                // flow-2.0 之前的版本，没有link
                if (current.getLinksOut().size() == 0) {
                    if (!gotoLevel.equals(0) && gotoLevel < flowGraph.getNodes().size()) {
                        next = flowGraph.getNodes().get(gotoLevel);
                    }
                    if (next == null) {
                        //get next level
                        next = flowGraph.getNodes().get(current.getFlowNode().getNodeLevel() + 1);
                    }
                }
                // 节点只有一个出口
                else if (current.getLinksOut().size() == 1) {
                    next = current.getLinksOut().get(0).getToNode(ctx, this);
                } else {
                    throw RuntimeErrorException.errorWith("", 1, "flow graph link error");
                }

                ctx.setNextNode(next);

                boolean isConditionNode = FlowNodeType.fromCode(next.getFlowNode().getNodeType()) == FlowNodeType.CONDITION_FRONT;
                // 条件节点
                if (isConditionNode) {
                    next.stepEnter(ctx, current);
                }

                tracker = new FlowEventLog();
                tracker.setLogContent(flowService.getStepMessageTemplate(stepType, next.getExpectStatus(), ctx.getCurrentEvent().getUserType(), templateMap));
                tracker.setStepCount(ctx.getFlowCase().getStepCount());
                if (next.getExpectStatus() == FlowCaseStatus.FINISHED && subject == null) {
                    //显示任务跟踪语句
                    subject = new FlowSubject();
                }

                flowCase.setStepCount(flowCase.getStepCount() + 1L);
                break;
            case REJECT_STEP:
                if (current.getFlowNode().getNodeLevel() < 1) {
                    throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                            "flow node step error");
                }

                flowService.fixupUserInfoInContext(ctx, firedUser);
                templateMap.put("processorName", firedUser.getNickName());

                tracker = new FlowEventLog();
                tracker.setLogContent(flowService.getFireButtonTemplate(stepType, templateMap));
                tracker.setStepCount(ctx.getFlowCase().getStepCount());

                // flow-2.0 之前的版本，没有link
                List<FlowGraphLink> linksIn = current.getLinksIn();
                if (linksIn.size() == 0) {
                    if (!gotoLevel.equals(0) && gotoLevel < flowGraph.getNodes().size()) {
                        next = flowGraph.getNodes().get(gotoLevel);
                    } else if (linksIn.size() == 0){
                        next = flowGraph.getNodes().get(current.getFlowNode().getNodeLevel() - 1);
                    }
                } else {
                    // 驳回到上一级
                    if (gotoLevel.equals(0) || /*指定节点就是上一个节点*/(linksIn.size() == 1 && linksIn.get(0).getFromNode(ctx, this).equals(flowGraph.getGraphNode(gotoLevel)))) {
                        // 当前节点不是汇总节点
                        if (linksIn.size() == 1) {
                            next = linksIn.get(0).getFromNode(ctx, this);
                        }
                        // 当前节点是汇总节点
                        else {
                            List<FlowCase> subFlowCase = flowCaseProvider.listFlowCaseByParentId(ctx.getFlowCase().getId());
                            for (FlowCase subCase : subFlowCase) {
                                FlowCaseState subCtx = flowStateProcessor.prepareSubFlowCaseStart(ctx.getOperator(), subCase);
                                ctx.setCurrentNode(current);
                                ctx.setNextNode(current.getFlowLink(subCase.getEndLinkId()).getFromNode(ctx, this));
                                ctx.getChildStates().add(subCtx);
                            }
                        }
                    }
                    // 驳回到指定节点
                    else {
                        // FlowGraphNode nextNode = flowGraph.getGraphNode(gotoLevel);


                        next = flowStateProcessor.rejectToNode(ctx, gotoLevel, current);


                        /*// 兄弟case都+1
                        for (FlowCaseState flowCaseState : ctx.getParentState().getChildStates()) {
                            flowCaseState.getFlowCase().setStepCount(flowCaseState.getFlowCase().getStepCount() + 1);
                        }
                        // TODO reject
                        ctx.setNextNode(nextNode);*/
                    }
                }

                if (subject == null) {
                    subject = new FlowSubject();
                }

                flowCase.setRejectNodeId(current.getFlowNode().getId());
                flowCase.setRejectCount(flowCase.getRejectCount() + 1);
                flowCase.setStepCount(flowCase.getStepCount() + 1);

                break;
            case TRANSFER_STEP:
                next = current;
                ctx.setNextNode(next);

                if (current.getTrackTransferLeave() != null) {
                    current.getTrackTransferLeave().fireAction(ctx, ctx.getCurrentEvent());
                } else {
                    tracker = new FlowEventLog();
                    tracker.setLogContent(flowService.getFireButtonTemplate(stepType, templateMap));
                    tracker.setStepCount(ctx.getFlowCase().getStepCount());
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
                next = current;
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
                tracker = new FlowEventLog();
                if (ctx.getOperator() != null) {
                    templateMap.put("applierName", ctx.getOperator().getNickName());
                }

                next = flowGraph.getNodes().get(flowGraph.getNodes().size() - 1);

                // 现在在子分支进行，找到最外层的flowCase的end节点
                FlowCaseState parentState = ctx.getParentState();
                while (parentState != null) {
                    parentState.setNextNode(parentState.getFlowGraph().getNodes().get(parentState.getFlowGraph().getNodes().size() - 1));
                    // TODO step count +1
                    // next = parentState.getFlowGraph().getNodes().get(parentState.getFlowGraph().getNodes().size() - 1);
                    parentState = parentState.getParentState();
                }

                tracker.setLogContent(flowService.getStepMessageTemplate(stepType, next.getExpectStatus(), ctx.getCurrentEvent().getUserType(), templateMap));
                tracker.setStepCount(ctx.getFlowCase().getStepCount());
                if (subject == null) {
                    //显示任务跟踪语句
                    subject = new FlowSubject();
                }

                ctx.setNextNode(next);
                flowCase.setStepCount(flowCase.getStepCount() + 1L);

                break;
            case REMINDER_STEP:
                next = current;
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
            case EVALUATE_STEP:
                //TODO save isTrue score
                break;
            default:
                break;
        }

        //button actions
        if (null != btn.getMessage()) {
            FlowActionStatus status = FlowActionStatus.fromCode(btn.getMessage().getFlowAction().getStatus());
            if (status == FlowActionStatus.ENABLED) {
                btn.getMessage().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (null != btn.getSms()) {
            FlowActionStatus status = FlowActionStatus.fromCode(btn.getSms().getFlowAction().getStatus());
            if (status == FlowActionStatus.ENABLED) {
                btn.getSms().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (null != btn.getTracker()) {
            FlowActionStatus status = FlowActionStatus.fromCode(btn.getTracker().getFlowAction().getStatus());
            if (status == FlowActionStatus.ENABLED) {
                btn.getTracker().fireAction(ctx, ctx.getCurrentEvent());
            }
        }
        if (null != btn.getScripts()) {
            for (FlowGraphAction action : btn.getScripts()) {
                FlowActionStatus status = FlowActionStatus.fromCode(action.getFlowAction().getStatus());
                if (status == FlowActionStatus.ENABLED) {
                    action.fireAction(ctx, ctx.getCurrentEvent());
                }
            }
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
            if (subject.getContent() != null && !subject.getContent().isEmpty()) {
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
//		if(FlowEntityType.FLOW_SELECTION.getCode().equals(cmd.getFlowEntityType())) {
//			log.setFlowSelectionId(cmd.getEntityId());
//		}

        if (subject != null) {
            log.setSubjectId(subject.getId());
        }

        log.setButtonFiredStep(stepType.getCode());
        log.setButtonFiredFromNode(current.getFlowNode().getId());
        log.setStepCount(oldStepCount);

        ctx.getLogs().add(log);    //added but not save to database now.
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
