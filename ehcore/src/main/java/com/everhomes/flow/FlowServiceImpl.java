package com.everhomes.flow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.everhomes.aclink.AclinkConstant;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.aclink.AclinkServiceErrorCode;
import com.everhomes.rest.aclink.DoorAccessDriverType;
import com.everhomes.rest.flow.ActionStepType;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.CreateFlowNodeCommand;
import com.everhomes.rest.flow.CreateFlowUserSelectionCommand;
import com.everhomes.rest.flow.DeleteFlowUserSelectionCommand;
import com.everhomes.rest.flow.DisableFlowButtonCommand;
import com.everhomes.rest.flow.FlowButtonDTO;
import com.everhomes.rest.flow.FlowButtonStatus;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowEvaluateDTO;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowNodeDTO;
import com.everhomes.rest.flow.FlowNodeDetailDTO;
import com.everhomes.rest.flow.FlowNodePriority;
import com.everhomes.rest.flow.FlowNodeStatus;
import com.everhomes.rest.flow.FlowPostEvaluateCommand;
import com.everhomes.rest.flow.FlowPostSubjectCommand;
import com.everhomes.rest.flow.FlowPostSubjectDTO;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserSelectionDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.FlowVariableResponse;
import com.everhomes.rest.flow.ListBriefFlowNodeResponse;
import com.everhomes.rest.flow.ListButtonProcessorSelectionsCommand;
import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.flow.SearchFlowCaseResponse;
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
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class FlowServiceImpl implements FlowService {

	@Autowired
	private FlowProvider flowProvider;
	
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ConfigurationProvider  configProvider;
    
    @Autowired
    private FlowNodeProvider flowNodeProvider;
    
    @Autowired
    private FlowButtonProvider flowButtonProvider;
	
    /**
     * create config flow
     */
	@Override
	public FlowDTO createFlow(CreateFlowCommand cmd) {
		if(cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		if(cmd.getModuleType() == null) {
			cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
		}
		
		Flow flow = flowProvider.findFlowByName(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getModuleType(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getFlowName());
		if(flow != null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NAME_EXISTS, "flow name exists");	
		}
		
    	Flow obj = new Flow();
    	obj.setOwnerId(cmd.getOwnerId());
    	obj.setOwnerType(cmd.getOwnerType());
    	obj.setModuleId(cmd.getModuleId());
    	obj.setModuleType(cmd.getModuleType());
    	obj.setFlowName(cmd.getFlowName());
    	obj.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
    	obj.setStatus(FlowStatusType.CONFIG.getCode());
    	
    	Flow resultObj = this.dbProvider.execute(new TransactionCallback<Flow>() {

			@Override
			public Flow doInTransaction(TransactionStatus arg0) {
				Flow execObj = flowProvider.findFlowByName(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getModuleType(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getFlowName());
				if(execObj != null) {
					//already exists
					return null;
				}
				
				flowProvider.createFlow(obj);
				if(obj.getId() > 0) {
					return obj;
				}
				
				return null;
			}
    		
    	});
    	
    	if(resultObj == null) {
    		throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NAME_EXISTS, "flow name exists");
    	}
		
    	return ConvertHelper.convert(resultObj, FlowDTO.class);
	}

	@Override
	public FlowDTO updateFlowName(UpdateFlowNameCommand cmd) {
		Flow oldFlow = flowProvider.getFlowById(cmd.getFlowId());
		if(oldFlow == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");	
		}
		
		Flow flow = flowProvider.findFlowByName(oldFlow.getNamespaceId(), oldFlow.getModuleId(), oldFlow.getModuleType(), oldFlow.getOwnerId(), oldFlow.getOwnerType(), cmd.getNewFlowName());
		if(flow != null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NAME_EXISTS, "flow name exists");	
		}
		
    	Flow resultObj = this.dbProvider.execute(new TransactionCallback<Flow>() {

			@Override
			public Flow doInTransaction(TransactionStatus arg0) {
				Flow execObj = flowProvider.findFlowByName(oldFlow.getNamespaceId(), oldFlow.getModuleId(), oldFlow.getModuleType(), oldFlow.getOwnerId(), oldFlow.getOwnerType(), cmd.getNewFlowName());
				if(execObj != null) {
					//already exists
					return null;
				}
				
				oldFlow.setFlowName(cmd.getNewFlowName());
				flowProvider.updateFlow(oldFlow);
				return oldFlow;
			}
    		
    	});
    	
    	if(resultObj == null) {
    		throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NAME_EXISTS, "flow name exists");
    	}
    	
    	return ConvertHelper.convert(resultObj, FlowDTO.class);
	}
	
	@Override
	public ListFlowBriefResponse listBriefFlows(ListFlowCommand cmd) {
		int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		cmd.setPageSize(count);
		ListFlowBriefResponse resp = new ListFlowBriefResponse();
		List<FlowDTO> dtos = new ArrayList<FlowDTO>();
		resp.setFlows(dtos);
		
		ListingLocator locator = new ListingLocator();
		List<Flow> flows = flowProvider.findFlowsByModule(locator, cmd);
		for(Flow flow : flows) {
			dtos.add(ConvertHelper.convert(flow, FlowDTO.class));
		}
		
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}
	
	@Override
	public FlowNodeDTO createFlowNode(CreateFlowNodeCommand cmd) {
		Flow flow = flowProvider.getFlowById(cmd.getFlowMainId());
		if(flow == null || flow.getStatus().equals(FlowStatusType.INVALID.getCode()) || !flow.getFlowMainId().equals(0l)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");	
		}
		
		FlowNode flowNode = this.dbProvider.execute(new TransactionCallback<FlowNode>() {
			@Override
			public FlowNode doInTransaction(TransactionStatus arg0) {
				FlowNode nodeObj = flowNodeProvider.findFlowNodeByName(flow.getId(), flow.getFlowVersion(), cmd.getNodeName());	
				if(nodeObj != null) {
					return null;
				}
				
				//step1 mark flow as updated
				flowProvider.flowMarkUpdated(flow);
				
				//step2 create node
				nodeObj = new FlowNode();
				nodeObj.setNodeName(cmd.getNodeName());
				nodeObj.setFlowMainId(flow.getId());
				nodeObj.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
				nodeObj.setNamespaceId(flow.getNamespaceId());
				nodeObj.setStatus(FlowNodeStatus.VISIBLE.getCode());
				nodeObj.setNodeLevel(cmd.getNodeLevel());
				nodeObj.setDescription("");
				flowNodeProvider.createFlowNode(nodeObj);
				
				//step2 create node buttons
				createDefaultButtons(flow, nodeObj);
				
				return nodeObj;
			}
		});
		
		return ConvertHelper.convert(flowNode, FlowNodeDTO.class);
	}
	
	private void createDefaultButtons(Flow flow, FlowNode flowNode) {
		FlowStepType[] steps = {FlowStepType.APPROVE_STEP, FlowStepType.REJECT_STEP, 
				FlowStepType.TRANSFER_STEP, FlowStepType.COMMENT_STEP, FlowStepType.ABSORT_STEP};
		for(FlowStepType step : steps) {
			createDefButton(flow, flowNode, FlowUserType.PROCESSOR, step, FlowButtonStatus.ENABLED);
		}
		
		FlowStepType[] steps2 = {FlowStepType.REMINDER_STEP, FlowStepType.COMMENT_STEP, FlowStepType.ABSORT_STEP};
		for(FlowStepType step : steps2) {
			createDefButton(flow, flowNode, FlowUserType.APPLIER, step, FlowButtonStatus.ENABLED);
		}
		
		createDefButton(flow, flowNode, FlowUserType.APPLIER, FlowStepType.EVALUATE_STEP, FlowButtonStatus.DISABLED);
	}
	
	private void createDefButton(Flow flow, FlowNode flowNode, FlowUserType userType, FlowStepType stepType, FlowButtonStatus enabled) {
		FlowButton button = new FlowButton();
		button.setFlowMainId(flow.getId());
		button.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
		button.setFlowNodeId(flowNode.getId());
		button.setNamespaceId(flow.getNamespaceId());
		button.setStatus(enabled.getCode());
		button.setFlowStepType(stepType.getCode());
		button.setNeedSubject((byte)1);
		button.setFlowUserType(userType.getCode());
		button.setButtonName(buttonDefName(flow.getNamespaceId(), stepType));
		flowButtonProvider.createFlowButton(button);
	}
	
	private String buttonDefName(Integer namespaceId, FlowStepType step) {
		String code = step.getCode();
		String conf = FlowConstants.FLOW_STEP_NAME_PREFIX + code;
		return configProvider.getValue(namespaceId, conf, code);
	}
	
	@Override
	public Flow deleteFlow(Long flowId) {
		Flow flow = flowProvider.getFlowById(flowId);
		if(flow != null) {
			flow.setStatus(FlowStatusType.INVALID.getCode());
			flowProvider.updateFlow(flow);
		}
		
		return flow;
	}

	@Override
	public FlowNode deleteFlowNode(Long flowNodeId) {
		FlowNode flowNode = flowNodeProvider.getFlowNodeById(flowNodeId);
		flowNode.setStatus(FlowNodeStatus.INVALID.getCode());
		flowNodeProvider.updateFlowNode(flowNode);
		
		return flowNode;
	}

	@Override
	public ListBriefFlowNodeResponse listBriefFlowNodes(Long flowId) {
		List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, FlowConstants.FLOW_CONFIG_VER);
		flowNodes.sort((n1, n2) -> {
			return n1.getNodeLevel().compareTo(n2.getNodeLevel());
		});
		
		ListBriefFlowNodeResponse resp = new ListBriefFlowNodeResponse();
		List<FlowNodeDTO> dtos = new ArrayList<FlowNodeDTO>(); 
		resp.setFlowNodes(dtos);
		for(FlowNode fn : flowNodes) {
			dtos.add(ConvertHelper.convert(fn, FlowNodeDTO.class));
		}
		
		return resp;
	}
	
	@Override
	public ListBriefFlowNodeResponse updateNodePriority(
			UpdateFlowNodePriorityCommand cmd) {
		Flow flow = flowProvider.getFlowById(cmd.getFlowMainId());
		if(flow == null || flow.getStatus().equals(FlowStatusType.INVALID.getCode()) || !flow.getFlowMainId().equals(0l)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");	
		}
		
		if(cmd.getFlowNodes() == null) {
			return listBriefFlowNodes(cmd.getFlowMainId());
		}
		
		List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(cmd.getFlowMainId(), FlowConstants.FLOW_CONFIG_VER);
		
		this.dbProvider.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus arg0) {
				for(FlowNodePriority pr : cmd.getFlowNodes()) {
					for(FlowNode fn : flowNodes) {
						if(fn.getId().equals(pr.getId())) {
							if(pr.getNodeLevel() != null) {
								fn.setNodeLevel(pr.getNodeLevel());	
							}
							if(pr.getNodeName() != null) {
								fn.setNodeName(pr.getNodeName());
							}
							if(pr.getDescription() != null) {
								fn.setDescription(pr.getDescription());
							}
							
							flowNodeProvider.updateFlowNode(fn);
							break;
						}
					}
				}
				
				return true;
			}
    	});
		
		flowNodes.sort((n1, n2) -> {
			return n1.getNodeLevel().compareTo(n2.getNodeLevel());
		});
		
		ListBriefFlowNodeResponse resp = new ListBriefFlowNodeResponse();
		List<FlowNodeDTO> dtos = new ArrayList<FlowNodeDTO>(); 
		resp.setFlowNodes(dtos);
		for(FlowNode fn : flowNodes) {
			dtos.add(ConvertHelper.convert(fn, FlowNodeDTO.class));
		}
		
		return resp;
	}
	
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

	@Override
	public SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowPostSubjectDTO postSubject(FlowPostSubjectCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowButtonDTO fireButton(FlowFireButtonCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowEvaluateDTO postEvaluate(FlowPostEvaluateCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListFlowUserSelectionResponse listButtonProcessorSelections(
			ListButtonProcessorSelectionsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
}
