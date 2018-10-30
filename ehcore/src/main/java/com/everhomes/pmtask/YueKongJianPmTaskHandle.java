package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.List;
import java.util.stream.Collectors;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.YUE_KONG_JIAN)
class YueKongJianPmTaskHandle extends DefaultPmTaskHandle {

	private static final Logger LOGGER = LoggerFactory.getLogger(YueKongJianPmTaskHandle.class);
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private PmTaskProvider pmTaskProvider;
	@Autowired
	private PmTaskSearch pmTaskSearch;
	@Autowired
	private FlowService flowService;
	@Autowired
	private PmTaskCommonServiceImpl pmTaskCommonService;
	@Autowired
	private FlowCaseProvider flowCaseProvider;
	@Autowired
	private FlowNodeProvider flowNodeProvider;
	@Autowired
	private BuildingProvider buildingProvider;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private FlowButtonProvider flowButtonProvider;
	@Autowired
	private CategoryProvider categoryProvider;
	@Autowired
	private PortalService portalService;

	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd, Long requestorUid, String requestorName, String requestorPhone){

		PmTask task1 = dbProvider.execute((TransactionStatus status) -> {
			PmTask task = pmTaskCommonService.createTask(cmd, requestorUid, requestorName, requestorPhone);
			//新建flowcase
			Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
			Flow flow = null;

			Long parentTaskId = categoryProvider.findCategoryById(cmd.getTaskCategoryId()).getParentId();
			if (parentTaskId == PmTaskAppType.SUGGESTION_ID)
				flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
						FlowModuleType.SUGGESTION_MODULE.getCode(), cmd.getOwnerId(), FlowOwnerType.PMTASK.getCode());
			else
				// if (cmd.getTaskCategoryId()==PmTaskAppType.REPAIR_ID)
				flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
						FlowModuleType.NO_MODULE.getCode(), cmd.getOwnerId(), FlowOwnerType.PMTASK.getCode());

			if(null == flow) {
				LOGGER.error("Enable pmtask flow not found, moduleId={}", FlowConstants.PM_TASK_MODULE);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
						"Enable pmtask flow not found.");
			}
			CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
			Category taskCategory = categoryProvider.findCategoryById(task.getTaskCategoryId());

			ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
			listServiceModuleAppsCommand.setNamespaceId(namespaceId);
			listServiceModuleAppsCommand.setModuleId(FlowConstants.PM_TASK_MODULE);
			listServiceModuleAppsCommand.setCustomTag(String.valueOf(parentTaskId));
			ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);

			if (apps!=null && apps.getServiceModuleApps().size()>0)
				createFlowCaseCommand.setTitle(apps.getServiceModuleApps().get(0).getName());
			else
				createFlowCaseCommand.setTitle(taskCategory.getName());
			createFlowCaseCommand.setServiceType(taskCategory.getName());
			if (requestorUid!=null)
				createFlowCaseCommand.setApplyUserId(requestorUid);
			else
				createFlowCaseCommand.setApplyUserId(UserContext.currentUserId());
			createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
			createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
			createFlowCaseCommand.setReferId(task.getId());
			createFlowCaseCommand.setReferType(EntityType.PM_TASK.getCode());
			String content = "服务内容:"+task.getContent()+"\n";
			content += "服务地点:"+task.getAddress();
			createFlowCaseCommand.setContent(content);
			createFlowCaseCommand.setCurrentOrganizationId(cmd.getFlowOrganizationId());

			createFlowCaseCommand.setProjectId(task.getOwnerId());
			createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());
			if (StringUtils.isNotBlank(task.getBuildingName())) {
				Building building = buildingProvider.findBuildingByName(namespaceId, task.getOwnerId(),
						task.getBuildingName());
				if(building != null){
					ResourceCategoryAssignment resourceCategory = communityProvider.findResourceCategoryAssignment(building.getId(),
							EntityType.BUILDING.getCode(), namespaceId);
					if (null != resourceCategory) {
						createFlowCaseCommand.setProjectIdA(resourceCategory.getResourceCategryId());
						createFlowCaseCommand.setProjectTypeA(EntityType.CHILD_PROJECT.getCode());
					}
				}
			}

			FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);

//			if(StringUtils.isBlank(params)) {
//				LOGGER.error("Invalid flowNode param.");
//				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
//						"Invalid flowNode param.");
//			}

//			JSONObject paramJson = JSONObject.parseObject(params);
//			String nodeType = paramJson.getString("nodeType");
			task.setStatus(FlowCaseStatus.PROCESS.getCode());
			task.setFlowCaseId(flowCase.getId());
			pmTaskProvider.updateTask(task);
			return task;
		});

		pmTaskSearch.feedDoc(task1);

		return ConvertHelper.convert(task1, PmTaskDTO.class);
	}

	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
		return pmTaskCommonService.getTaskDetail(cmd, false);
	}

	public ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd) {
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		final List<PmTask> list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), UserContext.current().getUser().getId(),
				cmd.getPageAnchor(), pageSize);
		ListUserTasksResponse response = new ListUserTasksResponse();
		int size = list.size();
		if(size > 0){
			response.setRequests(list.stream().map(r -> {
				PmTaskDTO dto = ConvertHelper.convert(r, PmTaskDTO.class);

				Category category = categoryProvider.findCategoryById(r.getCategoryId());
				Category taskCategory = checkCategory(r.getTaskCategoryId());
				if(null != category)
					dto.setCategoryName(category.getName());
				dto.setTaskCategoryName(taskCategory.getName());

				return dto;
			}).collect(Collectors.toList()));
			if(size != pageSize){
				response.setNextPageAnchor(null);
			}else{
				response.setNextPageAnchor(list.get(size-1).getCreateTime().getTime());
			}
		}

		return response;
	}

	private Category checkCategory(Long id){
		Category category = categoryProvider.findCategoryById(id);
		if(null == category) {
			LOGGER.error("Category not found, categoryId={}", id);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_NOT_EXIST,
					"Category not found.");
		}
		return category;
	}

	@Override
	public void updateTaskByOrg(UpdateTaskCommand cmd) {
		super.updateTaskByOrg(cmd);

		PmTask task = pmTaskProvider.findTaskById(cmd.getTaskId());
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(task.getFlowCaseId());
		//TODO:autoStep 没有消息 暂时改成firebutton
//		FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
//		stepDTO.setFlowCaseId(flowCase.getId());
//		stepDTO.setFlowMainId(flowCase.getFlowMainId());
//		stepDTO.setFlowVersion(flowCase.getFlowVersion());
//		stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
//		stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//		stepDTO.setStepCount(flowCase.getStepCount());
//		flowService.processAutoStep(stepDTO);

		List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getFlowMainId(), flowCase.getCurrentNodeId(),
				flowCase.getFlowVersion(), FlowUserType.PROCESSOR.getCode());

		FlowButton button = null;
		for (FlowButton b: buttons) {
			if (FlowStepType.APPROVE_STEP.getCode().equals(b.getFlowStepType()))
				button = b;
		}

		FlowFireButtonCommand fireButtonCommand = new FlowFireButtonCommand();
		fireButtonCommand.setFlowCaseId(flowCase.getId());
		fireButtonCommand.setButtonId(button.getId());
		flowService.fireButton(fireButtonCommand);
	}

}
