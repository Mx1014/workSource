package com.everhomes.flow;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.ActionStepType;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.CreateFlowNodeCommand;
import com.everhomes.rest.flow.CreateFlowUserSelectionCommand;
import com.everhomes.rest.flow.DeleteFlowUserSelectionCommand;
import com.everhomes.rest.flow.DisableFlowButtonCommand;
import com.everhomes.rest.flow.FlowButtonDTO;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowNodeDTO;
import com.everhomes.rest.flow.FlowNodeDetailDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserSelectionDTO;
import com.everhomes.rest.flow.FlowVariableResponse;
import com.everhomes.rest.flow.ListBriefFlowNodeResponse;
import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.flow.ListFlowCommand;
import com.everhomes.rest.flow.ListFlowUserSelectionCommand;
import com.everhomes.rest.flow.ListFlowUserSelectionResponse;
import com.everhomes.rest.flow.ListFlowVariablesCommand;
import com.everhomes.rest.flow.UpdateFlowButtonCommand;
import com.everhomes.rest.flow.UpdateFlowNameCommand;
import com.everhomes.rest.flow.UpdateFlowNodeCommand;
import com.everhomes.rest.flow.UpdateFlowNodePriorityCommand;
import com.everhomes.rest.flow.UpdateFlowNodeReminder;
import com.everhomes.rest.flow.UpdateFlowNodeTrackerCommand;

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

	@Override
	public FlowNodeDTO createFlowNode(CreateFlowNodeCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowNode deleteFlowNode(Long flowNodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListBriefFlowNodeResponse listBriefFlowNodes(Long flowId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListBriefFlowNodeResponse updateNodePriority(
			UpdateFlowNodePriorityCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowNodeDTO updateFlowNodeName(UpdateFlowNodeCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowUserSelectionDTO createFlowUserSelection(
			CreateFlowUserSelectionCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListFlowUserSelectionResponse listFlowUserSelection(
			ListFlowUserSelectionCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowUserSelectionDTO deleteUserSelection(
			DeleteFlowUserSelectionCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowButtonDTO updateFlowButton(UpdateFlowButtonCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowButtonDTO disableFlowButton(DisableFlowButtonCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowNodeDetailDTO updateFlowNodeReminder(UpdateFlowNodeReminder cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowNodeDetailDTO updateFlowNodeTracker(
			UpdateFlowNodeTrackerCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowVariableResponse listFlowVariables(ListFlowVariablesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
