package com.everhomes.pmtask;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.family.FamilyProvider;
import com.everhomes.flow.*;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.namespace.*;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.organization.*;

import com.everhomes.rest.pmtask.*;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.*;
import com.everhomes.util.DownloadUtils;
import com.everhomes.util.doc.DocUtil;
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








import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

@Component
public class PmTaskServiceImpl implements PmTaskService {
	final String downloadDir ="\\download\\";

	private static final String CATEGORY_SEPARATOR = "/";

	private static final String HANDLER = "pmtask.handler-";
	
    private SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmTaskServiceImpl.class);
	@Autowired
	private PmTaskProvider pmTaskProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private CategoryProvider categoryProvider;
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
	private SmsProvider smsProvider;
	@Autowired
    private DbProvider dbProvider;
	@Autowired
    private CoordinationProvider coordinationProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private AddressProvider addressProvider;
	@Autowired
    private FamilyService familyService;
	@Autowired
	private NamespaceResourceProvider namespaceResourceProvider;
	@Autowired
	private PmTaskCommonServiceImpl pmTaskCommonService;
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowNodeProvider flowNodeProvider;
	@Autowired
	private BuildingProvider buildingProvider;
	@Autowired
	private FlowCaseProvider flowCaseProvider;
	@Autowired
	private FamilyProvider familyProvider;
	@Autowired
	private ScheduleProvider scheduleProvider;

	@Autowired
	private ServiceModuleService serviceModuleService;
	@Autowired
	private AppProvider appProvider;
	@Autowired
	private NamespaceProvider namespaceProvider;

	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;

	@Override
	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
		//userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_LIST);

		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		//TODO:为科兴与一碑对接
		if(namespaceId == 999983 && null != cmd.getTaskCategoryId() && 
				cmd.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			handle = PmTaskHandle.EBEI;
		}
		
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		return handler.searchTasks(cmd);
	}

	
	@Override
	public ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd) {

		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		//TODO:为科兴与一碑对接
		if(namespaceId == 999983 && null != cmd.getTaskCategoryId() && 
				cmd.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			handle = PmTaskHandle.EBEI;
		}
		
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		return handler.listUserTasks(cmd);
	}

	@Override
	public void evaluateTask(EvaluateTaskCommand cmd) {

		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		handler.evaluateTask(cmd);

	}
	
	@Override
	public GetPrivilegesDTO getPrivileges(GetPrivilegesCommand cmd){
		checkOrganizationId(cmd.getOrganizationId());

		GetPrivilegesDTO dto = new GetPrivilegesDTO();
		User user = UserContext.current().getUser();
		
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		
		List<String> result = new ArrayList<>();
			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.LISTALLTASK))
				result.add(PmTaskPrivilege.LISTALLTASK.getCode());
			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.LISTUSERTASK))
				result.add(PmTaskPrivilege.LISTUSERTASK.getCode());
			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ASSIGNTASK))
				result.add(PmTaskPrivilege.ASSIGNTASK.getCode());
			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.COMPLETETASK))
				result.add(PmTaskPrivilege.COMPLETETASK.getCode());
			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.CLOSETASK))
				result.add(PmTaskPrivilege.CLOSETASK.getCode());
			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.REVISITTASK))
				result.add(PmTaskPrivilege.REVISITTASK.getCode());
		dto.setPrivileges(result);
		return dto;
	}
	
	private void checkPrivilege(Long organizationId, String ownerType, Long ownerId, Long userId, Long privilegeId) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		if(!resolver.checkUserPrivilege(userId, ownerType, ownerId, organizationId, privilegeId)){
    		returnNoPrivileged(Collections.singletonList(privilegeId), userId);
		}
	}
	
	private void setTaskStatus(Long organizationId, String ownerType, Long ownerId, PmTask task, String content, 
			List<AttachmentDescriptor> attachments, Byte status) {
		
		checkOwnerIdAndOwnerType(ownerType, ownerId);
		checkOrganizationId(organizationId);
		if(StringUtils.isBlank(content)) {
        	LOGGER.error("Content cannot be null.");
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CONTENT_NULL,
    				"Content cannot be null.");
        }
		User user = UserContext.current().getUser();
		long time = System.currentTimeMillis();
		Timestamp now = new Timestamp(time);
		
		if(status.equals(PmTaskStatus.PROCESSED.getCode())){
	    	checkPrivilege(organizationId, EntityType.COMMUNITY.getCode(), ownerId, user.getId(), PrivilegeConstants.COMPLETETASK);
			task.setProcessedTime(now);
		}else if(status.equals(PmTaskStatus.CLOSED.getCode())){
	    	checkPrivilege(organizationId, EntityType.COMMUNITY.getCode(), ownerId, user.getId(), PrivilegeConstants.CLOSETASK);
			task.setClosedTime(now);
		}
		
		task.setStatus(status);
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
		
		pmTaskCommonService.addAttachments(attachments, user.getId(), pmTaskLog.getId(), PmTaskAttachmentType.TASKLOG.getCode());
		
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		Map<String, Object> map = new HashMap<>();
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
			pmTaskCommonService.sendMessageToUser(task.getCreatorUid(), text);
			
			code = PmTaskNotificationTemplateCode.NOTIFY_TO_ASSIGNER;
			map.remove("day");
			map.remove("hour");
			User creator = userProvider.findUserById(task.getCreatorUid());
			UserIdentifier creatorIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(creator.getId(), IdentifierType.MOBILE.getCode());
			map.put("creatorName", creator.getNickName());
			map.put("creatorPhone", creatorIdentifier.getIdentifierToken());
			text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			pmTaskCommonService.sendMessageToUser(user.getId(), text);
		}else if(status.equals(PmTaskStatus.CLOSED.getCode())){
			int code = PmTaskNotificationTemplateCode.CLOSED_TASK_LOG;
			String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			pmTaskCommonService.sendMessageToUser(task.getCreatorUid(), text);
		}
		
		//elasticsearch更新
		updateElasticsearch(task);   
	}
	
	@Override
	public void completeTask(CompleteTaskCommand cmd) {
		checkId(cmd.getId());
		
		dbProvider.execute((TransactionStatus transactionStatus) -> {
		
			PmTask task = checkPmTask(cmd.getId());
			if(task.getStatus() >= PmTaskStatus.PROCESSED.getCode() ){
				LOGGER.error("Task cannot be completed. cmd={}", cmd);
	    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_TASK_PROCCESING,
	    				"Task cannot be completed.");
			}
			
			setTaskStatus(cmd.getOrganizationId(), cmd.getOwnerType(), cmd.getOwnerId(), task, cmd.getContent(), 
					cmd.getAttachments(), PmTaskStatus.PROCESSED.getCode());
			
			return null;
		});
		
	}

	@Override
	public void revisit(RevisitCommand cmd) {
		checkId(cmd.getId());
		
		dbProvider.execute((TransactionStatus transactionStatus) -> {
			
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
			updateElasticsearch(task);
			
			return null;
		});
		
	}
	
	private void updateElasticsearch(PmTask task) {
		try{
			pmTaskSearch.deleteById(task.getId());
			pmTaskSearch.feedDoc(task);
		}catch(Exception e){
			//事务无法保证elasticsearch同步
			pmTaskSearch.deleteById(task.getId());

			LOGGER.error("Update pmTask elasticsearch error, task={}", task);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Update pmTask elasticsearch error.");
		}
	}
	
	@Override
	public void closeTask(CloseTaskCommand cmd) {
		checkId(cmd.getId());

		dbProvider.execute((TransactionStatus transactionStatus) -> {

			PmTask task = checkPmTask(cmd.getId());
			if (task.getStatus() >= PmTaskStatus.PROCESSED.getCode()) {
				LOGGER.error("Task cannot be closed. cmd={}", cmd);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_TASK_PROCCESING,
						"Task cannot be closed.");
			}

			setTaskStatus(cmd.getOrganizationId(), cmd.getOwnerType(), cmd.getOwnerId(), task, cmd.getContent(), null,
					PmTaskStatus.CLOSED.getCode());

			return null;
		});
	}
	
	@Override
	public void cancelTask(CancelTaskCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		handler.cancelTask(cmd);
	}
	
	@Override
	public void assignTask(AssignTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());
		checkOrganizationId(cmd.getOrganizationId());
		
		User user = UserContext.current().getUser();
		if(null == cmd.getTargetId()){
			LOGGER.error("TargetId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"TargetId cannot be null.");
		}
		User targetUser = userProvider.findUserById(cmd.getTargetId());
		if(null == targetUser){
			LOGGER.error("TargetUser not found, cmd={}", cmd);
    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_USER_NOT_EXIST,
    				"TargetUser not found");
		}
		
    	checkPrivilege(cmd.getOrganizationId(), EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), user.getId(), PrivilegeConstants.ASSIGNTASK);
    	
		dbProvider.execute((TransactionStatus transactionStatus) -> {

			PmTask task = checkPmTask(cmd.getId());
			if(task.getStatus() >= PmTaskStatus.PROCESSED.getCode() ){
				LOGGER.error("Task cannot be assigned. cmd={}", cmd);
	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
	    				"Task cannot be assigned.");
			}
			long time = System.currentTimeMillis();
			Timestamp now = new Timestamp(time);
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
			Map<String, Object> map = new HashMap<>();
		    map.put("operatorName", user.getNickName());
		    map.put("operatorPhone", userIdentifier.getIdentifierToken());
		    
			UserIdentifier targetIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(targetUser.getId(), IdentifierType.MOBILE.getCode());
			map.put("targetName", targetUser.getNickName());
		    map.put("targetPhone", targetIdentifier.getIdentifierToken());
		    
		    String scope = PmTaskNotificationTemplateCode.SCOPE;
		    String locale = PmTaskNotificationTemplateCode.LOCALE;
	    	
		    int code = PmTaskNotificationTemplateCode.PROCESSING_TASK_LOG;
			String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

			pmTaskCommonService.sendMessageToUser(task.getCreatorUid(), text);
			
			Category category = categoryProvider.findCategoryById(task.getTaskCategoryId());
	        
	        String categoryName = category.getName();

			List<Tuple<String, Object>> variables = smsProvider.toTupleList("operatorName", user.getNickName());
			smsProvider.addToTupleList(variables, "operatorPhone", userIdentifier.getIdentifierToken());
			smsProvider.addToTupleList(variables, "categoryName", categoryName);
			smsProvider.sendSms(user.getNamespaceId(), targetIdentifier.getIdentifierToken(), SmsTemplateCode.SCOPE, SmsTemplateCode.PM_TASK_ASSIGN_CODE, user.getLocale(), variables);
			// 给任务发起者 发短信
			String phoneNumber = null;
			if(null == task.getOrganizationId() || task.getOrganizationId() ==0 ){
				User creator = userProvider.findUserById(task.getCreatorUid());
    			UserIdentifier creatorIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(creator.getId(), IdentifierType.MOBILE.getCode());
    			phoneNumber = creatorIdentifier.getIdentifierToken();
			}else{
    			phoneNumber = task.getRequestorPhone();
			}
		    	Date d = new Date(time);
		    	SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
		    	SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
				List<Tuple<String, Object>> variables2 = smsProvider.toTupleList("day", sdf1.format(d));
				smsProvider.addToTupleList(variables2, "hour", sdf2.format(d));
				smsProvider.addToTupleList(variables2, "operatorName", targetUser.getNickName());
				smsProvider.addToTupleList(variables2, "operatorPhone", targetIdentifier.getIdentifierToken());
				smsProvider.sendSms(user.getNamespaceId(), phoneNumber, SmsTemplateCode.SCOPE, SmsTemplateCode.PM_TASK_ASSIGN_TO_CREATOR_CODE, user.getLocale(), variables2);
			
			//elasticsearch更新
			updateElasticsearch(task);

			return null;
		});
	}
	
	private void returnNoPrivileged(List<Long> privileges, Long userId){
    	LOGGER.error("non-privileged, privileges={}, userId={}", privileges, userId);
		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
				"non-privileged.");
    }
	
	@Override
	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {

		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		return handler.getTaskDetail(cmd);
	}

	private void checkBlacklist(String ownerType, Long ownerId){
		ownerType = org.springframework.util.StringUtils.isEmpty(ownerType) ? "" : ownerType;
		ownerId = null == ownerId ? 0L : ownerId;
		Long userId = UserContext.current().getUser().getId();
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserBlacklistAuthority(userId, ownerType, ownerId, PrivilegeConstants.BLACKLIST_PROPERTY_POST);
	}

	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd) {
		//黑名单权限校验 by sfyan20161213
		checkBlacklist(null, null);

		cmd.setSourceType(PmTaskSourceType.APP.getCode());

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		//Todo:为科兴与一碑对接
		if(namespaceId == 999983 && null != cmd.getTaskCategoryId() && 
				cmd.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			handle = PmTaskHandle.EBEI;
		}
		
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);

		if (null == cmd.getOrganizationId()) {
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
			return handler.createTask(cmd, user.getId(), user.getNickName(), userIdentifier.getIdentifierToken());
		}else {
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
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(user.getNamespaceId(), requestorPhone);
			Long requestorUid = null;
			if (null != userIdentifier) {
				requestorUid = userIdentifier.getOwnerUid();
			}
			return handler.createTask(cmd, requestorUid, requestorName, requestorPhone);
		}
	}
	
	@Override
	public PmTaskDTO createTaskByOrg(CreateTaskCommand cmd) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		User user = UserContext.current().getUser();
		if(!resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE)) {
			LOGGER.error("Not privilege", cmd);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_TASK_PRIVILEGE,
					"Not privilege");
		}
