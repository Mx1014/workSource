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
import com.everhomes.flow.node.FlowGraphNodeCondition;
import com.everhomes.flow.node.FlowGraphNodeEnd;
import com.everhomes.flow.node.FlowGraphNodeNormal;
import com.everhomes.flow.node.FlowGraphNodeStart;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.namespace.Namespace;
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
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private FlowPredefinedParamProvider flowPredefinedParamProvider;

    @Autowired
    private FlowLinkProvider flowLinkProvider;

    @Autowired
    private FlowConditionProvider flowConditionProvider;

    @Autowired
    private FlowConditionExpressionProvider flowConditionExpressionProvider;

    @Autowired
    private FlowBranchProvider flowBranchProvider;

    @Autowired
    private FlowLaneProvider flowLaneProvider;

    @Autowired
    private FlowServiceTypeProvider flowServiceTypeProvider;

    @Autowired
    private LocaleStringService localeStringService;

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
        obj.setValidationStatus(FlowValidationStatus.UNKNOWN.getCode());
        obj.setStatus(FlowStatusType.CONFIG.getCode());
        obj.setOrganizationId(cmd.getOrgId());
        obj.setNamespaceId(cmd.getNamespaceId());
        obj.setProjectType(cmd.getProjectType() != null ? cmd.getProjectType() : EntityType.COMMUNITY.getCode());
        obj.setProjectId(cmd.getProjectId() != null ? cmd.getProjectId() : 0L);
        obj.setStringTag1(cmd.getStringTag1());
        flowListenerManager.onFlowCreating(obj);

        Flow resultObj = this.dbProvider.execute(status -> {
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
        if (flow != null && !flow.getId().equals(cmd.getFlowId())) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NAME_EXISTS, "flow name exists");
        }

        oldFlow.setFlowName(cmd.getNewFlowName());
        oldFlow.setDescription(cmd.getNewFlowDesc());
        flowProvider.updateFlow(oldFlow);

        return ConvertHelper.convert(oldFlow, FlowDTO.class);
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
        if (flow == null || flow.getStatus().equals(FlowStatusType.INVALID.getCode()) || !flow.getFlowMainId().equals(0L)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS, "flowId not exists");
        }

        FlowNode flowNode = this.dbProvider.execute(status -> {
            FlowNode nodeObj;
            if (cmd.getId() != null) {
                nodeObj = flowNodeProvider.getFlowNodeById(cmd.getId());
                if (nodeObj == null) {
                    nodeObj = new FlowNode();
                }
            } else {
                nodeObj = new FlowNode();
            }

            //step2 create node
            nodeObj.setNodeName(cmd.getNodeName());
            nodeObj.setFlowMainId(flow.getId());
            nodeObj.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
            nodeObj.setNamespaceId(flow.getNamespaceId());
            nodeObj.setStatus(FlowNodeStatus.VISIBLE.getCode());
            nodeObj.setNodeLevel(cmd.getNodeLevel());
            nodeObj.setNodeType(cmd.getNodeType());
            nodeObj.setFlowLaneLevel(cmd.getFlowLaneLevel());
            nodeObj.setFlowLaneId(cmd.getFlowLaneId());

            // 已经存在的node，更新即可
            if (nodeObj.getId() != null) {
                flowNodeProvider.updateFlowNode(nodeObj);
            } else {
                nodeObj.setGotoProcessButtonName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.GO_TO_PROCESS));
                nodeObj.setParams("");
                nodeObj.setNeedAllProcessorComplete(TrueOrFalseFlag.FALSE.getCode());

                // 不存在的node, 创建
                flowNodeProvider.createFlowNode(nodeObj);
                createDefaultButtons(flow, nodeObj);
            }
            return nodeObj;
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
        // 特殊节点没有按钮
        FlowNodeType nodeType = FlowNodeType.fromCode(flowNode.getNodeType());
        if (nodeType == FlowNodeType.START
                || nodeType == FlowNodeType.END
                || nodeType == FlowNodeType.CONDITION_FRONT
                || nodeType == FlowNodeType.CONDITION_BACK) {
            return;
        }
        FlowStepType[] steps = {FlowStepType.APPROVE_STEP, FlowStepType.REJECT_STEP,
                FlowStepType.TRANSFER_STEP, FlowStepType.COMMENT_STEP, FlowStepType.ABSORT_STEP};

        for (int i = 0; i < steps.length; i++) {
            int defaultOrder = i == 0 ? 10 : (i + 1) * 10;
            createDefButton(flow, flowNode, FlowUserType.PROCESSOR, steps[i], FlowButtonStatus.ENABLED, defaultOrder);
        }

        FlowStepType[] steps2 = {FlowStepType.REMINDER_STEP, FlowStepType.COMMENT_STEP, FlowStepType.ABSORT_STEP};
        for (int i = 0; i < steps2.length; i++) {
            int defaultOrder = i == 0 ? 10 : (i + 1) * 10;
            createDefButton(flow, flowNode, FlowUserType.APPLIER, steps2[i], FlowButtonStatus.ENABLED, defaultOrder);
        }

        createDefButton(flow, flowNode, FlowUserType.APPLIER, FlowStepType.EVALUATE_STEP, FlowButtonStatus.DISABLED, (steps2.length + 1) * 10);
    }

    private FlowButtonDTO createDefButton(Flow flow, FlowNode flowNode, FlowUserType userType, FlowStepType stepType, FlowButtonStatus enabled, Integer defaultOrder) {
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
        button.setDefaultOrder(defaultOrder);
        if (stepType == FlowStepType.COMMENT_STEP) {
            button.setSubjectRequiredFlag(TrueOrFalseFlag.TRUE.getCode());
        } else {
            button.setSubjectRequiredFlag(TrueOrFalseFlag.FALSE.getCode());
        }
        if (stepType == FlowStepType.REMINDER_STEP) {
            button.setRemindCount(1);
        }
        if (stepType == FlowStepType.TRANSFER_STEP) {
            button.setNeedProcessor((byte) 1);
        }
        if (stepType == FlowStepType.EVALUATE_STEP) {
            button.setEvaluateStep(FlowStepType.APPROVE_STEP.getCode());
        }

        flowButtonProvider.createFlowButton(button);

        if (stepType == FlowStepType.REMINDER_STEP) {
            // 催办按钮的默认催办消息
            FlowActionInfo actionInfo = new FlowActionInfo();
            actionInfo.setEnabled(FlowActionStatus.ENABLED.getCode());
            actionInfo.setRenderText(getLocalString(FlowTemplateCode.DEFAULT_REMIND_MESSAGE_TEXT));

            CreateFlowUserSelectionCommand selectionCmd = new CreateFlowUserSelectionCommand();
            ArrayList<FlowSingleUserSelectionCommand> singleSel = new ArrayList<>();
            FlowSingleUserSelectionCommand singleCmd = new FlowSingleUserSelectionCommand();
            singleCmd.setFlowUserSelectionType(FlowUserSelectionType.VARIABLE.getCode());
            singleCmd.setSourceTypeA(FlowUserSourceType.SOURCE_VARIABLE.getCode());

            List<FlowVariable> currentProcessor = flowVariableProvider.findVariables(0, 0L,
                    null, 0L, null, FlowVariableUserResolver.ALL_CURRENT_NODE_PROCESSORS,
                    "hidden");

            if (currentProcessor != null && currentProcessor.size() > 0) {
                singleCmd.setSourceIdA(currentProcessor.get(0).getId());
                singleCmd.setSelectionName(currentProcessor.get(0).getLabel());
            } else {
                LOGGER.error("Can not find all_current_node_processors for remind button!");
            }

            singleSel.add(singleCmd);
            selectionCmd.setSelections(singleSel);
            actionInfo.setUserSelections(selectionCmd);

            createButtonAction(button, actionInfo, FlowActionType.REMIND_MSG.getCode()
                    , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        }
        if (stepType == FlowStepType.APPROVE_STEP) {
            FlowActionInfo actionInfo = new FlowActionInfo();
            actionInfo.setEnabled(FlowActionStatus.ENABLED.getCode());
            actionInfo.setTrackerApplier(1L);
            actionInfo.setTrackerProcessor(1L);
            actionInfo.setRenderText(getLocalString(FlowTemplateCode.DEFAULT_APPROVAL_BUTTON_TRACK_TEXT));
            createButtonAction(button, actionInfo, FlowActionType.TRACK.getCode()
                    , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        }
        if (stepType == FlowStepType.REJECT_STEP) {
            FlowActionInfo actionInfo = new FlowActionInfo();
            actionInfo.setEnabled(FlowActionStatus.ENABLED.getCode());
            actionInfo.setTrackerApplier(1L);
            actionInfo.setTrackerProcessor(1L);
            actionInfo.setRenderText(getLocalString(FlowTemplateCode.DEFAULT_REJECT_BUTTON_TRACK_TEXT));
            createButtonAction(button, actionInfo, FlowActionType.TRACK.getCode()
                    , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        }
        return ConvertHelper.convert(button, FlowButtonDTO.class);
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

        applierButtonList.sort(Comparator.comparingInt(FlowButton::getDefaultOrder));

        List<FlowButtonDTO> applierBtn = applierButtonList.stream()
                .map(r -> ConvertHelper.convert(r, FlowButtonDTO.class))
                .collect(Collectors.toList());

        List<FlowButton> processorButtonList = flowButtonProvider.findFlowButtonsByUserType(flowNodeId,
                FlowConstants.FLOW_CONFIG_VER, FlowUserType.PROCESSOR.getCode());

        processorButtonList.sort(Comparator.comparingInt(FlowButton::getDefaultOrder));

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

        List<FlowUserSelectionDTO> processorList = selections.parallelStream()
                .map(r -> ConvertHelper.convert(r, FlowUserSelectionDTO.class))
                .collect(Collectors.toList());

        detail.setProcessors(processorList);

        detail.setReminder(getReminderDTO(flowNodeId));
        detail.setTracker(getTrackerDTO(flowNodeId));

        if (FlowNodeType.fromCode(flowNode.getNodeType()) == FlowNodeType.CONDITION_FRONT) {
            FlowBranch branch = flowBranchProvider.findBranch(flowNode.getId());
            detail.setBranch(toFlowBranchDTO(branch));
        }
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
            dto.setEnterTracker(actionToDTO(action));
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.REJECT_STEP.getCode());
        if (action != null) {
            dto.setRejectTracker(actionToDTO(action));
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_LEAVE.getCode(), FlowStepType.TRANSFER_STEP.getCode());
        if (action != null) {
            dto.setTransferTracker(actionToDTO(action));
        }
        return dto;
    }

    private FlowActionDTO actionToDTO(FlowAction action) {
        FlowActionDTO actionDTO = ConvertHelper.convert(action, FlowActionDTO.class);
        // if (actionDTO.getStatus() != null && actionDTO.getStatus().equals(FlowActionStatus.ENABLED.getCode())) {
        //     actionDTO.setEnabled((byte) 1);
        // }

        actionDTO.setEnabled(action.getStatus());

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

        if (cmd.getFlowNodeId() == null) {
            return null;
        }

        Tuple<FlowNode, Boolean> tuple = coordinationProvider.getNamedLock(CoordinationLocks.FLOW_NODE_UPDATE.getCode() + cmd.getFlowNodeId()).enter(() -> {
            FlowNode flowNode = flowNodeProvider.getFlowNodeById(cmd.getFlowNodeId());
            if (flowNode != null) {
                if (cmd.getFlowNodeName() != null) {
                    flowNode.setNodeName(cmd.getFlowNodeName());
                }
                if (cmd.getAllowApplierUpdate() != null) {
                    // flowNode.setAllowApplierUpdate(cmd.getAllowApplierUpdate());
                }
                flowNode.setAllowApplierUpdate(TrueOrFalseFlag.FALSE.getCode());
                if (cmd.getAllowTimeoutAction() != null) {
                    flowNode.setAllowTimeoutAction(cmd.getAllowTimeoutAction());
                }
                if (cmd.getParams() != null) {
                    flowNode.setParams(cmd.getParams());
                }
                if (cmd.getAutoStepMinute() != null) {
                    flowNode.setAutoStepMinute(cmd.getAutoStepMinute());
                }
                if (cmd.getAutoStepType() != null) {
                    flowNode.setAutoStepType(cmd.getAutoStepType());
                }
                if (cmd.getGotoProcessButtonName() != null) {
                    flowNode.setGotoProcessButtonName(cmd.getGotoProcessButtonName());
                }
                if (cmd.getNeedAllProcessorComplete() != null) {
                    flowNode.setNeedAllProcessorComplete(cmd.getNeedAllProcessorComplete());
                }
                flowNodeProvider.updateFlowNode(flowNode);
            }
            return flowNode;
        });
        return ConvertHelper.convert(tuple.first(), FlowNodeDTO.class);
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
        if (cmd.getEvaluateStep() != null) {
            flowButton.setEvaluateStep(cmd.getEvaluateStep());
        }
        if (cmd.getParam() != null) {
            flowButton.setParam(cmd.getParam());
        }

        flowButtonProvider.updateFlowButton(flowButton);

        if (null != cmd.getMessageAction()) {
            dbProvider.execute((a) -> {
                return createButtonAction(flowButton, cmd.getMessageAction(), FlowActionType.MESSAGE.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
            });
        }
        if (null != cmd.getRemindMsgAction()) {
            dbProvider.execute((a) -> {
                return createButtonAction(flowButton, cmd.getRemindMsgAction(), FlowActionType.REMIND_MSG.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
            });
        }
        if (null != cmd.getSmsAction()) {
            dbProvider.execute((a) -> {
                return createButtonAction(flowButton, cmd.getSmsAction(), FlowActionType.SMS.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
            });
        }
        if (null != cmd.getTracker()) {
            dbProvider.execute((a) -> {
                return createButtonAction(flowButton, cmd.getTracker(), FlowActionType.TRACK.getCode()
                        , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
            });
        }

        if (null != cmd.getEnterScriptId()) {
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
            action.setScriptId(cmd.getEnterScriptId());
            flowActionProvider.createFlowAction(action);
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
            action.setStatus(actionInfo.getEnabled() != null ? actionInfo.getEnabled() : FlowActionStatus.ENABLED.getCode());
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
            action.setStatus(actionInfo.getEnabled());
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
                , FlowActionType.REMIND_MSG.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        if (action != null) {
            dto.setRemindMsg(actionToDTO(action));
        }
        action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.SMS.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        if (action != null) {
            dto.setPushSms(actionToDTO(action));
        }

        action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        if (action != null) {
            dto.setTracker(actionToDTO(action));
        }

        List<FlowAction> flowActions = flowActionProvider.findFlowActionsByBelong(flowButtonId, FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.ENTER_SCRIPT.getCode(), FlowActionStepType.STEP_ENTER.getCode(), null);

        List<FlowActionDTO> actionDTOS = new ArrayList<>();
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
        Flow flow = flowProvider.getFlowById(flowId);

        checkFlowValidationStatus(flow);

        // 避免同时启用工作流的问题
        String lockKey = String.format("%s:%s:%s:%s:%s:%s:%s:%s",
                CoordinationLocks.FLOW.getCode(), flow.getNamespaceId(), flow.getProjectType(), flow.getProjectId(),
                flow.getModuleType(), flow.getModuleId(), flow.getOwnerType(), flow.getOwnerId());

        Tuple<Boolean, Boolean> tuple = coordinationProvider.getNamedLock(lockKey).enter(() -> {
            // 查询看是否有原来已经开启的工作流
            Flow enabledFlow = flowProvider.getEnabledConfigFlow(flow.getNamespaceId(), flow.getModuleId(), flow.getModuleType(), flow.getOwnerId(), flow.getOwnerType());
            if (enabledFlow != null && !enabledFlow.getId().equals(flowId)) {
                dbProvider.execute(status -> {
                    enabledFlow.setStatus(FlowStatusType.STOP.getCode());
                    Timestamp now = DateUtils.currentTimestamp();
                    enabledFlow.setStopTime(now);
                    flow.setUpdateTime(now);
                    flowProvider.updateFlow(enabledFlow);

                    Flow snapshotFlow = flowProvider.getSnapshotFlow(enabledFlow.getId(), FlowStatusType.RUNNING.getCode());
                    snapshotFlow.setStatus(FlowStatusType.STOP.getCode());
                    snapshotFlow.setUpdateTime(now);
                    snapshotFlow.setRunTime(now);
                    flowProvider.updateFlow(snapshotFlow);
                    return true;
                });
            }

            // 如果configFlow只是STOP状态
            // 说明该工作流没有被改过，则不用创建snapshot版本，可以直接启用，并使用原来的snapshot版本
            if (flow.getStatus().equals(FlowStatusType.STOP.getCode())) {
                dbProvider.execute(status -> {
                    //restart it
                    flow.setStatus(FlowStatusType.RUNNING.getCode());
                    Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
                    flow.setUpdateTime(now);
                    flow.setRunTime(now);
                    flowProvider.updateFlow(flow);

                    Flow snapshotFlow = flowProvider.getSnapshotFlow(flowId, FlowStatusType.STOP.getCode());
                    snapshotFlow.setStatus(FlowStatusType.RUNNING.getCode());
                    snapshotFlow.setUpdateTime(now);
                    snapshotFlow.setRunTime(now);
                    flowProvider.updateFlow(snapshotFlow);
                    return true;
                });
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
            flowNodes.sort(Comparator.comparing(EhFlowNodes::getNodeLevel));

            // 老版本的工作流在数据库都没有开始和结束节点
            boolean hasStartNode = false;
            boolean hasEndNode = false;

            // int i = 1;
            for (FlowNode fn : flowNodes) {
            /*if (!fn.getNodeLevel().equals(i)) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE,
                        FlowServiceErrorCode.ERROR_FLOW_NODE_LEVEL_ERR, "node_level error");
            }
            i++;*/
                // 老版本的工作流在数据库都没有开始和结束节点
                if (FlowNodeType.START.getCode().equals(fn.getNodeType())) hasStartNode = true;
                if (FlowNodeType.END.getCode().equals(fn.getNodeType())) hasEndNode = true;
            }

            final FlowGraph flowGraph = new FlowGraph();
            flowGraph.setFlow(flow);

            FlowNode nodeObj;
            if (!hasStartNode) {
                // 开始节点snapshot
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
            }

            // 节点snapshot
            flowNodes.forEach((fn) -> {
                if (fn.getNodeName().equals("START")) {
                    flowGraph.getNodes().add(new FlowGraphNodeStart(fn));
                } else if (fn.getNodeName().equals("END")) {
                    flowGraph.getNodes().add(new FlowGraphNodeEnd(fn));
                } else if (fn.getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {
                    flowGraph.getNodes().add(getFlowGraphConditionNode(fn, FlowConstants.FLOW_CONFIG_VER));
                } else {
                    flowGraph.getNodes().add(getFlowGraphNode(fn, FlowConstants.FLOW_CONFIG_VER));
                }
            });

            if (!hasEndNode) {
                // 结束节点snapshot
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
            }

            // 泳道
            List<FlowLane> laneList = flowLaneProvider.listFlowLane(flowId, FlowConstants.FLOW_CONFIG_VER);
            laneList.forEach(r -> flowGraph.getLanes().add(getFlowGraphLane(r)));
            // 分支
            List<FlowBranch> branchList = flowBranchProvider.findByFlowId(flowId, FlowConstants.FLOW_CONFIG_VER);
            branchList.forEach(r -> flowGraph.getBranches().add(getFlowGraphBranch(r)));

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

            if (flow.getFlowMainId().equals(0L)) {
                isOk = false;
            }

            if (isOk) {
                //running now
                flow.setId(flow.getFlowMainId());
                flow.setFlowMainId(0L);
                flow.setRunTime(now);
                flow.setStatus(FlowStatusType.RUNNING.getCode());
                flowProvider.updateFlow(flow);
            }

            return isOk;
        });
        return tuple.first();
    }

    private void checkFlowValidationStatus(Flow flow) {
        FlowValidationStatus validationStatus = FlowValidationStatus.fromCode(flow.getValidationStatus());
        switch (validationStatus) {
            case INVALID:
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_VALIDATION_INVALID,
                        "Flow validation invalid");
            case VALID:
                break;
        }
    }

    private FlowGraphBranch getFlowGraphBranch(FlowBranch branch) {
        FlowGraphBranch graphBranch = new FlowGraphBranchNormal();
        graphBranch.setFlowBranch(branch);
        return graphBranch;
    }

    private FlowGraphLane getFlowGraphLane(FlowLane flowLane) {
        FlowGraphLane graphLane = new FlowGraphLane();
        graphLane.setFlowLane(flowLane);
        return graphLane;
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
            graphAction = new FlowGraphSMSAction(action.getReminderAfterMinute(), action.getReminderTickMinute(), 1L);
            graphAction.setFlowAction(action);
            graphNode.setSmsAction(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TICK_MESSAGE.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
        if (action != null) {
            graphAction = new FlowGraphMessageAction(action.getReminderAfterMinute(), action.getReminderTickMinute(), 50L);
            graphAction.setFlowAction(action);
            graphNode.setTickMessageAction(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowNodeId, FlowEntityType.FLOW_NODE.getCode()
                , FlowActionType.TICK_SMS.getCode(), FlowActionStepType.STEP_TIMEOUT.getCode(), null);
        if (action != null) {
            graphAction = new FlowGraphSMSAction(action.getReminderAfterMinute(), action.getReminderTickMinute(), 1L);
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

        List<FlowButton> supervisorButtons = flowButtonProvider.findFlowButtonsByUserType(flowNode.getFlowMainId(), 0L, flowVersion, FlowUserType.SUPERVISOR.getCode());
        supervisorButtons.forEach((btn) -> {
            graphNode.getSupervisorButtons().add(getFlowGraphButton(btn));
        });

        List<FlowLink> linksIn = flowLinkProvider.listFlowLinkByToNodeId(flowNodeId, flowVersion);
        for (FlowLink link : linksIn) {
            FlowGraphLink graphLink = new FlowGraphLinkNormal();
            graphLink.setFlowLink(link);
            graphNode.getLinksIn().add(graphLink);
        }

        List<FlowLink> linksOut = flowLinkProvider.listFlowLinkByFromNodeId(flowNodeId, flowVersion);
        for (FlowLink link : linksOut) {
            FlowGraphLink graphLink = new FlowGraphLinkNormal();
            graphLink.setFlowLink(link);
            graphNode.getLinksOut().add(graphLink);
        }
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
                , FlowActionType.REMIND_MSG.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        if (action != null) {
            graphAction = new FlowGraphMessageAction();
            graphAction.setFlowAction(action);
            graphBtn.setRemindMsg(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.SMS.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        if (action != null) {
            graphAction = new FlowGraphSMSAction();
            graphAction.setFlowAction(action);
            graphBtn.setSms(graphAction);
        }

        action = flowActionProvider.findFlowActionByBelong(flowButton.getId(), FlowEntityType.FLOW_BUTTON.getCode()
                , FlowActionType.TRACK.getCode(), FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());
        if (action != null) {
            graphAction = new FlowGraphTrackerAction();
            graphAction.setFlowAction(action);
            graphBtn.setTracker(graphAction);
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

        // 新版本把旧的评价的起始节点配置去掉，避免影响评价按钮出现的时机
        flow.setEvaluateStart(0L);
        flow.setEvaluateEnd(0L);

        flowProvider.createFlow(flow);

        //step2 create flowNodes
        Map<Long, Long> configNodeIdToSnapshotNodeIdMap = new HashMap<>();
        for (FlowGraphNode node : flowGraph.getNodes()) {
            FlowNode flowNode = node.getFlowNode();
            Long oldFlowNodeId = flowNode.getId();
            flowNode.setId(null);
            flowNode.setFlowMainId(flow.getFlowMainId());
            flowNode.setFlowVersion(flow.getFlowVersion());
            flowNodeProvider.createFlowNode(flowNode);

            configNodeIdToSnapshotNodeIdMap.put(oldFlowNodeId, flowNode.getId());

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

        Map<Long, Long> configLaneIdToSnapshotLaneIdMap = new HashMap<>();
        // 泳道
        for (FlowGraphLane lane : flowGraph.getLanes()) {
            Long configLaneId = lane.getFlowLane().getId();
            doSnapshotFlowLane(flow, lane, configNodeIdToSnapshotNodeIdMap.get(lane.getFlowLane().getIdentifierNodeId()));
            configLaneIdToSnapshotLaneIdMap.put(configLaneId, lane.getFlowLane().getId());
        }
        // 分支
        for (FlowGraphBranch branch : flowGraph.getBranches()) {
            Long originalNodeId = configNodeIdToSnapshotNodeIdMap.get(branch.getFlowBranch().getOriginalNodeId());
            Long convergenceNodeId = configNodeIdToSnapshotNodeIdMap.get(branch.getFlowBranch().getConvergenceNodeId());
            doSnapshotFlowBranch(flow, branch, originalNodeId, convergenceNodeId);
        }

        // 链接，条件
        for (FlowGraphNode node : flowGraph.getNodes()) {
            for (FlowGraphLink link : node.getLinksOut()) {
                Long fromNodeId = configNodeIdToSnapshotNodeIdMap.get(link.getFlowLink().getFromNodeId());
                Long toNodeId = configNodeIdToSnapshotNodeIdMap.get(link.getFlowLink().getToNodeId());
                doSnapshotLink(flow, link, toNodeId, fromNodeId);
            }
            for (FlowGraphCondition condition : node.getConditions()) {
                Long flowNodeId = configNodeIdToSnapshotNodeIdMap.get(condition.getCondition().getFlowNodeId());
                Long nextNodeId = configNodeIdToSnapshotNodeIdMap.get(condition.getCondition().getNextNodeId());
                doSnapshotCondition(flow, condition, flowNodeId, nextNodeId);
            }

            // 把泳道id修改成snapshotId
            FlowNode flowNode = node.getFlowNode();
            flowNode.setFlowLaneId(configLaneIdToSnapshotLaneIdMap.get(flowNode.getFlowLaneId()));
            flowNodeProvider.updateFlowNode(flowNode);
        }

        // 督办
        doSnapshotSupervisor(flow);

        //step9 copy flow's isTrue
        List<FlowEvaluateItem> items = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flow.getFlowMainId(), FlowConstants.FLOW_CONFIG_VER);
        if (items != null && items.size() > 0) {
            items.forEach(item -> {
                item.setId(null);
                item.setFlowMainId(flow.getFlowMainId());
                item.setFlowVersion(flow.getFlowVersion());
            });
            flowEvaluateItemProvider.createFlowEvaluateItem(items);

            //TODO support build for isTrue
        }

        flow.setStartNode(flowGraph.getNodes().get(0).getFlowNode().getId());
        flow.setEndNode(flowGraph.getNodes().get(flowGraph.getNodes().size() - 1).getFlowNode().getId());
        flowProvider.updateFlow(flow);
    }

    private void doSnapshotSupervisor(Flow flow) {
        FlowButton flowButton = new FlowButton();

        flowButton.setFlowMainId(flow.getFlowMainId());
        flowButton.setFlowVersion(flow.getFlowVersion());
        flowButton.setFlowNodeId(0L);
        flowButton.setButtonName(buttonDefName(flow.getNamespaceId(), FlowStepType.SUPERVISE));
        flowButton.setRemindCount(1);
        flowButton.setDefaultOrder(1);
        flowButton.setFlowStepType(FlowStepType.SUPERVISE.getCode());
        flowButton.setFlowUserType(FlowUserType.SUPERVISOR.getCode());
        flowButton.setNeedProcessor((byte) 0);
        flowButton.setNeedSubject((byte) 0);
        flowButton.setNamespaceId(flow.getNamespaceId());
        flowButton.setStatus(FlowButtonStatus.ENABLED.getCode());

        flowButtonProvider.createFlowButton(flowButton);

        FlowActionInfo actionInfo = new FlowActionInfo();
        actionInfo.setEnabled(FlowActionStatus.ENABLED.getCode());
        actionInfo.setRenderText(getLocalString(FlowTemplateCode.DEFAULT_SUPERVISE_MESSAGE_TEXT));

        CreateFlowUserSelectionCommand selectionCmd = new CreateFlowUserSelectionCommand();
        ArrayList<FlowSingleUserSelectionCommand> singleSel = new ArrayList<>();
        FlowSingleUserSelectionCommand singleCmd = new FlowSingleUserSelectionCommand();
        singleCmd.setFlowUserSelectionType(FlowUserSelectionType.VARIABLE.getCode());
        singleCmd.setSourceTypeA(FlowUserSourceType.SOURCE_VARIABLE.getCode());

        List<FlowVariable> currentProcessor = flowVariableProvider.findVariables(0, 0L,
                null, 0L, null, FlowVariableUserResolver.ALL_CURRENT_NODE_PROCESSORS,
                "hidden");

        if (currentProcessor != null && currentProcessor.size() > 0) {
            singleCmd.setSourceIdA(currentProcessor.get(0).getId());
            singleCmd.setSelectionName(currentProcessor.get(0).getLabel());
        } else {
            LOGGER.error("Can not find all_current_node_processors for remind button!");
        }

        singleSel.add(singleCmd);
        selectionCmd.setSelections(singleSel);
        actionInfo.setUserSelections(selectionCmd);

        createButtonAction(flowButton, actionInfo, FlowActionType.MESSAGE.getCode()
                , FlowActionStepType.STEP_ENTER.getCode(), FlowStepType.NO_STEP.getCode());

        //step8 copy flow's supervisor
        List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(flow.getFlowMainId()
                , FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode(), FlowConstants.FLOW_CONFIG_VER);
        if (selections != null && selections.size() > 0) {
            for (FlowUserSelection sel : selections) {
                // 这个副本是为了给flowCase创建督办人
                sel.setBelongTo(flow.getFlowMainId());
                sel.setFlowMainId(flow.getFlowMainId());
                sel.setFlowVersion(flow.getFlowVersion());
                flowUserSelectionProvider.createFlowUserSelection(sel);

                /*// 这个副本是为了关联flowAction
                sel.setBelongEntity(FlowEntityType.FLOW_ACTION.getCode());
                sel.setBelongTo(flowAction.getId());
                sel.setFlowMainId(flow.getFlowMainId());
                sel.setFlowVersion(flow.getFlowVersion());
                flowUserSelectionProvider.createFlowUserSelection(sel);*/
            }
        }
    }

    private void doSnapshotFlowBranch(Flow flow, FlowGraphBranch branch, Long originalNodeId, Long convergenceNodeId) {
        FlowBranch flowBranch = branch.getFlowBranch();
        flowBranch.setId(null);
        flowBranch.setFlowMainId(flow.getFlowMainId());
        flowBranch.setFlowVersion(flow.getFlowVersion());
        flowBranch.setOriginalNodeId(originalNodeId);
        flowBranch.setConvergenceNodeId(convergenceNodeId);
        flowBranchProvider.createFlowBranch(flowBranch);
    }

    private void doSnapshotCondition(Flow flow, FlowGraphCondition condition, Long flowNodeId, Long nextNodeId) {
        FlowCondition cond = condition.getCondition();
        cond.setId(null);
        cond.setFlowMainId(flow.getFlowMainId());
        cond.setFlowVersion(flow.getFlowVersion());
        cond.setNextNodeId(nextNodeId);
        cond.setFlowNodeId(flowNodeId);
        flowConditionProvider.createFlowCondition(cond);
        for (FlowConditionExpression expression : condition.getExpressions()) {
            doSnapshotConditionExpression(flow, cond, expression);
        }
    }

    private void doSnapshotConditionExpression(Flow flow, FlowCondition cond, FlowConditionExpression expression) {
        expression.setId(null);
        expression.setFlowConditionId(cond.getId());
        expression.setFlowMainId(flow.getFlowMainId());
        expression.setFlowVersion(flow.getFlowVersion());
        flowConditionExpressionProvider.createFlowConditionExpression(expression);
    }

    private void doSnapshotLink(Flow flow, FlowGraphLink link, Long toNodeId, Long fromNodeId) {
        FlowLink flowLink = link.getFlowLink();
        flowLink.setId(null);
        flowLink.setFlowMainId(flow.getFlowMainId());
        flowLink.setFlowVersion(flow.getFlowVersion());
        flowLink.setFromNodeId(fromNodeId);
        flowLink.setToNodeId(toNodeId);
        flowLinkProvider.createFlowLink(flowLink);
    }

    private void doSnapshotFlowLane(Flow flow, FlowGraphLane lane, Long identifierNodeId) {
        FlowLane flowLane = lane.getFlowLane();
        flowLane.setId(null);
        flowLane.setFlowMainId(flow.getFlowMainId());
        flowLane.setFlowVersion(flow.getFlowVersion());
        flowLane.setIdentifierNodeId(identifierNodeId);
        flowLaneProvider.createFlowLane(flowLane);
    }

    private void doSnapshotButton(Flow flow, FlowNode flowNode, FlowGraphButton button) {
        FlowButton flowButton = button.getFlowButton();

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
        doSnapshotAction(flow, flowButton.getId(), button.getRemindMsg());
        doSnapshotAction(flow, flowButton.getId(), button.getMessage());
        doSnapshotAction(flow, flowButton.getId(), button.getSms());
        doSnapshotAction(flow, flowButton.getId(), button.getTracker());
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
        // graphMap.clear();
        String fmt = String.format("%d:%d", flowId, flowVer);
        return graphMap.computeIfAbsent(fmt, k -> getSnapshotGraph(flowId, flowVer));
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
        flowNodes.sort(Comparator.comparing(EhFlowNodes::getNodeLevel));

        // int i = 1;
        for (FlowNode fn : flowNodes) {
            /*if (!fn.getNodeLevel().equals(i)) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_LEVEL_ERR, "node_level error flowId=" + flowId + " ");
            }*/

            if (fn.getNodeName().equals("START") || FlowNodeType.fromCode(fn.getNodeType()) == FlowNodeType.START) {
                FlowGraphNode startNode = getFlowGraphStartNode(fn, flowVer);
                flowGraph.getNodes().add(startNode);
                flowGraph.setStartNode(startNode);
            } else if (fn.getNodeName().equals("END") || FlowNodeType.fromCode(fn.getNodeType()) == FlowNodeType.END) {
                FlowGraphNode endNode = getFlowGraphEndNode(fn, flowVer);
                flowGraph.getNodes().add(endNode);
                flowGraph.setEndNode(endNode);
            } else if (fn.getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {
                flowGraph.getNodes().add(getFlowGraphConditionNode(fn, flowVer));
            } else {
                flowGraph.getNodes().add(getFlowGraphNode(fn, flowVer));
            }

            // i++;
        }

        // 泳道
        List<FlowLane> laneList = flowLaneProvider.listFlowLane(flowId, flowVer);
        if (laneList.size() != 0) {
            for (FlowLane lane : laneList) {
                FlowGraphLane graphLane = new FlowGraphLane();
                graphLane.setFlowLane(lane);
                flowGraph.getLanes().add(graphLane);
            }
        }

        // 分支
        List<FlowBranch> branchList = flowBranchProvider.findByFlowId(flowId, flowVer);
        for (FlowBranch flowBranch : branchList) {
            flowGraph.getBranches().add(getFlowGraphBranch(flowBranch));
        }

        flowGraphCompatible(flow, flowGraph);

        flowGraph.saveIds();

        return flowGraph;
    }

    private FlowGraphNode getFlowGraphEndNode(FlowNode fn, Integer flowVersion) {
        FlowGraphNodeEnd nodeEnd = new FlowGraphNodeEnd(fn);
        List<FlowLink> linksIn = flowLinkProvider.listFlowLinkByToNodeId(fn.getId(), flowVersion);
        for (FlowLink link : linksIn) {
            FlowGraphLink graphLink = new FlowGraphLinkNormal();
            graphLink.setFlowLink(link);
            nodeEnd.getLinksIn().add(graphLink);
        }
        return nodeEnd;
    }

    private FlowGraphNode getFlowGraphStartNode(FlowNode fn, Integer flowVersion) {
        FlowGraphNodeStart nodeStart = new FlowGraphNodeStart(fn);
        List<FlowLink> linksOut = flowLinkProvider.listFlowLinkByFromNodeId(fn.getId(), flowVersion);
        for (FlowLink link : linksOut) {
            FlowGraphLink graphLink = new FlowGraphLinkNormal();
            graphLink.setFlowLink(link);
            nodeStart.getLinksOut().add(graphLink);
        }
        return nodeStart;
    }

    // 兼容flow-2.0之前
    private void flowGraphCompatible(Flow flow, FlowGraph flowGraph) {
        if (flowGraph.getLanes().size() == 0) {
            List<FlowGraphNode> nodes = flowGraph.getNodes();
            nodes.sort(Comparator.comparingInt(r -> r.getFlowNode().getNodeLevel()));

            FlowGraphLinkNormal prefixLink = null;
            int linkLevel = 0;
            for (FlowGraphNode graphNode : nodes) {
                FlowNode flowNode = graphNode.getFlowNode();

                if (graphNode instanceof FlowGraphNodeStart) {
                    graphNode.getFlowNode().setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.START_STEP));
                } else if (graphNode instanceof FlowGraphNodeEnd) {
                    graphNode.getFlowNode().setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.END_STEP));
                }

                flowNode.setFlowLaneLevel(flowNode.getNodeLevel());

                FlowGraphLane graphLane = new FlowGraphLane();
                FlowLane lane = new FlowLane();
                lane.setFlowMainId(flow.getFlowMainId());
                lane.setFlowVersion(flow.getFlowVersion());
                lane.setNamespaceId(flow.getNamespaceId());
                lane.setStatus(FlowCommonStatus.ACTIVE.getCode());
                lane.setId(0L);
                lane.setLaneLevel(flowNode.getNodeLevel());
                lane.setDisplayName(flowNode.getNodeName());
                lane.setDisplayNameAbsort(buttonDefName(Namespace.DEFAULT_NAMESPACE, FlowStepType.ABSORT_STEP));
                lane.setFlowNodeLevel(flowNode.getNodeLevel());
                graphLane.setFlowLane(lane);
                flowNode.setFlowLaneId(0L);
                flowGraph.getLanes().add(graphLane);

                if (prefixLink != null) {
                    FlowGraphLinkNormal linkNormal = null;
                    if (!(graphNode instanceof FlowGraphNodeEnd)) {
                        linkNormal = new FlowGraphLinkNormal();
                        FlowLink flowLink = ConvertHelper.convert(flow, FlowLink.class);
                        flowLink.setLinkLevel(++linkLevel);
                        flowLink.setFromNodeId(graphNode.getFlowNode().getId());
                        flowLink.setFromNodeLevel(graphNode.getFlowNode().getNodeLevel());
                        linkNormal.setFlowLink(flowLink);

                        graphNode.getLinksOut().add(linkNormal);
                    }
                    prefixLink.getFlowLink().setToNodeId(graphNode.getFlowNode().getId());
                    prefixLink.getFlowLink().setToNodeLevel(graphNode.getFlowNode().getNodeLevel());

                    graphNode.getLinksIn().add(prefixLink);

                    prefixLink = linkNormal;
                } else {
                    FlowGraphLinkNormal linkNormal = new FlowGraphLinkNormal();
                    FlowLink flowLink = ConvertHelper.convert(flow, FlowLink.class);
                    flowLink.setLinkLevel(++linkLevel);
                    flowLink.setFromNodeId(graphNode.getFlowNode().getId());
                    flowLink.setFromNodeLevel(graphNode.getFlowNode().getNodeLevel());
                    linkNormal.setFlowLink(flowLink);

                    graphNode.getLinksOut().add(linkNormal);
                    prefixLink = linkNormal;
                }
            }
        }
    }

    private FlowGraphNode getFlowGraphConditionNode(FlowNode flowNode, Integer flowVersion) {
        FlowGraphNodeCondition graphNode = new FlowGraphNodeCondition();
        graphNode.setFlowNode(flowNode);
        Long flowNodeId = flowNode.getId();

        List<FlowLink> linksIn = flowLinkProvider.listFlowLinkByToNodeId(flowNodeId, flowVersion);
        for (FlowLink link : linksIn) {
            FlowGraphLink graphLink = new FlowGraphLinkNormal();
            graphLink.setFlowLink(link);
            graphNode.getLinksIn().add(graphLink);
        }

        List<FlowLink> linksOut = flowLinkProvider.listFlowLinkByFromNodeId(flowNodeId, flowVersion);
        for (FlowLink link : linksOut) {
            FlowGraphLink graphLink = new FlowGraphLinkNormal();
            graphLink.setFlowLink(link);
            graphNode.getLinksOut().add(graphLink);
        }

        List<FlowCondition> conditions = flowConditionProvider.listFlowCondition(flowNodeId);
        for (FlowCondition cond : conditions) {
            FlowGraphCondition graphCondition = new FlowGraphConditionNormal();
            graphCondition.setCondition(cond);
            List<FlowConditionExpression> expressions = flowConditionExpressionProvider.listFlowConditionExpression(cond.getId());
            graphCondition.setExpressions(expressions);
            graphNode.getConditions().add(graphCondition);
        }
        return graphNode;
    }

    private FlowGraph getConfigGraph(Long flowId) {
        FlowGraph flowGraph = new FlowGraph();
        Flow flow = flowProvider.getFlowById(flowId);
        if (flow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_SNAPSHOT_NOEXISTS, "snapshot noexists");
        }
        flowGraph.setFlow(flow);

        List<FlowNode> flowNodes = flowNodeProvider.findFlowNodesByFlowId(flowId, FlowConstants.FLOW_CONFIG_VER);
        flowNodes.sort(Comparator.comparing(EhFlowNodes::getNodeLevel));

        // int i = 1;
        for (FlowNode fn : flowNodes) {
            // if (!fn.getNodeLevel().equals(i)) {
            //     throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NODE_LEVEL_ERR, "node_level error");
            // }
            if (fn.getNodeName().equals("START") || FlowNodeType.fromCode(fn.getNodeType()) == FlowNodeType.START) {
                flowGraph.getNodes().add(getFlowGraphStartNode(fn, FlowConstants.FLOW_CONFIG_VER));
            } else if (fn.getNodeName().equals("END") || FlowNodeType.fromCode(fn.getNodeType()) == FlowNodeType.END) {
                flowGraph.getNodes().add(getFlowGraphEndNode(fn, FlowConstants.FLOW_CONFIG_VER));
            } else if (fn.getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {
                flowGraph.getNodes().add(getFlowGraphConditionNode(fn, FlowConstants.FLOW_CONFIG_VER));
            } else {
                flowGraph.getNodes().add(getFlowGraphNode(fn, FlowConstants.FLOW_CONFIG_VER));
            }
            // i++;
        }

        // 泳道
        List<FlowLane> laneList = flowLaneProvider.listFlowLane(flowId, FlowConstants.FLOW_CONFIG_VER);
        for (FlowLane lane : laneList) {
            FlowGraphLane graphLane = new FlowGraphLane();
            graphLane.setFlowLane(lane);
            flowGraph.getLanes().add(graphLane);
        }
        // 分支
        List<FlowBranch> branchList = flowBranchProvider.findByFlowId(flowId, FlowConstants.FLOW_CONFIG_VER);
        for (FlowBranch flowBranch : branchList) {
            flowGraph.getBranches().add(getFlowGraphBranch(flowBranch));
        }

        flowGraph.saveIds();

        return flowGraph;
    }

    @Override
    public FlowButtonDTO fireButton(FlowFireButtonCommand cmd) {
        // 兼容老版本,我也不知道多老的版本用这个
        buttonFireCompatible(cmd);

        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(UserContext.current().getUser().getId());
        FlowCaseState ctx = flowStateProcessor.prepareButtonFire(userInfo, cmd);
        fixupUserInfoInContext(ctx, userInfo);

        if (ctx.isContinueFlag()) {
            flowStateProcessor.step(ctx, ctx.getCurrentEvent());
        }

        FlowButton btn = flowButtonProvider.getFlowButtonById(cmd.getButtonId());
        return ConvertHelper.convert(btn, FlowButtonDTO.class);
    }

    private void buttonFireCompatible(FlowFireButtonCommand cmd) {
        if (cmd.getEntityId() != null && cmd.getFlowEntityType() != null) {
            FlowEntitySel sel = new FlowEntitySel();
            sel.setEntityId(cmd.getEntityId());
            sel.setFlowEntityType(cmd.getFlowEntityType());
            cmd.getEntitySel().add(sel);
            cmd.setEntityId(null);
            cmd.setFlowEntityType(null);
        }
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
            String messageTitle = flowCase.getTitle() != null ? flowCase.getTitle() : ctx.getModuleName();
            meta.put(MessageMetaConstant.MESSAGE_SUBJECT, messageTitle);

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
                flow.setStatus(FlowStatusType.STOP.getCode());
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
        if (flow != null && flow.getStatus().equals(FlowStatusType.RUNNING.getCode())) {
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
        if (flowCaseCmd.getAdditionalFieldDTO() != null) {
            BeanUtils.copyProperties(flowCaseCmd.getAdditionalFieldDTO(), flowCase);
        }

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
        if (FlowModuleType.SERVICE_ALLIANCE.getCode().equals(snapshotFlow.getModuleType())) {
            moduleDTO = this.getModuleById(40500L);
        }
        flowCase.setNamespaceId(snapshotFlow.getNamespaceId());
        flowCase.setModuleId(snapshotFlow.getModuleId());
        flowCase.setModuleName(moduleDTO.getDisplayName());
        if (flowCaseCmd.getTitle() == null || flowCaseCmd.getTitle().isEmpty()) {
            flowCase.setTitle(moduleDTO.getModuleName());
        }
        if (flowCase.getServiceType() == null) {
            flowCase.setServiceType(flowCase.getTitle());
        }
        flowCase.setModuleType(snapshotFlow.getModuleType());
        flowCase.setOwnerId(snapshotFlow.getOwnerId());
        flowCase.setOwnerType(snapshotFlow.getOwnerType());
        flowCase.setCaseType(FlowCaseType.INNER.getCode());
        flowCase.setStatus(FlowCaseStatus.INITIAL.getCode());
        flowCase.setOrganizationId(snapshotFlow.getOrganizationId());
        flowCase.setApplierOrganizationId(flowCaseCmd.getCurrentOrganizationId());
        flowCase.setStartNodeId(snapshotFlow.getStartNode());
        flowCase.setEndNodeId(snapshotFlow.getEndNode());

        List<FlowLink> startLink = flowLinkProvider.listFlowLinkByFromNodeId(flowCase.getStartNodeId(), snapshotFlow.getFlowVersion());
        if (startLink.size() > 0) {
            flowCase.setStartLinkId(startLink.get(0).getId());
        }
        List<FlowLink> endLink = flowLinkProvider.listFlowLinkByToNodeId(flowCase.getEndNodeId(), snapshotFlow.getFlowVersion());
        if (endLink.size() > 0) {
            flowCase.setEndLinkId(endLink.get(0).getId());
        }

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
        if (FlowModuleType.SERVICE_ALLIANCE.getCode().equals(ga.getModuleType())) {
            moduleDTO = this.getModuleById(40500L);
        }
        flowCase.setFlowMainId(0L);
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

        List<FlowVariable> vars = new ArrayList<>();
        String para = null;
        List<FlowVariable> vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
                , flow.getOwnerId(), flow.getOwnerType(), flow.getModuleId(), flow.getModuleType(), para, cmd.getFlowVariableType());
        if (vars2 != null) {
            vars.addAll(vars2);
        }
        vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
                , 0L, null, flow.getModuleId(), flow.getModuleType(), para, cmd.getFlowVariableType());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        vars2 = flowVariableProvider.findVariables(cmd.getNamespaceId()
                , 0L, null, 0L, null, para, cmd.getFlowVariableType());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        if (!cmd.getNamespaceId().equals(0)) {
            vars2 = flowVariableProvider.findVariables(0
                    , 0L, null, 0L, null, para, cmd.getFlowVariableType());
            if (vars2 != null) {
                vars.addAll(vars2);
            }
        }

        Map<String, Long> map = new HashMap<>();
        for (FlowVariable var : vars) {
            if (!map.containsKey(var.getName())) {
                dtos.add(ConvertHelper.convert(var, FlowVariableDTO.class));
                map.put(var.getName(), 1L);
            }
        }

        Map<String, List<FlowVariableDTO>> varsMap = dtos.stream().collect(Collectors.groupingBy(FlowVariableDTO::getScriptType));

        resp.setFlowVars(varsMap.get(FlowVariableScriptType.BEAN_ID.getCode()));
        resp.setModuleVars(varsMap.get(FlowVariableScriptType.MODULE_ID.getCode()));
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
            List<FlowVariable> vars = flowVariableProvider.findVariables(0
                    , 0L, null, 0L, null, null, cmd.getFlowVariableType());

            List<FlowVariableDTO> dtos = new ArrayList<>();
            Map<String, Long> map = new HashMap<>();
            for (FlowVariable var : vars) {
                if (!map.containsKey(var.getName())) {
                    dtos.add(ConvertHelper.convert(var, FlowVariableDTO.class));
                    map.put(var.getName(), 1L);
                }
            }

            Map<String, List<FlowVariableDTO>> varsMap = dtos.stream().collect(Collectors.groupingBy(FlowVariableDTO::getScriptType));

            resp.setFlowVars(varsMap.get(FlowVariableScriptType.BEAN_ID.getCode()));
            resp.setModuleVars(varsMap.get(FlowVariableScriptType.MODULE_ID.getCode()));
            return resp;
        }
    }

    private void updateCaseDTO(FlowCase flowCase, FlowNode flowNode, FlowCaseDTO dto, int type) {
        dto.setAllowApplierUpdate(flowNode.getAllowApplierUpdate());
        dto.setCurrNodeParams(flowNode.getParams());
        dto.setFlowNodeName(flowNode.getNodeName());

        boolean allFlowCaseFlag = type != 3;
        List<UserInfo> currentProcessors = getCurrentProcessors(flowCase.getId(), allFlowCaseFlag);
        int i = 0;
        String name = "";
        for (UserInfo userInfo : currentProcessors) {
            if (i < 3) {
                name += (userInfo.getNickName() + ",");
            } else {
                break;
            }
            i++;
        }
        if (name.length() > 0) {
            name = name.substring(0, name.length() - 1);
        }
        dto.setProcessUserName(name);

        // 判断当前flowCase有没有兄弟flowCase，暨是不是并发状态执行
        if (flowCase.getParentId() != 0) {
            List<FlowCase> subFlowCases = flowCaseProvider.listFlowCaseByParentId(flowCase.getParentId());
            if (subFlowCases.size() > 1) {
                dto.setConcurrentFlag(TrueOrFalseFlag.TRUE.getCode());
            }
        }

        // Long realCurrentNodeId = getRealCurrentNodeId(flowCase, flowNode.getId());

        /*List<FlowUserSelection> sels = flowUserSelectionProvider.findSelectionByBelong(realCurrentNodeId,
                FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode());

        String name;
        if (sels != null && sels.size() > 0) {
            updateFlowUserName(sels.get(0));
            name = sels.get(0).getSelectionName();
            for (int i = 1; i < sels.size() && i < 3; i++) {
                updateFlowUserName(sels.get(i));
                name = name + "," + sels.get(i).getSelectionName();
            }
            dto.setProcessUserName(name);
        }*/

        // 老版本
        if (flowCase.getCurrentLaneId() == null || flowCase.getCurrentLaneId() == 0L) {
            if ("END".equals(flowNode.getNodeName())) {
                FlowStepType stepType = FlowStepType.END_STEP;
                if (FlowCaseStatus.ABSORTED.getCode().equals(flowCase.getStatus())) {
                    stepType = FlowStepType.ABSORT_STEP;
                }
                flowNode.setNodeName(buttonDefName(flowCase.getNamespaceId(), stepType));
            }
            dto.setCurrentLane(flowNode.getNodeName());
        } else {
            FlowLane currentLane = flowLaneProvider.findById(flowCase.getCurrentLaneId());
            dto.setCurrentLane(currentLane.getDisplayName());
            if (flowCase.getStatus().equals(FlowCaseStatus.ABSORTED.getCode())) {
                dto.setCurrentLane(currentLane.getDisplayNameAbsort());
            }
        }

        Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());

        //isTrue
        dto.setNeedEvaluate((byte) 0);
        List<FlowEvaluate> evas = flowEvaluateProvider.findEvaluates(flowCase.getId(), snapshotFlow.getFlowMainId(), snapshotFlow.getFlowVersion());
        if (evas != null && evas.size() > 0) {
            dto.setEvaluateScore(new Integer(evas.get(0).getStar()));
            // dto.setNeedEvaluate((byte) 2);
        } else if (type == 1) {
            if (!snapshotFlow.getNeedEvaluate().equals((byte) 0)
                    && flowNode.getNodeLevel() >= snapshotFlow.getEvaluateStart()
                    && flowNode.getNodeLevel() <= snapshotFlow.getEvaluateEnd()
                    && !flowCase.getStatus().equals(FlowCaseStatus.ABSORTED.getCode())) {
                dto.setNeedEvaluate((byte) 1);
            }

            // 结束后可评价处理
            if (FlowCaseStatus.FINISHED.getCode().equals(flowCase.getStatus())
                    && TrueOrFalseFlag.TRUE.getCode().equals(snapshotFlow.getAllowFlowCaseEndEvaluate())
                    && TrueOrFalseFlag.FALSE.getCode().equals(flowCase.getEvaluateStatus())) {
                // FlowButtonDTO buttonDTO = new FlowButtonDTO();
                // buttonDTO.setId(0L);
                // buttonDTO.setFlowStepType(FlowStepType.EVALUATE_STEP.getCode());
                // buttonDTO.setButtonName(buttonDefName(snapshotFlow.getNamespaceId(), FlowStepType.EVALUATE_STEP));
                // dto.setEvaluateBtn(buttonDTO);
                dto.setNeedEvaluate((byte) 1);
            }
        }
    }

    @Override
    public FlowCase getFlowCaseById(Long flowCaseId) {
        return flowCaseProvider.getFlowCaseById(flowCaseId);
    }

    @Override
    public List<UserInfo> getCurrentProcessors(Long flowCaseId, boolean allFlowCaseFlag) {
        List<FlowCase> allFlowCase = new ArrayList<>();
        if (allFlowCaseFlag) {
            allFlowCase = getAllFlowCase(flowCaseId);
        } else {
            allFlowCase.add(flowCaseProvider.getFlowCaseById(flowCaseId));
        }

        Long organizationId = null;
        List<FlowEventLog> enterLogs = new ArrayList<>();
        for (FlowCase aCase : allFlowCase) {
            if (organizationId == null) {
                organizationId = aCase.getOrganizationId();
            }
            List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(aCase.getCurrentNodeId(), aCase.getId(), aCase.getStepCount());
            if (logs.size() > 0) {
                enterLogs.addAll(logs);
            }
        }

        List<Long> userIds = enterLogs.stream().map(FlowEventLog::getFlowUserId).distinct().collect(Collectors.toList());

        List<UserInfo> userInfoList = new ArrayList<>();
        for (Long userId : userIds) {
            UserInfo userInfo = userService.getUserSnapshotInfo(userId);
            fixupUserInfo(organizationId, userInfo);
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    @Override
    public List<UserInfo> getSupervisor(FlowCase flowCase) {
        List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(flowCase.getFlowMainId()
                , FlowEntityType.FLOW.getCode(), FlowUserType.SUPERVISOR.getCode(), flowCase.getFlowVersion());
        return resolveSelectionUsers(flowCase.getFlowMainId(), selections);
    }

    @Override
    public SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd) {
        return searchFlowCases(cmd, null);
    }

    @Override
    public SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd, ListingQueryBuilderCallback callback) {
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
        if (cmd.getModuleId() != null && cmd.getModuleId() == 0L) {
            cmd.setModuleId(null);
        }

        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        cmd.setPageSize(count);
        ListingLocator locator = new ListingLocator();

        int type;
        FlowUserType flowUserType;
        List<FlowCaseDetail> details;

        if (cmd.getFlowCaseSearchType().equals(FlowCaseSearchType.APPLIER.getCode())) {
            type = 1;
            flowUserType = FlowUserType.APPLIER;
            details = flowCaseProvider.findApplierFlowCases(locator, count, cmd, callback);
        } else if (cmd.getFlowCaseSearchType().equals(FlowCaseSearchType.ADMIN.getCode())) {
            type = 2;
            flowUserType = FlowUserType.PROCESSOR;
            details = flowCaseProvider.findAdminFlowCases(locator, count, cmd, callback);
        } else {
            type = 3;
            flowUserType = FlowUserType.PROCESSOR;
            details = flowEventLogProvider.findProcessorFlowCases(locator, count, cmd, callback);
        }

        List<FlowCaseDTO> dtos = new ArrayList<FlowCaseDTO>();
        if (details != null) {
            for (FlowCaseDetail detail : details) {
                detail.setContent(onFlowCaseBriefRender(flowUserType, detail));

                FlowCaseDTO dto = ConvertHelper.convert(detail, FlowCaseDTO.class);
                FlowNode flowNode = flowNodeProvider.getFlowNodeById(dto.getCurrentNodeId());
                if (flowNode != null) {
                    updateCaseDTO(detail, flowNode, dto, type);
                }
                if (dto.getTitle() != null && dto.getModuleName() == null) {
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

    private String onFlowCaseBriefRender(FlowUserType flowUserType, FlowCase flowCase) {
        String flowCaseContent = flowCase.getContent();
        try {
            flowCaseContent = flowListenerManager.onFlowCaseBriefRender(flowCase, flowUserType);
        } catch (Exception e) {
            LOGGER.error("Flow listener onFlowCaseBriefRender error, flowCaseId = "+flowCase.getId(), e);
        }
        return flowCaseContent;
    }

    @Override
    public FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId, Long inUserId, FlowUserType flowUserType) {
        return getFlowCaseDetail(flowCaseId, inUserId, flowUserType, false);
    }

    private FlowButtonDTO flowButtonToDTO(Flow snapshotFlow, FlowButton button, Map<Long, FlowNode> nodeMap, int level) {
        FlowButtonDTO btnDTO = ConvertHelper.convert(button, FlowButtonDTO.class);

        FlowStepType stepType = FlowStepType.fromCode(button.getFlowStepType());
        if (stepType == FlowStepType.TRANSFER_STEP) {
            // force use processor
            btnDTO.setNeedProcessor((byte) 1);
        } else if (stepType == FlowStepType.APPROVE_STEP && level >= nodeMap.size() - 2) {
            btnDTO.setNeedProcessor((byte) 0);
        } else if (stepType != FlowStepType.APPROVE_STEP) {
            btnDTO.setNeedProcessor((byte) 0);
        }
        // default with user settings needProcessor
        return btnDTO;
    }

    @Override
    public FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId, Long inUserId, FlowUserType flowUserType, boolean checkProcessor) {
        /*Long userId = inUserId;
        if (userId == null) {
            userId = UserContext.current().getUser().getId();
        }

        FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
        if (flowCase == null) {
            return new FlowCaseDetailDTO();
        }
        Flow snapshotFlow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        LOGGER.debug("start rental getFlowCaseDetail  flowCase={}, flowUserType={}, inst={}", flowCase, flowUserType);

        List<FlowCaseEntity> entities = getFlowCaseEntities(Collections.singletonList(flowUserType), flowCase);

        FlowCaseDetailDTO dto = ConvertHelper.convert(flowCase, FlowCaseDetailDTO.class);
        dto.setEntities(entities);
        if (dto.getStatus().equals(FlowCaseStatus.INVALID.getCode())) {
            return dto;
        }

        if (dto.getTitle() != null) {
            dto.setModuleName(dto.getTitle());
        }

        List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowCase.getFlowMainId(), flowCase.getFlowVersion());
        // List<FlowNode> nodes = originalNodes.stream().filter(r -> r.getNodeType().equals(FlowNodeType.NORMAL.getCode())).collect(Collectors.toList());

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

        // 当前节点的level
        final Integer nlevel = level;

        if (nodes.size() < 3) {
            return dto;
        }

        List<FlowButtonDTO> btnDTOS = new ArrayList<>();
        if (flowUserType == FlowUserType.PROCESSOR) {

            if (!checkProcessor || (null != flowEventLogProvider.isProcessor(userId, flowCase))) {
                List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), flowUserType.getCode());

                // 排序
                buttons.sort(Comparator.comparingInt(FlowButton::getDefaultOrder));

                buttons.forEach((button) -> {
                    boolean isAdd = true;

                    // 第一个节点的驳回按钮特殊处理
                    if (flowCase.getCurrentNodeId().equals(nodes.get(1).getId())
                            && button.getFlowStepType().equals(FlowStepType.REJECT_TRACKER.getCode())) {
                        isAdd = false;
                    }

                    if (isAdd && button.getStatus().equals(FlowButtonStatus.ENABLED.getCode())
                            && !button.getFlowStepType().equals(FlowStepType.COMMENT_STEP.getCode())) {
                        FlowButtonDTO btnDTO = flowButtonToDTO(snapshotFlow, button, nodeMap, nlevel);
                        btnDTOS.add(btnDTO);
                    }
                });
            }

            dto.setButtons(btnDTOS);
        } else if (flowUserType == FlowUserType.APPLIER) {
            List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), flowUserType.getCode());
            buttons.forEach((button) -> {
                if (button.getStatus().equals(FlowButtonStatus.ENABLED.getCode())
                        && !button.getFlowStepType().equals(FlowStepType.COMMENT_STEP.getCode())) {
                    FlowButtonDTO btnDTO = ConvertHelper.convert(button, FlowButtonDTO.class);
                    FlowStepType stepType = FlowStepType.fromCode(button.getFlowStepType());
                    if (stepType == FlowStepType.REMINDER_STEP) {
                        btnDTO.setNeedSubject((byte) 0);
                        btnDTO.setSubjectRequiredFlag(TrueOrFalseFlag.FALSE.getCode());
                    }

                    btnDTOS.add(btnDTO);
                }
            });

            dto.setButtons(btnDTOS);
        } //SUPERVISOR at last

        List<FlowCase> allFlowCase = flowStateProcessor.getAllFlowCase(flowCase.getId());
        List<Long> flowCaseIdList = allFlowCase.stream().map(FlowCase::getId).collect(Collectors.toList());
        //got all nodes tracker logs
        List<FlowEventLog> stepLogs = flowEventLogProvider.findStepEventLogs(flowCaseIdList);

        List<FlowNodeLogDTO> nodeDTOS = new ArrayList<>();
        dto.setNodes(nodeDTOS);

        // 开始节点
        FlowNodeLogDTO nodeDTO = new FlowNodeLogDTO();
        nodeDTO.setNodeLevel(0);
        nodeDTO.setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.START_STEP));
        nodeDTOS.add(nodeDTO);

        FlowNode currNode = null;
        boolean absorted = false;

        // 构建有stepLog的节点信息
        for (FlowEventLog eventLog : stepLogs) {
            //获取工作流经过的节点日志
            if (currNode == null || !currNode.getId().equals(eventLog.getFlowNodeId())) { // 把nodeLog按node进行分类
                currNode = nodeMap.get(eventLog.getFlowNodeId());
                final FlowNodeLogDTO nodeLogDTO = new FlowNodeLogDTO();
                nodeLogDTO.setNodeId(currNode.getId());
                nodeLogDTO.setNodeLevel(currNode.getNodeLevel());
                nodeLogDTO.setNodeName(currNode.getNodeName());
                nodeLogDTO.setParams(currNode.getParams());

                if (flowCase.getStepCount().equals(eventLog.getStepCount())) {
                    nodeLogDTO.setIsCurrentNode((byte) 1);
                    dto.setCurrNodeParams(currNode.getParams());

                    // 附言按钮
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

        // 正常状态
        if (!absorted) {
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

            // 结束节点
            nodeDTO = new FlowNodeLogDTO();
            nodeDTO.setNodeLevel(nodes.size() + 1);
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
        }
        // 终止状态
        else {
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
        return dto;*/

        FlowCaseDetailDTOV2 v2Response = getFlowCaseDetailByIdV2(flowCaseId, inUserId, flowUserType, checkProcessor, true);
        FlowCaseDetailDTO v1Response = ConvertHelper.convert(v2Response, FlowCaseDetailDTO.class);

        List<FlowNodeLogDTO> flowNodeLogDTOS = new ArrayList<>();
        for (FlowLaneLogDTO laneLogDTO : v2Response.getLanes()) {
            FlowNodeLogDTO nodeLogDTO = new FlowNodeLogDTO();
            if (TrueOrFalseFlag.fromCode(laneLogDTO.getIsCurrentLane()) == TrueOrFalseFlag.TRUE) {
                v1Response.setCurrentNodeId(laneLogDTO.getLaneId());
            }
            nodeLogDTO.setIsCurrentNode(laneLogDTO.getIsCurrentLane());
            nodeLogDTO.setLogs(laneLogDTO.getLogs());
            nodeLogDTO.setNodeName(laneLogDTO.getLaneName());
            nodeLogDTO.setNodeLevel(laneLogDTO.getLaneLevel());
            nodeLogDTO.setNodeId(laneLogDTO.getLaneId());

            flowNodeLogDTOS.add(nodeLogDTO);
        }
        v1Response.setNodes(flowNodeLogDTOS);
        return v1Response;
    }

    //获取每个节点的跟踪日志，如果有文本，则格式化文本
    private void getFlowNodeLogDTO(FlowCase flowCase, FlowUserType flowUserType, FlowNode currNode, Long stepCount, FlowNodeLogDTO nodeLogDTO) {
        /*List<FlowEventLog> trackerLogs = flowEventLogProvider.findEventLogsByNodeId(currNode.getId()
                , flowCase.getId(), stepCount, Collections.singletonList(flowUserType));
        if (trackerLogs != null) {
            trackerLogs.forEach((t) -> {
                FlowEventLogDTO eventDTO = ConvertHelper.convert(t, FlowEventLogDTO.class);
                if (FlowStepType.EVALUATE_STEP.getCode().equals(t.getButtonFiredStep())) {
                    eventDTO.setIsEvaluate((byte) 1);
                }
                nodeLogDTO.getLogs().add(eventDTO);
            });
        }*/
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
        if (snapshotFlow == null || flowCase.getStatus().equals(FlowCaseStatus.INVALID.getCode())) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS, "flowcase noexists, flowCaseId=" + flowCase);
        }

        FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
        stepDTO.setEventType(FlowEventType.BUTTON_FIRED.getCode());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setOperatorId(UserContext.currentUserId());
        stepDTO.setFlowMainId(flowCase.getFlowMainId());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
        stepDTO.setFlowVersion(flowCase.getFlowVersion());
        stepDTO.setStepCount(flowCase.getStepCount());

        FlowCaseState ctx = flowStateProcessor.prepareNoStep(stepDTO);
        flowCase = ctx.getFlowCase();

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
            eva.setFlowCaseId(flowCase.getId());
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

        ctx.setFlowEvas(flowEvas);

        // flowEvaluateProvider.createFlowEvaluate(flowEvas);

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

        ctx.getLogs().add(tracker);

        // flowEventLogProvider.createFlowEventLog(tracker);

        flowCase.setEvaluateStatus(TrueOrFalseFlag.TRUE.getCode());
        // flowCaseProvider.updateFlowCase(flowCase);

        FlowGraph flowGraph = ctx.getFlowGraph();
        FlowGraphLane graphLane = flowGraph.getGraphLane(flowCase.getCurrentLaneId());
        Long identifierNodeId = graphLane.getFlowLane().getIdentifierNodeId();
        if (identifierNodeId == null) {
            identifierNodeId = getRealCurrentNodeId(flowCase, flowCase.getCurrentNodeId());
        }
        FlowGraphNode graphNode = flowGraph.getGraphNode(identifierNodeId);

        for (FlowGraphButton btn : graphNode.getApplierButtons()) {
            if (btn.getFlowButton().getFlowStepType().equals(FlowStepType.EVALUATE_STEP.getCode())) {
                btn.fireActions(ctx);
                break;
            }
        }

        flushState(ctx);

        if (snapshotFlow.getEvaluateStep() != null
                && snapshotFlow.getEvaluateStep().equals(FlowStepType.APPROVE_STEP.getCode())
                && flowCase.getStatus().equals(FlowCaseStatus.PROCESS.getCode())) {
            stepDTO = new FlowAutoStepDTO();
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

        List<FlowLink> linkOut = flowLinkProvider.listFlowLinkByFromNodeId(flowNode.getId(), flowNode.getFlowVersion());
        if (linkOut == null || linkOut.size() == 0) {
            List<FlowNode> nodes = flowNodeProvider.findFlowNodesByFlowId(flowNode.getFlowMainId(), flowNode.getFlowVersion());
            Map<Long, FlowNode> nodeMap = new HashMap<>();
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
        } else {
            nextNode = flowNodeProvider.getFlowNodeById(linkOut.get(0).getToNodeId());
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
        ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(moduleId);
        if (serviceModule != null) {
            FlowModuleDTO dto = new FlowModuleDTO();
            dto.setDisplayName(serviceModule.getName());
            dto.setModuleName(serviceModule.getName());
            dto.setModuleId(moduleId);
            return dto;
        }
        return null;
    }

    @Override
    public ListFlowModulesResponse listModules(ListFlowModulesCommand cmd) {
        ListFlowModulesResponse resp = new ListFlowModulesResponse();
        List<FlowModuleDTO> modules = new ArrayList<>();
        resp.setModules(modules);

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
        FlowGraphEvent evt = ctx.getCurrentEvent();
        List<FlowUserSelection> selections = new ArrayList<>();
        List<Long> users = new ArrayList<>();

        if (evt != null && evt.getEntitySel() != null && evt.getEntitySel().size() > 0) {
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
            for (FlowCaseState flowCaseState : ctx.getAllFlowState()) {
                FlowCase flowCase = flowCaseState.getFlowCase();
                if (flowCaseProvider.updateIfValid(flowCase.getId(), flowCase.getLastStepTime(), now)) {

                    flowCase.setLastStepTime(now);
                    flowCaseProvider.updateFlowCase(flowCase);

                    flowEventLogProvider.createFlowEventLogs(flowCaseState.getLogs());
                    flowEventLogProvider.updateFlowEventLogs(flowCaseState.getUpdateLogs());

                    flowEvaluateProvider.createFlowEvaluate(ctx.getFlowEvas());
                } else {
                    throw new FlowStepBusyException("already step by others, flowCaseId = " + flowCase.getId());
                }
            }
            return true;
        });

        //flush timeouts
        for (FlowCaseState flowCaseState : ctx.getAllFlowState()) {
            for (FlowTimeout ft : flowCaseState.getTimeouts()) {
                flowTimeoutService.pushTimeout(ft);
            }
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
                , 0L, null, fc.getModuleId(), fc.getModuleType(), para, FlowVariableType.TEXT.getCode());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        vars2 = flowVariableProvider.findVariables(fc.getNamespaceId()
                , 0L, null, 0L, null, para, FlowVariableType.TEXT.getCode());
        if (vars2 != null) {
            vars.addAll(vars2);
        }

        if (!fc.getNamespaceId().equals(0)) {
            vars2 = flowVariableProvider.findVariables(0
                    , 0L, null, 0L, null, para, FlowVariableType.TEXT.getCode());
            if (vars2 != null) {
                vars.addAll(vars2);
            }
        }

        try {
            for (FlowVariable fv : vars) {
                FlowVariableScriptType scriptType = FlowVariableScriptType.fromCode(fv.getScriptType());
                switch (scriptType) {
                    case BEAN_ID:
                        FlowVariableTextResolver ftr = PlatformContext.getComponent(fv.getScriptCls());
                        if (ftr != null) {
                            String val = ftr.variableTextRender(ctx, para);
                            if (null != val) {
                                return val;
                            }
                        }
                        break;
                    case MODULE_ID:
                        List<String> paramList = new ArrayList<>();
                        paramList.add(para);
                        Map<String, String> variableMap = flowListenerManager.onFlowVariableRender(ctx, paramList);
                        if (variableMap != null) {
                            String val = variableMap.get(para);
                            if (val != null) {
                                return val;
                            }
                        }
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Resolve text variable error, moduleId = {}, flowCaseId = {}, var = {}", fc.getModuleId(), fc.getId(), para);
            e.printStackTrace();
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

    @Override
    public String getButtonFireEventContentTemplate(FlowStepType step, Map<String, Object> map) {
        String scope = FlowTemplateCode.SCOPE;
        int code = 0;
        switch (step) {
            case NO_STEP:
            case APPROVE_STEP:
            case REJECT_STEP:
            case ABSORT_STEP:
            case COMMENT_STEP:
            case TRANSFER_STEP:
                code = FlowTemplateCode.GENERAL_BUTTON_FIRE_LOG_TEMPLATE;
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
    public String getStepMessageTemplate(FlowStepType fromStep, FlowCaseStatus nextStatus, FlowGraphEvent event, Map<String, Object> map) {
        String scope = FlowTemplateCode.SCOPE;
        int code = 0;
        if (nextStatus == FlowCaseStatus.FINISHED || nextStatus == FlowCaseStatus.ABSORTED) {
            //到终止节点
            if (fromStep == FlowStepType.APPROVE_STEP || fromStep == FlowStepType.END_STEP) {
                code = FlowTemplateCode.NEXT_STEP_DONE;
            } else if (fromStep == FlowStepType.ABSORT_STEP) {
                if (event.getUserType() == FlowUserType.PROCESSOR) {
                    if (event.getEventType() == FlowEventType.STEP_TIMEOUT) {
                        code = FlowTemplateCode.TIMEOUT_ABSORT;
                    } else {
                        code = FlowTemplateCode.PROCESSOR_ABSORT;
                    }
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
            return localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
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
            List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(fn.getId(),
                    FlowConstants.FLOW_CONFIG_VER, FlowUserType.PROCESSOR.getCode());

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

        if (cmd.getAllowFlowCaseEndEvaluate() != null) {
            flow.setAllowFlowCaseEndEvaluate(cmd.getAllowFlowCaseEndEvaluate());
            flowMarkUpdated(flow);
        }

        /*this.dbProvider.execute(status -> {

            List<FlowEvaluateItem> configItems = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(
                    flow.getId(), FlowConstants.FLOW_CONFIG_VER);

            if (configItems != null && configItems.size() > 0) {
                // 删除原来的item
                flowEvaluateItemProvider.deleteFlowEvaluateItem(configItems);
            }

            configItems = new ArrayList<>();
            List<FlowEvaluateItemDTO> items = cmd.getItems();
            if (cmd.getItems() != null) {
                for (FlowEvaluateItemDTO dto : items) {
                    FlowEvaluateItem item = new FlowEvaluateItem();
                    item.setFlowMainId(flow.getId());
                    item.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
                    item.setName(dto.getName());
                    item.setNamespaceId(flow.getNamespaceId());
                    item.setInputFlag(dto.getInputFlag());
                    configItems.add(item);
                }
                if (configItems.size() > 0) {
                    flowEvaluateItemProvider.createFlowEvaluateItem(configItems);
                }
            }

            *//*if (cmd.getMessageAction() != null) {
                createEvaluateAction(flow, FlowConstants.FLOW_CONFIG_VER, cmd.getMessageAction(),
                        FlowActionType.MESSAGE.getCode(), FlowActionStepType.STEP_NONE.getCode(), FlowStepType.EVALUATE_STEP.getCode());
            }
            if (cmd.getSmsAction() != null) {
                createEvaluateAction(flow, FlowConstants.FLOW_CONFIG_VER, cmd.getSmsAction(),
                        FlowActionType.SMS.getCode(), FlowActionStepType.STEP_NONE.getCode(), FlowStepType.EVALUATE_STEP.getCode());
            }*//*

            return null;
        });*/
        return null;
    }

    private FlowEvaluateDetailDTO getFlowEvaluate(Flow flow) {
        FlowEvaluateDetailDTO dto = new FlowEvaluateDetailDTO();
        dto.setFlowId(flow.getId());

        // ---- Deprecated Start ----
        // dto.setEvaluateEnd(flow.getEvaluateEnd());
        // dto.setEvaluateStart(flow.getEvaluateStart());
        // dto.setEvaluateStep(flow.getEvaluateStep());
        // dto.setNeedEvaluate(flow.getNeedEvaluate());
        // ---- Deprecated End ----

        dto.setAllowFlowCaseEndEvaluate(flow.getAllowFlowCaseEndEvaluate());

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
        List<FlowUserSelection> sels = new ArrayList<>();
        FlowUserSelection sel = flowUserSelectionProvider.getFlowUserSelectionById(selectionUserId);
        sels.add(sel);

        List<UserInfo> userInfoList = resolveSelectionUsers(flowId, sels);
        FlowResolveUsersResponse resp = new FlowResolveUsersResponse();
        resp.setUsers(userInfoList);
        return resp;
    }

    private List<UserInfo> resolveSelectionUsers(Long flowId, List<FlowUserSelection> selections) {
        FlowCaseState ctx = new FlowCaseState();
        FlowGraph graph = new FlowGraph();
        Flow flow = flowProvider.getFlowById(flowId);
        graph.setFlow(flow);
        ctx.setFlowGraph(graph);

        List<Long> users = resolvUserSelections(ctx, null, null, selections);

        List<UserInfo> infos = new ArrayList<>();
        if (users != null && users.size() > 0) {
            users.forEach((u) -> {
                UserInfo ui = getUserInfoInContext(ctx, u);
                infos.add(ConvertHelper.convert(ui, UserInfo.class));
            });
        }
        return infos;
    }

    @Override
    public ListSelectUsersResponse listUserSelections(ListSelectUsersCommand cmd) {
        FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
        stepDTO.setFlowCaseId(cmd.getFlowCaseId());
        stepDTO.setOperatorId(UserContext.currentUserId());
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
        fixupUserInfo(ctx.getFlowGraph().getFlow().getOrganizationId(), ui);
    }

    private void fixupUserInfo(Long organizationId, UserInfo userInfo) {
        if (userInfo != null) {
            OrganizationMember om = organizationProvider.findOrganizationMemberByOrgIdAndUId(userInfo.getId(), organizationId);
            if (om != null && om.getContactName() != null && !om.getContactName().isEmpty()) {
                userInfo.setNickName(om.getContactName());
            }
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
                    flowCase.setDeleteFlag(TrueOrFalseFlag.TRUE.getCode());
                    flowCaseProvider.updateFlowCase(flowCase);
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public ListFlowPredefinedParamResponse listPredefinedParam(ListPredefinedParamCommand cmd) {
        ValidatorUtil.validate(cmd);

        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<FlowPredefinedParam> paramList = flowPredefinedParamProvider.listPredefinedParam(namespaceId,
                cmd.getModuleType(), cmd.getModuleId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getEntityType());

        if (paramList.isEmpty()) {
            paramList = flowPredefinedParamProvider.listPredefinedParam(namespaceId,
                    FlowModuleType.NO_MODULE.getCode(), cmd.getModuleId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getEntityType());
        }
        if (paramList.isEmpty()) {
            paramList = flowPredefinedParamProvider.listPredefinedParam(Namespace.DEFAULT_NAMESPACE,
                    cmd.getModuleType(), cmd.getModuleId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getEntityType());
        }
        if (paramList.isEmpty()) {
            paramList = flowPredefinedParamProvider.listPredefinedParam(Namespace.DEFAULT_NAMESPACE,
                    FlowModuleType.NO_MODULE.getCode(), cmd.getModuleId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getEntityType());
        }

        List<FlowPredefinedParamDTO> dtoList = paramList.stream().map(this::toPredefinedParamDTO).collect(Collectors.toList());

        ListFlowPredefinedParamResponse response = new ListFlowPredefinedParamResponse();
        response.setParams(dtoList);
        return response;
    }

    private FlowPredefinedParamDTO toPredefinedParamDTO(FlowPredefinedParam param) {
        return ConvertHelper.convert(param, FlowPredefinedParamDTO.class);
    }

    @Override
    public void updateFlowButtonOrder(UpdateFlowButtonOrderCommand cmd) {
        ValidatorUtil.validate(cmd);

        FlowButton button1 = flowButtonProvider.getFlowButtonById(cmd.getId());
        FlowButton button2 = flowButtonProvider.getFlowButtonById(cmd.getExchangeId());

        if (button1 != null && button2 != null) {
            Integer button1DefaultOrder = button1.getDefaultOrder();
            Integer button2DefaultOrder = button2.getDefaultOrder();

            Flow flow = flowProvider.getFlowById(button1.getFlowMainId());

            button1.setDefaultOrder(button2DefaultOrder);
            button2.setDefaultOrder(button1DefaultOrder);

            dbProvider.execute(status -> {
                flowMarkUpdated(flow);
                flowButtonProvider.updateFlowButton(button1);
                flowButtonProvider.updateFlowButton(button2);
                return true;
            });
        }
    }

    @Override
    public FlowLaneDTO updateFlowLane(UpdateFlowLaneCommand cmd) {
        ValidatorUtil.validate(cmd);

        Tuple<FlowLane, Boolean> tuple = coordinationProvider.getNamedLock(CoordinationLocks.FLOW_LANE.getCode() + cmd.getLaneId()).enter(() -> {
            FlowLane flowLane = flowLaneProvider.findById(cmd.getLaneId());
            if (flowLane != null) {
                Flow flow = flowProvider.getFlowById(flowLane.getFlowMainId());
                flowMarkUpdated(flow);
                flowLane.setDisplayName(cmd.getDisplayName());
                flowLane.setIdentifierNodeLevel(cmd.getIdentifierNodeLevel());
                flowLane.setIdentifierNodeId(cmd.getIdentifierNodeId());
                flowLane.setDisplayNameAbsort(cmd.getDisplayNameAbsort());
                flowLaneProvider.updateFlowLane(flowLane);
            }
            return flowLane;
        });
        return toFlowLaneDTO(tuple.first());
    }

    @Override
    public FlowButtonDTO createFlowButton(CreateFlowButtonCommand cmd) {
        ValidatorUtil.validate(cmd);

        FlowButtonDTO buttonDTO = new FlowButtonDTO();
        Flow flow = flowProvider.getFlowById(cmd.getFlowId());
        if (flow != null) {
            FlowNode flowNode = flowNodeProvider.getFlowNodeById(cmd.getNodeId());
            if (flowNode != null) {
                FlowUserType userType = FlowUserType.fromCode(cmd.getUserType());
                FlowStepType stepType = FlowStepType.fromCode(cmd.getStepType());

                Integer defaultOrder = cmd.getButtonsCount() != null ? (cmd.getButtonsCount() + 1) * 10 : null;

                buttonDTO = dbProvider.execute(status -> {
                    flowMarkUpdated(flow);
                    return createDefButton(flow, flowNode, userType, stepType, FlowButtonStatus.ENABLED, defaultOrder);
                });
            }
        }
        return buttonDTO;
    }

    @Override
    public FlowGraphDTO getFlowGraphNew(FlowIdCommand cmd) {
        ValidatorUtil.validate(cmd);
        Flow flow = flowProvider.getFlowById(cmd.getFlowId());
        if (flow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flow not exist flowId=%s", cmd.getFlowId());
        }
        FlowGraphDTO graphDTO = toFlowGraphDTO(flow);

        // flow-2.0之前的工作流没有link和lane，兼容一下
        if (graphDTO.getNodes().size() > 0
                && graphDTO.getLinks().size() == 0
                && graphDTO.getLanes().size() == 0) {
            processStartAndEndNode(cmd, flow, graphDTO);
        }
        return graphDTO;
    }

    private void processStartAndEndNode(FlowIdCommand cmd, Flow flow, FlowGraphDTO graphDTO) {
        dbProvider.execute(status -> {
            List<FlowNodeDTO> nodes = graphDTO.getNodes();
            nodes.sort(Comparator.comparingInt(FlowNodeDTO::getNodeLevel));

            // 开始节点level是1，结束节点level是2，所以把原来的节点level都往后推
            /*for (FlowNodeDTO node : nodes) {
                FlowNode flowNode = flowNodeProvider.getFlowNodeById(node.getId());
                if (node.getNodeType() == null || node.getNodeType().isEmpty()) {
                    node.setNodeType(FlowNodeType.NORMAL.getCode());
                    flowNode.setNodeType(FlowNodeType.NORMAL.getCode());
                }
                nodeLevel++;
                node.setNodeLevel(nodeLevel);
                flowNode.setNodeLevel(nodeLevel);
                flowNodeProvider.updateFlowNode(flowNode);
            }*/

            List<FlowLaneDTO> laneList = new ArrayList<>();

            FlowLaneCommand flowLaneCmd = null;
            FlowLaneDTO laneDTO = null;

            Map<Integer, Long> nodeLevelToIdMap = new HashMap<>();

            // order is important
            int laneLevel = 2;
            int nodeLevel = 3;
            for (FlowNodeDTO node : nodes) {
                FlowNode flowNode = flowNodeProvider.getFlowNodeById(node.getId());
                nodeLevelToIdMap.put(flowNode.getNodeLevel(), flowNode.getId());

                if (node.getNodeType() == null || node.getNodeType().isEmpty()) {
                    node.setNodeType(FlowNodeType.NORMAL.getCode());
                    flowNode.setNodeType(FlowNodeType.NORMAL.getCode());
                }
                nodeLevel++;
                node.setNodeLevel(nodeLevel);
                flowNode.setNodeLevel(nodeLevel);

                flowLaneCmd = new FlowLaneCommand();
                flowLaneCmd.setLaneLevel(laneLevel++);
                flowLaneCmd.setDisplayName(node.getNodeName());
                flowLaneCmd.setFlowNodeLevel(node.getNodeLevel());
                laneDTO = createFlowLane(flow, flowLaneCmd, node.getNodeName());
                laneList.add(laneDTO);

                flowNode.setFlowLaneId(laneDTO.getId());

                flowNodeProvider.updateFlowNode(flowNode);
            }

            // start node
            flowLaneCmd = new FlowLaneCommand();
            flowLaneCmd.setLaneLevel(1);
            flowLaneCmd.setDisplayName(buttonDefName(flow.getNamespaceId(), FlowStepType.START_STEP));
            flowLaneCmd.setFlowNodeLevel(1);
            laneDTO = createFlowLane(flow, flowLaneCmd, null);
            laneList.add(laneDTO);

            CreateFlowNodeCommand flowNodeCmd = new CreateFlowNodeCommand();
            flowNodeCmd.setFlowMainId(cmd.getFlowId());
            flowNodeCmd.setNodeLevel(1);
            flowNodeCmd.setNodeName(buttonDefName(flow.getNamespaceId(), FlowStepType.START_STEP));
            flowNodeCmd.setNodeType(FlowNodeType.START.getCode());
            flowNodeCmd.setFlowLaneId(laneDTO.getId());
            flowNodeCmd.setFlowLaneLevel(laneDTO.getLaneLevel());
            FlowNodeDTO startNode = createFlowNode(flowNodeCmd);
            nodes.add(startNode);

            // end node
            flowLaneCmd = new FlowLaneCommand();
            flowLaneCmd.setLaneLevel(laneLevel);
            flowLaneCmd.setDisplayName(buttonDefName(flow.getNamespaceId(), FlowStepType.END_STEP));
            flowLaneCmd.setFlowNodeLevel(2);
            laneDTO = createFlowLane(flow, flowLaneCmd, buttonDefName(flow.getNamespaceId(), FlowStepType.ABSORT_STEP));
            laneList.add(laneDTO);

            flowNodeCmd = new CreateFlowNodeCommand();
            flowNodeCmd.setFlowMainId(cmd.getFlowId());
            flowNodeCmd.setNodeLevel(2);
            flowNodeCmd.setNodeName(buttonDefName(flow.getNamespaceId(), FlowStepType.END_STEP));
            flowNodeCmd.setNodeType(FlowNodeType.END.getCode());
            flowNodeCmd.setFlowLaneId(laneDTO.getId());
            flowNodeCmd.setFlowLaneLevel(laneDTO.getLaneLevel());
            FlowNodeDTO endNode = createFlowNode(flowNodeCmd);
            // nodes.add(endNode);

            nodes.sort(Comparator.comparingInt(FlowNodeDTO::getNodeLevel));

            List<FlowLinkDTO> linkList = new ArrayList<>();

            int linkLevel = 1;
            FlowNodeDTO prefixNode = null;
            for (FlowNodeDTO node : nodes) {
                if (prefixNode != null) {
                    FlowLinkCommand flowLinkCmd = new FlowLinkCommand();
                    flowLinkCmd.setFromNodeId(prefixNode.getId());
                    flowLinkCmd.setFromNodeLevel(prefixNode.getNodeLevel());
                    flowLinkCmd.setToNodeId(node.getId());
                    flowLinkCmd.setToNodeLevel(node.getNodeLevel());
                    flowLinkCmd.setLinkLevel(linkLevel++);

                    FlowLinkDTO linkDTO = createFlowLink(flow, flowLinkCmd);
                    linkList.add(linkDTO);
                }
                prefixNode = node;
            }

            if (prefixNode != null) {
                FlowLinkCommand flowLinkCmd = new FlowLinkCommand();
                flowLinkCmd.setFromNodeId(prefixNode.getId());
                flowLinkCmd.setFromNodeLevel(prefixNode.getNodeLevel());
                flowLinkCmd.setToNodeId(endNode.getId());
                flowLinkCmd.setToNodeLevel(endNode.getNodeLevel());
                flowLinkCmd.setLinkLevel(linkLevel);

                FlowLinkDTO linkDTO = createFlowLink(flow, flowLinkCmd);
                linkList.add(linkDTO);
            }

            nodes.add(endNode);

            laneList.sort(Comparator.comparingInt(FlowLaneDTO::getLaneLevel));

            graphDTO.setLinks(linkList);
            graphDTO.setLanes(laneList);

            // 评价按钮根据之前的设置启用
            if (flow.getEvaluateStart() != null && flow.getEvaluateEnd() != null) {
                long nodeCount = flow.getEvaluateEnd() - flow.getEvaluateStart();
                for (long i = 0; i <= nodeCount; i++) {
                    Long nodeId = nodeLevelToIdMap.get(Integer.parseInt(String.valueOf(flow.getEvaluateStart() + i)));
                    FlowButton evalButton = flowButtonProvider.findFlowButtonByStepType(nodeId,
                            FlowConstants.FLOW_CONFIG_VER, FlowStepType.EVALUATE_STEP.getCode(), FlowUserType.APPLIER.getCode());
                    if (evalButton != null) {
                        evalButton.setStatus(FlowButtonStatus.ENABLED.getCode());
                        flowButtonProvider.updateFlowButton(evalButton);
                    }
                }
                flow.setAllowFlowCaseEndEvaluate(TrueOrFalseFlag.TRUE.getCode());
            }
            flowMarkUpdated(flow);
            return true;
        });
    }

    @Override
    public FlowGraphDTO createOrUpdateFlowCondition(CreateFlowConditionCommand cmd) {
        ValidatorUtil.validate(cmd);

        Flow flow = flowProvider.getFlowById(cmd.getFlowId());
        if (flow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flow not exist flowId=%s", cmd.getFlowId());
        }

        FlowNode flowNode = flowNodeProvider.getFlowNodeById(cmd.getFlowNodeId());
        dbProvider.execute(status -> {
            flowMarkUpdated(flow);

            if (cmd.getBranch() != null) {
                FlowBranch flowBranch = flowBranchProvider.findBranchByOriginalNodeId(flow.getId(), FlowConstants.FLOW_CONFIG_VER, cmd.getFlowNodeId());
                if (flowBranch != null) {
                    FlowBranchCommand branchCmd = cmd.getBranch();
                    flowBranch.setProcessMode(branchCmd.getProcessMode());
                    flowBranch.setBranchDecider(branchCmd.getBranchDecider());
                    flowBranchProvider.updateFlowBranch(flowBranch);
                }
            } else {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_PARAM_ERROR,
                        "param error");
            }

            // 创建condition
            if (cmd.getConditions() != null) {
                flowConditionProvider.deleteFlowCondition(flow.getId(), cmd.getFlowNodeId(), FlowConstants.FLOW_CONFIG_VER);
                for (FlowConditionCommand conditionCmd : cmd.getConditions()) {
                    conditionCmd.setFlowNodeId(cmd.getFlowNodeId());
                    conditionCmd.setFlowNodeLevel(flowNode.getNodeLevel());
                    createFlowCondition(flow, conditionCmd);
                }
            }
            return true;
        });
        return toFlowGraphDTO(flow);
    }

    @Override
    public void updateFlowValidationStatus(UpdateFlowValidationStatusCommand cmd) {
        Flow flow = flowProvider.getFlowById(cmd.getFlowId());
        if (flow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flow not exist flowId=%s", cmd.getFlowId());
        }
        dbProvider.execute(status -> {
            flow.setValidationStatus(cmd.getValidationStatus());
            flowMarkUpdated(flow);
            return true;
        });
    }

    @Override
    public FlowGraphDTO createOrUpdateFlowGraph(CreateFlowGraphCommand cmd) {
        ValidatorUtil.validate(cmd);

        Flow flow = flowProvider.getFlowById(cmd.getFlowId());
        if (flow == null) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flow not exist flowId=%s", cmd.getFlowId());
        }
        dbProvider.execute(status -> {
            flowMarkUpdated(flow);

            // 创建lane
            Map<Integer, Long> laneLevelToLaneIdMap = new HashMap<>();
            String absortDisplayName = null;
            for (int i = 0; i < cmd.getLanes().size(); i++) {
                if (i == cmd.getLanes().size() - 1) {
                    absortDisplayName = buttonDefName(flow.getNamespaceId(), FlowStepType.ABSORT_STEP);
                }
                FlowLaneDTO flowLane = createFlowLane(flow, cmd.getLanes().get(i), absortDisplayName);
                laneLevelToLaneIdMap.put(flowLane.getLaneLevel(), flowLane.getId());
            }

            List<Long> currentLaneIdList = laneLevelToLaneIdMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

            // 删除不在这次流程图里的lane
            flowLaneProvider.deleteFlowLane(flow.getId(), FlowConstants.FLOW_CONFIG_VER, currentLaneIdList);

            // 创建node
            Map<Integer, Long> nodeLevelToNodeIdMap = new HashMap<>();
            for (CreateFlowNodeCommand flowNodeCmd : cmd.getNodes()) {
                flowNodeCmd.setFlowLaneId(laneLevelToLaneIdMap.get(flowNodeCmd.getFlowLaneLevel()));
                flowNodeCmd.setFlowMainId(flow.getId());
                FlowNodeDTO flowNode = createFlowNode(flowNodeCmd);
                if (flowNode.getNodeType().equals(FlowNodeType.START.getCode())) {
                    flow.setStartNode(flowNode.getId());
                } else if (flowNode.getNodeType().equals(FlowNodeType.END.getCode())) {
                    flow.setEndNode(flowNode.getId());
                }
                nodeLevelToNodeIdMap.put(flowNode.getNodeLevel(), flowNode.getId());
            }
            flowProvider.updateFlow(flow);

            List<Long> currentNodeIdList = nodeLevelToNodeIdMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

            // 删除不在这次流程图里的node
            flowNodeProvider.deleteFlowNode(flow.getId(), FlowConstants.FLOW_CONFIG_VER, currentNodeIdList);

            // 更新泳道里的identifierNodeId
            List<FlowLane> laneList = flowLaneProvider.listFlowLane(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
            for (FlowLane lane : laneList) {
                lane.setIdentifierNodeId(nodeLevelToNodeIdMap.get(lane.getIdentifierNodeLevel()));
                flowLaneProvider.updateFlowLane(lane);
            }

            // 创建branch
            flowBranchProvider.deleteFlowBranch(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
            for (CreateFlowNodeCommand flowNodeCmd : cmd.getNodes()) {
                if (FlowNodeType.fromCode(flowNodeCmd.getNodeType()) == FlowNodeType.CONDITION_FRONT) {
                    FlowBranchCommand branchCmd = flowNodeCmd.getBranch();
                    branchCmd.setOriginalNodeId(nodeLevelToNodeIdMap.get(branchCmd.getOriginalNodeLevel()));
                    branchCmd.setConvergenceNodeId(nodeLevelToNodeIdMap.get(branchCmd.getConvergenceNodeLevel()));
                    createFlowBranch(branchCmd, flow);
                }
            }

            // 创建link
            // Map<Integer, Long> linkLevelToLinkIdMap = new HashMap<>();
            flowLinkProvider.deleteFlowLink(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
            for (FlowLinkCommand flowLinkCmd : cmd.getLinks()) {
                flowLinkCmd.setFromNodeId(nodeLevelToNodeIdMap.get(flowLinkCmd.getFromNodeLevel()));
                flowLinkCmd.setToNodeId(nodeLevelToNodeIdMap.get(flowLinkCmd.getToNodeLevel()));
                FlowLinkDTO flowLink = createFlowLink(flow, flowLinkCmd);
                // linkLevelToLinkIdMap.put(flowLink.getLinkLevel(), flowLink.getId());
            }

            /*// 创建condition
            if (cmd.getConditions() != null) {
                flowConditionProvider.deleteFlowCondition(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
                for (FlowConditionCommand conditionCmd : cmd.getConditions()) {
                    conditionCmd.setFlowNodeId(nodeLevelToNodeIdMap.get(conditionCmd.getFlowNodeLevel()));
                    conditionCmd.setNextNodeId(nodeLevelToNodeIdMap.get(conditionCmd.getNextNodeLevel()));
                    conditionCmd.setFlowLinkId(linkLevelToLinkIdMap.get(conditionCmd.getFlowLinkLevel()));
                    createFlowCondition(flow, conditionCmd);
                }
            }*/
            return true;
        });

        return toFlowGraphDTO(flow);
    }

    private FlowBranchDTO createFlowBranch(FlowBranchCommand cmd, Flow flow) {
        FlowBranch branch = new FlowBranch();
        branch.setFlowMainId(flow.getId());
        branch.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
        branch.setNamespaceId(flow.getNamespaceId());

        branch.setConvergenceNodeLevel(cmd.getConvergenceNodeLevel());
        branch.setConvergenceNodeId(cmd.getConvergenceNodeId());

        branch.setOriginalNodeLevel(cmd.getOriginalNodeLevel());
        branch.setOriginalNodeId(cmd.getOriginalNodeId());

        branch.setBranchDecider(cmd.getBranchDecider());
        branch.setProcessMode(cmd.getProcessMode());
        branch.setStatus(FlowCommonStatus.ACTIVE.getCode());
        flowBranchProvider.createFlowBranch(branch);
        return toFlowBranchDTO(branch);
    }

    @Override
    public FlowGraphDTO createOrUpdateFlowGraph(CreateFlowGraphJsonCommand cmd) {
        return createOrUpdateFlowGraph((CreateFlowGraphCommand) StringHelper.fromJsonString(cmd.getBody(), CreateFlowGraphCommand.class));
    }

    /*public static void main(String[] args) throws Exception {
        BlutoClassLoader loader = new BlutoClassLoader();
        Method method = loader.getClass().getDeclaredMethod("findClass", String.class);
        method.setAccessible(true);
        Object bluto = method.invoke(loader, "com.everhomes.tool.Bluto");

        bluto = ((Class) bluto);

        Object o = ((Class) bluto).newInstance();

        for (Field field : ((Class) bluto).getDeclaredFields()) {
            field.setAccessible(true);
            Object o1 = field.get(o);
            if (o1 instanceof byte[]) {
                o1 = Arrays.toString(((byte[]) o1));
            }
            System.out.println(field.getName() + ":"+ o1 + ":"+field.getType());
        }
        for (Method field : ((Class) bluto).getDeclaredMethods()) {
            field.setAccessible(true);
            System.out.println(field.getName() + ":"+ field.getName() + ":"+field.getReturnType());
        }

        Object mixer = method.invoke(loader, "com.everhomes.tool.BlutoMixer");

        ((Class) bluto).getMethod("get", new Class[]{((Class) mixer)});
    }*/

    @Override
    public FlowCaseDetailDTOV2 getFlowCaseDetailByIdV2(Long flowCaseId, Long userId, FlowUserType flowUserType, boolean checkProcessor, boolean needFlowButton) {
        if (userId == null) {
            userId = UserContext.currentUserId();
        }

        FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
        if (flowCase == null) {
            return new FlowCaseDetailDTOV2();
        }

        if (FlowCaseType.fromCode(flowCase.getCaseType()) == FlowCaseType.DUMB) {
            return ConvertHelper.convert(getDumpFlowCaseBrief(flowCase), FlowCaseDetailDTOV2.class);
        }

        FlowCaseDetailDTOV2 dto = ConvertHelper.convert(flowCase, FlowCaseDetailDTOV2.class);
        if (dto.getStatus().equals(FlowCaseStatus.INVALID.getCode())) {
            return null;
        }

        if (dto.getTitle() != null && dto.getModuleName() == null) {
            dto.setModuleName(dto.getTitle());
        } else {
            dto.setTitle(dto.getModuleName());
        }

        FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
        stepDTO.setEventType(FlowEventType.BUTTON_FIRED.getCode());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setOperatorId(UserContext.currentUserId());
        stepDTO.setFlowMainId(flowCase.getFlowMainId());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
        stepDTO.setFlowVersion(flowCase.getFlowVersion());
        stepDTO.setStepCount(flowCase.getStepCount());

        // FlowGraph flowGraph = getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());

        FlowCaseState ctx = flowStateProcessor.prepareNoStep(stepDTO);
        FlowGraph flowGraph = ctx.getFlowGraph();

        List<FlowCase> allFlowCase = ctx.getAllFlowCases();

        List<FlowNode> nodes = flowGraph.getNodes().stream().map(FlowGraphNode::getFlowNode).collect(Collectors.toList());
        nodes.sort(Comparator.comparingInt(FlowNode::getNodeLevel));

        List<FlowLane> laneList = flowGraph.getLanes().stream().map(FlowGraphLane::getFlowLane).collect(Collectors.toList());
        if (laneList.size() < 3) {
            return dto;
        }
        laneList.sort(Comparator.comparingInt(FlowLane::getLaneLevel));

        if (userId == null) {
            return dto;
        }

        List<FlowUserType> flowUserTypes = new ArrayList<>();
        if (flowUserType == null) {
            if (userId.equals(flowCase.getApplyUserId())) {
                flowUserTypes.add(FlowUserType.APPLIER);
            }

            FlowEventLog enterLog = flowEventLogProvider.isProcessor(userId, flowCase);
            if (enterLog != null) {
                flowUserTypes.add(FlowUserType.PROCESSOR);
            }
        } else {
            flowUserTypes.add(flowUserType);
        }

        // 详情信息
        List<FlowCaseEntity> entities = getFlowCaseEntities(flowUserTypes, ctx.getGrantParentState().getFlowCase());
        dto.setEntities(entities);
        dto.setCustomObject(ctx.getGrantParentState().getFlowCase().getCustomObject());

        // 后台管理界面不显示按钮
        if (needFlowButton) {
            // 按钮，在这里只要处理人的按钮，只有处理人会看到这个界面
            List<FlowButtonDTO> btnList = getFlowButtonDTOList(flowGraph, userId, flowUserTypes, checkProcessor, flowCase, nodes, laneList);

            if (btnList.size() > 4) {
                // 给按钮分组
                Tuple<List<FlowButtonDTO>, List<FlowButtonDTO>> tuple = btnGroupBy(btnList);
                dto.setButtons(tuple.first());
                dto.setMoreButtons(tuple.second());
            } else {
                btnList.sort((b1, b2) -> b2.getDefaultOrder() - b1.getDefaultOrder());
                dto.setButtons(btnList);
            }
        }

        // 节点日志
        List<FlowLaneLogDTO> laneLogList = getFlowLaneLogDTOList(flowGraph, flowUserTypes, flowCase, allFlowCase, laneList);
        dto.setLanes(laneLogList);
        return dto;
    }

    private Tuple<List<FlowButtonDTO>, List<FlowButtonDTO>> btnGroupBy(List<FlowButtonDTO> btnList) {
        btnList = btnList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        btnList.sort(Comparator.comparingInt(FlowButtonDTO::getDefaultOrder));

        List<FlowButtonDTO> buttons = new ArrayList<>();
        List<FlowButtonDTO> moreButtons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            buttons.add(btnList.get(i));
        }
        for (int i = 4; i < btnList.size(); i++) {
            moreButtons.add(btnList.get(i));
        }
        buttons.sort((b1, b2) -> b2.getDefaultOrder() - b1.getDefaultOrder());
        moreButtons.sort(Comparator.comparingInt(FlowButtonDTO::getDefaultOrder));
        return new Tuple<>(buttons, moreButtons);
    }

    @Override
    public FlowCaseTrackDTO getFlowCaseTrack(GetFlowCaseTrackCommand cmd) {
        ValidatorUtil.validate(cmd);
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
        if (flowCase == null) {
            return null;
        }
        if (flowCase.getFlowMainId() == 0) {
            return ConvertHelper.convert(flowCase, FlowCaseTrackDTO.class);
        }
        FlowCaseTrackDTO dto = new FlowCaseTrackDTO();

        FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
        stepDTO.setEventType(FlowEventType.BUTTON_FIRED.getCode());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setOperatorId(UserContext.currentUserId());
        stepDTO.setFlowMainId(flowCase.getFlowMainId());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
        stepDTO.setFlowVersion(flowCase.getFlowVersion());
        stepDTO.setStepCount(flowCase.getStepCount());

        // FlowGraph flowGraph = getFlowGraph(flowCase.getFlowMainId(), flowCase.getFlowVersion());

        FlowCaseState ctx = flowStateProcessor.prepareNoStep(stepDTO);
        FlowGraph flowGraph = ctx.getFlowGraph();

        List<FlowCase> allFlowCase = ctx.getAllFlowCases();

        List<FlowNode> nodes = flowGraph.getNodes().stream().map(FlowGraphNode::getFlowNode).collect(Collectors.toList());
        nodes.sort(Comparator.comparingInt(FlowNode::getNodeLevel));

        List<FlowLane> laneList = flowGraph.getLanes().stream().map(FlowGraphLane::getFlowLane).collect(Collectors.toList());
        // if (laneList.size() < 3) {
        //     return dto;
        // }
        laneList.sort(Comparator.comparingInt(FlowLane::getLaneLevel));

        Long userId = UserContext.currentUserId();
        if (userId == null) {
            return dto;
        }

        List<FlowUserType> flowUserTypes = new ArrayList<>();
        if (userId.equals(flowCase.getApplyUserId())) {
            flowUserTypes.add(FlowUserType.APPLIER);
        }

        FlowEventLog enterLog = flowEventLogProvider.isProcessor(userId, flowCase);
        if (enterLog != null) {
            flowUserTypes.add(FlowUserType.PROCESSOR);
        }

        List<FlowLaneLogDTO> list = getFlowLaneLogDTOList(flowGraph, flowUserTypes, flowCase, allFlowCase, laneList);
        dto.setLanes(list);
        return dto;
    }

    @Override
    public FlowCaseBriefDTO getFlowCaseBrief(GetFlowCaseBriefCommand cmd) {
        ValidatorUtil.validate(cmd);

        FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
        if (flowCase == null) {
            return null;
        }

        if (FlowCaseType.fromCode(flowCase.getCaseType()) == FlowCaseType.DUMB) {
            return getDumpFlowCaseBrief(flowCase);
        }

        FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
        stepDTO.setEventType(FlowEventType.BUTTON_FIRED.getCode());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setOperatorId(UserContext.currentUserId());
        stepDTO.setFlowMainId(flowCase.getFlowMainId());
        stepDTO.setFlowCaseId(flowCase.getId());
        stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
        stepDTO.setFlowVersion(flowCase.getFlowVersion());
        stepDTO.setStepCount(flowCase.getStepCount());

        FlowCaseState ctx = flowStateProcessor.prepareNoStep(stepDTO);
        FlowGraph flowGraph = ctx.getFlowGraph();

        FlowCaseBriefDTO dto = ConvertHelper.convert(flowCase, FlowCaseBriefDTO.class);
        if (dto.getTitle() != null && dto.getModuleName() == null) {
            dto.setModuleName(dto.getTitle());
        } else {
            dto.setTitle(dto.getModuleName());
        }

        List<FlowCase> allFlowCase = ctx.getAllFlowCases();

        List<FlowNode> nodes = flowGraph.getNodes().stream().map(FlowGraphNode::getFlowNode).collect(Collectors.toList());
        nodes.sort(Comparator.comparingInt(FlowNode::getNodeLevel));

        List<FlowLane> laneList = flowGraph.getLanes().stream().map(FlowGraphLane::getFlowLane).collect(Collectors.toList());
        if (laneList.size() < 3) {
            return dto;
        }
        laneList.sort(Comparator.comparingInt(FlowLane::getLaneLevel));

        Long userId = UserContext.currentUserId();
        if (userId == null) {
            return dto;
        }

        List<FlowUserType> flowUserTypes = new ArrayList<>();
        if (userId.equals(flowCase.getApplyUserId())) {
            flowUserTypes.add(FlowUserType.APPLIER);
            dto.getButtons().addAll(getApplierButtonDTOList(flowGraph, flowCase));
        }

        // 督办按钮处理
        for (FlowCase aCase : allFlowCase) {
            FlowEventLog enterLog = flowEventLogProvider.isSupervisors(userId, aCase);
            if (enterLog != null) {
                if (!flowUserTypes.contains(FlowUserType.SUPERVISOR)) {
                    flowUserTypes.add(FlowUserType.SUPERVISOR);
                }
                if (flowCase.getStatus().equals(FlowCaseStatus.PROCESS.getCode())) {
                    dto.getButtons().add(getSupervisorButton(flowCase));
                    break;
                }
            }
        }

        // 去处理按钮处理
        FlowButtonDTO goToProcessButton = null;
        for (FlowCase aCase : allFlowCase) {
            FlowEventLog enterLog = flowEventLogProvider.isProcessor(userId, aCase);
            if (enterLog != null) {
                if (goToProcessButton == null) {
                    goToProcessButton = getGoToProcessButton(aCase, flowGraph);
                    flowUserTypes.add(FlowUserType.PROCESSOR);
                    dto.getButtons().add(goToProcessButton);
                }
                FlowCaseGoToProcessDTO caseDTO = new FlowCaseGoToProcessDTO();
                caseDTO.setId(aCase.getId());
                caseDTO.setFlowNodeName(ctx.getFlowGraph().getGraphNode(enterLog.getFlowNodeId()).getFlowNode().getNodeName());
                goToProcessButton.getGoToProcessFlowCase().add(caseDTO);
            }
        }

        if (dto.getButtons().size() > 4) {
            // 给按钮分组
            Tuple<List<FlowButtonDTO>, List<FlowButtonDTO>> tuple = btnGroupBy(dto.getButtons());
            dto.setButtons(tuple.first());
            dto.setMoreButtons(tuple.second());
        } else {
            dto.getButtons().sort((b1, b2) -> b2.getDefaultOrder() - b1.getDefaultOrder());
        }

        List<FlowCaseEntity> entities = getFlowCaseEntities(flowUserTypes, ctx.getGrantParentState().getFlowCase());
        dto.setEntities(entities);
        dto.setCustomObject(ctx.getGrantParentState().getFlowCase().getCustomObject());

        List<FlowLaneLogDTO> list = getFlowLaneLogDTOList(flowGraph, flowUserTypes, flowCase, allFlowCase, laneList);
        for (int i = list.size() - 1; i >= 0; i--) {
            FlowLaneLogDTO laneLogDTO = list.get(i);
            if (laneLogDTO.getLogs().size() > 0 || TrueOrFalseFlag.TRUE.getCode().equals(laneLogDTO.getIsCurrentLane())) {
                dto.setCurrentLane(laneLogDTO);
                break;
            }
        }
        return dto;
    }

    private FlowCaseBriefDTO getDumpFlowCaseBrief(FlowCase flowCase) {
        FlowCaseBriefDTO dto = ConvertHelper.convert(flowCase, FlowCaseBriefDTO.class);
        List<FlowUserType> flowUserTypes = new ArrayList<>();
        flowUserTypes.add(FlowUserType.APPLIER);
        List<FlowCaseEntity> entities = getFlowCaseEntities(flowUserTypes, flowCase);
        dto.setEntities(entities);
        return dto;
    }

    private FlowButtonDTO getSupervisorButton(FlowCase flowCase) {
        FlowButton flowButton = flowButtonProvider.findFlowButtonByStepType(flowCase.getFlowMainId(),
                0L, flowCase.getFlowVersion(), FlowStepType.SUPERVISE.getCode(), FlowUserType.SUPERVISOR.getCode());
        return ConvertHelper.convert(flowButton, FlowButtonDTO.class);
    }

    @Override
    public void deleteFlowButton(DeleteFlowButtonCommand cmd) {
        Flow flow = getFlowByEntity(cmd.getFlowButtonId(), FlowEntityType.FLOW_BUTTON);
        FlowButton flowButton = flowButtonProvider.getFlowButtonById(cmd.getFlowButtonId());
        if (flowButton != null) {
            dbProvider.execute(status -> {
                flowMarkUpdated(flow);
                flowButtonProvider.deleteFlowButton(flowButton);
                return true;
            });
        }
    }

    @Override
    public ListFlowServiceTypeResponse listFlowServiceTypes(ListFlowServiceTypesCommand cmd) {
        List<FlowServiceTypeDTO> serviceTypes = new ArrayList<>();
        serviceTypes.addAll(flowListenerManager.listFlowServiceTypes(UserContext.getCurrentNamespaceId(), cmd.getModuleId()));

        List<FlowServiceTypeDTO> nsServiceTypes = flowServiceTypeProvider.listFlowServiceType(
                UserContext.getCurrentNamespaceId(), cmd.getModuleId(), FlowServiceTypeDTO.class);
        if (nsServiceTypes.size() == 0) {
            nsServiceTypes = flowServiceTypeProvider.listFlowServiceType(
                    Namespace.DEFAULT_NAMESPACE, cmd.getModuleId(), FlowServiceTypeDTO.class);
        }
        serviceTypes.addAll(nsServiceTypes);

        ListFlowServiceTypeResponse response = new ListFlowServiceTypeResponse();
        response.setServiceTypes(serviceTypes);
        return response;
    }

    @Override
    public ListNextBranchesResponse listNextBranches(ListNextBranchesCommand cmd) {
        ValidatorUtil.validate(cmd);
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
        if (flowCase == null) {
            return null;
        }

        List<FlowLink> toLinkList = flowLinkProvider.listFlowLinkByFromNodeId(cmd.getConditionNodeId(), flowCase.getFlowVersion());

        List<FlowNodeDTO> dtoList = new ArrayList<>();
        for (FlowLink flowLink : toLinkList) {
            FlowNode node = flowNodeProvider.getFlowNodeById(flowLink.getToNodeId());
            dtoList.add(ConvertHelper.convert(node, FlowNodeDTO.class));
        }

        ListNextBranchesResponse response = new ListNextBranchesResponse();
        response.setNodes(dtoList);
        return response;
    }

    @Override
    public SearchFlowOperateLogResponse searchFlowOperateLogs(SearchFlowOperateLogsCommand cmd) {
        Long userId = cmd.getUserId();
        if (userId == null) {
            userId = UserContext.currentUserId();
        }
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, 20);

        List<FlowOperateLogDTO> operateLogs = flowEventLogProvider.searchOperateLogs(
                cmd.getModuleId(), cmd.getFlowCaseId(), userId, cmd.getServiceType(), cmd.getKeyword(), pageSize, locator);
        SearchFlowOperateLogResponse response = new SearchFlowOperateLogResponse();
        response.setLogs(operateLogs);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private FlowButtonDTO getGoToProcessButton(FlowCase flowCase, FlowGraph flowGraph) {
        FlowButtonDTO dto = new FlowButtonDTO();
        String processButtonName = flowGraph.getGraphNode(flowCase.getCurrentNodeId()).getFlowNode().getGotoProcessButtonName();
        if (processButtonName != null && processButtonName.length() > 0) {
            dto.setButtonName(processButtonName);
        } else {
            dto.setButtonName(buttonDefName(flowCase.getNamespaceId(), FlowStepType.GO_TO_PROCESS));
        }
        dto.setDefaultOrder(0);
        dto.setFlowStepType(FlowStepType.GO_TO_PROCESS.getCode());
        return dto;
    }

    private List<FlowCaseEntity> getFlowCaseEntities(List<FlowUserType> flowUserTypes, FlowCase flowCase) {
        try {
            if (flowUserTypes.size() > 0) {
                return flowListenerManager.onFlowCaseDetailRender(flowCase, flowUserTypes.get(0));
            }
            return flowListenerManager.onFlowCaseDetailRender(flowCase, null);
        } catch (Exception e) {
            LOGGER.error("Flow module listener onFlowCaseDetailRender error, flowCaseId = "+flowCase.getId(), e);
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_DETAIL_RENDER,
                    "Flow module listener onFlowCaseDetailRender error, flowCaseId=%s", flowCase.getId());
        }
    }

    private List<FlowButtonDTO> getFlowButtonDTOList(FlowGraph flowGraph, Long userId, List<FlowUserType> flowUserTypes, boolean checkProcessor, FlowCase flowCase, List<FlowNode> nodes, List<FlowLane> laneList) {
        List<FlowButtonDTO> btnList = new ArrayList<>();
        if (flowUserTypes.contains(FlowUserType.PROCESSOR)) {
            btnList.addAll(getProcessorButtonDTOList(flowGraph, userId, checkProcessor, flowCase, nodes, laneList));
        }
        if (flowUserTypes.contains(FlowUserType.APPLIER)) {
            btnList.addAll(getApplierButtonDTOList(flowGraph, flowCase));
        }
        //SUPERVISOR at last
        return btnList;
    }

    private List<FlowButtonDTO> getProcessorButtonDTOList(FlowGraph flowGraph, Long userId, boolean checkProcessor, FlowCase flowCase, List<FlowNode> nodes, List<FlowLane> laneList) {
        List<FlowButtonDTO> btnList = new ArrayList<>();
        if (!checkProcessor || (null != flowEventLogProvider.isProcessor(userId, flowCase))) {
            Map<Long, FlowLane> laneMap = new HashMap<>();
            // 当前泳道的绝对level
            int currAbsLaneLevel = 0;
            boolean found = false;
            for (FlowLane lane : laneList) {
                laneMap.put(lane.getId(), lane);
                if (!found) {
                    if (!flowCase.getCurrentLaneId().equals(lane.getId())) {
                        currAbsLaneLevel++;
                    } else {
                        found = true;
                    }
                }
            }
            if (currAbsLaneLevel == laneList.size()) {
                //not found
                currAbsLaneLevel = 0;
            }

            final Integer _currAbsLaneLevel = currAbsLaneLevel;

            // List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(), flowCase.getFlowVersion(), FlowUserType.PROCESSOR.getCode());
            List<FlowButton> buttons = flowGraph.getGraphNode(flowCase.getCurrentNodeId()).getProcessorButtons()
                    .stream().map(FlowGraphButton::getFlowButton).collect(Collectors.toList());
            // 排序
            buttons.sort(Comparator.comparingInt(FlowButton::getDefaultOrder));

            buttons.forEach((button) -> {
                boolean isAdd = true;

                // 第一个节点的驳回按钮特殊处理
                if (flowCase.getCurrentNodeId().equals(nodes.get(1).getId())
                        && button.getFlowStepType().equals(FlowStepType.REJECT_STEP.getCode())) {
                    isAdd = false;
                }

                if (isAdd && button.getStatus().equals(FlowButtonStatus.ENABLED.getCode())) {
                    FlowButtonDTO btnDTO = flowButtonToDTOV2(button, laneMap, _currAbsLaneLevel);
                    if (button.getFlowStepType().equals(FlowStepType.APPROVE_STEP.getCode())) {
                        Tuple<Long, Byte> nextBranchTuple = getSelectNextBranchFlag(flowGraph, flowCase, nodes);
                        btnDTO.setConditionNodeId(nextBranchTuple.first());
                        btnDTO.setNeedSelectBranch(nextBranchTuple.second());
                    }
                    btnList.add(btnDTO);
                }
            });
        }
        return btnList;
    }

    private Tuple<Long, Byte> getSelectNextBranchFlag(FlowGraph flowGraph, FlowCase flowCase, List<FlowNode> nodes) {
        List<FlowLink> linksOut = flowGraph.getGraphNode(flowCase.getCurrentNodeId()).getLinksOut().stream()
                .map(FlowGraphLink::getFlowLink).collect(Collectors.toList());

        // List<FlowLink> linkOut = flowLinkProvider.listFlowLinkByFromNodeId(flowCase.getCurrentNodeId(), flowCase.getFlowVersion());
        if (linksOut.size() != 1) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_CASE_NOEXISTS,
                    "flow link size error, flowCaseId = %s, nodeId = %s", flowCase.getId(), flowCase.getCurrentNodeId());
        }

        TrueOrFalseFlag[] flag = {TrueOrFalseFlag.FALSE};
        Long nextNodeId = linksOut.get(0).getToNodeId();
        nodes.stream().filter(r -> Objects.equals(nextNodeId, r.getId())).findFirst().ifPresent(r -> {
            if (r.getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {
                FlowBranch branch = flowGraph.getBranchByOriginalNode(nextNodeId).getFlowBranch();
                if (branch.getBranchDecider().equals(FlowBranchDecider.PROCESSOR.getCode())) {
                    flag[0] = TrueOrFalseFlag.TRUE;
                }
            }
        });
        return new Tuple<>(nextNodeId, flag[0].getCode());
    }

    private List<FlowButtonDTO> getApplierButtonDTOList(FlowGraph flowGraph, FlowCase flowCase) {
        List<FlowButtonDTO> btnList = new ArrayList<>();

        Long currentNodeId = flowCase.getCurrentNodeId();
        FlowGraphLane graphLane = flowGraph.getGraphLane(flowCase.getCurrentLaneId());

        if (graphLane != null && graphLane.getFlowLane().getIdentifierNodeId() != null) {
            currentNodeId = graphLane.getFlowLane().getIdentifierNodeId();
        } else {
            // 在流程走到子flowCase里的时候，申请人的按钮需要特殊处理
            currentNodeId = getRealCurrentNodeId(flowCase, currentNodeId);
        }

        List<FlowButton> buttons = flowGraph.getGraphNode(currentNodeId).getApplierButtons()
                .stream().map(FlowGraphButton::getFlowButton).collect(Collectors.toList());

        for (FlowButton button : buttons) {
            if (button.getStatus().equals(FlowButtonStatus.ENABLED.getCode())) {
                FlowButtonDTO btnDTO = ConvertHelper.convert(button, FlowButtonDTO.class);
                FlowStepType stepType = FlowStepType.fromCode(button.getFlowStepType());
                if (stepType == FlowStepType.REMINDER_STEP) {
                    btnDTO.setNeedSubject((byte) 0);
                    btnDTO.setSubjectRequiredFlag(TrueOrFalseFlag.FALSE.getCode());
                }
                if (stepType == FlowStepType.EVALUATE_STEP && TrueOrFalseFlag.TRUE.getCode().equals(flowCase.getEvaluateStatus())) {
                    continue;
                }
                btnList.add(btnDTO);
            }
        }

        // 结束后可以评价的按钮
        if (flowCase.getStatus().equals(FlowCaseStatus.FINISHED.getCode())
                && flowGraph.getFlow().getAllowFlowCaseEndEvaluate().equals(TrueOrFalseFlag.TRUE.getCode())
                && flowCase.getEvaluateStatus().equals(TrueOrFalseFlag.FALSE.getCode())) {
            FlowButtonDTO evalBtn = new FlowButtonDTO();
            evalBtn.setButtonName(buttonDefName(flowCase.getNamespaceId(), FlowStepType.EVALUATE_STEP));
            evalBtn.setFlowStepType(FlowStepType.EVALUATE_STEP.getCode());
            btnList.add(evalBtn);
        }
        return btnList;
    }

    private Long getRealCurrentNodeId(FlowCase flowCase, Long currentNodeId) {
        FlowNode flowNode = flowNodeProvider.getFlowNodeById(currentNodeId);
        if (FlowNodeType.CONDITION_FRONT.getCode().equals(flowNode.getNodeType())) {
            List<FlowCase> subCases = flowCaseProvider.listFlowCaseByParentId(flowCase.getId());
            // 如果有子分支，则拿子分支的currentNode的按钮
            // 1, 如果只是单一分支，那就没错，就是子分支的节点按钮
            // 2, 如果遇到并发分支，则随便取一个case子分支
            if (subCases.size() > 0) {
                return getRealCurrentNodeId(subCases.get(0), subCases.get(0).getCurrentNodeId());
            }
        }
        return currentNodeId;
    }

    private List<FlowLaneLogDTO> getFlowLaneLogDTOList(FlowGraph flowGraph, List<FlowUserType> flowUserTypes,
                                                       FlowCase flowCase, List<FlowCase> allFlowCase, List<FlowLane> laneList) {
        List<Long> flowCaseIdList = allFlowCase.stream().map(FlowCase::getId).collect(Collectors.toList());
        //got all nodes tracker logs
        List<FlowEventLog> stepLogs = flowEventLogProvider.findStepEventLogs(flowCaseIdList);

        FlowNode currNode = null;
        boolean absorted = false;

        List<FlowNodeLogDTO> nodeDTOS = new ArrayList<>();

        // 构建有stepLog的节点信息
        for (FlowEventLog eventLog : stepLogs) {
            //获取工作流经过的节点日志
            if (currNode == null || !currNode.getId().equals(eventLog.getFlowNodeId())) { // 相邻相同去重
                currNode = flowGraph.getGraphNode(eventLog.getFlowNodeId()).getFlowNode();
                final FlowNodeLogDTO nodeLogDTO = new FlowNodeLogDTO();
                nodeLogDTO.setNodeId(currNode.getId());
                nodeLogDTO.setNodeLevel(currNode.getNodeLevel());
                nodeLogDTO.setNodeName(currNode.getNodeName());
                nodeLogDTO.setParams(currNode.getParams());
                nodeLogDTO.setLaneId(currNode.getFlowLaneId());
                nodeLogDTO.setNodeEnterTime(eventLog.getCreateTime().getTime());

                for (FlowCase aCase : allFlowCase) {
                    if (eventLog.getFlowCaseId().equals(aCase.getId()) && aCase.getStepCount().equals(eventLog.getStepCount())) {
                        nodeLogDTO.setIsCurrentNode((byte) 1);
                        // dto.setCurrNodeParams(currNode.getParams());

                        // 下一步需要处理人选择下一步节点
                        // List<FlowLink> linksOut = flowLinkProvider.listFlowLinkByFromNodeId(currNode.getId(), aCase.getFlowVersion());
                        List<FlowLink> linksOut = flowGraph.getGraphNode(currNode.getId()).getLinksOut().stream().map(FlowGraphLink::getFlowLink).collect(Collectors.toList());

                        for (FlowLink flowLink : linksOut) {
                            FlowNode nextNode = flowGraph.getGraphNode(flowLink.getToNodeId()).getFlowNode();
                            if (nextNode.getNodeType().equals(FlowNodeType.CONDITION_FRONT.getCode())) {
                                // FlowBranch branch = flowBranchProvider.findBranch(nextNode.getId());
                                FlowGraphBranch graphBranch = flowGraph.getBranchByOriginalNode(nextNode.getId());
                                if (graphBranch != null
                                        && graphBranch.getFlowBranch().getBranchDecider().equals(FlowBranchDecider.PROCESSOR.getCode())) {
                                    nodeLogDTO.setNeedSelectNextNode(TrueOrFalseFlag.TRUE.getCode());
                                }
                            }
                        }

                        // 附言按钮,支持老版本
                        if (flowUserTypes.size() > 0) {
                            FlowButton commentBtn = flowButtonProvider.findFlowButtonByStepType(currNode.getId()
                                    , currNode.getFlowVersion(), FlowStepType.COMMENT_STEP.getCode(), flowUserTypes.get(0).getCode());
                            if (commentBtn != null && commentBtn.getStatus().equals(FlowButtonStatus.ENABLED.getCode())) {
                                nodeLogDTO.setAllowComment((byte) 1);
                                nodeLogDTO.setCommentButtonId(commentBtn.getId());
                            }
                        }
                        // --end

                        if (FlowStepType.ABSORT_STEP.getCode().equals(eventLog.getButtonFiredStep())) {
                            absorted = true;
                            nodeLogDTO.setNodeName(buttonDefName(UserContext.getCurrentNamespaceId(), FlowStepType.ABSORT_STEP));
                        }
                        break;
                    }
                }

                nodeDTOS.add(nodeLogDTO);

                getFlowNodeLogDTOV2(eventLog.getFlowCaseId(), flowUserTypes, currNode, eventLog.getStepCount(), nodeLogDTO);
            }
        }

        Map<Long, FlowLane> laneMap = null;
        if (laneList.get(0).getId() == 0) {
            // 还是兼容老版本
            laneMap = new HashMap<>();
        } else {
            laneMap = laneList.stream().collect(Collectors.toMap(FlowLane::getId, r -> r));
        }
        List<FlowLaneLogDTO> laneLogDTOS = new ArrayList<>();

        FlowLaneLogDTO startLaneDTO = new FlowLaneLogDTO();
        FlowLane startLane = laneList.get(0);
        startLaneDTO.setLaneLevel(startLane.getLaneLevel());
        startLaneDTO.setLaneId(startLane.getId());
        startLaneDTO.setLaneName(startLane.getDisplayName());
        laneLogDTOS.add(startLaneDTO);

        FlowLaneLogDTO prefixLane = null;
        for (FlowNodeLogDTO nodeLogDTO : nodeDTOS) {
            if (prefixLane != null && nodeLogDTO.getLaneId().equals(prefixLane.getLaneId()) && prefixLane.getLaneId() != 0/*还是老版本*/) { // 相邻相同去重
                // 判断当前泳道
                if (flowCase.getCurrentLaneId().equals(prefixLane.getLaneId()) && Objects.equals(nodeLogDTO.getIsCurrentNode(), TrueOrFalseFlag.TRUE.getCode())) {
                    prefixLane.setIsCurrentLane(TrueOrFalseFlag.TRUE.getCode());
                    prefixLane.setNeedSelectNextNode(nodeLogDTO.getNeedSelectNextNode());
                }
                if (nodeLogDTO.getIsRejectNode() != null) {
                    prefixLane.setIsRejectLane(nodeLogDTO.getIsRejectNode());
                }
                prefixLane.getLogs().addAll(nodeLogDTO.getLogs());
            } else {
                FlowLane lane = laneMap.get(nodeLogDTO.getLaneId());
                // 还是兼容老版本
                if (lane == null) {
                    lane = new FlowLane();
                    lane.setId(0L);
                    lane.setDisplayName(nodeLogDTO.getNodeName());
                    lane.setLaneLevel(nodeLogDTO.getNodeLevel() + 1);
                }
                // end--

                prefixLane = ConvertHelper.convert(lane, FlowLaneLogDTO.class);
                prefixLane.setLaneId(lane.getId());
                prefixLane.setLaneName(lane.getDisplayName());
                prefixLane.setLogs(nodeLogDTO.getLogs());
                prefixLane.setLaneEnterTime(nodeLogDTO.getNodeEnterTime());
                if (nodeLogDTO.getIsRejectNode() != null) {
                    prefixLane.setIsRejectLane(nodeLogDTO.getIsRejectNode());
                }

                laneLogDTOS.add(prefixLane);
                // 判断当前泳道
                if (flowCase.getCurrentLaneId().equals(lane.getId()) && Objects.equals(nodeLogDTO.getIsCurrentNode(), TrueOrFalseFlag.TRUE.getCode())) {
                    prefixLane.setIsCurrentLane(TrueOrFalseFlag.TRUE.getCode());
                    prefixLane.setNeedSelectNextNode(nodeLogDTO.getNeedSelectNextNode());
                }
            }
        }

        for (FlowLaneLogDTO laneLogDTO : laneLogDTOS) {
            laneLogDTO.getLogs().sort(Comparator.comparing(FlowEventLogDTO::getCreateTime));
        }

        // 正常状态
        if (!absorted) {
            int i = 1;
            if (prefixLane != null) {
                i = prefixLane.getLaneLevel();
            }

            // 构建没有stepLog的节点信息
            for (; i <= laneList.size() - 1; i++) {
                prefixLane = new FlowLaneLogDTO();
                prefixLane.setLaneLevel(laneList.get(i).getLaneLevel());
                prefixLane.setLaneName(laneList.get(i).getDisplayName());
                laneLogDTOS.add(prefixLane);
            }

            /*if (!flowCase.getStatus().equals(FlowCaseStatus.PROCESS.getCode())) {
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
            nodeDTOS.add(nodeDTO);*/
        }
        // 终止状态
        else {
            FlowLaneLogDTO laneLogDTO = laneLogDTOS.get(laneLogDTOS.size() - 1);
            FlowLane lane = laneMap.get(laneLogDTO.getLaneId());
            if (lane != null) {
                laneLogDTO.setLaneName(lane.getDisplayNameAbsort());
            }
            laneLogDTO.setIsAbsortLane(TrueOrFalseFlag.TRUE.getCode());
            //异常结束 BUG 6052, 本来取消任务的跟踪是在中间节点的，要把他放到结束节点
            /*out:
            for (int j = laneLogDTOS.size() - 2; j >= 0; j--) {
                FlowLaneLogDTO laneLogDTO = laneLogDTOS.get(j);
                for (FlowEventLogDTO logDTO : laneLogDTO.getLogs()) {
                    if (FlowStepType.ABSORT_STEP.getCode().equals(logDTO.getButtonFiredStep())) {
                        laneLogDTOS.get(laneLogDTOS.size() - 1).getLogs().add(logDTO);
                        laneLogDTO.getLogs().remove(logDTO);
                        laneLogDTO.getLogs().sort(Comparator.comparing(FlowEventLogDTO::getCreateTime));
                        break out;
                    }
                }
               *//* if (tmpDTO.getLogs().size() > 0) {
                    laneLogDTOS.get(laneLogDTOS.size() - 1).getLogs().add(tmpDTO.getLogs().get(tmpDTO.getLogs().size() - 1));
                    tmpDTO.getLogs().remove(tmpDTO.getLogs().size() - 1);
                    break;
                }*//*
            }*/
        }
        return laneLogDTOS;
    }

    private void getFlowNodeLogDTOV2(Long flowCaseId, List<FlowUserType> flowUserTypes, FlowNode currNode, Long stepCount, FlowNodeLogDTO nodeLogDTO) {
        List<FlowEventLog> trackerLogs = flowEventLogProvider.findEventLogsByNodeId(currNode.getId(), flowCaseId, stepCount, flowUserTypes);
        if (trackerLogs != null) {
            trackerLogs.forEach((t) -> {
                FlowEventLogDTO eventDTO = ConvertHelper.convert(t, FlowEventLogDTO.class);
                if (FlowStepType.EVALUATE_STEP.getCode().equals(t.getButtonFiredStep())) {
                    eventDTO.setIsEvaluate((byte) 1);
                }
                nodeLogDTO.getLogs().add(eventDTO);
            });
        }

        // 驳回节点需要特殊显示
        List<FlowEventLog> rejectTrackerLogs = flowEventLogProvider.findRejectEventLogsByNodeId(currNode.getId(), flowCaseId, stepCount);
        if (rejectTrackerLogs != null) {
            for (FlowEventLog rejectLog : rejectTrackerLogs) {
                if (Objects.equals(rejectLog.getCrossLaneRejectFlag(), 1L)) {
                    nodeLogDTO.setIsRejectNode(TrueOrFalseFlag.TRUE.getCode());
                    break;
                }
            }
        }
    }

    private FlowButtonDTO flowButtonToDTOV2(FlowButton button, Map<Long, FlowLane> laneMap, Integer currAbsLaneLevel) {
        FlowButtonDTO btnDTO = ConvertHelper.convert(button, FlowButtonDTO.class);

        FlowStepType stepType = FlowStepType.fromCode(button.getFlowStepType());
        if (stepType == FlowStepType.TRANSFER_STEP) {
            // force use processor
            btnDTO.setNeedProcessor((byte) 1);
        } else if (stepType == FlowStepType.APPROVE_STEP && currAbsLaneLevel >= laneMap.size() - 2) {
            btnDTO.setNeedProcessor((byte) 0);
        } else if (stepType != FlowStepType.APPROVE_STEP) {
            btnDTO.setNeedProcessor((byte) 0);
        }
        // default with user settings needProcessor
        return btnDTO;
    }

    private FlowConditionDTO createFlowCondition(Flow flow, FlowConditionCommand conditionCmd) {
        FlowCondition condition = new FlowCondition();
        condition.setNamespaceId(flow.getNamespaceId());
        condition.setFlowMainId(flow.getId());
        condition.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
        condition.setStatus(FlowCommonStatus.ACTIVE.getCode());
        condition.setConditionLevel(conditionCmd.getConditionLevel());

        condition.setFlowNodeId(conditionCmd.getFlowNodeId());
        condition.setFlowNodeLevel(conditionCmd.getFlowNodeLevel());

        condition.setNextNodeLevel(conditionCmd.getNextNodeLevel());
        condition.setNextNodeId(conditionCmd.getNextNodeId());

        condition.setFlowLinkLevel(conditionCmd.getFlowLinkLevel());
        condition.setFlowLinkId(conditionCmd.getFlowLinkId());

        flowConditionProvider.createFlowCondition(condition);

        if (conditionCmd.getExpressions() != null) {
            flowConditionExpressionProvider.deleteFlowConditionExpression(flow.getFlowMainId(), FlowConstants.FLOW_CONFIG_VER);
            for (FlowConditionExpressionCommand expressionCommand : conditionCmd.getExpressions()) {
                createFlowConditionExpression(flow, condition, expressionCommand);
            }
        }
        return toFlowConditionDTO(condition);
    }

    private void createFlowConditionExpression(Flow flow, FlowCondition condition, FlowConditionExpressionCommand expressionCmd) {
        FlowConditionExpression exp = new FlowConditionExpression();
        exp.setStatus(FlowCommonStatus.ACTIVE.getCode());
        exp.setNamespaceId(flow.getNamespaceId());
        exp.setFlowMainId(flow.getId());
        exp.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
        exp.setFlowConditionId(condition.getId());
        exp.setLogicOperator(expressionCmd.getLogicOperator());
        exp.setRelationalOperator(expressionCmd.getRelationalOperator());
        exp.setVariable1(expressionCmd.getVariable1());
        exp.setVariable2(expressionCmd.getVariable2());
        exp.setVariableType1(expressionCmd.getVariableType1());
        exp.setVariableType2(expressionCmd.getVariableType2());

        flowConditionExpressionProvider.createFlowConditionExpression(exp);
    }

    private FlowLinkDTO createFlowLink(Flow flow, FlowLinkCommand flowLinkCmd) {
        FlowLink flowLink = new FlowLink();
        flowLink.setStatus(FlowCommonStatus.ACTIVE.getCode());
        flowLink.setNamespaceId(flow.getNamespaceId());
        flowLink.setFlowMainId(flow.getId());
        flowLink.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
        flowLink.setLinkLevel(flowLinkCmd.getLinkLevel());

        flowLink.setToNodeId(flowLinkCmd.getToNodeId());
        flowLink.setToNodeLevel(flowLinkCmd.getToNodeLevel());

        flowLink.setFromNodeId(flowLinkCmd.getFromNodeId());
        flowLink.setFromNodeLevel(flowLinkCmd.getFromNodeLevel());

        flowLinkProvider.createFlowLink(flowLink);
        return toFlowLinkDTO(flowLink);
    }

    private FlowLaneDTO createFlowLane(Flow flow, FlowLaneCommand flowLaneCmd, String absortDisplayName) {
        FlowLane flowLane = new FlowLane();
        if (flowLaneCmd.getId() != null) {
            flowLane = flowLaneProvider.findById(flowLaneCmd.getId());
            flowLane.setFlowMainId(flow.getId());
            flowLane.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
            flowLane.setLaneLevel(flowLaneCmd.getLaneLevel());
            flowLane.setNamespaceId(flow.getNamespaceId());
            flowLane.setStatus(FlowCommonStatus.ACTIVE.getCode());
            flowLane.setIdentifierNodeLevel(flowLaneCmd.getIdentifierNodeLevel());
            flowLaneProvider.updateFlowLane(flowLane);
        } else {
            flowLane.setDisplayNameAbsort(absortDisplayName);
            flowLane.setDisplayName(flowLaneCmd.getDisplayName());
            flowLane.setFlowMainId(flow.getId());
            flowLane.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
            flowLane.setLaneLevel(flowLaneCmd.getLaneLevel());
            flowLane.setFlowNodeLevel(flowLaneCmd.getFlowNodeLevel());
            flowLane.setNamespaceId(flow.getNamespaceId());
            flowLane.setStatus(FlowCommonStatus.ACTIVE.getCode());
            flowLane.setIdentifierNodeLevel(flowLaneCmd.getIdentifierNodeLevel());
            flowLaneProvider.createFlowLane(flowLane);
        }
        return toFlowLaneDTO(flowLane);
    }

    private FlowConditionDTO toFlowConditionDTO(FlowCondition condition) {
        FlowConditionDTO dto = ConvertHelper.convert(condition, FlowConditionDTO.class);
        List<FlowConditionExpression> exps = flowConditionExpressionProvider.listFlowConditionExpression(condition.getId());
        dto.setExpressions(exps.stream().map(this::toFlowConditionExpressionDTO).collect(Collectors.toList()));
        return dto;
    }

    private FlowConditionExpressionDTO toFlowConditionExpressionDTO(FlowConditionExpression expression) {
        return ConvertHelper.convert(expression, FlowConditionExpressionDTO.class);
    }

    private FlowNodeDTO toFlowNodeDTO(FlowNode flowNode) {
        FlowNodeDetailDTO flowNodeDetail = this.getFlowNodeDetail(flowNode.getId());
        return ConvertHelper.convert(flowNodeDetail, FlowNodeDTO.class);
    }

    private FlowLinkDTO toFlowLinkDTO(FlowLink flowLink) {
        return ConvertHelper.convert(flowLink, FlowLinkDTO.class);
    }

    private FlowLaneDTO toFlowLaneDTO(FlowLane flowLane) {
        return ConvertHelper.convert(flowLane, FlowLaneDTO.class);
    }

    private FlowBranchDTO toFlowBranchDTO(FlowBranch flowBranch) {
        return ConvertHelper.convert(flowBranch, FlowBranchDTO.class);
    }

    private FlowGraphDTO toFlowGraphDTO(Flow flow) {
        FlowGraphDTO flowGraph = new FlowGraphDTO();
        flowGraph.setFlowId(flow.getId());

        List<FlowNode> nodeList = flowNodeProvider.findFlowNodesByFlowId(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
        List<FlowNodeDTO> nodes = nodeList.stream().map(this::toFlowNodeDTO).collect(Collectors.toList());
        flowGraph.setNodes(nodes);

        List<FlowLink> linkList = flowLinkProvider.listFlowLink(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
        List<FlowLinkDTO> links = linkList.stream().map(this::toFlowLinkDTO).collect(Collectors.toList());
        flowGraph.setLinks(links);

        List<FlowLane> laneList = flowLaneProvider.listFlowLane(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
        List<FlowLaneDTO> lanes = laneList.stream().map(this::toFlowLaneDTO).collect(Collectors.toList());
        lanes.sort(Comparator.comparingInt(FlowLaneDTO::getLaneLevel));
        flowGraph.setLanes(lanes);

        List<FlowCondition> conditions = flowConditionProvider.listFlowCondition(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
        flowGraph.setConditions(conditions.stream().map(this::toFlowConditionDTO).collect(Collectors.toList()));

        return flowGraph;
    }

    @Override
    public FlowEvaluateItemDTO createFlowEvaluateItem(CreateFlowEvaluateItemCommand cmd) {
        Flow flow = flowProvider.getFlowById(cmd.getFlowId());
        if (flow == null || !flow.getFlowMainId().equals(0L)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flowId not exists");
        }

        FlowEvaluateItem evaluateItem = this.dbProvider.execute(status -> {
            flowMarkUpdated(flow);

            List<FlowEvaluateItem> configItems = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(
                    flow.getId(), FlowConstants.FLOW_CONFIG_VER);

            if (configItems != null && configItems.size() > 5) {
                throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_EVALUATE_ITEM_SIZE_ERROR,
                        "items size error!");
            }

            FlowEvaluateItem item = new FlowEvaluateItem();
            item.setFlowMainId(flow.getId());
            item.setFlowVersion(FlowConstants.FLOW_CONFIG_VER);
            item.setNamespaceId(flow.getNamespaceId());
            item.setName(cmd.getName());
            item.setInputFlag(cmd.getInputFlag());

            flowEvaluateItemProvider.createFlowEvaluateItem(item);
            return item;
        });
        return ConvertHelper.convert(evaluateItem, FlowEvaluateItemDTO.class);
    }

    @Override
    public void deleteFlowEvaluateItem(DeleteFlowEvaluateItemCommand cmd) {
        Flow flow = flowProvider.getFlowById(cmd.getFlowId());
        if (flow == null || !flow.getFlowMainId().equals(0L)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flowId not exists");
        }

        this.dbProvider.execute(status -> {
            flowMarkUpdated(flow);

            FlowEvaluateItem item = flowEvaluateItemProvider.getFlowEvaluateItemById(cmd.getItemId());
            if (item != null) {
                flowEvaluateItemProvider.deleteFlowEvaluateItem(item);
            }
            return item;
        });
    }

    @Override
    public FlowEvaluateItemDTO updateFlowEvaluateItem(CreateFlowEvaluateItemCommand cmd) {
        Flow flow = flowProvider.getFlowById(cmd.getFlowId());
        if (flow == null || !flow.getFlowMainId().equals(0L)) {
            throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_NOT_EXISTS,
                    "flowId not exists");
        }

        FlowEvaluateItem evaluateItem = this.dbProvider.execute(status -> {
            FlowEvaluateItem item = flowEvaluateItemProvider.getFlowEvaluateItemById(cmd.getItemId());
            if (item != null) {
                flowMarkUpdated(flow);
                item.setName(cmd.getName());
                item.setInputFlag(cmd.getInputFlag());
                flowEvaluateItemProvider.updateFlowEvaluateItem(item);
            }
            return item;
        });
        return ConvertHelper.convert(evaluateItem, FlowEvaluateItemDTO.class);
    }

    public String getLocalString(int code) {
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        User user = UserContext.current().getUser();
        if (user != null) {
            locale = user.getLocale();
        }
        return localeStringService.getLocalizedString(FlowTemplateCode.SCOPE, code + "", locale, "");
    }

    @Override
    public List<FlowCase> getAllFlowCase(Long flowCaseId) {
        FlowCase grantParentCase = flowCaseProvider.getFlowCaseById(flowCaseId);
        while (grantParentCase.getParentId() != 0) {
            grantParentCase = flowCaseProvider.getFlowCaseById(grantParentCase.getParentId());
        }
        return getAllChildFlowCase(grantParentCase);
    }

    private List<FlowCase> getAllChildFlowCase(FlowCase parentCase) {
        List<FlowCase> subFlowCases = flowCaseProvider.listFlowCaseByParentId(parentCase.getId());
        List<FlowCase> flowCases = new ArrayList<>();
        flowCases.add(parentCase);
        for (FlowCase flowCase : subFlowCases) {
            flowCases.addAll(getAllChildFlowCase(flowCase));
        }
        return flowCases;
    }
}