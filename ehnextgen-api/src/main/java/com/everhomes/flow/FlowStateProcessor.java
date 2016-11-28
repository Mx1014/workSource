package com.everhomes.flow;

import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.user.User;

public interface FlowStateProcessor {

	void step(FlowCaseState ctx, FlowGraphEvent event);

	FlowCaseState prepareButtonFire(User logonUser, FlowFireButtonCommand cmd);

	FlowCaseState prepareStart(User logonUser, FlowCase flowCase);

}
