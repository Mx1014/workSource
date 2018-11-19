package com.everhomes.flow;

import java.util.List;
import java.util.Map;

import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.user.UserInfo;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;

public interface FlowService {
	
	/**
	 * 启用某一个业务模块下的工作流
	 * @param flowId
	 * @return
	 */
	Boolean enableFlow(Long flowId);
	
	/**
	 * 禁用某个 Flow
	 * @param flowId
	 */
	void disableFlow(Long flowId);
	
	/**
	 * 获取当前运行的 Flow 下的某一个节点的任务。业务上层需要此接口进行搜索
	 * @param snapshotFlow
	 * @param nodeNumber
	 * @return
	 */
	List<FlowCase> findFlowCasesByNodeNumber(Flow snapshotFlow, Integer nodeNumber);
	
	/**
	 * 客户端获取某个 flow 状态下的 FlowCase，flowMainId 为通用的所有副本的 flow
	 * @param flowId
	 * @param caseStatus
	 * @return
	 */
	List<FlowCase> findFlowCasesByFlowId(Long flowMainId, FlowCaseStatus caseStatus);
	
	/**
	 * 获取某个用户的 FlowCase
	 * @param userId
	 * @param status
	 * @return
	 */
	List<FlowCase> findFlowCasesByUserId(Long userId, FlowCaseStatus status);
	
	/**
	 * 获取当前运行中的 FlowCase 的 Buttons
	 * @param flowCase
	 * @return
	 */
	List<FlowButton> findFlowCaseButtons(FlowCase flowCase);
	
	/**
	 * 客户端触发按钮的事件。
	 * @param button
	 * @param flowCase
	 * @param formValues
	 */
	void fireFlowButton(FlowButton button, FlowCase flowCase, Map<String, String> formValues);
	
	/**
	 * 业务模块使用，通过名字找到按钮，从而可以动态触发事件。
	 * @param flowNode
	 * @param buttonName
	 * @return
	 */
	FlowButton findFlowButtonByName(FlowNode flowNode, String buttonName);
	
	/**
	 * 获取某一个 FlowCase 的所有处理信息日志
	 * @param flowCase
	 * @return
	 */
	List<FlowEventLog> findFlowEventLogsByFlowCase(FlowCase flowCase);
	
	/**
	 * 获取某一个 FlowNode 的详细处理信息，比如附言，则一个节点会有很多日志。
	 * @param flowCase
	 * @param flowNode
	 * @return
	 */
	List<FlowEventLog> findFlowEventDetail(FlowCase flowCase, FlowNode flowNode);

	/**
	 * 来自管理后台的创建 Flow 的请求
	 * @param cmd
	 * @return
	 */
	FlowDTO createFlow(CreateFlowCommand cmd);

	/**
	 * 列出 Flow 的简要列表
	 * @param cmd
	 * @return
	 */
	ListFlowBriefResponse listBriefFlows(ListFlowCommand cmd);

	/**
	 * 删除某一个工作流
	 * @param flowId
	 * @return 
	 */
	Flow deleteFlow(Long flowId);

	/**
	 * 修改工作流的名字
	 * @param cmd
	 */
	FlowDTO updateFlowName(UpdateFlowNameCommand cmd);

	/**
	 * 创建一个新的节点
	 * @param cmd
	 * @return
	 */
	FlowNodeDTO createFlowNode(CreateFlowNodeCommand cmd);

	/**
	 * 删除工作流的节点
	 * @param flowNodeId
	 * @return
	 */
	FlowNode deleteFlowNode(Long flowNodeId);

	ListBriefFlowNodeResponse listBriefFlowNodes(Long flowId);

	/**
	 * 更新所有节点的顺序
	 * @param cmd
	 * @return
	 */
	ListBriefFlowNodeResponse updateNodePriority(UpdateFlowNodePriorityCommand cmd);

