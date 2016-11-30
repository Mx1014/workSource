package com.everhomes.flow;

import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.util.RuntimeErrorException;

public class FlowGraphNodeNormal extends FlowGraphNode {
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
		FlowStepType fromStep = ctx.getStepType();
		FlowGraphNode curr = ctx.getCurrentNode();
		
		if(curr.getFlowNode().getNodeLevel() >= 1) {
			ctx.getFlowCase().setStatus(FlowCaseStatus.PROCESS.getCode());
		}
		
		switch(fromStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			if(curr.getTrackApproveEnter() != null) {
				curr.getTrackApproveEnter().fireAction(ctx, null);	
			}
			
			break;
		case REJECT_STEP:
			if(curr.getTrackRejectEnter() != null) {
				curr.getTrackRejectEnter().fireAction(ctx, null);	
			}
			
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
	public void stepLeave(FlowCaseState ctx, FlowGraphNode to)
			throws FlowStepErrorException {
		FlowStepType fromStep = ctx.getStepType();
		FlowGraphNode curr = ctx.getCurrentNode();
		switch(fromStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			break;
		case REJECT_STEP:
			break;
		case TRANSFER_STEP:
			if(curr.getTrackTransferLeave() != null) {
				curr.getTrackTransferLeave().fireAction(ctx, null);	
			}
			
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

}
