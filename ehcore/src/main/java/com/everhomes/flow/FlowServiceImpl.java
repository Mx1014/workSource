// @formatter:off
package com.everhomes.flow;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.action.FlowGraphMessageAction;
import com.everhomes.flow.action.FlowGraphSMSAction;
import com.everhomes.flow.action.FlowGraphScriptAction;
import com.everhomes.flow.action.FlowGraphTrackerAction;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.news.Attachment;
import com.everhomes.news.AttachmentProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.FlowCaseDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.news.NewsCommentContentType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhFlowAttachments;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.server.schema.tables.pojos.EhFlowNodes;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.*;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class FlowServiceImpl implements FlowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowServiceImpl.class);

    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private FlowProvider flowProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ConfigurationProvider configProvider;

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

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    private static final Pattern pParam = Pattern.compile("\\$\\{([^\\}]*)\\}");
    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    private StringTemplateLoader templateLoader;
    private Configuration templateConfig;

    private Map<String, FlowGraph> graphMap;

    public FlowServiceImpl() {
        graphMap = new ConcurrentHashMap<>();
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
        if (cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }
        if (cmd.getModuleType() == null) {
            cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
        }

        Flow flow = flowProvider.findFlowByName(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getModuleType(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getFlowName());
        if (flow != null) {
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
        obj.setProjectType(cmd.getProjectType() != null ? cmd.getProjectType() : EntityType.COMMUNITY.getCode());
        obj.setProjectId(cmd.getProjectId() != null ? cmd.getProjectId() : 0L);
        obj.setStringTag1(cmd.getStringTag1());
        flowListenerManager.onFlowCreating(obj);

        Flow resultObj = this.dbProvider.execute(new TransactionCallback<Flow>() {

            @Override
            public Flow doInTransaction(TransactionStatus arg0) {
                Flow execObj = flowProvider.findFlowByName(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getModuleType(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getFlowName());
                if (execObj != null) {
                    //already exists
                    return null;
                }

                flowProvider.createFlow(obj);
                if (obj.getId() > 0) {
                    return obj;
                }

                return null;
            }

        });

        if (resultObj == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NAME_EXISTS, "flow name exists");
        }

        return ConvertHelper.convert(resultObj, FlowDTO.class);
    }

    @Override
    public FlowDTO updateFlowName(UpdateFlowNameCommand cmd) {
        Flow oldFlow = flowProvider.getFlowById(cmd.getFlowId());
        if (oldFlow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");
        }

        Flow flow = flowProvider.findFlowByName(oldFlow.getNamespaceId(), oldFlow.getModuleId(), oldFlow.getModuleType(), oldFlow.getOwnerId(), oldFlow.getOwnerType(), cmd.getNewFlowName());
        if (flow != null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NAME_EXISTS, "flow name exists");
        }

        Flow resultObj = this.dbProvider.execute(new TransactionCallback<Flow>() {

            @Override
            public Flow doInTransaction(TransactionStatus arg0) {
                Flow execObj = flowProvider.findFlowByName(oldFlow.getNamespaceId(), oldFlow.getModuleId(), oldFlow.getModuleType(), oldFlow.getOwnerId(), oldFlow.getOwnerType(), cmd.getNewFlowName());
                if (execObj != null) {
                    //already exists
                    return null;
                }

                oldFlow.setFlowName(cmd.getNewFlowName());
                flowProvider.updateFlow(oldFlow);
                return oldFlow;
            }

        });

        if (resultObj == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NAME_EXISTS, "flow name exists");
        }

        return ConvertHelper.convert(resultObj, FlowDTO.class);
    }

    @Override
    public Flow getFlowByEntity(Long entityId, FlowEntityType entity) {
        return getFlowByEntity(entityId, entity, 1);
    }

    private Flow getFlowByEntity(Long entityId, FlowEntityType entity, int loop) {
        if (loop > 20) {
            LOGGER.error("getFlowEntity loop overlop");
            return null;
        }
        switch (entity) {
            case FLOW:
                return flowProvider.getFlowById(entityId);
            case FLOW_EVALUATE:
                return flowProvider.getFlowById(entityId);
            case FLOW_NODE:
                FlowNode flowNode = flowNodeProvider.getFlowNodeById(entityId);
                if (flowNode == null) {
                    return null;
                }
                if (flowNode.getFlowMainId().equals(0l)) {
                    return null;
                }
                return getFlowByEntity(flowNode.getFlowMainId(), FlowEntityType.FLOW, ++loop);
            case FLOW_BUTTON:
                FlowButton flowButton = flowButtonProvider.getFlowButtonById(entityId);
                if (flowButton == null) {
                    return null;
                }
                if (flowButton.getFlowMainId().equals(0l)) {
                    Flow flow = getFlowByEntity(flowButton.getFlowNodeId(), FlowEntityType.FLOW_NODE, ++loop);
                    if (flow != null) {
                        flowButton.setFlowMainId(flow.getTopId());
                        flowButtonProvider.updateFlowButton(flowButton);
                    }

                    return flow;
                } else {
                    return getFlowByEntity(flowButton.getFlowMainId(), FlowEntityType.FLOW, ++loop);
                }

            case FLOW_ACTION:
                FlowAction flowAction = flowActionProvider.getFlowActionById(entityId);
                if (flowAction == null) {
                    return null;
                }

                if (flowAction.getFlowMainId().equals(0l)) {
                    Flow flow = getFlowByEntity(flowAction.getBelongTo(), FlowEntityType.fromCode(flowAction.getBelongEntity()), ++loop);
                    if (flow != null) {
                        flowAction.setFlowMainId(flow.getTopId());
                        flowActionProvider.updateFlowAction(flowAction);
                    }
                    return flow;
                } else {
                    return getFlowByEntity(flowAction.getFlowMainId(), FlowEntityType.FLOW, ++loop);
                }

            case FLOW_SELECTION:
                FlowUserSelection flowSel = flowUserSelectionProvider.getFlowUserSelectionById(entityId);
                if (flowSel == null) {
                    return null;
                }
                if (flowSel.getFlowMainId().equals(0l)) {
                    Flow flow = getFlowByEntity(flowSel.getBelongTo(), FlowEntityType.fromCode(flowSel.getBelongEntity()), ++loop);
                    if (flow != null) {
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
        if (cmd == null) {//TODO need this ?
            return null;
        }

        if (cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }

        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        cmd.setPageSize(count);
        ListFlowBriefResponse resp = new ListFlowBriefResponse();
        List<FlowDTO> dtos = new ArrayList<FlowDTO>();
        resp.setFlows(dtos);

        ListingLocator locator = new ListingLocator();
        List<Flow> flows = flowProvider.findFlowsByModule(locator, cmd);
        for (Flow flow : flows) {
            dtos.add(ConvertHelper.convert(flow, FlowDTO.class));
        }

        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }

    @Override
    public FlowNodeDTO createFlowNode(CreateFlowNodeCommand cmd) {
        Flow flow = flowProvider.getFlowById(cmd.getFlowMainId());
        if (flow == null || flow.getStatus().equals(FlowStatusType.INVALID.getCode()) || !flow.getFlowMainId().equals(0l)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");
        }

        FlowNode flowNode = this.dbProvider.execute(new TransactionCallback<FlowNode>() {
            @Override
            public FlowNode doInTransaction(TransactionStatus arg0) {
                FlowNode nodeObj = flowNodeProvider.findFlowNodeByName(flow.getId(), flow.getFlowVersion(), cmd.getNodeName());
                if (nodeObj != null) {
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
        if (flow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");
        }

        flowProvider.flowMarkUpdated(flow);
    }

    private void createDefaultButtons(Flow flow, FlowNode flowNode) {
        FlowStepType[] steps = {FlowStepType.APPROVE_STEP, FlowStepType.REJECT_STEP,
                FlowStepType.TRANSFER_STEP, FlowStepType.COMMENT_STEP, FlowStepType.ABSORT_STEP};
        for (FlowStepType step : steps) {
            createDefButton(flow, flowNode, FlowUserType.PROCESSOR, step, FlowButtonStatus.ENABLED);
        }

        FlowStepType[] steps2 = {FlowStepType.REMINDER_STEP, FlowStepType.COMMENT_STEP, FlowStepType.ABSORT_STEP};
        for (FlowStepType step : steps2) {
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
        button.setNeedSubject((byte) 1);
        button.setFlowUserType(userType.getCode());
        button.setButtonName(buttonDefName(flow.getNamespaceId(), stepType));
        button.setSubjectRequiredFlag(TrueOrFalseFlag.FALSE.getCode());
        if (stepType == FlowStepType.REMINDER_STEP) {
            button.setRemindCount(1);
        }
        if (stepType == FlowStepType.TRANSFER_STEP) {
            button.setNeedProcessor((byte) 1);
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
        if (flow != null) {
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
        flowNodes.sort(Comparator.comparing(EhFlowNodes::getNodeLevel));

        ListBriefFlowNodeResponse resp = new ListBriefFlowNodeResponse();
        List<FlowNodeDTO> dtos = new ArrayList<>();
        resp.setFlowNodes(dtos);
        for (FlowNode fn : flowNodes) {
            FlowNodeDTO dto = ConvertHelper.convert(fn, FlowNodeDTO.class);
            List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(
                    fn.getId(), FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
            dto.setProcessors(new ArrayList<>());
            if (selections != null) {
                selections.forEach((s) -> {
                    dto.getProcessors().add(ConvertHelper.convert(s, FlowUserSelectionDTO.class));
                });
            }

            dtos.add(dto);
        }

        return resp;
    }

    @Override
    public ListBriefFlowNodeResponse updateNodePriority(
            UpdateFlowNodePriorityCommand cmd) {
        Flow flow = flowProvider.getFlowById(cmd.getFlowMainId());
        if (flow == null || flow.getStatus().equals(FlowStatusType.INVALID.getCode()) || !flow.getFlowMainId().equals(0l)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");
        }

        if (cmd.getFlowNodes() == null) {
            return listBriefFlowNodes(cmd.getFlowMainId());
        }

        flowMarkUpdated(flow);

        List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(cmd.getFlowMainId(), FlowConstants.FLOW_CONFIG_VER);

        this.dbProvider.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus arg0) {
                for (FlowNodePriority pr : cmd.getFlowNodes()) {
                    for (FlowNode fn : flowNodes) {
                        if (fn.getId().equals(pr.getId())) {
                            if (pr.getNodeLevel() != null) {
                                fn.setNodeLevel(pr.getNodeLevel());
                            }
                            if (pr.getNodeName() != null) {
                                fn.setNodeName(pr.getNodeName());
                            }
                            if (pr.getDescription() != null) {
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

        flowNodes.sort(Comparator.comparing(EhFlowNodes::getNodeLevel));

        ListBriefFlowNodeResponse resp = new ListBriefFlowNodeResponse();
        List<FlowNodeDTO> dtos = new ArrayList<>();
        resp.setFlowNodes(dtos);
        for (FlowNode fn : flowNodes) {
            dtos.add(ConvertHelper.convert(fn, FlowNodeDTO.class));
        }

        return resp;
    }

    @Override
    public ListFlowButtonResponse listFlowNodeButtons(Long flowNodeId) {
        ListFlowButtonResponse resp = new ListFlowButtonResponse();

        List<FlowButton> applierButtonList = flowButtonProvider.findFlowButtonsByUserType(flowNodeId,
                FlowConstants.FLOW_CONFIG_VER, FlowUserType.APPLIER.getCode());

        List<FlowButtonDTO> applierBtn = applierButtonList.stream()
                .filter(r -> !r.getFlowStepType().equals(FlowStepType.EVALUATE_STEP.getCode()))
                .map(r -> ConvertHelper.convert(r, FlowButtonDTO.class))
                .collect(Collectors.toList());

        List<FlowButton> processorButtonList = flowButtonProvider.findFlowButtonsByUserType(flowNodeId,
                FlowConstants.FLOW_CONFIG_VER, FlowUserType.PROCESSOR.getCode());

        List<FlowButtonDTO> processorBtn = processorButtonList.stream()
                .map(r -> ConvertHelper.convert(r, FlowButtonDTO.class))
                .collect(Collectors.toList());

        resp.setApplierButtons(applierBtn);
        resp.setProcessorButtons(processorBtn);

        return resp;
    }

    @Override
    public FlowNodeDetailDTO getFlowNodeDetail(Long flowNodeId) {
        FlowNode flowNode = flowNodeProvider.getFlowNodeById(flowNodeId);
        FlowNodeDetailDTO detail = ConvertHelper.convert(flowNode, FlowNodeDetailDTO.class);
        List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(flowNodeId,
                FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
        if (selections != null) {
            List<FlowUserSelectionDTO> processorList = selections.parallelStream()
                    .map(r -> ConvertHelper.convert(r, FlowUserSelectionDTO.class))
                    .collect(Collectors.toList());
            detail.setProcessors(processorList);
        }

        detail.setReminder(getReminderDTO(flowNodeId));
        detail.setTracker(getTrackerDTO(flowNodeId));
        return detail;
    }

    private FlowNodeReminderDTO getReminderDTO(Long flowNodeId) {
        FlowNodeReminderDTO dto = new FlowNodeReminderDTO();
        FlowAction action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
        if (action != null) {
            if (action.getStatus().equals(FlowActionStatus.ENABLED.getCode())) {
                dto.setReminderMessageEnabled((byte) 1);
            }

            dto.setMessageAction(actionToDTO(action));
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.SMS.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
        if (action != null) {
            if (action.getStatus().equals(FlowActionStatus.ENABLED.getCode())) {
                dto.setReminderSMSEnabled((byte) 1);
            }

            dto.setSmsAction(actionToDTO(action));
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TICK_MESSAGE.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
        if (action != null) {
            if (action.getStatus().equals(FlowActionStatus.ENABLED.getCode())) {
                dto.setReminderTickMsgEnabled((byte) 1);
            }

            dto.setTickMessageAction(actionToDTO(action));
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TICK_SMS.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
        if (action != null) {
            if (action.getStatus().equals(FlowActionStatus.ENABLED.getCode())) {
                dto.setReminderTickSMSEnabled((byte) 1);
            }

            dto.setTickSMSAction(actionToDTO(action));
        }

        return dto;
    }

    private FlowNodeTrackerDTO getTrackerDTO(Long flowNodeId) {
        FlowNodeTrackerDTO dto = new FlowNodeTrackerDTO();
        FlowAction action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.APPROVE_STEP.getCode());
        if (action != null) {
            dto.setEnterTracker(ConvertHelper.convert(action, FlowActionDTO.class));
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.REJECT_STEP.getCode());
        if (action != null) {
            dto.setRejectTracker(ConvertHelper.convert(action, FlowActionDTO.class));
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_LEAVE.getCode(), FlowStepType.TRANSFER_STEP.getCode());
        if (action != null) {
            dto.setTransferTracker(ConvertHelper.convert(action, FlowActionDTO.class));
        }

        return dto;
    }

    private FlowActionDTO actionToDTO(FlowAction action) {
        FlowActionDTO actionDTO = ConvertHelper.convert(action, FlowActionDTO.class);
        if (actionDTO.getStatus() != null && actionDTO.getStatus().equals(FlowActionStatus.ENABLED.getCode())) {
            actionDTO.setEnabled((byte) 1);
        }

        List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(action.getId(),
                FlowEntityType.FLOW_ACTION.getCode(), FlowUserType.PROCESSOR.getCode());
        if (selections != null) {
            List<FlowUserSelectionDTO> userSelectionDTOList = selections.stream()
                    .map(r -> ConvertHelper.convert(r, FlowUserSelectionDTO.class)).collect(Collectors.toList());
            actionDTO.setProcessors(userSelectionDTOList);
        }
        return actionDTO;
    }

    @Override
    public FlowNodeDetailDTO updateFlowNodeReminder(UpdateFlowNodeReminderCommand cmd) {
        FlowNode flowNode = flowNodeProvider.getFlowNodeById(cmd.getFlowNodeId());
        Flow flow = getFlowByEntity(cmd.getFlowNodeId(), FlowEntityType.FLOW_NODE);
        flowMarkUpdated(flow);

        if (cmd.getMessageAction() != null) {
            dbProvider.execute(r -> {
                return createNodeAction(flowNode, cmd.getMessageAction(), FlowActionType.MESSAGE.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), null);
            });
        }

        if (cmd.getSmsAction() != null) {
            dbProvider.execute((a) -> {
                return createNodeAction(flowNode, cmd.getSmsAction(), FlowActionType.SMS.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), null);
            });
        }

        if (cmd.getTickMessageAction() != null) {
            //超过 x 小时未处理则推送消息
//			if(cmd.getMessageAction().getReminderAfterMinute() != null && cmd.getMessageAction().getReminderAfterMinute() > 0) {
//			}
            dbProvider.execute((a) -> {
                return createNodeAction(flowNode, cmd.getTickMessageAction(), FlowActionType.TICK_MESSAGE.getCode()
                        , FlowActionStepType.STEP_TIMEOUT.getCode(), null);
            });
        }

        if (cmd.getTickSMSAction() != null) {
            dbProvider.execute((a) -> {
                return createNodeAction(flowNode, cmd.getTickSMSAction(), FlowActionType.TICK_SMS.getCode()
                        , FlowActionStepType.STEP_TIMEOUT.getCode(), null);
            });
        }

        return getFlowNodeDetail(cmd.getFlowNodeId());
    }

    private FlowAction createNodeAction(FlowNode flowNode, FlowActionInfo actionInfo,
                                        String actionType, String actionStepType, String flowStepType) {

        FlowAction action = flowActionProvider.findFlowActionByBelong(
                flowNode.getId(), FlowEntityType.FLOW_NODE.getCode(), actionType, actionStepType, flowStepType);

        CreateFlowUserSelectionCommand selectionCmd = actionInfo.getUserSelections();

        if (action == null) {
            action = new FlowAction();
            action.setFlowMainId(flowNode.getFlowMainId());
            action.setFlowVersion(flowNode.getFlowVersion());
            action.setActionStepType(actionStepType);
            action.setActionType(actionType);
            action.setBelongTo(flowNode.getId());
            action.setBelongEntity(FlowEntityType.FLOW_NODE.getCode());
            action.setNamespaceId(flowNode.getNamespaceId());
            action.setFlowStepType(flowStepType);

            if (actionInfo.getReminderTickMinute() != null) {
                action.setReminderTickMinute(actionInfo.getReminderTickMinute());
            }
            if (actionInfo.getReminderAfterMinute() != null) {
                action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
            }
            if (actionInfo.getTrackerApplier() != null) {
                action.setTrackerApplier(actionInfo.getTrackerApplier());
            }
            if (actionInfo.getTrackerProcessor() != null) {
                action.setTrackerProcessor(actionInfo.getTrackerProcessor());
            }

            if (actionInfo.getEnabled() == null || actionInfo.getEnabled() > 0) {
                action.setStatus(FlowActionStatus.ENABLED.getCode());
            } else {
                action.setStatus(FlowActionStatus.DISABLED.getCode());
            }
            action.setTemplateId(actionInfo.getTemplateId());

            action.setRenderText(actionInfo.getRenderText());
            flowActionProvider.createFlowAction(action);

        } else {
            if (actionInfo.getEnabled() == null || actionInfo.getEnabled() > 0) {
                action.setStatus(FlowActionStatus.ENABLED.getCode());
            } else {
                action.setStatus(FlowActionStatus.DISABLED.getCode());
            }

            if (actionInfo.getReminderTickMinute() != null) {
                action.setReminderTickMinute(actionInfo.getReminderTickMinute());
            }
            if (actionInfo.getReminderAfterMinute() != null) {
                action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
            }
            if (actionInfo.getTrackerApplier() != null) {
                action.setTrackerApplier(actionInfo.getTrackerApplier());
            }
            if (actionInfo.getTrackerProcessor() != null) {
                action.setTrackerProcessor(actionInfo.getTrackerProcessor());
            }
            if (actionInfo.getTemplateId() != null) {
                action.setTemplateId(actionInfo.getTemplateId());
            }
            if (actionInfo.getRenderText() != null) {
                action.setRenderText(actionInfo.getRenderText());
            }
            flowActionProvider.updateFlowAction(action);

            flowUserSelectionProvider.deleteSelectionByBelong(action.getId(), FlowEntityType.FLOW_ACTION.getCode(), FlowUserType.PROCESSOR.getCode());
        }

        if (selectionCmd != null && selectionCmd.getSelections() != null && selectionCmd.getSelections().size() > 0) {
            List<FlowSingleUserSelectionCommand> seles = selectionCmd.getSelections();
            for (FlowSingleUserSelectionCommand selCmd : seles) {
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

        if (cmd.getEnterTracker() != null) {
            dbProvider.execute((a) -> {
                return createNodeAction(flowNode, cmd.getEnterTracker(), FlowActionType.TRACK.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.APPROVE_STEP.getCode());
            });
        }

        if (cmd.getRejectTracker() != null) {
            dbProvider.execute((a) -> {
                return createNodeAction(flowNode, cmd.getRejectTracker(), FlowActionType.TRACK.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.REJECT_STEP.getCode());
            });
        }

        if (cmd.getTransferTracker() != null) {
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
        // flowNode.setAllowApplierUpdate(cmd.getAllowApplierUpdate());
        flowNode.setAllowApplierUpdate(TrueOrFalseFlag.FALSE.getCode());
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
        List<FlowUserSelectionDTO> selections = new ArrayList<>();
        resp.setSelections(selections);

        List<FlowSingleUserSelectionCommand> cmds = cmd.getSelections();
        if (cmds != null && cmds.size() > 0) {
            for (FlowSingleUserSelectionCommand sCmd : cmds) {
                FlowUserSelection sel = ConvertHelper.convert(sCmd, FlowUserSelection.class);
                sel.setBelongEntity(cmd.getFlowEntityType());
                sel.setBelongTo(cmd.getBelongTo());
                sel.setBelongType(cmd.getFlowUserType());
                sel.setFlowMainId(flow.getTopId());
                sel.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
                sel.setSelectType(sCmd.getFlowUserSelectionType());
                sel.setStatus(FlowStatusType.VALID.getCode());
                sel.setNamespaceId(UserContext.getCurrentNamespaceId());
                if (sel.getOrganizationId() == null) {
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
        List<FlowUserSelectionDTO> selections = new ArrayList<>();
        resp.setSelections(selections);
        List<FlowUserSelection> seles = flowUserSelectionProvider.findSelectionByBelong(
                cmd.getBelongTo(), cmd.getFlowEntityType(), cmd.getFlowUserType(), 0);
        if (seles != null && seles.size() > 0) {
            seles.forEach((sel) -> {
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
        if (sel != null) {
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
        if (cmd.getButtonName() != null) {
            flowButton.setButtonName(cmd.getButtonName());
        }
        flowButton.setDescription(cmd.getDescription());
        if (cmd.getGotoNodeId() != null) {
            flowButton.setGotoNodeId(cmd.getGotoNodeId());
        }
        if (cmd.getNeedSubject() != null) {
            flowButton.setNeedSubject(cmd.getNeedSubject());
            // 节点信息必填标志
            if (cmd.getSubjectRequiredFlag() != null) {
                flowButton.setSubjectRequiredFlag(cmd.getSubjectRequiredFlag());
            } else {
                flowButton.setSubjectRequiredFlag(TrueOrFalseFlag.FALSE.getCode());
            }
        }
        if (cmd.getNeedProcessor() != null) {
            flowButton.setNeedProcessor(cmd.getNeedProcessor());
        }
        if (cmd.getRemindCount() != null) {
            flowButton.setRemindCount(cmd.getRemindCount());
        }

        flowButtonProvider.updateFlowButton(flowButton);

        if (null != cmd.getMessageAction()) {
            dbProvider.execute((a) -> {
                return createButtonAction(flowButton, cmd.getMessageAction(), FlowActionType.MESSAGE.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
            });
        }

        if (null != cmd.getSmsAction()) {
            dbProvider.execute((a) -> {
                return createButtonAction(flowButton, cmd.getSmsAction(), FlowActionType.SMS.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
            });
        }

        if (null != cmd.getEnterScriptIds()) {
            for (Long scriptId : cmd.getEnterScriptIds()) {
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
        if (flowButton != null) {
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
        if (flowButton != null) {
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

        if (action == null) {
            action = new FlowAction();
            action.setFlowMainId(flowButton.getFlowMainId());
            action.setFlowVersion(flowButton.getFlowVersion());
            action.setActionStepType(actionStepType);
            action.setActionType(actionType);
            action.setBelongTo(flowButton.getId());
            action.setBelongEntity(FlowEntityType.FLOW_BUTTON.getCode());
            action.setNamespaceId(flowButton.getNamespaceId());
            action.setFlowStepType(flowStepType);
            if (actionInfo.getTemplateId() != null) {
                action.setTemplateId(actionInfo.getTemplateId());
            }

            action.setReminderTickMinute(actionInfo.getReminderTickMinute());
            action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
            action.setTrackerApplier(actionInfo.getTrackerApplier());
            action.setTrackerProcessor(actionInfo.getTrackerProcessor());
            action.setStatus(FlowActionStatus.ENABLED.getCode());
            action.setRenderText(actionInfo.getRenderText());
            flowActionProvider.createFlowAction(action);

        } else {
            if (actionInfo.getTemplateId() != null) {
                action.setTemplateId(actionInfo.getTemplateId());
            }
            action.setReminderTickMinute(actionInfo.getReminderTickMinute());
            action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
            action.setTrackerApplier(actionInfo.getTrackerApplier());
            action.setTrackerProcessor(actionInfo.getTrackerProcessor());
            action.setStatus(FlowActionStatus.ENABLED.getCode());
            action.setRenderText(actionInfo.getRenderText());
            flowActionProvider.updateFlowAction(action);

            //delete all old selections
            flowUserSelectionProvider.deleteSelectionByBelong(action.getId(), FlowEntityType.FLOW_ACTION.getCode(), FlowUserType.PROCESSOR.getCode());
        }

        if (selectionCmd != null && selectionCmd.getSelections() != null && selectionCmd.getSelections().size() > 0) {
            List<FlowSingleUserSelectionCommand> seles = selectionCmd.getSelections();
            for (FlowSingleUserSelectionCommand selCmd : seles) {
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
        if (action != null) {
            dto.setPushMessage(actionToDTO(action));
        }

        action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.SMS.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        if (action != null) {
            dto.setPushSms(actionToDTO(action));
        }

        List<FlowAction> flowActions = flowActionProvider.findFlowActionsByBelong(flowButtonId, FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.ENTER_SCRIPT.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
        List<FlowActionDTO> actionDTOS = new ArrayList<FlowActionDTO>();
        if (flowActions != null) {
            flowActions.forEach((fa) -> {
                actionDTOS.add(ConvertHelper.convert(fa, FlowActionDTO.class));
            });

            dto.setEnterScripts(actionDTOS);
        }

        return dto;
    }

    private void updateFlowVersion(Flow flow) {//TODO better for version increment
        Flow snapshotFlow = flowProvider.getSnapshotFlowById(flow.getId());

        String key = String.format("flow:%d", flow.getId());
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);

        if (snapshotFlow != null) {
            clearSnapshotGraph(snapshotFlow);
            if (snapshotFlow.getFlowVersion() > flow.getFlowVersion()) {
                flow.setFlowVersion(snapshotFlow.getFlowVersion() + 1);
            }
        }

        Long ver = redisTemplate.opsForValue().increment(key, 1);
        if (ver == null || ver.intValue() < flow.getFlowVersion()) {
            redisTemplate.opsForValue().set(key, String.valueOf(flow.getFlowVersion()));
        } else {
            flow.setFlowVersion(ver.intValue());
        }
    }

    @Override
    public Boolean enableFlow(Long flowId) {
        final FlowGraph flowGraph = new FlowGraph();
        Flow flow = flowProvider.getFlowById(flowId);

        Flow enabledFlow = flowProvider.getEnabledConfigFlow(flow.getNamespaceId(), flow.getModuleId(), flow.getModuleType(), flow.getOwnerId(), flow.getOwnerType());
        if (enabledFlow != null && !enabledFlow.getId().equals(flowId)
                && enabledFlow.getStatus().equals(FlowStatusType.RUNNING.getCode())) {
            enabledFlow.setStatus(FlowStatusType.STOP.getCode());
            flowProvider.updateFlow(enabledFlow);
        }

        if (flow.getStatus().equals(FlowStatusType.STOP.getCode())) {
            //restart it
            flow.setStatus(FlowStatusType.RUNNING.getCode());
            Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
            flow.setUpdateTime(now);
            flow.setRunTime(now);
            flowProvider.updateFlow(flow);
            return true;
        } else if (flow.getStatus().equals(FlowStatusType.RUNNING.getCode())) {
            //already running
            Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
            flow.setUpdateTime(now);
            flow.setRunTime(now);
            flowProvider.updateFlow(flow);
            return true;
        }


        updateFlowVersion(flow);

        List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, FlowConstants.FLOW_CONFIG_VER);
        flowNodes.sort((n1, n2) -> {
            return n1.getNodeLevel().compareTo(n2.getNodeLevel());
        });

        int i = 1;
        for (FlowNode fn : flowNodes) {
            if (!fn.getNodeLevel().equals(i)) {
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

        flowNodes.forEach((fn) -> {
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
            dbProvider.execute((s) -> {
                doSnapshot(flowGraph);
                return true;
            });

        } catch (Exception ex) {
            isOk = false;
            LOGGER.error("do snapshot error", ex);
        }

        if (flow.getFlowMainId().equals(0l)) {
            isOk = false;
        }

        if (isOk) {
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
        if (action != null) {
            graphAction = new FlowGraphMessageAction();
            graphAction.setFlowAction(action);
            graphNode.setMessageAction(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.SMS.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
        if (action != null) {
            graphAction = new FlowGraphSMSAction(action.getReminderAfterMinute(), action.getReminderTickMinute(), 1l);
            graphAction.setFlowAction(action);
            graphNode.setSmsAction(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TICK_MESSAGE.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
        if (action != null) {
            graphAction = new FlowGraphMessageAction(action.getReminderAfterMinute(), action.getReminderTickMinute(), 50l);
            graphAction.setFlowAction(action);
            graphNode.setTickMessageAction(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TICK_SMS.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
        if (action != null) {
            graphAction = new FlowGraphSMSAction(action.getReminderAfterMinute(), action.getReminderTickMinute(), 1l);
            graphAction.setFlowAction(action);
            graphNode.setTickSMSAction(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.APPROVE_STEP.getCode());
        if (action != null) {
            graphAction = new FlowGraphTrackerAction(FlowStepType.APPROVE_STEP);
            graphAction.setFlowAction(action);
            graphNode.setTrackApproveEnter(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.REJECT_STEP.getCode());
        if (action != null) {
            graphAction = new FlowGraphTrackerAction(FlowStepType.REJECT_STEP);
            graphAction.setFlowAction(action);
            graphNode.setTrackRejectEnter(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_LEAVE.getCode(), FlowStepType.TRANSFER_STEP.getCode());
        if (action != null) {
            graphAction = new FlowGraphTrackerAction(FlowStepType.TRANSFER_STEP);
            graphAction.setFlowAction(action);
            graphNode.setTrackTransferLeave(graphAction);
        }

        List<FlowButton> applierButtons = flowButtonProvider.findFlowButtonsByUserType(flowNodeId, flowVersion, FlowUserType.APPLIER.getCode());
        applierButtons.forEach((btn) -> {
            graphNode.getApplierButtons().add(getFlowGraphButton(btn));
        });

        List<FlowButton> processorButtons = flowButtonProvider.findFlowButtonsByUserType(flowNodeId, flowVersion, FlowUserType.PROCESSOR.getCode());
        processorButtons.forEach((btn) -> {
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
        if (action != null) {
            graphAction = new FlowGraphMessageAction();
            graphAction.setFlowAction(action);
            graphBtn.setMessage(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.SMS.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        if (action != null) {
            graphAction = new FlowGraphSMSAction();
            graphAction.setFlowAction(action);
            graphBtn.setSms(graphAction);
        }

        List<FlowAction> flowActions = flowActionProvider.findFlowActionsByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.ENTER_SCRIPT.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);
        if (flowActions != null && flowActions.size() > 0) {
            for (FlowAction fa : flowActions) {
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
        for (FlowGraphNode node : flowGraph.getNodes()) {
            FlowNode flowNode = node.getFlowNode();
            Long oldFlowNodeId = flowNode.getId();
            flowNode.setId(null);
            flowNode.setFlowMainId(flow.getFlowMainId());
            flowNode.setFlowVersion(flow.getFlowVersion());
            flowNodeProvider.createFlowNode(flowNode);
            if (oldFlowNodeId == null) {// start or end flowNode
                continue;
            }

            //step3 create flowNode's buttons
            for (FlowGraphButton button : node.getApplierButtons()) {
                doSnapshotButton(flow, flowNode, button);
            }
            for (FlowGraphButton button : node.getProcessorButtons()) {
                doSnapshotButton(flow, flowNode, button);
            }

            //step6 copy flowNode's processors
            List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(oldFlowNodeId
                    , FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
            if (selections != null && selections.size() > 0) {
                for (FlowUserSelection sel : selections) {
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
        if (selections != null && selections.size() > 0) {
            for (FlowUserSelection sel : selections) {
                sel.setBelongTo(flow.getFlowMainId());
                sel.setFlowMainId(flow.getFlowMainId());
                sel.setFlowVersion(flow.getFlowVersion());
                flowUserSelectionProvider.createFlowUserSelection(sel);
            }
        }

        //step9 copy flow's evaluate
        List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flow.getFlowMainId(), FlowConstants.FLOW_CONFIG_VER);
        if (items != null && items.size() > 0) {
            items.forEach(item -> {
                item.setId(null);
                item.setFlowMainId(flow.getFlowMainId());
                item.setFlowVersion(flow.getFlowVersion());
            });
            flowEvaluateItemProvider.createFlowEvaluateItem(items);

            //TODO support build for evaluate
        }

        flow.setStartNode(flowGraph.getNodes().get(0).getFlowNode().getId());
        flow.setEndNode(flowGraph.getNodes().get(flowGraph.getNodes().size() - 1).getFlowNode().getId());
        flowProvider.updateFlow(flow);
    }

    private void doSnapshotButton(Flow flow, FlowNode flowNode, FlowGraphButton button) {
        FlowButton flowButton = button.getFlowButton();
//		Long oldFlowButtonId = flowButton.getId();

        flowButton.setId(null);
        flowButton.setFlowMainId(flow.getFlowMainId());
        flowButton.setFlowVersion(flow.getFlowVersion());
        flowButton.setFlowNodeId(flowNode.getId());
        if (!flowButton.getGotoNodeId().equals(0L)) {
            //把 gotoNodeId 变成相对值 gotoLevel
            FlowNode fn = flowNodeProvider.getFlowNodeById(flowButton.getGotoNodeId());
            if (fn != null) {
                flowButton.setGotoLevel(fn.getNodeLevel());
            }
        }
        flowButtonProvider.createFlowButton(flowButton);

        //step4 create flowButton's actions
        doSnapshotAction(flow, flowButton.getId(), button.getMessage());
        doSnapshotAction(flow, flowButton.getId(), button.getSms());
        if (null != button.getScripts()) {
            for (FlowGraphAction action : button.getScripts()) {
                doSnapshotAction(flow, flowButton.getId(), action);
            }
        }
    }

    private void doSnapshotAction(Flow flow, Long belongTo, FlowGraphAction action) {
        if (action == null) {
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
        if (selections != null && selections.size() > 0) {
            for (FlowUserSelection sel : selections) {
                sel.setBelongTo(flowAction.getId());
                sel.setFlowVersion(flowAction.getFlowVersion());
                flowUserSelectionProvider.createFlowUserSelection(sel);
            }
        }
    }

    @Override
    public FlowGraph getFlowGraph(Long flowId, Integer flowVer) {
        if (flowVer.equals(0)) {
            return getConfigGraph(flowId);
        }

        String fmt = String.format("%d:%d", flowId, flowVer);
        FlowGraph flowGraph = graphMap.get(fmt);
        if (flowGraph == null) {
            flowGraph = getSnapshotGraph(flowId, flowVer);
            graphMap.put(fmt, flowGraph);
        }

        return flowGraph;
    }

    private void clearSnapshotGraph(Flow snapshotFlow) {
        if (snapshotFlow != null) {
            for (int i = 1; i <= snapshotFlow.getFlowVersion(); i++) {
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
        if (flowVer <= 0) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot noexists");
        }

        FlowGraph flowGraph = new FlowGraph();
        flowGraph.setCreateTime(System.currentTimeMillis());
        Flow flow = flowProvider.findSnapshotFlow(flowId, flowVer);
        if (flow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot noexists");
        }
        flowGraph.setFlow(flow);

        List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, flowVer);
        flowNodes.sort((n1, n2) -> {
            return n1.getNodeLevel().compareTo(n2.getNodeLevel());
        });

        int i = 0;
        for (FlowNode fn : flowNodes) {
            if (!fn.getNodeLevel().equals(i)) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_LEVEL_ERR, "node_level error flowId=" + flowId + " ");
            }

            if (fn.getNodeName().equals("START")) {
                flowGraph.getNodes().add(new FlowGraphNodeStart(fn));
            } else if (fn.getNodeName().equals("END")) {
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
        if (flow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot noexists");
        }
        flowGraph.setFlow(flow);

        List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, 0);
        flowNodes.sort((n1, n2) -> {
            return n1.getNodeLevel().compareTo(n2.getNodeLevel());
        });

        int i = 1;
        for (FlowNode fn : flowNodes) {
            if (!fn.getNodeLevel().equals(i)) {
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
        if (cmd.getEntitySel() == null) {
            cmd.setEntitySel(new ArrayList<>());
        }
        if (cmd.getEntityId() != null && cmd.getFlowEntityType() != null) {
            FlowEntitySel sel = new FlowEntitySel();
            sel.setEntityId(cmd.getEntityId());
            sel.setFlowEntityType(cmd.getFlowEntityType());
            cmd.getEntitySel().add(sel);
            cmd.setEntityId(null);
            cmd.setFlowEntityType(null);
        }
        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(UserContext.current().getUser().getId());
        FlowCaseState ctx = flowStateProcessor.prepareButtonFire(userInfo, cmd);
        flowStateProcessor.step(ctx, ctx.getCurrentEvent());

        FlowButton btn = flowButtonProvider.getFlowButtonById(cmd.getButtonId());
        return ConvertHelper.convert(btn, FlowButtonDTO.class);
    }

    @Override
    public void processStepTimeout(FlowTimeout ft) {
        FlowCaseState ctx = flowStateProcessor.prepareStepTimeout(ft);
        if (ctx != null) {
            ctx.pushProcessType(FlowCaseStateStackType.STEP_ASYNC_TIMEOUT);
            flowStateProcessor.step(ctx, ctx.getCurrentEvent());
            ctx.popProcessType();
        }
    }

    @Override
    public void processAutoStep(FlowAutoStepDTO stepDTO) {
        FlowCaseState ctx = flowStateProcessor.prepareAutoStep(stepDTO);
        if (ctx != null) {
            ctx.pushProcessType(FlowCaseStateStackType.STEP_SYNC_PROCESS);
            flowStateProcessor.step(ctx, ctx.getCurrentEvent());
            ctx.popProcessType();
        }
    }

    @Override
    public void processMessageTimeout(FlowTimeout ft) {
        FlowTimeoutMessageDTO dto = (FlowTimeoutMessageDTO) StringHelper.fromJsonString(ft.getJson(), FlowTimeoutMessageDTO.class);
        if (dto == null) {
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
        if (ctx == null) {
            LOGGER.error("flowtimeout context error ft=" + ft.getId());
            return;
        }
        ctx.pushProcessType(FlowCaseStateStackType.STEP_ASYNC_TIMEOUT);
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(dto.getFlowCaseId());
        FlowAction flowAction = flowActionProvider.getFlowActionById(ft.getBelongTo());
        ctx.setFlowCase(flowCase);
        if (FlowActionType.TICK_MESSAGE.getCode().equals(flowAction.getActionType())
                || FlowActionType.TICK_SMS.getCode().equals(flowAction.getActionType())) {
            //check if the step is processed
            if (!flowCase.getStepCount().equals(dto.getStepCount()) || !flowCase.getCurrentNodeId().equals(dto.getFlowNodeId())) {
                //NOT OK
                LOGGER.info("ft timeout occur but step is processed! ft=" + ft.getId());
                return;
            }
        }

        List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(ft.getBelongTo()
                , ft.getBelongEntity(), FlowUserType.PROCESSOR.getCode());
        List<Long> users = resolvUserSelections(ctx, FlowEntityType.FLOW_ACTION, flowAction.getId(), selections);
        String dataStr = parseActionTemplate(ctx, ft.getBelongTo(), flowAction.getRenderText());

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("flowtimeout tick message, text={}, size={}", dataStr, users.size());

        if (dataStr == null || dataStr.trim().isEmpty()) {
            LOGGER.error("flowtimeout dataStr empty ft=" + ft.getId());
            return;
        }

        for (Long userId : users) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("flowtimeout tick message, text={}, userId={}, ftId={}", dataStr, userId, ft.getId());
            }

            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()),
                    new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.BIZ_USER_LOGIN.getUserId())));
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            messageDto.setBody(dataStr);
            messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

            FlowCaseDetailActionData actionData = new FlowCaseDetailActionData();
            actionData.setFlowCaseId(flowCase.getId());

            // 根据当前发送消息的用户是否是申请人，来确定 FlowUserType
            if (Objects.equals(flowCase.getApplyUserId(), userId)) {
                actionData.setFlowUserType(FlowUserType.APPLIER.getCode());
            } else {
                actionData.setFlowUserType(FlowUserType.PROCESSOR.getCode());
            }

            actionData.setModuleId(ctx.getModule().getModuleId());

            RouterMetaObject rmo = new RouterMetaObject();
            rmo.setUrl(RouterBuilder.build(Router.WORKFLOW_DETAIL, actionData));

            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, rmo.toString());
            meta.put(MessageMetaConstant.MESSAGE_SUBJECT, flowCase.getTitle());

            messageDto.setMeta(meta);

            flowListenerManager.onFlowMessageSend(ctx, messageDto);
            messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                    userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
        }

        if (dto.getRemindTick() != null && dto.getRemindTick() > 0 && dto.getRemindCount() != null && dto.getRemindCount() > 0) {
            dto.setRemindCount(dto.getRemindCount() - 1);
            // dto.setTimeoutAtTick(dto.getRemindTick());
            ft.setId(null);
            ft.setStatus(FlowStatusType.VALID.getCode());
            ft.setJson(dto.toString());
            Long timeoutTick = DateHelper.currentGMTTime().getTime() + dto.getRemindTick() * 60 * 1000L;
            ft.setTimeoutTick(new Timestamp(timeoutTick));
            flowTimeoutService.pushTimeout(ft);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("flowMessageTimeout remindTick did not run, ftId={}, dto={}", ft.getId(), dto);
            }
        }
        ctx.popProcessType();
    }

    @Override
    public void processSMSTimeout(FlowTimeout ft) {
        FlowTimeoutMessageDTO dto = (FlowTimeoutMessageDTO) StringHelper.fromJsonString(ft.getJson(), FlowTimeoutMessageDTO.class);
        if (dto == null) {
            LOGGER.error("flowsmstimeout error ft=" + ft.getId());
            return;
        }
        FlowAutoStepDTO stepDTO = ConvertHelper.convert(dto, FlowAutoStepDTO.class);
        FlowCaseState ctx = flowStateProcessor.prepareNoStep(stepDTO);
        if (ctx == null) {
            LOGGER.error("flowsmstimeout context error ft=" + ft.getId());
            return;
        }
        ctx.pushProcessType(FlowCaseStateStackType.STEP_ASYNC_TIMEOUT);
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(dto.getFlowCaseId());
        FlowAction flowAction = flowActionProvider.getFlowActionById(ft.getBelongTo());
        ctx.setFlowCase(flowCase);
        if (flowAction.getTemplateId() == null
                || FlowActionType.TICK_MESSAGE.getCode().equals(flowAction.getActionType())
                || FlowActionType.TICK_SMS.getCode().equals(flowAction.getActionType())) {
            //check if the step is processed
            if (!flowCase.getStepCount().equals(dto.getStepCount()) || !flowCase.getCurrentNodeId().equals(dto.getFlowNodeId())) {
                //NOT OK
                LOGGER.info("flowsmstimeout template empty or occur but step is processed! ft=" + ft.getId());
                return;
            }
        }

        List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(ft.getBelongTo()
                , ft.getBelongEntity(), FlowUserType.PROCESSOR.getCode());
        List<Long> users = resolvUserSelections(ctx, FlowEntityType.FLOW_ACTION, flowAction.getId(), selections);
        Integer namespaceId = ctx.getFlowCase().getNamespaceId();
        //TODO better here
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        int templateId = flowAction.getTemplateId().intValue();
//		String scope = "flow:" + ctx.getModule().getModuleId();
//		LocaleTemplate template = localeTemplateService.getLocalizedTemplate(namespaceId, scope, templateId, locale);
        List<Tuple<String, Object>> variables = new ArrayList<Tuple<String, Object>>();
        flowListenerManager.onFlowSMSVariableRender(ctx, templateId, variables);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("flowsmstimeout tick message, size={}", users.size());

        for (Long userId : users) {
            UserInfo user = userService.getUserSnapshotInfoWithPhone(userId);
            if (user != null && user.getPhones().size() > 0) {
                sendCodeSms(namespaceId, locale, SmsTemplateCode.SCOPE, templateId, user, variables);
            }
            LOGGER.debug("flowsmstimeout tick message, userId={}", userId);
        }

        ctx.popProcessType();
    }

    private void sendCodeSms(Integer namespaceId, String locale, String templateScope, int templateId, UserInfo user, List<Tuple<String, Object>> variables) {

        String phoneNumber = user.getPhones().get(0);
        smsProvider.sendSms(namespaceId, phoneNumber, templateScope, templateId, locale, variables);
    }

    @Override
    public void disableFlow(Long flowId) {
        Flow flow = flowProvider.getFlowById(flowId);
        if (flow == null /* || !flow.getFlowVersion().equals(FlowConstants.FLOW_CONFIG_VER) */) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flow not exists");
        }

        if (flow.getStatus() != null && flow.getStatus().equals(FlowStatusType.RUNNING.getCode())) {

            Flow snapshotFlow = flowProvider.getSnapshotFlowById(flowId);
            clearSnapshotGraph(snapshotFlow);

            dbProvider.execute(status -> {
                flow.setStatus(FlowStatusType.CONFIG.getCode());
                Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
                flow.setUpdateTime(now);
                flowProvider.updateFlow(flow);

                snapshotFlow.setStatus(FlowStatusType.STOP.getCode());
                snapshotFlow.setUpdateTime(now);
                flowProvider.updateFlow(snapshotFlow);
                return true;
            });
        }
    }

    /**
     * 获取正在启用的 Flow
     */
    @Override
    public Flow getEnabledFlow(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType) {
        Flow flow = flowProvider.getEnabledConfigFlow(namespaceId, moduleId, moduleType, ownerId, ownerType);
        if (flow != null) {
            return flowProvider.getSnapshotFlowById(flow.getId());
        }
        return null;
    }

    @Override
    public FlowCase createFlowCase(CreateFlowCaseCommand flowCaseCmd) {
        Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCaseCmd.getFlowMainId(), flowCaseCmd.getFlowVersion());
        if (snapshotFlow == null || snapshotFlow.getFlowVersion().equals(FlowConstants.FLOW_CONFIG_VER)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flow node error");
        }

        FlowCase flowCase = ConvertHelper.convert(flowCaseCmd, FlowCase.class);
        flowCase.setCurrentNodeId(0L);
        if (flowCase.getApplyUserId() == null) {
            flowCase.setApplyUserId(UserContext.current().getUser().getId());
        }
        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(flowCase.getApplyUserId());
        flowCase.setApplierName(userInfo.getNickName());
        if (userInfo.getPhones() != null && userInfo.getPhones().size() > 0) {
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
        flowCase.setOrganizationId(snapshotFlow.getOrganizationId());
        flowCase.setApplierOrganizationId(flowCaseCmd.getCurrentOrganizationId());

        if (flowCase.getModuleType() == null) {
            flowCase.setModuleType(FlowModuleType.NO_MODULE.getCode());
        }

        if (flowCaseCmd.getProjectId() == null) {
            //use default projectId
            flowCase.setProjectId(snapshotFlow.getProjectId());
            flowCase.setProjectType(snapshotFlow.getProjectType());
        }

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowCases.class));
        flowCase.setId(id);

        flowListenerManager.onFlowCaseCreating(flowCase);

        flowCaseProvider.createFlowCaseHasId(flowCase);

        flowListenerManager.onFlowCaseCreated(flowCase);
        flowCase = flowCaseProvider.getFlowCaseById(flowCase.getId());//get again for default values

        FlowCaseState ctx = flowStateProcessor.prepareStart(userInfo, flowCase);
        ctx.pushProcessType(FlowCaseStateStackType.STEP_SYNC_PROCESS);
        flowStateProcessor.step(ctx, ctx.getCurrentEvent());
        ctx.popProcessType();

        return flowCase;
    }

    /**
     * 创建一个没有工作流对应的 flowCase
     */
    @Override
    public FlowCase createDumpFlowCase(GeneralModuleInfo ga, CreateFlowCaseCommand flowCaseCmd) {
        FlowCase flowCase = ConvertHelper.convert(flowCaseCmd, FlowCase.class);
        flowCase.setCurrentNodeId(0l);
        if (flowCase.getApplyUserId() == null) {
            flowCase.setApplyUserId(UserContext.current().getUser().getId());
        }
        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(flowCase.getApplyUserId());
        flowCase.setApplierName(userInfo.getNickName());
        if (userInfo.getPhones() != null && userInfo.getPhones().size() > 0) {
            flowCase.setApplierPhone(userInfo.getPhones().get(0));
        }

        FlowModuleDTO moduleDTO = this.getModuleById(ga.getModuleId());

        flowCase.setFlowMainId(0l);
        flowCase.setFlowVersion(0);
        flowCase.setNamespaceId(ga.getNamespaceId());
        flowCase.setModuleId(ga.getModuleId());
        flowCase.setModuleName(moduleDTO.getDisplayName());
        if (ga.getModuleType() == null) {
            ga.setModuleType(FlowModuleType.NO_MODULE.getCode());
        }
        flowCase.setModuleType(ga.getModuleType());
        flowCase.setOwnerId(ga.getOwnerId());
        flowCase.setOwnerType(ga.getOwnerType());
        flowCase.setCaseType(FlowCaseType.DUMB.getCode());
        flowCase.setStatus(FlowCaseStatus.PROCESS.getCode());
        flowCase.setProjectId(ga.getProjectId());
        flowCase.setProjectType(ga.getProjectType());
        flowCase.setOrganizationId(ga.getOrganizationId());

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowCases.class));
        flowCase.setId(id);
        flowListenerManager.onFlowCaseCreating(flowCase);

        flowCaseProvider.createFlowCaseHasId(flowCase);


        flowListenerManager.onFlowCaseCreated(flowCase);


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
                , flow.getOwnerId(), flow.getOwnerType(), flow.getModuleId(), flow.getModuleType(), para, cmd.getFlowVariableType());
        if (vars2 != null) {
            vars.addAll(vars2);
        }
        vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
                , 0l, null, flow.getModuleId(), flow.getModuleType(), para, cmd.getFlowVariableType());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
                , 0l, null, flow.getModuleId(), flow.getModuleType(), para, cmd.getFlowVariableType());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
                , 0l, null, 0l, null, para, cmd.getFlowVariableType());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        if (!cmd.getNamespaceId().equals(0)) {
            vars2 = flowVariableProvider.findVariables(0
                    , 0l, null, 0l, null, para, cmd.getFlowVariableType());
            if (vars2 != null) {
                vars.addAll(vars2);
            }
        }

        Map<String, Long> map = new HashMap<String, Long>();
        for (FlowVariable var : vars) {
            if (!map.containsKey(var.getName())) {
                dtos.add(ConvertHelper.convert(var, FlowVariableDTO.class));
                map.put(var.getName(), 1l);
            }
        }

        return resp;
    }

    @Override
    public FlowVariableResponse listFlowVariables(ListFlowVariablesCommand cmd) {
        if (cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(UserContext.current().getNamespaceId());
        }
        if (cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(0);
        }

        FlowVariableType varType = FlowVariableType.fromCode(cmd.getFlowVariableType());
        if (varType == FlowVariableType.TEXT || varType == FlowVariableType.TEXT_BUTTON
                || varType == FlowVariableType.TEXT_REMIND || varType == FlowVariableType.TEXT_TRACKER) {
            FlowEntityType entityType = FlowEntityType.fromCode(cmd.getEntityType());
            if (entityType == null) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow params error");
            }

            Flow flow = getFlowByEntity(cmd.getEntityId(), entityType);
            if (flow == null) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow not found");
            }

            return listFlowTextVariables(flow, cmd);
        } else {
            FlowVariableResponse resp = new FlowVariableResponse();
            String para = null;
            List<FlowVariable> vars = flowVariableProvider.findVariables(0
                    , 0l, null, 0l, null, para, cmd.getFlowVariableType());

            List<FlowVariableDTO> dtos = new ArrayList<>();
            resp.setDtos(dtos);

            Map<String, Long> map = new HashMap<String, Long>();
            for (FlowVariable var : vars) {
                if (!map.containsKey(var.getName())) {
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
        if (sels != null && sels.size() > 0) {
            updateFlowUserName(sels.get(0));
            name = sels.get(0).getSelectionName();
            for (int i = 1; i < sels.size() && i < 3; i++) {
                updateFlowUserName(sels.get(i));
                name = name + "," + sels.get(i).getSelectionName();
            }
            dto.setProcessUserName(name);
        }

        Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());

        //evaluate
        dto.setNeedEvaluate((byte) 0);
        List<FlowEvaluate> evas = flowEvaluateProvider.findEvaluates(flowCase.getId(), snapshotFlow.getFlowMainId(), snapshotFlow.getFlowVersion());
        if (evas != null && evas.size() > 0) {
            dto.setEvaluateScore(new Integer(evas.get(0).getStar()));
            // dto.setNeedEvaluate((byte) 2);
        } else {
            if (1 == type && !snapshotFlow.getNeedEvaluate().equals((byte) 0)
                    && flowNode.getNodeLevel() >= snapshotFlow.getEvaluateStart()
                    && flowNode.getNodeLevel() <= snapshotFlow.getEvaluateEnd()
                    && !flowCase.getStatus().equals(FlowCaseStatus.ABSORTED.getCode())) {
                dto.setNeedEvaluate((byte) 1);
            }
        }

    }

    @Override
    public SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd) {
        SearchFlowCaseResponse resp = new SearchFlowCaseResponse();
        if (cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }
        if (cmd.getFlowCaseSearchType() == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow param error");
        }

        if (!cmd.getFlowCaseSearchType().equals(FlowCaseSearchType.ADMIN.getCode()) && cmd.getUserId() == null) {
            cmd.setUserId(UserContext.current().getUser().getId());
        }

        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        cmd.setPageSize(count);
        ListingLocator locator = new ListingLocator();

        List<FlowCaseDetail> details = null;

        int type = 0;
        if (cmd.getFlowCaseSearchType().equals(FlowCaseSearchType.APPLIER.getCode())) {
            type = 1;
            details = flowCaseProvider.findApplierFlowCases(locator, count, cmd);
        } else if (cmd.getFlowCaseSearchType().equals(FlowCaseSearchType.ADMIN.getCode())) {
            type = 2;
            details = flowCaseProvider.findAdminFlowCases(locator, count, cmd);
        } else {
            type = 3;
            details = flowEventLogProvider.findProcessorFlowCases(locator, count, cmd);
        }

        List<FlowCaseDTO> dtos = new ArrayList<FlowCaseDTO>();
        if (details != null) {
            for (FlowCaseDetail detail : details) {
                FlowCaseDTO dto = ConvertHelper.convert(detail, FlowCaseDTO.class);
                FlowNode flowNode = flowNodeProvider.getFlowNodeById(dto.getCurrentNodeId());
                if (flowNode != null) {
                    updateCaseDTO(detail, flowNode, dto, type);
                }
                if (dto.getTitle() != null) {
                    dto.setModuleName(dto.getTitle());
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

    private FlowButtonDTO flowButtonToDTO(Flow snapshotFlow, FlowButton b, Map<Long, FlowNode> nodeMap, int level) {
        FlowButtonDTO btnDTO = ConvertHelper.convert(b, FlowButtonDTO.class);

        FlowStepType stepType = FlowStepType.fromCode(b.getFlowStepType());
        if (stepType != FlowStepType.APPROVE_STEP && stepType != FlowStepType.TRANSFER_STEP) {
            btnDTO.setNeedProcessor((byte) 0);
        }
        if (stepType == FlowStepType.TRANSFER_STEP) {
            /* force use processor */
            btnDTO.setNeedProcessor((byte) 1);
        }
        if (stepType == FlowStepType.APPROVE_STEP && level >= nodeMap.size() - 2) {
            btnDTO.setNeedProcessor((byte) 0);
        }

        return btnDTO;
    }

    @Override
    public FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId, Long inUserId, FlowUserType flowUserType, boolean checkProcessor) {
        Long userId = inUserId;
        if (userId == null) {
            userId = UserContext.current().getUser().getId();
        }

        FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
        if (flowCase == null) {
            return new FlowCaseDetailDTO();
        }
        Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());

        List<FlowCaseEntity> entities = flowListenerManager.onFlowCaseDetailRender(flowCase, flowUserType);

        FlowCaseDetailDTO dto = ConvertHelper.convert(flowCase, FlowCaseDetailDTO.class);
        dto.setEntities(entities);
        if (dto.getStatus().equals(FlowCaseStatus.INVALID.getCode())) {
            return dto;
        }

        if (dto.getTitle() != null) {
            dto.setModuleName(dto.getTitle());
        }

        List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        Map<Long, FlowNode> nodeMap = new HashMap<>();
        int level = 0;
        boolean found = false;
        for (FlowNode node : nodes) {
            nodeMap.put(node.getId(), node);

            if (!found) {
                if (!flowCase.getCurrentNodeId().equals(node.getId())) {
                    level++;
                } else {
                    found = true;
                }
            }
        }
        if (level == nodes.size()) {
            //not found
            level = 0;
        }
        final Integer nlevel = level;

        if (nodes.size() < 3) {
            return dto;
        }

        List<FlowButtonDTO> btnDTOS = new ArrayList<>();
        if (flowUserType == FlowUserType.PROCESSOR) {

            if (!checkProcessor || (null != flowEventLogProvider.isProcessor(userId, flowCase))) {
                List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), flowUserType.getCode());
                buttons.forEach((b) -> {
                    boolean isAdd = true;
                    if (flowCase.getCurrentNodeId().equals(nodes.get(1).getId())
                            && b.getFlowStepType().equals(FlowStepType.REJECT_STEP.getCode())) {
                        isAdd = false;
                    }
                    // if(flowCase.getCurrentNodeId().equals(nodes.get(nodes.size()-1).getId())
                    // 		&& b.getFlowStepType().equals(FlowStepType.APPROVE_STEP.getCode())) {
                    // 	isAdd = false;
                    // }

                    if (isAdd && b.getStatus().equals(FlowButtonStatus.ENABLED.getCode())
                            && !b.getFlowStepType().equals(FlowStepType.COMMENT_STEP.getCode())) {
                        FlowButtonDTO btnDTO = flowButtonToDTO(snapshotFlow, b, nodeMap, nlevel);
                        btnDTOS.add(btnDTO);
                    }
                });
            }

            dto.setButtons(btnDTOS);
        } else if (flowUserType == FlowUserType.APPLIER) {
            List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), flowUserType.getCode());
            buttons.forEach((b) -> {
                if (b.getStatus().equals(FlowButtonStatus.ENABLED.getCode())
                        && !b.getFlowStepType().equals(FlowStepType.COMMENT_STEP.getCode())) {
                    FlowButtonDTO btnDTO = ConvertHelper.convert(b, FlowButtonDTO.class);
                    FlowStepType stepType = FlowStepType.fromCode(b.getFlowStepType());
                    if (stepType == FlowStepType.REMINDER_STEP) {
                        btnDTO.setNeedSubject((byte) 0);
                        btnDTO.setSubjectRequiredFlag(TrueOrFalseFlag.FALSE.getCode());
                    }

                    btnDTOS.add(btnDTO);
                }
            });

            dto.setButtons(btnDTOS);
        }//SUPERVISOR at last

        //got all nodes tracker logs
        List<FlowEventLog> stepLogs = flowEventLogProvider.findStepEventLogs(flowCaseId);
//		if(stepLogs != null) {
//			FlowNode fn = nodes.get(nodes.size()-1);
//			FlowEventLog fe = new FlowEventLog();
//			fe.setFlowNodeId(fn.getId());
//			fe.setFlowCaseId(flowCase.getId());
//			fe.setFlowMainId(flowCase.getFlowMainId());
//			fe.setFlowVersion(flowCase.getFlowVersion());
//			fe.setLogType(FlowLogType.STEP_TRACKER.getCode());
//			stepLogs.add(fe);
//		}
        List<FlowNodeLogDTO> nodeDTOS = new ArrayList<>();
        dto.setNodes(nodeDTOS);

        FlowNodeLogDTO nodeDTO = new FlowNodeLogDTO();
        nodeDTO.setNodeLevel(0);
        nodeDTO.setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.START_STEP));
        nodeDTOS.add(nodeDTO);

        FlowNode currNode = null;
        boolean absorted = false;

        // 构建有stepLog的节点信息
        for (FlowEventLog eventLog : stepLogs) {
            //获取工作流经过的节点日志
            if (currNode == null || !currNode.getId().equals(eventLog.getFlowNodeId())) {
                currNode = nodeMap.get(eventLog.getFlowNodeId());
                final FlowNodeLogDTO nodeLogDTO = new FlowNodeLogDTO();
                nodeLogDTO.setNodeId(currNode.getId());
                nodeLogDTO.setNodeLevel(currNode.getNodeLevel());
                nodeLogDTO.setNodeName(currNode.getNodeName());
                nodeLogDTO.setParams(currNode.getParams());

                if (flowCase.getStepCount().equals(eventLog.getStepCount())) {
                    nodeLogDTO.setIsCurrentNode((byte) 1);
                    dto.setCurrNodeParams(currNode.getParams());

                    FlowButton commentBtn = flowButtonProvider.findFlowButtonByStepType(currNode.getId()
                            , currNode.getFlowVersion(), FlowStepType.COMMENT_STEP.getCode(), flowUserType.getCode());
                    if (commentBtn != null && commentBtn.getStatus().equals(FlowButtonStatus.ENABLED.getCode())) {
                        nodeLogDTO.setAllowComment((byte) 1);
                        nodeLogDTO.setCommentButtonId(commentBtn.getId());
                    }
                    if (eventLog.getButtonFiredStep().equals(FlowStepType.ABSORT_STEP.getCode())) {
                        absorted = true;
                        nodeLogDTO.setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.ABSORT_STEP));
                    }
                }

                nodeDTOS.add(nodeLogDTO);

                getFlowNodeLogDTO(flowCase, flowUserType, currNode, eventLog.getStepCount(), nodeLogDTO);
            }
        }

        if (!absorted) {
            //表示已完成
            int i = 1;
            if (currNode != null) {
                i = currNode.getNodeLevel() + 1;
            }
            // 构建没有stepLog的节点信息
            for (; i < nodes.size() - 1; i++) {
                nodeDTO = new FlowNodeLogDTO();
                nodeDTO.setNodeLevel(nodes.get(i).getNodeLevel());
                nodeDTO.setNodeName(nodes.get(i).getNodeName());
                nodeDTOS.add(nodeDTO);
            }
            nodeDTO = new FlowNodeLogDTO();
            nodeDTO.setNodeLevel(nodes.size());
            nodeDTO.setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.END_STEP));
            if (!flowCase.getStatus().equals(FlowCaseStatus.PROCESS.getCode())) {
                getFlowNodeLogDTO(flowCase, flowUserType, nodes.get(nodes.size() - 1), flowCase.getStepCount(), nodeDTO);
                nodeDTO.setIsCurrentNode((byte) 1);
                if (nodeDTO.getLogs().size() == 0) {
                    for (int j = nodeDTOS.size() - 1; j >= 0; j--) {
                        FlowNodeLogDTO tmpDTO = nodeDTOS.get(j);
                        if (tmpDTO.getLogs().size() > 0) {
                            nodeDTO.getLogs().add(tmpDTO.getLogs().get(tmpDTO.getLogs().size() - 1));
                            tmpDTO.getLogs().remove(tmpDTO.getLogs().size() - 1);
                            break;
                        }
                    }
                }
            }

            nodeDTOS.add(nodeDTO);
        } else {
            //异常结束 BUG 6052
            for (int j = nodeDTOS.size() - 2; j >= 0; j--) {
                FlowNodeLogDTO tmpDTO = nodeDTOS.get(j);
                if (tmpDTO.getLogs().size() > 0) {
                    nodeDTOS.get(nodeDTOS.size() - 1).getLogs().add(tmpDTO.getLogs().get(tmpDTO.getLogs().size() - 1));
                    tmpDTO.getLogs().remove(tmpDTO.getLogs().size() - 1);
                    break;
                }
            }
        }

        //fix multiple current node
//		for(int i = nodeDTOS.size()-1; i >= 0; i--) {
//			nodeDTO = nodeDTOS.get(i);
//			if(nodeDTO.getIsCurrentNode() != null && nodeDTO.getIsCurrentNode().equals((byte)1)) {
//				int j = i-1;
//				for(; j >= 0; j--) {
//					nodeDTO.setIsCurrentNode((byte)0);
//				}
//
//				break;
//			}
//		}

        return dto;
    }

    //获取每个节点的跟踪日志，如果有文本，则格式化文本
    private void getFlowNodeLogDTO(FlowCase flowCase, FlowUserType flowUserType, FlowNode currNode, Long stepCount, FlowNodeLogDTO nodeLogDTO) {
        List<FlowEventLog> trackerLogs = flowEventLogProvider.findEventLogsByNodeId(currNode.getId()
                , flowCase.getId(), stepCount, flowUserType);
        if (trackerLogs != null) {
            trackerLogs.forEach((t) -> {
                FlowEventLogDTO eventDTO = ConvertHelper.convert(t, FlowEventLogDTO.class);
                if (FlowStepType.EVALUATE_STEP.getCode().equals(t.getButtonFiredStep())) {
                    eventDTO.setIsEvaluate((byte) 1);
                }
//				if(eventDTO.getLogContent() != null) {
//					String dateStr = sdf1.format(new Date(eventDTO.getCreateTime().getTime()));
//					eventDTO.setLogContent(dateStr + " " + eventDTO.getLogContent());
//				}
                nodeLogDTO.getLogs().add(eventDTO);
            });
        }
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

        if (null != cmd.getImages() && cmd.getImages().size() > 0) {
            List<Attachment> attachments = new ArrayList<>();
            for (String image : cmd.getImages()) {
                Attachment attach = new Attachment();
                attach.setContentType(NewsCommentContentType.IMAGE.getCode());
                attach.setContentUri(image);
                attach.setCreatorUid(UserContext.current().getUser().getId());
                attach.setOwnerId(subject.getId());
                attachments.add(attach);
            }
            attachmentProvider.createAttachments(EhFlowAttachments.class, attachments);

            for (Attachment at : attachments) {
                String url = contentServerService.parserUri(at.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
                if (url != null && !url.isEmpty()) {
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
        if (flowCase == null || snapshotFlow == null || snapshotFlow.getNeedEvaluate().equals((byte) 0)
                || flowCase.getStatus().equals(FlowCaseStatus.INVALID.getCode())
                ) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS, "flowcase noexists, flowCaseId=" + flowCase);
        }

        Map<Long, FlowEvaluateItemStar> evaMap = new HashMap<>();
        if (cmd.getStars() != null && cmd.getStars().size() > 0) {
            cmd.getStars().forEach(ev -> evaMap.put(ev.getItemId(), ev));
        }

        List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        if (items == null || evaMap.size() != items.size()) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "params error");
        }

        List<FlowEvaluate> flowEvas = new ArrayList<>();
        for (FlowEvaluateItem item : items) {
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
            FlowEvaluateItemStar star = evaMap.get(item.getId());
            eva.setStar(star.getStat());
            // 如果配置项为允许输入评论内容，则接收评论内容
            if (TrueOrFalseFlag.fromCode(item.getInputFlag()) == TrueOrFalseFlag.TRUE) {
                eva.setContent(star.getContent());
            }
            flowEvas.add(eva);
        }

        flowEvaluateProvider.createFlowEvaluate(flowEvas);

        FlowEventLog tracker = new FlowEventLog();

        Map<String, Object> templateMap = new HashMap<>();
        // 如果评价内容只有1条则显示分数和评价内容，如果有多条，则显示:"用户已评价，查看详情"
        if (flowEvas.size() == 1) {
            FlowEvaluate evaluate = flowEvas.get(0);
            templateMap.put("score", String.valueOf(evaluate.getStar()));
            // 如果有评价内容，则把评价内容也加上
            if (evaluate.getContent() != null && evaluate.getContent().trim().length() > 0) {
                templateMap.put("content", evaluate.getContent().trim());
            }
        }

        tracker.setLogContent(getFireButtonTemplate(FlowStepType.EVALUATE_STEP, templateMap));
        tracker.setStepCount(flowCase.getStepCount());
        tracker.setId(flowEventLogProvider.getNextId());
        tracker.setFlowMainId(flowCase.getFlowMainId());
        tracker.setFlowVersion(flowCase.getFlowVersion());
        tracker.setNamespaceId(flowCase.getNamespaceId());
        tracker.setFlowNodeId(flowCase.getCurrentNodeId());
        tracker.setParentId(0L);
        tracker.setFlowCaseId(flowCase.getId());
        tracker.setFlowUserId(UserContext.current().getUser().getId());
        tracker.setLogType(FlowLogType.NODE_TRACKER.getCode());
        tracker.setButtonFiredStep(FlowStepType.EVALUATE_STEP.getCode());
        if (items.size() > 0) {
            tracker.setSubjectId(1L);
        }
        tracker.setTrackerApplier(1L);
        tracker.setTrackerProcessor(1L);
        flowEventLogProvider.createFlowEventLog(tracker);

        if (snapshotFlow.getEvaluateStep() != null
                && snapshotFlow.getEvaluateStep().equals(FlowStepType.APPROVE_STEP.getCode())
                && flowCase.getStatus().equals(FlowCaseStatus.PROCESS.getCode())) {
            FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
            stepDTO.setAutoStepType(snapshotFlow.getEvaluateStep());
            stepDTO.setFlowCaseId(flowCase.getId());
            stepDTO.setFlowMainId(flowCase.getFlowMainId());
            stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
            stepDTO.setFlowVersion(flowCase.getFlowVersion());
            if (cmd.getStepCount() == null) {
                cmd.setStepCount(flowCase.getStepCount());
            }
            stepDTO.setStepCount(cmd.getStepCount());
            processAutoStep(stepDTO);//fire next step
        }

        //TODO ignore the result ?
        return null;
    }

    private void updateFlowUserName(FlowUserSelectionDTO dto) {
        if (dto.getSelectionName() == null) {
            FlowUserSelectionType selType = FlowUserSelectionType.fromCode(dto.getSelectType());
            if (selType == FlowUserSelectionType.DEPARTMENT) {
                //Users selection
                UserInfo userInfo = userService.getUserSnapshotInfo(dto.getSourceIdA());
                if (dto.getSelectionName() == null) {
                    dto.setSelectionName(userInfo.getNickName());
                }
            }
        }
    }

    private void updateFlowUserName(FlowUserSelection dto) {
        if (dto.getSelectionName() == null) {
            FlowUserSelectionType selType = FlowUserSelectionType.fromCode(dto.getSelectType());
            if (selType == FlowUserSelectionType.DEPARTMENT) {
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
        if (btn != null) {
            flowNode = flowNodeProvider.getFlowNodeById(btn.getFlowNodeId());
        }
        if (flowNode == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "flow node not exists");
        }
        if (cmd.getFlowUserType() == null) {
            cmd.setFlowUserType(FlowUserType.PROCESSOR.getCode());
        }
        FlowNode nextNode = null;
        List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowNode.getFlowMainId(), flowNode.getFlowVersion());
        Map<Long, FlowNode> nodeMap = new HashMap<Long, FlowNode>();
        for (int i = 0; i < nodes.size(); i++) {
            FlowNode node = nodes.get(i);
            if (flowNode.getId().equals(node.getId())) {
                if ((i + 1) < nodes.size()) {
                    nextNode = nodes.get(i + 1);
                }
            }
            nodeMap.put(node.getId(), node);
        }
        if (btn.getGotoNodeId() != null && nodeMap.containsKey(btn.getGotoNodeId())) {
            nextNode = nodeMap.get(btn.getGotoNodeId());
        }
        if (nextNode == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_NOEXISTS, "next node not exists");
        }

        List<FlowUserSelection> seles = flowUserSelectionProvider.findSelectionByBelong(nextNode.getId(), FlowEntityType.FLOW_NODE.getCode(), cmd.getFlowUserType());
        if (seles != null) {
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
        if (moduleId.equals(111l)) {
            FlowModuleDTO dto = new FlowModuleDTO();
            dto.setModuleId(111l);
            dto.setModuleName("yuanqu");
            dto.setDisplayName("园区入驻");
            return dto;
        }

        if (moduleId.equals(112l)) {
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
        if (serviceModule != null) {
            FlowModuleDTO dto = new FlowModuleDTO();
            dto.setDisplayName(serviceModule.getName());
            dto.setModuleName(serviceModule.getName());
            dto.setModuleId(moduleId);
            return dto;
        }

//
//    Only for test. by Janson
//		if(moduleId.equals(41500L)) {
//			FlowModuleDTO dto = new FlowModuleDTO();
//			dto.setModuleId(41500L);
//			dto.setModuleName("车辆放行");
//			dto.setDisplayName("车辆放行");
//			return dto;
//		}

        return null;
    }

    @Override
    public ListFlowModulesResponse listModules(ListFlowModulesCommand cmd) {
        ListFlowModulesResponse resp = new ListFlowModulesResponse();
        List<FlowModuleDTO> modules = new ArrayList<>();
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

    @Override
    public List<Long> resolvUserSelections(FlowCaseState ctx, FlowEntityType entityType, Long entityId, List<FlowUserSelection> selections) {
        Map<String, Long> processedEntities = new HashMap<>();
        List<Long> userIdList = resolvUserSelections(ctx, processedEntities, entityType, entityId, selections, 1);
        // Remove dup users
        return userIdList.stream().distinct().collect(Collectors.toList());

        /*List<Long> rlts = new ArrayList<>();
        Map<Long, Long> maps = new HashMap<>();
		for(Long l : userIdList) {
			if(!maps.containsKey(l)) {
				maps.put(l, 1L);
				rlts.add(l);
			}
		}
		return rlts;*/
    }

    public List<Long> resolvUserSelections(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType entityType, Long entityId, List<FlowUserSelection> selections, int loopCnt) {
        return resolvUserSelections(ctx, processedEntities, entityType, entityId, selections, loopCnt, 10000);
    }

    /**
     * <ul>此函数需要关注三个问题：
     * <li> 1. 变量引用，不能循环引用。 </li>
     * <li> 2. 不能过深的循环 </li>
     * <li> 3. 很多情况只需要求得部分值，不需要求得全部值 </li>
     * </ul>
     * @param ctx 当前工作流上下文
     * @param processedEntities 已经处理过的对象
     * @param entityType
     * @param entityId
     * @param selections
     * @param loopCnt
     * @param maxCount
     * @return
     *
     */
    private List<Long> resolvUserSelections(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType entityType, Long entityId, List<FlowUserSelection> selections, int loopCnt, int maxCount) {
        List<Long> users = new ArrayList<>();

        //判断是否调用层次过深，避免让服务器崩溃
        if (selections == null || loopCnt >= 5) {
            return users;
        }

        Flow flow = ctx.getFlowGraph().getFlow();
        Long orgId = flow.getOrganizationId();

        for (FlowUserSelection sel : selections) {
            if (users.size() >= maxCount) {
                //为了加快处理的速度，有的情况不需要拿太多用户
                break;
            }

            if (sel.getId() != null) {
                //判断是否已经处理过，避免循环引用。如果直接是用户选择，则不需要判断
                String key = "sel:" + sel.getId();
                if (processedEntities.containsKey(key)) {
                    continue;
                }
                processedEntities.put(key, 1L);
            }

            if (FlowUserSourceType.SOURCE_USER.getCode().equals(sel.getSourceTypeA())) {
                users.add(sel.getSourceIdA());
            } else if (FlowUserSelectionType.POSITION.getCode().equals(sel.getSelectType())) {
                //sourceA is position, sourceB is department
                Long parentOrgId = orgId;
                if (sel.getOrganizationId() != null) {
                    parentOrgId = sel.getOrganizationId();
                }
                Long departmentId = parentOrgId;
                if (sel.getSourceIdB() != null) {
                    if (FlowUserSourceType.SOURCE_DUTY_DEPARTMENT.getCode().equals(sel.getSourceTypeB())) {
                        FlowCase flowCase = ctx.getFlowCase();
                        List<Long> tmp = flowUserSelectionService.findUsersByDudy(parentOrgId, flowCase.getModuleId(), flowCase.getProjectType(), flowCase.getProjectId(), sel.getSourceIdA());
                        users.addAll(tmp);
                        continue;
                    }

                    if (!sel.getSourceIdB().equals(0L)
                            && FlowUserSourceType.SOURCE_DEPARTMENT.getCode().equals(sel.getSourceTypeB())) {
                        departmentId = sel.getSourceIdB();
                    }
                }

//				LOGGER.error("position selId= " + sel.getId() + " positionId= " + sel.getSourceIdA() + " departmentId= " + departmentId);
                if (FlowUserSourceType.SOURCE_POSITION.getCode().equals(sel.getSourceTypeA())) {
                    List<Long> tmp = flowUserSelectionService.findUsersByJobPositionId(parentOrgId, sel.getSourceIdA(), departmentId);
                    if (tmp != null) {
                        users.addAll(tmp);
                    }
                } else {
                    LOGGER.error("resolvUser selId= " + sel.getId() + " position parse error!");
                }

            } else if (FlowUserSelectionType.MANAGER.getCode().equals(sel.getSelectType())) {
                Long parentOrgId = orgId;
                if (sel.getOrganizationId() != null) {
                    parentOrgId = sel.getOrganizationId();
                }

                Long departmentId = parentOrgId;
                if (sel.getSourceTypeA() == null
                        || FlowUserSourceType.SOURCE_DEPARTMENT.getCode().equals(sel.getSourceTypeA())) {
                    if (null != sel.getSourceIdA() && !sel.getSourceIdA().equals(0L)) {
                        departmentId = sel.getSourceIdA();
                    }

                    List<Long> tmp = flowUserSelectionService.findManagersByDepartmentId(parentOrgId, departmentId, ctx.getFlowGraph().getFlow());
                    users.addAll(tmp);
                } else if (FlowUserSourceType.SOURCE_DUTY_MANAGER.getCode().equals(sel.getSourceTypeA())) {
                    List<Long> idList = flowUserSelectionService.findModuleDutyManagers(departmentId, flow.getModuleId(), flow.getProjectType(), flow.getProjectId());
                    users.addAll(idList);
                } else {
                    LOGGER.error("resolvUser selId= " + sel.getId() + " manager parse error!");
                }
            } else if (FlowUserSelectionType.VARIABLE.getCode().equals(sel.getSelectType())) {
                if (sel.getSourceIdA() != null) {
                    FlowVariable variable = flowVariableProvider.getFlowVariableById(sel.getSourceIdA());
//					variable.getScriptCls();
                    FlowVariableUserResolver ftr = PlatformContext.getComponent(variable.getScriptCls());
                    if (ftr != null) {
                        List<Long> tmp = ftr.variableUserResolve(ctx, processedEntities, entityType, entityId, sel, loopCnt + 1);
                        if (null != tmp) {
                            users.addAll(tmp);
                        }
                    }
                } else {
                    LOGGER.error("user params error selId= " + sel.getId() + " variable error!");
                }
            }
        }
        return users.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public void createSnapshotNodeProcessors(FlowCaseState ctx, FlowGraphNode nextNode) {
        List<Long> users = new ArrayList<>();
        List<FlowUserSelection> selections = new ArrayList<>();

        FlowGraphEvent evt = ctx.getCurrentEvent();

        if (evt.getEntitySel() != null && evt.getEntitySel().size() > 0) {
            for (FlowEntitySel sel : evt.getEntitySel()) {
                if (sel.getEntityId() != null) {
                    if (FlowEntityType.FLOW_SELECTION.getCode().equals(sel.getFlowEntityType())) {
                        FlowUserSelection ul = flowUserSelectionProvider.getFlowUserSelectionById(sel.getEntityId());
                        selections.add(ul);
                    } else if (FlowEntityType.FLOW_USER.getCode().equals(sel.getFlowEntityType())) {
                        FlowUserSelection ul = new FlowUserSelection();
                        ul.setSourceIdA(sel.getEntityId());
                        ul.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
                        selections.add(ul);
                    }
                }
            }
        }
        // 驳回操作，驳回后的处理人为转到驳回节点时指定的处理人员
        else if (ctx.getStepType() == FlowStepType.REJECT_STEP) {
            Long flowNodeId = nextNode.getFlowNode().getId();
            Long flowCaseId = ctx.getFlowCase().getId();

            // 因为REJECT_STEP步骤在event的fire方法里也会执行 stepCount += 1,
            // 所以在这里要找到上一个节点的stepCount需要使用 stepCount - rejectCount * 2
            // Long stepCount = ctx.getFlowCase().getStepCount() - ctx.getFlowCase().getRejectCount() * 2;

            Long stepCount = flowEventLogProvider.findMaxStepCountByNodeEnterLog(flowNodeId, flowCaseId);

            List<FlowEventLog> enterLogs = flowEventLogProvider.findCurrentNodeEnterLogs(flowNodeId, flowCaseId, stepCount);
            for (FlowEventLog enterLog : enterLogs) {
                FlowUserSelection ul = new FlowUserSelection();
                ul.setSourceIdA(enterLog.getFlowUserId());
                ul.setSourceTypeA(FlowUserSourceType.SOURCE_USER.getCode());
                selections.add(ul);
            }
        } else {
            List<FlowUserSelection> subs = flowUserSelectionProvider.findSelectionByBelong(nextNode.getFlowNode().getId()
                    , FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
            if (subs != null && subs.size() > 0) {
                selections.addAll(subs);
            }
        }

        if (selections.size() > 0) {
            users = resolvUserSelections(ctx, FlowEntityType.FLOW_NODE, null, selections);
        }

        if (users.size() > 0) {
            for (Long selUser : users) {
                FlowEventLog log = new FlowEventLog();
                log.setId(flowEventLogProvider.getNextId());
                log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
                log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());//get real version
                log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
                if (ctx.getCurrentEvent() != null) {
                    log.setFlowButtonId(ctx.getCurrentEvent().getFiredButtonId());
                }

                log.setFlowNodeId(nextNode.getFlowNode().getId());
                log.setParentId(0L);
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
        if (users.size() > 0) {
            for (Long selUser : users) {
                FlowEventLog log = new FlowEventLog();
                log.setId(flowEventLogProvider.getNextId());
                log.setFlowMainId(flowCase.getFlowMainId());
                log.setFlowVersion(flowCase.getFlowVersion());//get real version
                log.setNamespaceId(flowCase.getNamespaceId());

                log.setParentId(0L);
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
            if (flowCaseProvider.updateIfValid(ctx.getFlowCase().getId(), ctx.getFlowCase().getLastStepTime(), now)) {
                ctx.getFlowCase().setLastStepTime(now);
                flowCaseProvider.updateFlowCase(ctx.getFlowCase());
                flowEventLogProvider.createFlowEventLogs(ctx.getLogs());

                flowEventLogProvider.updateFlowEventLogs(ctx.getUpdateLogs());
            } else {
                throw new FlowStepBusyException("already step by others");
            }
            return true;
        });

        //flush timeouts
        for (FlowTimeout ft : ctx.getTimeouts()) {
            flowTimeoutService.pushTimeout(ft);
        }
    }

    private List<String> getAllParams(String renderText) {
        List<String> params = new ArrayList<>();
        try {
            Matcher m = pParam.matcher(renderText);
            while (m.find()) {
                if (m.groupCount() > 0) {
                    params.add(m.group(1));
                }
            }
        } catch (Exception ex) {
            //TODO log ?
        }

        return params;
    }

    private String resolveTextVariable(FlowCaseState ctx, String para) {
        FlowCase fc = ctx.getFlowCase();
        List<FlowVariable> vars = new ArrayList<>();
        List<FlowVariable> vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
                , fc.getOwnerId(), fc.getOwnerType(), fc.getModuleId(), fc.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if (vars2 != null) {
            vars.addAll(vars2);
        }
        vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
                , 0l, null, fc.getModuleId(), fc.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
                , 0l, null, fc.getModuleId(), fc.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
                , 0l, null, 0l, null, para, FlowVariableType.TEXT.getCode());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        if (!fc.getNamespaceId().equals(0)) {
            vars2 = flowVariableProvider.findVariables(0
                    , 0l, null, 0l, null, para, FlowVariableType.TEXT.getCode());
            if (vars2 != null) {
                vars.addAll(vars2);
            }
        }


        for (FlowVariable fv : vars) {
//			try {
//				Class clz = Class.forName(flowScript.getScriptCls());
//				runnableScript = (FlowScriptFire)PlatformContext.getComponent(clz);
//			} catch (ClassNotFoundException e) {
//				LOGGER.error("flow script class not found", e);
//			}
            FlowVariableTextResolver ftr = PlatformContext.getComponent(fv.getScriptCls());
            if (ftr != null) {
                String val = ftr.variableTextRender(ctx, para);
                if (null != val) {
                    return val;
                }
            }

        }

        return "error";
    }

    @Override
    public String parseActionTemplate(FlowCaseState ctx, Long actionId, String renderText) {
        String templateKey = String.format("build:%d", actionId);
        Map<String, String> model = new HashMap<String, String>();
        List<String> params = getAllParams(renderText);
        if (params == null || params.size() == 0) {
            return renderText;
        }

        for (String para : params) {
            String fv = resolveTextVariable(ctx, para);
            if (fv != null) {
                model.put(para, fv);
            }
        }

        try {
            templateLoader.putTemplate(templateKey, renderText);
            Template freeMarkerTemplate = templateConfig.getTemplate(templateKey, "UTF8");
            if (freeMarkerTemplate != null) {
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
        if (attaches != null) {
            for (Attachment at : attaches) {
                String url = contentServerService.parserUri(at.getContentUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
                if (url != null && !url.isEmpty()) {
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
        switch (step) {
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
                code = switchCommentStepTemplateCode(map);
                break;
            case EVALUATE_STEP:
                code = switchEvaluateStepTemplateCode(map);
                break;
            default:
                break;
        }

        if (code == 0) {
            return "";
        }

        User user = UserContext.current().getUser();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (user != null) {
            locale = user.getLocale();
        }
        return localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
    }

    // 附言动作的字符串模板code
    private int switchCommentStepTemplateCode(Map<String, Object> map) {
        int code = 0;
        Object processorName = map.get("processorName");
        // 非发起人模板
        if (processorName != null) {
            if (map.get("imageCount") != null) {
                code = FlowTemplateCode.COMMENT_STEP_IMAGE_COUNT_WITH_USERNAME;
            } else if (map.get("content") != null) {
                code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_USERNAME;
            }
        }
        // 发起人模板
        else {
            if (map.get("imageCount") != null) {
                code = FlowTemplateCode.COMMENT_STEP_IMAGE_COUNT_WITH_APPLIER;
            } else if (map.get("content") != null) {
                code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER;
            }
        }
        return code;
    }

    // 评价动作的字符串模板code
    private int switchEvaluateStepTemplateCode(Map<String, Object> map) {
        int code;
        int size = map.size();
        switch (size) {
            case 1:
                code = FlowTemplateCode.EVALUATE_STEP;
                break;
            case 2:
                code = FlowTemplateCode.EVALUATE_STEP_WITH_CONTENT;
                break;
            default:
                code = FlowTemplateCode.EVALUATE_STEP_MULTI;
        }
        return code;
    }

    @Override
    public String getStepMessageTemplate(FlowStepType fromStep, FlowCaseStatus nextStatus, FlowUserType flowUserType, Map<String, Object> map) {
        String scope = FlowTemplateCode.SCOPE;
        int code = 0;
        if (nextStatus == FlowCaseStatus.FINISHED) {
            //到终止节点
            if (fromStep == FlowStepType.APPROVE_STEP) {
                code = FlowTemplateCode.NEXT_STEP_DONE;
            } else if (fromStep == FlowStepType.ABSORT_STEP) {
                if (flowUserType == FlowUserType.PROCESSOR) {
                    code = FlowTemplateCode.PROCESSOR_ABSORT;
                } else {
                    code = FlowTemplateCode.APPLIER_ABSORT;
                }
            }
        }

        if (code != 0) {
            User user = UserContext.current().getUser();
            String locale = Locale.SIMPLIFIED_CHINESE.toString();
            if (user != null) {
                locale = user.getLocale();
            }

            String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            return text;
        } else {
            return getFireButtonTemplate(fromStep, map);
        }

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

        List<FlowUserSelectionDTO> selections = new ArrayList<>();
        graphDetail.setSupervisors(selections);

        List<FlowUserSelection> seles = flowUserSelectionProvider.findSelectionByBelong(flowId
                , FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode(), 0);
        if (seles != null && seles.size() > 0) {
            seles.forEach((sel) -> {
                selections.add(ConvertHelper.convert(sel, FlowUserSelectionDTO.class));
            });
        }

        List<FlowNodeDetailDTO> nodes = new ArrayList<>();
        graphDetail.setNodes(nodes);
        List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, FlowConstants.FLOW_CONFIG_VER);

        flowNodes.sort(Comparator.comparing(EhFlowNodes::getNodeLevel));

        for (FlowNode fn : flowNodes) {
            FlowNodeDetailDTO nodeDetail = this.getFlowNodeDetail(fn.getId());
            List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(fn.getId()
                    , FlowConstants.FLOW_CONFIG_VER, FlowUserType.PROCESSOR.getCode());
            List<FlowButtonDetailDTO> btnDetails = new ArrayList<>();
            for (FlowButton btn : buttons) {
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
        if (!flow.getFlowMainId().equals(0L)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "Please use a config flowId");
        }

        deleteAllNodeProcessors(flow, flow.getId(), 0, userId);

        Flow snapshotFlow = flowProvider.getSnapshotFlowById(flowId);
        if (snapshotFlow != null) {
            deleteAllNodeProcessors(snapshotFlow, snapshotFlow.getFlowMainId(), snapshotFlow.getFlowVersion(), userId);
        }
    }

    private void deleteAllNodeProcessors(Flow snapshotFlow, Long flowMainId,
                                         Integer ver, Long userId) {
        List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowMainId, ver);
        List<FlowUserSelection> objs = new ArrayList<>();
        for (FlowNode fn : nodes) {
            FlowUserSelection sel = flowUserSelectionProvider.findFlowNodeSelectionUser(fn.getId(), ver, userId);
            if (sel != null) {
                objs.add(sel);
            }
        }

        flowUserSelectionProvider.deleteFlowUserSelections(objs);
    }

    @Override
    public void addSnapshotProcessUser(Long flowId, Long userId) {
        Flow flow = flowProvider.getFlowById(flowId);
        if (!flow.getFlowMainId().equals(0l)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "Please use a config flowId");
        }

        addAllNodeProcessors(flow, flow.getId(), 0, userId);

        Flow snapshotFlow = flowProvider.getSnapshotFlowById(flowId);
        if (snapshotFlow != null) {
            addAllNodeProcessors(snapshotFlow, snapshotFlow.getFlowMainId(), flow.getFlowVersion(), userId);
        }
    }

    private void addAllNodeProcessors(Flow flow, Long flowMainId, Integer ver, Long userId) {
        List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowMainId, ver);
        List<FlowUserSelection> objs = new ArrayList<>();
        for (FlowNode fn : nodes) {
            FlowUserSelection sel = flowUserSelectionProvider.findFlowNodeSelectionUser(fn.getId(), ver, userId);
            if (sel == null) {
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

        if (action == null) {
            action = new FlowAction();
            action.setFlowMainId(flow.getFlowMainId());
            action.setFlowVersion(flowVer);
            action.setActionStepType(actionStepType);
            action.setActionType(actionType);
            action.setBelongTo(flow.getId());
            action.setBelongEntity(FlowEntityType.FLOW_EVALUATE.getCode());
            action.setNamespaceId(flow.getNamespaceId());
            action.setFlowStepType(flowStepType);

            if (actionInfo.getReminderTickMinute() != null) {
                action.setReminderTickMinute(actionInfo.getReminderTickMinute());
            }
            if (actionInfo.getReminderAfterMinute() != null) {
                action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
            }
            if (actionInfo.getTrackerApplier() != null) {
                action.setTrackerApplier(actionInfo.getTrackerApplier());
            }
            if (actionInfo.getTrackerProcessor() != null) {
                action.setTrackerProcessor(actionInfo.getTrackerProcessor());
            }

            action.setStatus(FlowActionStatus.ENABLED.getCode());
            action.setRenderText(actionInfo.getRenderText());
            flowActionProvider.createFlowAction(action);

        } else {
            if (actionInfo.getReminderTickMinute() != null) {
                action.setReminderTickMinute(actionInfo.getReminderTickMinute());
            }
            if (actionInfo.getReminderAfterMinute() != null) {
                action.setReminderAfterMinute(actionInfo.getReminderAfterMinute());
            }
            if (actionInfo.getTrackerApplier() != null) {
                action.setTrackerApplier(actionInfo.getTrackerApplier());
            }
            if (actionInfo.getTrackerProcessor() != null) {
                action.setTrackerProcessor(actionInfo.getTrackerProcessor());
            }

            action.setStatus(FlowActionStatus.ENABLED.getCode());
            action.setRenderText(actionInfo.getRenderText());
            flowActionProvider.updateFlowAction(action);

            flowUserSelectionProvider.deleteSelectionByBelong(action.getId(), FlowEntityType.FLOW_ACTION.getCode(), FlowUserType.PROCESSOR.getCode());
        }

        if (selectionCmd != null && selectionCmd.getSelections() != null && selectionCmd.getSelections().size() > 0) {
            List<FlowSingleUserSelectionCommand> seles = selectionCmd.getSelections();
            for (FlowSingleUserSelectionCommand selCmd : seles) {
                FlowUserSelection userSel = new FlowUserSelection();
                userSel.setBelongTo(action.getId());
                userSel.setBelongEntity(FlowEntityType.FLOW_ACTION.getCode());
                userSel.setBelongType(FlowUserType.PROCESSOR.getCode());
                userSel.setFlowMainId(action.getFlowMainId());
                userSel.setFlowVersion(action.getFlowVersion());
                userSel.setNamespaceId(action.getNamespaceId());
                if (userSel.getOrganizationId() == null) {
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
        if (flow == null || !flow.getFlowMainId().equals(0L)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flowId not exists");
        }

        TrueOrFalseFlag needEvaluate = TrueOrFalseFlag.fromCode(cmd.getNeedEvaluate());
        // 开启评论的时候，评论项至少1个，至多5个
        if (needEvaluate == TrueOrFalseFlag.TRUE) {
            if (cmd.getItems() == null || cmd.getItems().size() < 1 || cmd.getItems().size() > 5) {
                LOGGER.error("items size error");
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_EVALUATE_ITEM_SIZE_ERROR,
                        "items size error!");
            }
        } else {
            cmd.setItems(null);
        }

        // FlowNode node1 = flowNodeProvider.getFlowNodeById(cmd.getEvaluateStart());
        // FlowNode node2 = flowNodeProvider.getFlowNodeById(cmd.getEvaluateEnd());

        flow.setEvaluateStart(cmd.getEvaluateStart());
        flow.setEvaluateEnd(cmd.getEvaluateEnd());
        flow.setEvaluateStep(cmd.getEvaluateStep());
        flow.setNeedEvaluate(cmd.getNeedEvaluate());

        this.dbProvider.execute(status -> {
            flowMarkUpdated(flow);

            List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(
                    flow.getId(), FlowConstants.FLOW_CONFIG_VER);
            if (items != null && items.size() > 0) {
                flowEvaluateItemProvider.deleteFlowEvaluateItem(items);
            }

            items = new ArrayList<>();
            if (cmd.getItems() != null) {
                for (FlowEvaluateItemDTO dto : cmd.getItems()) {
                    FlowEvaluateItem item = new FlowEvaluateItem();
                    item.setFlowMainId(flow.getId());
                    item.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
                    item.setName(dto.getName());
                    item.setNamespaceId(flow.getNamespaceId());
                    item.setInputFlag(dto.getInputFlag());
                    items.add(item);
                }
            }

            if (items.size() > 0) {
                flowEvaluateItemProvider.createFlowEvaluateItem(items);
            }

            if (cmd.getMessageAction() != null) {
                createEvaluateAction(flow, FlowConstants.FLOW_CONFIG_VER, cmd.getMessageAction(),
                        FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_NONE.getCode(), FlowStepType.EVALUATE_STEP.getCode());
            }
            if (cmd.getSmsAction() != null) {
                createEvaluateAction(flow, FlowConstants.FLOW_CONFIG_VER, cmd.getSmsAction(),
                        FlowActionType.SMS.getCode(), FlowActionStepType.STEP_NONE.getCode(), FlowStepType.EVALUATE_STEP.getCode());
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
        // dto.setItems(new ArrayList<>());

        FlowAction action = flowActionProvider.findFlowActionByBelong(flow.getId(), FlowEntityType.FLOW_EVALUATE.getCode()
                , FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_NONE.getCode(), null);
        if (action != null) {
            dto.setMessageAction(actionToDTO(action));
        }

        action = flowActionProvider.findFlowActionByBelong(flow.getId(), FlowEntityType.FLOW_EVALUATE.getCode()
                , FlowActionType.SMS.getCode(), FlowActionStepType.STEP_NONE.getCode(), null);
        if (action != null) {
            dto.setMessageAction(actionToDTO(action));
        }

        List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
        if (items != null && items.size() > 0) {
            List<FlowEvaluateItemDTO> itemDTOList = items.parallelStream().map(item -> {
                FlowEvaluateItemDTO itemDTO = new FlowEvaluateItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setName(item.getName());
                itemDTO.setInputFlag(item.getInputFlag());
                return itemDTO;
            }).collect(Collectors.toList());

            dto.setItems(itemDTOList);
        }

        return dto;
    }

    @Override
    public FlowEvaluateDetailDTO getFlowEvaluate(Long flowId) {
        Flow flow = flowProvider.getFlowById(flowId);
        if (flow == null || !flow.getFlowMainId().equals(0L)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flowId not exists");
        }
        return getFlowEvaluate(flow);
    }

    @Override
    public FlowEvaluateDTO getEvaluateInfo(Long flowCaseId) {
        FlowEvaluateDTO dto = new FlowEvaluateDTO();
        dto.setFlowCaseId(flowCaseId);
        List<FlowEvaluateResultDTO> results = new ArrayList<>();
        dto.setResults(results);

        FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);

        dto.setNamespaceId(flowCase.getNamespaceId());

        List<FlowEvaluate> evas = flowEvaluateProvider.findEvaluates(flowCaseId, flowCase.getFlowMainId(), flowCase.getFlowVersion());
        Map<Long, FlowEvaluate> evaMap = new HashMap<>();
        if (evas != null && evas.size() > 0) {
            evas.forEach(ev -> evaMap.put(ev.getEvaluateItemId(), ev));
        }

        List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        for (FlowEvaluateItem item : items) {
            FlowEvaluateResultDTO rltDTO = new FlowEvaluateResultDTO();
            rltDTO.setEvaluateItemId(item.getId());
            rltDTO.setName(item.getName());
            rltDTO.setInputFlag(item.getInputFlag());

            if (evaMap.containsKey(item.getId())) {
                FlowEvaluate eva = evaMap.get(item.getId());
                rltDTO.setStar(eva.getStar());
                rltDTO.setContent(eva.getContent());
            }
            results.add(rltDTO);
        }

        dto.setHasResults((byte) 0);
        if (items.size() != 0 && items.size() == evaMap.size()) {
            dto.setHasResults((byte) 1);
        }

        return dto;
    }

    @Override
    public ListScriptsResponse listScripts(ListScriptsCommand cmd) {
        ListScriptsResponse resp = new ListScriptsResponse();
        List<FlowScriptDTO> scripts = new ArrayList<>();
        resp.setScripts(scripts);

        if (cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }

        FlowEntityType entityType = FlowEntityType.fromCode(cmd.getEntityType());
        if (entityType == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR, "flow params error");
        }
        Flow flow = getFlowByEntity(cmd.getEntityId(), entityType);
        if (flow == null) {
            return resp;
        }

        List<FlowScript> scs = flowScriptProvider.findFlowScriptByModuleId(flow.getModuleId(), flow.getModuleType());
        if (scs != null && scs.size() > 0) {
            scs.forEach(s -> {
                FlowScriptDTO dto = ConvertHelper.convert(s, FlowScriptDTO.class);
                scripts.add(dto);
            });
        }

        return resp;
    }

    @Override
    public FlowSMSTemplateResponse listSMSTemplates(ListSMSTemplateCommand cmd) {
        if (cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }

        FlowEntityType entityType = FlowEntityType.fromCode(cmd.getEntityType());
        if (entityType == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR,
                    "flow params error");
        }

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        FlowSMSTemplateResponse resp = new FlowSMSTemplateResponse();
        Flow flow = getFlowByEntity(cmd.getEntityId(), entityType);
        if (flow == null) {
            return resp;
        }

        String scope = "flow:" + String.valueOf(flow.getModuleId()) + "%";

        User user = UserContext.current().getUser();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (user != null) {
            locale = user.getLocale();
        }

        List<FlowSMSTemplateDTO> dtos = new ArrayList<>();
        resp.setDtos(dtos);

        List<LocaleTemplate> templates = localeTemplateProvider.listLocaleTemplatesByScope(locator, cmd.getNamespaceId(), scope, locale, cmd.getKeyword(), count);
        resp.setNextPageAnchor(locator.getAnchor());
        if (templates != null) {
            templates.forEach(t -> {
                dtos.add(ConvertHelper.convert(t, FlowSMSTemplateDTO.class));
            });
        }

        return resp;
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

        if (users != null && users.size() > 0) {
            users.forEach((u) -> {
                UserInfo ui = userService.getUserSnapshotInfoWithPhone(u);
                infos.add(ConvertHelper.convert(ui, UserInfo.class));
            });
        }

        return resp;
    }

    @Override
    public ListSelectUsersResponse listUserSelections(ListSelectUsersCommand cmd) {
        FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
        stepDTO.setFlowCaseId(cmd.getFlowCaseId());
        stepDTO.setOperatorId(UserContext.current().getUser().getId());
        FlowCaseState ctx = flowStateProcessor.prepareNoStep(stepDTO);
        ctx.pushProcessType(FlowCaseStateStackType.NO_STEP_PROCESS);
        List<FlowUserSelection> selections = new ArrayList<FlowUserSelection>();
        FlowUserSelection ul = flowUserSelectionProvider.getFlowUserSelectionById(cmd.getEntityId());
        FlowCase fc = ctx.getFlowCase();
        ListSelectUsersResponse resp = new ListSelectUsersResponse();
        resp.setUsers(new ArrayList<UserInfo>());

        if (ul != null && fc != null) {
            selections.add(ul);
            List<Long> users = resolvUserSelections(ctx, FlowEntityType.FLOW_NODE, ctx.getFlowCase().getCurrentNodeId(), selections);
            for (Long u : users) {
                UserInfo ui = userService.getUserSnapshotInfo(u);
                if (ui != null) {
                    OrganizationMember om = organizationProvider.findOrganizationMemberByOrgIdAndUId(u, ul.getOrganizationId());
                    if (om != null && om.getContactName() != null && !om.getContactName().isEmpty()) {
                        ui.setNickName(om.getContactName());
                    }
                    resp.getUsers().add(ui);
                }
            }
        }
        ctx.popProcessType();

        return resp;
    }

    /**
     * 获取当前节点的所有用户信息
     * @param ctx
     * @return
     */
    @Override
    public List<UserInfo> listUserSelectionsByNode(FlowCaseState ctx, Long nodeId) {
        List<UserInfo> userSels = new ArrayList<UserInfo>();
        List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(nodeId
                , FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());
        if (selections == null || selections.size() == 0) {
            return userSels;
        }
        List<Long> users = resolvUserSelections(ctx, new HashMap<String, Long>(), FlowEntityType.FLOW_NODE
                , nodeId, selections, 1, 3);
        for (Long u : users) {
            UserInfo ui = userService.getUserSnapshotInfoWithPhone(u);
            if (ui != null) {
                fixupUserInfoInContext(ctx, ui);
                userSels.add(ui);
            }
        }

        return userSels;
    }

    /**
     * 上一个节点的执行人，也就是进入上个节点的执行者
     * @param ctx
     * @param prefixNodeId
     * @return
     */
    @Override
    public UserInfo getPrefixProcessor(FlowCaseState ctx, Long prefixNodeId) {
        FlowEventLog log = flowEventLogProvider.findPefixFireLog(ctx.getFlowCase().getCurrentNodeId(), prefixNodeId
                , ctx.getFlowCase().getId(), ctx.getFlowCase().getStepCount());
        UserInfo ui = null;
        if (log != null && log.getFlowUserId() != null && !log.getFlowUserId().equals(0L)) {
            ui = userService.getUserSnapshotInfoWithPhone(log.getFlowUserId());
            if (ui != null) {
                fixupUserInfoInContext(ctx, ui);
            }
        }

        return ui;
    }

    /**
     * 当前执行人，进入这个节点之前的执行人
     */
    @Override
    public UserInfo getCurrProcessor(FlowCaseState ctx, String variable) {
        UserInfo ui = ctx.getOperator();
        if (ui != null) {
           fixupUserInfoInContext(ctx, ui);
        }
        return ui;
    }

    /**
     * 申请人
     */
    @Override
    public UserInfo getUserInfoInContext(FlowCaseState ctx, Long userId) {
        UserInfo ui = userService.getUserSnapshotInfoWithPhone(userId);
        if (ui != null) {
            fixupUserInfoInContext(ctx, ui);
        }
        return ui;
    }

    @Override
    public List<Long> getApplierSelection(FlowCaseState ctx, FlowUserSelection sel) {
        List<Long> users = new ArrayList<>();
        UserInfo userInfo = getUserInfoInContext(ctx, ctx.getFlowCase().getApplyUserId());
        if (null != userInfo) {
            users.add(userInfo.getId());
        }
        return users;
    }

    @Override
    public void fixupUserInfoInContext(FlowCaseState ctx, UserInfo ui) {
        OrganizationMember om = organizationProvider.findOrganizationMemberByOrgIdAndUId(ui.getId(), ctx.getFlowGraph().getFlow().getOrganizationId());
        if (om != null && om.getContactName() != null && !om.getContactName().isEmpty()) {
            ui.setNickName(om.getContactName());
        }
    }

    @Override
    public void deleteFlowCase(DeleteFlowCaseCommand cmd) {
        if (cmd.getFlowCaseId() == null) {
            LOGGER.error("invalid parameter flowCaseId = {}", cmd.getFlowCaseId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "invalid parameter flowCaseId = %s", cmd.getFlowCaseId());
        }
        coordinationProvider.getNamedLock(CoordinationLocks.FLOW_CASE_UPDATE.getCode() + cmd.getFlowCaseId()).enter(() -> {
            FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
            if (flowCase == null) {
                LOGGER.error("flowCase not exist flowCaseId = {}", cmd.getFlowCaseId());
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS,
                        "flowCase not exist flowCaseId = %s", cmd.getFlowCaseId());
            }

            Long currUid = UserContext.current().getUser().getId();
            if (Objects.equals(currUid, flowCase.getApplyUserId())) {
                FlowCaseStatus status = FlowCaseStatus.fromCode(flowCase.getStatus());
                if (status != FlowCaseStatus.INVALID &&
                        (status == FlowCaseStatus.ABSORTED || status == FlowCaseStatus.FINISHED)) {
                    flowCase.setStatus(FlowCaseStatus.INVALID.getCode());
                    flowCaseProvider.updateFlowCase(flowCase);
                    return true;
                }
            }
            return false;
        });
    }

//	@Override
//    public List<FlowEventLog> getAllPrefixSteps(FlowCaseState ctx) {
//		flowEventLogProvider
//	}

}
