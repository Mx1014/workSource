package com.everhomes.pmtask;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;





import javax.servlet.http.HttpServletResponse;





import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;





import com.everhomes.acl.AclProvider;
import com.everhomes.acl.RoleAssignment;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.AttachmentDescriptor;
import com.everhomes.rest.pmtask.CancelTaskCommand;
import com.everhomes.rest.pmtask.CategoryStatisticsDTO;
import com.everhomes.rest.pmtask.CategoryTaskStatisticsDTO;
import com.everhomes.rest.pmtask.CloseTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskOperatePersonCommand;
import com.everhomes.rest.pmtask.DeleteTaskOperatePersonCommand;
import com.everhomes.rest.pmtask.EvaluateScoreDTO;
import com.everhomes.rest.pmtask.GetPrivilegesCommand;
import com.everhomes.rest.pmtask.GetPrivilegesDTO;
import com.everhomes.rest.pmtask.GetTaskLogCommand;
import com.everhomes.rest.pmtask.ListAllTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListOperatePersonnelsCommand;
import com.everhomes.rest.pmtask.ListOperatePersonnelsResponse;
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
import com.everhomes.rest.pmtask.PmTaskOperateType;
import com.everhomes.rest.pmtask.PmTaskOwnerType;
import com.everhomes.rest.pmtask.PmTaskPrivilege;
import com.everhomes.rest.pmtask.PmTaskProcessStatus;
import com.everhomes.rest.pmtask.PmTaskSourceType;
import com.everhomes.rest.pmtask.PmTaskStatus;
import com.everhomes.rest.pmtask.PmTaskTargetStatus;
import com.everhomes.rest.pmtask.PmTaskTargetType;
import com.everhomes.rest.pmtask.RevisitCommand;
import com.everhomes.rest.pmtask.SearchTaskCategoryStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTaskStatisticsCommand;
import com.everhomes.rest.pmtask.SearchTaskStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.pmtask.CompleteTaskCommand;
import com.everhomes.rest.pmtask.TaskCategoryStatisticsDTO;
import com.everhomes.rest.pmtask.TaskStatisticsDTO;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class PmTaskServiceImpl implements PmTaskService {
	
	public static final String CATEGORY_SEPARATOR = "/";

    SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
	private PmTaskSearch pmTaskSearch;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private RolePrivilegeService rolePrivilegeService;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private MessagingService messagingService;
	@Autowired
	private SmsProvider smsProvider;
	@Autowired
	private AclProvider aclProvider;
	@Autowired
    private DbProvider dbProvider;
	@Autowired
    private CoordinationProvider coordinationProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private AddressProvider addressProvider;
	
	@Override
	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		SearchTasksResponse response = new SearchTasksResponse();
		List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getStatus(), cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTaskCategoryId(), 
				cmd.getStartDate(), cmd.getEndDate(), cmd.getPageAnchor(), pageSize);
		
		if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> {
    			PmTaskDTO dto = ConvertHelper.convert(r, PmTaskDTO.class);
    			
    			Category category = checkCategory(r.getTaskCategoryId());
    			dto.setTaskCategoryId(category.getId());
    			dto.setTaskCategoryName(category.getName());
    			if(null != r.getAddressId() && r.getAddressId() != 0) {
    				Address address = addressProvider.findAddressById(r.getAddressId());
    				dto.setAddress(address.getAddress());
    			}
    			
    			return dto;
    		}).collect(Collectors.toList()));
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getCreateTime().getTime());
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
			
			if(null == cmd.getOrganizationId()){
				LOGGER.error("OrganizationId cannot be null.");
	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
	    				"OrganizationId cannot be null.");
			}
			List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), current.getId());
	    	if(privileges.contains(PrivilegeConstants.LISTALLTASK)){
	    		list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), status
						, cmd.getPageAnchor(), cmd.getPageSize());
			}else if(!privileges.contains(PrivilegeConstants.LISTUSERTASK)){
				list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), PmTaskProcessStatus.USER_UNPROCESSED.getCode()
						, cmd.getPageAnchor(), cmd.getPageSize());
			}else{
				returnNoPrivileged(privileges, current);
			}
	    	
		}else{
			list = pmTaskProvider.listPmTask(cmd.getOwnerType(), cmd.getOwnerId(), current.getId(), status
					, cmd.getPageAnchor(), cmd.getPageSize());
		}
		
		ListUserTasksResponse response = new ListUserTasksResponse();
		
		if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> {
    			PmTaskDTO dto = ConvertHelper.convert(r, PmTaskDTO.class);
    			if(null == r.getOrganizationId() || r.getOrganizationId() ==0 ){
    				User user = userProvider.findUserById(r.getCreatorUid());
        			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
        			dto.setRequestorName(user.getNickName());
        			dto.setRequestorPhone(userIdentifier.getIdentifierToken());
    			}
//    			Category category = checkCategory(r.getCategoryId());
    			Category category = categoryProvider.findCategoryById(r.getCategoryId());
    			Category taskCategory = checkCategory(r.getTaskCategoryId());
    			if(null != category)
    				dto.setCategoryName(category.getName());
    	    	dto.setTaskCategoryName(taskCategory.getName());
    			
    	    	if(null != r.getAddressId() && r.getAddressId() != 0) {
    				Address address = addressProvider.findAddressById(r.getAddressId());
    				dto.setAddress(address.getAddress());
    			}
    	    	
    			return dto;
    		}).collect(Collectors.toList()));
    		if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getCreateTime().getTime());
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
	@Override
	public GetPrivilegesDTO getPrivileges(GetPrivilegesCommand cmd){
		if(null == cmd.getOrganizationId()){
			LOGGER.error("OrganizationId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId cannot be null.");
		}
		GetPrivilegesDTO dto = new GetPrivilegesDTO();
		User user = UserContext.current().getUser();

		List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
		List<String> result = new ArrayList<String>();
		for(Long p:privileges){
			if(p.longValue() == PrivilegeConstants.LISTALLTASK)
				result.add(PmTaskPrivilege.LISTALLTASK.getCode());
			else if(p.longValue() == PrivilegeConstants.LISTUSERTASK)
				result.add(PmTaskPrivilege.LISTUSERTASK.getCode());
			else if(p.longValue() == PrivilegeConstants.ASSIGNTASK)
				result.add(PmTaskPrivilege.ASSIGNTASK.getCode());
			else if(p.longValue() == PrivilegeConstants.COMPLETETASK)
				result.add(PmTaskPrivilege.COMPLETETASK.getCode());
			else if(p.longValue() == PrivilegeConstants.CLOSETASK)
				result.add(PmTaskPrivilege.CLOSETASK.getCode());
			else if(p.longValue() == PrivilegeConstants.REVISITTASK)
				result.add(PmTaskPrivilege.REVISITTASK.getCode());
		}
		dto.setPrivileges(result);
		return dto;
	}
	
	private void setTaskStatus(Long organizationId, String ownerType, Long ownerId, PmTask task, String content, 
			List<AttachmentDescriptor> attachments, Byte status) {
		checkOwnerIdAndOwnerType(ownerType, ownerId);
		if(null == organizationId){
			LOGGER.error("OrganizationId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId cannot be null.");
		}
		if(StringUtils.isBlank(content)) {
        	LOGGER.error("Content cannot be null.");
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CONTENT_NULL,
    				"Content cannot be null.");
        }
		User user = UserContext.current().getUser();
		long time = System.currentTimeMillis();
		Timestamp now = new Timestamp(time);
		task.setStatus(status);
		 
		if(status.equals(PmTaskStatus.PROCESSED.getCode())){
			task.setProcessedTime(now);
			List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, organizationId, user.getId());
	    	if(!privileges.contains(PrivilegeConstants.COMPLETETASK)){
	    		returnNoPrivileged(privileges, user);
			}
		}
			
		if(status.equals(PmTaskStatus.CLOSED.getCode())){
			task.setClosedTime(now);
			List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, organizationId, user.getId());
	    	if(!privileges.contains(PrivilegeConstants.CLOSETASK)){
	    		returnNoPrivileged(privileges, user);
			}
		}
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
		
		addAttachments(attachments, user.getId(), pmTaskLog.getId(), PmTaskAttachmentType.TASKLOG.getCode());
		
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("operatorName", user.getNickName());
	    map.put("operatorPhone", userIdentifier.getIdentifierToken());
	    
	    String scope = PmTaskNotificationTemplateCode.SCOPE;
	    String locale = PmTaskNotificationTemplateCode.LOCALE;
	    
		
		if(status.equals(PmTaskStatus.PROCESSED.getCode())){
	    	int code = PmTaskNotificationTemplateCode.NOTIFY_TO_CREATOR;
	    	Date d = new Date(time);
	    	SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
	    	SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
	    	map.put("day", sdf1.format(d));
		    map.put("hour", sdf2.format(d));
		    String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			sendMessageToUser(task.getCreatorUid(), text);
			
			code = PmTaskNotificationTemplateCode.NOTIFY_TO_ASSIGNER;
			map.remove("day");
			map.remove("hour");
			User creator = userProvider.findUserById(task.getCreatorUid());
			UserIdentifier creatorIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(creator.getId(), IdentifierType.MOBILE.getCode());
			map.put("creatorName", creator.getNickName());
			map.put("creatorPhone", creatorIdentifier.getIdentifierToken());
			text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			sendMessageToUser(user.getId(), text);
		}
		if(status.equals(PmTaskStatus.CLOSED.getCode())){
			int code = PmTaskNotificationTemplateCode.CLOSED_TASK_LOG;
			String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			sendMessageToUser(task.getCreatorUid(), text);
		}
		
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);
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
	
	@Override
	public void completeTask(CompleteTaskCommand cmd) {
		checkId(cmd.getId());
		PmTask task = checkPmTask(cmd.getId());
		if(task.getStatus() >= PmTaskStatus.PROCESSED.getCode() ){
			LOGGER.error("Task cannot be completed. cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Task cannot be completed.");
		}
		
		setTaskStatus(cmd.getOrganizationId(), cmd.getOwnerType(), cmd.getOwnerId(), task, cmd.getContent(), 
				cmd.getAttachments(), PmTaskStatus.PROCESSED.getCode());
		
	}

	@Override
	public void revisit(RevisitCommand cmd) {
		checkId(cmd.getId());
		PmTask task = checkPmTask(cmd.getId());
		if(task.getStatus() != PmTaskStatus.PROCESSED.getCode() ){
			LOGGER.error("Task cannot be completed. cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Task cannot be completed.");
		}
		
		User user = UserContext.current().getUser();
		long time = System.currentTimeMillis();
		Timestamp now = new Timestamp(time);
		task.setStatus(PmTaskStatus.REVISITED.getCode());
		task.setRevisitTime(now);
		 
		pmTaskProvider.updateTask(task);
		
		PmTaskLog pmTaskLog = new PmTaskLog();
		pmTaskLog.setNamespaceId(task.getNamespaceId());
		pmTaskLog.setOperatorTime(now);
		pmTaskLog.setOperatorUid(user.getId());
		pmTaskLog.setContent(cmd.getContent());
		pmTaskLog.setOwnerId(task.getOwnerId());
		pmTaskLog.setOwnerType(task.getOwnerType());
		pmTaskLog.setStatus(task.getStatus());
		pmTaskLog.setTaskId(task.getId());
		pmTaskProvider.createTaskLog(pmTaskLog);
		
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);
	}
	
	@Override
	public void closeTask(CloseTaskCommand cmd) {
		checkId(cmd.getId());
		
		PmTask task = checkPmTask(cmd.getId());
		if(task.getStatus() >= PmTaskStatus.PROCESSED.getCode()){
			LOGGER.error("Task cannot be closed. cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Task cannot be closed.");
		}
		setTaskStatus(cmd.getOrganizationId(), cmd.getOwnerType(), cmd.getOwnerId(), task, cmd.getContent(), 
				null, PmTaskStatus.CLOSED.getCode());
		
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
		User user = UserContext.current().getUser();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		task.setStatus(PmTaskStatus.INACTIVE.getCode());
		task.setDeleteUid(user.getId());
		task.setDeleteTime(now);
		pmTaskProvider.updateTask(task);
		
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
	}
	
	@Override
	public void assignTask(AssignTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		if(null == cmd.getOrganizationId()){
			LOGGER.error("OrganizationId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId cannot be null.");
		}
		User user = UserContext.current().getUser();
		if(null == cmd.getTargetId()){
			LOGGER.error("TargetId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"TargetId cannot be null.");
		}
		User targetUser = userProvider.findUserById(cmd.getTargetId());
		if(null == targetUser){
			LOGGER.error("TargetUser not found, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_USER_NULL,
    				"TargetUser not found");
		}
		
		
    	List<Long> privileges = rolePrivilegeService.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
    	if(!privileges.contains(PrivilegeConstants.ASSIGNTASK)){
    		returnNoPrivileged(privileges, user);
		}
		
		PmTask task = checkPmTask(cmd.getId());
		
		if(task.getStatus() >= PmTaskStatus.PROCESSED.getCode() ){
			LOGGER.error("Task cannot be assigned. cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Task cannot be assigned.");
		}
		
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
		
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("operatorName", user.getNickName());
	    map.put("operatorPhone", userIdentifier.getIdentifierToken());
	    
		UserIdentifier targetIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(targetUser.getId(), IdentifierType.MOBILE.getCode());
		map.put("targetName", targetUser.getNickName());
	    map.put("targetPhone", targetIdentifier.getIdentifierToken());
	    
	    String scope = PmTaskNotificationTemplateCode.SCOPE;
	    String locale = PmTaskNotificationTemplateCode.LOCALE;
    	
	    int code = PmTaskNotificationTemplateCode.PROCESSING_TASK_LOG;
		String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
		
		sendMessageToUser(task.getCreatorUid(), text);
		
		Category category = categoryProvider.findCategoryById(task.getTaskCategoryId());
        
        String categoryName = category.getName();
    	
		List<Tuple<String, Object>> variables = smsProvider.toTupleList("operatorName", user.getNickName());
		smsProvider.addToTupleList(variables, "operatorPhone", userIdentifier.getIdentifierToken());
		smsProvider.addToTupleList(variables, "categoryName", categoryName);
		smsProvider.sendSms(targetUser.getNamespaceId(), targetIdentifier.getIdentifierToken(), SmsTemplateCode.SCOPE, SmsTemplateCode.PM_TASK_ASSIGN_CODE, user.getLocale(), variables);

		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);
	}
	
	private void returnNoPrivileged(List<Long> privileges, User user){
    	LOGGER.error("non-privileged, privileges="+privileges + ", userId=" + user.getId());
		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
				"non-privileged.");
    }
	
	@Override
	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		PmTask task = checkPmTask(cmd.getId());
		
		PmTaskDTO dto  = ConvertHelper.convert(task, PmTaskDTO.class);
		
		if(null != task.getAddressId() && task.getAddressId() != 0) {
			Address address = addressProvider.findAddressById(task.getAddressId());
			dto.setAddress(address.getAddress());
		}
		
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

	private List<PmTaskLogDTO> listPmTaskLogs(PmTaskDTO task) {
		List<PmTaskLog> taskLogs = pmTaskProvider.listPmTaskLogs(task.getId(), null);
		List<PmTaskLogDTO> taskLogDtos = taskLogs.stream().map(r -> {
			
			PmTaskLogDTO pmTaskLogDTO = ConvertHelper.convert(r, PmTaskLogDTO.class);
			
			Map<String, Object> map = new HashMap<String, Object>();
			
		    String scope = PmTaskNotificationTemplateCode.SCOPE;
		    String locale = PmTaskNotificationTemplateCode.LOCALE;
		    
			if(r.getStatus().equals(PmTaskStatus.UNPROCESSED.getCode())){
			    
				if(null == task.getOrganizationId()){
					setParam(map, task.getCreatorUid());
//					User user = userProvider.findUserById(task.getCreatorUid());
//	    			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
//	    			map.put("operatorName", user.getNickName());
//				    map.put("operatorPhone", userIdentifier.getIdentifierToken());
				}else{
					map.put("operatorName", task.getRequestorName());
				    map.put("operatorPhone", task.getRequestorPhone());
				}
				
				int code = PmTaskNotificationTemplateCode.UNPROCESS_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
				
			}else if(r.getStatus().equals(PmTaskStatus.PROCESSING.getCode())){
				setParam(map, r.getOperatorUid());
				User target = userProvider.findUserById(r.getTargetId());
				UserIdentifier targetIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(target.getId(), IdentifierType.MOBILE.getCode());
				map.put("targetName", target.getNickName());
			    map.put("targetPhone", targetIdentifier.getIdentifierToken());
			    
			    int code = PmTaskNotificationTemplateCode.PROCESSING_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
				
			}else if(r.getStatus().equals(PmTaskStatus.PROCESSED.getCode())){
				setParam(map, r.getOperatorUid());
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
				setParam(map, r.getOperatorUid());
				int code = PmTaskNotificationTemplateCode.CLOSED_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
			}else {
				setParam(map, r.getOperatorUid());
				int code = PmTaskNotificationTemplateCode.REVISITED_TASK_LOG;
				String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
				pmTaskLogDTO.setText(text);
			}
			
			return pmTaskLogDTO;
		}).collect(Collectors.toList());
		return taskLogDtos;
	}
	
	private void setParam(Map<String, Object> map, Long userId) {
		User user = userProvider.findUserById(userId);
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		map.put("operatorName", user.getNickName());
	    map.put("operatorPhone", userIdentifier.getIdentifierToken());
	}
	
	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd) {
		
		User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		cmd.setSourceType(PmTaskSourceType.APP.getCode());
		return createTask(cmd, user.getId(), user.getNickName(), userIdentifier.getIdentifierToken());
	}
	
	@Override
	public PmTaskDTO createTaskByOrg(CreateTaskCommand cmd) {
		String requestorPhone = cmd.getRequestorPhone();
		String requestorName = cmd.getRequestorName();
		if(StringUtils.isBlank(requestorPhone)){
			LOGGER.error("RequestorPhone cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"RequestorPhone cannot be null.");
		}
		if(StringUtils.isBlank(requestorName)){
			LOGGER.error("RequestorName cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"RequestorName cannot be null.");
		}
		
		if(null == cmd.getOrganizationId()){
			LOGGER.error("OrganizationId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId cannot be null.");
		}
		
		return createTask(cmd, null, requestorName, requestorPhone);
	}
	
	private PmTaskDTO createTask(CreateTaskCommand cmd, Long userId, String requestorName, String requestorPhone){
		String ownerType = cmd.getOwnerType();
		Long ownerId = cmd.getOwnerId();
		Long taskCategoryId = cmd.getTaskCategoryId();
		String content = cmd.getContent();
		checkCreateTaskParam(ownerType, ownerId, taskCategoryId, content);
		
		PmTask task = new PmTask();
		dbProvider.execute((TransactionStatus status) -> {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			User user = UserContext.current().getUser();

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
			task.setAddressId(cmd.getAddressId());
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
			
			pmTaskSearch.feedDoc(task);
			
			Category category = categoryProvider.findCategoryById(taskCategoryId);
	    	
	    	Long organizationId = cmd.getOrganizationId();
	    	if(null == organizationId) {
	            List<OrganizationCommunity> orgs = organizationProvider.listOrganizationByCommunityId(ownerId);
	            if(null != orgs && orgs.size() != 0) {
	            	organizationId = orgs.get(0).getOrganizationId();
	            }
			}
	    	
	    	List<PmTaskTarget> targets = pmTaskProvider.listTaskTargets(cmd.getOwnerType(), cmd.getOwnerId(), RoleConstants.PM_TASK_EXECUTOR, 
					null, null);
	    	int size = targets.size();
	    	if(LOGGER.isDebugEnabled())
	    		LOGGER.debug("Create pmtask and send message, size={}, cmd={}", size, cmd);
	    	if(size > 0){
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
	        		smsProvider.addToTupleList(variables, "categoryName", category.getName());
	        		smsProvider.sendSms(user.getNamespaceId(), s, SmsTemplateCode.SCOPE, 
	        				SmsTemplateCode.PM_TASK_CREATOR_CODE, user.getLocale(), variables);
	        	}
	        	
	    	}
			return null;
		});
    	
		return ConvertHelper.convert(task, PmTaskDTO.class);
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
	
	@Override
	public void deleteTaskCategory(DeleteTaskCategoryCommand cmd) {
		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
		Long id = cmd.getId();
		checkId(id);
		
		Category category = categoryProvider.findCategoryById(id);
		if(category == null) {
			LOGGER.error("PmTask category not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_NULL,
					"PmTask category not found");
		}
		if(!category.getNamespaceId().equals(namespaceId)){
			LOGGER.error("Current User have no legal power, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
					"Current User have no legal power");
		}
		category.setStatus(CategoryAdminStatus.INACTIVE.getCode());
		categoryProvider.updateCategory(category);
	}

	@Override
	public CategoryDTO createTaskCategory(CreateTaskCategoryCommand cmd) {
		
		Integer namespaceId = UserContext.current().getUser().getNamespaceId();
//		checkNamespaceId(namespaceId);
		//防止插入重复
		Long parentId = cmd.getParentId();
		String path = "";
		Category category = null;
		if(null == parentId){
			
//			String defaultName = configProvider.getValue("pmtask.category.ancestor", "");
			Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
			Category ancestor = categoryProvider.findCategoryById(defaultId);
			parentId = ancestor.getId();
			path = ancestor.getPath() + CATEGORY_SEPARATOR + cmd.getName();
			
			category = categoryProvider.findCategoryByPath(namespaceId, path);
			if(category != null) {
				LOGGER.error("PmTask category have been in existing");
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_SERVICE_CATEGORY_EXIST,
						"PmTask category have been in existing");
			}
		}else{
			category = categoryProvider.findCategoryById(parentId);
			if(category == null) {
				LOGGER.error("PmTask parent category not found, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_SERVICE_CATEGORY_NULL,
						"PmTask parent category not found");
			}
			path = category.getPath() + CATEGORY_SEPARATOR + cmd.getName();
			
			category = categoryProvider.findCategoryByPath(namespaceId, path);
			if(category != null) {
				LOGGER.error("PmTask category have been in existing");
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_EXIST,
						"PmTask category have been in existing");
			}
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
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		//Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		Integer pageSize = cmd.getPageSize();
		Long parentId = cmd.getParentId();
		if(null == parentId){
//			String defaultName = configProvider.getValue("pmtask.category.ancestor", "");
//			Category ancestor = categoryProvider.findCategoryByPath(cmd.getNamespaceId(), defaultName);
			Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
			Category ancestor = categoryProvider.findCategoryById(defaultId);
			parentId = ancestor.getId();
		}
		ListTaskCategoriesResponse response = new ListTaskCategoriesResponse();
		
		List<Category> list = categoryProvider.listTaskCategories(namespaceId, parentId, cmd.getKeyword(),
				cmd.getPageAnchor(), cmd.getPageSize());
		
		if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> {
    			CategoryDTO dto = ConvertHelper.convert(r, CategoryDTO.class);
    			List<Category> tempList = categoryProvider.listTaskCategories(namespaceId, null, r.getPath(),
    					null, null);
    			getChildCategories(tempList.stream().map(k -> ConvertHelper.convert(k, CategoryDTO.class))
    					.collect(Collectors.toList()), dto);
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
	public List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
//		Category ancestor = categoryProvider.findCategoryById(defaultId);
		
		List<Category> categories = categoryProvider.listTaskCategories(namespaceId, null, null,
				null, null);
		
		List<CategoryDTO> dtos = categories.stream().map(r -> ConvertHelper.convert(r, CategoryDTO.class))
				.collect(Collectors.toList());
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
	
	@Override
	public void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		//Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		if(null == cmd.getPageSize())
			cmd.setPageSize(100000);
		List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getStatus(), cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTaskCategoryId(), 
				cmd.getStartDate(), cmd.getEndDate(), cmd.getPageAnchor(), cmd.getPageSize());
		
		Workbook wb = new XSSFWorkbook();
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("task");
		sheet.setDefaultColumnWidth(20);  
		sheet.setDefaultRowHeightInPoints(20); 
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("服务类型");
		row.createCell(1).setCellValue("内容");
		row.createCell(2).setCellValue("发起人");
		row.createCell(3).setCellValue("联系电话");
		row.createCell(4).setCellValue("发起时间");
		row.createCell(5).setCellValue("状态");
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			PmTaskDTO task = list.get(i);
			Category category = checkCategory(task.getTaskCategoryId());
			tempRow.createCell(0).setCellValue(category.getName());
			tempRow.createCell(1).setCellValue(task.getContent());
			tempRow.createCell(2).setCellValue(task.getRequestorName());
			tempRow.createCell(3).setCellValue(task.getRequestorPhone());
			tempRow.createCell(4).setCellValue(datetimeSF.format(task.getCreateTime()));
			tempRow.createCell(5).setCellValue(convertStatus(task.getStatus()));
			
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			download(out, resp);
		} catch (IOException e) {
			LOGGER.error("ExportTasks is fail, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"ExportTasks is fail.");
		}
	}
	private String convertStatus(Byte status){
		if(status.byteValue() == 1)
			return "未处理";
		else if(status.byteValue() == 2)
			return "已分派";
		else if(status.byteValue() == 3)
			return "已完成";
		else if(status.byteValue() == 4)
			return "已关闭";
		else
			return "";
	}
	private HttpServletResponse download(ByteArrayOutputStream out, HttpServletResponse response) {
        try {

            // 清空response
            //response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + System.currentTimeMillis()+".xlsx");
            //response.addHeader("Content-Length", "" + out.);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(out.toByteArray());
            toClient.flush();
            toClient.close();
            
        } catch (IOException ex) { 
 			LOGGER.error(ex.getMessage());
 			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
 					ErrorCodes.ERROR_GENERAL_EXCEPTION,
 					ex.getLocalizedMessage());
     		 
        }
        return response;
    }
	
	@Override
	public SearchTaskStatisticsResponse searchTaskStatistics(SearchTaskStatisticsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		SearchTaskStatisticsResponse response = new SearchTaskStatisticsResponse();
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<PmTaskStatistics> list = pmTaskProvider.searchTaskStatistics(namespaceId, null, cmd.getTaskCategoryId(), cmd.getKeyword(), new Timestamp(cmd.getDateStr()),
				cmd.getPageAnchor(), cmd.getPageSize());
		
		list = mergeTaskOwnerList(list);
		
		if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> {
    			TaskStatisticsDTO dto = new TaskStatisticsDTO();
    			
    			dto.setNewCount(r.getTotalCount());
    			dto.setOwnerId(r.getOwnerId());
    			Community community = communityProvider.findCommunityById(r.getOwnerId());
    			dto.setOwnerName(community.getName());
    			dto.setUnProcessedCount(r.getUnprocessCount());
    			dto.setCompletePercent(r.getTotalCount()!=null&&!r.getTotalCount().equals(0)?(float)r.getProcessedCount()/r.getTotalCount():0);
    			dto.setClosePercent(r.getTotalCount()!=null&&!r.getTotalCount().equals(0)?(float)r.getCloseCount()/r.getTotalCount():0);
    			float avgStar = calculatePerson(r)!=0?(float) (calculateStar(r)) / (calculatePerson(r)):0;
    			dto.setAvgStar((float)Math.round(avgStar * 10)/10);
    			Integer totalCount = pmTaskProvider.countTaskStatistics(r.getOwnerId(), r.getTaskCategoryId(), null);
    			dto.setTotalCount(totalCount);
    			
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
	
	private List<PmTaskStatistics> mergeTaskCategoryList(List<PmTaskStatistics> list) {
		List<PmTaskStatistics> result = new ArrayList<PmTaskStatistics>();
		Map<Long, PmTaskStatistics> tempMap = new HashMap<>();
		for(PmTaskStatistics p: list){
			Long id = p.getTaskCategoryId();
			PmTaskStatistics pts = null;
			if(tempMap.containsKey(id)){
				pts = tempMap.get(id);
				pts.setTotalCount(pts.getTotalCount() + p.getTotalCount());
				pts.setUnprocessCount(pts.getUnprocessCount() + p.getUnprocessCount());
				pts.setProcessingCount(pts.getProcessingCount() + p.getProcessingCount());
				pts.setProcessedCount(pts.getProcessedCount() + p.getProcessedCount());
				pts.setCloseCount(pts.getCloseCount() + p.getCloseCount());
				pts.setStar1(pts.getStar1() + p.getStar1());
				pts.setStar2(pts.getStar2() + p.getStar2());
				pts.setStar3(pts.getStar3() + p.getStar3());
				pts.setStar4(pts.getStar4() + p.getStar4());
				pts.setStar5(pts.getStar5() + p.getStar5());
				continue;
			}
			tempMap.put(id, p);
		}
	
		for(PmTaskStatistics p1:tempMap.values()){
			result.add(p1);
		}
		
		return result;
	}
	
	private List<PmTaskStatistics> mergeTaskOwnerList(List<PmTaskStatistics> list) {
		
		List<PmTaskStatistics> result = new ArrayList<PmTaskStatistics>();
		Map<Long, List<PmTaskStatistics>> tempMap = new HashMap<>();
		for(PmTaskStatistics p: list){
			Long id = p.getOwnerId();
			if(tempMap.containsKey(id)){
				List<PmTaskStatistics> ptsList = tempMap.get(id);
				List<PmTaskStatistics> temp = new ArrayList<PmTaskStatistics>();
				temp.addAll(ptsList);
				temp.add(p);
				tempMap.put(id, temp);
				continue;
			}
			p.setCategoryId(null);
			tempMap.put(id, Collections.singletonList(p));
		}
		
		for(List<PmTaskStatistics>  l:tempMap.values()){
			l = mergeTaskCategoryList(l);
			result.addAll(l);
		}
		return result;
	}
	
	private int calculateStar(PmTaskStatistics r){
		return 1*r.getStar1()+2*r.getStar2()+3*r.getStar3()+4*r.getStar4()+5*r.getStar5();
	}
	private int calculatePerson(PmTaskStatistics r){
		return r.getStar1()+r.getStar2()+r.getStar3()+r.getStar4()+r.getStar5();
	}
	
	@Override
	public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		GetStatisticsResponse response = new GetStatisticsResponse();
		
		response.setOwnerId(cmd.getOwnerId());
		
		List<PmTaskStatistics> list = pmTaskProvider.searchTaskStatistics(namespaceId, cmd.getOwnerId(), null, null, new Timestamp(cmd.getDateStr()),
				null, null);
		
		if(null != cmd.getOwnerId()){
			Community community = communityProvider.findCommunityById(cmd.getOwnerId());
			list = mergeTaskCategoryList(list);
			response.setOwnerName(community.getName());
		}else{
			list = mergeTaskOwnerList(list);
			list = mergeTaskCategoryList(list);
		}
		
		int totalCount = 0;
		int evaluateCount = 0;
		int totalStar = 0;
		int[] stars = new int[5];
		
		List<EvaluateScoreDTO> evaluates = new ArrayList<EvaluateScoreDTO>();
		List<CategoryTaskStatisticsDTO> categoryTaskStatistics = new ArrayList<CategoryTaskStatisticsDTO>();
		
		for(PmTaskStatistics statistics: list){
			totalCount += statistics.getTotalCount();
			evaluateCount += calculatePerson(statistics);
			totalStar += calculateStar(statistics);
			stars[0] += statistics.getStar1();
			stars[1] += statistics.getStar2();
			stars[2] += statistics.getStar3();
			stars[3] += statistics.getStar4();
			stars[4] += statistics.getStar5();
			
			CategoryTaskStatisticsDTO dto = new CategoryTaskStatisticsDTO();
			dto.setTaskCategoryId(statistics.getTaskCategoryId());
			Category category = checkCategory(statistics.getTaskCategoryId());
			dto.setTaskCategoryName(category.getName());
			dto.setCloseCount(statistics.getCloseCount());
			dto.setProcessedCount(statistics.getProcessedCount());
			dto.setProcessingCount(statistics.getProcessingCount());
			dto.setTotalCount(statistics.getTotalCount());
			dto.setUnprocesseCount(statistics.getUnprocessCount());
			
			categoryTaskStatistics.add(dto);
		}
		
		for(int i=0;i<5;i++){
			evaluates.add(new EvaluateScoreDTO(i+1, stars[i]));
		}
		
		response.setTotalCount(totalCount);
		response.setEvaluateCount(evaluateCount);
		float avgStar = evaluateCount!=0?(float) (totalStar/evaluateCount):0;
		response.setAvgScore(avgStar);
		response.setEvaluates(evaluates);
		response.setCategoryTaskStatistics(categoryTaskStatistics);
		
		return response;
	}
	
	@Scheduled(cron="0 5 0 1 * ? ")
	public void createStatistics(){
		
		this.coordinationProvider.getNamedLock(CoordinationLocks.PMTASK_STATISTICS.getCode()).enter(()-> {
			List<Namespace> namepaces = pmTaskProvider.listNamespace();
			long now = System.currentTimeMillis();
			Timestamp startDate = getBeginOfMonth(now);     
			Timestamp endDate = getEndOfMonth(now);
			boolean isOperateByAdmin = configProvider.getBooleanValue("pmtask.statistics.create", false);
			if(isOperateByAdmin){
				startDate = getEndOfMonth(now);
				endDate = null;
			}
		for(Namespace n: namepaces){
			Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
			Category ancestor = categoryProvider.findCategoryById(defaultId);
			
			if(ancestor != null){
				//防止定时任务重复执行
				List<PmTaskStatistics> list = pmTaskProvider.searchTaskStatistics(n.getId(), null, null, null, startDate,
						null, 10);
				if(list.size() != 0)
					break;
				
				List<Category> categories = categoryProvider.listTaskCategories(n.getId(), ancestor.getId(), null, null, null);
				if(null != categories && !categories.isEmpty()){
					List<Community> communities = communityProvider.listCommunitiesByNamespaceId(n.getId());
					for(Community community:communities){
						for(Category taskCategory: categories) {
							
							List<Category> tempCategories = categoryProvider.listTaskCategories(n.getId(), taskCategory.getId(), null, null, null);
							for(Category category: tempCategories) {
								PmTaskStatistics statistics = new PmTaskStatistics();
								Integer totalCount = pmTaskProvider.countTask(community.getId(), null, taskCategory.getId(), category.getId(), null, startDate, endDate);
								Integer unprocessCount = pmTaskProvider.countTask(community.getId(), PmTaskStatus.UNPROCESSED.getCode(), taskCategory.getId(), category.getId(), null, startDate, endDate);
								Integer processingCount = pmTaskProvider.countTask(community.getId(), PmTaskStatus.PROCESSING.getCode(), taskCategory.getId(), category.getId(), null, startDate, endDate);
								Integer processedCount = pmTaskProvider.countTask(community.getId(), PmTaskStatus.PROCESSED.getCode(), taskCategory.getId(), category.getId(), null, startDate, endDate);
								Integer closeCount = pmTaskProvider.countTask(community.getId(), PmTaskStatus.CLOSED.getCode(), taskCategory.getId(), category.getId(), null, startDate, endDate);
								
								Integer star1 = pmTaskProvider.countTask(community.getId(), null, taskCategory.getId(), category.getId(), (byte)1, startDate, endDate);
								Integer star2 = pmTaskProvider.countTask(community.getId(), null, taskCategory.getId(), category.getId(), (byte)2, startDate, endDate);
								Integer star3 = pmTaskProvider.countTask(community.getId(), null, taskCategory.getId(), category.getId(), (byte)3, startDate, endDate);
								Integer star4 = pmTaskProvider.countTask(community.getId(), null, taskCategory.getId(), category.getId(), (byte)4, startDate, endDate);
								Integer star5 = pmTaskProvider.countTask(community.getId(), null, taskCategory.getId(), category.getId(), (byte)5, startDate, endDate);
								
								statistics.setTaskCategoryId(taskCategory.getId());
								statistics.setCategoryId(category.getId());
								statistics.setCreateTime(new Timestamp(now));
								statistics.setDateStr(startDate);
								statistics.setNamespaceId(n.getId());
								statistics.setOwnerId(community.getId());
								statistics.setOwnerType(PmTaskOwnerType.COMMUNITY.getCode());
								
								statistics.setTotalCount(totalCount);
								statistics.setUnprocessCount(unprocessCount);
								statistics.setProcessedCount(processedCount);
								statistics.setProcessingCount(processingCount);
								statistics.setCloseCount(closeCount);
								
								statistics.setStar1(star1);
								statistics.setStar2(star2);
								statistics.setStar3(star3);
								statistics.setStar4(star4);
								statistics.setStar5(star5);
								pmTaskProvider.createTaskStatistics(statistics);
							}
							
						}
					}
					
				}
				
			}
		}
		return null;
        });
		
	}

	private Timestamp getBeginOfMonth(Long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return new Timestamp(calendar.getTime().getTime());
	}
	
	private Timestamp getEndOfMonth(Long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return new Timestamp(calendar.getTime().getTime());
	}
	
	@Override
	public PmTaskLogDTO getTaskLog(GetTaskLogCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		
		PmTaskLog pmTaskLog = checkPmTaskLog(cmd.getId());
		PmTaskLogDTO pmTaskLogDTO = ConvertHelper.convert(pmTaskLog, PmTaskLogDTO.class);
		
		List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(pmTaskLog.getId(), PmTaskAttachmentType.TASKLOG.getCode());
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

	private void checkNamespaceId(Integer namespaceId){
		if(namespaceId == null) {
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
	
	private void checkCreateTaskParam(String ownerType, Long ownerId, Long taskCategoryId, String content){
		checkOwnerIdAndOwnerType(ownerType, ownerId);
    	if(null == taskCategoryId) {
        	LOGGER.error("TaskCategoryId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"TaskCategoryId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(content)) {
        	LOGGER.error("Content cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Content cannot be null.");
        }
    	checkCategory(taskCategoryId);
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

	@Override
	public void exportStatistics(GetStatisticsCommand cmd, HttpServletResponse resp) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		
		Workbook wb = new XSSFWorkbook();
		
		List<PmTaskStatistics> list = pmTaskProvider.searchTaskStatistics(namespaceId, cmd.getOwnerId(), null, null, new Timestamp(cmd.getDateStr()),
				null, null);
		
		Map<Long, List<PmTaskStatistics>> map = convertStatistics(list);
		
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("task");
		setHeader(sheet);
		
		setValue(sheet, map);
		
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			download(out, resp);
		} catch (IOException e) {
			LOGGER.error("ExportStatistics is fail, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"ExportStatistics is fail.");
		}
		
	}

	private void setValue(Sheet sheet, Map<Long, List<PmTaskStatistics>> map){
		Set<Long> keys = map.keySet();
		int start=1,end = 0,i=0;
		for(Long id: keys){
			List<PmTaskStatistics> temp = map.get(id);
			int totalCount = 0;
			//评价人数
			int evaluateCount = 0;
			int totalStar = 0;
//			int[] stars = new int[5];
			for(PmTaskStatistics statistics: temp){
				i++;
				totalCount += statistics.getTotalCount();
				evaluateCount += calculatePerson(statistics);
				totalStar += calculateStar(statistics);
//				stars[0] += statistics.getStar1();
//				stars[1] += statistics.getStar2();
//				stars[2] += statistics.getStar3();
//				stars[3] += statistics.getStar4();
//				stars[4] += statistics.getStar5();
				
				Category category = checkCategory(statistics.getTaskCategoryId());
				Row tempRow = sheet.createRow(i);
				
				tempRow.createCell(0);
				tempRow.createCell(1);
				tempRow.createCell(2);
				tempRow.createCell(3);
				tempRow.createCell(4).setCellValue(category.getName());
				tempRow.createCell(5).setCellValue(statistics.getTotalCount());
				tempRow.createCell(6).setCellValue(statistics.getUnprocessCount());
				tempRow.createCell(7).setCellValue(statistics.getProcessingCount());
				tempRow.createCell(8).setCellValue(statistics.getProcessedCount());
				tempRow.createCell(9).setCellValue(statistics.getCloseCount());
				
				tempRow.createCell(10).setCellValue(statistics.getStar1());
				tempRow.createCell(11).setCellValue(statistics.getStar2());
				tempRow.createCell(12).setCellValue(statistics.getStar3());
				tempRow.createCell(13).setCellValue(statistics.getStar4());
				tempRow.createCell(14).setCellValue(statistics.getStar5());
				
			}
			//设置结束点
			end = end + temp.size();
			CellRangeAddress cra1 = new CellRangeAddress(start, end, 0, 0);
			CellRangeAddress cra2 = new CellRangeAddress(start, end, 1, 1);
			CellRangeAddress cra3 = new CellRangeAddress(start, end, 2, 2);
			CellRangeAddress cra4 = new CellRangeAddress(start, end, 3, 3);
			
			sheet.addMergedRegion(cra1);
			sheet.addMergedRegion(cra2);
			sheet.addMergedRegion(cra3);
			sheet.addMergedRegion(cra4);
			Row tempRow = sheet.getRow(start);
			Community community = communityProvider.findCommunityById(id);
			tempRow.getCell(0).setCellValue(community.getName());
			tempRow.getCell(1).setCellValue(totalCount);
			tempRow.getCell(2).setCellValue(evaluateCount!=0?totalStar/evaluateCount:0);
			tempRow.getCell(3).setCellValue(evaluateCount);
			//设置开始点
			start = end + 1;
			
		}
	}
	
	private void setHeader(Sheet sheet){
		sheet.setDefaultColumnWidth(20);  
		sheet.setDefaultRowHeightInPoints(20); 
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("园区名称");
		row.createCell(1).setCellValue("服务总数量");
		row.createCell(2).setCellValue("平均得分");
		row.createCell(3).setCellValue("评价人数");
		row.createCell(4).setCellValue("服务类型");
		row.createCell(5).setCellValue("总量");
		row.createCell(6).setCellValue("未处理数量");
		row.createCell(7).setCellValue("处理中数量");
		row.createCell(8).setCellValue("已完成数量");
		row.createCell(9).setCellValue("已关闭数量");
		row.createCell(10).setCellValue("一分");
		row.createCell(11).setCellValue("两分");
		row.createCell(12).setCellValue("三分");
		row.createCell(13).setCellValue("四分");
		row.createCell(14).setCellValue("五分");
	}
	
	private Map<Long, List<PmTaskStatistics>> convertStatistics(List<PmTaskStatistics> list){
		Map<Long, List<PmTaskStatistics>> result = new HashMap<>();
		for(PmTaskStatistics s: list){
			List<PmTaskStatistics> tempList = null;
				
			if(result.containsKey(s.getOwnerId())){
				tempList = result.get(s.getOwnerId());
				tempList.add(s);
				continue;
			}
			tempList = new ArrayList<>();
			tempList.add(s);
			result.put(s.getOwnerId(), tempList);
		}
		return result;
	}
	
	@Override
	public void exportListStatistics(SearchTaskStatisticsCommand cmd, HttpServletResponse resp) {
		
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		
		List<PmTaskStatistics> list = pmTaskProvider.searchTaskStatistics(namespaceId, null, cmd.getTaskCategoryId(), cmd.getKeyword(), new Timestamp(cmd.getDateStr()),
				cmd.getPageAnchor(), cmd.getPageSize());
		list = mergeTaskOwnerList(list);
		
		XSSFWorkbook wb = new XSSFWorkbook();
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("task");
		sheet.setDefaultColumnWidth(20);  
		sheet.setDefaultRowHeightInPoints(20); 
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("项目名称");
		row.createCell(1).setCellValue("服务类别");
		row.createCell(2).setCellValue("新增");
		row.createCell(3).setCellValue("总量");
		row.createCell(4).setCellValue("剩余未处理");
		row.createCell(5).setCellValue("任务完成率");
		row.createCell(6).setCellValue("任务关闭率");
		row.createCell(7).setCellValue("客户评价均分");
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			PmTaskStatistics pts = list.get(i);
			Category category = checkCategory(pts.getTaskCategoryId());
			Community community = communityProvider.findCommunityById(pts.getOwnerId());
			tempRow.createCell(0).setCellValue(community.getName());
			tempRow.createCell(1).setCellValue(category.getName());
			tempRow.createCell(2).setCellValue(pts.getTotalCount());
			Integer totalCount = pmTaskProvider.countTaskStatistics(pts.getOwnerId(), pts.getTaskCategoryId(), null);
			tempRow.createCell(3).setCellValue(totalCount);
			tempRow.createCell(4).setCellValue(pts.getUnprocessCount());
			CellStyle cellStyle = wb.createCellStyle();
			XSSFDataFormat fmt = wb.createDataFormat(); 
			cellStyle.setDataFormat(fmt.getFormat("0.00%"));
			Cell cell4 = tempRow.createCell(5);
			cell4.setCellStyle(cellStyle);
			cell4.setCellValue(pts.getTotalCount()!=null&&!pts.getTotalCount().equals(0)?(float)pts.getProcessedCount()/pts.getTotalCount():0);
			Cell cell5 = tempRow.createCell(6);
			cell5.setCellStyle(cellStyle);
			cell5.setCellValue(pts.getTotalCount()!=null&&!pts.getTotalCount().equals(0)?(float)pts.getCloseCount()/pts.getTotalCount():0);
			float avgStar = calculatePerson(pts)!=0?(float) (calculateStar(pts)) / (calculatePerson(pts)):0;
			tempRow.createCell(7).setCellValue(avgStar);
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			download(out, resp);
		} catch (IOException e) {
			LOGGER.error("ExportListStatistics is fail, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"ExportListStatistics is fail.");
		}
		
	}


	@Override
	public ListOperatePersonnelsResponse listOperatePersonnels(ListOperatePersonnelsCommand cmd) {
		
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		if(null == cmd.getOperateType()) {
        	LOGGER.error("OperateType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OperateType cannot be null.");
        }
		if(null == cmd.getOrganizationId()) {
        	LOGGER.error("OrganizationId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId cannot be null.");
        }
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(null == org) {
        	LOGGER.error("OrganizationId not found.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId not found.");
        }
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		Long roleId = null;
		if(cmd.getOperateType().equals(PmTaskOperateType.EXECUTOR.getCode()))
			roleId = RoleConstants.PM_TASK_EXECUTOR;
		else if(cmd.getOperateType().equals(PmTaskOperateType.REPAIR.getCode()))
			roleId = RoleConstants.PM_TASK_REPAIRMEN;
		List<PmTaskTarget> targets = pmTaskProvider.listTaskTargets(cmd.getOwnerType(), cmd.getOwnerId(), roleId, 
				cmd.getPageAnchor(), cmd.getPageSize());
		
		ListOperatePersonnelsResponse response = new ListOperatePersonnelsResponse();

		List<OrganizationMember> organizationMembers = new ArrayList<OrganizationMember>();
		for(PmTaskTarget t: targets) {
			OrganizationMember m = organizationProvider.findOrganizationMemberByOrgIdAndUId(t.getTargetId(), cmd.getOrganizationId());
			if(null != m)
				organizationMembers.add(m);
		}
		
		if(targets.size() > 0){
			response.setMembers(organizationService.convertOrganizationMemberDTO(organizationMembers, org));
    		if(targets.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(targets.get(targets.size()-1).getId());
        	}
    	}
		
		return response;
	}

	@Override
	public void createTaskOperatePerson(CreateTaskOperatePersonCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		if(null == cmd.getOperateType()) {
        	LOGGER.error("OperateType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OperateType cannot be null.");
        }
		if(null == cmd.getOrganizationId()) {
        	LOGGER.error("OrganizationId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId cannot be null.");
        }
		Long tempRoleId = null;
		if(cmd.getOperateType().equals(PmTaskOperateType.EXECUTOR.getCode()))
			tempRoleId = RoleConstants.PM_TASK_EXECUTOR;
		else if(cmd.getOperateType().equals(PmTaskOperateType.REPAIR.getCode()))
			tempRoleId = RoleConstants.PM_TASK_REPAIRMEN;
//		PmTaskTarget target = pmTaskProvider.findTaskTarget(cmd.getOwnerType(), cmd.getOwnerId(), roleId,
//				cmd.getTargetType(), cmd.getTargetId());
		List<Long> targetIds = cmd.getTargetIds();
		if(null == targetIds || targetIds.isEmpty()) {
			LOGGER.error("TargetIds cannot be null or empty.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"TargetIds cannot be null or empty.");
		}
		
		final Long roleId = tempRoleId;
		dbProvider.execute((TransactionStatus status) -> {
			
			for(int i=0,l=targetIds.size(); i<l;i++ ) {
				PmTaskTarget pmTaskTarget = pmTaskProvider.findTaskTarget(cmd.getOwnerType(), cmd.getOwnerId(), roleId,
						EntityType.USER.getCode(), targetIds.get(i));
				if(null == pmTaskTarget) {
					pmTaskTarget = new PmTaskTarget();
					pmTaskTarget.setRoleId(roleId);
					pmTaskTarget.setOwnerId(cmd.getOwnerId());
					pmTaskTarget.setOwnerType(cmd.getOwnerType());
					pmTaskTarget.setStatus(PmTaskTargetStatus.ACTIVE.getCode());
					pmTaskTarget.setTargetType(EntityType.USER.getCode());
					pmTaskTarget.setTargetId(targetIds.get(i));
					
					pmTaskProvider.createTaskTarget(pmTaskTarget);
					
					RoleAssignment roleAssignment = new RoleAssignment();
					roleAssignment.setRoleId(pmTaskTarget.getRoleId());
					roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
					roleAssignment.setOwnerId(cmd.getOrganizationId());
					roleAssignment.setTargetType(EntityType.USER.getCode());
					roleAssignment.setTargetId(targetIds.get(i));
					roleAssignment.setCreatorUid(UserContext.current().getUser().getId());
					aclProvider.createRoleAssignment(roleAssignment);
				}
				
			}
			
		return null;
		});
		
	}

	@Override
	public void deleteTaskOperatePerson(DeleteTaskOperatePersonCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		if(null == cmd.getOperateType()) {
        	LOGGER.error("OperateType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OperateType cannot be null.");
        }
		if(null == cmd.getOrganizationId()) {
        	LOGGER.error("OrganizationId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId cannot be null.");
        }
		dbProvider.execute((TransactionStatus status) -> {
			Long roleId = null;
			if(cmd.getOperateType().equals(PmTaskOperateType.EXECUTOR.getCode()))
				roleId = RoleConstants.PM_TASK_EXECUTOR;
			else if(cmd.getOperateType().equals(PmTaskOperateType.REPAIR.getCode()))
				roleId = RoleConstants.PM_TASK_REPAIRMEN;
			PmTaskTarget pmTaskTarget = pmTaskProvider.findTaskTarget(cmd.getOwnerType(), cmd.getOwnerId(), roleId,
					EntityType.USER.getCode(), cmd.getTargetId());
			pmTaskTarget.setStatus(PmTaskTargetStatus.INACTIVE.getCode());
			
			pmTaskProvider.updateTaskTarget(pmTaskTarget);
			
			List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(),
					cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getTargetId());
			for(RoleAssignment r: roleAssignments) {
				if(r.getRoleId().equals(roleId)) {
					aclProvider.deleteRoleAssignment(r);
				}
			}
		return null;
		});
		
	}

	@Override
	public SearchTaskCategoryStatisticsResponse searchTaskCategoryStatistics(SearchTaskStatisticsCommand cmd) {
		SearchTaskCategoryStatisticsResponse response = new SearchTaskCategoryStatisticsResponse();

		List<TaskCategoryStatisticsDTO> list = queryTaskCategoryStatistics(cmd);
		if(list.size() > 0){
    		response.setRequests(list);
        	response.setNextPageAnchor(null);
    	}
		
		return response;
	}

	private List<TaskCategoryStatisticsDTO> queryTaskCategoryStatistics(SearchTaskStatisticsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<PmTaskStatistics> temp = pmTaskProvider.searchTaskStatistics(namespaceId, null, cmd.getTaskCategoryId(), cmd.getKeyword(), new Timestamp(cmd.getDateStr()),
				cmd.getPageAnchor(), cmd.getPageSize());
		List<TaskCategoryStatisticsDTO> list = handleTaskCategoryStatistics(temp);
		
		return list;
	}
	
	private List<TaskCategoryStatisticsDTO> handleTaskCategoryStatistics(List<PmTaskStatistics> temp) {
		List<TaskCategoryStatisticsDTO> list = new ArrayList<TaskCategoryStatisticsDTO>();
		outer:
		for(PmTaskStatistics pts: temp) {
			Community community = communityProvider.findCommunityById(pts.getOwnerId());
			for(TaskCategoryStatisticsDTO d: list) {
				if(pts.getOwnerId().equals(d.getOwnerId())) {
					CategoryStatisticsDTO categoryStatisticsDTO = new CategoryStatisticsDTO();
					categoryStatisticsDTO.setCategoryId(pts.getCategoryId());
					Category category = categoryProvider.findCategoryById(pts.getCategoryId());
					categoryStatisticsDTO.setCategoryName(category.getName());
					categoryStatisticsDTO.setTotalCount(pts.getTotalCount());
					categoryStatisticsDTO.setOwnerId(pts.getOwnerId());
					categoryStatisticsDTO.setOwnerName(community.getName());
					d.getRequests().add(categoryStatisticsDTO);
					continue outer;
				}
			}
			TaskCategoryStatisticsDTO dto = new TaskCategoryStatisticsDTO();
			
			Category taskCategory = categoryProvider.findCategoryById(pts.getTaskCategoryId());
			Category category = categoryProvider.findCategoryById(pts.getCategoryId());

			dto.setTaskCategoryId(pts.getTaskCategoryId());
			dto.setTaskCategoryName(taskCategory.getName());
			dto.setOwnerId(pts.getOwnerId());
//			dto.setOwnerName(community.getName());
			CategoryStatisticsDTO categoryStatisticsDTO = new CategoryStatisticsDTO();
			categoryStatisticsDTO.setCategoryId(pts.getCategoryId());
			categoryStatisticsDTO.setCategoryName(category.getName());
			categoryStatisticsDTO.setTotalCount(pts.getTotalCount());
			categoryStatisticsDTO.setOwnerId(pts.getOwnerId());
			categoryStatisticsDTO.setOwnerName(community.getName());
			
			List<CategoryStatisticsDTO> list2 = new ArrayList<CategoryStatisticsDTO>();
			list2.add(categoryStatisticsDTO);
			dto.setRequests(list2);
			
			list.add(dto);
		}
		
		return list;
	}
	
	@Override
	public void exportTaskCategoryStatistics(SearchTaskStatisticsCommand cmd, HttpServletResponse resp) {
		
		List<TaskCategoryStatisticsDTO> list = queryTaskCategoryStatistics(cmd);
		XSSFWorkbook wb = new XSSFWorkbook();
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("task");
		sheet.setDefaultColumnWidth(20);  
		sheet.setDefaultRowHeightInPoints(20); 
		Row firstRow = sheet.createRow(0);
		firstRow.createCell(0).setCellValue("项目名称");
		
		for(int i=0,l=list.size();i<l;i++){
			Row tempRow = sheet.createRow(i + 1);
			TaskCategoryStatisticsDTO dto = list.get(i);
			Category category = checkCategory(dto.getTaskCategoryId());
			Community community = communityProvider.findCommunityById(dto.getOwnerId());
			tempRow.createCell(0).setCellValue(community.getName());
			for(int j=0,l2=dto.getRequests().size();j<l2;j++ ) {
				firstRow.createCell(j+1).setCellValue(dto.getRequests().get(j).getCategoryName());
				tempRow.createCell(j+1).setCellValue(dto.getRequests().get(j).getTotalCount());
			}
			
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			wb.write(out);
			download(out, resp);
		} catch (IOException e) {
			LOGGER.error("ExportListStatistics is fail, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"ExportListStatistics is fail.");
		}
		
	}

	@Override
	public TaskCategoryStatisticsDTO getTaskCategoryStatistics(SearchTaskStatisticsCommand cmd) {
		TaskCategoryStatisticsDTO dto = new TaskCategoryStatisticsDTO();

		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<PmTaskStatistics> temp = pmTaskProvider.searchTaskStatistics(namespaceId, null, cmd.getTaskCategoryId(), cmd.getKeyword(), new Timestamp(cmd.getDateStr()),
				cmd.getPageAnchor(), cmd.getPageSize());
		
		mergeCategoryList(temp);
		List<TaskCategoryStatisticsDTO> list = handleTaskCategoryStatistics(temp);
		
		for(TaskCategoryStatisticsDTO d: list) {
			d.setOwnerId(null);
			d.setOwnerName(null);
			for(CategoryStatisticsDTO cd: d.getRequests()) {
				cd.setOwnerId(null);
				cd.setOwnerName(null);
			}
		}
		if(list.size() != 0)
			dto = list.get(0);
		return dto;
	}

	private void mergeCategoryList(List<PmTaskStatistics> list) {
		
		Map<Long, PmTaskStatistics> tempMap = new HashMap<>();
		for(PmTaskStatistics p: list){
			Long id = p.getCategoryId();
			PmTaskStatistics pts = null;
			if(null != id) {
				if(tempMap.containsKey(id)){
					pts = tempMap.get(id);
					pts.setTotalCount(pts.getTotalCount() + p.getTotalCount());
					pts.setUnprocessCount(pts.getUnprocessCount() + p.getUnprocessCount());
					pts.setProcessingCount(pts.getProcessingCount() + p.getProcessingCount());
					pts.setProcessedCount(pts.getProcessedCount() + p.getProcessedCount());
					pts.setCloseCount(pts.getCloseCount() + p.getCloseCount());
					pts.setStar1(pts.getStar1() + p.getStar1());
					pts.setStar2(pts.getStar2() + p.getStar2());
					pts.setStar3(pts.getStar3() + p.getStar3());
					pts.setStar4(pts.getStar4() + p.getStar4());
					pts.setStar5(pts.getStar5() + p.getStar5());
					continue;
				}
				tempMap.put(id, p);
			}
		}
		list.clear();
		for(PmTaskStatistics p1:tempMap.values()){
			list.add(p1);
		}
	}
}
