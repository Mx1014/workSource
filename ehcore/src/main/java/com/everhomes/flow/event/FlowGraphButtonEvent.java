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

public class FlowGraphButtonEvent implements FlowGraphEvent {
	private FlowUserType userType;
	private UserInfo firedUser;
	private FlowFireButtonCommand cmd;
	private FlowSubject subject;
	
	private FlowEventLogProvider flowEventLogProvider;
	private UserService userService;
	private FlowService flowService;

    public FlowGraphButtonEvent() {
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
		userService = PlatformContext.getComponent(UserService.class);
		flowService = PlatformContext.getComponent(FlowService.class);
		// flowButtonProvider = PlatformContext.getComponent(FlowButtonProvider.class);
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
	public List<FlowEntitySel> getEntitySel() {
		return cmd.getEntitySel();
	}

	@Override
	public void fire(FlowCaseState ctx) {
        FlowGraphButton btn = ctx.getFlowGraph().getGraphButton(cmd.getButtonId());
        Integer gotoLevel = btn.getFlowButton().getGotoLevel();

        FlowStepType nextStep = FlowStepType.fromCode(btn.getFlowButton().getFlowStepType());
        ctx.setStepType(nextStep);

        FlowLogType logType = FlowLogType.BUTTON_FIRED;
        FlowEventLog log = null;
        FlowEventLog tracker = null;
        FlowCase flowCase = ctx.getFlowCase();
        Long oldStep = flowCase.getStepCount();

        //current state change to next step
        FlowGraphNode current = ctx.getCurrentNode();
        FlowGraphNode next = null;

        UserInfo applier = userService.getUserSnapshotInfo(flowCase.getApplyUserId());
        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("nodeName", current.getFlowNode().getNodeName());
        templateMap.put("applierName", applier.getNickName());

        switch(nextStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			next = null;
			if(!gotoLevel.equals(0) && gotoLevel < ctx.getFlowGraph().getNodes().size()) {
				next = ctx.getFlowGraph().getNodes().get(gotoLevel);
			}
			if(next == null) {
				//get next level
				next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel()+1);
			}
			
			tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getStepMessageTemplate(nextStep, next.getExpectStatus(), ctx.getCurrentEvent().getUserType(), templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			if(next.getExpectStatus() == FlowCaseStatus.FINISHED && subject == null) {
				//显示任务跟踪语句
				subject = new FlowSubject();
			}
			
			ctx.setNextNode(next);
			flowCase.setStepCount(flowCase.getStepCount() + 1L);
			
			break;
		case REJECT_STEP:
			if(current.getFlowNode().getNodeLevel() < 1) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR,
                        "flow node step error");
			}

			flowService.fixupUserInfoInContext(ctx, firedUser);
            templateMap.put("processorName", firedUser.getNickName());

            tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getFireButtonTemplate(nextStep, templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());

            if (!gotoLevel.equals(0) && gotoLevel < ctx.getFlowGraph().getNodes().size()) {
                next = ctx.getFlowGraph().getNodes().get(gotoLevel);
            } else {
                next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel() - 1);
            }

			ctx.setNextNode(next);
			if(subject == null) {
				subject = new FlowSubject();
			}
			
			flowCase.setRejectNodeId(current.getFlowNode().getId());
			flowCase.setRejectCount(flowCase.getRejectCount() + 1);
			flowCase.setStepCount(flowCase.getStepCount() + 1);
			
			break;
		case TRANSFER_STEP:
			next = current;
			ctx.setNextNode(next);
			
			if(current.getTrackTransferLeave() != null) {
				current.getTrackTransferLeave().fireAction(ctx, ctx.getCurrentEvent());	
			} else {
				tracker = new FlowEventLog();
				tracker.setLogContent(flowService.getFireButtonTemplate(nextStep, templateMap));
				tracker.setStepCount(ctx.getFlowCase().getStepCount());
			}
			