	FlowNodeDTO updateFlowNode(UpdateFlowNodeCommand cmd);

	/**
	 * 创建用户选择项
	 * @param cmd
	 * @return
	 */
	ListFlowUserSelectionResponse createFlowUserSelection(CreateFlowUserSelectionCommand cmd);

	/**
	 * 获取某一个项目下的所有用户选择实体
	 * @param cmd
	 * @return
	 */
	ListFlowUserSelectionResponse listFlowUserSelection(ListFlowUserSelectionCommand cmd);

	/**
	 * 删除用户选择项
	 * @param cmd
	 * @return
	 */
	FlowUserSelectionDTO deleteUserSelection(DeleteFlowUserSelectionCommand cmd);
	
	/**
	 * 修改节点按钮信息
	 * @param cmd
	 * @return
	 */
	FlowButtonDetailDTO updateFlowButton(UpdateFlowButtonCommand cmd);

	/**
	 * 禁用按钮
	 * @param cmd
	 * @return
	 */
	FlowButtonDTO disableFlowButton(DisableFlowButtonCommand cmd);

	FlowButtonDTO enableFlowButton(Long buttonId);
	
	/**
	 * 修改任务节点的消息提醒
	 * @param cmd
	 * @return
	 */
	FlowNodeDetailDTO updateFlowNodeReminder(UpdateFlowNodeReminderCommand cmd);

	/**
	 * 任务跟踪
	 * @param cmd
	 * @return
	 */
	FlowNodeDetailDTO updateFlowNodeTracker(UpdateFlowNodeTrackerCommand cmd);

	/**
	 * 显示所有的变量列表
	 * @param cmd
	 * @return
	 */
	FlowVariableResponse listFlowVariables(ListFlowVariablesCommand cmd);

    FlowCase getFlowCaseById(Long flowCaseId);

	/**
	 * 获取当前任务的处理人列表
	 */
    FlowCaseProcessorsResolver getCurrentProcessors(Long flowCaseId, boolean allFlowCaseFlag);

    List<UserInfo> getSupervisor(FlowCase flowCase);