//		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE);
		//黑名单权限校验 by sfyan20161213
		checkBlacklist(null, null);
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
		checkOrganizationId(cmd.getOrganizationId());
		
		cmd.setAddressType(PmTaskAddressType.FAMILY.getCode());

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);

		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		return handler.createTask(cmd, null, requestorName, requestorPhone);
	}
	
	@Override
	public void deleteTaskCategory(DeleteTaskCategoryCommand cmd) {
		Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
		if(cmd.getParentId() == null || defaultId.equals(cmd.getParentId())) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_SERVICE_CATEGORY_DELETE);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_DETAIL_CATEGORY_DELETE);
		}
		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}		Long id = cmd.getId();
		checkId(id);
		
		Category category = categoryProvider.findCategoryById(id);
		if(category == null) {
			LOGGER.error("PmTask category not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CATEGORY_NOT_EXIST,
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
		if(cmd.getParentId() == null) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_SERVICE_CATEGORY_CREATE);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_DETAIL_CATEGORY_CREATE);
		}

		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		Long parentId = cmd.getParentId();
		String path;
		Category category;
		if(null == parentId){
			
			Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
			Category ancestor = categoryProvider.findCategoryById(defaultId);
			parentId = ancestor.getId();
			path = ancestor.getPath() + CATEGORY_SEPARATOR + cmd.getName();
			
		}else{
			category = categoryProvider.findCategoryById(parentId);
			if(category == null) {
				LOGGER.error("PmTask parent category not found, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_SERVICE_CATEGORY_NULL,
						"PmTask parent category not found");
			}
			path = category.getPath() + CATEGORY_SEPARATOR + cmd.getName();
			
		}
		
		category = categoryProvider.findCategoryByNamespaceAndName(parentId, namespaceId, cmd.getName());
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

		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		//为科兴与一碑对接
		if(namespaceId == 999983 && null != cmd.getTaskCategoryId() && 
				cmd.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			handle = PmTaskHandle.EBEI;
		}
		
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		return handler.listTaskCategories(cmd);
		
	}

	@Override
	public List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		return handler.listAllTaskCategories(cmd);
	}
	
	@Override
	public void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp, HttpServletRequest req) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		//Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		if(null == cmd.getPageSize())
			cmd.setPageSize(100000);
		List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getStatus(), cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getTaskCategoryId(), 
				cmd.getStartDate(), cmd.getEndDate(), cmd.getAddressId(), cmd.getBuildingName(), cmd.getPageAnchor(), cmd.getPageSize());
		
		
		Workbook wb = null;
		InputStream in;
		LOGGER.debug("!!!!!!!!!!!!!!!"+this.getClass().getResource("").getPath());
		LOGGER.debug("~~~~~~~~~~~~~~"+this.getClass().getResource("/").getPath());

		LOGGER.debug("@@@@@@@@@@@@@@@@@@@@@@"+this.getClass().getResource("/").getPath());
		in = this.getClass().getResourceAsStream("/excels/pmtask.xlsx");
		
		try {
			wb = new XSSFWorkbook(copyInputStream(in));
		} catch (IOException e) {
			LOGGER.error("Copy inputStream error.");
		}

		Sheet sheet = wb.getSheetAt(0);
		if (null != sheet) {
			Row defaultRow = sheet.getRow(4);
			Cell cell = defaultRow.getCell(1);
			CellStyle style = cell.getCellStyle();
			int size = list.size();
			for(int i=0;i<size;i++){
				Row tempRow = sheet.createRow(i + 4);
				PmTaskDTO task = list.get(i);
				Category category = null;
				if(UserContext.getCurrentNamespaceId() == 999983 && null != cmd.getTaskCategoryId() &&
						cmd.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
					category = createEbeiCategory();
				} else {
					category = checkCategory(task.getTaskCategoryId());
				}

				Cell cell1 = tempRow.createCell(1);
				cell1.setCellStyle(style);
				cell1.setCellValue(i + 1);
				Cell cell2 = tempRow.createCell(2);
				cell2.setCellStyle(style);
				cell2.setCellValue(datetimeSF.format(task.getCreateTime()));
				Cell cell3 = tempRow.createCell(3);
				cell3.setCellStyle(style);
				cell3.setCellValue(task.getRequestorName());
				Cell cell4 = tempRow.createCell(4);
				cell4.setCellStyle(style);
				PmTask pmTask = pmTaskProvider.findTaskById(task.getId());
				if(pmTask != null) {
					if(PmTaskAddressType.FAMILY.equals(PmTaskAddressType.fromCode(pmTask.getAddressType()))) {
						Address address = addressProvider.findAddressById(pmTask.getAddressId());
						if(null != address)
							cell4.setCellValue(address.getAddress());
					}else {
						Organization organization = organizationProvider.findOrganizationById(pmTask.getAddressOrgId());
						if(null != organization)
							cell4.setCellValue(organization.getName());
					}
				}


				Cell cell5 = tempRow.createCell(5);
				cell5.setCellStyle(style);
				cell5.setCellValue(task.getRequestorPhone());
				Cell cell6 = tempRow.createCell(6);
				cell6.setCellStyle(style);
				cell6.setCellValue(task.getContent());
				List<PmTaskLog> logs = pmTaskProvider.listPmTaskLogs(task.getId(), PmTaskStatus.PROCESSING.getCode());
				int logSize = logs.size();
				Cell cell7 = tempRow.createCell(7);
				cell7.setCellStyle(style);
				if(logSize != 0){
					User user = userProvider.findUserById(logs.get(logSize-1).getTargetId());
					cell7.setCellValue(user.getNickName());
				}
				Cell cell8 = tempRow.createCell(8);
				cell8.setCellStyle(style);
				cell8.setCellValue(category.getName());
				Cell cell9 = tempRow.createCell(9);
				cell9.setCellStyle(style);
				cell9.setCellValue(pmTaskCommonService.convertStatus(task.getStatus()));

			}

			Row tempRow4 = sheet.createRow(size + 4);
			tempRow4.createCell(1).setCellValue("物业服务中心主任");
			tempRow4.createCell(5).setCellValue("日期：" + dateSF.format(new Date()));

			exportExcel(wb, resp);
		}
	}
	
	private InputStream copyInputStream(InputStream source) {
		if(null == source)
			 return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		  
		byte[] buffer = new byte[1024];  
		int len;  
		try {
			while ((len = source.read(buffer)) > -1 ) {  
			    baos.write(buffer, 0, len);  
			}
			baos.flush();  
		} catch (IOException e) {
			LOGGER.error("ExportTasks is fail, cmd={}");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"ExportTasks is fail.");
		}  
		// 打开一个新的输入流  
		return new ByteArrayInputStream(baos.toByteArray());
	}

	@Override
	public SearchTaskStatisticsResponse searchTaskStatistics(SearchTaskStatisticsCommand cmd) {
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		SearchTaskStatisticsResponse response = new SearchTaskStatisticsResponse();
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<PmTaskStatistics> list = pmTaskProvider.searchTaskStatistics(namespaceId, null, cmd.getTaskCategoryId(), cmd.getKeyword(), new Timestamp(cmd.getDateStr()),
				cmd.getPageAnchor(), cmd.getPageSize());
		
		list = mergeTaskOwnerList(list);

		int size = list.size();
		if (size > 0){
    		response.setRequests(list.stream().map(r -> {
    			TaskStatisticsDTO dto = new TaskStatisticsDTO();
    			
    			dto.setNewCount(r.getTotalCount());
    			dto.setOwnerId(r.getOwnerId());
    			Community community = communityProvider.findCommunityById(r.getOwnerId());
    			dto.setOwnerName(community.getName());
    			dto.setUnProcessedCount(r.getUnprocessCount());
    			dto.setCompletePercent(r.getTotalCount() != null && !r.getTotalCount().equals(0) ?(float)r.getProcessedCount()/r.getTotalCount():0);
    			dto.setClosePercent(r.getTotalCount()!=null&&!r.getTotalCount().equals(0)?(float)r.getCloseCount()/r.getTotalCount():0);
    			float avgStar = calculatePerson(r)!=0?(float) (calculateStar(r)) / (calculatePerson(r)):0;
    			dto.setAvgStar((float)Math.round(avgStar * 10)/10);
    			Integer totalCount = pmTaskProvider.countTaskStatistics(r.getOwnerId(), r.getTaskCategoryId(), null);
    			dto.setTotalCount(totalCount);
    			
    			return dto;
    		}).collect(Collectors.toList()));
    		if (size != pageSize) {
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(size-1).getId());
        	}
    	}
		
		return response;
	}
	
	private List<PmTaskStatistics> mergeTaskCategoryList(List<PmTaskStatistics> list) {
		
		Map<Long, PmTaskStatistics> tempMap = new HashMap<>();
		for(PmTaskStatistics p: list){
			Long id = p.getTaskCategoryId();
			PmTaskStatistics pts;
			if(tempMap.containsKey(id)){
				pts = tempMap.get(id);
				calculatePmTask(pts, p);
				continue;
			}
			tempMap.put(id, p);
		}

		List<PmTaskStatistics> result = new ArrayList<>();
		result.addAll(tempMap.values());

		return result;
	}

	private void mergeCategoryList(List<PmTaskStatistics> list) {

		Map<Long, PmTaskStatistics> tempMap = new HashMap<>();
		for(PmTaskStatistics p: list){
			Long id = p.getCategoryId();
			if(null != id && id != 0) {
				PmTaskStatistics pts;

				if(tempMap.containsKey(id)){
					pts = tempMap.get(id);
					calculatePmTask(pts, p);
					continue;
				}
				tempMap.put(id, p);
			}
		}
		list.clear();
		list.addAll(tempMap.values());
	}

	private void calculatePmTask(PmTaskStatistics pts, PmTaskStatistics p) {
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
	}

	private List<PmTaskStatistics> mergeTaskOwnerList(List<PmTaskStatistics> list) {
		
		List<PmTaskStatistics> result = new ArrayList<>();
		Map<Long, List<PmTaskStatistics>> tempMap = new HashMap<>();
		for(PmTaskStatistics p: list){
			Long id = p.getOwnerId();
			if(tempMap.containsKey(id)){
				List<PmTaskStatistics> ptsList = tempMap.get(id);
				List<PmTaskStatistics> temp = new ArrayList<>();
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
		return r.getStar1()+2*r.getStar2()+3*r.getStar3()+4*r.getStar4()+5*r.getStar5();
	}
	private int calculatePerson(PmTaskStatistics r){
		return r.getStar1()+r.getStar2()+r.getStar3()+r.getStar4()+r.getStar5();
	}
	
	@Override
	public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
		if(cmd.getOwnerId() == null) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_ALL_TASK_STATISTICS_LIST);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
		}
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
		
		List<EvaluateScoreDTO> evaluates = new ArrayList<>();
		List<CategoryTaskStatisticsDTO> categoryTaskStatistics = new ArrayList<>();
		
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

		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {

			this.coordinationProvider.getNamedLock(CoordinationLocks.PMTASK_STATISTICS.getCode()).tryEnter(() -> {

				List<Namespace> namespaces = pmTaskProvider.listNamespace();
				long now = System.currentTimeMillis();
				Timestamp startDate = getBeginOfMonth(now);
				Timestamp endDate = getEndOfMonth(now);
				boolean isOperateByAdmin = configProvider.getBooleanValue("pmtask.statistics.create", false);
				if (isOperateByAdmin) {
					startDate = getEndOfMonth(now);
					endDate = null;
				}
				for (Namespace n : namespaces) {
					Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
					Category ancestor = categoryProvider.findCategoryById(defaultId);

					if (null != ancestor) {
						List<Category> categories = categoryProvider.listTaskCategories(n.getId(), ancestor.getId(), null, null, null);
						if (null != categories && !categories.isEmpty()) {
							List<Community> communities = communityProvider.listCommunitiesByNamespaceId(n.getId());
							for (Community community : communities) {
								for (Category taskCategory : categories) {
									createTaskStatistics(community.getId(), taskCategory.getId(), 0L, startDate, endDate, now, n.getId());
									List<Category> tempCategories = categoryProvider.listTaskCategories(n.getId(), taskCategory.getId(), null, null, null);
									for (Category category : tempCategories) {
										createTaskStatistics(community.getId(), taskCategory.getId(), category.getId(), startDate, endDate, now, n.getId());
									}
								}
							}
						}
					}
				}
			});
		}
	}

	@Scheduled(cron="0 5 0 1 * ? ")
	public void createTaskTargetStatistics(){
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {

			this.coordinationProvider.getNamedLock(CoordinationLocks.PMTASK_TARGET_STATISTICS.getCode()).tryEnter(() -> {
				List<Namespace> namespaces = pmTaskProvider.listNamespace();
				long now = System.currentTimeMillis();
				Timestamp startDate = getBeginOfMonth(now);
				Timestamp endDate = getEndOfMonth(now);
				boolean isOperateByAdmin = configProvider.getBooleanValue("pmtask.statistics.create", false);
				if (isOperateByAdmin) {
					startDate = getEndOfMonth(now);
					endDate = null;
				}
				for (Namespace n : namespaces) {
					Long defaultId = configProvider.getLongValue("pmtask.category.ancestor", 0L);
					Category ancestor = categoryProvider.findCategoryById(defaultId);

					if (ancestor != null) {

						List<Category> categories = categoryProvider.listTaskCategories(n.getId(), ancestor.getId(), null, null, null);
						if (null != categories && !categories.isEmpty()) {
							List<Community> communities = communityProvider.listCommunitiesByNamespaceId(n.getId());
							for (Community community : communities) {
								List<PmTaskTarget> targets = pmTaskProvider.listTaskTargets(PmTaskOwnerType.COMMUNITY.getCode(),
										community.getId(), PmTaskOperateType.REPAIR.getCode(), null, null);
								for (PmTaskTarget t : targets) {
									for (Category taskCategory : categories) {
										List<PmTask> tasks = pmTaskProvider.listPmTask4Stat(PmTaskOwnerType.COMMUNITY.getCode(), community.getId(), taskCategory.getId(),
												t.getTargetId(), startDate, endDate);
										double starSum = 0;
										int size = tasks.size();
										for (PmTask task : tasks) {
											starSum += task.getOperatorStar();
										}
										PmTaskTargetStatistic statistic = new PmTaskTargetStatistic();
										statistic.setOwnerId(community.getId());
										statistic.setOwnerType(PmTaskOwnerType.COMMUNITY.getCode());
										statistic.setCreateTime(new Timestamp(now));
										statistic.setDateStr(startDate);
										statistic.setNamespaceId(n.getId());
										statistic.setTargetId(t.getTargetId());
										statistic.setTaskCategoryId(taskCategory.getId());
										statistic.setAvgStar(size != 0 ? new BigDecimal(starSum / size) : new BigDecimal(0));
										pmTaskProvider.createTaskTargetStatistic(statistic);
									}
								}
							}
						}
					}
				}
			});
		}
	}

	private void createTaskStatistics(Long communityId, Long taskCategoryId, Long categoryId, Timestamp startDate,
			Timestamp endDate, Long now, Integer namespaceId) {
		PmTaskStatistics statistics = new PmTaskStatistics();
		Integer totalCount = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, null, startDate, endDate);
		Integer unprocessCount = pmTaskProvider.countTask(communityId, PmTaskStatus.UNPROCESSED.getCode(), taskCategoryId, categoryId, null, startDate, endDate);
		Integer processingCount = pmTaskProvider.countTask(communityId, PmTaskStatus.PROCESSING.getCode(), taskCategoryId, categoryId, null, startDate, endDate);
		Integer processedCount = pmTaskProvider.countTask(communityId, PmTaskStatus.PROCESSED.getCode(), taskCategoryId, categoryId, null, startDate, endDate);
		Integer closeCount = pmTaskProvider.countTask(communityId, PmTaskStatus.CLOSED.getCode(), taskCategoryId, categoryId, null, startDate, endDate);
		
		Integer star1 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, (byte)1, startDate, endDate);
		Integer star2 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, (byte)2, startDate, endDate);
		Integer star3 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, (byte)3, startDate, endDate);
		Integer star4 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, (byte)4, startDate, endDate);
		Integer star5 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, (byte)5, startDate, endDate);
		
		statistics.setTaskCategoryId(taskCategoryId);
		statistics.setCategoryId(categoryId);
		statistics.setCreateTime(new Timestamp(now));
		statistics.setDateStr(startDate);
		statistics.setNamespaceId(namespaceId);
		statistics.setOwnerId(communityId);
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
		List<PmTaskAttachmentDTO> attachmentDTOs = pmTaskCommonService.convertAttachmentDTO(attachments);
		pmTaskLogDTO.setAttachments(attachmentDTOs);
		
		return pmTaskLogDTO;
	}

	private void checkNamespaceId(Integer namespaceId){
		if(namespaceId == null) {
        	LOGGER.error("Invalid namespaceId parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid namespaceId parameter.");
        }
	}
	
	private void checkId(Long id){
		if(null == id) {
        	LOGGER.error("Invalid id parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid id parameter.");
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

	private Category createEbeiCategory() {
		Category category = new Category();
		category.setId(PmTaskHandle.EBEI_TASK_CATEGORY);
		category.setName("物业报修");
		category.setParentId(0L);
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
	
	@Override
	public void exportStatistics(GetStatisticsCommand cmd, HttpServletResponse resp) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		
		Workbook wb = new XSSFWorkbook();
		
		List<PmTaskStatistics> list = pmTaskProvider.searchTaskStatistics(namespaceId, cmd.getOwnerId(), null, null, new Timestamp(cmd.getDateStr())
				,null, null);
		
		Map<Long, List<PmTaskStatistics>> map = convertStatistics(list);
		
		Font font = wb.createFont();   
		font.setFontName("黑体");   
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		
		Sheet sheet = wb.createSheet("task");
		setHeader(sheet);
		
		setValue(sheet, map);

		exportExcel(wb, resp);

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
			for(PmTaskStatistics statistics: temp){
				i++;
				totalCount += statistics.getTotalCount();
				evaluateCount += calculatePerson(statistics);
				totalStar += calculateStar(statistics);
				
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
			List<PmTaskStatistics> tempList;
				
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

		exportExcel(wb, resp);

	}


	@Override
	public ListOperatePersonnelsResponse listOperatePersonnels(ListOperatePersonnelsCommand cmd) {
		
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkOperateType(cmd.getOperateType());
		checkOrganizationId(cmd.getOrganizationId());
		
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		if(null == org) {
        	LOGGER.error("OrganizationId not found.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OrganizationId not found.");
        }
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<PmTaskTarget> targets = pmTaskProvider.listTaskTargets(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOperateType(),
				cmd.getPageAnchor(), cmd.getPageSize());
		
		ListOperatePersonnelsResponse response = new ListOperatePersonnelsResponse();

		List<OrganizationMember> organizationMembers = new ArrayList<>();
		for(PmTaskTarget t: targets) {
			OrganizationMember m = organizationProvider.findOrganizationMemberByOrgIdAndUId(t.getTargetId(), cmd.getOrganizationId());
			if(null != m)
				organizationMembers.add(m);
		}
		int size = targets.size();
		if (size > 0) {
			List<OrganizationMemberDTO> dtos = organizationService.convertOrganizationMemberDTO(organizationMembers, org);
			dtos.forEach(member -> {
				Integer count = pmTaskProvider.countUserProccsingPmTask(cmd.getOwnerType(), cmd.getOwnerId(), member.getTargetId());
				member.setProccesingTaskCount(count);
			});
			response.setMembers(dtos);
    		if(size != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(targets.get(size-1).getId());
        	}
    	}
		
		return response;
	}
	
	private void checkOperateType(Byte operateType) {
		if (null == operateType) {
        	LOGGER.error("Invalid operateType parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid operateType parameter.");
        }
		
	}
	
	private void checkOrganizationId(Long organizationId) {
		if (null == organizationId) {
        	LOGGER.error("Invalid organizationId parameter.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Invalid organizationId parameter.");
        }
	}
	
	@Override
	public void createTaskOperatePerson(CreateTaskOperatePersonCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkOperateType(cmd.getOperateType());
		checkOrganizationId(cmd.getOrganizationId());
		
		List<Long> privilegeIds = new ArrayList<>();
		if(cmd.getOperateType().equals(PmTaskOperateType.EXECUTOR.getCode())) {
			privilegeIds.add(PrivilegeConstants.LISTALLTASK);
			privilegeIds.add(PrivilegeConstants.LISTUSERTASK);
			privilegeIds.add(PrivilegeConstants.ASSIGNTASK);
			privilegeIds.add(PrivilegeConstants.COMPLETETASK);
			privilegeIds.add(PrivilegeConstants.CLOSETASK);
			privilegeIds.add(PrivilegeConstants.REVISITTASK);
		}
		else if(cmd.getOperateType().equals(PmTaskOperateType.REPAIR.getCode())) {
			privilegeIds.add(PrivilegeConstants.LISTUSERTASK);
			privilegeIds.add(PrivilegeConstants.COMPLETETASK);
		}

		List<Long> targetIds = cmd.getTargetIds();
		if(null == targetIds || targetIds.isEmpty()) {
			LOGGER.error("TargetIds cannot be null or empty.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"TargetIds cannot be null or empty.");
		}
		
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		
		dbProvider.execute((TransactionStatus status) -> {
			targetIds.forEach(t -> {
				// 有下面这段代码会导致：如果在业务授权那边指了权限，这些人会在报修模块这边添加不进去，故注释掉 by lqs 20170123
//				if(resolver.checkUserPrivilege(targetIds.get(i), EntityType.COMMUNITY.getCode(),
//						cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.PM_TASK_MODULE)) {
//					continue;
//				}
				PmTaskTarget pmTaskTarget = pmTaskProvider.findTaskTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOperateType(),
						EntityType.USER.getCode(), t);

				if (null == pmTaskTarget) {
					pmTaskTarget = new PmTaskTarget();
					pmTaskTarget.setRoleId(cmd.getOperateType());
					pmTaskTarget.setOwnerId(cmd.getOwnerId());
					pmTaskTarget.setOwnerType(cmd.getOwnerType());
					pmTaskTarget.setStatus(PmTaskTargetStatus.ACTIVE.getCode());
					pmTaskTarget.setTargetType(EntityType.USER.getCode());
					pmTaskTarget.setTargetId(t);
					pmTaskTarget.setCreateTime(new Timestamp(System.currentTimeMillis()));
					pmTaskTarget.setCreatorUid(UserContext.current().getUser().getId());

					pmTaskProvider.createTaskTarget(pmTaskTarget);

					//检查 （超级管理员，模块管理员都不添加物业报修子权限，会覆盖物业报修模块权限） 如果没有物业报修模块权限，才添加物业报修的权限  add by sw 20170120
					if (!resolver.checkUserPrivilege(t, EntityType.COMMUNITY.getCode(), cmd.getOwnerId(),
							cmd.getOrganizationId(), PrivilegeConstants.PM_TASK_MODULE)) {

						rolePrivilegeService.assignmentPrivileges(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(),
								EntityType.USER.getCode(), t, "pmtask", privilegeIds);
					}
				}
			});
			return null;
		});
	}

	@Override
	public void deleteTaskOperatePerson(DeleteTaskOperatePersonCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkOperateType(cmd.getOperateType());
		checkOrganizationId(cmd.getOrganizationId());
		
		dbProvider.execute((TransactionStatus status) -> {
			List<Long> privilegeIds = new ArrayList<>();
			
			PmTaskTarget pmTaskTarget2 = null;
			if (cmd.getOperateType().equals(PmTaskOperateType.EXECUTOR.getCode())) {
				privilegeIds.add(PrivilegeConstants.LISTUSERTASK);
				privilegeIds.add(PrivilegeConstants.COMPLETETASK);
				pmTaskTarget2 = pmTaskProvider.findTaskTarget(cmd.getOwnerType(), cmd.getOwnerId(), PmTaskOperateType.REPAIR.getCode(),
						EntityType.USER.getCode(), cmd.getTargetId());
			}
			else if (cmd.getOperateType().equals(PmTaskOperateType.REPAIR.getCode())) {
				pmTaskTarget2 = pmTaskProvider.findTaskTarget(cmd.getOwnerType(), cmd.getOwnerId(), PmTaskOperateType.EXECUTOR.getCode(),
						EntityType.USER.getCode(), cmd.getTargetId());
				privilegeIds.add(PrivilegeConstants.LISTALLTASK);
				privilegeIds.add(PrivilegeConstants.LISTUSERTASK);
				privilegeIds.add(PrivilegeConstants.ASSIGNTASK);
				privilegeIds.add(PrivilegeConstants.COMPLETETASK);
				privilegeIds.add(PrivilegeConstants.CLOSETASK);
				privilegeIds.add(PrivilegeConstants.REVISITTASK);
			}
			PmTaskTarget pmTaskTarget = pmTaskProvider.findTaskTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOperateType(),
					EntityType.USER.getCode(), cmd.getTargetId());
//			pmTaskTarget.setStatus(PmTaskTargetStatus.INACTIVE.getCode());
			
			pmTaskProvider.deleteTaskTarget(pmTaskTarget);

			//
			rolePrivilegeService.deleteAcls(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), 
					EntityType.USER.getCode(), cmd.getTargetId(), ServiceModuleConstants.PM_TASK_MODULE, null);
			if (null != pmTaskTarget2) {
				rolePrivilegeService.assignmentPrivileges(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), 
						EntityType.USER.getCode(), cmd.getTargetId(), "pmtask", privilegeIds);
			}
			return null;
		});
		
	}

	@Override
	public SearchTaskCategoryStatisticsResponse searchTaskCategoryStatistics(SearchTaskStatisticsCommand cmd) {
		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
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
//		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<PmTaskStatistics> temp = pmTaskProvider.searchTaskStatistics(namespaceId, null, cmd.getTaskCategoryId(), cmd.getKeyword(), new Timestamp(cmd.getDateStr()),
				cmd.getPageAnchor(), cmd.getPageSize());

		return handleTaskCategoryStatistics(temp);
	}
	
	private List<TaskCategoryStatisticsDTO> handleTaskCategoryStatistics(List<PmTaskStatistics> temp) {
		List<TaskCategoryStatisticsDTO> list = new ArrayList<>();
		outer:
		for(PmTaskStatistics pts: temp) {
			if(null != pts.getCategoryId() && pts.getCategoryId() != 0) {
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
//				dto.setOwnerName(community.getName());
				CategoryStatisticsDTO categoryStatisticsDTO = new CategoryStatisticsDTO();
				categoryStatisticsDTO.setCategoryId(pts.getCategoryId());
				categoryStatisticsDTO.setCategoryName(category.getName());
				categoryStatisticsDTO.setTotalCount(pts.getTotalCount());
				categoryStatisticsDTO.setOwnerId(pts.getOwnerId());
				categoryStatisticsDTO.setOwnerName(community.getName());
				
				List<CategoryStatisticsDTO> list2 = new ArrayList<>();
				list2.add(categoryStatisticsDTO);
				dto.setRequests(list2);
				
				list.add(dto);
			}
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
			checkCategory(dto.getTaskCategoryId());
			Community community = communityProvider.findCommunityById(dto.getOwnerId());
			tempRow.createCell(0).setCellValue(community.getName());
			for(int j=0,l2=dto.getRequests().size();j<l2;j++ ) {
				firstRow.createCell(j+1).setCellValue(dto.getRequests().get(j).getCategoryName());
				tempRow.createCell(j+1).setCellValue(dto.getRequests().get(j).getTotalCount());
			}
			
		}

		exportExcel(wb, resp);
		
	}

	@Override
	public TaskCategoryStatisticsDTO getTaskCategoryStatistics(SearchTaskStatisticsCommand cmd) {
		if(cmd.getCommunityId() == null) {
			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_ALL_TASK_STATISTICS_LIST);
		} else {
			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
		}
		TaskCategoryStatisticsDTO dto = new TaskCategoryStatisticsDTO();

		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
