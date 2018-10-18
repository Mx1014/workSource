package com.everhomes.pmtask;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.docking.DockingMapping;
import com.everhomes.docking.DockingMappingProvider;
import com.everhomes.flow.FlowEvaluate;
import com.everhomes.flow.FlowEvaluateProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.pmtask.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract class DefaultPmTaskHandle implements PmTaskHandle {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPmTaskHandle.class);
	@Autowired
	CategoryProvider categoryProvider;
	@Autowired
	DbProvider dbProvider;
	@Autowired
	PmTaskProvider pmTaskProvider;
	@Autowired
	PmTaskSearch pmTaskSearch;
	@Autowired
    ConfigurationProvider configProvider;
	@Autowired
	PmTaskCommonServiceImpl pmTaskCommonService;
	@Autowired
	private FlowService flowService;
	@Autowired
	private ServiceModuleService serviceModuleService;
	@Autowired
	private FlowEvaluateProvider flowEvaluateProvider;
	@Autowired
	private DockingMappingProvider dockingMappingProvider;

	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd, Long requestorUid, String requestorName, String requestorPhone){

		PmTask task = dbProvider.execute((TransactionStatus status) ->
			pmTaskCommonService.createTask(cmd, requestorUid, requestorName, requestorPhone)
		);

		pmTaskSearch.feedDoc(task);
		
		return ConvertHelper.convert(task, PmTaskDTO.class);
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
	
	private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId){
		if(null == ownerId) {
        	LOGGER.error("Invalid ownerId parameter.");
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
    				"Invalid ownerId parameter.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("Invalid ownerType parameter.");
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
    				"Invalid ownerType parameter.");
        }
	}

//	@Override
//	public void evaluateTask(EvaluateTaskCommand cmd) {
//		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
//		pmTaskCommonService.checkId(cmd.getId());
////		if(null == cmd.getStar()){
////			cmd.setStar((byte)0);
////		}
////		if(null == cmd.getOperatorStar()){
////			cmd.setOperatorStar((byte)0);
////		}
//		PmTask task = pmTaskCommonService.checkPmTask(cmd.getId());
//		if(!task.getStatus().equals(PmTaskStatus.PROCESSED.getCode())){
//			LOGGER.error("Task have not been completed, cmd={}", cmd);
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//    				"Task have not been completed.");
//		}
//		task.setOperatorStar(cmd.getOperatorStar());
//		task.setStar(cmd.getStar());
//		pmTaskProvider.updateTask(task);
//
//	}
	
//	@Override
//	public void cancelTask(CancelTaskCommand cmd) {
//		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
//		pmTaskCommonService.checkId(cmd.getId());
//
//		User user = UserContext.current().getUser();
//		Timestamp now = new Timestamp(System.currentTimeMillis());
//
//		dbProvider.execute((TransactionStatus transactionStatus) -> {
//
//			PmTask task = pmTaskCommonService.checkPmTask(cmd.getId());
//			if(!task.getStatus().equals(PmTaskStatus.UNPROCESSED.getCode())){
//				LOGGER.error("Task cannot be canceled. cmd={}", cmd);
//				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//						"Task cannot be canceled.");
//			}
//			task.setStatus(PmTaskStatus.INACTIVE.getCode());
//			task.setDeleteUid(user.getId());
//			task.setDeleteTime(now);
//			pmTaskProvider.updateTask(task);
//
//			//elasticsearch更新
//			pmTaskSearch.deleteById(task.getId());
//
//			return null;
//		});
//
//	}
	
