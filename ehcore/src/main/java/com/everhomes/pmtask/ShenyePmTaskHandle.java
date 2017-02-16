package com.everhomes.pmtask;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.pmtask.CancelTaskCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListAllTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.ListUserTasksCommand;
import com.everhomes.rest.pmtask.ListUserTasksResponse;
import com.everhomes.rest.pmtask.PmTaskAttachmentDTO;
import com.everhomes.rest.pmtask.PmTaskAttachmentType;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.PmTaskLogDTO;
import com.everhomes.rest.pmtask.PmTaskNotificationTemplateCode;
import com.everhomes.rest.pmtask.PmTaskProcessStatus;
import com.everhomes.rest.pmtask.PmTaskStatus;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE)
class ShenyePmTaskHandle implements PmTaskHandle {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShenyePmTaskHandle.class);
	@Autowired
	private CategoryProvider categoryProvider;
	@Autowired
    private DbProvider dbProvider;
	@Autowired
	private PmTaskProvider pmTaskProvider;
	@Autowired
	private PmTaskSearch pmTaskSearch;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private PmTaskCommonServiceImpl pmTaskCommonService;
	
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
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Category not found.");
        }
		return category;
	}
	
	private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId){
		if(null == ownerId) {
        	LOGGER.error("Invalid ownerId parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid ownerId parameter.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("Invalid ownerType parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid ownerType parameter.");
        }
	}

	@Override
	public void evaluateTask(EvaluateTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
//		if(null == cmd.getStar()){
//			cmd.setStar((byte)0);
//		}
//		if(null == cmd.getOperatorStar()){
//			cmd.setOperatorStar((byte)0);
//		}
		PmTask task = checkPmTask(cmd.getId());
		if(!task.getStatus().equals(PmTaskStatus.PROCESSED.getCode())){
			LOGGER.error("Task have not been completed, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Task have not been completed.");
		}
		task.setOperatorStar(cmd.getOperatorStar());
		task.setStar(cmd.getStar());
		pmTaskProvider.updateTask(task);

	}
	
	@Override
	public void cancelTask(CancelTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());

		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());

		dbProvider.execute((TransactionStatus transactionStatus) -> {

			PmTask task = checkPmTask(cmd.getId());
			if(!task.getStatus().equals(PmTaskStatus.UNPROCESSED.getCode())){
				LOGGER.error("Task cannot be canceled. cmd={}", cmd);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"Task cannot be canceled.");
			}
			task.setStatus(PmTaskStatus.INACTIVE.getCode());
			task.setDeleteUid(user.getId());
			task.setDeleteTime(now);
			pmTaskProvider.updateTask(task);
			
			//elasticsearch更新
			pmTaskSearch.deleteById(task.getId());

			return null;
		});
		
	}
	
	@Override
	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
		
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		PmTask task = checkPmTask(cmd.getId());
		
		PmTaskDTO dto  = ConvertHelper.convert(task, PmTaskDTO.class);
		
		//查询服务类型
		Category category = categoryProvider.findCategoryById(task.getCategoryId());
		Category taskCategory = checkCategory(task.getTaskCategoryId());
		if(null != category)
			dto.setCategoryName(category.getName());
    	dto.setTaskCategoryName(taskCategory.getName());
		
		//查询图片
		List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(task.getId(), PmTaskAttachmentType.TASK.getCode());
		List<PmTaskAttachmentDTO> attachmentDtos = pmTaskCommonService.convertAttachmentDTO(attachments);
		dto.setAttachments(attachmentDtos);
		//查询task log
		List<PmTaskLogDTO> taskLogDtos = listPmTaskLogs(dto);
		dto.setTaskLogs(taskLogDtos);
		
		return dto;
	}
	
	private List<PmTaskLogDTO> listPmTaskLogs(PmTaskDTO task) {
		
		List<PmTaskLog> taskLogs = pmTaskProvider.listPmTaskLogs(task.getId(), null);
		return taskLogs.stream().map(r -> {
			
			PmTaskLogDTO pmTaskLogDTO = ConvertHelper.convert(r, PmTaskLogDTO.class);
			
			Map<String, Object> map = new HashMap<>();
			
		    String scope = PmTaskNotificationTemplateCode.SCOPE;
		    String locale = PmTaskNotificationTemplateCode.LOCALE;
		    
			if(r.getStatus().equals(PmTaskStatus.UNPROCESSED.getCode())){
			    
				if(null == task.getOrganizationId() || task.getOrganizationId() == 0){
					setParam(map, task.getCreatorUid(), pmTaskLogDTO);
				}else{
					map.put("operatorName", task.getRequestorName());
				    map.put("operatorPhone", task.getRequestorPhone());
				}
				
				int code = PmTaskNotificationTemplateCode.UNPROCESS_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
				
			}else if(r.getStatus().equals(PmTaskStatus.PROCESSING.getCode())){
				setParam(map, r.getOperatorUid(), pmTaskLogDTO);
				User target = userProvider.findUserById(r.getTargetId());
				UserIdentifier targetIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(target.getId(), IdentifierType.MOBILE.getCode());
				map.put("targetName", target.getNickName());
			    map.put("targetPhone", targetIdentifier.getIdentifierToken());
			    
			    int code = PmTaskNotificationTemplateCode.PROCESSING_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
				
			}else if(r.getStatus().equals(PmTaskStatus.PROCESSED.getCode())){
				setParam(map, r.getOperatorUid(), pmTaskLogDTO);
				int code = PmTaskNotificationTemplateCode.PROCESSED_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
				List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(r.getId(), PmTaskAttachmentType.TASKLOG.getCode());
				List<PmTaskAttachmentDTO> attachmentDtos = pmTaskCommonService.convertAttachmentDTO(attachments);
				pmTaskLogDTO.setAttachments(attachmentDtos);
				
			}else if(r.getStatus().equals(PmTaskStatus.CLOSED.getCode())){
				setParam(map, r.getOperatorUid(), pmTaskLogDTO);
				int code = PmTaskNotificationTemplateCode.CLOSED_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
			}else {
				setParam(map, r.getOperatorUid(), pmTaskLogDTO);
				int code = PmTaskNotificationTemplateCode.REVISITED_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
			}
			
			return pmTaskLogDTO;
		}).collect(Collectors.toList());
	}

	private void setParam(Map<String, Object> map, Long userId, PmTaskLogDTO dto) {
		User user = userProvider.findUserById(userId);
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		map.put("operatorName", user.getNickName());
		map.put("operatorPhone", userIdentifier.getIdentifierToken());
		dto.setOperatorName(user.getNickName());
		dto.setOperatorPhone(userIdentifier.getIdentifierToken());
	}

	private void checkId(Long id){
		if(null == id) {
        	LOGGER.error("Invalid id parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid id parameter.");
        }
	}
	
	private PmTask checkPmTask(Long id){
		PmTask pmTask = pmTaskProvider.findTaskById(id);
		if(null == pmTask) {
        	LOGGER.error("PmTask not found, id={}", id);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"PmTask not found.");
        }
		return pmTask;
	}
	
	@Override
	public ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		//Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Integer pageSize = cmd.getPageSize();
		Long parentId = cmd.getParentId();
		if(null == parentId){
			Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
			Category ancestor = categoryProvider.findCategoryById(defaultId);
			parentId = ancestor.getId();
		}
		ListTaskCategoriesResponse response = new ListTaskCategoriesResponse();
		
		List<Category> list;
		if(null != cmd.getTaskCategoryId() && cmd.getTaskCategoryId() != 0L && (null == cmd.getParentId() || cmd.getParentId() == 0L)) {
			Category category = categoryProvider.findCategoryById(cmd.getTaskCategoryId());
			list = new ArrayList<>();
			list.add(category);
		}else{
			list = categoryProvider.listTaskCategories(namespaceId, parentId, cmd.getKeyword(),
					cmd.getPageAnchor(), cmd.getPageSize());
		}
				
		int size = list.size();
		if(size > 0){
    		response.setRequests(list.stream().map(r -> {
    			CategoryDTO dto = ConvertHelper.convert(r, CategoryDTO.class);
    			List<Category> tempList = categoryProvider.listTaskCategories(namespaceId, null, r.getPath(),
    					null, null);
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
		Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);

		List<Category> categories = categoryProvider.listTaskCategories(namespaceId, null, null, null, null);
		
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
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid namespaceId parameter.");
        }
	}

	@Override
	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		SearchTasksResponse response = new SearchTasksResponse();
		List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getStatus(), cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(), 
				cmd.getTaskCategoryId(), cmd.getStartDate(), cmd.getEndDate(), cmd.getAddressId(), cmd.getBuildingName(), 
				cmd.getPageAnchor(), pageSize);
		int listSize = list.size();
		if (listSize > 0) {
    		response.setRequests(list.stream().map(t -> {
    			PmTask task = pmTaskProvider.findTaskById(t.getId());
    			PmTaskDTO dto = ConvertHelper.convert(t, PmTaskDTO.class);
    			
    			Category taskCategory = checkCategory(task.getTaskCategoryId());
    			dto.setTaskCategoryId(taskCategory.getId());
    			dto.setTaskCategoryName(taskCategory.getName());
    			
    			return dto;
    		}).collect(Collectors.toList()));
    		if(listSize != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(listSize-1).getCreateTime().getTime());
        	}
    	}
		
		return response;
	}
	
	@Override
	public ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		User current = UserContext.current().getUser();
		
		Byte status = cmd.getStatus();
		List<PmTask> list = new ArrayList<>();
		if (null != status && (status.equals(PmTaskProcessStatus.PROCESSED.getCode()) ||
				status.equals(PmTaskProcessStatus.UNPROCESSED.getCode()))) {
			
			checkOrganizationId(cmd.getOrganizationId());

	    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
	    	
	    	if(resolver.checkUserPrivilege(current.getId(), EntityType.COMMUNITY.getCode(), 
	    			cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.LISTALLTASK)
	    			){
	    		
	    		list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), status, null,
	    				cmd.getPageAnchor(), cmd.getPageSize());
			}else if(resolver.checkUserPrivilege(current.getId(), EntityType.COMMUNITY.getCode(), 
	    			cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.LISTUSERTASK)
	    			){
				
				if(status.equals(PmTaskProcessStatus.UNPROCESSED.getCode()))
				list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), PmTaskProcessStatus.USER_UNPROCESSED.getCode(),
						null, cmd.getPageAnchor(), cmd.getPageSize());
				else if(status.equals(PmTaskProcessStatus.PROCESSED.getCode()))
					list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), PmTaskProcessStatus.PROCESSED.getCode(),
							null, cmd.getPageAnchor(), cmd.getPageSize());
			}else{
				returnNoPrivileged(null, current.getId());
			}
	    	
		}else{
			list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), status, cmd.getTaskCategoryId(),
					cmd.getPageAnchor(), cmd.getPageSize());
		}
		
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
	
	private void checkOrganizationId(Long organizationId) {
		if(null == organizationId) {
        	LOGGER.error("Invalid organizationId parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid organizationId parameter.");
        }
	}
	
	private void returnNoPrivileged(List<Long> privileges, Long userId){
    	LOGGER.error("non-privileged, privileges={}, userId={}", privileges, userId);
		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
				"non-privileged.");
    }
}
