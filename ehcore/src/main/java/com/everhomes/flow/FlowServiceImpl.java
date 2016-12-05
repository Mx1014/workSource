package com.everhomes.flow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.hslf.record.CurrentUserAtom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.everhomes.aclink.AclinkConstant;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.ListingLocator;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.pusher.PusherServiceImpl;
import com.everhomes.rest.aclink.AclinkServiceErrorCode;
import com.everhomes.rest.aclink.DoorAccessDriverType;
import com.everhomes.rest.flow.ActionStepType;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.CreateFlowNodeCommand;
import com.everhomes.rest.flow.CreateFlowUserSelectionCommand;
import com.everhomes.rest.flow.DeleteFlowUserSelectionCommand;
import com.everhomes.rest.flow.DisableFlowButtonCommand;
import com.everhomes.rest.flow.FLowUserSourceType;
import com.everhomes.rest.flow.FlowActionDTO;
import com.everhomes.rest.flow.FlowActionInfo;
import com.everhomes.rest.flow.FlowActionStatus;
import com.everhomes.rest.flow.FlowActionStepType;
import com.everhomes.rest.flow.FlowActionType;
import com.everhomes.rest.flow.FlowButtonDTO;
import com.everhomes.rest.flow.FlowButtonDetailDTO;
import com.everhomes.rest.flow.FlowButtonStatus;
import com.everhomes.rest.flow.FlowCaseDTO;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowCaseType;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowEvaluateDTO;
import com.everhomes.rest.flow.FlowEventLogDTO;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowNodeDTO;
import com.everhomes.rest.flow.FlowNodeDetailDTO;
import com.everhomes.rest.flow.FlowNodeLogDTO;
import com.everhomes.rest.flow.FlowNodePriority;
import com.everhomes.rest.flow.FlowNodeReminderDTO;
import com.everhomes.rest.flow.FlowNodeStatus;
import com.everhomes.rest.flow.FlowNodeTrackerDTO;
import com.everhomes.rest.flow.FlowPostEvaluateCommand;
import com.everhomes.rest.flow.FlowPostSubjectCommand;
import com.everhomes.rest.flow.FlowPostSubjectDTO;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowSingleUserSelectionCommand;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowSubjectDTO;
import com.everhomes.rest.flow.FlowUserSelectionDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.FlowVariableResponse;
import com.everhomes.rest.flow.GetFlowButtonDetailByIdCommand;
import com.everhomes.rest.flow.ListBriefFlowNodeResponse;
import com.everhomes.rest.flow.ListButtonProcessorSelectionsCommand;
import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.flow.ListFlowButtonResponse;
import com.everhomes.rest.flow.ListFlowModulesCommand;
import com.everhomes.rest.flow.ListFlowModulesResponse;
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
import com.everhomes.rest.flow.UpdateFlowNodeReminderCommand;
import com.everhomes.rest.flow.UpdateFlowNodeTrackerCommand;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.tables.pojos.EhFlowAttachments;
import com.everhomes.server.schema.tables.pojos.EhNewsAttachments;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class FlowServiceImpl implements FlowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlowServiceImpl.class);
	
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
    
    @Autowired
    private FlowUserSelectionProvider flowUserSelectionProvider;
    
    @Autowired
    private FlowActionProvider flowActionProvider;
    
    @Autowired
    private FlowStateProcessor flowStateProcessor;
    
    @Autowired
    private FlowEventLogProvider flowEventLogProvider;
    
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    
    @Autowired
    private FlowSubjectProvider flowSubjectProvider;
    
    @Autowired
    private AttachmentProvider attachmentProvider;
    
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FlowListenerManager flowListenerManager;
    
    private static final Pattern pParam = Pattern.compile("\\$\\{([^\\}]*)\\}");
    
    private StringTemplateLoader templateLoader;
    private Configuration templateConfig;
    
    private Map<String, FlowGraph> graphMap;
    
    public FlowServiceImpl() {
    	graphMap = new ConcurrentHashMap<String, FlowGraph>();
    	templateLoader = new StringTemplateLoader();
    	templateConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    	templateConfig.setTemplateLoader(templateLoader);
    	templateConfig.setTemplateUpdateDelay(0);
    }
	
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
    	obj.setFlowVersion(FlowConstants.FLOW_CONFIG_START);
    	obj.setStatus(FlowStatusType.CONFIG.getCode());
    	obj.setOrganizationId(cmd.getOrgId());
    	
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
	public Flow getFlowByEntity(Long entityId, FlowEntityType entity) {
		return getFlowByEntity(entityId, entity, 1);
	}
	
	private Flow getFlowByEntity(Long entityId, FlowEntityType entity, int loop) {
		if(loop > 20) {
			LOGGER.error("getFlowEntity loop overlop");
			return null;
		}
		switch(entity) {
		case FLOW:
			return flowProvider.getFlowById(entityId);
		case FLOW_NODE:
			FlowNode flowNode = flowNodeProvider.getFlowNodeById(entityId);
			if(flowNode == null) {
				return null;
			}
			if(null == flowNode.getFlowMainId() || flowNode.getFlowMainId().equals(0l)) {
				return null;
			}
			return getFlowByEntity(flowNode.getFlowMainId(), FlowEntityType.FLOW, ++loop);
		case FLOW_BUTTON:
			FlowButton flowButton = flowButtonProvider.getFlowButtonById(entityId);
			if(flowButton == null) {
				return null;
			}
			if(null == flowButton.getFlowMainId() || flowButton.getFlowMainId().equals(0l)) {
				Flow flow = getFlowByEntity(flowButton.getFlowNodeId(), FlowEntityType.FLOW_NODE, ++loop);
				if(flow != null) {
					flowButton.setFlowMainId(flow.getTopId());
					flowButtonProvider.updateFlowButton(flowButton);	
				}
				
				return flow;
			} else {
				return getFlowByEntity(flowButton.getFlowMainId(), FlowEntityType.FLOW, ++loop);
			}
			
		case FLOW_ACTION:
			FlowAction flowAction = flowActionProvider.getFlowActionById(entityId);
			if(flowAction == null) {
				return null;
			}
			
			if(flowAction.getFlowMainId() == null || flowAction.getFlowMainId().equals(0l)) {
				Flow flow = getFlowByEntity(flowAction.getBelongTo(), FlowEntityType.fromCode(flowAction.getBelongEntity()), ++loop);
				if(flow != null) {
					flowAction.setFlowMainId(flow.getTopId());
					flowActionProvider.updateFlowAction(flowAction);
				}
				return flow;
			} else {
				return getFlowByEntity(flowAction.getFlowMainId(), FlowEntityType.FLOW, ++loop);
			}
			
		case FLOW_SELECTION:
			FlowUserSelection flowSel = flowUserSelectionProvider.getFlowUserSelectionById(entityId);
			if(flowSel == null) {
				return null;
			}
			if(flowSel.getFlowMainId() == null || flowSel.getFlowMainId().equals(0l)) {
				Flow flow = getFlowByEntity(flowSel.getBelongTo(), FlowEntityType.fromCode(flowSel.getBelongEntity()), ++loop);
				if(flow != null) {
					flowSel.setFlowMainId(flow.getTopId());
					flowUserSelectionProvider.updateFlowUserSelection(flowSel);
				}
			}
			
		default:
			return null;
		}
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
	
	private void flowMarkUpdated(Flow flow) {
		if(flow == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");	
		}
		
		flowProvider.flowMarkUpdated(flow);;
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
		Flow flow = getFlowByEntity(flowNodeId, FlowEntityType.FLOW_NODE);
		flowMarkUpdated(flow);
		
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
		
		flowMarkUpdated(flow);
		
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
	public ListFlowButtonResponse listFlowNodeButtons(Long flowNodeId) {
		ListFlowButtonResponse resp = new ListFlowButtonResponse();
		
		List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowNodeId, FlowConstants.FLOW_CONFIG_VER, FlowUserType.APPLIER.getCode());
		List<FlowButtonDTO> dtos1 = new ArrayList<FlowButtonDTO>();
		buttons.stream().forEach((fb) ->{
			dtos1.add(ConvertHelper.convert(fb, FlowButtonDTO.class));
		});
		
		List<FlowButtonDTO> dtos2 = new ArrayList<FlowButtonDTO>();
		buttons = flowButtonProvider.findFlowButtonsByUserType(flowNodeId, FlowConstants.FLOW_CONFIG_VER, FlowUserType.PROCESSOR.getCode());
		buttons.stream().forEach((fb) ->{
			dtos2.add(ConvertHelper.convert(fb, FlowButtonDTO.class));
		});
		
		return resp;
	}
	
	@Override
	public FlowNodeDetailDTO getFlowNodeDetail(Long flowNodeId) {
		FlowNode flowNode = flowNodeProvider.getFlowNodeById(flowNodeId);
		FlowNodeDetailDTO detail = ConvertHelper.convert(flowNode, FlowNodeDetailDTO.class);
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
		detail.setProcessors(new ArrayList<FlowUserSelectionDTO>());
		if(selections != null) {
			selections.stream().forEach((s) -> {
				detail.getProcessors().add(ConvertHelper.convert(s, FlowUserSelectionDTO.class));
			});
		}
		
		detail.setReminder(getReminderDTO(flowNodeId));
		detail.setTracker(getTrackerDTO(flowNodeId));
		return detail;
	}
	
	private FlowNodeReminderDTO getReminderDTO(Long flowNodeId) {
		FlowNodeReminderDTO dto = new FlowNodeReminderDTO();
		FlowAction action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
		if(action != null) {
			dto.setReminderMessageEnabled((byte)1);
			dto.setMessageAction(actionToDTO(action));
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.SMS.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
		if(action != null) {
			dto.setReminderSMSEnabled((byte)1);
			dto.setSmsAction(actionToDTO(action));
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TICK_MESSAGE.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
		if(action != null) {
			dto.setReminderTickMsgEnabled((byte)1);
			dto.setTickMessageAction(actionToDTO(action));
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TICK_SMS.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
		if(action != null) {
			dto.setReminderTickSMSEnabled((byte)1);
			dto.setTickSMSAction(actionToDTO(action));
		}
		
		return dto;
	}
	
	private FlowNodeTrackerDTO getTrackerDTO(Long flowNodeId) {
		FlowNodeTrackerDTO dto = new FlowNodeTrackerDTO();
		FlowAction action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.APPROVE_STEP.getCode());
		if(action != null) {
			dto.setEnterTracker(ConvertHelper.convert(action, FlowActionDTO.class));	
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.REJECT_STEP.getCode());
		if(action != null) {
			dto.setRejectTracker(ConvertHelper.convert(action, FlowActionDTO.class));	
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_LEAVE.getCode(), FlowStepType.TRANSFER_STEP.getCode());
		if(action != null) {
			dto.setTransferTracker(ConvertHelper.convert(action, FlowActionDTO.class));
		}
		
		return dto;
	}
	
	private FlowActionDTO actionToDTO(FlowAction action) {
		FlowActionDTO actionDTO = ConvertHelper.convert(action, FlowActionDTO.class);
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(action.getId()
				, FlowEntityType.FLOW_ACTION.getCode(), FlowUserType.PROCESSOR.getCode());
		if(selections != null) {
			List<FlowUserSelectionDTO> userSeles = new ArrayList<FlowUserSelectionDTO>();
			selections.stream().forEach((s) -> {
				userSeles.add(ConvertHelper.convert(s, FlowUserSelectionDTO.class));
			});
			actionDTO.setProcessors(userSeles);
		}	
		
		return actionDTO;
	}

	@Override
	public FlowNodeDetailDTO updateFlowNodeReminder(UpdateFlowNodeReminderCommand cmd) {
		FlowNode flowNode = flowNodeProvider.getFlowNodeById(cmd.getFlowNodeId());
		Flow flow = getFlowByEntity(cmd.getFlowNodeId(), FlowEntityType.FLOW_NODE);
		flowMarkUpdated(flow);
		
		if(cmd.getMessageAction() != null) {
			dbProvider.execute(new TransactionCallback<FlowAction>(){
				@Override
				public FlowAction doInTransaction(TransactionStatus arg0) {
					return createNodeAction(flowNode, cmd.getMessageAction(), FlowActionType.MESSAGE.getCode()
							, FlowActionStepType.STEP_ENTER.getCode(), null);
				}
			});
		}
		
		if(cmd.getSmsAction() != null) {
			dbProvider.execute((a) -> {
				return createNodeAction(flowNode, cmd.getSmsAction(), FlowActionType.SMS.getCode()
						, FlowActionStepType.STEP_ENTER.getCode(), null);
			});
		}
		
		if(cmd.getTickMessageAction() != null) {
			dbProvider.execute((a) -> {
				return createNodeAction(flowNode, cmd.getTickMessageAction(), FlowActionType.TICK_MESSAGE.getCode()
						, FlowActionStepType.STEP_TIMEOUT.getCode(), null);
			});
		}
		
		if(cmd.getTickSMSAction() != null) {
			dbProvider.execute((a) -> {
				return createNodeAction(flowNode, cmd.getTickSMSAction(), FlowActionType.TICK_SMS.getCode()
						, FlowActionStepType.STEP_TIMEOUT.getCode(), null);
			});
		}
		
		return getFlowNodeDetail(cmd.getFlowNodeId());
	}
	
	private FlowAction createNodeAction(FlowNode flowNode, FlowActionInfo actionInfo
			, String actionType, String actionStepType, String flowStepType) {
		FlowAction action = flowActionProvider.findFlowActionByBelong(flowNode.getId(), FlowEntityType.FLOW_NODE.getCode()
				, actionType, actionStepType, flowStepType);
		
		CreateFlowUserSelectionCommand selectionCmd = actionInfo.getUserSelections();
		boolean configUser = false;
		if(selectionCmd != null && selectionCmd.getSelections() != null && selectionCmd.getSelections().size() > 0) {
			configUser = true;
		}
		
		if(action == null) {
			action = new FlowAction();
			action.setFlowMainId(flowNode.getFlowMainId());
			action.setFlowVersion(flowNode.getFlowVersion());
			action.setActionStepType(actionStepType);
			action.setActionType(actionType);
			action.setBelongTo(flowNode.getId());
			action.setBelongEntity(FlowEntityType.FLOW_NODE.getCode());
			action.setNamespaceId(flowNode.getNamespaceId());
			action.setFlowStepType(flowStepType);

			action.setReminderTickMinute(actionInfo.getReminderTickMinute());
			action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
			action.setTrackerApplier(actionInfo.getTrackerApplier());
			action.setTrackerProcessor(actionInfo.getTrackerProcessor());
			action.setStatus(FlowActionStatus.ENABLED.getCode());
			action.setRenderText(actionInfo.getRenderText());
			flowActionProvider.createFlowAction(action);
			
		} else {
			action.setReminderTickMinute(actionInfo.getReminderTickMinute());
			action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
			action.setTrackerApplier(actionInfo.getTrackerApplier());
			action.setTrackerProcessor(actionInfo.getTrackerProcessor());
			action.setStatus(FlowActionStatus.ENABLED.getCode());
			action.setRenderText(actionInfo.getRenderText());
			flowActionProvider.updateFlowAction(action);
			
			//delete all old selections
			if(configUser) {
				flowUserSelectionProvider.deleteSelectionByBelong(action.getId(), FlowEntityType.FLOW_ACTION.getCode(), FlowUserType.PROCESSOR.getCode());	
			}
		}
		
		if(configUser) {
			List<FlowSingleUserSelectionCommand> seles = selectionCmd.getSelections();
			for(FlowSingleUserSelectionCommand selCmd : seles) {
				FlowUserSelection userSel = new FlowUserSelection(); 
				userSel.setBelongTo(action.getId());
				userSel.setBelongEntity(FlowEntityType.FLOW_ACTION.getCode());
				userSel.setBelongType(FlowUserType.PROCESSOR.getCode());
				userSel.setFlowMainId(action.getFlowMainId());
				userSel.setFlowVersion(action.getFlowVersion());
				userSel.setNamespaceId(action.getNamespaceId());
				createUserSelection(userSel, selCmd);
			}
		}
		
		return action;
	}
	
	private void createUserSelection(FlowUserSelection userSel, FlowSingleUserSelectionCommand selCmd) {
		userSel.setSelectType(selCmd.getFlowUserSelectionType());
		userSel.setSourceIdA(selCmd.getSourceIdA());
		userSel.setSourceIdB(selCmd.getSourceIdB());
		userSel.setSourceTypeA(selCmd.getSourceTypeA());
		userSel.setSourceTypeB(selCmd.getSourceTypeB());
		userSel.setStatus(FlowStatusType.VALID.getCode());
		flowUserSelectionProvider.createFlowUserSelection(userSel);		
	}

	@Override
	public FlowNodeDetailDTO updateFlowNodeTracker(
			UpdateFlowNodeTrackerCommand cmd) {
		FlowNode flowNode = flowNodeProvider.getFlowNodeById(cmd.getFlowNodeId());
		Flow flow = getFlowByEntity(cmd.getFlowNodeId(), FlowEntityType.FLOW_NODE);
		flowMarkUpdated(flow);
		
		if(cmd.getEnterTracker() != null) {
			dbProvider.execute((a) -> {
				return createNodeAction(flowNode, cmd.getEnterTracker(), FlowActionType.TRACK.getCode()
						, FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.APPROVE_STEP.getCode());
			});
		}
		
		if(cmd.getRejectTracker() != null) {
			dbProvider.execute((a) -> {
				return createNodeAction(flowNode, cmd.getEnterTracker(), FlowActionType.TRACK.getCode()
						, FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.REJECT_STEP.getCode());
			});
		}
		
		if(cmd.getTransferTracker() != null) {
			dbProvider.execute((a) -> {
				return createNodeAction(flowNode, cmd.getEnterTracker(), FlowActionType.TRACK.getCode()
						, FlowActionStepType.STEP_LEAVE.getCode(), FlowStepType.TRANSFER_STEP.getCode());
			});			
		}
		
		return getFlowNodeDetail(cmd.getFlowNodeId());
	}

	@Override
	public FlowNodeDTO updateFlowNodeName(UpdateFlowNodeCommand cmd) {
		Flow flow = getFlowByEntity(cmd.getFlowNodeId(), FlowEntityType.FLOW_NODE);
		flowMarkUpdated(flow);
		
		FlowNode flowNode = new FlowNode();
		flowNode.setId(cmd.getFlowNodeId());
		flowNode.setNodeName(cmd.getFlowNodeName());
		flowNode.setAutoStepMinute(cmd.getAutoStepMinute());
		flowNode.setAutoStepType(cmd.getAutoStepType());
		flowNode.setAllowApplierUpdate(cmd.getAllowApplierUpdate());
		flowNodeProvider.updateFlowNode(flowNode);
		
		return ConvertHelper.convert(flowNode, FlowNodeDTO.class);
	}

	@Override
	public ListFlowUserSelectionResponse createFlowUserSelection(
			CreateFlowUserSelectionCommand cmd) {
		Flow flow = getFlowByEntity(cmd.getBelongTo(), FlowEntityType.fromCode(cmd.getFlowEntityType()));
		flowMarkUpdated(flow);
		
		ListFlowUserSelectionResponse resp = new ListFlowUserSelectionResponse();
		List<FlowUserSelectionDTO> selections = new ArrayList<FlowUserSelectionDTO>();
		resp.setSelections(selections);
		
		List<FlowSingleUserSelectionCommand> cmds = cmd.getSelections();
		if(cmds != null && cmds.size() > 0) {
			for(FlowSingleUserSelectionCommand sCmd : cmds) {
				FlowUserSelection sel = ConvertHelper.convert(sCmd, FlowUserSelection.class);
				sel.setBelongEntity(cmd.getFlowEntityType());
				sel.setBelongTo(cmd.getBelongTo());
				sel.setBelongType(cmd.getFlowUserType());	
				sel.setFlowMainId(flow.getTopId());
				sel.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
				sel.setSelectType(sCmd.getFlowUserSelectionType());
				sel.setStatus(FlowStatusType.VALID.getCode());
				flowUserSelectionProvider.createFlowUserSelection(sel);
			}
		}
		
		return resp;
	}

	@Override
	public ListFlowUserSelectionResponse listFlowUserSelection(
			ListFlowUserSelectionCommand cmd) {
		ListFlowUserSelectionResponse resp = new ListFlowUserSelectionResponse();
		List<FlowUserSelectionDTO> selections = new ArrayList<FlowUserSelectionDTO>();
		resp.setSelections(selections);
		List<FlowUserSelection> seles = flowUserSelectionProvider.findSelectionByBelong(cmd.getBelongTo(), cmd.getFlowEntityType(), cmd.getFlowUserType());
		if(seles != null && seles.size() > 0) {
			seles.stream().forEach((sel) -> {
				selections.add(ConvertHelper.convert(sel, FlowUserSelectionDTO.class));
			});
		}
		
		return resp;
	}

	@Override
	public FlowUserSelectionDTO deleteUserSelection(
			DeleteFlowUserSelectionCommand cmd) {
		Flow flow = getFlowByEntity(cmd.getUserSelectionId(), FlowEntityType.FLOW_SELECTION);
		flowMarkUpdated(flow);
		
		FlowUserSelection sel = flowUserSelectionProvider.getFlowUserSelectionById(cmd.getUserSelectionId());
		if(sel != null) {
			flowUserSelectionProvider.deleteFlowUserSelection(sel);
			return ConvertHelper.convert(sel, FlowUserSelectionDTO.class);
		}
		
		return null;
	}

	@Override
	public FlowButtonDetailDTO updateFlowButton(UpdateFlowButtonCommand cmd) {
		Flow flow = getFlowByEntity(cmd.getFlowButtonId(), FlowEntityType.FLOW_BUTTON);
		flowMarkUpdated(flow);
		
		FlowButton flowButton = flowButtonProvider.getFlowButtonById(cmd.getFlowButtonId());
		flowButton.setButtonName(cmd.getButtonName());
		flowButton.setDescription(cmd.getDescription());
		flowButton.setGotoNodeId(cmd.getGotoNodeId());
		flowButton.setNeedSubject(cmd.getNeedSubject());
		flowButton.setNeedProcessor(cmd.getNeedProcessor());
		flowButtonProvider.updateFlowButton(flowButton);
		
		if(null != cmd.getMessageAction()) {
			dbProvider.execute((a) -> {
				return createButtonAction(flowButton, cmd.getMessageAction(), FlowActionType.MESSAGE.getCode()
						, FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
			});		
		}
		
		if(null != cmd.getSmsAction()) {
			dbProvider.execute((a) -> {
				return createButtonAction(flowButton, cmd.getMessageAction(), FlowActionType.SMS.getCode()
						, FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
			});	
		}
		
		if(null != cmd.getEnterScriptIds()) {
			for(Long scriptId : cmd.getEnterScriptIds()) {
				FlowAction action = new FlowAction();
				action.setFlowMainId(flowButton.getFlowMainId());
				action.setFlowVersion(flowButton.getFlowVersion());
				action.setActionStepType(FlowActionStepType.STEP_ENTER.getCode());
				action.setActionType(FlowActionType.ENTER_SCRIPT.getCode());
				action.setBelongTo(flowButton.getId());
				action.setBelongEntity(FlowEntityType.FLOW_BUTTON.getCode());
				action.setFlowStepType(FlowStepType.NO_STEP.getCode());
				action.setNamespaceId(flowButton.getNamespaceId());
				action.setStatus(FlowActionStatus.ENABLED.getCode());
				action.setScriptId(scriptId);
				flowActionProvider.createFlowAction(action);
			}
		}
		
		return getFlowButtonDetail(cmd.getFlowButtonId());
	}

	@Override
	public FlowButtonDTO disableFlowButton(DisableFlowButtonCommand cmd) {
		Flow flow = getFlowByEntity(cmd.getFlowButtonId(), FlowEntityType.FLOW_BUTTON);
		flowMarkUpdated(flow);
		
		FlowButton flowButton = flowButtonProvider.getFlowButtonById(cmd.getFlowButtonId());
		if(flowButton != null) {
			flowButton.setStatus(FlowButtonStatus.DISABLED.getCode());
			flowButtonProvider.updateFlowButton(flowButton);
			return ConvertHelper.convert(flowButton, FlowButtonDTO.class);
		}
		
		return null;
	}
	
	private FlowAction createButtonAction(FlowButton flowButton, FlowActionInfo actionInfo
			, String actionType, String actionStepType, String flowStepType) {
		FlowAction action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
				, actionType, actionStepType, flowStepType);
		
		CreateFlowUserSelectionCommand selectionCmd = actionInfo.getUserSelections();
		boolean configUser = false;
		if(selectionCmd != null && selectionCmd.getSelections() != null && selectionCmd.getSelections().size() > 0) {
			configUser = true;
		}
		
		if(action == null) {
			action = new FlowAction();
			action.setFlowMainId(flowButton.getFlowMainId());
			action.setFlowVersion(flowButton.getFlowVersion());
			action.setActionStepType(actionStepType);
			action.setActionType(actionType);
			action.setBelongTo(flowButton.getId());
			action.setBelongEntity(FlowEntityType.FLOW_BUTTON.getCode());
			action.setNamespaceId(flowButton.getNamespaceId());
			action.setFlowStepType(flowStepType);

			action.setReminderTickMinute(actionInfo.getReminderTickMinute());
			action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
			action.setTrackerApplier(actionInfo.getTrackerApplier());
			action.setTrackerProcessor(actionInfo.getTrackerProcessor());
			action.setStatus(FlowActionStatus.ENABLED.getCode());
			action.setRenderText(actionInfo.getRenderText());
			flowActionProvider.createFlowAction(action);
			
		} else {
			action.setReminderTickMinute(actionInfo.getReminderTickMinute());
			action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
			action.setTrackerApplier(actionInfo.getTrackerApplier());
			action.setTrackerProcessor(actionInfo.getTrackerProcessor());
			action.setStatus(FlowActionStatus.ENABLED.getCode());
			action.setRenderText(actionInfo.getRenderText());
			flowActionProvider.updateFlowAction(action);
			
			//delete all old selections
			if(configUser) {
				flowUserSelectionProvider.deleteSelectionByBelong(action.getId(), FlowEntityType.FLOW_ACTION.getCode(), FlowUserType.PROCESSOR.getCode());	
			}
		}
		
		if(configUser) {
			List<FlowSingleUserSelectionCommand> seles = selectionCmd.getSelections();
			for(FlowSingleUserSelectionCommand selCmd : seles) {
				FlowUserSelection userSel = new FlowUserSelection(); 
				userSel.setBelongTo(action.getId());
				userSel.setBelongEntity(FlowEntityType.FLOW_ACTION.getCode());
				userSel.setBelongType(FlowUserType.PROCESSOR.getCode());
				userSel.setFlowMainId(action.getFlowMainId());
				userSel.setFlowVersion(action.getFlowVersion());
				userSel.setNamespaceId(action.getNamespaceId());
				createUserSelection(userSel, selCmd);
			}
		}
		
		return action;
	}
	
	@Override
	public FlowButtonDetailDTO getFlowButtonDetail(Long flowButtonId) {
		FlowButton flowButton = flowButtonProvider.getFlowButtonById(flowButtonId);
		FlowButtonDetailDTO dto = ConvertHelper.convert(flowButton, FlowButtonDetailDTO.class);
		FlowAction action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
				, FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
		if(action != null) {
			dto.setPushMessage(actionToDTO(action));	
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
				, FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
		if(action != null) {
			dto.setPushSms(actionToDTO(action));
		}
		
		List<FlowAction> flowActions = flowActionProvider.findFlowActionsByBelong(flowButtonId, FlowEntityType.FLOW_BUTTON.getCode()
				, FlowActionType.ENTER_SCRIPT.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
		List<FlowActionDTO> actionDTOS = new ArrayList<FlowActionDTO>();
		if(flowActions != null) {
			flowActions.stream().forEach((fa) -> {
				actionDTOS.add(ConvertHelper.convert(fa, FlowActionDTO.class));	
			});
			
			dto.setEnterScripts(actionDTOS);
		}
		
		return dto;
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
		final FlowGraph flowGraph = new FlowGraph();
		Flow flow = flowProvider.getFlowById(flowId);
		if(flow.getStatus().equals(FlowStatusType.STOP.getCode())) {
			//restart it
			flow.setStatus(FlowStatusType.RUNNING.getCode());
			Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
			flow.setUpdateTime(now);
			flowProvider.updateFlow(flow);
			return true;
		} else if(flow.getStatus().equals(FlowStatusType.RUNNING.getCode())) {
			//already running
			return true;
		}
		
		List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, FlowConstants.FLOW_CONFIG_VER);
		flowNodes.sort((n1, n2) -> {
			return n1.getNodeLevel().compareTo(n2.getNodeLevel());
		});
		
		int i = 1;
		for(FlowNode fn : flowNodes) {
			if(!fn.getNodeLevel().equals(i)) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_LEVEL_ERR, "node_level error");	
			}
			i++;
		}
		
		flowGraph.setFlow(flow);
		
		FlowNode nodeObj = null;
		nodeObj = new FlowNode();
		nodeObj.setNodeName("START");
		nodeObj.setFlowMainId(flow.getId());
		nodeObj.setFlowVersion(flow.getFlowVersion());//now not use config version, but real flow version 
		nodeObj.setNamespaceId(flow.getNamespaceId());
		nodeObj.setStatus(FlowNodeStatus.HIDDEN.getCode());
		nodeObj.setNodeLevel(0);
		nodeObj.setDescription("");
		FlowGraphNode start = new FlowGraphNodeStart();
		start.setFlowNode(nodeObj);
		flowGraph.getNodes().add(start);
		
		flowNodes.forEach((fn)->{
			flowGraph.getNodes().add(getFlowGraphNode(fn, FlowConstants.FLOW_CONFIG_VER));
		});
		
		nodeObj = new FlowNode();
		nodeObj.setNodeName("END");
		nodeObj.setFlowMainId(flow.getId());
		nodeObj.setFlowVersion(flow.getFlowVersion());
		nodeObj.setNamespaceId(flow.getNamespaceId());
		nodeObj.setStatus(FlowNodeStatus.HIDDEN.getCode());
		nodeObj.setNodeLevel(flowGraph.getNodes().size());
		nodeObj.setDescription("");
		FlowGraphNode end = new FlowGraphNodeEnd();
		end.setFlowNode(nodeObj);		
		flowGraph.getNodes().add(end);
//		flowGraph.saveIds();
		
		//now all the graph is collected, do snapshot
		Flow flowNew = flowProvider.getFlowById(flowId);
		if(!flowNew.getUpdateTime().equals(flow.getUpdateTime())) {
			//the flow is updated, retry
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CONFIG_BUSY, "flow has changed, retry");
		}
		
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		flowNew.setUpdateTime(now);
		flowNew.setStatus(FlowStatusType.SNAPSHOT.getCode());
		flowProvider.updateFlow(flowNew);
		
		boolean isOk = true;
		try {
			dbProvider.execute((s)->{
				doSnapshot(flowGraph);
				return true;
			});
			
			//running now
			now = new Timestamp(DateHelper.currentGMTTime().getTime());
			flowNew.setUpdateTime(now);
			flowNew.setStatus(FlowStatusType.RUNNING.getCode());
			flowProvider.updateFlow(flowNew);
			
		} catch(Exception ex) {
			isOk = false;
			LOGGER.error("do snapshot error", ex);
		}
		
		if(isOk) {
			//TODO need this?
			if(flowGraph.getFlow().getId().equals(flowNew.getId())) {
				isOk = false;
			} else {
				Flow snapFlow = flowProvider.getFlowById(flowGraph.getFlow().getId());
				if(snapFlow == null) {
					isOk = false;
				}	
			}
		}
		
		if(!isOk) {
			//change back to config
			now = new Timestamp(DateHelper.currentGMTTime().getTime());
			flowNew.setUpdateTime(now);
			flowNew.setStatus(FlowStatusType.CONFIG.getCode());
			flowProvider.updateFlow(flowNew);			
		} else {
			String fmt = String.format("%d:%d", flowGraph.getFlow().getFlowMainId(), flowGraph.getFlow().getFlowVersion());
			graphMap.put(fmt, flowGraph);
		}
		
		return isOk;
	}
	
	private FlowGraphNode getFlowGraphNode(FlowNode flowNode, Integer flowVersion) {
		FlowGraphNodeNormal graphNode = new FlowGraphNodeNormal();
		graphNode.setFlowNode(flowNode);
		Long flowNodeId = flowNode.getId();
		
		FlowAction action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
		FlowGraphAction graphAction = null;
		if(action != null) {
			graphAction = new FlowGraphMessageAction();
			graphAction.setFlowAction(action);
			graphNode.setMessageAction(graphAction);	
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.SMS.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
		if(action != null) {
			graphAction = new FlowGraphSMSAction();
			graphAction.setFlowAction(action);
			graphNode.setSmsAction(graphAction);	
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TICK_MESSAGE.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
		if(action != null) {
			graphAction = new FlowGraphMessageAction();
			graphAction.setFlowAction(action);
			graphNode.setTickMessageAction(graphAction);	
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TICK_SMS.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
		if(action != null) {
			graphAction = new FlowGraphSMSAction();
			graphAction.setFlowAction(action);
			graphNode.setTickMessageAction(graphAction);	
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.APPROVE_STEP.getCode());
		if(action != null) {
			graphAction = new FlowGraphTrackerAction(FlowStepType.APPROVE_STEP);
			graphAction.setFlowAction(action);
			graphNode.setTrackApproveEnter(graphAction);
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.REJECT_STEP.getCode());
		if(action != null) {
			graphAction = new FlowGraphTrackerAction(FlowStepType.REJECT_STEP);
			graphAction.setFlowAction(action);
			graphNode.setTrackRejectEnter(graphAction);
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_LEAVE.getCode(), FlowStepType.TRANSFER_STEP.getCode());
		if(action != null) {
			graphAction = new FlowGraphTrackerAction(FlowStepType.TRANSFER_STEP);
			graphAction.setFlowAction(action);
			graphNode.setTrackTransferLeave(graphAction);
		}
		
		List<FlowButton> applierButtons = flowButtonProvider.findFlowButtonsByUserType(flowNodeId, flowVersion, FlowUserType.APPLIER.getCode());
		applierButtons.forEach((btn)->{
			graphNode.getApplierButtons().add(getFlowGraphButton(btn));
		});
		
		List<FlowButton> processorButtons = flowButtonProvider.findFlowButtonsByUserType(flowNodeId, flowVersion, FlowUserType.PROCESSOR.getCode());
		processorButtons.forEach((btn)->{
			graphNode.getProcessorButtons().add(getFlowGraphButton(btn));
		});
		
		return graphNode;
	}
	
	private FlowGraphButton getFlowGraphButton(FlowButton flowButton) {
		FlowGraphButton graphBtn = new FlowGraphButton();
		graphBtn.setFlowButton(flowButton);
		FlowGraphAction graphAction = null;
		FlowAction action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
				, FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
		if(action != null) {
			graphAction = new FlowGraphMessageAction();
			graphAction.setFlowAction(action);
			graphBtn.setMessage(graphAction);
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
				, FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
		if(action != null) {
			graphAction = new FlowGraphSMSAction();
			graphAction.setFlowAction(action);
			graphBtn.setSms(graphAction);
		}
		
		List<FlowAction> flowActions = flowActionProvider.findFlowActionsByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
				, FlowActionType.ENTER_SCRIPT.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
		if(flowActions != null && flowActions.size() > 0) {
			for(FlowAction fa : flowActions) {
				graphAction = new FlowGraphScriptAction();
				graphAction.setFlowAction(fa);
				graphBtn.getScripts().add(graphAction);				
			}
		}
		
		return graphBtn;
	}
	
	private void doSnapshot(FlowGraph flowGraph) {
		//step1 create flow
		Flow flow = flowGraph.getFlow();
		flow.setFlowMainId(flow.getId());
		flow.setId(null);
		flow.setStatus(FlowStatusType.RUNNING.getCode());
		flowProvider.createFlow(flowGraph.getFlow());
		
		//step2 create flowNodes
		for(FlowGraphNode node : flowGraph.getNodes()) {
			FlowNode flowNode = node.getFlowNode();
			Long oldFlowNodeId = flowNode.getId();
			flowNode.setId(null);
			flowNode.setFlowMainId(flow.getFlowMainId());
			flowNode.setFlowVersion(flow.getFlowVersion());
			flowNodeProvider.createFlowNode(flowNode);
			if(oldFlowNodeId == null) {// start or end flowNode
				continue;
			}
			
			//step3 create flowNode's buttons
			for(FlowGraphButton button : node.getApplierButtons()) {
				doSnapshotButton(flow, flowNode, button);
			}
			for(FlowGraphButton button : node.getProcessorButtons()) {
				doSnapshotButton(flow, flowNode, button);
			}
			
			//step6 copy flowNode's processors
			List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(oldFlowNodeId
					, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
			if(selections != null && selections.size() > 0) {
				for(FlowUserSelection sel: selections) {
					sel.setBelongTo(flowNode.getId());
					sel.setFlowMainId(flow.getFlowMainId());
					sel.setFlowVersion(flow.getFlowVersion());
					flowUserSelectionProvider.createFlowUserSelection(sel);
				}
			}
			
			//step7 copy flowNode's actions
			doSnapshotAction(flow, flowNode.getId(), node.getMessageAction());
			doSnapshotAction(flow, flowNode.getId(), node.getSmsAction());
			doSnapshotAction(flow, flowNode.getId(), node.getTickMessageAction());
			doSnapshotAction(flow, flowNode.getId(), node.getTickSMSAction());
			doSnapshotAction(flow, flowNode.getId(), node.getTrackApproveEnter());
			doSnapshotAction(flow, flowNode.getId(), node.getTrackRejectEnter());
			doSnapshotAction(flow, flowNode.getId(), node.getTrackTransferLeave());
		}
		
		//step8 copy flow's supervisor
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(flow.getFlowMainId()
				, FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode());
		if(selections != null && selections.size() > 0) {
			for(FlowUserSelection sel: selections) {
				sel.setBelongTo(flow.getFlowMainId());
				sel.setFlowMainId(flow.getFlowMainId());
				sel.setFlowVersion(flow.getFlowVersion());
				flowUserSelectionProvider.createFlowUserSelection(sel);
			}
		}
		
		flow.setStartNode(flowGraph.getNodes().get(0).getFlowNode().getId());
		flow.setEndNode(flowGraph.getNodes().get(flowGraph.getNodes().size()-1).getFlowNode().getId());
		flowProvider.updateFlow(flow);
	}
	
	private void doSnapshotButton(Flow flow, FlowNode flowNode, FlowGraphButton button) {
		FlowButton flowButton = button.getFlowButton();
//		Long oldFlowButtonId = flowButton.getId();
		
		flowButton.setId(null);
		flowButton.setFlowMainId(flow.getFlowMainId());
		flowButton.setFlowVersion(flow.getFlowVersion());
		flowButton.setFlowNodeId(flowNode.getId());
		flowButtonProvider.createFlowButton(flowButton);
		
		//step4 create flowButton's actions
		doSnapshotAction(flow, flowButton.getId(), button.getMessage());
		doSnapshotAction(flow, flowButton.getId(), button.getSms());
		if(null != button.getScripts()) {
			for(FlowGraphAction action: button.getScripts()) {
				doSnapshotAction(flow, flowButton.getId(), action);
			}	
		}
	}
	
	private void doSnapshotAction(Flow flow, Long belongTo, FlowGraphAction action) {
		if(action == null) {
			return;
		}
		
		FlowAction flowAction = action.getFlowAction();
		Long oldFlowActionId = flowAction.getId();
		flowAction.setId(null);
		flowAction.setFlowMainId(flow.getFlowMainId());
		flowAction.setFlowVersion(flow.getFlowVersion());
		flowAction.setBelongTo(belongTo);
		flowActionProvider.createFlowAction(flowAction);
		
		//step5 create user-selections
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(oldFlowActionId, FlowEntityType.FLOW_ACTION.getCode(), FlowUserType.PROCESSOR.getCode());
		if(selections != null && selections.size() > 0) {
			for(FlowUserSelection sel : selections) {
				sel.setBelongTo(flowAction.getId());
				sel.setFlowVersion(flowAction.getFlowVersion());
				flowUserSelectionProvider.createFlowUserSelection(sel);
			}
		}
	}
	
	@Override
	public FlowGraph getFlowGraph(Long flowId, Integer flowVer) {
		String fmt = String.format("%d:%d", flowId, flowVer);
		FlowGraph flowGraph = graphMap.get(fmt);
		if(flowGraph == null) {
			flowGraph = getSnapshotGraph(flowId, flowVer);
			graphMap.put(fmt, flowGraph);
		}
		
		return flowGraph;
	}
	
	private FlowGraph getSnapshotGraph(Long flowId, Integer flowVer) {
		if(flowVer <= 0) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot noexists");	
		}
		
		FlowGraph flowGraph = new FlowGraph();
		Flow flow = flowProvider.findSnapshotFlow(flowId, flowVer);
		if(flow == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot noexists");
		}
		flowGraph.setFlow(flow);
		
		List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, flowVer);
		flowNodes.sort((n1, n2) -> {
			return n1.getNodeLevel().compareTo(n2.getNodeLevel());
		});
		
		int i = 0;
		for(FlowNode fn : flowNodes) {
			if(!fn.getNodeLevel().equals(i)) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_LEVEL_ERR, "node_level error");	
			}
			
			if(fn.getNodeName().equals("START")) {
				flowGraph.getNodes().add(new FlowGraphNodeStart(fn));
			} else if(fn.getNodeName().equals("END")) {
				flowGraph.getNodes().add(new FlowGraphNodeEnd(fn));
			} else {
				flowGraph.getNodes().add(getFlowGraphNode(fn, flowVer));	
			}
			
			i++;
		}
		
		flowGraph.saveIds();
		
		return flowGraph;
	}
	
	@Override
	public FlowButtonDTO fireButton(FlowFireButtonCommand cmd) {
		UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(UserContext.current().getUser().getId());
		FlowCaseState ctx = flowStateProcessor.prepareButtonFire(userInfo, cmd);
		flowStateProcessor.step(ctx, ctx.getCurrentEvent());
		
		FlowButton btn = flowButtonProvider.getFlowButtonById(cmd.getButtonId());
		return ConvertHelper.convert(btn, FlowButtonDTO.class);
	}

	@Override
	public void disableFlow(Long flowId) {
		Flow flow = flowProvider.getFlowById(flowId);
		if(flow == null || !flow.getFlowVersion().equals(FlowConstants.FLOW_CONFIG_VER)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flow not exists");	
		}
		
		if(FlowStatusType.RUNNING.equals(flow.getStatus())) {
			flow.setStatus(FlowStatusType.STOP.getCode());
			Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
			flow.setUpdateTime(now);
			flowProvider.updateFlow(flow);
		}
	}

	@Override
	public Flow getEnabledFlow(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType) {
		return flowProvider.findEnabledFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
	}

	@Override
	public FlowCase createFlowCase(CreateFlowCaseCommand flowCaseCmd) {
		Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCaseCmd.getFlowMainId(), flowCaseCmd.getFlowVersion());
		if(snapshotFlow == null || snapshotFlow.getFlowVersion().equals(FlowConstants.FLOW_CONFIG_VER)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flow node error");	
		}
		
		FlowCase flowCase = ConvertHelper.convert(flowCaseCmd, FlowCase.class);
		flowCase.setCurrentNodeId(0l);
		if(flowCase.getApplyUserId() == null) {
			flowCase.setApplyUserId(UserContext.current().getUser().getId());	
		}
		UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(flowCase.getApplyUserId());
		flowCase.setApplierName(userInfo.getNickName());
		if(userInfo.getPhones() != null && userInfo.getPhones().size() > 0) {
			flowCase.setApplierPhone(userInfo.getPhones().get(0));	
		}
		
		FlowModuleDTO moduleDTO = this.getModuleById(snapshotFlow.getModuleId());
		
		flowCase.setNamespaceId(snapshotFlow.getNamespaceId());
		flowCase.setModuleId(snapshotFlow.getModuleId());
		flowCase.setModuleName(moduleDTO.getDisplayName());
		flowCase.setModuleType(snapshotFlow.getModuleType());
		flowCase.setOwnerId(snapshotFlow.getOwnerId());
		flowCase.setOwnerType(snapshotFlow.getOwnerType());
		flowCase.setCaseType(FlowCaseType.INNER.getCode());
		flowCase.setStatus(FlowCaseStatus.INITIAL.getCode());
		flowCaseProvider.createFlowCase(flowCase);
		flowCase = flowCaseProvider.getFlowCaseById(flowCase.getId());//get again for default values
		
		FlowCaseState ctx = flowStateProcessor.prepareStart(userInfo, flowCase);
		flowStateProcessor.step(ctx, ctx.getCurrentEvent());
		
		return flowCase;
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
	public FlowVariableResponse listFlowVariables(ListFlowVariablesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd) {
		SearchFlowCaseResponse resp = new SearchFlowCaseResponse();
		if(cmd.getUserId() == null) {
			cmd.setUserId(UserContext.current().getUser().getId());
		}
		if(cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		
		int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		cmd.setPageSize(count);
		ListingLocator locator = new ListingLocator();
		
		List<FlowCaseDetail> details = null;
		boolean isApplier = false;
		
		if(FlowCaseSearchType.APPLIER.equals(cmd.getFlowCaseSearchType())) {
			isApplier = true;
			details = flowCaseProvider.findApplierFlowCases(locator, count, cmd);
		} else {
			details = flowEventLogProvider.findProcessorFlowCases(locator, count, cmd);
		}
		
		List<FlowCaseDTO> dtos = new ArrayList<FlowCaseDTO>();
		if(details != null) {
			for(FlowCaseDetail detail : details) {
				FlowCaseDTO dto = ConvertHelper.convert(detail, FlowCaseDTO.class);
				if(isApplier) {
					FlowNode flowNode = flowNodeProvider.getFlowNodeById(dto.getCurrentNodeId());
					if(flowNode != null) {
						dto.setAllowApplierUpdate(flowNode.getAllowApplierUpdate());
					}
				}
				dtos.add(dto);
			}	
			resp.setNextPageAnchor(locator.getAnchor());
		} else {
			resp.setNextPageAnchor(null);
		}
		
		resp.setFlowCases(dtos);
		return resp;
	}

	@Override
	public FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId, Long inUserId, FlowUserType flowUserType) {
		Long userId = inUserId;
		if(userId == null) {
			userId = UserContext.current().getUser().getId();
		}
		
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
		List<FlowCaseEntity> entities = flowListenerManager.onFlowCaseDetailRender(flowCase);
		
		FlowCaseDetailDTO dto = ConvertHelper.convert(flowCase, FlowCaseDetailDTO.class);
		dto.setEntities(entities);
		if(dto.getStatus().equals(FlowCaseStatus.INVALID.getCode())) {
			return dto;
		}
		
		flowListenerManager.onFlowCaseDetailRender(flowCase);
		
		List<FlowButtonDTO> btnDTOS = new ArrayList<>();
		if(flowUserType == FlowUserType.PROCESSOR) {
			List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), flowUserType.getCode());
			buttons.stream().forEach((b)->{
				if(b.getStatus().equals(FlowButtonStatus.ENABLED.getCode()) 
						&& !b.getFlowStepType().equals(FlowStepType.COMMENT_STEP.getCode())) {
					FlowButtonDTO btnDTO = ConvertHelper.convert(b, FlowButtonDTO.class);
					btnDTOS.add(btnDTO);
				}
			});
		} else if(flowUserType == FlowUserType.APPLIER) {
			List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), flowUserType.getCode());
			buttons.stream().forEach((b)->{
				if(b.getStatus().equals(FlowButtonStatus.ENABLED.getCode())) {
					FlowButtonDTO btnDTO = ConvertHelper.convert(b, FlowButtonDTO.class);
					btnDTOS.add(btnDTO);
				}
			});
			
		} else {
			return dto;
		}
		dto.setButtons(btnDTOS);
		
		List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		Map<Long, FlowNode> nodeMap = new HashMap<Long, FlowNode>();
		for(FlowNode node : nodes) {
			nodeMap.put(node.getId(), node);
		}
		
		//got all nodes tracker logs
		List<FlowEventLog> stepLogs = flowEventLogProvider.findStepEventLogs(flowCaseId);
		List<FlowNodeLogDTO> nodeDTOS = new ArrayList<>();
		dto.setNodes(nodeDTOS);

		FlowNodeLogDTO logDTO = new FlowNodeLogDTO();
		logDTO.setNodeLevel(0);
		logDTO.setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.START_STEP));
		nodeDTOS.add(logDTO);
		
		FlowNode currNode = null;
		boolean absorted = false;
		for(FlowEventLog eventLog : stepLogs) {
			if(currNode == null || !currNode.getId().equals(eventLog.getFlowNodeId())) {
				currNode = nodeMap.get(eventLog.getFlowNodeId());
				final FlowNodeLogDTO nodeLogDTO = new FlowNodeLogDTO();
				nodeLogDTO.setNodeId(currNode.getId());
				nodeLogDTO.setNodeLevel(currNode.getNodeLevel());
				nodeLogDTO.setNodeName(currNode.getNodeName());
				
				if(flowCase.getStepCount().equals(eventLog.getStepCount())) {
					nodeLogDTO.setIsCurrentNode((byte)1);
					
					FlowButton commentBtn = flowButtonProvider.findFlowButtonByStepType(currNode.getId()
							, currNode.getFlowVersion(), FlowStepType.COMMENT_STEP.getCode(), flowUserType.getCode());
					if(commentBtn != null) {
						nodeLogDTO.setAllowComment((byte)1);
						nodeLogDTO.setCommentButtonId(commentBtn.getId());
					}
					if(eventLog.getButtonFiredStep().equals(FlowStepType.ABSORT_STEP.getCode())) {
						absorted = true;
						nodeLogDTO.setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.ABSORT_STEP));
					}
				}
				
				nodeDTOS.add(nodeLogDTO);
				
				List<FlowEventLog> trackerLogs = flowEventLogProvider.findEventLogsByNodeId(currNode.getId()
						, flowCase.getId(), eventLog.getStepCount(), flowUserType);
				if(trackerLogs != null) {
					trackerLogs.forEach((t)-> {
						FlowEventLogDTO eventDTO = ConvertHelper.convert(t, FlowEventLogDTO.class);
						nodeLogDTO.getLogs().add(eventDTO);			
					});
				}
			}
		}
		
		if(!absorted) {
			int i = 1;
			if(currNode != null) {
				i = currNode.getNodeLevel() + 1;
			}
			for(; i < nodes.size()-1; i++) {
				logDTO = new FlowNodeLogDTO();
				logDTO.setNodeLevel(nodes.get(i).getNodeLevel());
				logDTO.setNodeName(nodes.get(i).getNodeName());
				nodeDTOS.add(logDTO);				
			}
			logDTO = new FlowNodeLogDTO();
			logDTO.setNodeLevel(nodes.size());
			logDTO.setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.END_STEP));
			nodeDTOS.add(logDTO);
		}
		
		return dto;
	}

	@Override
	public FlowSubjectDTO postSubject(FlowPostSubjectCommand cmd) {
		FlowSubject subject = new FlowSubject();
		subject.setBelongEntity(cmd.getFlowEntityType());
		subject.setBelongTo(cmd.getFlowEntityId());
		subject.setContent(cmd.getContent());
		subject.setNamespaceId(UserContext.current().getNamespaceId());
		subject.setStatus(FlowStatusType.VALID.getCode());
		subject.setTitle(cmd.getTitle());
		flowSubjectProvider.createFlowSubject(subject);

		FlowSubjectDTO subjectDTO = ConvertHelper.convert(subject, FlowSubjectDTO.class);
		
		if(null != cmd.getImages() && cmd.getImages().size() > 0) {
			List<Attachment> attachments = new ArrayList<>();
			for(String image : cmd.getImages()) {
				Attachment attach = new Attachment();
				attach.setContentType(NewsCommentContentType.IMAGE.getCode());
				attach.setContentUri(image);
				attach.setCreatorUid(UserContext.current().getUser().getId());
				attach.setOwnerId(subject.getId());
				attachments.add(attach);
			}
			attachmentProvider.createAttachments(EhFlowAttachments.class, attachments);
			
			for(Attachment at : attachments) {
				String url = contentServerService.parserUri(at.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
				if(url != null && !url.isEmpty()) {
					subjectDTO.getImages().add(url);
				}
			}
			
		}
		
		return subjectDTO;
	}

	@Override
	public FlowEvaluateDTO postEvaluate(FlowPostEvaluateCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListFlowUserSelectionResponse listButtonProcessorSelections(
			ListButtonProcessorSelectionsCommand cmd) {
		ListFlowUserSelectionResponse resp = new ListFlowUserSelectionResponse();
		FlowButton btn = flowButtonProvider.getFlowButtonById(cmd.getButtonId());
		FlowNode flowNode = null;
		if(btn != null) {
			flowNode = flowNodeProvider.getFlowNodeById(btn.getFlowNodeId());		
		}
		if(flowNode == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "flow node not exists");	
		}
		if(cmd.getFlowUserType() == null) {
			cmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());
		}
		List<FlowUserSelection> seles = flowUserSelectionProvider.findSelectionByBelong(flowNode.getId(), FlowEntityType.FLOW_NODE.getCode(), cmd.getFlowUserType());
		if(seles != null) {
			seles.forEach((s) -> {
				resp.getSelections().add(ConvertHelper.convert(s, FlowUserSelectionDTO.class));
			});
		}
		
		return resp;
	}
	
	@Override
	public FlowModuleDTO getModuleById(Long moduleId) {
		if(moduleId.equals(111l)) {
			FlowModuleDTO dto = new FlowModuleDTO();
			dto.setModuleId(111l);
			dto.setModuleName("yuanqu");
			dto.setDisplayName("园区入驻");
			return dto;
		}
		
		if(moduleId.equals(112l)) {
			FlowModuleDTO dto = new FlowModuleDTO();
			dto.setModuleId(112l);
			dto.setModuleName("wuye");
			dto.setDisplayName("物业保修");
			return dto;
		}
		
		if(moduleId.equals(113l)) {
			FlowModuleDTO dto = new FlowModuleDTO();
			dto.setModuleId(113l);
			dto.setModuleName("yueka");
			dto.setDisplayName("月卡申请");
			return dto;
		}
		
		if(moduleId.equals(114l)) {
			FlowModuleDTO dto = new FlowModuleDTO();
			dto.setModuleId(114l);
			dto.setModuleName("jiaoliu");
			dto.setDisplayName("交流大厅");
			return dto;
		}
		
		return null;
	}

	@Override
	public ListFlowModulesResponse listModules(ListFlowModulesCommand cmd) {
		ListFlowModulesResponse resp = new ListFlowModulesResponse();
		List<FlowModuleDTO> modules = new ArrayList<FlowModuleDTO>();
		resp.setModules(modules);
		
		FlowModuleDTO dto = new FlowModuleDTO();
		dto.setModuleId(111l);
		dto.setModuleName("yuanqu");
		dto.setDisplayName("园区入驻");
		modules.add(dto);
		
		dto = new FlowModuleDTO();
		dto.setModuleId(112l);
		dto.setModuleName("wuye");
		dto.setDisplayName("物业保修");
		modules.add(dto);
		
		dto = new FlowModuleDTO();
		dto.setModuleId(113l);
		dto.setModuleName("yueka");
		dto.setDisplayName("月卡申请");
		modules.add(dto);
		
		dto = new FlowModuleDTO();
		dto.setModuleId(114l);
		dto.setModuleName("jiaoliu");
		dto.setDisplayName("交流大厅");
		modules.add(dto);
		
		return resp;
	}
	
	private List<Long> resolvUserSelections(List<FlowUserSelection> selections) {
		List<Long> users = new ArrayList<Long>();
		if(selections == null) {
			return users;
		}
		
		for(FlowUserSelection sel : selections) {
			if(FLowUserSourceType.SOURCE_USER.getCode().equals(sel.getSourceTypeA())) {
				users.add(sel.getSourceIdA());
			}	
		}
		
		return users;
	}
	
	@Override
	public void createSnapshotNodeProcessors(FlowCaseState ctx, FlowGraphNode nextNode) {
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(nextNode.getFlowNode().getId()
				, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
		List<Long> users = resolvUserSelections(selections);
		if(users.size() > 0) {
			for(Long selUser : users) {
				FlowEventLog log = new FlowEventLog();
				log.setId(flowEventLogProvider.getNextId());
				log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
				log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());//get real version
				log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
				if(ctx.getCurrentEvent() != null) {
					log.setFlowButtonId(ctx.getCurrentEvent().getFiredButtonId());	
				}
				
				log.setFlowNodeId(nextNode.getFlowNode().getId());
				log.setParentId(0l);
				log.setFlowCaseId(ctx.getFlowCase().getId());
				
//				if(ctx.getOperator() != null) {
//					log.setFlowUserId(ctx.getOperator().getId());
//					log.setFlowUserName(ctx.getOperator().getNickName());	
//				}
//				if(FlowEntityType.FLOW_SELECTION.getCode().equals(cmd.getFlowEntityType())) {
//					log.setFlowSelectionId(cmd.getEntityId());
//				}
				log.setFlowUserId(selUser);
				log.setStepCount(ctx.getFlowCase().getStepCount());
				
				log.setLogType(FlowLogType.NODE_ENTER.getCode());
				log.setLogTitle("");
				ctx.getLogs().add(log);
			}
		} else {
			LOGGER.warn("not processors for nodeId=" + nextNode.getFlowNode().getId() + " flowCaseId=" + ctx.getFlowCase().getId());
		}
	}
	
	@Override
	public void flushState(FlowCaseState ctx) {
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		dbProvider.execute((s) -> {
			ctx.getFlowCase().setLastStepTime(now);
			flowCaseProvider.updateFlowCase(ctx.getFlowCase());
			flowEventLogProvider.createFlowEventLogs(ctx.getLogs());
			return true;
		});		
	}
	
	private List<String> getAllParams(String renderText) {
        Matcher m = pParam.matcher(renderText);
        while(m.find()) {
        	LOGGER.info("param=" + m.group(1));
        }
        
        return null;
	}
	
	@Override
	public String parseActionTemplate(FlowCaseState ctx, Long actionId, String renderText) {
        String templateKey = String.format("action:%d", actionId);
        Map<String, String> model = null;
        List<String> params = getAllParams(renderText);
        if(params == null) {
        	return renderText;
        }
        //TODO render model.
        
        try {
        		templateLoader.putTemplate(templateKey, renderText);
        		Template freeMarkerTemplate = templateConfig.getTemplate(templateKey, "UTF8");
            if(freeMarkerTemplate != null) {
            	return FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, model);
            	}
        } catch (Exception ex) {
        	  LOGGER.error("load template actionId=" + actionId + " renderText=" + renderText, ex);
          }
        
        return null;
	}

	@Override
	public FlowSubjectDTO getSubectById(Long subjectId) {
		FlowSubject subject = flowSubjectProvider.getFlowSubjectById(subjectId);
		FlowSubjectDTO subjectDTO = ConvertHelper.convert(subject, FlowSubjectDTO.class);
		
		List<Attachment> attaches = attachmentProvider.listAttachmentByOwnerId(EhFlowAttachments.class, subjectId);
		if(attaches != null) {
			for(Attachment at : attaches) {
				String url = contentServerService.parserUri(at.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
				if(url != null && !url.isEmpty()) {
					subjectDTO.getImages().add(url);
				}
			}
		}
		
		return subjectDTO;
	}

	@Override
	public FlowDTO getFlowById(Long flowId) {
		Flow flow = flowProvider.getFlowById(flowId);
		return ConvertHelper.convert(flow, FlowDTO.class);
	}

}