//		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

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



	@Override
	public void updateTaskByOrg(UpdateTaskCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);

		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);

		handler.updateTaskByOrg(cmd);
	}

	@Override
	public ListAuthorizationCommunityByUserResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd) {

		if (null != cmd.getCheckPrivilegeFlag() && cmd.getCheckPrivilegeFlag() == PmTaskCheckPrivilegeFlag.CHECKED.getCode()) {
			if(null == cmd.getOrganizationId()) {
				LOGGER.error("Not privilege", cmd);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_TASK_PRIVILEGE,
						"Not privilege");
			}
		}else {
			checkOrganizationId(cmd.getOrganizationId());
		}

		ListAuthorizationCommunityByUserResponse response = new ListAuthorizationCommunityByUserResponse();

		ListUserRelatedProjectByModuleCommand listUserRelatedProjectByModuleCommand = new ListUserRelatedProjectByModuleCommand();
		listUserRelatedProjectByModuleCommand.setOrganizationId(cmd.getOrganizationId());
		listUserRelatedProjectByModuleCommand.setModuleId(FlowConstants.PM_TASK_MODULE);

		List<CommunityDTO> dtos = serviceModuleService.listUserRelatedCommunityByModuleId(listUserRelatedProjectByModuleCommand);

		if (null != cmd.getCheckPrivilegeFlag() && cmd.getCheckPrivilegeFlag() == PmTaskCheckPrivilegeFlag.CHECKED.getCode()) {

			if(0 == dtos.size()) {
				LOGGER.error("Not privilege", cmd);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_TASK_PRIVILEGE,
						"Not privilege");
			}

			SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
			User user = UserContext.current().getUser();
			List<CommunityDTO> result = new ArrayList<>();
			dtos.forEach(r -> {
				if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), r.getId(), cmd.getOrganizationId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE)) {
//				userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), r.getId(), cmd.getOrganizationId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE);
					result.add(r);
				}
			});
			if(0 == result.size()) {
				LOGGER.error("Not privilege", cmd);
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_TASK_PRIVILEGE,
						"Not privilege");
			}
			response.setCommunities(result);

		}else{
			response.setCommunities(dtos);
		}
		return response;
	}

	@Override
	public GetUserRelatedAddressByCommunityResponse getUserRelatedAddressesByCommunity(GetUserRelatedAddressesByCommunityCommand cmd) {

		GetUserRelatedAddressByCommunityResponse response = new GetUserRelatedAddressByCommunityResponse();

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		if (StringUtils.isNotBlank(cmd.getKeyword())) {
			LOGGER.info("findClaimedIdentifierByToken: {}", cmd.getKeyword());
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(user.getNamespaceId(), cmd.getKeyword());
			if (null == userIdentifier) {
				return response;
			}
			user = userProvider.findUserById(userIdentifier.getOwnerUid());
			LOGGER.info("findClaimedIdentifierByToken userid: {}, userIdentifier: {}", user.getId(), userIdentifier);
		}
		response.setUserName(user.getNickName());
		Long userId = user.getId();
		Long communityId = cmd.getOwnerId();

	    NamespaceDetail namespaceDetail = namespaceResourceProvider.findNamespaceDetailByNamespaceId(namespaceId);
	    if(null != namespaceDetail) {
//	    	NamespaceCommunityType type = NamespaceCommunityType.fromCode(namespaceDetail.getResourceType());
//	    	if(type== NamespaceCommunityType.COMMUNITY_COMMERCIAL) {
//	    		OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
//	    		List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(namespaceId, userId, groupType);
//	    		List<OrgAddressDTO> addressDTOs = convertAddress(organizationList, communityId);
//
//	    		response.setOrganizationList(addressDTOs);
//	    	}else if(type== NamespaceCommunityType.COMMUNITY_RESIDENTIAL) {
//				//根据查到的userid查家庭 而不是当前登录用户来查 by xiongying20170524
////	    		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
//				List<FamilyDTO> familyList = familyProvider.getUserFamiliesByUserId(userId);
//	    		List<FamilyDTO> families = new ArrayList<>();
//				if(familyList != null && familyList.size() > 0) {
//					familyList.forEach(f -> {
//						if(GroupMemberStatus.ACTIVE.equals(f.getMembershipStatus())) {
//							if(f.getCommunityId().equals(communityId))
//								families.add(f);
//						}
//
//					});
//				}
//
//	    		response.setFamilyList(families);
//	    	}else {
				//根据查到的userid查家庭 而不是当前登录用户来查 by xiongying20170524
//	    		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
			List<FamilyDTO> familyList = familyProvider.getUserFamiliesByUserId(userId);
			List<FamilyDTO> families = new ArrayList<>();
			if(familyList != null && familyList.size() > 0) {
				familyList.forEach(f -> {
					if(GroupMemberStatus.ACTIVE.equals(GroupMemberStatus.fromCode(f.getMembershipStatus()))) {
						if(f.getCommunityId().equals(communityId))
							families.add(f);
					}

				});
			}

			response.setFamilyList(families);

			OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
			List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(namespaceId, userId, groupType);
			List<OrganizationDTO> organizations = new ArrayList<>();
			if(organizationList != null && organizationList.size() > 0) {
				organizationList.forEach(f -> {
					if(OrganizationMemberStatus.ACTIVE.equals(OrganizationMemberStatus.fromCode(f.getMemberStatus()))) {
						if(f.getCommunityId().equals(communityId))
							organizations.add(f);
					}

				});
			}
			List<OrgAddressDTO> addressDTOs = convertAddress(organizations, communityId);

			response.setOrganizationList(addressDTOs);
//	    	}

	    }

	    List<PmTaskHistoryAddress> addresses = pmTaskProvider.listTaskHistoryAddresses(namespaceId, PmTaskOwnerType.COMMUNITY.getCode(),
				communityId, userId, null, null);

		response.setHistoryAddresses(addresses.stream().map(r -> ConvertHelper.convert(r, PmTaskHistoryAddressDTO.class))
				.collect(Collectors.toList()));
		return response;
	}

	private List<OrgAddressDTO> convertAddress(List<OrganizationDTO> organizationList, Long communityId) {
		List<OrgAddressDTO> addressDTOs = new ArrayList<>();

		organizationList.forEach(o -> {
			if (null != o && null != o.getCommunityId()) {
				if(o.getCommunityId().equals(communityId)) {
					List<OrganizationAddress> organizationAddresses = organizationProvider.findOrganizationAddressByOrganizationId(o.getId());
					List<OrgAddressDTO> addresses = new ArrayList<OrgAddressDTO>();
					organizationAddresses.stream().map( r -> {
						Address address = addressProvider.findAddressById(r.getAddressId());
						OrgAddressDTO dto = ConvertHelper.convert(address, OrgAddressDTO.class);
						if(dto != null) {
							dto.setOrganizationId(o.getId());
							dto.setDisplayName(o.getName());
							dto.setAddressId(address.getId());
							addresses.add(dto);
						}
						return null;
					}).collect(Collectors.toList());

					addressDTOs.addAll(addresses);
				}
			}
		});

		return addressDTOs;
	}

	@Override
	public SearchTaskOperatorStatisticsResponse searchTaskOperatorStatistics(SearchTaskOperatorStatisticsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		SearchTaskOperatorStatisticsResponse response = new SearchTaskOperatorStatisticsResponse();
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<PmTaskTargetStatistic> list = pmTaskProvider.searchTaskTargetStatistics(namespaceId, null, cmd.getTaskCategoryId(), null,
				new Timestamp(cmd.getDateStr()), cmd.getPageAnchor(), cmd.getPageSize());

		list = mergeTaskOperatorList(list);

		int size = list.size();
		if(size > 0){
			List<TaskOperatorStatisticsDTO> results = new ArrayList<>();
			for(PmTaskTargetStatistic pts: list) {
				TaskOperatorStatisticsDTO dto = new TaskOperatorStatisticsDTO();

				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(pts.getTargetId(), cmd.getOrganizationId());
				if(null != member) {
					dto.setOperatorName(member.getContactName());
				}else {

					LOGGER.error("OrganizationMember not found, orgId={}, targetId={}, ownerId={}", cmd.getOrganizationId(), pts.getTargetId(), pts.getOwnerId());
					continue;
				}
	            	
    			dto.setTaskCategoryId(pts.getTaskCategoryId());
    			Category taskCategory = checkCategory(pts.getTaskCategoryId());
    			dto.setTaskCategoryName(taskCategory.getName());
    			dto.setAvgStar(pts.getAvgStar());
    			results.add(dto);
			}
    		response.setRequests(results);
    		if(size != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(size-1).getId());
        	}
    	}
		
		return response;
	}

	private List<PmTaskTargetStatistic> mergeTaskOperatorList(List<PmTaskTargetStatistic> list) {
		
		List<PmTaskTargetStatistic> result = new ArrayList<>();
		Map<Long, List<PmTaskTargetStatistic>> tempMap = new HashMap<>();
		for(PmTaskTargetStatistic p: list){
			Long id = p.getTargetId();
			if(tempMap.containsKey(id)){
				List<PmTaskTargetStatistic> ptsList = tempMap.get(id);
				List<PmTaskTargetStatistic> temp = new ArrayList<>();
				temp.addAll(ptsList);
				temp.add(p);
				tempMap.put(id, temp);
				continue;
			}
			tempMap.put(id, Collections.singletonList(p));
		}
		
		for(List<PmTaskTargetStatistic> l:tempMap.values()){
			l = mergeTaskOperatorCategoryList(l);
			result.addAll(l);
		}
		return result;
	}
	
	private List<PmTaskTargetStatistic> mergeTaskOperatorCategoryList(List<PmTaskTargetStatistic> list) {
		
		Map<Long, PmTaskTargetStatistic> tempMap = new HashMap<>();
		for(PmTaskTargetStatistic p: list){
			Long id = p.getTaskCategoryId();
			PmTaskTargetStatistic pts;
			if(tempMap.containsKey(id)){
				pts = tempMap.get(id);
				if(pts.getAvgStar().intValue() != 0) {
					if(p.getAvgStar().intValue() != 0)
						pts.setAvgStar(pts.getAvgStar().add(p.getAvgStar()).divide(new BigDecimal(2), RoundingMode.HALF_DOWN));
				}else {
					pts.setAvgStar(pts.getAvgStar().add(p.getAvgStar()));
				}
				continue;
			}
			tempMap.put(id, p);
		}
	
		List<PmTaskTargetStatistic> result = new ArrayList<>();
		result.addAll(tempMap.values());
		
		return result;
	}
	
	@Override
	public void exportTaskOperatorStatistics(SearchTaskOperatorStatisticsCommand cmd, HttpServletResponse resp) {
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);
		
		List<TaskOperatorStatisticsDTO> list = searchTaskOperatorStatistics(cmd).getRequests();
		
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
		row.createCell(0).setCellValue("服务类别");
		row.createCell(1).setCellValue("人员名称");
		row.createCell(2).setCellValue("平均得分");
		for(int i=0;i<list.size();i++){
			Row tempRow = sheet.createRow(i + 1);
			TaskOperatorStatisticsDTO pts = list.get(i);
			tempRow.createCell(0).setCellValue(pts.getTaskCategoryName());
            
            tempRow.createCell(1).setCellValue(pts.getOperatorName());
			
			tempRow.createCell(2).setCellValue(pts.getAvgStar().doubleValue());
			
		}

		exportExcel(wb, resp);
	}

	private void exportExcel(Workbook wb, HttpServletResponse resp ) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
			DownloadUtils.download(out, resp);
		} catch (IOException e) {
			LOGGER.error("Export pmTask excel failed.", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Export pmTask excel failed.");
		}try {
			out.close();
			wb.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override   
	public NamespaceHandlerDTO getNamespaceHandler(GetNamespaceHandlerCommand cmd) {
		
		NamespaceHandlerDTO dto = new NamespaceHandlerDTO();
		
		if(null == cmd.getNamespaceId())
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		
		StringBuilder sb = new StringBuilder(HANDLER);
		sb.append(cmd.getNamespaceId());
		
		if(null != cmd.getCategoryId())
			sb.append("-").append(cmd.getCategoryId());
			
		String key = sb.toString();
		
		String handler = configProvider.getValue(key, PmTaskHandle.FLOW);
		
		dto.setHandler(handler);
		
		return dto;
	}

	@Override
	public void synchronizedData(SearchTasksCommand cmd) {

		User user = UserContext.current().getUser();

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Flow flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
				FlowModuleType.NO_MODULE.getCode(), 0L, FlowOwnerType.PMTASK.getCode());
		if(null == flow) {
			LOGGER.error("Enable pmtask flow not found, moduleId={}", FlowConstants.PM_TASK_MODULE);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
					"Enable pmtask flow not found.");
		}

		Long flowId = flow.getFlowMainId();
		long endNode = flow.getEndNode();
			List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getStatus(), cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(),
					cmd.getTaskCategoryId(), cmd.getStartDate(), cmd.getEndDate(), cmd.getAddressId(), cmd.getBuildingName(),
					null, null);

			for(PmTaskDTO t: list) {
				PmTask task = pmTaskProvider.findTaskById(t.getId());

				if (0 != task.getFlowCaseId())
					continue;

				if (PmTaskStatus.INACTIVE.getCode() == task.getStatus()) {
					pmTaskProvider.deleteTask(task);
					pmTaskSearch.deleteById(task.getId());
					continue;
				}


				CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
				createFlowCaseCommand.setApplyUserId(task.getCreatorUid());
				createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
				createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
				createFlowCaseCommand.setReferId(task.getId());
				createFlowCaseCommand.setReferType(EntityType.PM_TASK.getCode());
				//createFlowCaseCommand.setContent("发起人：" + requestorName + "\n" + "联系方式：" + requestorPhone);
				createFlowCaseCommand.setContent(task.getContent());

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

				if (PmTaskStatus.UNPROCESSED.getCode() == task.getStatus() ||
						PmTaskStatus.PROCESSING.getCode() == task.getStatus()) {
					FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
					stepDTO.setFlowCaseId(flowCase.getId());
					stepDTO.setFlowMainId(flowCase.getFlowMainId());
					stepDTO.setFlowVersion(flowCase.getFlowVersion());
					stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
					stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
					stepDTO.setStepCount(flowCase.getStepCount());
					flowService.processAutoStep(stepDTO);
				} else if (PmTaskStatus.PROCESSED.getCode() == task.getStatus() ||
						PmTaskStatus.CLOSED.getCode() == task.getStatus() ||
						PmTaskStatus.REVISITED.getCode() == task.getStatus()) {
					while (flowCase.getCurrentNodeId() != endNode) {

						FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
						stepDTO.setFlowCaseId(flowCase.getId());
						stepDTO.setFlowMainId(flowCase.getFlowMainId());
						stepDTO.setFlowVersion(flowCase.getFlowVersion());
						stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
						stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
						stepDTO.setStepCount(flowCase.getStepCount());
						flowService.processAutoStep(stepDTO);

						flowCase = flowCaseProvider.getFlowCaseById(flowCase.getId());
					}

				}
				FlowNode flowNode = flowNodeProvider.getFlowNodeById(flowCase.getCurrentNodeId());
				task.setStatus(pmTaskCommonService.convertFlowStatus(flowNode.getParams()));
				task.setFlowCaseId(flowCase.getId());
				pmTaskProvider.updateTask(task);

				pmTaskSearch.deleteById(task.getId());
				pmTaskSearch.feedDoc(task);

			}

	}

	@Override
	public void deleteTaskHistoryAddress(DeleteTaskHistoryAddressCommand cmd) {
		PmTaskHistoryAddress pmTaskHistoryAddress = pmTaskProvider.findTaskHistoryAddressById(cmd.getId());
		if (null == pmTaskHistoryAddress) {
			LOGGER.error("PmTaskHistoryAddress not found, id={}", cmd.getId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"PmTaskHistoryAddress not found.");
		}
		pmTaskHistoryAddress.setStatus(PmTaskHistoryAddressStatus.INACTIVE.getCode());
		pmTaskProvider.updateTaskHistoryAddress(pmTaskHistoryAddress);
	}

	@Override
	public PmTaskHistoryAddressDTO createTaskHistoryAddress(CreateTaskHistoryAddressCommand cmd) {

		if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
			LOGGER.error("Invalid parameter, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Invalid parameter.");
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		//addressId 为空，保存地址
		PmTaskHistoryAddressDTO dto = dbProvider.execute((TransactionStatus transactionStatus) -> {
			PmTaskHistoryAddress pmTaskHistoryAddress = new PmTaskHistoryAddress();
			pmTaskHistoryAddress.setNamespaceId(namespaceId);
			pmTaskHistoryAddress.setOwnerId(cmd.getOwnerId());
			pmTaskHistoryAddress.setOwnerType(cmd.getOwnerType());
			pmTaskHistoryAddress.setBuildingName(cmd.getBuildingName());
			pmTaskHistoryAddress.setAddress(cmd.getAddress());
			pmTaskHistoryAddress.setCreateTime(new Timestamp(System.currentTimeMillis()));
			pmTaskHistoryAddress.setCreatorUid(UserContext.current().getUser().getId());
			pmTaskHistoryAddress.setStatus(PmTaskHistoryAddressStatus.ACTIVE.getCode());
			pmTaskProvider.createTaskHistoryAddress(pmTaskHistoryAddress);

			return ConvertHelper.convert(pmTaskHistoryAddress, PmTaskHistoryAddressDTO.class);
		});

		return dto;
	}

	@Override
	public void notifyTaskResult(NotifyTaskResultCommand cmd) {
		//根据app key来验证是否有接口权限以及是提供给哪个第三方的 eh_apps里面拿到name
		App app = appProvider.findAppByKey(cmd.getAppKey());
		if(app == null) {
			LOGGER.error("app key is not exist.");
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_APP_KEY,
					"app key is not exist.");
		}