    /**
	 * 搜索 FlowCase 的信息
	 * @param cmd
	 * @return
	 */
	SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd);

	/**
	 * 创建工作流相关的帖子信息
	 * @param cmd
	 * @return
	 */
	FlowSubjectDTO postSubject(FlowPostSubjectCommand cmd);

    /**
	 * 触发工作流按钮事件的响应
	 * @param cmd
	 * @return
	 */
	FlowButtonDTO fireButton(FlowFireButtonCommand cmd);

	/**
	 * 在某一个节点中进行评价
	 * @param cmd
	 * @return
	 */
	FlowEvaluateDTO postEvaluate(FlowPostEvaluateCommand cmd);

	/**
	 * 获取按钮下个节点的处理人员选择
	 * @param cmd
	 * @return
	 */
	ListFlowUserSelectionResponse listButtonProcessorSelections(
			ListButtonProcessorSelectionsCommand cmd);

	/**
	 * 获取某配置节点的详细信息
	 * @param flowNodeId
	 * @return
	 */
	FlowNodeDetailDTO getFlowNodeDetail(Long flowNodeId);

	/**
	 * 获取 FlowButton 的详细信息
	 * @param cmd
	 * @return
	 */
	FlowButtonDetailDTO getFlowButtonDetail(Long flowButtonId);

	/**
	 * 获取某一个实体对应的顶级真实 Flow
	 * @param entityId
	 * @param entity
	 * @return
	 */
	Flow getFlowByEntity(Long entityId, FlowEntityType entity);

	/**
	 * 获取按钮的列表
	 * @param flowNodeId
	 * @return
	 */
	ListFlowButtonResponse listFlowNodeButtons(Long flowNodeId);

	/**
	 * 获取缓存的 FlowGraph
	 * @param flowId
	 * @param flowVer
	 * @return
	 */
	FlowGraph getFlowGraph(Long flowId, Integer flowVer);

	/**
	 * 返回模块信息
	 * @param cmd
	 * @return
	 */
	ListFlowModulesResponse listModules(ListFlowModulesCommand cmd);

	String parseActionTemplate(FlowCaseState ctx, Long actionId,
			String renderText);

	void flushState(FlowCaseState ctx) throws FlowStepBusyException;

	void createSnapshotNodeProcessors(FlowCaseState ctx, FlowGraphNode node);

	void createSnapshotSupervisors(FlowCaseState ctx);
	
	/**
	 * 获取当前业务模块下启用的工作流
	 * @param moduleId
	 * @param moduleType
	 * @param ownerId
	 * @param ownerType
	 * @return
	 */
	Flow getEnabledFlow(Integer namespaceId, Long moduleId, String moduleType,
			Long ownerId, String ownerType);

    Flow getEnabledFlow(Integer namespaceId, String projectType, Long projectId, Long moduleId, String moduleType, Long ownerId, String ownerType);

	Flow getAssociatedFlow(Integer namespaceId, String projectType, Long projectId, Long moduleId, String moduleType, Long ownerId, String ownerType);

	FlowServiceMapping getFlowServiceMapping(Integer namespaceId, String projectType, Long projectId, Long moduleId, String moduleType, Long ownerId, String ownerType);

	/**
	 * 添加一个 Case 到工作流中，注意此时为 snapshotFlow，即为运行中的 Flow 副本。
	 * @param flowCaseCmd
	 * @return
	 */
	FlowCase createFlowCase(CreateFlowCaseCommand flowCaseCmd);

	FlowModuleDTO getModuleById(Long moduleId);

    SearchFlowCaseResponse searchFlowCases(SearchFlowCaseCommand cmd, ListingQueryBuilderCallback callback);

    /**
	 * 获取 FlowCase 的详细日志信息
	 * @param flowCaseId
	 * @return
	 */
	FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId, Long userId,
			FlowUserType flowUserType);

	FlowCaseDetailDTO getFlowCaseDetail(Long flowCaseId, Long inUserId,
			FlowUserType flowUserType, boolean checkProcessor);
	
	/**
	 * Subject 的详细信息
	 * @param subjectId
	 * @return
	 */
	FlowSubjectDTO getSubectById(Long subjectId);

	/**
	 * 拿到 flow 的详细信息
	 * @param flowId
	 * @return
	 */
	FlowDTO getFlowById(Long flowId);

	/**
	 * step timeout
	 * @param ft
	 */
	void processStepTimeout(FlowTimeout ft);

    void processSubFlowEnd(FlowTimeout ft);

    /**
	 * message timeout
	 * @param ft
	 */
	void processMessageTimeout(FlowTimeout ft);
	
	/**
	 * Only for test
	 */
	void testFlowCase();

	String getFireButtonTemplate(FlowStepType step, Map<String, Object> map);

	FlowGraphDetailDTO getFlowGraphDetail(Long flowId);

	/** 注意通过 stepCount 与 flowNodeId 来避免多次提交而产生多次下一步
	 * @param stepDTO
	 */
	void processAutoStep(FlowAutoStepDTO stepDTO);

	/**
	 * 添加配置中所有节点的处理人员，同时修改正在运行的处理人员。不需要重新启动或关闭 flow
	 * @param flowId 配置当中的 flowId
	 * @param userId
	 */
	void addSnapshotProcessUser(Long flowId, Long userId);

	/**
	 * 删除配置中所有节点的处理人员，同时修改正在运行的处理人员。不需要重新启动或关闭 flow
	 * @param flowId 配置当中的 flowId
	 * @param userId
	 */
	void deleteSnapshotProcessUser(Long flowId, Long userId);

	List<Long> resolvUserSelections(FlowCaseState ctx, Map<String, Long> processedEntities,
			FlowEntityType entityType, Long entityId,
			List<FlowUserSelection> selections, int loopCnt);

	void clearFlowGraphCache(Long flowId);

	FlowEvaluateDetailDTO updateFlowEvaluate(UpdateFlowEvaluateCommand cmd);

	FlowEvaluateDetailDTO getFlowEvaluate(Long flowId);

	FlowEvaluateDTO getEvaluateInfo(Long flowCaseId);

	FlowSMSTemplateResponse listSMSTemplates(ListSMSTemplateCommand cmd);

	FlowResolveUsersResponse resolveSelectionUsers(Long flowId, Long selectionUserId);

    /**
     * 预先申请一个flowCaseId
     */
    Long getNextFlowCaseId();

    FlowCase createDumpFlowCase(GeneralModuleInfo ga,
                                CreateFlowCaseCommand flowCaseCmd);
 
	List<Long> resolvUserSelections(FlowCaseState ctx, FlowEntityType entityType, Long entityId,
			List<FlowUserSelection> selections);
 
	void processSMSTimeout(FlowTimeout ft);

    String getButtonFireEventContentTemplate(FlowStepType step, Map<String, Object> map);

    String templateRender(int code, Map<String, Object> map);

    String getStepMessageTemplate(FlowStepType fromStep, FlowCaseStatus nextStatus, FlowGraphEvent event, Map<String, Object> map);

	ListSelectUsersResponse listUserSelections(ListSelectUsersCommand cmd);

	List<UserInfo> listUserSelectionsByNode(FlowCaseState ctx, Long nodeId);

	UserInfo getPrefixProcessor(FlowCaseState ctx, Long prefixNodeId);

	UserInfo getCurrProcessor(FlowCaseState ctx, String variable);

	UserInfo getUserInfoInContext(FlowCaseState ctx, Long userId);

	List<Long> getApplierSelection(FlowCaseState ctx, FlowUserSelection sel);

	void fixupUserInfoInContext(FlowCaseState ctx, UserInfo ui);

    void fixupUserInfo(Long organizationId, UserInfo userInfo);

    /**
     * 删除flowCase
     */
    void deleteFlowCase(DeleteFlowCaseCommand cmd);

    ListFlowPredefinedParamResponse listPredefinedParam(ListPredefinedParamCommand cmd);

    void updateFlowButtonOrder(UpdateFlowButtonOrderCommand cmd);

    FlowLaneDTO updateFlowLane(UpdateFlowLaneCommand cmd);

    FlowButtonDTO createFlowButton(CreateFlowButtonCommand cmd);

    FlowGraphDTO getFlowGraphNew(FlowIdCommand cmd);

	FlowRuntimeScript toRuntimeScript(FlowScript script);

	FlowGraphDTO createOrUpdateFlowGraph(CreateFlowGraphCommand cmd);

    FlowGraphDTO createOrUpdateFlowGraph(CreateFlowGraphJsonCommand cmd);

    FlowCaseDetailDTOV2 getFlowCaseDetailByIdV2(Long flowCaseId, Long userId, FlowUserType flowUserType, boolean checkProcessor, boolean needButton);

    FlowCaseTrackDTO getFlowCaseTrack(GetFlowCaseTrackCommand cmd);

    FlowCaseBriefDTO getFlowCaseBrief(GetFlowCaseBriefCommand cmd);

    void deleteFlowButton(DeleteFlowButtonCommand cmd);

    ListFlowServiceTypeResponse listFlowServiceTypes(SearchFlowCaseCommand cmd);

    ListNextBranchesResponse listNextBranches(ListNextBranchesCommand cmd);

    SearchFlowOperateLogResponse searchFlowOperateLogs(SearchFlowOperateLogsCommand cmd);

    /**
     * 获取正在执行的任务列表（子任务 or 父任务）
     * @param flowCaseId  子任务id或者父任务id
     * @return  正在执行的任务列表 {@link FlowCaseTree}
     */
    FlowCaseTree getProcessingFlowCaseTree(Long flowCaseId);

    List<FlowEventLog> getNodeEnterLogs(Long flowCaseId, Long flowNodeId, Long stepCount);

    List<FlowEventLog> getNodeEnterLogsIgnoreCompleteFlag(Long flowCaseId, Long flowNodeId);

    List<FlowNodeLogDTO> getStepTrackerLogs(List<FlowCase> allFlowCase);

	FlowEvaluateItemDTO createFlowEvaluateItem(CreateFlowEvaluateItemCommand cmd);

    void deleteFlowEvaluateItem(DeleteFlowEvaluateItemCommand cmd);

    FlowEvaluateItemDTO updateFlowEvaluateItem(CreateFlowEvaluateItemCommand cmd);

    List<FlowCase> getAllFlowCase(Long flowCaseId);

    FlowGraphDTO createOrUpdateFlowCondition(CreateFlowConditionCommand cmd);

    void updateFlowValidationStatus(UpdateFlowValidationStatusCommand cmd);

    String getFlowCaseRouteURI(Long flowCaseId, Long moduleId);

    FlowConditionVariable getFormFieldValueByVariable(FlowCaseState ctx, String variable, String entityType, Long entityId, String extra);

    ListFlowConditionVariablesResponse listFlowConditionVariables(ListFlowConditionVariablesCommand cmd);

    ListFlowFormsResponse listFlowForms(ListFlowFormsCommand cmd);

    FlowFormDTO updateFlowFormVersion(UpdateFlowFormCommand cmd);

    FlowFormDTO createFlowForm(UpdateFlowFormCommand cmd);

    void deleteFlowForm(UpdateFlowFormCommand cmd);

    FlowFormDTO getFlowForm(GetFlowFormCommand cmd);

    FlowCaseDetailDTOV2 getFlowCaseDetailByRefer(Long moduleId, FlowUserType flowUserType,
                                                 Long userId, String referType, Long referId, boolean needFlowButton);

    GetFlowCaseCountResponse getFlowCaseCount(SearchFlowCaseCommand cmd);

    FlowScriptDTO createFlowScript(CreateFlowScriptCommand cmd);

    void deleteFlowScript(DeleteFlowScriptCommand cmd);

	FlowScriptDTO updateFlowScript(UpdateFlowScriptCommand cmd);

	ListFlowScriptsResponse listFlowScripts(ListFlowScriptsCommand cmd);

    void updateNeedAllProcessorComplete(UpdateNeedAllProcessorCompleteCommand cmd);

    ListFlowModuleAppsResponse listFlowModuleApps(SearchFlowCaseCommand cmd);

	ListFlowModuleAppServiceTypesResponse listFlowModuleAppServiceTypes(SearchFlowCaseCommand cmd);

    DeferredResult<Object> flowScriptMappingCall(Byte mode, Long id1, Long id2, String functionName, HttpServletRequest request);

    void updateFlowFormStatus(UpdateFlowFormStatusCommand cmd);

    void doFlowSnapshot(Long flowId);

	void createFlowServiceMapping(CreateFlowServiceMappingCommand cmd);

	List<FlowServiceMappingDTO> listFlowServiceMappings(ListFlowServiceMappingsCommand cmd);

	void updateSubFlowInfo(UpdateSubFlowInfoCommand cmd);

    FlowServiceMappingDTO getFlowServiceMapping(GetFlowServiceMappingCommand cmd);

	void enableProjectCustomize(EnableProjectCustomizeCommand cmd);

	void disableProjectCustomize(DisableProjectCustomizeCommand cmd);

    Byte getProjectCustomize(GetProjectCustomizeCommand cmd);

    void doFlowMirror(DoFlowMirrorCommand cmd);

	FlowGraphDTO getFlowGraphByFlowVersion(FlowIdVersionCommand cmd);
}
