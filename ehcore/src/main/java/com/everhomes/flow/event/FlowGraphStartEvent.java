package com.everhomes.flow.event;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowGraphLink;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowSubject;
import com.everhomes.rest.flow.*;

import java.util.ArrayList;
import java.util.List;

public class FlowGraphStartEvent extends AbstractFlowGraphEvent {
	private Long firedUserId;
	
	@Override
	public FlowUserType getUserType() {
		return FlowUserType.APPLIER;
	}

	@Override
	public FlowEventType getEventType() {
		return FlowEventType.STEP_START;
	}

	@Override
	public Long getFiredUserId() {
		return firedUserId;
	}

	public void setFiredUserId(Long firedUserId) {
		this.firedUserId = firedUserId;
	}

	@Override
	public void fire(FlowCaseState ctx) {
        FlowGraphNode startNode = ctx.getFlowGraph().getStartNode();
        List<FlowGraphLink> linksOut = startNode.getLinksOut();

        FlowGraphNode next;
        if (linksOut.size() > 0) {
            next = linksOut.get(0).getToNode(ctx, this);
        } else {
            startNode = ctx.getFlowGraph().getNodes().get(0);
            next = ctx.getFlowGraph().getNodes().get(1);
        }

        if (FlowNodeType.fromCode(next.getFlowNode().getNodeType()) == FlowNodeType.CONDITION_FRONT) {
            next.stepEnter(ctx, startNode);
            next = null;
        }

		ctx.setCurrentNode(startNode);
		ctx.setNextNode(next);
		ctx.setStepType(FlowStepType.APPROVE_STEP);
		ctx.getFlowCase().setStatus(FlowCaseStatus.PROCESS.getCode());
		ctx.getFlowCase().setStepCount(ctx.getFlowCase().getStepCount() + 1L);
		ctx.getFlowCase().setCurrentNodeId(startNode.getFlowNode().getId());
	}

	@Override
	public Long getFiredButtonId() {
		return 0L;
	}

	@Override
	public List<FlowEntitySel> getEntitySel() {
		return new ArrayList<>();
	}

	@Override
	public FlowSubject getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

}
