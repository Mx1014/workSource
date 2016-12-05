package com.everhomes.flow;

import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;

public interface FlowStateProcessor {

	void step(FlowCaseState ctx, FlowGraphEvent event);

	FlowCaseState prepareButtonFire(UserInfo logonUser, FlowFireButtonCommand cmd);

	FlowCaseState prepareStart(UserInfo logonUser, FlowCase flowCase);

	FlowCaseState prepareStepTimeout(FlowTimeout ft);

}
