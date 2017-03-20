package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.user.User;
import com.everhomes.util.RuntimeErrorException;

public class FlowGraphAutoStepEvent implements FlowGraphEvent {
	FlowAutoStepDTO stepDTO;
	private Long firedUserId;
	private FlowEventLogProvider flowEventLogProvider;
	private FlowButtonProvider flowButtonProvider;
	
	public FlowGraphAutoStepEvent() {
		this(null);
	}
	
	public FlowGraphAutoStepEvent(FlowAutoStepDTO o) {
		firedUserId = User.SYSTEM_UID;
		this.stepDTO = o;
		flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
		flowButtonProvider = PlatformContext.getComponent(FlowButtonProvider.class);
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
		FlowCase flowCase = ctx.getFlowCase();
		
		//current state change to next step
		FlowGraphNode current = ctx.getCurrentNode();
		FlowGraphNode next = null;
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
			ctx.setNextNode(next);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			break;
		case REJECT_STEP:
			if(current.getFlowNode().getNodeLevel() < 1) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR, "flow node step error");
			}
			next = ctx.getFlowGraph().getNodes().get(current.getFlowNode().getNodeLevel()-1);
			ctx.setNextNode(next);
			
			flowCase.setRejectNodeId(current.getFlowNode().getId());
			flowCase.setRejectCount(flowCase.getRejectCount() + 1);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			break;
		case ABSORT_STEP:
			next = ctx.getFlowGraph().getNodes().get(ctx.getFlowGraph().getNodes().size()-1);
			ctx.setNextNode(next);
			flowCase.setStepCount(flowCase.getStepCount() + 1l);
			break;
		default:
			break;
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

}
