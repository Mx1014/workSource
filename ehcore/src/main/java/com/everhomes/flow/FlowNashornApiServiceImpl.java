package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlowNashornApiServiceImpl implements FlowModuleNashornApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowNashornApiServiceImpl.class);

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowStateProcessor flowStateProcessor;

    @Override
    public String name() {
        return "flowService";
    }

    public FlowGraph getFlowGraph(Long flowId, Integer flowVersion) {
        return flowService.getFlowGraph(flowId, flowVersion);
    }

    public FlowCaseState getFlowCaseState(FlowCaseStateBrief brief) {
        FlowCase flowCase = brief.getFlowCase();
        FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
        stepDTO.setEventType(FlowEventType.BUTTON_FIRED.getCode());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setOperatorId(brief.getOperator().getId());
        stepDTO.setFlowMainId(flowCase.getFlowMainId());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
        stepDTO.setFlowVersion(flowCase.getFlowVersion());
        stepDTO.setStepCount(flowCase.getStepCount());

        return flowStateProcessor.prepareNoStep(stepDTO);
    }

    public FlowCaseProcessorsResolver getCurrentProcessorsResolver(Long flowCaseId, boolean allFlowCaseFlag) {
        return flowService.getCurrentProcessors(flowCaseId, allFlowCaseFlag);
    }

    public void testDummyCall() {
        LOGGER.debug("this is test api call");
    }
}