package com.everhomes.pmtask;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.category.Category;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.FLOW)
class FlowPmTaskHandle extends DefaultPmTaskHandle {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlowPmTaskHandle.class);

	@Autowired
	private FlowService flowService;
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
	private PortalService portalService;
	@Autowired
	private FlowEventLogProvider flowEventLogProvider;
	@Autowired
	private ServiceModuleAppService serviceModuleAppService;

	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd, Long requestorUid, String requestorName, String requestorPhone){

		PmTask task1 = dbProvider.execute((TransactionStatus status) -> {
			PmTask task = pmTaskCommonService.createTask(cmd, requestorUid, requestorName, requestorPhone);
			//新建flowcase
			Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
			Flow flow = null;

//            Long parentTaskId = pmTaskProvider.findCategoryById(cmd.getTaskCategoryId()).getParentId();
//            if (parentTaskId == PmTaskAppType.SUGGESTION_ID)
//                flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
//                        FlowModuleType.SUGGESTION_MODULE.getCode(), cmd.getOwnerId(), FlowOwnerType.PMTASK.getCode());
//            else
//                if (cmd.getTaskCategoryId()==PmTaskAppType.REPAIR_ID)
//                    flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
//                        FlowModuleType.NO_MODULE.getCode(), cmd.getOwnerId(), FlowOwnerType.PMTASK.getCode());
			flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
                        String.valueOf(task.getAppId()), cmd.getOwnerId(), FlowOwnerType.PMTASK.getCode());
			if(null == flow) {
				LOGGER.error("Enable pmtask flow not found, moduleId={}", FlowConstants.PM_TASK_MODULE);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
						"Enable pmtask flow not found.");
			}
			CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
			PmTaskCategory taskCategory = pmTaskProvider.findCategoryById(task.getCategoryId());

			ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(task.getAppId());

			if (app!=null)
				createFlowCaseCommand.setTitle(app.getName());
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
			task.setStatus(flowCase.getStatus());
			pmTaskProvider.updateTask(task);
			return task;
		});

		pmTaskSearch.feedDoc(task1);

		return ConvertHelper.convert(task1, PmTaskDTO.class);
	}

//	@Override
//	public void cancelTask(CancelTaskCommand cmd) {
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//
//		//TODO:为科兴与一碑对接
//		if(namespaceId == 999983) {
//
//			PmTask task = pmTaskProvider.findTaskById(cmd.getId());
//
//			if(StringUtils.isNotBlank(task.getStringTag1())) {
//
//				PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);
//
//				handler.cancelTask(cmd);
//			}
//		}
//
//	}

//	@Override
//	public void evaluateTask(EvaluateTaskCommand cmd) {
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//
//		//TODO:为科兴与一碑对接
//		if(namespaceId == 999983) {
//
//			PmTask task = pmTaskProvider.findTaskById(cmd.getId());
//
//			if(StringUtils.isNotBlank(task.getStringTag1())) {
//
//				PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);
//
//				handler.evaluateTask(cmd);
//			}
//		}
//	}

//	@Override
//	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
//
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//		//TODO:为科兴与一碑对接
//		if(namespaceId == 999983) {
//
//			PmTask task = pmTaskProvider.findTaskById(cmd.getId());
//
//			if(task != null && StringUtils.isNotBlank(task.getStringTag1())) {
//
//				PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI);
//
//				return handler.getTaskDetail(cmd);
//			}
//		}
//
////		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);
//		return super.getTaskDetail(cmd);
//	}

//	@Override
//	public ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd) {
//		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);
//
//		return handler.listTaskCategories(cmd);
//	}

//	@Override
//	public List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd) {
//		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);
//
//		return handler.listAllTaskCategories(cmd);
//	}

//	@Override
//	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
//		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);
//
//		return handler.searchTasks(cmd);
//	}

//	@Override
//	public ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd) {
//		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);
//
//		return handler.listUserTasks(cmd);
//	}

	@Override
	public void updateTaskByOrg(UpdateTaskCommand cmd) {
//		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE);
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
		fireButtonCommand.setStepCount(flowCase.getStepCount());
		flowService.fireButton(fireButtonCommand);
	}

}
