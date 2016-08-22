package com.everhomes.pmtask;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.CancelTaskCommand;
import com.everhomes.rest.pmtask.CloseTaskCommand;
import com.everhomes.rest.pmtask.GetTaskLogCommand;
import com.everhomes.rest.pmtask.PmTaskAttachmentDTO;
import com.everhomes.rest.pmtask.PmTaskAttachmentType;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetStatisticsCommand;
import com.everhomes.rest.pmtask.GetStatisticsResponse;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListUserTasksCommand;
import com.everhomes.rest.pmtask.ListUserTasksResponse;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.pmtask.PmTaskLogDTO;
import com.everhomes.rest.pmtask.PmTaskNotificationTemplateCode;
import com.everhomes.rest.pmtask.PmTaskStatus;
import com.everhomes.rest.pmtask.PmTaskTargetType;
import com.everhomes.rest.pmtask.SearchTaskStatisticsCommand;
import com.everhomes.rest.pmtask.SearchTaskStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.pmtask.CompleteTaskCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class PmTaskServiceImpl implements PmTaskService {
	
	public static final String CATEGORY_SEPARATOR = "/";
	public static final Long CATEGORY_FIRST_ANCESTOR = 5L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmTaskServiceImpl.class);
	@Autowired
	private PmTaskProvider pmTaskProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private CategoryProvider categoryProvider;
	@Autowired
    private ContentServerService contentServerService;
	@Autowired
	private UserProvider userProvider;
