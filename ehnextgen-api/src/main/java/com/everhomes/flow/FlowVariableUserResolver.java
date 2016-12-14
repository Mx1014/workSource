package com.everhomes.flow;

public interface FlowVariableUserResolver {
	String variableUserResolve(FlowCaseState ctx, FlowUserSelection userSelection);
}
