package com.everhomes.flow;

import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.util.RuntimeErrorException;

public class FlowGraphNodeNormal extends FlowGraphNode {
	@Override
	public void stepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException {
		FlowStepType fromStep = ctx.getStepType();
		FlowGraphNode curr = ctx.getCurrentNode();
		switch(fromStep) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			curr.getTrackApproveEnter().fireAction(ctx, null);
			break;
		case REJECT_STEP:
			curr.getTrackRejectEnter().fireAction(ctx, null);
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
			curr.getTrackTransferLeave().fireAction(ctx, null);
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