//	@Autowired
//	private LocaleStringService localeStringService;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
	private PmTaskSearch pmTaskSearch;
	
	@Override
	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		SearchTasksResponse response = new SearchTasksResponse();
		List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getCategoryId(), 
				cmd.getStartDate(), cmd.getEndDate(), cmd.getPageAnchor(), pageSize);
		
		response.setRequests(list);
		return response;
	}

	@Override
	public ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		ListUserTasksResponse response = new ListUserTasksResponse();
		List<PmTask> list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), UserContext.current().getUser().getId(), cmd.getStatus()
				, cmd.getPageAnchor(), cmd.getPageSize());
		if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> {
    			PmTaskDTO dto = ConvertHelper.convert(r, PmTaskDTO.class);
    			User user = userProvider.findUserById(r.getCreatorUid());
    			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
    			dto.setNickName(user.getNickName());
    			dto.setMobile(userIdentifier.getIdentifierToken());
    			
    			return dto;
    		}).collect(Collectors.toList()));
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
		
		return response;
	}

	@Override
	public void evaluateTask(EvaluateTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		if(null == cmd.getStar()){
			LOGGER.error("Star cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Star cannot be null.");
		}
		PmTask task = checkPmTask(cmd.getId());
		if(!task.getStatus().equals(PmTaskStatus.PROCESSED.getCode())){
			LOGGER.error("Task have not been completed, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Task have not been completed.");
		}
		task.setStar(cmd.getStar());
		pmTaskProvider.updateTask(task);

	}

	private void setTaskStatus(String ownerType, Long ownerId, Long id, String content, List<String> attachments, Byte status) {
		checkOwnerIdAndOwnerType(ownerType, ownerId);
		checkId(id);
		PmTask task = checkPmTask(id);
		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		task.setStatus(status);
		if(status.equals(PmTaskStatus.PROCESSED.getCode()))
			task.setProcessedTime(now);
		if(status.equals(PmTaskStatus.OTHER.getCode()))
			task.setClosedTime(now);
		pmTaskProvider.updateTask(task);
		
		PmTaskLog pmTaskLog = new PmTaskLog();
		pmTaskLog.setNamespaceId(task.getNamespaceId());
		pmTaskLog.setOperatorTime(now);
		pmTaskLog.setOperatorUid(user.getId());
		pmTaskLog.setContent(content);
		pmTaskLog.setOwnerId(task.getOwnerId());
		pmTaskLog.setOwnerType(task.getOwnerType());
		pmTaskLog.setStatus(task.getStatus());
		pmTaskLog.setTaskId(task.getId());
		pmTaskProvider.createTaskLog(pmTaskLog);
		
		addAttachments(attachments, user.getId(), pmTaskLog.getId());
	}

	@Override
	public void completeTask(CompleteTaskCommand cmd) {
		setTaskStatus(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId(), cmd.getContent(), 
				cmd.getAttachments(), PmTaskStatus.PROCESSED.getCode());
		
	}

	@Override
	public void closeTask(CloseTaskCommand cmd) {
		setTaskStatus(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId(), cmd.getContent(), 
				null, PmTaskStatus.OTHER.getCode());
		
	}
	
	@Override
	public void cancelTask(CancelTaskCommand cmd) {
		
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		PmTask task = checkPmTask(cmd.getId());
		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		task.setStatus(PmTaskStatus.INACTIVE.getCode());
		task.setDeleteUid(user.getId());
		task.setDeleteTime(now);
		pmTaskProvider.updateTask(task);
		
	}
	
	@Override
	public void assignTask(AssignTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		if(null == cmd.getTargetId()){
			LOGGER.error("TargetId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"TargetId cannot be null.");
		}
		PmTask task = checkPmTask(cmd.getId());
		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(!task.getStatus().equals(PmTaskStatus.PROCESSING.getCode())){
			task.setStatus(PmTaskStatus.PROCESSING.getCode());
			task.setProcessingTime(now);
			pmTaskProvider.updateTask(task);
		}
		
		PmTaskLog pmTaskLog = new PmTaskLog();
		pmTaskLog.setNamespaceId(task.getNamespaceId());
		pmTaskLog.setOperatorTime(now);
		pmTaskLog.setOperatorUid(user.getId());
		pmTaskLog.setOwnerId(task.getOwnerId());
		pmTaskLog.setOwnerType(task.getOwnerType());
		pmTaskLog.setStatus(task.getStatus());
		pmTaskLog.setTargetId(cmd.getTargetId());
		pmTaskLog.setTargetType(PmTaskTargetType.USER.getCode());
		pmTaskLog.setTaskId(task.getId());
		pmTaskProvider.createTaskLog(pmTaskLog);
	}

	@Override
	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		PmTask task = checkPmTask(cmd.getId());
		PmTaskDTO dto  = ConvertHelper.convert(task, PmTaskDTO.class);
		//查询服务类型
		Category category = checkCategory(task.getCategoryId());
		Category parentCategory = checkCategory(category.getParentId());
		dto.setCategoryName(category.getName());
		dto.setParentCategoryId(parentCategory.getId());
		dto.setParentCategoryName(parentCategory.getName());
		//查询图片
		List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(task.getId());
		List<PmTaskAttachmentDTO> attachmentDtos =  attachments.stream().map(r -> {
			PmTaskAttachmentDTO attachmentDto = ConvertHelper.convert(r, PmTaskAttachmentDTO.class);
			String contentUrl = getResourceUrlByUir(r.getContentUri(), 
	                EntityType.USER.getCode(), r.getCreatorUid());
			attachmentDto.setContentUrl(contentUrl);
			return attachmentDto;
		}).collect(Collectors.toList());
		dto.setAttachments(attachmentDtos);
		//查询task log
		List<PmTaskLogDTO> taskLogDtos = listPmTaskLogs(task.getId());
		dto.setTaskLogs(taskLogDtos);
		
		User user = userProvider.findUserById(task.getCreatorUid());
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		dto.setNickName(user.getNickName());
		dto.setMobile(userIdentifier.getIdentifierToken());
		
		return dto;
	}

	private List<PmTaskLogDTO> listPmTaskLogs(Long taskId) {
		List<PmTaskLog> taskLogs = pmTaskProvider.listPmTaskLogs(taskId);
		List<PmTaskLogDTO> taskLogDtos = taskLogs.stream().map(r -> {
			
			PmTaskLogDTO pmTaskLogDTO = ConvertHelper.convert(r, PmTaskLogDTO.class);
			
			User user = userProvider.findUserById(r.getOperatorUid());
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
			
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("operatorName", user.getNickName());
		    map.put("operatorPhone", userIdentifier.getIdentifierToken());
		    String scope = PmTaskNotificationTemplateCode.SCOPE;
		    String locale = PmTaskNotificationTemplateCode.LOCALE;
		    
			if(r.getStatus().equals(PmTaskStatus.UNPROCESSED.getCode())){
				
				int code = PmTaskNotificationTemplateCode.UNPROCESS_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
				
			}else if(r.getStatus().equals(PmTaskStatus.PROCESSING.getCode())){
				
				User target = userProvider.findUserById(r.getTargetId());
				UserIdentifier targetIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(target.getId(), IdentifierType.MOBILE.getCode());
				map.put("targetName", target.getNickName());
			    map.put("targetPhone", targetIdentifier.getIdentifierToken());
			    
			    int code = PmTaskNotificationTemplateCode.PROCESSING_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
				
			}else if(r.getStatus().equals(PmTaskStatus.PROCESSED.getCode())){
				
				int code = PmTaskNotificationTemplateCode.PROCESSED_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
				
				List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(r.getId());
				List<PmTaskAttachmentDTO> attachmentDtos =  attachments.stream().map(r2 -> {
					PmTaskAttachmentDTO dto = ConvertHelper.convert(r2, PmTaskAttachmentDTO.class);
					String contentUrl = getResourceUrlByUir(r2.getContentUri(), 
			                EntityType.USER.getCode(), r2.getCreatorUid());
					dto.setContentUrl(contentUrl);
					return dto;
				}).collect(Collectors.toList());
				pmTaskLogDTO.setAttachments(attachmentDtos);
				
			}else{
				
				int code = PmTaskNotificationTemplateCode.CLOSED_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
			}
			
			return pmTaskLogDTO;
		}).collect(Collectors.toList());
		return taskLogDtos;
	}
	
	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd) {
		String ownerType = cmd.getOwnerType();
		Long ownerId = cmd.getOwnerId();
		Long categoryId = cmd.getCategoryId();
		String content = cmd.getContent();
		checkCreateTaskParam(ownerType, ownerId, categoryId, content);
		
		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		PmTask task = new PmTask();
		task.setAddress(cmd.getAddress());
		task.setCategoryId(categoryId);
		task.setContent(content);
		task.setCreateTime(now);
		task.setCreatorUid(user.getId());
		task.setNamespaceId(user.getNamespaceId());
		task.setOwnerId(ownerId);
		task.setOwnerType(ownerType);
		task.setStatus(PmTaskStatus.UNPROCESSED.getCode());
		task.setUnprocessedTime(now);
		pmTaskProvider.createTask(task);
		addAttachments(cmd.getAttachments(), user.getId(), task.getId());
		
		PmTaskLog pmTaskLog = new PmTaskLog();
		pmTaskLog.setNamespaceId(task.getNamespaceId());
		pmTaskLog.setOperatorTime(now);
		pmTaskLog.setOperatorUid(user.getId());
		pmTaskLog.setOwnerId(task.getOwnerId());
		pmTaskLog.setOwnerType(task.getOwnerType());
		pmTaskLog.setStatus(task.getStatus());
		pmTaskLog.setTaskId(task.getId());
		pmTaskProvider.createTaskLog(pmTaskLog);
		
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		task.setNickName(user.getNickName());
		task.setMobile(userIdentifier.getIdentifierToken());
		pmTaskSearch.feedDoc(task);
		
		return ConvertHelper.convert(task, PmTaskDTO.class);
	}
	
	private void addAttachments(List<String> list, Long userId, Long ownerId){
		if(!CollectionUtils.isEmpty(list)){
			for(String uri: list){
				if(StringUtils.isNotBlank(uri)){
					PmTaskAttachment attachment = new PmTaskAttachment();
					attachment.setContentType(PmTaskAttachmentType.IMAGE.getCode());
					attachment.setContentUri(uri);
					attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
					attachment.setCreatorUid(userId);
					attachment.setOwnerId(ownerId);
					pmTaskProvider.createTaskAttachment(attachment);
				}
			}
		}
	}
	
	@Override
	public void deleteTaskCategory(DeleteTaskCategoryCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		Long id = cmd.getId();
		checkNamespaceId(namespaceId);
		checkId(id);
		
		Category category = categoryProvider.findCategoryById(id);
		if(category == null) {
			LOGGER.error("PmTask category not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_NULL,
					"PmTask category not found");
		}
		if(!category.getNamespaceId().equals(namespaceId)){
			LOGGER.error("NamespaceId is not correctly, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
					"NamespaceId is not correctly");
		}
		category.setStatus(CategoryAdminStatus.INACTIVE.getCode());
		categoryProvider.updateCategory(category);
	}

	@Override
	public CategoryDTO createTaskCategory(CreateTaskCategoryCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		//防止插入重复
		Long parentId = cmd.getParentId();
		String path = "";
		Category category = null;
		if(null == parentId){
			path = cmd.getName();
			parentId = CATEGORY_FIRST_ANCESTOR;
		}else{
			category = categoryProvider.findCategoryById(parentId);
			if(category == null) {
				LOGGER.error("PmTask parent category not found, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_NULL,
						"PmTask parent category not found");
			}
			path = category.getName() + CATEGORY_SEPARATOR + cmd.getName();
		}
		category = categoryProvider.findCategoryByPath(namespaceId, path);
		if(category != null) {
			LOGGER.error("PmTask category have been in existing");
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_EXIST,
					"PmTask category have been in existing");
		}
		
		category = new Category();
		category.setCreateTime(new Timestamp(System.currentTimeMillis()));
		category.setDefaultOrder(0);
		category.setName(cmd.getName());
		category.setNamespaceId(namespaceId);
		category.setPath(path);
		category.setParentId(parentId);
		category.setStatus(CategoryAdminStatus.ACTIVE.getCode());
		categoryProvider.createCategory(category);
		
		return ConvertHelper.convert(category, CategoryDTO.class);
	}

	@Override
	public ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd) {
		
		checkNamespaceId(cmd.getNamespaceId());
		//Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Integer pageSize = cmd.getPageSize();
		Long parentId = cmd.getParentId();
		if(null == parentId){
			parentId = CATEGORY_FIRST_ANCESTOR;
		}
		ListTaskCategoriesResponse response = new ListTaskCategoriesResponse();
		
		List<Category> list = categoryProvider.listTaskCategories(cmd.getNamespaceId(), parentId, cmd.getKeyword(),
				cmd.getPageAnchor(), cmd.getPageSize());
		
		if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, CategoryDTO.class))
    				.collect(Collectors.toList()));
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
		
		return response;
	}

	@Override
	public void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearchTaskStatisticsResponse searchTaskStatistics(SearchTaskStatisticsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PmTaskLogDTO getTaskLog(GetTaskLogCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		
		PmTaskLog pmTaskLog = checkPmTaskLog(cmd.getId());
		PmTaskLogDTO pmTaskLogDTO = ConvertHelper.convert(pmTaskLog, PmTaskLogDTO.class);
		
		List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(pmTaskLog.getId());
		List<PmTaskAttachmentDTO> attachmentDtos =  attachments.stream().map(r -> {
			PmTaskAttachmentDTO dto = ConvertHelper.convert(r, PmTaskAttachmentDTO.class);
			String contentUrl = getResourceUrlByUir(r.getContentUri(), 
	                EntityType.USER.getCode(), r.getCreatorUid());
			dto.setContentUrl(contentUrl);
			return dto;
		}).collect(Collectors.toList());
		pmTaskLogDTO.setAttachments(attachmentDtos);
		
		return pmTaskLogDTO;
	}

	private void checkNamespaceId(Integer namsepaceId){
		if(namsepaceId == null) {
        	LOGGER.error("NamespaceId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"NamespaceId cannot be null.");
        }
	}
	
	private void checkId(Long id){
		if(null == id) {
        	LOGGER.error("Id cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Id cannot be null.");
        }
	}
	
	private void checkCreateTaskParam(String ownerType, Long ownerId, Long categoryId, String content){
		checkOwnerIdAndOwnerType(ownerType, ownerId);
    	if(categoryId == null ) {
        	LOGGER.error("CategoryId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"CategoryId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(content)) {
        	LOGGER.error("Content cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Content cannot be null.");
        }
    	checkCategory(categoryId);
    }
	
	private Category checkCategory(Long id){
		Category category = categoryProvider.findCategoryById(id);
		if(null == category) {
        	LOGGER.error("Category not found, id={}", id);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Category not found.");
        }
		return category;
	}
	
	private void checkOwnerIdAndOwnerType(String ownerType, Long ownerId){
		if(null == ownerId) {
        	LOGGER.error("OwnerId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("OwnerType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerType cannot be null.");
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
	
	private PmTaskLog checkPmTaskLog(Long id){
		PmTaskLog pmTaskLog = pmTaskProvider.findTaskLogById(id);
		if(null == pmTaskLog) {
        	LOGGER.error("PmTaskLog not found, id={}", id);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"PmTaskLog not found.");
        }
		return pmTaskLog;
	}
	
	private String getResourceUrlByUir(String uri, String ownerType, Long ownerId) {
        String url = null;
        if(uri != null && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, uri=, ownerType=, ownerId=", uri, ownerType, ownerId, e);
            }
        }
        
        return url;
    }


}
