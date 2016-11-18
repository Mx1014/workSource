package com.everhomes.flow;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.ActionStepType;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.flow.ListFlowCommand;
import com.everhomes.rest.flow.UpdateFlowNameCommand;

@Component
public class FlowServiceImpl implements FlowService {

	@Override
	public Long createNodeEnterAction(FlowNode flowNode, FlowAction flowAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createNodeWatchdogAction(FlowNode flowNode,
			FlowAction flowAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createNodeTrackAction(FlowNode flowNode, FlowStepType stepType,
			FlowAction flowAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createNodeScriptAction(FlowNode flowNode,
			FlowStepType stepType, ActionStepType step, FlowAction flowAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createButtonFireAction(FlowButton flowButton,
			FlowAction flowAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createButtonProcessorForm(FlowButton flowButton) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createFlowNode(Flow flow, FlowStepType stepType,
			FlowNode flowNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createFlow(Long moduleId, FlowModuleType moduleType,
			Long ownerId, Long ownerType, String flowName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean enableFlow(Long flowId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disableFlow(Long flowId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Flow getEnabledFlow(Long moduleId, FlowModuleType moduleType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long createFlowCase(Flow snapshotFlow, FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCase> findFlowCasesByNodeNumber(Flow snapshotFlow,
			Integer nodeNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCase> findFlowCasesByFlowId(Long flowMainId,
			FlowCaseStatus caseStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCase> findFlowCasesByUserId(Long userId,
			FlowCaseStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowButton> findFlowCaseButtons(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fireFlowButton(FlowButton button, FlowCase flowCase,
			Map<String, String> formValues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FlowButton findFlowButtonByName(FlowNode flowNode, String buttonName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowEventLog> findFlowEventLogsByFlowCase(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowEventLog> findFlowEventDetail(FlowCase flowCase,
			FlowNode flowNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowDTO createFlow(CreateFlowCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListFlowBriefResponse listBriefFlows(ListFlowCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFlow(Long flowId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FlowDTO updateFlowName(UpdateFlowNameCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
}