//	@Override
//	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
//
//		return pmTaskCommonService.getTaskDetail(cmd, true);
//	}

	@Override
	public void updateTaskByOrg(UpdateTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		if(null == cmd.getTaskId()) {
			LOGGER.error("Invalid taskId parameter, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
					"Invalid taskId parameter.");
		}
		PmTask task = pmTaskProvider.findTaskById(cmd.getTaskId());
		if(null == task) {
			LOGGER.error("PmTask not found.");
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
					"PmTask not found.");
		}
		if(null != cmd.getCategoryId()) {
			checkCategory(cmd.getCategoryId());
		}
		task.setCategoryId(cmd.getCategoryId());
		task.setPriority(cmd.getPriority());
		if(null != cmd.getReserveTime()) {
			task.setReserveTime(new Timestamp(cmd.getReserveTime()));
		}else {
			task.setReserveTime(null);
		}
		task.setSourceType(cmd.getSourceType());
		pmTaskProvider.updateTask(task);
	}

	@Override
	public ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		//Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Integer pageSize = cmd.getPageSize();
		Long parentId = cmd.getParentId();

		ListTaskCategoriesResponse response = new ListTaskCategoriesResponse();

		//多入口时，app始终会传taskCategoryId，根据时候传parentId来查询服务类型
		if(null != cmd.getTaskCategoryId() && cmd.getTaskCategoryId() != 0L && cmd.getParentId() == null) {
			//app服务广场 物业报修配固定id  PmTaskAppType.REPAIR_ID
			//投诉建议 配固定id PmTaskAppType.SUGGESTION_ID, 如果配置错误，则需要修改
			parentId = cmd.getTaskCategoryId();
//			if (PmTaskAppType.REPAIR_ID == cmd.getTaskCategoryId()) {
//
//			}else if (PmTaskAppType.REPAIR_ID == cmd.getTaskCategoryId())

//			Category category = categoryProvider.findCategoryById(cmd.getTaskCategoryId());
//			list = new ArrayList<>();
//			list.add(category);
		}else{
//			if(null == parentId){
//				Category ancestor = categoryProvider.findCategoryById(defaultId);
//				parentId = ancestor.getId();
//			}else {
//				Category parent = categoryProvider.findCategoryById(parentId);
//				if (null == parent) {
//					LOGGER.error("Category not found, cmd={}", cmd);
//					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//							"Category not found.");
//				}
//				if (CategoryAdminStatus.INACTIVE.getCode() == parent.getStatus()) {
//					return response;
//				}
//			}
			Category parent = categoryProvider.findCategoryById(parentId);
			if (null == parent) {
				DockingMapping mapping = dockingMappingProvider.findDockingMappingById(parentId);
				if(null != mapping){
					return response;
				}
				LOGGER.error("Category not found, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_NOT_EXIST,
						"Category not found.");
			}
			if (CategoryAdminStatus.INACTIVE.getCode() == parent.getStatus()) {
				return response;
			}

		}
		List<Category> list = new ArrayList<>();
		List<Long> ownIds = this.getOwnerIds(cmd);
		if (ownIds.size() > 1){
			for (Long ownId : ownIds) {
				list.addAll(categoryProvider.listTaskCategories(namespaceId,cmd.getOwnerType(),ownId,
						parentId, cmd.getKeyword(), null, null));
			}
		} else {
			list.addAll(categoryProvider.listTaskCategories(namespaceId,cmd.getOwnerType(),cmd.getOwnerId(),
					parentId, cmd.getKeyword(), cmd.getPageAnchor(), cmd.getPageSize()));
		}

		int size = list.size();
		if(size > 0){
    		response.setRequests(list.stream().map(r -> {
    			CategoryDTO dto = ConvertHelper.convert(r, CategoryDTO.class);
    			List<Category> tempList = categoryProvider.listTaskCategories(namespaceId,r.getOwnerType(),r.getOwnerId(),
						null, r.getPath(), null, null);
    			getChildCategories(tempList.stream().map(k -> ConvertHelper.convert(k, CategoryDTO.class))
    					.collect(Collectors.toList()), dto);
    			
    			dto.setIsSupportDelete((byte)1);
    			return dto;
    		}).collect(Collectors.toList()));
    		if(pageSize != null && size != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(size-1).getId());
        	}
    	}
		
		return response;
	}
	
	@Override
	public List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		Long defaultId = PmTaskAppType.REPAIR_ID;

		List<Category> categories = categoryProvider.listTaskCategories(namespaceId, cmd.getOwnerType(),cmd.getOwnerId(),
				null, null, null, null);
		
		List<CategoryDTO> dtos = categories.stream().map(r -> {
			CategoryDTO dto = ConvertHelper.convert(r, CategoryDTO.class);
			dto.setIsSupportDelete((byte)1);
			return dto;
		}).collect(Collectors.toList());
		List<CategoryDTO> result = new ArrayList<>();
		dtos.forEach(c -> {
			if(defaultId.equals(c.getParentId())) {
				result.add(getChildCategories(dtos, c));
			}
		});

		return result;
	}
	
	private CategoryDTO getChildCategories(List<CategoryDTO> categories, CategoryDTO dto){
		
		List<CategoryDTO> children = new ArrayList<>();

		categories.forEach(c -> {
			if(dto.getId().equals(c.getParentId())){
				children.add(getChildCategories(categories, c));
			}
		});

		dto.setChildrens(children);
		return dto;
	}
	
	private void checkNamespaceId(Integer namespaceId){
		if(namespaceId == null) {
        	LOGGER.error("Invalid namespaceId parameter.");
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
    				"Invalid namespaceId parameter.");
        }
	}

	@Override
	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		SearchTasksResponse response = new SearchTasksResponse();
		List<PmTaskDTO> dtos = new ArrayList<>();
//		V3.5修改兼容查询域空间下全部项目
		List<PmTaskDTO> list = pmTaskSearch.searchAllDocsByType(cmd,pageSize + 1);
