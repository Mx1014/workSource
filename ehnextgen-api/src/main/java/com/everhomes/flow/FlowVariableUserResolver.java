package com.everhomes.flow;

public interface FlowVariableUserResolver {
	public static final String APPLIER = "flow-variable-applier";
	public static final String PREFIX_NODE_PROCESSOR = "flow-variable-prefix-node-processor";
	public static final String CURRENT_NODE_PROCESSOR = "flow-variable-current-node-processor";
	public static final String NEXT_NODE_PROCESSOR = "flow-variable-next-node-processor";
	public static final String N_NODE_PROCESSOR = "flow-variable-n-node-processor";
	public static final String SUPERVISOR = "flow-variable-supervisor";
	
	String variableUserResolve(FlowCaseState ctx, FlowUserSelection userSelection);
}
