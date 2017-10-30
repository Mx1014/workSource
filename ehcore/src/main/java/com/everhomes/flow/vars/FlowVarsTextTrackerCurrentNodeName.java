package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowVariableTextResolver;
import org.springframework.stereotype.Component;

/**
 * 节点跟踪的 当前节点名称
 * @author janson
 *
 */
@Component("flow-variable-text-tracker-curr-node-name")
public class FlowVarsTextTrackerCurrentNodeName implements FlowVariableTextResolver {

	// @Autowired
	// FlowService flowService;
	
	// @Autowired
	// FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
	    return ctx.getFlowGraph().getGraphNode(ctx.getFlowCase().getCurrentNodeId()).getFlowNode().getNodeName();
	}
}
