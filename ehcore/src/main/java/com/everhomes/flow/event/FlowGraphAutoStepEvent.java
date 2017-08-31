package com.everhomes.flow.event;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowGraphAutoStepEvent implements FlowGraphEvent {
	FlowAutoStepDTO stepDTO;
	private Long firedUserId;
    private FlowSubject subject;

	private FlowEventLogProvider flowEventLogProvider;
	private FlowButtonProvider flowButtonProvider;
	private UserService userService;
	private FlowService flowService;
	
	public FlowGraphAutoStepEvent() {
		this(null);
	}
	
	public FlowGraphAutoStepEvent(FlowAutoStepDTO o) {
		firedUserId = User.SYSTEM_UID;
		if(o.getOperatorId() != null) {
			firedUserId = o.getOperatorId();
		}
		this.stepDTO = o;
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
		flowButtonProvider = PlatformContext.getComponent(FlowButtonProvider.class);
		userService = PlatformContext.getComponent(UserService.class);
		flowService = PlatformContext.getComponent(FlowService.class);
	}
	@Override
	public FlowUserType getUserType() {
		return FlowUserType.PROCESSOR;
	}

	@Override
	public FlowEventType getEventType() {
		return FlowEventType.STEP_TIMEOUT;
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
		return new ArrayList<FlowEntitySel>();
	}
	
	@Override
	public void fire(FlowCaseState ctx) {
		FlowStepType nextStep = FlowStepType.fromCode(stepDTO.getAutoStepType());
		ctx.setStepType(nextStep);
		
		FlowEventLog log = null;
		FlowEventLog tracker = null;
		FlowCase flowCase = ctx.getFlowCase();
		
		//current state change to next step
		FlowGraphNode current = ctx.getCurrentNode();
		FlowGraphNode next = null;
		// FlowSubject subject = null;
		
		UserInfo applier = userService.getUserSnapshotInfo(flowCase.getApplyUserId());
		Map<String, Object> templateMap = new HashMap<String, Object>();
		templateMap.put("nodeName", current.getFlowNode().getNodeName());
		templateMap.put("applierName", applier.getNickName());
		
		switch(nextStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			next = null;
			//TODO use id from graph
	    	FlowButton flowBtn = flowButtonProvider.findFlowButtonByStepType(stepDTO.getFlowNodeId()
	    			, stepDTO.getFlowVersion(), nextStep.getCode(), FlowUserType.PROCESSOR.getCode());
			
			if(!flowBtn.getGotoNodeId().equals(0l)) {
				next = ctx.getFlowGraph().getGraphNode(flowBtn.getGotoNodeId());
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
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			
			break;
		case REJECT_STEP:
			if(current.getFlowNode().getNodeLevel() < 1) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR, "flow node step error");
			}
			
			tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getFireButtonTemplate(nextStep, templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			
			next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel()-1);
			ctx.setNextNode(next);
			if(subject == null) {
				subject = new FlowSubject();
			}
			
			flowCase.setRejectNodeId(current.getFlowNode().getId());
			flowCase.setRejectCount(flowCase.getRejectCount() + 1);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			break;
		case END_STEP:
		case ABSORT_STEP:
			tracker = new FlowEventLog();
			if(ctx.getOperator() != null) {
                if (ctx.getOperator().getId() == User.SYSTEM_UID) {
                    templateMap.put("applierName", "系统");
                } else {
                    templateMap.put("applierName", ctx.getOperator().getNickName());
                }
            }
			next = ctx.getFlowGraph().getNodes().get(ctx.getFlowGraph().getNodes().size()-1);
			
			tracker.setLogContent(flowService.getStepMessageTemplate(nextStep, next.getExpectStatus(), ctx.getCurrentEvent().getUserType(), templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			if(subject == null) {
				//显示任务跟踪语句
				subject = new FlowSubject();
			}
			
			ctx.setNextNode(next);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			break;
		default:
			break;
		}

		if(tracker != null && subject != null) {
			tracker.setId(flowEventLogProvider.getNextId());
			tracker.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			tracker.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
			tracker.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			tracker.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
			tracker.setParentId(0l);
			tracker.setFlowCaseId(ctx.getFlowCase().getId());
			tracker.setFlowUserId(ctx.getOperator().getId());
			tracker.setFlowUserName(ctx.getOperator().getNickName());
			if(subject.getContent() != null && !subject.getContent().isEmpty()) {
				tracker.setSubjectId(subject.getId());	
			} else {
				tracker.setSubjectId(0l);// BUG #5431
			}
			
			tracker.setLogType(FlowLogType.NODE_TRACKER.getCode());
			
			tracker.setButtonFiredStep(nextStep.getCode());
			tracker.setTrackerApplier(1l);
			tracker.setTrackerProcessor(1l);	
			ctx.getLogs().add(tracker);
		}
		
		log = new FlowEventLog();
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
		log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
		log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
		log.setFlowNodeId(next.getFlowNode().getId());
		log.setParentId(0l);
		log.setFlowCaseId(ctx.getFlowCase().getId());
		log.setFlowUserId(ctx.getOperator().getId());
		log.setLogType(FlowLogType.AUTO_STEP.getCode());
		log.setButtonFiredStep(nextStep.getCode());
		log.setButtonFiredFromNode(current.getFlowNode().getId());
		ctx.getLogs().add(log);	//added but not save to database now.
	}

	@Override
	public FlowSubject getSubject() {
		return subject;
	}

    public void setSubject(FlowSubject subject) {
        this.subject = subject;
    }
}
