package com.everhomes.flow;

public interface FlowTimeoutService {
	void processTimeout(FlowTimeout ft);

	void pushTimeout(FlowTimeout ft);

}
