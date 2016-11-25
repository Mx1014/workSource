package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlowStateProcessorImpl implements FlowStateProcessor {
	@Autowired
	private FlowListenerManager flowListenerManager;
	
	public void step(FlowCaseState ctx, FlowGraphEvent event) {
		boolean stepOK = true;
		FlowGraphNode currentNode = ctx.getCurrentNode();
		currentNode.onAction(ctx, event);
		FlowGraphNode nextNode = ctx.getNextNode();
		if(nextNode != currentNode) {
			try {
				currentNode.stepLeave(ctx, nextNode);
				ctx.setPrefixNode(currentNode);
				ctx.setCurrentNode(nextNode);
				ctx.setNextNode(null);
				nextNode.stepEnter(ctx, currentNode);	
				stepOK = true;
			} catch(FlowStepErrorException ex) {
				stepOK = false;
			}
			
		}
		
		//Now save info to databases here
		if(stepOK) {
			
		} else {
			
		}
	}
}
