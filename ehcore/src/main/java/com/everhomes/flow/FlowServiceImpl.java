package com.everhomes.flow;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.parking.ParkingFlowConstant;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.server.schema.tables.pojos.EhFlowAttachments;
import com.everhomes.server.schema.tables.pojos.EhNewsAttachments;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	@Autowired
	private FlowVariableProvider flowVariableProvider;
	
	@Autowired
	private FlowTimeoutService flowTimeoutService;
	
	@Autowired
	private ServiceModuleProvider serviceModuleProvider;
	
    @Autowired
    MessagingService messagingService;
    
    @Autowired
    LocaleTemplateService localeTemplateService;
    
    @Autowired
    FlowUserSelectionService flowUserSelectionService;
    
    @Autowired
    private FlowEvaluateItemProvider flowEvaluateItemProvider;
    
    @Autowired
    private FlowEvaluateProvider flowEvaluateProvider;
    
    @Autowired
    private FlowScriptProvider flowScriptProvider;
    
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    LocaleTemplateProvider localeTemplateProvider;
    
    @Autowired
    private SmsProvider smsProvider;
    
    private static final Pattern pParam = Pattern.compile("\\$\\{([^\\}]*)\\}");
    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    
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
    	obj.setNamespaceId(cmd.getNamespaceId());
    	obj.setProjectId(cmd.getProjectId());
    	obj.setProjectType(cmd.getProjectType());
    	obj.setStringTag1(cmd.getStringTag1());
    	flowListenerManager.onFlowCreating(obj);
    	
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
		case FLOW_EVALUATE:
			return flowProvider.getFlowById(entityId);
		case FLOW_NODE:
			FlowNode flowNode = flowNodeProvider.getFlowNodeById(entityId);
			if(flowNode == null) {
				return null;
			}
			if(flowNode.getFlowMainId().equals(0l)) {
				return null;
			}
			return getFlowByEntity(flowNode.getFlowMainId(), FlowEntityType.FLOW, ++loop);
		case FLOW_BUTTON:
			FlowButton flowButton = flowButtonProvider.getFlowButtonById(entityId);
			if(flowButton == null) {
				return null;
			}
			if(flowButton.getFlowMainId().equals(0l)) {
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
			
			if(flowAction.getFlowMainId().equals(0l)) {
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
			if(flowSel.getFlowMainId().equals(0l)) {
				Flow flow = getFlowByEntity(flowSel.getBelongTo(), FlowEntityType.fromCode(flowSel.getBelongEntity()), ++loop);
				if(flow != null) {
					flowSel.setFlowMainId(flow.getTopId());
					flowUserSelectionProvider.updateFlowUserSelection(flowSel);
				}
			} else {
				return getFlowByEntity(flowSel.getFlowMainId(), FlowEntityType.FLOW, ++loop);
			}
			
		default:
			return null;
		}
	}
	
	@Override
	public ListFlowBriefResponse listBriefFlows(ListFlowCommand cmd) {
		if(cmd == null) {//TODO need this ?
			return null;
		}
		
		if(cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		
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
		if(stepType == FlowStepType.TRANSFER_STEP) {
			button.setNeedProcessor((byte)1);
		}
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
		
		resp.setApplierButtons(dtos1);
		resp.setProcessorButtons(dtos2);
		
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
		if(actionDTO.getStatus() != null && actionDTO.getStatus().equals(FlowActionStatus.ENABLED.getCode())) {
			actionDTO.setEnabled((byte)1);
		}
		
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

			if(actionInfo.getReminderTickMinute() != null) {
				action.setReminderTickMinute(actionInfo.getReminderTickMinute());	
			}
			if(actionInfo.getReminderAfterMinute() != null) {
				action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());	
			}
			if(actionInfo.getTrackerApplier() != null) {
				action.setTrackerApplier(actionInfo.getTrackerApplier());	
			}
			if(actionInfo.getTrackerProcessor() != null) {
				action.setTrackerProcessor(actionInfo.getTrackerProcessor());	
			}
			
			if(actionInfo.getEnabled() == null || actionInfo.getEnabled() > 0) {
				action.setStatus(FlowActionStatus.ENABLED.getCode());	
			} else {
				action.setStatus(FlowActionStatus.DISABLED.getCode());
			}
			
			action.setRenderText(actionInfo.getRenderText());
			flowActionProvider.createFlowAction(action);
			
		} else {
			if(actionInfo.getEnabled() == null || actionInfo.getEnabled() > 0) {
				action.setStatus(FlowActionStatus.ENABLED.getCode());	
			} else {
				action.setStatus(FlowActionStatus.DISABLED.getCode());
			}
			
			if(actionInfo.getReminderTickMinute() != null) {
				action.setReminderTickMinute(actionInfo.getReminderTickMinute());	
			}
			if(actionInfo.getReminderAfterMinute() != null) {
				action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());	
			}
			if(actionInfo.getTrackerApplier() != null) {
				action.setTrackerApplier(actionInfo.getTrackerApplier());	
			}
			if(actionInfo.getTrackerProcessor() != null) {
				action.setTrackerProcessor(actionInfo.getTrackerProcessor());	
			}
			
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
		userSel.setSelectionName(selCmd.getSelectionName());
		
		updateFlowUserName(userSel);
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
				return createNodeAction(flowNode, cmd.getRejectTracker(), FlowActionType.TRACK.getCode()
						, FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.REJECT_STEP.getCode());
			});
		}
		
		if(cmd.getTransferTracker() != null) {
			dbProvider.execute((a) -> {
				return createNodeAction(flowNode, cmd.getTransferTracker(), FlowActionType.TRACK.getCode()
						, FlowActionStepType.STEP_LEAVE.getCode(), FlowStepType.TRANSFER_STEP.getCode());
			});			
		}
		
		return getFlowNodeDetail(cmd.getFlowNodeId());
	}

	@Override
	public FlowNodeDTO updateFlowNode(UpdateFlowNodeCommand cmd) {
		Flow flow = getFlowByEntity(cmd.getFlowNodeId(), FlowEntityType.FLOW_NODE);
		flowMarkUpdated(flow);
		
		FlowNode flowNode = new FlowNode();
		flowNode.setId(cmd.getFlowNodeId());
		flowNode.setNodeName(cmd.getFlowNodeName());
		flowNode.setAutoStepMinute(cmd.getAutoStepMinute());
		flowNode.setAutoStepType(cmd.getAutoStepType());
		flowNode.setAllowApplierUpdate(cmd.getAllowApplierUpdate());
		flowNode.setAllowTimeoutAction(cmd.getAllowTimeoutAction());
		flowNode.setParams(cmd.getParams());
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
				sel.setNamespaceId(UserContext.getCurrentNamespaceId());
				if(sel.getOrganizationId() == null) {
					sel.setOrganizationId(flow.getOrganizationId());
				}
				updateFlowUserName(sel);
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
		List<FlowUserSelection> seles = flowUserSelectionProvider.findSelectionByBelong(cmd.getBelongTo(), cmd.getFlowEntityType(), cmd.getFlowUserType(), 0);
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
		if(cmd.getButtonName() != null) {
			flowButton.setButtonName(cmd.getButtonName());	
		}
		if(cmd.getDescription() != null) {
			flowButton.setDescription(cmd.getDescription());	
		}
		if(cmd.getGotoNodeId() != null) {
			flowButton.setGotoNodeId(cmd.getGotoNodeId());	
		}
		if(cmd.getNeedSubject() != null) {
			flowButton.setNeedSubject(cmd.getNeedSubject());	
		}
		if(cmd.getNeedProcessor() != null) {
			flowButton.setNeedProcessor(cmd.getNeedProcessor());	
		}
		if(cmd.getRemindCount() != null) {
			flowButton.setRemindCount(cmd.getRemindCount());
		}
		
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
	
	@Override
	public FlowButtonDTO enableFlowButton(Long buttonId) {
		Flow flow = getFlowByEntity(buttonId, FlowEntityType.FLOW_BUTTON);
		flowMarkUpdated(flow);
		
		FlowButton flowButton = flowButtonProvider.getFlowButtonById(buttonId);
		if(flowButton != null) {
			flowButton.setStatus(FlowButtonStatus.ENABLED.getCode());
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
	
	private void updateFlowVersion(Flow flow) {//TODO better for version increment
		Flow snapshotFlow = flowProvider.getSnapshotFlowById(flow.getId());
		if(snapshotFlow != null) {
			clearSnapshotGraph(snapshotFlow);
			if(snapshotFlow.getFlowVersion() > flow.getFlowVersion()) {
				flow.setFlowVersion(snapshotFlow.getFlowVersion() + 1);
			}
		}
		
        String key = String.format("flow:%d", flow.getId());
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Long ver = redisTemplate.opsForValue().increment(key, flow.getFlowVersion());
        if(ver == null || ver < flow.getFlowVersion()) {
        	redisTemplate.opsForValue().set(key, String.valueOf(flow.getFlowVersion()));
        }
	}

	@Override
	public Boolean enableFlow(Long flowId) {
		final FlowGraph flowGraph = new FlowGraph();
		Flow flow = flowProvider.getFlowById(flowId);
		
		Flow enabledFlow = flowProvider.getEnabledConfigFlow(flow.getNamespaceId(), flow.getModuleId(), flow.getModuleType(), flow.getOwnerId(), flow.getOwnerType());
		if(enabledFlow != null && !enabledFlow.getId().equals(flowId)
				&& enabledFlow.getStatus().equals(FlowStatusType.RUNNING.getCode())) {
			enabledFlow.setStatus(FlowStatusType.STOP.getCode());
			flowProvider.updateFlow(enabledFlow);
		}
		
		if(flow.getStatus().equals(FlowStatusType.STOP.getCode())) {
			//restart it
			flow.setStatus(FlowStatusType.RUNNING.getCode());
			Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
			flow.setUpdateTime(now);
			flow.setRunTime(now);
			flowProvider.updateFlow(flow);
			return true;
		} else if(flow.getStatus().equals(FlowStatusType.RUNNING.getCode())) {
			//already running
			return true;
		}
		
		
		updateFlowVersion(flow);
		
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
		
		//TODO busy check ???
//		if(!flowNew.getUpdateTime().equals(flow.getUpdateTime())) {
//			//the flow is updated, retry
//			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CONFIG_BUSY, "flow has changed, retry");
//		}
		
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		flow.setUpdateTime(now);
		
		boolean isOk = true;
		try {
			dbProvider.execute((s)->{				
				doSnapshot(flowGraph);
				return true;
			});
			
		} catch(Exception ex) {
			isOk = false;
			LOGGER.error("do snapshot error", ex);
		}
		
		if(flow.getFlowMainId().equals(0l)) {
			isOk = false;
		}
		
		if(isOk) {
			//running now
			flow.setId(flow.getFlowMainId());
			flow.setFlowMainId(0l);
			flow.setRunTime(now);
			flow.setStatus(FlowStatusType.RUNNING.getCode());
			flowProvider.updateFlow(flow);
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
			graphAction = new FlowGraphMessageAction(action.getReminderAfterMinute(), action.getReminderTickMinute(), 50l);
			graphAction.setFlowAction(action);
			graphNode.setTickMessageAction(graphAction);	
		}
		
		action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
				, FlowActionType.TICK_SMS.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
		if(action != null) {
			graphAction = new FlowGraphSMSAction();//TODO action.getReminderAfterMinute(), action.getReminderTickMinute(), 50l
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
				, FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode(), 0);
		if(selections != null && selections.size() > 0) {
			for(FlowUserSelection sel: selections) {
				sel.setBelongTo(flow.getFlowMainId());
				sel.setFlowMainId(flow.getFlowMainId());
				sel.setFlowVersion(flow.getFlowVersion());
				flowUserSelectionProvider.createFlowUserSelection(sel);
			}
		}
		
		//step9 copy flow's evaluate
		List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flow.getFlowMainId(), FlowConstants.FLOW_CONFIG_VER);
		if(items != null && items.size() > 0) {
			items.forEach(item -> {
				item.setId(null);
				item.setFlowMainId(flow.getFlowMainId());
				item.setFlowVersion(flow.getFlowVersion());
			});
			flowEvaluateItemProvider.createFlowEvaluateItem(items);
			
			//TODO support action for evaluate
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
		if(flowVer.equals(0)) {
			return getConfigGraph(flowId);
		}
		
		String fmt = String.format("%d:%d", flowId, flowVer);
		FlowGraph flowGraph = graphMap.get(fmt);
		if(flowGraph == null) {
			flowGraph = getSnapshotGraph(flowId, flowVer);
			graphMap.put(fmt, flowGraph);
		}
		
		return flowGraph;
	}
	
	private void clearSnapshotGraph(Flow snapshotFlow) {
		if(snapshotFlow != null) {
			for(int i = 1; i <= snapshotFlow.getFlowVersion(); i++) {
				String fmt = String.format("%d:%d", snapshotFlow.getFlowMainId(), i);
				graphMap.remove(fmt);
			}	
		}
	}
	
	@Override
	public void clearFlowGraphCache(Long flowId) {
		Flow snapshotFlow = flowProvider.getSnapshotFlowById(flowId);
		clearSnapshotGraph(snapshotFlow);
	}
	
	private FlowGraph getSnapshotGraph(Long flowId, Integer flowVer) {
		if(flowVer <= 0) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot noexists");	
		}
		
		FlowGraph flowGraph = new FlowGraph();
		flowGraph.setCreateTime(System.currentTimeMillis());
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
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_LEVEL_ERR, "node_level error flowId=" + flowId + " ");	
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
	
	private FlowGraph getConfigGraph(Long flowId) {
		FlowGraph flowGraph = new FlowGraph();
		Flow flow = flowProvider.getFlowById(flowId);
		if(flow == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot noexists");
		}
		flowGraph.setFlow(flow);
		
		List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, 0);
		flowNodes.sort((n1, n2) -> {
			return n1.getNodeLevel().compareTo(n2.getNodeLevel());
		});
		
		int i = 1;
		for(FlowNode fn : flowNodes) {
			if(!fn.getNodeLevel().equals(i)) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_LEVEL_ERR, "node_level error");	
			}
			
			flowGraph.getNodes().add(getFlowGraphNode(fn, 0));	
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
    public void processStepTimeout(FlowTimeout ft) {
		FlowCaseState ctx = flowStateProcessor.prepareStepTimeout(ft);
		if(ctx != null) {
			flowStateProcessor.step(ctx, ctx.getCurrentEvent());	
		}
    }
	
	@Override
	public void processAutoStep(FlowAutoStepDTO stepDTO) {
		FlowCaseState ctx = flowStateProcessor.prepareAutoStep(stepDTO);
		if(ctx != null) {
			flowStateProcessor.step(ctx, ctx.getCurrentEvent());	
		}
	}
	
	@Override
	public void processMessageTimeout(FlowTimeout ft) {
		FlowTimeoutMessageDTO dto = (FlowTimeoutMessageDTO)StringHelper.fromJsonString(ft.getJson(), FlowTimeoutMessageDTO.class);
		if(dto == null) {
			LOGGER.error("flowtimeout error ft=" + ft.getId());
			return;
		}
//		FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
//		stepDTO.setAutoStepType(FlowStepType.NO_STEP.getCode());
//		stepDTO.setFlowCaseId(dto.getFlowCaseId());
//		stepDTO.setFlowMainId(dto.getFlowMainId());
//		stepDTO.setFlowNodeId(dto.getFlowNodeId());
//		stepDTO.setFlowVersion(dto.getFlowVersion());
//		stepDTO.setStepCount(dto.getStepCount());
		FlowAutoStepDTO stepDTO = ConvertHelper.convert(dto, FlowAutoStepDTO.class);
		FlowCaseState ctx = flowStateProcessor.prepareNoStep(stepDTO);
		if(ctx == null) {
			LOGGER.error("flowtimeout context error ft=" + ft.getId());
			return;
		}
		
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(dto.getFlowCaseId());
		FlowAction flowAction = flowActionProvider.getFlowActionById(ft.getBelongTo());
		ctx.setFlowCase(flowCase);
		if(FlowActionType.TICK_MESSAGE.getCode().equals(flowAction.getActionType())
				|| FlowActionType.TICK_SMS.getCode().equals(flowAction.getActionType())) {
			//check if the step is processed
			if(!flowCase.getStepCount().equals(dto.getStepCount()) || !flowCase.getCurrentNodeId().equals(dto.getFlowNodeId())) {
				//NOT OK
				LOGGER.info("ft timeout occur but step is processed! ft=" + ft.getId());
				return;
			}
		}

		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(ft.getBelongTo()
				, ft.getBelongEntity(), FlowUserType.PROCESSOR.getCode());
		List<Long> users = resolvUserSelections(ctx, FlowEntityType.FLOW_ACTION, flowAction.getId(), selections);
		String dataStr = parseActionTemplate(ctx, ft.getBelongTo(), flowAction.getRenderText());
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("flowtimeout tick message, text={}, size={}", dataStr, users.size());
		
		for(Long userId : users) {
			
			LOGGER.debug("flowtimeout tick message, text={}, userId={}", dataStr, userId);
			
			MessageDTO messageDto = new MessageDTO();
			messageDto.setAppId(AppConstants.APPID_MESSAGING);
			messageDto.setSenderUid(User.SYSTEM_UID);
			messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()), 
	                new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.BIZ_USER_LOGIN.getUserId())));
	        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
	        messageDto.setBody(dataStr);
	        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
	        
	        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
	                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
		}
		
		if(dto.getRemindTick() != null && dto.getRemindTick() > 0
				&& dto.getRemindCount() != null && dto.getRemindCount() > 0) {
			dto.setRemindCount(dto.getRemindCount()-1);
//			dto.setTimeoutAtTick(dto.getRemindTick());
			ft.setId(null);
			ft.setJson(dto.toString());
			Long timeoutTick = DateHelper.currentGMTTime().getTime() + dto.getRemindTick() * 60*1000l;
			ft.setTimeoutTick(new Timestamp(timeoutTick));
			flowTimeoutService.pushTimeout(ft);
		}
	}

	@Override
	public void disableFlow(Long flowId) {
		Flow flow = flowProvider.getFlowById(flowId);
		if(flow == null /* || !flow.getFlowVersion().equals(FlowConstants.FLOW_CONFIG_VER) */ ) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flow not exists");	
		}
		
		if(flow.getStatus() != null && flow.getStatus().equals(FlowStatusType.RUNNING.getCode())) {
			
			Flow snapshotFlow = flowProvider.getSnapshotFlowById(flowId);
			clearSnapshotGraph(snapshotFlow);
			
			flow.setStatus(FlowStatusType.STOP.getCode());
			Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
			flow.setUpdateTime(now);
			flowProvider.updateFlow(flow);
		}
	}

	/**
	 * 获取正在启用的 Flow
	 */
	@Override
	public Flow getEnabledFlow(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType) {
		Flow flow = flowProvider.getEnabledConfigFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
		if(flow != null) {
			return flowProvider.getSnapshotFlowById(flow.getId());
		}
		return null;
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
		
		if(flowCaseCmd.getProjectId() == null) {
			//use default projectId
			flowCase.setProjectId(snapshotFlow.getProjectId());
			flowCase.setProjectType(snapshotFlow.getProjectType());
		}
		
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
	
	private FlowVariableResponse listFlowTextVariables(Flow flow, ListFlowVariablesCommand cmd) {
		FlowVariableResponse resp = new FlowVariableResponse();
		List<FlowVariableDTO> dtos = new ArrayList<>();
		resp.setDtos(dtos);
		
        List<FlowVariable> vars = new ArrayList<>();
        String para = null;
        List<FlowVariable> vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
        		, flow.getOwnerId(), flow.getOwnerType(), flow.getModuleId(), flow.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if(vars2 != null) {
        	vars.addAll(vars2);
        }
        vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
        		, 0l, null, flow.getModuleId(), flow.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if(vars2 != null) {
        	vars.addAll(vars2);
        }
        
        vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
        		, 0l, null, flow.getModuleId(), flow.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if(vars2 != null) {
        	vars.addAll(vars2);
        }
        
        vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
        		, 0l, null, 0l, null, para, FlowVariableType.TEXT.getCode());
        if(vars2 != null) {
        	vars.addAll(vars2);
        }
        
        if(!cmd.getNamespaceId().equals(0)) {
        	vars2 = flowVariableProvider.findVariables(0
        		, 0l, null, 0l, null, para, FlowVariableType.TEXT.getCode());
        	if(vars2 != null) {
        		vars.addAll(vars2);
        	} 	
        }
        
        Map<String, Long> map = new HashMap<String, Long>();
        for(FlowVariable var : vars) {
        	if(!map.containsKey(var.getName())) {
        		dtos.add(ConvertHelper.convert(var, FlowVariableDTO.class));
        		map.put(var.getName(), 1l);
        	}
        }
        
        return resp;		
	}

	@Override
	public FlowVariableResponse listFlowVariables(ListFlowVariablesCommand cmd) {
		if(cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(UserContext.current().getNamespaceId());
		}
		if(cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(0);
		}
		
		String flowVariableType = FlowVariableType.TEXT.getCode();
		if(flowVariableType.equals(cmd.getFlowVariableType())) {
			FlowEntityType entityType = FlowEntityType.fromCode(cmd.getEntityType());
			if(entityType == null) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow params error");	
			}
			
			Flow flow = getFlowByEntity(cmd.getEntityId(), entityType);
			if(flow == null) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow not found");	
			}
			
			return listFlowTextVariables(flow, cmd);
		} else {
			FlowVariableResponse resp = new FlowVariableResponse();
			String para = null;
			List<FlowVariable> vars = flowVariableProvider.findVariables(0
	        		, 0l, null, 0l, null, para, FlowVariableType.NODE_USER.getCode());
			
			List<FlowVariableDTO> dtos = new ArrayList<>();
			resp.setDtos(dtos);
			
			Map<String, Long> map = new HashMap<String, Long>();
			for(FlowVariable var : vars) {
				if(!map.containsKey(var.getName())) {
	        		dtos.add(ConvertHelper.convert(var, FlowVariableDTO.class));
	        		map.put(var.getName(), 1l);
	        	}
			}
			
			return resp;
		}
	}
	
	private void updateCaseDTO(FlowCase flowCase, FlowNode flowNode, FlowCaseDTO dto, int type) {
		dto.setAllowApplierUpdate(flowNode.getAllowApplierUpdate());
		dto.setCurrNodeParams(flowNode.getParams());
		dto.setFlowNodeName(flowNode.getNodeName());
		List<FlowUserSelection> sels = flowUserSelectionProvider.findSelectionByBelong(flowNode.getId()
				, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
		
		String name;
		if(sels != null && sels.size() > 0) {
			updateFlowUserName(sels.get(0));
			name = sels.get(0).getSelectionName();
			for(int i = 1; i < sels.size() && i < 3; i++) {
				updateFlowUserName(sels.get(i));
				name = name + "," + sels.get(i).getSelectionName();
			}
			dto.setProcessUserName(name);
		}
		
		Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		
		//evaluate
		dto.setNeedEvaluate((byte)0);
		List<FlowEvaluate> evas = flowEvaluateProvider.findEvaluates(flowCase.getId(), snapshotFlow.getFlowMainId(), snapshotFlow.getFlowVersion());
		if(evas != null && evas.size() > 0) {
			dto.setEvaluateScore(new Integer(evas.get(0).getStar()));
		} else {
			 if(1 == type && !snapshotFlow.getNeedEvaluate().equals((byte)0) 
					 && flowNode.getNodeLevel() >= snapshotFlow.getEvaluateStart() 
					 && flowNode.getNodeLevel() <= snapshotFlow.getEvaluateEnd() ) {
				 dto.setNeedEvaluate((byte)1);
			 }
		}
		
	}

	@Override
	public SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd) {
		SearchFlowCaseResponse resp = new SearchFlowCaseResponse();
		if(cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		if(cmd.getFlowCaseSearchType() == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow param error");	
		}
		
		if(!cmd.getFlowCaseSearchType().equals(FlowCaseSearchType.ADMIN.getCode()) && cmd.getUserId() == null) {
			cmd.setUserId(UserContext.current().getUser().getId());
		}
		
		int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		cmd.setPageSize(count);
		ListingLocator locator = new ListingLocator();
		
		List<FlowCaseDetail> details = null;
		
		int type = 0;
		if(cmd.getFlowCaseSearchType().equals(FlowCaseSearchType.APPLIER.getCode())) {
			type = 1;
			details = flowCaseProvider.findApplierFlowCases(locator, count, cmd);
		} else if(cmd.getFlowCaseSearchType().equals(FlowCaseSearchType.ADMIN.getCode())) {
			type = 2;
			details = flowCaseProvider.findAdminFlowCases(locator, count, cmd);
		} else {
			type = 3;
			details = flowEventLogProvider.findProcessorFlowCases(locator, count, cmd);
			
		}
		
		List<FlowCaseDTO> dtos = new ArrayList<FlowCaseDTO>();
		if(details != null) {
			for(FlowCaseDetail detail : details) {
				FlowCaseDTO dto = ConvertHelper.convert(detail, FlowCaseDTO.class);
				FlowNode flowNode = flowNodeProvider.getFlowNodeById(dto.getCurrentNodeId());
				if(flowNode != null) {
					updateCaseDTO(detail, flowNode, dto, type);	
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
		return getFlowCaseDetail(flowCaseId, inUserId, flowUserType, false);
	}
	
	private FlowButtonDTO flowButtonToDTO(Flow snapshotFlow, FlowButton b) {
		FlowButtonDTO btnDTO = ConvertHelper.convert(b, FlowButtonDTO.class);
		
		FlowStepType stepType = FlowStepType.fromCode(b.getFlowStepType());
		if(stepType != FlowStepType.APPROVE_STEP && stepType != FlowStepType.TRANSFER_STEP) {
			btnDTO.setNeedProcessor((byte)0);
		}
		
		return btnDTO;
	}
	
	@Override
	public FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId, Long inUserId, FlowUserType flowUserType, boolean checkProcessor) {
		Long userId = inUserId;
		if(userId == null) {
			userId = UserContext.current().getUser().getId();
		}
		
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
		Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		
		List<FlowCaseEntity> entities = flowListenerManager.onFlowCaseDetailRender(flowCase, flowUserType);
		
		FlowCaseDetailDTO dto = ConvertHelper.convert(flowCase, FlowCaseDetailDTO.class);
		dto.setEntities(entities);
		if(dto.getStatus().equals(FlowCaseStatus.INVALID.getCode())) {
			return dto;
		}
		
		List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		Map<Long, FlowNode> nodeMap = new HashMap<Long, FlowNode>();
		for(FlowNode node : nodes) {
			nodeMap.put(node.getId(), node);
		}
		if(nodes.size() < 3) {
			return dto;
		}
		
		List<FlowButtonDTO> btnDTOS = new ArrayList<>();
		if(flowUserType == FlowUserType.PROCESSOR) {
			
			if(!checkProcessor || (null != flowEventLogProvider.isProcessor(userId, flowCase))) {
					List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), flowUserType.getCode());
					buttons.stream().forEach((b)->{
						boolean isAdd = true;
						if(flowCase.getCurrentNodeId().equals(nodes.get(1).getId()) 
								&& b.getFlowStepType().equals(FlowStepType.REJECT_STEP.getCode())) {
							isAdd = false;
						}
//						if(flowCase.getCurrentNodeId().equals(nodes.get(nodes.size()-1).getId()) 
//								&& b.getFlowStepType().equals(FlowStepType.APPROVE_STEP.getCode())) {
//							isAdd = false;
//						}

						if(isAdd && b.getStatus().equals(FlowButtonStatus.ENABLED.getCode()) 
								&& !b.getFlowStepType().equals(FlowStepType.COMMENT_STEP.getCode())) {
							FlowButtonDTO btnDTO = flowButtonToDTO(snapshotFlow, b);
							btnDTOS.add(btnDTO);
						}
					});
			}
			
			dto.setButtons(btnDTOS);
		} else if(flowUserType == FlowUserType.APPLIER) {
			List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), flowUserType.getCode());
			buttons.stream().forEach((b)->{
				if(b.getStatus().equals(FlowButtonStatus.ENABLED.getCode())) {
					FlowButtonDTO btnDTO = ConvertHelper.convert(b, FlowButtonDTO.class);
					btnDTOS.add(btnDTO);
				}
			});
		
			dto.setButtons(btnDTOS);
		}//SUPERVISOR at last
		
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
				nodeLogDTO.setParams(currNode.getParams());
				
				if(flowCase.getStepCount().equals(eventLog.getStepCount())) {
					nodeLogDTO.setIsCurrentNode((byte)1);
					dto.setCurrNodeParams(currNode.getParams());
					
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

				SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd HH:mm");
				List<FlowEventLog> trackerLogs = flowEventLogProvider.findEventLogsByNodeId(currNode.getId()
						, flowCase.getId(), eventLog.getStepCount(), flowUserType);
				if(trackerLogs != null) {
					trackerLogs.forEach((t)-> {
						FlowEventLogDTO eventDTO = ConvertHelper.convert(t, FlowEventLogDTO.class);
						if(FlowStepType.EVALUATE_STEP.getCode().equals(t.getButtonFiredStep())) {
							eventDTO.setIsEvaluate((byte)1);
						}
						if(eventDTO.getLogContent() != null) {
							String dateStr = sdf1.format(new Date(eventDTO.getCreateTime().getTime()));
							eventDTO.setLogContent(dateStr + " " + eventDTO.getLogContent());
						}
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
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
		Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		if(flowCase == null || snapshotFlow == null || snapshotFlow.getNeedEvaluate().equals((byte)0)
				|| flowCase.getStatus().equals(FlowCaseStatus.INVALID.getCode())
				) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS, "flowcase noexists, flowCaseId=" + flowCase);
		}
		
		Map<Long, FlowEvaluateItemStar> evaMap = new HashMap<Long, FlowEvaluateItemStar>();
		if(cmd.getStars() != null && cmd.getStars().size() > 0) {
			cmd.getStars().forEach(ev -> {
				evaMap.put(ev.getItemId(), ev);
			});
		}
		
		List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		if(items == null || evaMap.size() != items.size()) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "params error");
		}
		
		List<FlowEvaluate> flowEvas = new ArrayList<>();
		for(FlowEvaluateItem item : items) {
			FlowEvaluate eva = new FlowEvaluate();
			eva.setEvaluateItemId(item.getId());
			eva.setFlowCaseId(cmd.getFlowCaseId());
			eva.setFlowMainId(flowCase.getFlowMainId());
			eva.setFlowVersion(flowCase.getFlowVersion());
			eva.setFlowNodeId(flowCase.getCurrentNodeId());
			eva.setModuleId(flowCase.getModuleId());
			eva.setModuleType(flowCase.getModuleType());
			eva.setProjectId(flowCase.getProjectId());
			eva.setProjectType(flowCase.getProjectType());
			eva.setNamespaceId(flowCase.getNamespaceId());
			eva.setOwnerId(flowCase.getOwnerId());
			eva.setOwnerType(flowCase.getOwnerType());
			eva.setUserId(UserContext.current().getUser().getId());
			eva.setStar(evaMap.get(item.getId()).getStat());
			flowEvas.add(eva);
		}
		
		flowEvaluateProvider.createFlowEvaluate(flowEvas);
		
		FlowEventLog tracker = new FlowEventLog();
		Map<String, Object> templateMap = new HashMap<String, Object>();
		
		templateMap.put("score", String.valueOf(flowEvas.get(0).getStar()));
		tracker.setLogContent(getFireButtonTemplate(FlowStepType.EVALUATE_STEP, templateMap));	
		tracker.setStepCount(flowCase.getStepCount());
		tracker.setId(flowEventLogProvider.getNextId());
		tracker.setFlowMainId(flowCase.getFlowMainId());
		tracker.setFlowVersion(flowCase.getFlowVersion());
		tracker.setNamespaceId(flowCase.getNamespaceId());
		tracker.setFlowNodeId(flowCase.getCurrentNodeId());
		tracker.setParentId(0l);
		tracker.setFlowCaseId(flowCase.getId());
		tracker.setFlowUserId(UserContext.current().getUser().getId());
		tracker.setLogType(FlowLogType.NODE_TRACKER.getCode());
		tracker.setButtonFiredStep(FlowStepType.EVALUATE_STEP.getCode());
		if(items.size() > 0) {
			tracker.setSubjectId(1l);	
		}
		tracker.setTrackerApplier(1l);
		tracker.setTrackerProcessor(1l);	
		flowEventLogProvider.createFlowEventLog(tracker);
		
		if(snapshotFlow.getEvaluateStep() != null 
				&& snapshotFlow.getEvaluateStep().equals(FlowStepType.APPROVE_STEP.getCode())) {
			FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
			stepDTO.setAutoStepType(snapshotFlow.getEvaluateStep());
			stepDTO.setFlowCaseId(flowCase.getId());
			stepDTO.setFlowMainId(flowCase.getFlowMainId());
			stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
			stepDTO.setFlowVersion(flowCase.getFlowVersion());
			if(cmd.getStepCount() == null) {
				cmd.setStepCount(flowCase.getStepCount());
			}
			stepDTO.setStepCount(cmd.getStepCount());
			processAutoStep(stepDTO);//fire next step
		}
		
		//TODO ignore the result ?
		return null;
	}
	
	private void updateFlowUserName(FlowUserSelectionDTO dto) {
		if(dto.getSelectionName() == null) {
			FlowUserSelectionType selType = FlowUserSelectionType.fromCode(dto.getSelectType());
			if(selType == FlowUserSelectionType.DEPARTMENT) {
				//Users selection
				UserInfo userInfo = userService.getUserSnapshotInfo(dto.getSourceIdA());
				dto.setSelectionName(userInfo.getNickName());
			}
		}
	}
	
	private void updateFlowUserName(FlowUserSelection dto) {
		if(dto.getSelectionName() == null) {
			FlowUserSelectionType selType = FlowUserSelectionType.fromCode(dto.getSelectType());
			if(selType == FlowUserSelectionType.DEPARTMENT) {
				//Users selection
				UserInfo userInfo = userService.getUserSnapshotInfo(dto.getSourceIdA());
				dto.setSelectionName(userInfo.getNickName());
			}
		}
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
				FlowUserSelectionDTO dto = ConvertHelper.convert(s, FlowUserSelectionDTO.class);
				updateFlowUserName(dto);
				resp.getSelections().add(dto);
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
			dto.setModuleName("testwuye");
			dto.setDisplayName("testwuye");
			return dto;
		}
		
