package com.everhomes.flow;

import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.user.UserInfo;

public interface FlowStateProcessor {

    void step(FlowCaseState ctx, FlowGraphEvent event);

    FlowCaseState prepareButtonFire(UserInfo logonUser, FlowFireButtonCommand cmd);

    FlowCaseState prepareStart(UserInfo logonUser, FlowCase flowCase);

    FlowCaseState prepareSubFlowCaseStart(UserInfo logonUser, FlowCase flowCase);

    FlowCaseState prepareStepTimeout(FlowTimeout ft);

    void normalStepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException;

    void normalStepEnter(FlowCaseState ctx, FlowGraphNode from) throws FlowStepErrorException;

    void endStepEnter(FlowCaseState ctx, FlowGraphNode from);

    FlowCaseState prepareAutoStep(FlowAutoStepDTO stepDTO);

    FlowCaseState prepareNoStep(FlowAutoStepDTO stepDTO);

    void rejectToNode(FlowCaseState ctx, Integer gotoLevel, FlowGraphNode currentNode);

    boolean allProcessorCompleteInCurrentNode(FlowCaseState ctx, FlowGraphNode currentNode, UserInfo firedUser);
}
