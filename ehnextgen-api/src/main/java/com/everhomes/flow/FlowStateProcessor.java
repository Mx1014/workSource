package com.everhomes.flow;

import java.util.List;

import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;

public interface FlowStateProcessor {

	void step(FlowCaseState ctx, FlowGraphEvent event);

	FlowCaseState prepareButtonFire(UserInfo logonUser, FlowFireButtonCommand cmd);

	FlowCaseState prepareStart(UserInfo logonUser, FlowCase flowCase);

	FlowCaseState prepareStepTimeout(FlowTimeout ft);

	void normalStepLeave(FlowCaseState ctx, FlowGraphNode to)
			throws FlowStepErrorException;

	void normalStepEnter(FlowCaseState ctx, FlowGraphNode from)
			throws FlowStepErrorException;

	void endStepEnter(FlowCaseState ctx, FlowGraphNode from);

	FlowCaseState prepareAutoStep(FlowAutoStepDTO stepDTO);

	FlowCaseState prepareNoStep(FlowAutoStepDTO stepDTO);

}