//		verifySignature();
		Long taskId = Long.valueOf(cmd.getTaskNum());
		PmTask task = pmTaskProvider.findTaskById(taskId);
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(task.getFlowCaseId());

		FlowAutoStepDTO stepDTO = ConvertHelper.convert(flowCase, FlowAutoStepDTO.class);
		stepDTO.setFlowCaseId(flowCase.getId());
		stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
		stepDTO.setAutoStepType(FlowStepType.END_STEP.getCode());
		dbProvider.execute((TransactionStatus status) -> {
			LOGGER.info("stepDTO: {}", stepDTO);
			flowService.processAutoStep(stepDTO);

			task.setRemarkSource(TaskRemarkSource.fromCode(app.getName()).getCode());
			task.setRemark(cmd.getRemark());
			task.setStatus(PmTaskFlowStatus.COMPLETED.getCode());
			pmTaskProvider.updateTask(task);
			pmTaskSearch.feedDoc(task);
			return null;
		});
	}

	public static String computeSignature(Map<String, String> params, String secretKey) {
		assert (params != null);
		assert (secretKey != null);

		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			byte[] rawKey = Base64.getDecoder().decode(secretKey);

			SecretKeySpec keySpec = new SecretKeySpec(rawKey, "HmacSHA1");
			mac.init(keySpec);

			List<String> keyList = new ArrayList<String>();
			CollectionUtils.addAll(keyList, params.keySet().iterator());
			Collections.sort(keyList);

			for (String key : keyList) {
				mac.update(key.getBytes("UTF-8"));
				String val = params.get(key);
				if (val != null && !val.isEmpty())
					mac.update(val.getBytes("UTF-8"));
			}

			byte[] encryptedBytes = mac.doFinal();
			String signature = Base64.getEncoder().encodeToString(encryptedBytes);

			return signature;
		} catch (InvalidKeyException e) {
			throw new InvalidParameterException("Invalid secretKey for signing");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("NoSuchAlgorithmException for HmacSHA1", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException for UTF-8", e);
		}
	}

	public static boolean verifySignature(Map<String, String> params, String secretKey, String signatureToVerify) {
		String signature = computeSignature(params, secretKey);

		if (signature.equals(signatureToVerify))
			return true;

		return false;
	}

	@Override
	public void exportTasksCard(ExportTasksCardCommand cmd, HttpServletResponse response) {
		List<Long> taskIds = new ArrayList<>();
		if(!StringUtils.isEmpty(cmd.getIds())) {
			String[] ids = cmd.getIds().split(",");
			for(String id : ids) {
				taskIds.add(Long.valueOf(id));
			}
		}
		LOGGER.info("taskIds: {}", taskIds);
		List<PmTask> tasks = pmTaskProvider.listTasksById(taskIds);
		List<PmTaskDTO> dtos = tasks.stream().map(task -> {
			PmTaskDTO dto = ConvertHelper.convert(task, PmTaskDTO.class);
			return dto;
		}).collect(Collectors.toList());

		String filePath = cmd.getFilePath();
		if(StringUtils.isEmpty(cmd.getFilePath())) {
			URL rootPath = PmTaskServiceImpl.class.getResource("/");
			filePath = rootPath.getPath() + this.downloadDir ;
			File file = new File(filePath);
			if(!file.exists())
				file.mkdirs();

		}

		DocUtil docUtil=new DocUtil();
		List<String> files = new ArrayList<>();

		for(int i = 0; i <dtos.size(); i++ ) {
			PmTaskDTO dto = dtos.get(i);
			Map<String, Object> dataMap=createTaskCardDoc(dto);

			String savePath = filePath + dto.getId() + ".doc";

			docUtil.createDoc(dataMap, "zjgk", savePath);
			if(StringUtils.isEmpty(cmd.getFilePath())) {
				files.add(savePath);
			}
		}
		if(StringUtils.isEmpty(cmd.getFilePath())) {
			if(files.size() > 1) {
				String zipPath = filePath + System.currentTimeMillis() + "TaskCard.zip";
				LOGGER.info("filePath:{}, zipPath:{}",filePath,zipPath);
				DownloadUtils.writeZip(files, zipPath);
				download(zipPath,response);
			} else if(files.size() == 1) {
				download(files.get(0),response);
			}

		}
	}

	private Map<String, Object> createTaskCardDoc(PmTaskDTO dto) {
		Map<String, Object> dataMap=new HashMap<String, Object>();
		Namespace namespace = namespaceProvider.findNamespaceById(dto.getNamespaceId());
		if(namespace != null) {
			dataMap.put("namespaceName", namespace.getName());
		} else {
			dataMap.put("namespaceName", "");
		}

		dataMap.put("No", dto.getId());
		dataMap.put("name", dto.getRequestorName());
		dataMap.put("address", dto.getAddress());
		dataMap.put("requestorName", dto.getRequestorName());
		dataMap.put("requestorPhone", dto.getRequestorPhone());
		if(dto.getProcessingTime() != null) {
			dataMap.put("processingTime", datetimeSF.format(dto.getProcessingTime()));
		} else {
			dataMap.put("processingTime", "");
		}

		if(dto.getReserveTime() != null) {
			dataMap.put("reserveTime", datetimeSF.format(dto.getReserveTime()));
		} else {
			dataMap.put("reserveTime", "");
		}


		Community community = communityProvider.findCommunityById(dto.getOwnerId());
		Organization org = organizationProvider.findOrganizationByCommunityIdAndOrgType(dto.getOwnerId(), OrganizationType.PM.getCode());
		if(community != null) {
			dataMap.put("communityName", community.getName());
		} else {
			dataMap.put("communityName", "");
		}

		if(org != null) {
			dataMap.put("organizationName", org.getName());
		} else {
			dataMap.put("organizationName", "");
		}

		Category category = categoryProvider.findCategoryById(dto.getCategoryId());

		if(category != null) {
			dataMap.put("categoryName",category.getName());
		} else {
			dataMap.put("categoryName","");
		}

		dataMap.put("content",dto.getContent());

		return dataMap;
	}

	public HttpServletResponse download(String path, HttpServletResponse response) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			if ( !file.isFile() ) {
				LOGGER.info("filename:{} is not a file", path);
			}
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
			String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

			// 读取完成删除文件
			if (file.isFile() && file.exists()) {
				file.delete();
			}

		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE,
					PmTaskErrorCode.ERROR_DOWNLOAD,
					ex.getLocalizedMessage());

		}
		return response;
	}
}
