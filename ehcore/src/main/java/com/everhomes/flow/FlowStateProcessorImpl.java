package com.everhomes.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.user.User;
import com.everhomes.util.RuntimeErrorException;

@Component
public class FlowStateProcessorImpl implements FlowStateProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowStateProcessorImpl.class);
	
	@Autowired
	private FlowListenerManager flowListenerManager;
	
	@Autowired
	private FlowCaseProvider flowCaseProvider;
	
	@Autowired
	private FlowService flowService;
	
	@Autowired
	private FlowNodeProvider flowNodeProvider;
	
   @Autowired
   BigCollectionProvider bigCollectionProvider;
    
	private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	
	public FlowCaseState prepareButtonFire(User logonUser, FlowFireButtonCommand cmd) {
		FlowCaseState ctx = new FlowCaseState();
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
		if(flowCase == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS, "flowcase noexists");
		}
		ctx.setFlowCase(flowCase);
		ctx.setModuleName(flowCase.getModuleName());
		
		FlowGraph flowGraph = flowService.getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		ctx.setFlowGraph(flowGraph);
		
		FlowGraphNode node = flowGraph.getGraphNode(flowCase.getCurrentNodeId());
		if(node == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "flownode noexists");
		}
		ctx.setCurrentNode(node);
		
		FlowGraphButton button = flowGraph.getGraphButton(cmd.getButtonId());
		FlowGraphButtonEvent event = new FlowGraphButtonEvent();
		event.setUserType(FlowUserType.fromCode(button.getFlowButton().getFlowUserType()));
		event.setCmd(cmd);
		ctx.setCurrentEvent(event);
		
		return ctx;
	}
	
	public void step(FlowCaseState ctx, FlowGraphEvent event) {
		boolean stepOK = true;
		FlowGraphNode currentNode = ctx.getCurrentNode();
		if(event != null) {
			event.fire(ctx);
		} else {
			currentNode.onAction(ctx, FlowStepType.NO_STEP);	
		}
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
