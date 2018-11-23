package com.everhomes.flow.event;

import com.everhomes.flow.*;
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
		FlowGraph flowGraph = ctx.getFlowGraph();

		FlowGraphNode startNode = flowGraph.getStartNode();
		FlowGraphLane currentLane = flowGraph.getGraphLane(startNode.getFlowLaneId());

		List<FlowGraphLink> linksOut = startNode.getLinksOut();

        FlowGraphNode next;
        if (linksOut.size() > 0) {
            next = linksOut.get(0).getToNode(ctx, this);
        } else {
            startNode = flowGraph.getNodes().get(0);
            next = flowGraph.getNodes().get(1);
        }

        ctx.setNextNode(next);
        ctx.setStepType(FlowStepType.APPROVE_STEP);
        ctx.setFlowCaseStatus(FlowCaseStatus.PROCESS);
		ctx.flushCurrentNode(startNode);
		ctx.flushCurrentLane(currentLane);
		ctx.incrStepCount();

        startNode.stepEnter(ctx, null);

        if (FlowNodeType.fromCode(next.getFlowNode().getNodeType()) == FlowNodeType.CONDITION_FRONT) {
            next.stepEnter(ctx, startNode);
            ctx.setNextNode(null);
        }
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
		return null;
	}
}
