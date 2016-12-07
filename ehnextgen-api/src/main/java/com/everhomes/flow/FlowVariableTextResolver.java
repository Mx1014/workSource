package com.everhomes.flow;

public interface FlowVariableTextResolver {
//	public static final String TARGET_PROCESSOR_NAME = "flow-variable-target-processor-name";
//	public static final String SUPERVISOR_NAME = "flow-variable-supervisor-name";
//	public static final String LAST_NODE_PROCESSOR_NAME = "flow-varible-last-processor-name";
//	public static final String N_NODE_PROCESSOR_NAME = "flow-varible-n-processor-name";

	public static final String APPLIER_NAME = "flow-variable-applier-name";
	public static final String APPLIER_PHONE = "flow-variable-applier-phone";
	public static final String CURR_PROCESSOR_PHONE = "flow-variable-curr-processor-phone";
	public static final String CURR_PROCESSOR_NAME = "flow-variable-curr-processor-name";
	
	String variableTextRender(FlowCaseState ctx, String variable);
}