			log = new FlowEventLog();
			log.setId(flowEventLogProvider.getNextId());
			log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());//get real version
			log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			if(ctx.getCurrentEvent() != null) {
				log.setFlowButtonId(ctx.getCurrentEvent().getFiredButtonId());	
			}
			log.setFlowNodeId(next.getFlowNode().getId());
			log.setParentId(0L);
			log.setFlowCaseId(ctx.getFlowCase().getId());
			if(cmd.getEntitySel() != null && cmd.getEntitySel().size() == 1) {
				log.setFlowUserId(cmd.getEntitySel().get(0).getEntityId());	
			}
			log.setStepCount(ctx.getFlowCase().getStepCount());
			log.setLogType(FlowLogType.NODE_ENTER.getCode());
			log.setLogTitle("");
			log.setButtonFiredStep(nextStep.getCode());//mark as transfer log
			ctx.getLogs().add(log);
			log = null;
			
			//Remove the old logs
			log = flowEventLogProvider.getValidEnterStep(firedUser.getId(), ctx.getFlowCase());
			if(null != log) {
				log.setStepCount(-1L); // mark as invalid
				ctx.getUpdateLogs().add(log);
				log = null;
			}

			break;
		case COMMENT_STEP:
			next = current;
			ctx.setNextNode(next);
			
			if(subject == null) {
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

            String template = flowService.getFireButtonTemplate(nextStep, templateMap);
            tracker.setLogContent(template);
            tracker.setStepCount(ctx.getFlowCase().getStepCount());

			break;
		case ABSORT_STEP:
			tracker = new FlowEventLog();
			if(ctx.getOperator() != null) {
				templateMap.put("applierName", ctx.getOperator().getNickName());	
			}
			next = ctx.getFlowGraph().getNodes().get(ctx.getFlowGraph().getNodes().size()-1);
			
			tracker.setLogContent(flowService.getStepMessageTemplate(nextStep, next.getExpectStatus(), ctx.getCurrentEvent().getUserType(), templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			if(subject == null) {
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
			log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
			log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
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
            if(remindLogs != null && remindLogs.size() >= remindCount) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_REMIND_ERROR,
                        "remind count overflow");
			}
			
			tracker = null;
			
			break;
		case EVALUATE_STEP:
			//TODO save evaluate score
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
        if (null != btn.getScripts()) {
            for (FlowGraphAction action : btn.getScripts()) {
                FlowActionStatus status = FlowActionStatus.fromCode(action.getFlowAction().getStatus());
                if (status == FlowActionStatus.ENABLED) {
                    action.fireAction(ctx, ctx.getCurrentEvent());
                }
            }
        }

        if(tracker != null && subject != null) {
			tracker.setId(flowEventLogProvider.getNextId());
			tracker.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			tracker.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
			tracker.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			tracker.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
			tracker.setParentId(0L);
			tracker.setFlowCaseId(ctx.getFlowCase().getId());
			tracker.setFlowUserId(ctx.getOperator().getId());
			tracker.setFlowUserName(ctx.getOperator().getNickName());
			if(subject.getContent() != null && !subject.getContent().isEmpty()) {
				tracker.setSubjectId(subject.getId());	
			} else if (tracker.getSubjectId() == null) {
				tracker.setSubjectId(0L);// BUG #5431
			}
			
			tracker.setLogType(FlowLogType.NODE_TRACKER.getCode());
			
			tracker.setButtonFiredStep(nextStep.getCode());
			tracker.setTrackerApplier(1L);
			tracker.setTrackerProcessor(1L);
			ctx.getLogs().add(tracker);
		}
		
		log = new FlowEventLog();
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
		log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
		log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
		log.setFlowCaseId(ctx.getFlowCase().getId());
		log.setFlowUserId(firedUser.getId());
		log.setLogType(logType.getCode());
        log.setButtonFiredCount(0L);
		
		//Important!!! not change this order
		if(logType == FlowLogType.BUTTON_FIRED) {
			//记录某一个节点的按钮被执行的次数
			List<FlowEventLog> likeLogs = flowEventLogProvider.findFiredEventsByLog(log);
			if(likeLogs != null && likeLogs.size() > 0) {
				log.setButtonFiredCount((long) likeLogs.size());
			}
		}
		
		log.setFlowButtonId(btn.getFlowButton().getId());
		log.setFlowNodeId(next.getFlowNode().getId());
		log.setParentId(0L);
		log.setFlowUserName(firedUser.getNickName());
//		if(FlowEntityType.FLOW_SELECTION.getCode().equals(cmd.getFlowEntityType())) {
//			log.setFlowSelectionId(cmd.getEntityId());
//		}
		
		if(subject != null) {
			log.setSubjectId(subject.getId());
		}
		
		log.setButtonFiredStep(nextStep.getCode());
		log.setButtonFiredFromNode(current.getFlowNode().getId());
		log.setStepCount(oldStep);
		
		ctx.getLogs().add(log);	//added but not save to database now.
	}
}