//		List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getStatus(), cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(),
//				cmd.getTaskCategoryId(), cmd.getStartDate(), cmd.getEndDate(), cmd.getAddressId(), cmd.getBuildingName(), cmd.getCreatorType(),
//				cmd.getPageAnchor(), pageSize+1);
		int listSize = list.size();
		if (listSize > 0) {
    		dtos = list.stream().map(t -> {
    			PmTask task = pmTaskProvider.findTaskById(t.getId());
    			PmTaskDTO dto = ConvertHelper.convert(t, PmTaskDTO.class);
    			if(task != null) {
					Category taskCategory = categoryProvider.findCategoryById(task.getTaskCategoryId());
//					Category taskCategory = checkCategory(task.getTaskCategoryId());
					if(taskCategory != null) {
						dto.setTaskCategoryId(taskCategory.getId());
						dto.setTaskCategoryName(taskCategory.getName());
					}
				}
				
				if (dto.getOrganizationUid()==0)
					dto.setOrganizationUid(null);

//				PmTaskOrder order = pmTaskProvider.findPmTaskOrderByTaskId(t.getId());
//				if(null != order){
//					dto.setAmount(order.getAmount());
//				}

    			return dto;
    		}).collect(Collectors.toList());
    		response.setRequests(dtos);
    		if(response.getRequests().size() > 0 && response.getRequests().size() > pageSize){
				response.setNextPageAnchor(list.get(listSize-1).getCreateTime().getTime());
				response.getRequests().remove(list.get(listSize-1));
        	}else{
				response.setNextPageAnchor(null);
        	}
    	}
		
		return response;
	}

	@Override
	public Object getThirdAddress(HttpServletRequest req) {
		return null;
	}

	@Override
	public Object createThirdTask(HttpServletRequest req) {
		return null;
	}

	@Override
	public Object listThirdTasks(HttpServletRequest req) {
		return null;
	}

	@Override
	public Object getThirdTaskDetail(HttpServletRequest req) {
		return null;
	}

	//	@Override
//	public ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd) {
//		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
//		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
//		User current = UserContext.current().getUser();
//
//		Byte status = cmd.getStatus();
//		List<PmTask> list = new ArrayList<>();
//		if (null != status && (status.equals(PmTaskProcessStatus.PROCESSED.getCode()) ||
//				status.equals(PmTaskProcessStatus.UNPROCESSED.getCode()))) {
//
//			checkOrganizationId(cmd.getOrganizationId());
//
//	    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//
//	    	if(resolver.checkUserPrivilege(current.getId(), EntityType.COMMUNITY.getCode(),
//	    			cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.LISTALLTASK)
//	    			){
//
//	    		list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), status, null,
//	    				cmd.getPageAnchor(), cmd.getPageSize());
//			}else if(resolver.checkUserPrivilege(current.getId(), EntityType.COMMUNITY.getCode(),
//	    			cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.LISTUSERTASK)
//	    			){
//
//				if(status.equals(PmTaskProcessStatus.UNPROCESSED.getCode()))
//				list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), PmTaskProcessStatus.USER_UNPROCESSED.getCode(),
//						null, cmd.getPageAnchor(), cmd.getPageSize());
//				else if(status.equals(PmTaskProcessStatus.PROCESSED.getCode()))
//					list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), PmTaskProcessStatus.PROCESSED.getCode(),
//							null, cmd.getPageAnchor(), cmd.getPageSize());
//			}else{
//				returnNoPrivileged(null, current.getId());
//			}
//
//		}else{
//			list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), status, cmd.getTaskCategoryId(),
//					cmd.getPageAnchor(), cmd.getPageSize());
//		}
//
//		ListUserTasksResponse response = new ListUserTasksResponse();
//		int size = list.size();
//		if(size > 0){
//    		response.setRequests(list.stream().map(r -> {
//    			PmTaskDTO dto = ConvertHelper.convert(r, PmTaskDTO.class);
//
//    			Category category = categoryProvider.findCategoryById(r.getCategoryId());
//    			Category taskCategory = checkCategory(r.getTaskCategoryId());
//    			if(null != category)
//    				dto.setCategoryName(category.getName());
//    	    	dto.setTaskCategoryName(taskCategory.getName());
//
//    			return dto;
//    		}).collect(Collectors.toList()));
//    		if(size != pageSize){
//        		response.setNextPageAnchor(null);
//        	}else{
//        		response.setNextPageAnchor(list.get(size-1).getCreateTime().getTime());
//        	}
//    	}
//
//		return response;
//	}
	
	private void checkOrganizationId(Long organizationId) {
		if(null == organizationId) {
        	LOGGER.error("Invalid organizationId parameter.");
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_INVALD_PARAMS,
    				"Invalid organizationId parameter.");
        }
	}
	
	private void returnNoPrivileged(List<Long> privileges, Long userId){
    	LOGGER.error("non-privileged, privileges={}, userId={}", privileges, userId);
		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
				"non-privileged.");
    }

	private List<Long> getOwnerIds(ListTaskCategoriesCommand cmd){
		List<Long> ownerIds = new ArrayList<>();
		if(null == cmd.getOwnerId() || -1L == cmd.getOwnerId()){
			ListUserRelatedProjectByModuleCommand cmd1 = new ListUserRelatedProjectByModuleCommand();
			cmd1.setModuleId(20100L);
//			cmd1.setAppId(cmd.getAppId());
//			cmd1.setOrganizationId(cmd.getCurrentPMId());
			if(null != cmd.getOrganizationId())
				cmd1.setOrganizationId(cmd.getOrganizationId());
			List<ProjectDTO> dtos = serviceModuleService.listUserRelatedProjectByModuleId(cmd1);
			ownerIds.addAll(dtos.stream().map(elem -> elem.getProjectId()).collect(Collectors.toList()));
		} else {
			ownerIds.add(cmd.getOwnerId());
		}
		return ownerIds;
	}
}
