package com.everhomes.pmtask;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import java.util.stream.Collectors;

import com.everhomes.rest.pmtask.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.SHEN_YE)
public class ShenyePmTaskHandle implements PmTaskHandle {
	
	public static final String CATEGORY_SEPARATOR = "/";

    SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	
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
	private SmsProvider smsProvider;
	@Autowired
	private MessagingService messagingService;
	@Autowired
    private ContentServerService contentServerService;
	@Autowired
	private AddressProvider addressProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private CommunityProvider communityProvider;
	
	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd, Long userId, String requestorName, String requestorPhone){

		if(null == cmd.getAddressType()){
			LOGGER.error("Invalid addressType parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid addressType parameter.");
		}

		String ownerType = cmd.getOwnerType();
		Long ownerId = cmd.getOwnerId();
		Long taskCategoryId = cmd.getTaskCategoryId();
		String content = cmd.getContent();
		checkCreateTaskParam(ownerType, ownerId, taskCategoryId, content);
		Category taskCategory = checkCategory(taskCategoryId);

		User user = UserContext.current().getUser();
		PmTask task = new PmTask();
		dbProvider.execute((TransactionStatus status) -> {
			Timestamp now = new Timestamp(System.currentTimeMillis());

			task.setAddressType(cmd.getAddressType());

			if(cmd.getAddressType().equals(PmTaskAddressType.ORGANIZATION.getCode())) {
				if(null == cmd.getAddressOrgId()){
					LOGGER.error("Invalid addressOrgId parameter.");
		    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
		    				"Invalid addressOrgId parameter.");
				}
				task.setAddressOrgId(cmd.getAddressOrgId());
				task.setAddressId(cmd.getAddressId());
//				List<OrganizationAddress> addresses = organizationProvider.findOrganizationAddressByOrganizationId(cmd.getAddressOrgId());
//				int size = addresses.size();
//				if(size != 0) {
//					task.setAddressId(addresses.get(0).getAddressId());
//				}
			}else {
				task.setAddressId(cmd.getAddressId());
			}
			task.setAddress(cmd.getAddress());
			task.setTaskCategoryId(taskCategoryId);
			task.setCategoryId(cmd.getCategoryId());
			task.setContent(content);
			task.setCreateTime(now);

			if(null != cmd.getOrganizationId()) {
				task.setOrganizationId(cmd.getOrganizationId());
				task.setRequestorName(requestorName);
				task.setRequestorPhone(requestorPhone);
			}
			task.setCreatorUid(user.getId());
			task.setNamespaceId(user.getNamespaceId());
			task.setOwnerId(ownerId);
			task.setOwnerType(ownerType);
			task.setStatus(PmTaskStatus.UNPROCESSED.getCode());
			task.setUnprocessedTime(now);

			if(null != cmd.getReserveTime())
				task.setReserveTime(new Timestamp(cmd.getReserveTime()));
			task.setPriority(cmd.getPriority());
			task.setSourceType(cmd.getSourceType()==null?PmTaskSourceType.APP.getCode():cmd.getSourceType());

			pmTaskProvider.createTask(task);
			//图片
			addAttachments(cmd.getAttachments(), userId, task.getId(), PmTaskAttachmentType.TASK.getCode());

			PmTaskLog pmTaskLog = new PmTaskLog();
			pmTaskLog.setNamespaceId(task.getNamespaceId());
			pmTaskLog.setOperatorTime(now);
			pmTaskLog.setOperatorUid(userId);
			pmTaskLog.setOwnerId(task.getOwnerId());
			pmTaskLog.setOwnerType(task.getOwnerType());
			pmTaskLog.setStatus(task.getStatus());
			pmTaskLog.setTaskId(task.getId());
			pmTaskProvider.createTaskLog(pmTaskLog);

	    	List<PmTaskTarget> targets = pmTaskProvider.listTaskTargets(cmd.getOwnerType(), cmd.getOwnerId(),
	    			PmTaskOperateType.EXECUTOR.getCode(), null, null);
	    	int size = targets.size();
	    	if(LOGGER.isDebugEnabled())
	    		LOGGER.debug("Create pmtask and send message, size={}, cmd={}", size, cmd);
	    	if(size > 0){
	    		sendMessage4CreateTask(targets, requestorName, requestorPhone, taskCategory.getName(), user);
	    	}
			return null;
		});

		pmTaskSearch.feedDoc(task);
		
		return ConvertHelper.convert(task, PmTaskDTO.class);
	}

	private void sendMessage4CreateTask(List<PmTaskTarget> targets, String requestorName, String requestorPhone,
			String taskCategoryName, User user) {
		List<String> phones = new ArrayList<String>();
    	
    	//消息推送
    	String scope = PmTaskNotificationTemplateCode.SCOPE;
	    String locale = PmTaskNotificationTemplateCode.LOCALE;
    	for(PmTaskTarget p: targets) {
        	UserIdentifier sender = userProvider.findClaimedIdentifierByOwnerAndType(p.getTargetId(), IdentifierType.MOBILE.getCode());
        	phones.add(sender.getIdentifierToken());
        	//消息推送
        	Map<String, Object> map = new HashMap<String, Object>();
    	    map.put("creatorName", requestorName);
    	    map.put("creatorPhone", requestorPhone);
    	    map.put("categoryName", taskCategoryName);
    		int code = PmTaskNotificationTemplateCode.CREATE_PM_TASK;
    		String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
    		sendMessageToUser(p.getTargetId(), text);
    	}
    	int num = phones.size();
    	if(num > 0) {
    		String[] s = new String[num];
        	phones.toArray(s);
    		List<Tuple<String, Object>> variables = smsProvider.toTupleList("operatorName", requestorName);
    		smsProvider.addToTupleList(variables, "operatorPhone", requestorPhone);
    		smsProvider.addToTupleList(variables, "categoryName", taskCategoryName);
    		smsProvider.sendSms(user.getNamespaceId(), s, SmsTemplateCode.SCOPE, 
    				SmsTemplateCode.PM_TASK_CREATOR_CODE, user.getLocale(), variables);
    	}
	}

	private void sendMessageToUser(Long userId, String content) {
//		User user = UserContext.current().getUser();
		MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}
	
	private void addAttachments(List<AttachmentDescriptor> list, Long userId, Long ownerId, String targetType){
		if(!CollectionUtils.isEmpty(list)){
			for(AttachmentDescriptor ad: list){
				if(null != ad){
					PmTaskAttachment attachment = new PmTaskAttachment();
					attachment.setContentType(ad.getContentType());
					attachment.setContentUri(ad.getContentUri());
					attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
					attachment.setCreatorUid(userId);
					attachment.setOwnerId(ownerId);
					attachment.setOwnerType(targetType);
					pmTaskProvider.createTaskAttachment(attachment);
				}
			}
		}
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
	
	private void checkCreateTaskParam(String ownerType, Long ownerId, Long taskCategoryId, String content){
		checkOwnerIdAndOwnerType(ownerType, ownerId);
    	if(null == taskCategoryId) {
        	LOGGER.error("Invalid taskCategoryId parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid taskCategoryId parameter.");
        }
    	
    	if(StringUtils.isBlank(content)) {
        	LOGGER.error("Invalid content parameter.");
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CONTENT_NULL,
    				"Invalid content parameter.");
        }
    	
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
		PmTask task = checkPmTask(cmd.getId());
		if(!task.getStatus().equals(PmTaskStatus.UNPROCESSED.getCode())){
			LOGGER.error("Task cannot be canceled. cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Task cannot be canceled.");
		}
		dbProvider.execute((TransactionStatus transactionStatus) -> {
			User user = UserContext.current().getUser();
			Timestamp now = new Timestamp(System.currentTimeMillis());
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
		
		setPmTaskDTOAddress(task, dto);
		if(null == task.getOrganizationId() || task.getOrganizationId() ==0 ){
			User user = userProvider.findUserById(task.getCreatorUid());
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
			dto.setRequestorName(user.getNickName());
			dto.setRequestorPhone(userIdentifier.getIdentifierToken());
		}
		
		//查询服务类型
		Category category = categoryProvider.findCategoryById(task.getCategoryId());
		Category taskCategory = checkCategory(task.getTaskCategoryId());
		if(null != category)
			dto.setCategoryName(category.getName());
    	dto.setTaskCategoryName(taskCategory.getName());
		
		//查询图片
		List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(task.getId(), PmTaskAttachmentType.TASK.getCode());
		List<PmTaskAttachmentDTO> attachmentDtos =  attachments.stream().map(r -> {
			PmTaskAttachmentDTO attachmentDto = ConvertHelper.convert(r, PmTaskAttachmentDTO.class);
			
			String contentUrl = getResourceUrlByUir(r.getContentUri(), 
	                EntityType.USER.getCode(), r.getCreatorUid());
			attachmentDto.setContentUrl(contentUrl);
			return attachmentDto;
		}).collect(Collectors.toList());
		dto.setAttachments(attachmentDtos);
		//查询task log
		List<PmTaskLogDTO> taskLogDtos = listPmTaskLogs(dto);
		dto.setTaskLogs(taskLogDtos);
		
		return dto;
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
	
	private void setPmTaskDTOAddress(PmTask task, PmTaskDTO dto) {
		if(task.getAddressType().equals(PmTaskAddressType.FAMILY.getCode())) {
			Address address = addressProvider.findAddressById(task.getAddressId());
			if(null != address) {
				Community community = communityProvider.findCommunityById(address.getCommunityId());
				dto.setAddress(address.getCityName() + address.getAreaName() + community.getName() + address.getAddress());
			}
		}else {
			Organization organization = organizationProvider.findOrganizationById(task.getAddressOrgId());
			Address address = addressProvider.findAddressById(task.getAddressId());
			
			String addr = "";
			if(null != organization)
				addr = organization.getName();
			if(null != address) 
				addr = addr + address.getAddress();
			
			dto.setAddress(addr);
		}
	}
	
	private List<PmTaskLogDTO> listPmTaskLogs(PmTaskDTO task) {
		
		List<PmTaskLog> taskLogs = pmTaskProvider.listPmTaskLogs(task.getId(), null);
		List<PmTaskLogDTO> taskLogDtos = taskLogs.stream().map(r -> {
			
			PmTaskLogDTO pmTaskLogDTO = ConvertHelper.convert(r, PmTaskLogDTO.class);
			
			Map<String, Object> map = new HashMap<String, Object>();
			
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
				List<PmTaskAttachmentDTO> attachmentDtos =  attachments.stream().map(r2 -> {
					PmTaskAttachmentDTO dto = ConvertHelper.convert(r2, PmTaskAttachmentDTO.class);
					String contentUrl = getResourceUrlByUir(r2.getContentUri(), 
			                EntityType.USER.getCode(), r2.getCreatorUid());
					dto.setContentUrl(contentUrl);
					return dto;
				}).collect(Collectors.toList());
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
		
		return taskLogDtos;
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
	public void updateTaskByOrg(UpdateTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		if(null == cmd.getTaskId()) {
			LOGGER.error("Invalid taskId parameter, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid taskId parameter.");
		}
		PmTask task = pmTaskProvider.findTaskById(cmd.getTaskId());
		if(null == task) {
			LOGGER.error("PmTask not found.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"PmTask not found.");
		}
		if(null != cmd.getCategoryId())
			checkCategory(cmd.getCategoryId());
		task.setCategoryId(cmd.getCategoryId());
		task.setPriority(cmd.getPriority());
		if(null != cmd.getReserveTime())
			task.setReserveTime(new Timestamp(cmd.getReserveTime()));
		else
			task.setReserveTime(null);
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
		if(null == parentId){
			Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
			Category ancestor = categoryProvider.findCategoryById(defaultId);
			parentId = ancestor.getId();
		}
		ListTaskCategoriesResponse response = new ListTaskCategoriesResponse();
		
		List<Category> list = null;
		if(null != cmd.getTaskCategoryId() && cmd.getTaskCategoryId() != 0L && (null == cmd.getParentId() || cmd.getParentId() == 0L)) {
			Category category = categoryProvider.findCategoryById(cmd.getTaskCategoryId());
			list = new ArrayList<Category>();
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
//		Category ancestor = categoryProvider.findCategoryById(defaultId);
		
		List<Category> categories = categoryProvider.listTaskCategories(namespaceId, null, null,
				null, null);
		
		List<CategoryDTO> dtos = categories.stream().map(r -> {
			CategoryDTO dto = ConvertHelper.convert(r, CategoryDTO.class);
			dto.setIsSupportDelete((byte)1);
			return dto;
		}).collect(Collectors.toList());
		List<CategoryDTO> result = new ArrayList<CategoryDTO>();
		for(CategoryDTO c: dtos) {
			if(defaultId.equals(c.getParentId())) {
				result.add(getChildCategories(dtos, c));
			}
		}
		
		return result;
	}
	
	private CategoryDTO getChildCategories(List<CategoryDTO> categories, CategoryDTO dto){
		
		List<CategoryDTO> children = new ArrayList<CategoryDTO>();
		
		for (CategoryDTO categoryDTO : categories) {
			if(dto.getId().equals(categoryDTO.getParentId())){
				children.add(getChildCategories(categories, categoryDTO));
			}
		}
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
		if(listSize > 0){
    		response.setRequests(list.stream().map(t -> {
    			PmTask task = pmTaskProvider.findTaskById(t.getId());
    			PmTaskDTO dto = ConvertHelper.convert(t, PmTaskDTO.class);
    			
    			Category category = checkCategory(task.getTaskCategoryId());
    			dto.setTaskCategoryId(category.getId());
    			dto.setTaskCategoryName(category.getName());
    			
    			setPmTaskDTOAddress(task, dto);
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
		if(null != status && (status.equals(PmTaskProcessStatus.PROCESSED.getCode()) || 
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
    			if(null == r.getOrganizationId() || r.getOrganizationId() ==0 ){
    				User user = userProvider.findUserById(r.getCreatorUid());
        			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
        			dto.setRequestorName(user.getNickName());
        			dto.setRequestorPhone(userIdentifier.getIdentifierToken());
    			}
    			Category category = categoryProvider.findCategoryById(r.getCategoryId());
    			Category taskCategory = checkCategory(r.getTaskCategoryId());
    			if(null != category)
    				dto.setCategoryName(category.getName());
    	    	dto.setTaskCategoryName(taskCategory.getName());
    			
    			setPmTaskDTOAddress(r, dto);
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
