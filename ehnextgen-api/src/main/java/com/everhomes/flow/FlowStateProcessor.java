package com.everhomes.flow;

import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.user.UserInfo;

public interface FlowStateProcessor {

    void step(FlowCaseState ctx, FlowGraphEvent event);

    FlowCaseState prepareButtonFire(UserInfo logonUser, FlowFireButtonCommand cmd);

    FlowCaseState prepareStart(UserInfo logonUser, FlowCase flowCase);

    FlowCaseState prepareBranchFlowCaseStart(UserInfo logonUser, FlowCase flowCase);

    FlowCaseState prepareStepTimeout(FlowTimeout ft);

    void createStepTimeout(FlowCaseState ctx, FlowNode currentRawNode);

    void normalStepLeave(FlowCaseState ctx, FlowGraphNode to) throws FlowStepErrorException;

    void normalStepEnter(FlowCaseState ctx, FlowGraphNode from) throws FlowStepErrorException;

    void endStepEnter(FlowCaseState ctx, FlowGraphNode from);

    FlowCaseState prepareAutoStep(FlowAutoStepDTO stepDTO);

    FlowCaseState prepareNoStep(FlowAutoStepDTO stepDTO);

    void rejectToNode(FlowCaseState ctx, Integer gotoLevel, FlowGraphNode currentNode);

    boolean allProcessorCompleteInCurrentNode(FlowCaseState ctx, FlowGraphNode currentNode, UserInfo firedUser);

    void startStepEnter(FlowCaseState ctx, FlowGraphNode from);

    void subflowStepEnter(FlowCaseState ctx, FlowGraphNode from);

    void subflowStepLeave(FlowCaseState ctx, FlowGraphNode current, FlowGraphNode to);
}
