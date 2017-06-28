package com.everhomes.pmtask;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.pmtask.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.FLOW)
class FlowPmTaskHandle implements PmTaskHandle {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlowPmTaskHandle.class);
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

	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd, Long requestorUid, String requestorName, String requestorPhone){

		PmTask task1 = dbProvider.execute((TransactionStatus status) -> {
			PmTask task = pmTaskCommonService.createTask(cmd, requestorUid, requestorName, requestorPhone);
			//新建flowcase
			Integer namespaceId = UserContext.getCurrentNamespaceId();
			Flow flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
					FlowModuleType.NO_MODULE.getCode(), cmd.getOwnerId(), FlowOwnerType.PMTASK.getCode());
			if(null == flow) {
				LOGGER.error("Enable pmtask flow not found, moduleId={}", FlowConstants.PM_TASK_MODULE);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
						"Enable pmtask flow not found.");
			}
			CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
			Category taskCategory = categoryProvider.findCategoryById(task.getTaskCategoryId());

			createFlowCaseCommand.setTitle(taskCategory.getName());
			createFlowCaseCommand.setApplyUserId(task.getCreatorUid());
			createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
			createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
			createFlowCaseCommand.setReferId(task.getId());
			createFlowCaseCommand.setReferType(EntityType.PM_TASK.getCode());
			//createFlowCaseCommand.setContent("发起人：" + requestorName + "\n" + "联系方式：" + requestorPhone);
			createFlowCaseCommand.setContent(task.getContent());
			createFlowCaseCommand.setCurrentOrganizationId(cmd.getOrganizationId());

			createFlowCaseCommand.setProjectId(task.getOwnerId());
			createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());
			if (StringUtils.isNotBlank(task.getBuildingName())) {
				Building building = buildingProvider.findBuildingByName(namespaceId, task.getOwnerId(),
						task.getBuildingName());
				if(building != null){
					ResourceCategoryAssignment resourceCategory = communityProvider.findResourceCategoryAssignment(building.getId(),
							EntityType.BUILDING.getCode(), namespaceId);
					if (null != resourceCategory) {
						createFlowCaseCommand.setProjectId(resourceCategory.getResourceCategryId());
						createFlowCaseCommand.setProjectType(EntityType.RESOURCE_CATEGORY.getCode());
					}
				}
			}

			FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);
			FlowNode flowNode = flowNodeProvider.getFlowNodeById(flowCase.getCurrentNodeId());

			String params = flowNode.getParams();

			if(StringUtils.isBlank(params)) {
				LOGGER.error("Invalid flowNode param.");
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
						"Invalid flowNode param.");
			}

			JSONObject paramJson = JSONObject.parseObject(params);
			String nodeType = paramJson.getString("nodeType");

			task.setStatus(pmTaskCommonService.convertFlowStatus(nodeType));
			task.setFlowCaseId(flowCase.getId());
			pmTaskProvider.updateTask(task);
			return task;
		});

		pmTaskSearch.feedDoc(task1);

		return ConvertHelper.convert(task1, PmTaskDTO.class);
	}

	@Override
	public void cancelTask(CancelTaskCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		//TODO:为科兴与一碑对接
		if(namespaceId == 999983) {

			PmTask task = pmTaskProvider.findTaskById(cmd.getId());

			if(StringUtils.isNotBlank(task.getStringTag1())) {

				PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);

				handler.cancelTask(cmd);
			}
		}

	}

	@Override
	public void evaluateTask(EvaluateTaskCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		//TODO:为科兴与一碑对接
		if(namespaceId == 999983) {

			PmTask task = pmTaskProvider.findTaskById(cmd.getId());

			if(StringUtils.isNotBlank(task.getStringTag1())) {

				PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);

				handler.evaluateTask(cmd);
			}
		}
	}

	@Override
	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		//TODO:为科兴与一碑对接
		if(namespaceId == 999983) {

			PmTask task = pmTaskProvider.findTaskById(cmd.getId());

			if(task != null && StringUtils.isNotBlank(task.getStringTag1())) {

				PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);

				return handler.getTaskDetail(cmd);
			}
		}

		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);
		return handler.getTaskDetail(cmd);
	}

	@Override
	public ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd) {
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);

		return handler.listTaskCategories(cmd);
	}

	@Override
	public List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd) {
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);

		return handler.listAllTaskCategories(cmd);
	}

	@Override
	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);

		return handler.searchTasks(cmd);
	}

	@Override
	public ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd) {
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);

		return handler.listUserTasks(cmd);
	}

	@Override
	public void updateTaskByOrg(UpdateTaskCommand cmd) {
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);
		handler.updateTaskByOrg(cmd);

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

		List<FlowButton> buttons = flowButtonProvider.findFlowButtonsByUserType(flowCase.getCurrentNodeId(),
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