//		
//		if(moduleId.equals(113l)) {
//			FlowModuleDTO dto = new FlowModuleDTO();
//			dto.setModuleId(113l);
//			dto.setModuleName("yueka");
//			dto.setDisplayName("月卡申请");
//			return dto;
//		}
//		
//		if(moduleId.equals(114l)) {
//			FlowModuleDTO dto = new FlowModuleDTO();
//			dto.setModuleId(114l);
//			dto.setModuleName("jiaoliu");
//			dto.setDisplayName("交流大厅");
//			return dto;
//		}
		
		ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(moduleId);
		if(serviceModule != null) {
			FlowModuleDTO dto = new FlowModuleDTO();
			dto.setDisplayName(serviceModule.getName());
			dto.setModuleName(serviceModule.getName());
			dto.setModuleId(moduleId);
			return dto;
		}
		 
		if(moduleId.equals(41500L)) {
			FlowModuleDTO dto = new FlowModuleDTO();
			dto.setModuleId(41500L);
			dto.setModuleName("车辆放行");
			dto.setDisplayName("车辆放行");
			return dto;
		}
 
		return null;
	}

	@Override
	public ListFlowModulesResponse listModules(ListFlowModulesCommand cmd) {
		ListFlowModulesResponse resp = new ListFlowModulesResponse();
		List<FlowModuleDTO> modules = new ArrayList<FlowModuleDTO>();
		resp.setModules(modules);
		
//		FlowModuleDTO dto = new FlowModuleDTO();
//		dto.setModuleId(111l);
//		dto.setModuleName("yuanqu");
//		dto.setDisplayName("园区入驻");
//		modules.add(dto);
//		
//		dto = new FlowModuleDTO();
//		dto.setModuleId(112l);
//		dto.setModuleName("wuye");
//		dto.setDisplayName("物业保修");
//		modules.add(dto);
//		
//		dto = new FlowModuleDTO();
//		dto.setModuleId(113l);
//		dto.setModuleName("yueka");
//		dto.setDisplayName("月卡申请");
//		modules.add(dto);
//		
//		dto = new FlowModuleDTO();
//		dto.setModuleId(114l);
//		dto.setModuleName("jiaoliu");
//		dto.setDisplayName("交流大厅");
//		modules.add(dto);
		
		flowListenerManager.getModules().forEach(m -> {
			FlowModuleDTO dto = new FlowModuleDTO();
			dto.setModuleId(m.getModuleId());
			dto.setModuleName(m.getModuleName());
			dto.setDisplayName(m.getModuleName());
			modules.add(dto);
		});
		
		return resp;
	}
	
	
	private List<Long> resolvUserSelections(FlowCaseState ctx, FlowEntityType entityType, Long entityId, List<FlowUserSelection> selections) {
		// Remove dup users
		List<Long> tmps = resolvUserSelections(ctx, entityType, entityId, selections, 1);
		List<Long> rlts = new ArrayList<>();
		Map<Long, Long> maps = new HashMap<Long, Long>();
		for(Long l : tmps) {
			if(!maps.containsKey(l)) {
				maps.put(l, 1l);	
				rlts.add(l);
			}
			
		}
		
		return rlts;
	}
	
	@Override
	public List<Long> resolvUserSelections(FlowCaseState ctx, FlowEntityType entityType, Long entityId, List<FlowUserSelection> selections, int loopCnt) {
		List<Long> users = new ArrayList<Long>();
		if(selections == null || loopCnt >= 5) {
			return users;
		}
		
		Flow flow = ctx.getFlowGraph().getFlow();
		Long orgId = flow.getOrganizationId();
		
		for(FlowUserSelection sel : selections) {
			if(FlowUserSourceType.SOURCE_USER.getCode().equals(sel.getSourceTypeA())) {
				users.add(sel.getSourceIdA());
			} else if(FlowUserSelectionType.POSITION.getCode().equals(sel.getSelectType())) {
				//sourceA is position, sourceB is department
				Long parentOrgId = orgId;
				if(sel.getOrganizationId() != null) {
					parentOrgId = sel.getOrganizationId();
				}
				Long departmentId = parentOrgId;
				if(sel.getSourceIdB() != null && FlowUserSourceType.SOURCE_DEPARTMENT.getCode().equals(sel.getSourceTypeB())) {
					departmentId = sel.getSourceIdB();
				}
//				LOGGER.error("position selId= " + sel.getId() + " positionId= " + sel.getSourceIdA() + " departmentId= " + departmentId);
				if(FlowUserSourceType.SOURCE_POSITION.getCode().equals(sel.getSourceTypeA())) {
					List<Long> tmp = flowUserSelectionService.findUsersByJobPositionId(parentOrgId, sel.getSourceIdA(), departmentId);
					if(tmp != null) {
						users.addAll(tmp);	
					}
					
				} else {
					LOGGER.error("resolvUser selId= " + sel.getId() + " position parse error!");
				}
				
			} else if(FlowUserSelectionType.MANAGER.getCode().equals(sel.getSelectType())) {
				Long parentOrgId = orgId;
				if(sel.getOrganizationId() != null) {
					parentOrgId = sel.getOrganizationId();
				}
				
				Long departmentId = parentOrgId;
				if(FlowUserSourceType.SOURCE_DEPARTMENT.getCode().equals(sel.getSourceTypeA())) {
					if(null != sel.getSourceIdA()) {
						departmentId = sel.getSourceIdA();	
					}
					
					List<Long> tmp = flowUserSelectionService.findManagersByDepartmentId(parentOrgId, departmentId);
					users.addAll(tmp);
				} else {
					LOGGER.error("resolvUser selId= " + sel.getId() + " manager parse error!");
				}
				
			} else if(FlowUserSelectionType.VARIABLE.getCode().equals(sel.getSelectType())) {
				if(sel.getSourceIdA() != null) {
					FlowVariable variable = flowVariableProvider.getFlowVariableById(sel.getSourceIdA());
//					variable.getScriptCls();
					FlowVariableUserResolver ftr = PlatformContext.getComponent(variable.getScriptCls());
		        	if(ftr != null) {
		        		List<Long> tmp = ftr.variableUserResolve(ctx, entityType, entityId, sel, loopCnt + 1);
		        		if(null != tmp) {
		        			users.addAll(tmp);
		        		}
		        	}
		        	
				} else {
					LOGGER.error("user params error selId= " + sel.getId() + " variable error!");
				}
				
			}
		}
		
		return users;
	}
	
	@Override
	public void createSnapshotNodeProcessors(FlowCaseState ctx, FlowGraphNode nextNode) {
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(nextNode.getFlowNode().getId()
				, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
		List<Long> users = resolvUserSelections(ctx, FlowEntityType.FLOW_NODE, null, selections);
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
	public void createSnapshotSupervisors(FlowCaseState ctx) {
		FlowCase flowCase = ctx.getFlowCase();
		List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(flowCase.getFlowMainId()
				, FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode(), flowCase.getFlowVersion());
		List<Long> users = resolvUserSelections(ctx, FlowEntityType.FLOW, null, selections);
		if(users.size() > 0) {
			for(Long selUser : users) {
				FlowEventLog log = new FlowEventLog();
				log.setId(flowEventLogProvider.getNextId());
				log.setFlowMainId(flowCase.getFlowMainId());
				log.setFlowVersion(flowCase.getFlowVersion());//get real version
				log.setNamespaceId(flowCase.getNamespaceId());
				
				log.setParentId(0l);
				log.setFlowCaseId(flowCase.getId());
				
				log.setFlowUserId(selUser);
				
				log.setLogType(FlowLogType.FLOW_SUPERVISOR.getCode());
				log.setLogTitle("");
				ctx.getLogs().add(log);
			}
		} else {
			LOGGER.warn(" create supervisors failed flowCaseId=" + flowCase.getId());
		}
	}
	
	@Override
	public void flushState(FlowCaseState ctx) throws FlowStepBusyException {
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		dbProvider.execute((s) -> {
			if(flowCaseProvider.updateIfValid(ctx.getFlowCase().getId(), ctx.getFlowCase().getLastStepTime(), now)) {
				ctx.getFlowCase().setLastStepTime(now);
				flowCaseProvider.updateFlowCase(ctx.getFlowCase());
				flowEventLogProvider.createFlowEventLogs(ctx.getLogs());	
			} else {
				throw new FlowStepBusyException("already step by others");
			}
			
			return true;
		});		
		
		//flush timeouts
		for(FlowTimeout ft : ctx.getTimeouts()) {
			flowTimeoutService.pushTimeout(ft);
		}
	}
	
	private List<String> getAllParams(String renderText) {
		List<String> params = new ArrayList<>();
		try {
			  Matcher m = pParam.matcher(renderText);
		        while(m.find()) {
		        	if(m.groupCount() > 0) {
		        		params.add(m.group(1));
		        	}
		        }	
		} catch(Exception ex) {
			//TODO log ?
		}
      
        return params;
	}
	
	private String resolveTextVariable(FlowCaseState ctx, String para) {
		FlowCase fc = ctx.getFlowCase();
        List<FlowVariable> vars = new ArrayList<>();
        List<FlowVariable> vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
        		, fc.getOwnerId(), fc.getOwnerType(), fc.getModuleId(), fc.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if(vars2 != null) {
        	vars.addAll(vars2);
        }
        vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
        		, 0l, null, fc.getModuleId(), fc.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if(vars2 != null) {
        	vars.addAll(vars2);
        }
        
        vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
        		, 0l, null, fc.getModuleId(), fc.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if(vars2 != null) {
        	vars.addAll(vars2);
        }
        
        vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
        		, 0l, null, 0l, null, para, FlowVariableType.TEXT.getCode());
        if(vars2 != null) {
        	vars.addAll(vars2);
        }
        
        if(!fc.getNamespaceId().equals(0)) {
        	vars2 = flowVariableProvider.findVariables(0
        		, 0l, null, 0l, null, para, FlowVariableType.TEXT.getCode());
        	if(vars2 != null) {
        		vars.addAll(vars2);
        		} 	
        }

        
        for(FlowVariable fv : vars) {
//			try {
//				Class clz = Class.forName(flowScript.getScriptCls());
//				runnableScript = (FlowScriptFire)PlatformContext.getComponent(clz);
//			} catch (ClassNotFoundException e) {
//				LOGGER.error("flow script class not found", e);
//			}	
        	FlowVariableTextResolver ftr = PlatformContext.getComponent(fv.getScriptCls());
        	if(ftr != null) {
        		String val = ftr.variableTextRender(ctx, para);
        		if(null != val) {
        			return val;
        		}
        	}
        	
        }
        
        return "error";
	}
	
	@Override
	public String parseActionTemplate(FlowCaseState ctx, Long actionId, String renderText) {
        String templateKey = String.format("action:%d", actionId);
        Map<String, String> model = new HashMap<String, String>();
        List<String> params = getAllParams(renderText);
        if(params == null || params.size() == 0) {
        	return renderText;
        }
        
        for(String para : params) {
        	String fv = resolveTextVariable(ctx, para);
        	if(fv != null) {
        		model.put(para, fv);	
        	}
        }
        
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
	
	@Override
	public String getFireButtonTemplate(FlowStepType step, Map<String, Object> map) {
        String scope = FlowTemplateCode.SCOPE;
        int code = 0;
        switch(step) {
        case APPROVE_STEP:
        	code = FlowTemplateCode.APPROVE_STEP;
        	break;
        case REJECT_STEP:
        	code = FlowTemplateCode.REJECT_STEP;
        	break;
        case ABSORT_STEP:
        	code = FlowTemplateCode.ABSORT_STEP;
        	break;
        case TRANSFER_STEP:
        	code = FlowTemplateCode.TRANSFER_STEP;
        	break;
        case COMMENT_STEP:
        	code = FlowTemplateCode.COMMENT_STEP;
        case EVALUATE_STEP:
        	code = FlowTemplateCode.EVALUATE_STEP;
        	break;
        default:
        	break;
        }
        
        if(code == 0) {
        	return "";
        }
        
        User user = UserContext.current().getUser();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if(user != null) {
        	locale = user.getLocale();
        }
        
        String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        return text;
	}

	@Override
	public void testFlowCase() {
	    Long moduleId = 111l;
	    Long orgId = 1000001l;
	    
    	Long applyUserId = UserContext.current().getUser().getId();
    	
    	String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = orgId;
		String ownerType = FlowOwnerType.ENTERPRISE.getCode();
    	Flow flow = this.getEnabledFlow(UserContext.getCurrentNamespaceId(), moduleId, moduleType, ownerId, ownerType);
    	
    	CreateFlowCaseCommand cmd = new CreateFlowCaseCommand();
    	cmd.setApplyUserId(applyUserId);
    	cmd.setFlowMainId(flow.getFlowMainId());
    	cmd.setFlowVersion(flow.getFlowVersion());
    	cmd.setReferId(0l);
    	cmd.setReferType("test-type");
    	
    	Random r = new Random();
    	cmd.setContent("test content" + String.valueOf(r.nextDouble()));
    	this.createFlowCase(cmd);
	}

	@Override
	public FlowGraphDetailDTO getFlowGraphDetail(Long flowId) {
		Flow flow = flowProvider.getFlowById(flowId);
		FlowGraphDetailDTO graphDetail = ConvertHelper.convert(flow, FlowGraphDetailDTO.class);
		
		List<FlowUserSelectionDTO> selections = new ArrayList<FlowUserSelectionDTO>();
		graphDetail.setSupervisors(selections);
		
		List<FlowUserSelection> seles = flowUserSelectionProvider.findSelectionByBelong(flowId
				, FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode());
		if(seles != null && seles.size() > 0) {
			seles.stream().forEach((sel) -> {
				selections.add(ConvertHelper.convert(sel, FlowUserSelectionDTO.class));
			});
		}
		
		List<FlowNodeDetailDTO> nodes = new ArrayList<>();
		graphDetail.setNodes(nodes);
		List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, FlowConstants.FLOW_CONFIG_VER);
		flowNodes.sort((n1, n2) -> {
			return n1.getNodeLevel().compareTo(n2.getNodeLevel());
		});
		
		for(FlowNode fn: flowNodes) {
			FlowNodeDetailDTO nodeDetail = this.getFlowNodeDetail(fn.getId());
			List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(fn.getId()
					, FlowConstants.FLOW_CONFIG_VER, FlowUserType.PROCESSOR.getCode());
			List<FlowButtonDetailDTO> btnDetails = new ArrayList<>();
			for(FlowButton btn : buttons) {
				btnDetails.add(this.getFlowButtonDetail(btn.getId()));	
			}
			
			nodeDetail.setProcessButtons(btnDetails);
			nodes.add(nodeDetail);
		}
		
		return graphDetail;
	}
	
	@Override
	public void deleteSnapshotProcessUser(Long flowId, Long userId) {
		Flow flow = flowProvider.getFlowById(flowId);
		if(!flow.getFlowMainId().equals(0l)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "Please use a config flowId");	
		}
		
		deleteAllNodeProcessors(flow, flow.getId(), 0, userId);
		
		Flow snapshotFlow = flowProvider.getSnapshotFlowById(flowId);
		if(snapshotFlow != null) {
			deleteAllNodeProcessors(snapshotFlow, snapshotFlow.getFlowMainId(), snapshotFlow.getFlowVersion(), userId);
		}
	}
	
	private void deleteAllNodeProcessors(Flow snapshotFlow, Long flowMainId,
			Integer ver, Long userId) {
		List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowMainId, ver);
		List<FlowUserSelection> objs = new ArrayList<>();
		for(FlowNode fn : nodes) {
			FlowUserSelection sel = flowUserSelectionProvider.findFlowNodeSelectionUser(fn.getId(), ver, userId);
			if(sel != null) {
				objs.add(sel);
			}
		}
		
		flowUserSelectionProvider.deleteFlowUserSelections(objs);
	}

	@Override
	public void addSnapshotProcessUser(Long flowId, Long userId) {
		Flow flow = flowProvider.getFlowById(flowId);
		if(!flow.getFlowMainId().equals(0l)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "Please use a config flowId");	
		}
		
		addAllNodeProcessors(flow, flow.getId(), 0, userId);
		
		Flow snapshotFlow = flowProvider.getSnapshotFlowById(flowId);
		if(snapshotFlow != null) {
			addAllNodeProcessors(snapshotFlow, snapshotFlow.getFlowMainId(), flow.getFlowVersion(), userId);
		}
	}
	
	private void addAllNodeProcessors(Flow flow, Long flowMainId, Integer ver, Long userId) {
		List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowMainId, ver);
		List<FlowUserSelection> objs = new ArrayList<>();
		for(FlowNode fn : nodes) {
			FlowUserSelection sel = flowUserSelectionProvider.findFlowNodeSelectionUser(fn.getId(), ver, userId);
			if(sel == null) {
				sel = new FlowUserSelection();
				sel.setBelongEntity(FlowEntityType.FLOW_NODE.getCode());
				sel.setBelongTo(fn.getId());
				sel.setBelongType(FlowUserType.PROCESSOR.getCode());
				sel.setFlowMainId(flowMainId);
				sel.setFlowVersion(ver);
				sel.setNamespaceId(fn.getNamespaceId());
				sel.setOrganizationId(flow.getOrganizationId());
				sel.setSelectType(FlowUserSelectionType.DEPARTMENT.getCode());
				sel.setSourceIdA(userId);
				sel.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
				sel.setStatus(FlowStatusType.VALID.getCode());
				updateFlowUserName(sel);
				
				objs.add(sel);
			}
		}
		
		flowUserSelectionProvider.createFlowUserSelections(objs);
	}
	
	private FlowAction createEvaluateAction(Flow flow, Integer flowVer, FlowActionInfo actionInfo
			, String actionType, String actionStepType, String flowStepType) {
		FlowAction action = flowActionProvider.findFlowActionByBelong(flow.getId(), FlowEntityType.FLOW_EVALUATE.getCode()
				, actionType, actionStepType, flowStepType);
		
		CreateFlowUserSelectionCommand selectionCmd = actionInfo.getUserSelections();
		boolean configUser = false;
		if(selectionCmd != null && selectionCmd.getSelections() != null && selectionCmd.getSelections().size() > 0) {
			configUser = true;
		}
		
		if(action == null) {
			action = new FlowAction();
			action.setFlowMainId(flow.getFlowMainId());
			action.setFlowVersion(flowVer);
			action.setActionStepType(actionStepType);
			action.setActionType(actionType);
			action.setBelongTo(flow.getId());
			action.setBelongEntity(FlowEntityType.FLOW_EVALUATE.getCode());
			action.setNamespaceId(flow.getNamespaceId());
			action.setFlowStepType(flowStepType);

			if(actionInfo.getReminderTickMinute() != null) {
				action.setReminderTickMinute(actionInfo.getReminderTickMinute());	
			}
			if(actionInfo.getReminderAfterMinute() != null) {
				action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());	
			}
			if(actionInfo.getTrackerApplier() != null) {
				action.setTrackerApplier(actionInfo.getTrackerApplier());	
			}
			if(actionInfo.getTrackerProcessor() != null) {
				action.setTrackerProcessor(actionInfo.getTrackerProcessor());	
			}
			
			action.setStatus(FlowActionStatus.ENABLED.getCode());
			action.setRenderText(actionInfo.getRenderText());
			flowActionProvider.createFlowAction(action);
			
		} else {
			if(actionInfo.getReminderTickMinute() != null) {
				action.setReminderTickMinute(actionInfo.getReminderTickMinute());	
			}
			if(actionInfo.getReminderAfterMinute() != null) {
				action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());	
			}
			if(actionInfo.getTrackerApplier() != null) {
				action.setTrackerApplier(actionInfo.getTrackerApplier());	
			}
			if(actionInfo.getTrackerProcessor() != null) {
				action.setTrackerProcessor(actionInfo.getTrackerProcessor());	
			}
			
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
				if(userSel.getOrganizationId() == null) {
					userSel.setOrganizationId(flow.getOrganizationId());	
				}
				createUserSelection(userSel, selCmd);
			}
		}
		
		return action;
	}

	@Override
	public FlowEvaluateDetailDTO updateFlowEvaluate(UpdateFlowEvaluateCommand cmd) {
		Flow flow = flowProvider.getFlowById(cmd.getFlowId());
		if(flow == null || !flow.getFlowMainId().equals(0l)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");	
		}
		
//		FlowNode node1 = flowNodeProvider.getFlowNodeById(cmd.getEvaluateStart());
//		FlowNode node2 = flowNodeProvider.getFlowNodeById(cmd.getEvaluateEnd());
		
		flow.setEvaluateStart(cmd.getEvaluateStart());
		flow.setEvaluateEnd(cmd.getEvaluateEnd());
		flow.setEvaluateStep(cmd.getEvaluateStep());
		flow.setEvaluateStep(cmd.getEvaluateStep());
		flow.setNeedEvaluate(cmd.getNeedEvaluate());
		
		this.dbProvider.execute(status -> {
			flowMarkUpdated(flow);
			
			if(cmd.getItems() != null && cmd.getItems().size() > 0) {
				List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
				if(items != null && items.size() > 0) {
					flowEvaluateItemProvider.deleteFlowEvaluateItem(items);
				}
				
				items = new ArrayList<FlowEvaluateItem>();
				for(String s : cmd.getItems()) {
					FlowEvaluateItem item = new FlowEvaluateItem();
					item.setFlowMainId(flow.getId());
					item.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
					item.setName(s);
					item.setNamespaceId(flow.getNamespaceId());
					items.add(item);
				}
				
				flowEvaluateItemProvider.createFlowEvaluateItem(items);
			}
			
			if(cmd.getMessageAction() != null) {
				createEvaluateAction(flow, FlowConstants.FLOW_CONFIG_VER, cmd.getMessageAction()
						, FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_NONE.getCode(), FlowStepType.EVALUATE_STEP.getCode());	
			}
			if(cmd.getSmsAction() != null) {
				createEvaluateAction(flow, FlowConstants.FLOW_CONFIG_VER, cmd.getSmsAction()
						, FlowActionType.SMS.getCode(), FlowActionStepType.STEP_NONE.getCode(), FlowStepType.EVALUATE_STEP.getCode());				
			}
			
			return null;
		});

		return getFlowEvaluate(flow);
	}
	
	private FlowEvaluateDetailDTO getFlowEvaluate(Flow flow) {
		FlowEvaluateDetailDTO dto = new FlowEvaluateDetailDTO();
		dto.setEvaluateEnd(flow.getEvaluateEnd());
		dto.setEvaluateStart(flow.getEvaluateStart());
		dto.setEvaluateStep(flow.getEvaluateStep());
		dto.setFlowId(flow.getId());
		dto.setNeedEvaluate(flow.getNeedEvaluate());
		dto.setItems(new ArrayList<String>());
	
		FlowAction action = flowActionProvider.findFlowActionByBelong(flow.getId(), FlowEntityType.FLOW_EVALUATE.getCode()
				, FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_NONE.getCode(), null);
		if(action != null) {
			dto.setMessageAction(actionToDTO(action));
		}
		
		action = flowActionProvider.findFlowActionByBelong(flow.getId(), FlowEntityType.FLOW_EVALUATE.getCode()
				, FlowActionType.SMS.getCode(), FlowActionStepType.STEP_NONE.getCode(), null);
		if(action != null) {
			dto.setMessageAction(actionToDTO(action));
		}
		
		List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
		if(items != null && items.size() > 0) {
			items.forEach(item -> {
				dto.getItems().add(item.getName());
			});
		}
		
		return dto;
	}

	@Override
	public FlowEvaluateDetailDTO getFlowEvaluate(Long flowId) {
		Flow flow = flowProvider.getFlowById(flowId);
		if(flow == null || !flow.getFlowMainId().equals(0l)) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");	
		}
		
		return getFlowEvaluate(flow);
	}

	@Override
	public FlowEvaluateDTO getEvaluateInfo(Long flowCaseId) {
		FlowEvaluateDTO dto = new FlowEvaluateDTO();
		dto.setFlowCaseId(flowCaseId);
		List<FlowEvaluateResultDTO> results = new ArrayList<FlowEvaluateResultDTO>();
		dto.setResults(results);
		
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
		
		dto.setNamespaceId(flowCase.getNamespaceId());
		
		List<FlowEvaluate> evas = flowEvaluateProvider.findEvaluates(flowCaseId, flowCase.getFlowMainId(), flowCase.getFlowVersion());
		Map<Long, FlowEvaluate> evaMap = new HashMap<Long, FlowEvaluate>();
		if(evas != null && evas.size() > 0) {
			evas.forEach(ev -> {
				evaMap.put(ev.getEvaluateItemId(), ev);
			});
		}
		
		List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		for(FlowEvaluateItem item: items) {
			FlowEvaluateResultDTO rltDTO = new FlowEvaluateResultDTO();
			rltDTO.setEvaluateItemId(item.getId());
			rltDTO.setName(item.getName());
			
			if(evaMap.containsKey(item.getId())) {
				rltDTO.setStar(evaMap.get(item.getId()).getStar());	
			}
			results.add(rltDTO);
		}
		
		dto.setHasResults((byte)0);
		if(items.size() != 0 && items.size() == evaMap.size()) {
			dto.setHasResults((byte)1);
		}
		
		return dto;
		
	}

	@Override
	public ListScriptsResponse listScripts(ListScriptsCommand cmd) {
		ListScriptsResponse resp = new ListScriptsResponse();
		List<FlowScriptDTO> scripts = new ArrayList<>();
		resp.setScripts(scripts);
		
		if(cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		
		FlowEntityType entityType = FlowEntityType.fromCode(cmd.getEntityType());
		if(entityType == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow params error");	
		}
		Flow flow = getFlowByEntity(cmd.getEntityId(), entityType);
		if(flow == null) {
			return resp;
		}
		
		List<FlowScript> scs = flowScriptProvider.findFlowScriptByModuleId(flow.getModuleId(), flow.getModuleType());
		if(scs != null && scs.size() > 0) {
			scs.forEach(s->{
				FlowScriptDTO dto = ConvertHelper.convert(s, FlowScriptDTO.class);
				scripts.add(dto);
			});
		}
		
		return resp;
	}

	@Override
	public FlowSMSTemplateResponse listSMSTemplates(ListSMSTemplateCommand cmd) {
		if(cmd.getNamespaceId() == null) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		
		FlowEntityType entityType = FlowEntityType.fromCode(cmd.getEntityType());
		if(entityType == null) {
			throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow params error");	
		}
		
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getAnchor());
		int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		FlowSMSTemplateResponse resp = new FlowSMSTemplateResponse();
		Flow flow = getFlowByEntity(cmd.getEntityId(), entityType);
		if(flow == null) {
			return resp;
		}
		
		String scope = "flow:" + String.valueOf(flow.getModuleId()) + "%";
		
      User user = UserContext.current().getUser();
      String locale = Locale.SIMPLIFIED_CHINESE.toString();
      if(user != null) {
        	locale = user.getLocale();
        }
        
      List<FlowSMSTemplateDTO> dtos = new ArrayList<FlowSMSTemplateDTO>();
      resp.setDtos(dtos);
      
		List<LocaleTemplate> templates = localeTemplateProvider.listLocaleTemplatesByScope(locator, cmd.getNamespaceId(), scope, locale, cmd.getKeyword(), count);
		resp.setNextPageAnchor(locator.getAnchor());
		if(templates != null) {
			templates.forEach(t -> {
				dtos.add(ConvertHelper.convert(t, FlowSMSTemplateDTO.class));
			});
		}
		
		return resp;
	}
	
	private void sendVerificationCodeSms(Integer namespaceId, String phoneNumber, String verificationCode){
	    List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_VCODE, verificationCode);
	    String templateScope = SmsTemplateCode.SCOPE;
	    int templateId = SmsTemplateCode.VERIFICATION_CODE;
	    String templateLocale = UserContext.current().getUser().getLocale();
	    smsProvider.sendSms(namespaceId, phoneNumber, templateScope, templateId, templateLocale, variables);
	}

	@Override
	public FlowResolveUsersResponse resolveSelectionUsers(Long flowId, Long selectionUserId) {
		FlowCaseState ctx = new FlowCaseState();
		FlowGraph graph = new FlowGraph();
		Flow flow = flowProvider.getFlowById(flowId);
		graph.setFlow(flow);
		ctx.setFlowGraph(graph);
		
		List<FlowUserSelection> sels = new ArrayList<>();
		FlowUserSelection sel = flowUserSelectionProvider.getFlowUserSelectionById(selectionUserId);
		sels.add(sel);
		
		List<Long> users = resolvUserSelections(ctx, null, null, sels);
		
		FlowResolveUsersResponse resp = new FlowResolveUsersResponse();
		List<UserInfo> infos = new ArrayList<>();
		resp.setUsers(infos);
		
		if(users != null && users.size() > 0) {
			users.forEach((u)-> {
				UserInfo ui = userService.getUserSnapshotInfoWithPhone(u);
				infos.add(ConvertHelper.convert(ui, UserInfo.class));
			});
		}
		
		return resp;
	}
	
}
