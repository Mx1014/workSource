package com.everhomes.pmtask;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.address.AddressService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.building.BuildingProvider;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.flow.*;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceDetail;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.order.PayProvider;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.order.*;
import com.everhomes.pay.user.ListBusinessUsersCommand;
import com.everhomes.paySDK.api.PayService;
import com.everhomes.paySDK.pojo.PayOrderDTO;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.pmtask.ebei.EbeiBuildingType;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommand;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommandResponse;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.organization.*;

import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormSubformItemValue;
import com.everhomes.rest.general_approval.PostApprovalFormSubformValue;
import com.everhomes.rest.gorder.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.gorder.controller.GetPurchaseOrderRestResponse;
import com.everhomes.rest.gorder.order.*;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.order.*;
import com.everhomes.rest.order.OrderPaymentStatus;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.*;
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
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PmTaskServiceImpl implements PmTaskService {
	final String downloadDir ="\\download\\";

	private static final String CATEGORY_SEPARATOR = "/";

	public static final String HANDLER = "pmtask.handler-";

	public static Integer flag = 0;
	
    private SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");

	public final long EXPIRE_TIME_15_MIN_IN_SEC = 15 * 60L;
	
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
	private PmTaskSearch pmTaskSearch;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
    private DbProvider dbProvider;
	@Autowired
    private CoordinationProvider coordinationProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private AddressProvider addressProvider;
	@Autowired
	private NamespaceResourceProvider namespaceResourceProvider;
	@Autowired
	private FlowService flowService;
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
	@Autowired
	private CommunityService communityService;
	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private FlowEvaluateProvider flowEvaluateProvider;
	@Autowired
	private FlowEvaluateItemProvider flowEvaluateItemProvider;
	@Autowired
	private PayService payService;
	@Autowired
	private ContentServerService contentServerService;
	@Autowired
	private GeneralFormValProvider generalFormValProvider;
	@Autowired
	private FlowEventLogProvider flowEventLogProvider;
	@Autowired
	protected GeneralOrderService orderService;


	@Value("${server.contextPath:}")
	private String CONTEXT_PATH;

	@Override
	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
//		查询用户全部项目权限
		ListUserRelatedProjectByModuleCommand cmd1 = new ListUserRelatedProjectByModuleCommand();
		cmd1.setUserId(UserContext.currentUserId());
		cmd1.setAppId(cmd.getAppId());
		cmd1.setModuleId(20100L);
		List<ProjectDTO> projects = serviceModuleService.listUserRelatedProjectByModuleId(cmd1);

		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			if(-1L == cmd.getCurrentProjectId()){
				projects.forEach(r -> {
					userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2010020140L, cmd.getAppId(), null,r.getProjectId());//任务列表权限
				});
			} else {
				userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2010020140L, cmd.getAppId(), null,cmd.getCurrentProjectId());//任务列表权限
			}
		}
		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}

		//检查权限细化
//		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_LIST);


		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		
		//TODO:为科兴与一碑对接
		if(namespaceId == 999983 && null != cmd.getTaskCategoryId() && 
				cmd.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			handle = PmTaskHandle.EBEI;
		}

		//检查多入口应用权限
		if (!handle.equals(PmTaskHandle.EBEI)) {
			if(!checkAppPrivilege(namespaceId, cmd.getTaskCategoryId(), cmd.getCurrentOrgId(), EntityType.COMMUNITY.getCode(),
					cmd.getOwnerId(), PrivilegeConstants.PMTASK_LIST)){
				LOGGER.error("Permission is prohibited, namespaceId={}, taskCategoryId={}, orgId={}, ownerType={}, ownerId={}," +
								" privilege={}", namespaceId, cmd.getTaskCategoryId(), cmd.getCurrentOrgId(), EntityType.COMMUNITY.getCode(),
						cmd.getOwnerId(), PrivilegeConstants.PMTASK_LIST);
				throw  RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
						"check app privilege error");
			}
		}

		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		
		return handler.searchTasks(cmd);
	}

	@Override
	public SearchTasksResponse searchTasksWithoutAuth(SearchTasksCommand cmd) {

		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}

		//检查权限细化
//		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_LIST);


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

//		Integer namespaceId = cmd.getNamespaceId();
//		if (null == namespaceId) {
//			namespaceId = UserContext.getCurrentNamespaceId();
//		}
//		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);

		YueKongJianPmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.YUE_KONG_JIAN);

		return handler.listUserTasks(cmd);
	}

//	@Override
//	public void evaluateTask(EvaluateTaskCommand cmd) {
//
//		Integer namespaceId = cmd.getNamespaceId();
//		if (null == namespaceId) {
//			namespaceId = UserContext.getCurrentNamespaceId();
//		}
//		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
//
//		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
//
//		handler.evaluateTask(cmd);
//
//	}
	
//	@Override
//	public GetPrivilegesDTO getPrivileges(GetPrivilegesCommand cmd){
//		checkOrganizationId(cmd.getOrganizationId());
//
//		GetPrivilegesDTO dto = new GetPrivilegesDTO();
//		User user = UserContext.current().getUser();
//
//		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//
//		List<String> result = new ArrayList<>();
//			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.LISTALLTASK))
//				result.add(PmTaskPrivilege.LISTALLTASK.getCode());
//			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.LISTUSERTASK))
//				result.add(PmTaskPrivilege.LISTUSERTASK.getCode());
//			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ASSIGNTASK))
//				result.add(PmTaskPrivilege.ASSIGNTASK.getCode());
//			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.COMPLETETASK))
//				result.add(PmTaskPrivilege.COMPLETETASK.getCode());
//			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.CLOSETASK))
//				result.add(PmTaskPrivilege.CLOSETASK.getCode());
//			if(resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.REVISITTASK))
//				result.add(PmTaskPrivilege.REVISITTASK.getCode());
//		dto.setPrivileges(result);
//		return dto;
//	}
	
	private void checkPrivilege(Long organizationId, String ownerType, Long ownerId, Long userId, Long privilegeId) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		if(!resolver.checkUserPrivilege(userId, ownerType, ownerId, organizationId, privilegeId)){
    		returnNoPrivileged(Collections.singletonList(privilegeId), userId);
		}
	}
	
//	private void setTaskStatus(Long organizationId, String ownerType, Long ownerId, PmTask task, String content,
//			List<AttachmentDescriptor> attachments, Byte status) {
//
//		checkOwnerIdAndOwnerType(ownerType, ownerId);
//		checkOrganizationId(organizationId);
//		if(StringUtils.isBlank(content)) {
//        	LOGGER.error("Content cannot be null.");
//    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CONTENT_NULL,
//    				"Content cannot be null.");
//        }
//		User user = UserContext.current().getUser();
//		long time = System.currentTimeMillis();
//		Timestamp now = new Timestamp(time);
//
//		if(status.equals(PmTaskStatus.PROCESSED.getCode())){
//	    	checkPrivilege(organizationId, EntityType.COMMUNITY.getCode(), ownerId, user.getId(), PrivilegeConstants.COMPLETETASK);
//			task.setProcessedTime(now);
//		}else if(status.equals(PmTaskStatus.CLOSED.getCode())){
//	    	checkPrivilege(organizationId, EntityType.COMMUNITY.getCode(), ownerId, user.getId(), PrivilegeConstants.CLOSETASK);
//			task.setClosedTime(now);
//		}
//
//		task.setStatus(status);
//		pmTaskProvider.updateTask(task);
//
//		PmTaskLog pmTaskLog = new PmTaskLog();
//		pmTaskLog.setNamespaceId(task.getNamespaceId());
//		pmTaskLog.setOperatorTime(now);
//		pmTaskLog.setOperatorUid(user.getId());
//		pmTaskLog.setContent(content);
//		pmTaskLog.setOwnerId(task.getOwnerId());
//		pmTaskLog.setOwnerType(task.getOwnerType());
//		pmTaskLog.setStatus(task.getStatus());
//		pmTaskLog.setTaskId(task.getId());
//		pmTaskProvider.createTaskLog(pmTaskLog);
//
//		pmTaskCommonService.addAttachments(attachments, user.getId(), pmTaskLog.getId(), PmTaskAttachmentType.TASKLOG.getCode());
//
//		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
//		Map<String, Object> map = new HashMap<>();
//	    map.put("operatorName", user.getNickName());
//	    map.put("operatorPhone", userIdentifier.getIdentifierToken());
//
//	    String scope = PmTaskNotificationTemplateCode.SCOPE;
//	    String locale = PmTaskNotificationTemplateCode.LOCALE;
//
//		if(status.equals(PmTaskStatus.PROCESSED.getCode())){
//	    	int code = PmTaskNotificationTemplateCode.NOTIFY_TO_CREATOR;
//	    	Date d = new Date(time);
//	    	SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
//	    	SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//	    	map.put("day", sdf1.format(d));
//		    map.put("hour", sdf2.format(d));
//		    String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//			pmTaskCommonService.sendMessageToUser(task.getCreatorUid(), text);
//
//			code = PmTaskNotificationTemplateCode.NOTIFY_TO_ASSIGNER;
//			map.remove("day");
//			map.remove("hour");
//			User creator = userProvider.findUserById(task.getCreatorUid());
//			UserIdentifier creatorIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(creator.getId(), IdentifierType.MOBILE.getCode());
//			map.put("creatorName", creator.getNickName());
//			map.put("creatorPhone", creatorIdentifier.getIdentifierToken());
//			text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//			pmTaskCommonService.sendMessageToUser(user.getId(), text);
//		}else if(status.equals(PmTaskStatus.CLOSED.getCode())){
//			int code = PmTaskNotificationTemplateCode.CLOSED_TASK_LOG;
//			String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//			pmTaskCommonService.sendMessageToUser(task.getCreatorUid(), text);
//		}
//
//		//elasticsearch更新
//		updateElasticsearch(task);
//	}
	
//	@Override
//	public void completeTask(CompleteTaskCommand cmd) {
//		checkId(cmd.getId());
//
//		dbProvider.execute((TransactionStatus transactionStatus) -> {
//
//			PmTask task = checkPmTask(cmd.getId());
//			if(task.getStatus() >= PmTaskStatus.PROCESSED.getCode() ){
//				LOGGER.error("Task cannot be completed. cmd={}", cmd);
//	    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_TASK_PROCCESING,
//	    				"Task cannot be completed.");
//			}
//
//			setTaskStatus(cmd.getOrganizationId(), cmd.getOwnerType(), cmd.getOwnerId(), task, cmd.getContent(),
//					cmd.getAttachments(), PmTaskStatus.PROCESSED.getCode());
//
//			return null;
//		});
//
//	}

//	@Override
//	public void revisit(RevisitCommand cmd) {
//		checkId(cmd.getId());
//
//		dbProvider.execute((TransactionStatus transactionStatus) -> {
//
//			PmTask task = checkPmTask(cmd.getId());
//
//			if(task.getStatus() != PmTaskStatus.PROCESSED.getCode() ){
//				LOGGER.error("Task cannot be completed. cmd={}", cmd);
//	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//	    				"Task cannot be completed.");
//			}
//
//			User user = UserContext.current().getUser();
//
//			long time = System.currentTimeMillis();
//			Timestamp now = new Timestamp(time);
//
//			task.setStatus(PmTaskStatus.REVISITED.getCode());
//			task.setRevisitTime(now);
//			pmTaskProvider.updateTask(task);
//
//			PmTaskLog pmTaskLog = new PmTaskLog();
//			pmTaskLog.setNamespaceId(task.getNamespaceId());
//			pmTaskLog.setOperatorTime(now);
//			pmTaskLog.setOperatorUid(user.getId());
//			pmTaskLog.setContent(cmd.getContent());
//			pmTaskLog.setOwnerId(task.getOwnerId());
//			pmTaskLog.setOwnerType(task.getOwnerType());
//			pmTaskLog.setStatus(task.getStatus());
//			pmTaskLog.setTaskId(task.getId());
//
//			pmTaskProvider.createTaskLog(pmTaskLog);
//
//			//elasticsearch更新
//			updateElasticsearch(task);
//
//			return null;
//		});
//
//	}
	
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
	
//	@Override
//	public void closeTask(CloseTaskCommand cmd) {
//		checkId(cmd.getId());
//
//		dbProvider.execute((TransactionStatus transactionStatus) -> {
//
//			PmTask task = checkPmTask(cmd.getId());
//			if (task.getStatus() >= PmTaskStatus.PROCESSED.getCode()) {
//				LOGGER.error("Task cannot be closed. cmd={}", cmd);
//				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_TASK_PROCCESING,
//						"Task cannot be closed.");
//			}
//
//			setTaskStatus(cmd.getOrganizationId(), cmd.getOwnerType(), cmd.getOwnerId(), task, cmd.getContent(), null,
//					PmTaskStatus.CLOSED.getCode());
//
//			return null;
//		});
//	}
	
//	@Override
//	public void cancelTask(CancelTaskCommand cmd) {
//		Integer namespaceId = cmd.getNamespaceId();
//		if (null == namespaceId) {
//			namespaceId = UserContext.getCurrentNamespaceId();
//		}
//		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
//
//		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
//
//		handler.cancelTask(cmd);
//	}
	
//	@Override
//	public void assignTask(AssignTaskCommand cmd) {
//		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
//		checkId(cmd.getId());
//		checkOrganizationId(cmd.getOrganizationId());
//
//		User user = UserContext.current().getUser();
//		if(null == cmd.getTargetId()){
//			LOGGER.error("TargetId cannot be null.");
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    				"TargetId cannot be null.");
//		}
//		User targetUser = userProvider.findUserById(cmd.getTargetId());
//		if(null == targetUser){
//			LOGGER.error("TargetUser not found, cmd={}", cmd);
//    		throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_USER_NOT_EXIST,
//    				"TargetUser not found");
//		}
//
//    	checkPrivilege(cmd.getOrganizationId(), EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), user.getId(), PrivilegeConstants.ASSIGNTASK);
//
//		dbProvider.execute((TransactionStatus transactionStatus) -> {
//
//			PmTask task = checkPmTask(cmd.getId());
//			if(task.getStatus() >= PmTaskStatus.PROCESSED.getCode() ){
//				LOGGER.error("Task cannot be assigned. cmd={}", cmd);
//	    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//	    				"Task cannot be assigned.");
//			}
//			long time = System.currentTimeMillis();
//			Timestamp now = new Timestamp(time);
//			if(!task.getStatus().equals(PmTaskStatus.PROCESSING.getCode())){
//				task.setStatus(PmTaskStatus.PROCESSING.getCode());
//				task.setProcessingTime(now);
//				pmTaskProvider.updateTask(task);
//			}
//
//			PmTaskLog pmTaskLog = new PmTaskLog();
//			pmTaskLog.setNamespaceId(task.getNamespaceId());
//			pmTaskLog.setOperatorTime(now);
//			pmTaskLog.setOperatorUid(user.getId());
//			pmTaskLog.setOwnerId(task.getOwnerId());
//			pmTaskLog.setOwnerType(task.getOwnerType());
//			pmTaskLog.setStatus(task.getStatus());
//			pmTaskLog.setTargetId(cmd.getTargetId());
//			pmTaskLog.setTargetType(PmTaskTargetType.USER.getCode());
//			pmTaskLog.setTaskId(task.getId());
//			pmTaskProvider.createTaskLog(pmTaskLog);
//
//			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
//			Map<String, Object> map = new HashMap<>();
//		    map.put("operatorName", user.getNickName());
//		    map.put("operatorPhone", userIdentifier.getIdentifierToken());
//
//			UserIdentifier targetIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(targetUser.getId(), IdentifierType.MOBILE.getCode());
//			map.put("targetName", targetUser.getNickName());
//		    map.put("targetPhone", targetIdentifier.getIdentifierToken());
//
//		    String scope = PmTaskNotificationTemplateCode.SCOPE;
//		    String locale = PmTaskNotificationTemplateCode.LOCALE;
//
//		    int code = PmTaskNotificationTemplateCode.PROCESSING_TASK_LOG;
//			String text = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//
//			pmTaskCommonService.sendMessageToUser(task.getCreatorUid(), text);
//
//			Category category = categoryProvider.findCategoryById(task.getTaskCategoryId());
//
//	        String categoryName = category.getName();
//
//			List<Tuple<String, Object>> variables = smsProvider.toTupleList("operatorName", user.getNickName());
//			smsProvider.addToTupleList(variables, "operatorPhone", userIdentifier.getIdentifierToken());
//			smsProvider.addToTupleList(variables, "categoryName", categoryName);
//			smsProvider.sendSms(user.getNamespaceId(), targetIdentifier.getIdentifierToken(), SmsTemplateCode.SCOPE, SmsTemplateCode.PM_TASK_ASSIGN_CODE, user.getLocale(), variables);
//			// 给任务发起者 发短信
//			String phoneNumber = null;
//			if(null == task.getOrganizationId() || task.getOrganizationId() ==0 ){
//				User creator = userProvider.findUserById(task.getCreatorUid());
//    			UserIdentifier creatorIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(creator.getId(), IdentifierType.MOBILE.getCode());
//    			phoneNumber = creatorIdentifier.getIdentifierToken();
//			}else{
//    			phoneNumber = task.getRequestorPhone();
//			}
//		    	Date d = new Date(time);
//		    	SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
//		    	SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//				List<Tuple<String, Object>> variables2 = smsProvider.toTupleList("day", sdf1.format(d));
//				smsProvider.addToTupleList(variables2, "hour", sdf2.format(d));
//				smsProvider.addToTupleList(variables2, "operatorName", targetUser.getNickName());
//				smsProvider.addToTupleList(variables2, "operatorPhone", targetIdentifier.getIdentifierToken());
//				smsProvider.sendSms(user.getNamespaceId(), phoneNumber, SmsTemplateCode.SCOPE, SmsTemplateCode.PM_TASK_ASSIGN_TO_CREATOR_CODE, user.getLocale(), variables2);
//
//			//elasticsearch更新
//			updateElasticsearch(task);
//
//			return null;
//		});
//	}
	
	private void returnNoPrivileged(List<Long> privileges, Long userId){
    	LOGGER.error("non-privileged, privileges={}, userId={}", privileges, userId);
		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
				"non-privileged.");
    }

	@Override
	public ListApartmentByBuildingNameCommandResponse listApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd) {
		cmd.setUserId(1l);
		return addressService.listApartmentsByBuildingName(cmd);
	}

	@Override
	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {

//		Integer namespaceId = cmd.getNamespaceId();
//		if (null == namespaceId) {
//			namespaceId = UserContext.getCurrentNamespaceId();
//		}
//		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);

		YueKongJianPmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.YUE_KONG_JIAN);

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
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

		//黑名单权限校验 by sfyan20161213
		checkBlacklist(null, null);

		cmd.setSourceType(PmTaskSourceType.APP.getCode());

		User user = UserContext.current().getUser();
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
			OrganizationMember member = null;
			if (cmd.getFlowOrganizationId()!=null) {
				member = organizationProvider.findOrganizationMemberByOrgIdAndToken(userIdentifier.getIdentifierToken(), cmd.getFlowOrganizationId());
			}
			//真实姓名
			if (member==null ) {
				return handler.createTask(cmd, user.getId(), user.getNickName(), userIdentifier==null?"":userIdentifier.getIdentifierToken());
			}
			else {
				return handler.createTask(cmd, user.getId(), member.getContactName(), userIdentifier==null?"":userIdentifier.getIdentifierToken());
			}
		}else {

			//检查多入口应用权限
			if (!handle.equals(PmTaskHandle.EBEI)) {
				if (!checkAppPrivilege(namespaceId, cmd.getTaskCategoryId(), cmd.getOrganizationId(), EntityType.COMMUNITY.getCode(),
						cmd.getOwnerId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE)) {
					LOGGER.error("Permission is prohibited, namespaceId={}, taskCategoryId={}, orgId={}, ownerType={}, ownerId={}," +
									" privilege={}", namespaceId, cmd.getTaskCategoryId(), cmd.getOrganizationId(), EntityType.COMMUNITY.getCode(),
							cmd.getOwnerId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE);
					throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
							"check app privilege error");
				}
			}
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
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, requestorPhone);
			Long requestorUid = null;
			if (null != userIdentifier) {
				requestorUid = userIdentifier.getOwnerUid();
			}
			cmd.setEnterpriseId(cmd.getOrganizationId());
			return handler.createTask(cmd, requestorUid, requestorName, requestorPhone);
		}
	}

	@Override
	public ListBuildingCommandResponse listBuildings(ListBuildingCommand cmd) {
		ListBuildingCommandResponse response = communityService.listBuildings(cmd);
		if (null == cmd.getNamespaceId()) {
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		}
		//增加公共区域
		if (response.getNextPageAnchor()==null && cmd.getNamespaceId()==999983) {
			BuildingDTO buildingDTO = new BuildingDTO();
			buildingDTO.setName(EbeiBuildingType.publicArea);
			buildingDTO.setBuildingName(EbeiBuildingType.publicArea);
			buildingDTO.setId(0l);
			response.getBuildings().add(buildingDTO);
		}
		return response;
	}
	
	private boolean checkAppPrivilege(Integer namespaceId, Long taskCategoryId, Long orgId, String ownerType, Long ownerId, Long privilege) {
		return true;
	}

//	private boolean checkAppPrivilege(Integer namespaceId, Long taskCategoryId, Long orgId, String ownerType, Long ownerId, Long privilege) {
//
//		if (null != taskCategoryId ) {
//			//找到根节点, 多入口应用id是根节点id
//			boolean flag = true;
//			while (flag) {
//				Category category = categoryProvider.findCategoryById(taskCategoryId);
//				if (null != category && category.getParentId() != 0L) {
//					taskCategoryId = category.getParentId();
//				}else {
//					flag = false;
//				}
//			}
//
//			if (Arrays.asList(PmTaskAppType.TYPES).contains(taskCategoryId)) {
////				ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
////				listServiceModuleAppsCommand.setNamespaceId(namespaceId);
////				listServiceModuleAppsCommand.setModuleId(FlowConstants.PM_TASK_MODULE);
////				listServiceModuleAppsCommand.setCustomTag(String.valueOf(taskCategoryId));
////				ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
////				Long appId = null;
////				if(null != apps && apps.getServiceModuleApps().size() > 0){
////					appId = apps.getServiceModuleApps().get(0).getId();
////				}
////				if (null != apps) {
////					return userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), orgId,
////							orgId, privilege, appId, null, ownerId);
////				}
//				return userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilege, FlowConstants.PM_TASK_MODULE, null, String.valueOf(taskCategoryId), null, ownerId);
//			}
//		}
//
//		return false;
//	}

	@Override
	public PmTaskDTO createTaskByOrg(CreateTaskCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2010020150L, cmd.getAppId(), null,cmd.getCurrentProjectId());//服务录入权限
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}

//		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//		User user = UserContext.current().getUser();
//		if(!resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), cmd.getOrganizationId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE)) {
//			LOGGER.error("Not privilege", cmd);
//			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_TASK_PRIVILEGE,
//					"Not privilege");
//		}
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
		cmd.setEnterpriseId(cmd.getOrganizationId());
		cmd.setAddressType(PmTaskAddressType.FAMILY.getCode());

		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		//Todo:为科兴与一碑对接
		if(namespaceId == 999983 && null != cmd.getTaskCategoryId() &&
				cmd.getTaskCategoryId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
			handle = PmTaskHandle.EBEI;
		}

		//检查多入口应用权限
		if (!handle.equals(PmTaskHandle.EBEI)) {
			if (!checkAppPrivilege(namespaceId, cmd.getTaskCategoryId(), cmd.getOrganizationId(), EntityType.COMMUNITY.getCode(),
					cmd.getOwnerId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE)) {
				LOGGER.error("Permission is prohibited, namespaceId={}, taskCategoryId={}, orgId={}, ownerType={}, ownerId={}," +
								" privilege={}", namespaceId, cmd.getTaskCategoryId(), cmd.getOrganizationId(), EntityType.COMMUNITY.getCode(),
						cmd.getOwnerId(), PrivilegeConstants.PMTASK_AGENCY_SERVICE);
				throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
						"check app privilege error");
			}
		}

		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, requestorPhone);
		Long requestorUid = null;
		if (null != userIdentifier) {
			requestorUid = userIdentifier.getOwnerUid();
		}
		return handler.createTask(cmd, requestorUid, requestorName, requestorPhone);
	}
	
	@Override
	public void deleteTaskCategory(DeleteTaskCategoryCommand cmd) {
//		if(cmd.getParentId() == null || defaultId.equals(cmd.getParentId())) {
//			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_SERVICE_CATEGORY_DELETE);
//		} else {
//			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_DETAIL_CATEGORY_DELETE);
//		}
		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		Long id = cmd.getId();
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
//		if(cmd.getParentId() == null) {
//			userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), null, null,
//					cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_SERVICE_CATEGORY_CREATE, 3L, null, null);
//			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_SERVICE_CATEGORY_CREATE);
//		} else {
////			userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), null, null,
////					cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_DETAIL_CATEGORY_CREATE, 3L, null, null);
//			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_DETAIL_CATEGORY_CREATE);
//		}

		Integer namespaceId = cmd.getNamespaceId();
		if (null == namespaceId) {
			namespaceId = UserContext.getCurrentNamespaceId();
		}

//		if(null == parentId){
//
//			Category ancestor = categoryProvider.findCategoryById(defaultId);
//			parentId = ancestor.getId();
//			path = ancestor.getPath() + CATEGORY_SEPARATOR + cmd.getName();
//
//		}else{
//			category = categoryProvider.findCategoryById(parentId);
//			if(category == null) {
//				LOGGER.error("PmTask parent category not found, cmd={}", cmd);
//				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_SERVICE_CATEGORY_NULL,
//						"PmTask parent category not found");
//			}
//			path = category.getPath() + CATEGORY_SEPARATOR + cmd.getName();
//
//		}
		Long parentId = cmd.getParentId();

		Category category = categoryProvider.findCategoryById(parentId);
		if(category == null) {
			LOGGER.error("PmTask parent category not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_SERVICE_CATEGORY_NULL,
					"PmTask parent category not found");
		}
		String path = category.getPath() + CATEGORY_SEPARATOR + cmd.getName();

		category = categoryProvider.findCategoryByNamespaceAndName(parentId, namespaceId,cmd.getOwnerType(),cmd.getOwnerId(), cmd.getName());
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
		category.setOwnerId(cmd.getOwnerId());
		category.setOwnerType(cmd.getOwnerType());
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
		if (namespaceId == 999983 &&
				(this.isEbeiCategory(cmd.getTaskCategoryId()) || this.isEbeiCategory(cmd.getParentId()))) {
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
		if(null == cmd.getPageSize())
			cmd.setPageSize(99999);
		List<PmTaskDTO> list = this.searchTasks(cmd).getRequests();
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
			int size = 0;
			if(null != list){
				size = list.size();
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
						cell4.setCellValue(pmTask.getAddress());
						if(PmTaskAddressType.FAMILY.equals(PmTaskAddressType.fromCode(pmTask.getAddressType()))) {
							Address address = addressProvider.findAddressById(pmTask.getAddressId());
							if(null != address) {
								cell4.setCellValue(address.getAddress());
							}
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
					List<PmTaskLog> logs = pmTaskProvider.listPmTaskLogs(task.getId(), PmTaskFlowStatus.PROCESSING.getCode());
					int logSize = logs.size();
					Cell cell7 = tempRow.createCell(7);
					cell7.setCellStyle(style);
					if(logSize != 0){
						User user = userProvider.findUserById(logs.get(logSize-1).getTargetId());
						if (null != user) {
							cell7.setCellValue(user.getNickName());
						}
					}
					Cell cell8 = tempRow.createCell(8);
					cell8.setCellStyle(style);
					cell8.setCellValue(category.getName());
					Cell cell9 = tempRow.createCell(9);
					cell9.setCellStyle(style);
					FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(task.getId(), "EhPmTasks", 20100L);
					String status = "";
					if (null != flowCase) {
						switch (flowCase.getStatus()) {
							case (byte) 0:
								status = "无效";
								break;
							case (byte) 1:
								status = "初始化";
								break;
							case (byte) 2:
								status = "处理中";
								break;
							case (byte) 3:
								status = "已取消";
								break;
							case (byte) 4:
								status = "已完成";
								break;
							case (byte) 5:
								status = "待评价";
								break;
							case (byte) 6:
								status = "暂缓";
								break;
						}
					}
					cell9.setCellValue(status);

					Cell cell10 = tempRow.createCell(10);
					cell10.setCellStyle(style);
					cell10.setCellValue(null == task.getStar() ? "-" : task.getStar());

					Cell cell11 = tempRow.createCell(11);
					cell11.setCellStyle(style);

					Cell cell12 = tempRow.createCell(12);
					cell12.setCellStyle(style);

					Cell cell13 = tempRow.createCell(13);
					cell13.setCellStyle(style);

					Cell cell14 = tempRow.createCell(14);
					cell14.setCellStyle(style);
					String feeModel = "";
					if (null != cmd.getTaskCategoryId())
						feeModel = configProvider.getValue(cmd.getNamespaceId(),"pmtask.feeModel." + cmd.getTaskCategoryId(),"");
					if(StringUtils.isNotEmpty(feeModel) && "1".equals(feeModel)){
						if(null != task.getStatus() && (task.getStatus().equals(PmTaskFlowStatus.COMPLETED.getCode()) || task.getStatus().equals(PmTaskFlowStatus.CONFIRMED.getCode()))){
//						费用确认后导出费用清单
							PmTaskOrder order = pmTaskProvider.findPmTaskOrderByTaskId(task.getId());
							if(null != order){
								BigDecimal serviceFee = BigDecimal.valueOf(order.getServiceFee());
								BigDecimal productFee = BigDecimal.valueOf(order.getProductFee());
								BigDecimal totalAmount = BigDecimal.valueOf(order.getAmount());


								cell11.setCellValue(serviceFee.movePointLeft(2).toString());


								cell12.setCellValue(productFee.movePointLeft(2).toString());

								List<PmTaskOrderDetail> products = pmTaskProvider.findOrderDetailsByTaskId(null,null,null,task.getId());
								StringBuffer content = new StringBuffer();
								if(null != products && products.size() > 0){
									for (PmTaskOrderDetail r : products) {
										BigDecimal price = BigDecimal.valueOf(r.getProductPrice());
										BigDecimal amount = BigDecimal.valueOf(r.getProductAmount());
										content.append(r.getProductName() + ":");
										content.append(price.multiply(amount).movePointLeft(2).toString() + "元");
										content.append("(" + price.movePointLeft(2).toString() + "元*" + amount.intValue() + ")\n");
									}
								}


								cell13.setCellValue(content.toString());


								cell14.setCellValue(totalAmount.movePointLeft(2).toString());
							}

						}
					}

				}
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
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2010020190L, cmd.getAppId(), null,cmd.getCurrentProjectId());//统计信息权限
		}
//		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
		Integer namespaceId = cmd.getNamespaceId();
		checkNamespaceId(namespaceId);

		//检查多入口应用权限
		if(!checkAppPrivilege(namespaceId, cmd.getTaskCategoryId(), cmd.getCurrentOrgId(), EntityType.COMMUNITY.getCode(),
				cmd.getCommunityId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST)){
			LOGGER.error("Permission is prohibited, namespaceId={}, taskCategoryId={}, orgId={}, ownerType={}, ownerId={}," +
					" privilege={}", namespaceId, cmd.getTaskCategoryId(), cmd.getCurrentOrgId(), EntityType.COMMUNITY.getCode(),
					cmd.getCommunityId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
					"check app privilege error");
		}

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
//		if(cmd.getOwnerId() == null) {
//			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_ALL_TASK_STATISTICS_LIST);
//		} else {
//			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getOwnerId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
//		}
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

				Timestamp startDate = getBeginOfLastMonth(now);
				Timestamp endDate = getBeginOfMonth(now);
				createTaskStatistics(namespaces, startDate, endDate, now);
			});
		}
	}

	@Override
	public void syncTaskStatistics(HttpServletResponse resp) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Namespace> namespaces = pmTaskProvider.listNamespace();
//				List<Namespace> namespaces = new ArrayList<>();
//				Namespace m = new Namespace();
//				m.setId(1000000);
//				namespaces.add(m);

				long now = System.currentTimeMillis();

				String dateStr = "2017-01-01 00:00:00";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					Date date = sdf.parse(dateStr);

					Calendar begin = Calendar.getInstance();
					begin.setTime(date);

					Calendar nowCalendar = Calendar.getInstance();
					nowCalendar.setTimeInMillis(now);

					while (begin.before(nowCalendar)) {

						Timestamp startDate = new Timestamp(begin.getTimeInMillis());

						begin.add(Calendar.MONTH, 1);
						Timestamp endDate = new Timestamp(begin.getTimeInMillis());
						createTaskStatistics(namespaces, startDate, endDate, now);

					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}).start();

		PrintWriter writer = null;
		try {
			writer = resp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.write("success");
		writer.flush();
		writer.close();
	}

	private void createTaskStatistics(List<Namespace> namespaces, Timestamp startDate, Timestamp endDate, long now) {
		for (Namespace n : namespaces) {
			for (Long id : PmTaskAppType.TYPES) {
				Category ancestor = categoryProvider.findCategoryById(id);

				if (null != ancestor) {
					List<Community> communities = communityProvider.listCommunitiesByNamespaceId(n.getId());
					for (Community community : communities) {
						List<Category> categories = categoryProvider.listTaskCategories(n.getId(), "community", community.getId(), ancestor.getId(), null, null, null);
						if (null != categories && !categories.isEmpty()) {
							for (Category taskCategory : categories) {
								createTaskStatistics(community.getId(), taskCategory.getId(), 0L, startDate, endDate, now, n.getId());
								List<Category> tempCategories = categoryProvider.listTaskCategories(n.getId(), "community", community.getId(), taskCategory.getId(), null, null, null);
								for (Category category : tempCategories) {
									createTaskStatistics(community.getId(), taskCategory.getId(), category.getId(), startDate, endDate, now, n.getId());
								}
							}
						}
					}
				}
			}
		}
	}

	private void createTaskStatistics(Long communityId, Long taskCategoryId, Long categoryId, Timestamp startDate,
			Timestamp endDate, Long now, Integer namespaceId) {
		PmTaskStatistics statistics = new PmTaskStatistics();
		Integer totalCount = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, null, startDate, endDate);
		Integer unProcessCount = pmTaskProvider.countTask(communityId, PmTaskFlowStatus.ACCEPTING.getCode(), taskCategoryId, categoryId, null, startDate, endDate);
		Integer processingCount = pmTaskProvider.countTask(communityId, PmTaskFlowStatus.PROCESSING.getCode(), taskCategoryId, categoryId, null, startDate, endDate);
		Integer processedCount = pmTaskProvider.countTask(communityId, PmTaskFlowStatus.COMPLETED.getCode(), taskCategoryId, categoryId, null, startDate, endDate);
		Integer closeCount = pmTaskProvider.countTask(communityId, PmTaskFlowStatus.INACTIVE.getCode(), taskCategoryId, categoryId, null, startDate, endDate);
		
		Integer star1 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, "1", startDate, endDate);
		Integer star2 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, "2", startDate, endDate);
		Integer star3 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, "3", startDate, endDate);
		Integer star4 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, "4", startDate, endDate);
		Integer star5 = pmTaskProvider.countTask(communityId, null, taskCategoryId, categoryId, "5", startDate, endDate);
		
		statistics.setTaskCategoryId(taskCategoryId);
		statistics.setCategoryId(categoryId);
		statistics.setCreateTime(new Timestamp(now));
		statistics.setDateStr(startDate);
		statistics.setNamespaceId(namespaceId);
		statistics.setOwnerId(communityId);
		statistics.setOwnerType(PmTaskOwnerType.COMMUNITY.getCode());
		
		statistics.setTotalCount(totalCount);
		statistics.setUnprocessCount(unProcessCount);
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
	
	private Timestamp getBeginOfLastMonth(Long time){
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
	
	private Timestamp getBeginOfMonth(Long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return new Timestamp(calendar.getTime().getTime());
	}
	
//	@Override
//	public PmTaskLogDTO getTaskLog(GetTaskLogCommand cmd) {
//		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
//		checkId(cmd.getId());
//
//		PmTaskLog pmTaskLog = checkPmTaskLog(cmd.getId());
//		PmTaskLogDTO pmTaskLogDTO = ConvertHelper.convert(pmTaskLog, PmTaskLogDTO.class);
//
//		List<PmTaskAttachment> attachments = pmTaskProvider.listPmTaskAttachments(pmTaskLog.getId(), PmTaskAttachmentType.TASKLOG.getCode());
//		List<PmTaskAttachmentDTO> attachmentDTOs = pmTaskCommonService.convertAttachmentDTO(attachments);
//		pmTaskLogDTO.setAttachments(attachmentDTOs);
//
//		return pmTaskLogDTO;
//	}

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


//	@Override
//	public ListOperatePersonnelsResponse listOperatePersonnels(ListOperatePersonnelsCommand cmd) {
//
//		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
//		checkOperateType(cmd.getOperateType());
//		checkOrganizationId(cmd.getOrganizationId());
//
//		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
//		if(null == org) {
//        	LOGGER.error("OrganizationId not found.");
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    				"OrganizationId not found.");
//        }
//		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
//
//		List<PmTaskTarget> targets = pmTaskProvider.listTaskTargets(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOperateType(),
//				cmd.getPageAnchor(), cmd.getPageSize());
//
//		ListOperatePersonnelsResponse response = new ListOperatePersonnelsResponse();
//
//		List<OrganizationMember> organizationMembers = new ArrayList<>();
//		for(PmTaskTarget t: targets) {
//			OrganizationMember m = organizationProvider.findOrganizationMemberByUIdAndOrgId(t.getTargetId(), cmd.getOrganizationId());
//			if(null != m)
//				organizationMembers.add(m);
//		}
//		int size = targets.size();
//		if (size > 0) {
//			List<OrganizationMemberDTO> dtos = organizationService.convertOrganizationMemberDTO(organizationMembers, org);
//			dtos.forEach(member -> {
////				Integer count = pmTaskProvider. countUserProccsingPmTask(cmd.getOwnerType(), cmd.getOwnerId(), member.getTargetId());
////				member.setProccesingTaskCount(count);
//			});
//			response.setMembers(dtos);
//    		if(size != pageSize){
//        		response.setNextPageAnchor(null);
//        	}else{
//        		response.setNextPageAnchor(targets.get(size-1).getId());
//        	}
//    	}
//
//		return response;
//	}
	
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
	public SearchTaskCategoryStatisticsResponse searchTaskCategoryStatistics(SearchTaskStatisticsCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2010020190L, cmd.getAppId(), null,cmd.getCurrentProjectId());//统计信息权限
		}
//		userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
		SearchTaskCategoryStatisticsResponse response = new SearchTaskCategoryStatisticsResponse();

		Integer namespaceId = cmd.getNamespaceId();

		//检查多入口应用权限
		if(!checkAppPrivilege(namespaceId, cmd.getTaskCategoryId(), cmd.getCurrentOrgId(), EntityType.COMMUNITY.getCode(),
				cmd.getCommunityId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST)){
			LOGGER.error("Permission is prohibited, namespaceId={}, taskCategoryId={}, orgId={}, ownerType={}, ownerId={}," +
							" privilege={}", namespaceId, cmd.getTaskCategoryId(), cmd.getCurrentOrgId(), EntityType.COMMUNITY.getCode(),
					cmd.getCommunityId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
					"check app privilege error");
		}

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
		//TODO:此处 目前统计所有服务类型，没有区分多入口，校验权限暂时屏蔽，后面需要根据多入口来统计，在校验权限
//		if(cmd.getCommunityId() == null) {
//			userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_ALL_TASK_STATISTICS_LIST);
//		} else {
//			userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_TASK_STATISTICS_LIST);
//		}
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

	@Deprecated
	@Override
	public ListAuthorizationCommunityByUserResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd) {
		Long step1 = System.currentTimeMillis();
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
		Long step2 = System.currentTimeMillis();

		List<CommunityDTO> dtos = serviceModuleService.listUserRelatedCommunityByModuleId(listUserRelatedProjectByModuleCommand);

		Long step3 = System.currentTimeMillis();

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
			Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
			response.setCommunities(communityProvider.listCommunitiesByNamespaceId(namespaceId).
			stream().map(r->{
				return ConvertHelper.convert(r,CommunityDTO.class);
			}).collect(Collectors.toList()));
		}

		Long step4 = System.currentTimeMillis();
		//TODO: LEILV
//		List<CommunityDTO> dtos = this.communityProvider.listCommunitiesByNamespaceId(UserContext.getCurrentNamespaceId()).stream().map(r->{
//			return ConvertHelper.convert(r, CommunityDTO.class);
//		}).collect(Collectors.toList());
//		response.setCommunities(dtos);
		LOGGER.debug("step2-step1 = " + (step2-step1));
		LOGGER.debug("step3-step2 = " + (step3-step2));
		LOGGER.debug("step4-step3 = " + (step4-step3));
		return response;
	}

	@Override
	public ListAuthorizationCommunityByUserResponse listOrganizationCommunityByUser(ListOrganizationCommunityByUserCommand cmd) {

		ListAuthorizationCommunityByUserResponse response = new ListAuthorizationCommunityByUserResponse();

		Long userId = UserContext.currentUserId();
		List<OrganizationMember> orgMembers = organizationService.listOrganizationMemberByOrganizationPathAndUserId(
				"/" + cmd.getOrganizationId() + "/", userId);

		List<CommunityDTO> result = new ArrayList<>();
		if (null != orgMembers) {
			orgMembers.stream().filter(r->
				r.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode()) || r.getGroupType().equals(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode())
			).forEach(m -> {
				OrganizationCommunityRequest request = organizationProvider.getOrganizationCommunityRequestByOrganizationId(m.getOrganizationId());
				if (null != request) {
					Community community = communityProvider.findCommunityById(request.getCommunityId());
					if (null != community) {
						boolean flag = true;
						for (CommunityDTO d: result) {
							if (d.getUuid().equals(community.getUuid())) {
								flag = false;
								break;
							}
						}
						if (flag) {
							result.add(ConvertHelper.convert(community, CommunityDTO.class));
						}
					}
				}
			});
		}
		if (result.size() == 0) {
			OrganizationCommunityRequest request = organizationProvider.getOrganizationCommunityRequestByOrganizationId(cmd.getOrganizationId());
			if (null != request) {
				Community community = communityProvider.findCommunityById(request.getCommunityId());
				if (null != community) {
					result.add(ConvertHelper.convert(community, CommunityDTO.class));
				}
			}
		}
		response.setCommunities(result);
		return response;
	}

	@Override
	public ListOrganizationCommunityBySceneTokenResponse listOrganizationCommunityBySceneToken(ListOrganizationCommunityBySceneTokenCommand cmd) {
//		SceneTokenDTO sceneTokenDTO = null;
//		if (null != cmd.getSceneToken()) {
//			User user = UserContext.current().getUser();
//			sceneTokenDTO = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
//		}
//		if (sceneTokenDTO==null)
//			return null;
		ListOrganizationCommunityBySceneTokenResponse response = new ListOrganizationCommunityBySceneTokenResponse();
		//SceneType sceneType = SceneType.fromCode(sceneTokenDTO.getScene());
		List<Community> communities = new ArrayList<>();
		List<CommunityDTO> result = new ArrayList<>();

		//TODO 标准版没有场景的概念，直接使用园区类型
		AppContext appContext = UserContext.current().getAppContext();

		Community community = communityProvider.findCommunityById(appContext.getCommunityId());
		if (community != null){
			communities.add(community);
		}


//		Community community = null;
//		switch (sceneType) {
//			case DEFAULT:
//			case PARK_TOURIST:
//			case ENTERPRISE_NOAUTH:
//				community = communityProvider.findCommunityById(sceneTokenDTO.getEntityId());
//				if (community != null)
//					communities.add(community);
//
//				break;
//			case FAMILY:
//				FamilyDTO family = familyProvider.getFamilyById(sceneTokenDTO.getEntityId());
//
//				if (family != null) {
//					community = communityProvider.findCommunityById(family.getCommunityId());
//				} else {
//					if (LOGGER.isWarnEnabled()) {
//						LOGGER.warn("Family not found, sceneToken=" + sceneTokenDTO);
//					}
//				}
//				if (community != null) {
//					communities.add(community);
//				}
//
//				break;
//			case PM_ADMIN:// 无小区ID
//				communities.addAll(communityProvider.listCommunitiesByNamespaceId(UserContext.getCurrentNamespaceId()));
//				break;
//			case ENTERPRISE: // 增加两场景，与园区企业保持一致
//				List<OrganizationDTO> organizationDTOS = organizationService.listUserRelateOrganizations(
//						UserContext.getCurrentNamespaceId(),UserContext.currentUserId(),OrganizationGroupType.ENTERPRISE);
//
//				communities.addAll(organizationDTOS.stream().map(r->{
//					Community co = communityProvider.findCommunityById(r.getCommunityId());
//					return co;
//				}).collect(Collectors.toList()));
//				break;
//			default:
//				LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDTO);
//				break;
//		}
		communities.forEach(r->{
			result.add(ConvertHelper.convert(r, CommunityDTO.class));
		});
		response.setCommunities(result);
		return response;
	}

	@Override
	public GetUserRelatedAddressByCommunityResponse getUserRelatedAddressesByCommunity(GetUserRelatedAddressesByCommunityCommand cmd) {

		GetUserRelatedAddressByCommunityResponse response = new GetUserRelatedAddressByCommunityResponse();

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

		if (StringUtils.isNotBlank(cmd.getKeyword())) {
			LOGGER.info("findClaimedIdentifierByToken: {}", cmd.getKeyword());
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getKeyword());
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
			//选一个公司获得通讯录名字
			if (organizations.size()>0){
				OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(),organizations.get(0).getId());
				response.setUserName(member.getContactName());
			}
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
							dto.setCommunityName(o.getCommunityName());
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
	public GetIfHideRepresentResponse getIfHideRepresent(GetIfHideRepresentCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null ? UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		GetIfHideRepresentResponse response = new GetIfHideRepresentResponse();
		if(null == cmd.getAppId()) {
			LOGGER.error("Invalid appId parameter.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid appId parameter.");
		}
		String colName = "pmtask.hide.represent.";
		colName += cmd.getAppId().toString();
		response.setIfHide(configProvider.getIntValue(namespaceId,colName,0));

//		SceneTokenDTO sceneTokenDTO = null;
//		if (null != cmd.getSceneToken()) {
//			User user = UserContext.current().getUser();


//			sceneTokenDTO = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
//		}
//		Integer ifAdmain = 1;
//		if (sceneTokenDTO != null) {
//			String scene = sceneTokenDTO.getScene();
//			if (SceneType.PM_ADMIN.getCode().equals(scene))
//				ifAdmain = 0;
//		}

		colName = "pmtask.hide.represent.app";
		colName += cmd.getAppId().toString();
		response.setIfAppHide(configProvider.getIntValue(namespaceId, colName , 0));

		colName = "pmtask.hide.represent.bg";
		colName += cmd.getAppId().toString();
		response.setIfBgHide(configProvider.getIntValue(namespaceId, colName , 0));


		//去除场景，按照普通用户显示
		Integer ifAdmain = 0;
		response.setIfHide(response.getIfHide()|ifAdmain);
		response.setIfAppHide(response.getIfAppHide()|ifAdmain);
		response.setIfBgHide(response.getIfBgHide()|ifAdmain);
		return response;
	}

	//	@Override
//	public void synchronizedData(SearchTasksCommand cmd) {
//
//		User user = UserContext.current().getUser();
//
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//		Flow flow = flowService.getEnabledFlow(namespaceId, FlowConstants.PM_TASK_MODULE,
//				FlowModuleType.NO_MODULE.getCode(), 0L, FlowOwnerType.PMTASK.getCode());
//		if(null == flow) {
//			LOGGER.error("Enable pmtask flow not found, moduleId={}", FlowConstants.PM_TASK_MODULE);
//			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ENABLE_FLOW,
//					"Enable pmtask flow not found.");
//		}
//
//		Long flowId = flow.getFlowMainId();
//		long endNode = flow.getEndNode();
//			List<PmTaskDTO> list = pmTaskSearch.searchDocsByType(cmd.getStatus(), cmd.getKeyword(), cmd.getOwnerId(), cmd.getOwnerType(),
//					cmd.getTaskCategoryId(), cmd.getStartDate(), cmd.getEndDate(), cmd.getAddressId(), cmd.getBuildingName(),
//					null, null);
//
//			for(PmTaskDTO t: list) {
//				PmTask task = pmTaskProvider.findTaskById(t.getId());
//
//				if (0 != task.getFlowCaseId())
//					continue;
//
//				if (PmTaskFlowStatus.INACTIVE.getCode() == task.getStatus()) {
//					pmTaskProvider.deleteTask(task);
//					pmTaskSearch.deleteById(task.getId());
//					continue;
//				}
//
//
//				CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
//				createFlowCaseCommand.setApplyUserId(task.getCreatorUid());
//				createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
//				createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
//				createFlowCaseCommand.setReferId(task.getId());
//				createFlowCaseCommand.setReferType(EntityType.PM_TASK.getCode());
//				//createFlowCaseCommand.setContent("发起人：" + requestorName + "\n" + "联系方式：" + requestorPhone);
//				createFlowCaseCommand.setContent(task.getContent());
//
//				createFlowCaseCommand.setProjectId(task.getOwnerId());
//				createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());
//				if (StringUtils.isNotBlank(task.getBuildingName())) {
//					Building building = buildingProvider.findBuildingByName(namespaceId, task.getOwnerId(),
//							task.getBuildingName());
//					if(building != null){
//						ResourceCategoryAssignment resourceCategory = communityProvider.findResourceCategoryAssignment(building.getId(),
//								EntityType.BUILDING.getCode(), namespaceId);
//						if (null != resourceCategory) {
//							createFlowCaseCommand.setProjectId(resourceCategory.getResourceCategryId());
//							createFlowCaseCommand.setProjectType(EntityType.RESOURCE_CATEGORY.getCode());
//						}
//					}
//				}
//
//				FlowCase flowCase = flowService.createFlowCase(createFlowCaseCommand);
//
//				if (PmTaskStatus.UNPROCESSED.getCode() == task.getStatus() ||
//						PmTaskStatus.PROCESSING.getCode() == task.getStatus()) {
//					FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
//					stepDTO.setFlowCaseId(flowCase.getId());
//					stepDTO.setFlowMainId(flowCase.getFlowMainId());
//					stepDTO.setFlowVersion(flowCase.getFlowVersion());
//					stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
//					stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//					stepDTO.setStepCount(flowCase.getStepCount());
//					flowService.processAutoStep(stepDTO);
//				} else if (PmTaskStatus.PROCESSED.getCode() == task.getStatus() ||
//						PmTaskStatus.CLOSED.getCode() == task.getStatus() ||
//						PmTaskStatus.REVISITED.getCode() == task.getStatus()) {
//					while (flowCase.getCurrentNodeId() != endNode) {
//
//						FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
//						stepDTO.setFlowCaseId(flowCase.getId());
//						stepDTO.setFlowMainId(flowCase.getFlowMainId());
//						stepDTO.setFlowVersion(flowCase.getFlowVersion());
//						stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
//						stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//						stepDTO.setStepCount(flowCase.getStepCount());
//						flowService.processAutoStep(stepDTO);
//
//						flowCase = flowCaseProvider.getFlowCaseById(flowCase.getId());
//					}
//
//				}
//				FlowNode flowNode = flowNodeProvider.getFlowNodeById(flowCase.getCurrentNodeId());
//				task.setStatus(pmTaskCommonService.convertFlowStatus(flowNode.getGroupByParams()));
//				task.setFlowCaseId(flowCase.getId());
//				pmTaskProvider.updateTask(task);
//
//				pmTaskSearch.deleteById(task.getId());
//				pmTaskSearch.feedDoc(task);
//
//			}
//
//	}

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

	@Override
	public Object getThirdAddress(HttpServletRequest req) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		return handler.getThirdAddress(req);
	}

	@Override
	public Object createThirdTask(HttpServletRequest req) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		return handler.createThirdTask(req);
	}

	@Override
	public Object listThirdTasks(HttpServletRequest req) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		return handler.listThirdTasks(req);
	}

	@Override
	public Object getThirdTaskDetail(HttpServletRequest req) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		return handler.getThirdTaskDetail(req);
	}

	@Override
	public Object getThirdCategories(HttpServletRequest req) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		String handle = configProvider.getValue(HANDLER + namespaceId, PmTaskHandle.FLOW);
		PmTaskHandle handler = PlatformContext.getComponent(PmTaskHandle.PMTASK_PREFIX + handle);
		return handler.getThirdCategories(req);
	}

	// 查找报修公司的报修记录, 对接不查
	@Override
	public List<SearchTasksByOrgDTO> listTasksByOrg(SearchTasksByOrgCommand cmd17) {
		List<SearchTasksByOrgDTO> ret = new ArrayList<>();
		List<PmTask> list = pmTaskProvider.findTasksByOrg(cmd17.getCommunityId(), cmd17.getNamespaceId(), cmd17.getOrganizationId(),null);
		for(PmTask task : list){
			SearchTasksByOrgDTO dto = new SearchTasksByOrgDTO();
			dto.setBuildingName(task.getBuildingName());
			dto.setCreateTime(task.getCreateTime());
			dto.setRequestorName(task.getRequestorName());
			dto.setRequestorPhone(task.getRequestorPhone());
			if(task.getStatus()==1){
				dto.setStatus("待处理");
			}else if(task.getStatus()==2){
				dto.setStatus("处理中");
			} else if (task.getStatus() == 3) {
				dto.setStatus("已取消");
			} else if (task.getStatus() == 4) {
				dto.setStatus("已完成");
			}
			dto.setTaskCategoryName(categoryProvider.findCategoryById(task.getTaskCategoryId()).getName());
			dto.setContent(task.getContent());
			if (task.getOrganizationUid() == null || task.getOrganizationUid() == 0) {
				dto.setPmTaskSource("用户发起");
			} else {
				dto.setPmTaskSource("员工代发");
			}

			ret.add(dto);
		}
		return ret;
	}

	@Override
	public List<SearchTasksByOrgDTO> searchOrgTasks(SearchOrgTasksCommand cmd) {
		List<SearchTasksByOrgDTO> ret = new ArrayList<>();
		List<PmTask> list = pmTaskProvider.findTasksByOrg(cmd.getCommunityId(),cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getTaskCategoryId());
		if (list != null && list.size() > 0) {
			list.forEach(r -> {
				SearchTasksByOrgDTO dto = new SearchTasksByOrgDTO();
				if(r.getStatus()==1){
					dto.setStatus("待处理");
				}else if(r.getStatus()==2){
					dto.setStatus("处理中");
				}else if(r.getStatus()==3){
					dto.setStatus("已取消");
				}else if(r.getStatus()==4){
					dto.setStatus("已完成");
				}

				dto.setRequestorPhone(r.getRequestorPhone());
				dto.setRequestorName(r.getRequestorName());
				if (r.getOrganizationUid() == null || r.getOrganizationUid() == 0) {
					dto.setPmTaskSource("用户发起");
				} else {
					dto.setPmTaskSource("员工代发");
				}
				dto.setCreateTime(r.getCreateTime());
				dto.setBuildingName(r.getBuildingName());
				dto.setTaskCategoryName(categoryProvider.findCategoryById(r.getTaskCategoryId()).getName());
				dto.setContent(r.getContent());
				dto.setFlowCaseId(r.getFlowCaseId());
				ret.add(dto);
			});
		}
		return ret;
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
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2010020140L, cmd.getAppId(), null,cmd.getCurrentProjectId());//任务列表权限
		}
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

		if(null != dto.getCategoryId()){
			Category category = categoryProvider.findCategoryById(dto.getCategoryId());

			if(category != null) {
				dataMap.put("categoryName",category.getName());
			} else {
				dataMap.put("categoryName","");
			}
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

	@Override
	public void changeTasksStatus(UpdateTasksStatusCommand cmd) {
        List<PmTask> list = pmTaskProvider.findTaskByOrderId(cmd.getOrderId());
        if(list==null || list.size()==0)
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_ORDER_ID,
                    "OrderId does not exist.");
        if (cmd.getStateId()==null || cmd.getStateId()<1 || cmd.getStateId()>7)
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_STATE_ID,
					"Illegal stateId");
		PmTask task = list.get(0);


		EbeiPmTaskStatus state = EbeiPmTaskStatus.fromCode(cmd.getStateId());

		dbProvider.execute((TransactionStatus status) -> {


			//更新工作流case状态
			FlowCase flowCase = flowCaseProvider.getFlowCaseById(task.getFlowCaseId());

			if (FlowCaseStatus.INVALID.getCode() != flowCase.getStatus()) {
				Byte flowCaseStatus = FlowCaseStatus.PROCESS.getCode();
				switch (state){
					case UNPROCESSED: break;
					case PROCESSING: flowCaseStatus = FlowCaseStatus.PROCESS.getCode();break;
					case INACTIVE: flowCaseStatus = FlowCaseStatus.ABSORTED.getCode();break;
                    case REVISITED: flowCaseStatus = FlowCaseStatus.ABSORTED.getCode();break; //已关闭
                    case PROCESSED: flowCaseStatus = FlowCaseStatus.FINISHED.getCode();break;
					case CANCELED: flowCaseStatus = FlowCaseStatus.ABSORTED.getCode();break;
					default: flowCaseStatus = FlowCaseStatus.PROCESS.getCode();
				}
				task.setStatus(flowCaseStatus);
				pmTaskProvider.updateTask(task);
				if (flowCaseStatus == FlowCaseStatus.ABSORTED.getCode() && flowCase.getStatus() == FlowCaseStatus.PROCESS.getCode())
					cancelTask(task.getId());
				else if (flowCaseStatus == FlowCaseStatus.FINISHED.getCode() && flowCase.getStatus() == FlowCaseStatus.PROCESS.getCode())
					finishTask(task.getId());
			}
			return null;
		});
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);
		}
	private void cancelTask(Long id){
		PmTask task = checkPmTask(id);


		FlowCase flowCase = flowCaseProvider.getFlowCaseById(task.getFlowCaseId());
		//节点状态流转
		FlowAutoStepDTO stepDTO = ConvertHelper.convert(flowCase, FlowAutoStepDTO.class);
		stepDTO.setFlowCaseId(flowCase.getId());
		stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
		stepDTO.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
		flowService.processAutoStep(stepDTO);

	}

	private void finishTask(Long id){
		PmTask task = checkPmTask(id);

		FlowCase flowCase = flowCaseProvider.getFlowCaseById(task.getFlowCaseId());
		//节点状态流转
		FlowAutoStepDTO stepDTO = ConvertHelper.convert(flowCase, FlowAutoStepDTO.class);
		stepDTO.setFlowCaseId(flowCase.getId());
		stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
		stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
		flowService.processAutoStep(stepDTO);
	}

	private void copyCategories(Category c,Long parentId,Long communityId){
		List<Category> list = categoryProvider.listTaskCategories(null,null,null, c.getId(), null,
				null, null);
			Category newCategory = ConvertHelper.convert(c,Category.class);
			newCategory.setOwnerType("community");
			newCategory.setOwnerId(communityId);
			newCategory.setParentId(parentId);
			Long id = categoryProvider.createCategory(newCategory);
			if (list!=null && list.size()>0)
				list.forEach(r->{
					if (r.getNamespaceId()!=null && r.getNamespaceId()>0) {
						copyCategories(r, id,communityId);
					}
				});

	}

	@Override
	public void syncCategories() {
		if (flag==0) {
			LOGGER.info("syncCategories test:flag = "+flag);
			flag = 1;
		}else{
			LOGGER.info("syncCategories test:flag = "+flag);
			return;
		}
		List<Category> list = categoryProvider.listTaskCategories(null,null,null, 6L, null,
				null, null);
		list.forEach(r->{
			if (r.getNamespaceId()!=null && r.getNamespaceId()>0) {
				List<Community> communities = communityProvider.listCommunitiesByNamespaceId(r.getNamespaceId());
				if (communities!=null && communities.size()>0)
					for (Community community :communities)
					copyCategories(r, 6l,community.getId());
			}
		});

		list = categoryProvider.listTaskCategories(null,null,null, 9L, null,
				null, null);
		list.forEach(r->{
			if (r.getNamespaceId()!=null && r.getNamespaceId()>0) {
				List<Community> communities = communityProvider.listCommunitiesByNamespaceId(r.getNamespaceId());
				if (communities!=null && communities.size()>0)
					for (Community community :communities)
						copyCategories(r, 9l,community.getId());
			}
		});
	}

	@Override
	public PmTaskStatDTO getStatSurvey(GetTaskStatCommand cmd) {
		this.checkNamespaceId(cmd.getNamespaceId());
//		查询报修任务
		List<PmTask> list = this.getSurveyData(cmd);
//		聚合统计
		Map<Byte,Long> result = list.stream().collect(Collectors.groupingBy(PmTask::getStatus,Collectors.counting()));
//		构建响应数据对象
		PmTaskStatDTO pmTaskStatDTO = new PmTaskStatDTO();
		result.entrySet().forEach(elem ->{
			if(FlowCaseStatus.PROCESS.getCode() == elem.getKey().byteValue())
				pmTaskStatDTO.setProcessing(elem.getValue().intValue());
			if(FlowCaseStatus.ABSORTED.getCode() == elem.getKey().byteValue())
				pmTaskStatDTO.setClose(elem.getValue().intValue());
			if(FlowCaseStatus.FINISHED.getCode() == elem.getKey().byteValue())
				pmTaskStatDTO.setComplete(elem.getValue().intValue());
		});
		pmTaskStatDTO.setTotal(pmTaskStatDTO.getProcessing() + pmTaskStatDTO.getClose() + pmTaskStatDTO.getComplete());
		return pmTaskStatDTO;
	}

	@Override
	public List<PmTaskStatSubDTO> getStatByCategory(GetTaskStatCommand cmd) {
		this.checkNamespaceId(cmd.getNamespaceId());
//		查询报修任务
		List<PmTask> list = this.getSurveyData(cmd);
//		聚合统计
		Map<Long,Map<Long,List<PmTask>>> result = list.stream().collect(Collectors.groupingBy(PmTask::getOwnerId,Collectors.groupingBy(PmTask::getTaskCategoryId)));
//		构建响应数据对象
		List<PmTaskStatSubDTO> dtoList = new ArrayList<>();
		for (Map.Entry<Long,Map<Long,List<PmTask>>> elem: result.entrySet()) {
			Community community = communityProvider.findCommunityById(elem.getKey());
			for (Map.Entry<Long,List<PmTask>> elem1:elem.getValue().entrySet()
				 ) {
				PmTaskStatSubDTO bean = new PmTaskStatSubDTO();
				bean.setOwnerId(elem.getKey());
				bean.setOwnerName(null != community ? community.getName() : "");
				//为科兴与一碑对接
				if(cmd.getNamespaceId() == 999983 && null != cmd.getAppId() &&
						cmd.getAppId() == PmTaskHandle.EBEI_TASK_CATEGORY) {
					bean.setType("物业报修");
				}else{
					Category category = categoryProvider.findCategoryById(elem1.getKey());
					bean.setType(null != category ? category.getName() : "");
				}
				bean.setTotal(elem1.getValue().size());
				dtoList.add(bean);
			}
		}
		return dtoList;
	}

	@Override
	public List<PmTaskStatDTO> getStatByCreator(GetTaskStatCommand cmd) {
		this.checkNamespaceId(cmd.getNamespaceId());
//		查询报修任务
		List<PmTask> list = this.getSurveyData(cmd);
//		聚合统计
		Map<Long,Integer> initCount = new HashMap<>();
		Map<Long,Integer> agentCount = new HashMap<>();
		list.forEach(elem -> {
			if(!initCount.keySet().contains(elem.getOwnerId())){
				initCount.put(elem.getOwnerId(),0);
				agentCount.put(elem.getOwnerId(),0);
			}
			if(null == elem.getOrganizationUid() || 0 == elem.getOrganizationUid()){
				initCount.put(elem.getOwnerId(),initCount.get(elem.getOwnerId())+1);
			}else{
				agentCount.put(elem.getOwnerId(),agentCount.get(elem.getOwnerId())+1);
			}
		});
//		构建响应数据对象
		List<PmTaskStatDTO> dtolist = initCount.entrySet().stream().map(elem ->{
			PmTaskStatDTO bean = new PmTaskStatDTO();
			bean.setNamespaceId(cmd.getNamespaceId());
			bean.setOwnerType(null != cmd.getOwnerType() ? cmd.getOwnerType() : "");
			bean.setOwnerId(elem.getKey());
			Community community = communityProvider.findCommunityById(elem.getKey());
			if(null != community)
				bean.setOwnerName(community.getName());
			else
				bean.setOwnerName("");
			bean.setInit(elem.getValue());
			bean.setAgent(agentCount.get(elem.getKey()));
			bean.setTotal(bean.getInit() + bean.getAgent());
			return bean;
		}).collect(Collectors.toList());
		return dtolist;
	}

	@Override
	public List<PmTaskStatDTO> getStatByStatus(GetTaskStatCommand cmd) {
		this.checkNamespaceId(cmd.getNamespaceId());
//		查询报修任务
		List<PmTask> list = this.getSurveyData(cmd);
		Map<Long,Map<Byte,List<PmTask>>> result = list.stream().collect(Collectors.groupingBy(PmTask::getOwnerId,Collectors.groupingBy(PmTask::getStatus)));
		List<PmTaskStatDTO> dtolist = new ArrayList<>();
		for (Map.Entry<Long,Map<Byte,List<PmTask>>> elem : result.entrySet()) {
			PmTaskStatDTO bean = new PmTaskStatDTO();
			bean.setOwnerId(elem.getKey());
			Community community = communityProvider.findCommunityById(elem.getKey());
			bean.setOwnerName(null != community ? community.getName() : "");
			for (Map.Entry<Byte,List<PmTask>> elem1 : elem.getValue().entrySet()) {
				if(FlowCaseStatus.PROCESS.getCode() == elem1.getKey().byteValue())
					bean.setProcessing(elem1.getValue().size());
				if(FlowCaseStatus.ABSORTED.getCode() == elem1.getKey().byteValue())
					bean.setClose(elem1.getValue().size());
				if(FlowCaseStatus.FINISHED.getCode() == elem1.getKey().byteValue())
					bean.setComplete(elem1.getValue().size());
			}
			bean.setTotal(bean.getProcessing() + bean.getClose() + bean.getComplete());
			dtolist.add(bean);
		}
		return dtolist;
	}

	@Override
	public List<PmTaskStatSubDTO> getStatByArea(GetTaskStatCommand cmd) {
		this.checkNamespaceId(cmd.getNamespaceId());
//		查询报修任务
		List<PmTask> list = this.getSurveyData(cmd);
//		聚合统计
		Map<Long,Map<String,List<PmTask>>> result = list.stream().collect(Collectors.groupingBy(PmTask::getOwnerId,Collectors.groupingBy(PmTask::getBuildingName)));
//		构建响应数据对象
		List<PmTaskStatSubDTO> dtolist = new ArrayList<>();
		for(Map.Entry<Long,Map<String,List<PmTask>>> elem : result.entrySet()){
			Community community = communityProvider.findCommunityById(elem.getKey());
			for(Map.Entry<String,List<PmTask>> elem1 : elem.getValue().entrySet()){
				PmTaskStatSubDTO bean = new PmTaskStatSubDTO();
				bean.setNamespaceId(cmd.getNamespaceId());
				bean.setOwnerType(null != cmd.getOwnerType() ? cmd.getOwnerType() : "");
				bean.setOwnerId(elem.getKey());
				bean.setOwnerName(null != community ? community.getName() : "");
				bean.setType(elem1.getKey());
				bean.setTotal(elem1.getValue().size());
				dtolist.add(bean);
			}
		}
		return dtolist;
	}

	@Override
	public void exportTaskStat(GetTaskStatCommand cmd, HttpServletResponse resp) {

		Integer exportType = cmd.getExportType();
		if(null == exportType){
			LOGGER.error("ExportType cannot be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ExportType cannot be null.");
		}

		XSSFWorkbook wb = new XSSFWorkbook();

		Font font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 16);
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		Sheet sheet = wb.createSheet("task");
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(20);

//		1 导出按类型统计数据 2 导出按来源统计数据 3 导出按状态统计数据 4 导出按区域统计数据
		switch (exportType){
			case 1 : this.exportTaskStatByCategory(cmd,sheet);break;
			case 2 : this.exportTaskStatByCreator(cmd,sheet);break;
			case 3 : this.exportTaskStatByStatus(cmd,sheet);break;
			case 4 : this.exportTaskStatByArea(cmd,sheet);break;
            case 5 : this.exportTaskEval(cmd,sheet);break;
		}

		exportExcel(wb, resp);

	}

	@Override
	public List<PmTaskEvalStatDTO> getEvalStat(GetEvalStatCommand cmd) {
		List<PmTaskEvalStatDTO> dtos = new ArrayList<>();
//		取当前项目启用工作流
		Flow flow = flowService.getEnabledFlow(cmd.getNamespaceId(),20100L,cmd.getModuleType(),cmd.getOwnerId(),cmd.getOwnerType());
		if(null == flow || !flow.getAllowFlowCaseEndEvaluate().equals((byte) 1)){
//			不设置评价返回空列表
			return dtos;
		}
//		取评价项目
		List<FlowEvaluateItem> evalItems = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flow.getFlowMainId(),flow.getFlowVersion());
		List<String> evalItemNameList = evalItems.stream().map(r -> r.getName()).collect(Collectors.toList());
		List<FlowEvaluateItem> allEvalItems = flowEvaluateItemProvider.findFlowEvaluateItemsByFlowId(flow.getFlowMainId(),null);
		List<FlowEvaluateItem> tagEvalItems = allEvalItems.stream().filter(r -> evalItemNameList.contains(r.getName())).collect(Collectors.toList());
		Map<String,List<FlowEvaluateItem>> gbName = tagEvalItems.stream().collect(Collectors.groupingBy(FlowEvaluateItem::getName));
//		取工作流下所有评价
		List<FlowEvaluate> evals =  flowEvaluateProvider.findEvaluatesByFlowMainId(flow.getFlowMainId(),null,cmd.getBeginTime(),cmd.getEndTime());
//		根据评价项目分组
	 	Map<Long,List<FlowEvaluate>> evalgroupsById = evals.stream().collect(Collectors.groupingBy(FlowEvaluate::getEvaluateItemId));
	 	Map<String,List<FlowEvaluate>> evalgroupsByName = new HashMap<>();
	 	gbName.forEach((s, flowEvaluateItems) -> {
			List<FlowEvaluate> templist = new ArrayList<>();
	 		flowEvaluateItems.forEach(r-> {
	 			if(null != evalgroupsById.get(r.getId()))
					templist.addAll(evalgroupsById.get(r.getId()));
			});
	 		evalgroupsByName.put(s,templist);
		});
//		统计运算
	 	evalItems.forEach(item -> {
			PmTaskEvalStatDTO stat = new PmTaskEvalStatDTO();
			List<FlowEvaluate> evalgroup = evalgroupsByName.get(item.getName());
			if(null != evalgroup){
				int total = evalgroup.size();
				if(total > 0){
					stat.setEvalName(item.getName());
					BigDecimal totalstar;
					Map<Byte,List<FlowEvaluate>> stargroups = evalgroup.stream().collect(Collectors.groupingBy(FlowEvaluate::getStar));
					stargroups.entrySet().forEach(star -> {
						switch (star.getKey()){
							case (byte) 1 : stat.setAmount1(star.getValue().size()); break;
							case (byte) 2 : stat.setAmount2(star.getValue().size()); break;
							case (byte) 3 : stat.setAmount3(star.getValue().size()); break;
							case (byte) 4 : stat.setAmount4(star.getValue().size()); break;
							case (byte) 5 : stat.setAmount5(star.getValue().size()); break;
						}
					});
					stat.setTotalAmount(total);
					totalstar = BigDecimal.valueOf(stat.getAmount1() + stat.getAmount2() * 2 + stat.getAmount3() * 3 + stat.getAmount4() * 4 + stat.getAmount5() * 5);
					stat.setEvalAvg(totalstar.divide(BigDecimal.valueOf(total),1,BigDecimal.ROUND_HALF_UP).toString());
					dtos.add(stat);
				}
			}
		});
		return dtos;
	}

//	----------------------------------------- 3.7 -----------------------------------------

	@Override
	public PmTaskConfigDTO setPmTaskConfig(SetPmTaskConfigCommand cmd) {
		User user = UserContext.current().getUser();
		PmTaskConfig result = pmTaskProvider.findPmTaskConfigbyOwnerId(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getTaskCategoryId());
		if(null != result){
			if(null != cmd.getContentHint())
				result.setContentHint(cmd.getContentHint());
			else
				result.setContentHint("");
			if(null != cmd.getPaymentFlag())
				result.setPaymentFlag(cmd.getPaymentFlag());
			if(null != cmd.getPaymentAccount())
				result.setPaymentAccount(cmd.getPaymentAccount());
			if(null != cmd.getPaymentAccountType()){
				result.setPaymentAccountType(PaymentAccountType.getCode(cmd.getPaymentAccountType()));
			}
			result.setUpdaterId(user.getId());
			result = pmTaskProvider.updatePmTaskConfig(result);
		} else {
			result = ConvertHelper.convert(cmd, PmTaskConfig.class);
			result.setCreatorId(user.getId());
			result = pmTaskProvider.createPmTaskConfig(result);
		}
		return ConvertHelper.convert(result,PmTaskConfigDTO.class);
	}

	@Override
	public PmTaskConfigDTO searchPmTaskConfigByProject(GetPmTaskConfigCommand cmd) {
		if(null == cmd.getId() && (null == cmd.getNamespaceId() && null == cmd.getOwnerId())){
			LOGGER.error("Invalid parameter, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter.");
		}
		PmTaskConfig result = this.pmTaskProvider.findPmTaskConfigbyOwnerId(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getTaskCategoryId());
		return ConvertHelper.convert(result,PmTaskConfigDTO.class);
	}

	@Override
	public void createOrderDetails(CreateOrderDetailsCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = cmd.getNamespaceId();
		String ownerType = cmd.getOwnerType();
		Long ownerId = cmd.getOwnerId();
		Long taskId = cmd.getTaskId();
//		创建订单
		PmTaskOrder order = ConvertHelper.convert(cmd,PmTaskOrder.class);
		Long serviceFee = order.getServiceFee();
		if(null != serviceFee)
			order.setServiceFee(serviceFee);
		Long productFee = 0L;
		if(null != cmd.getOrderDetails()){
			productFee = cmd.getOrderDetails().stream().mapToLong(r -> r.getProductAmount() * r.getProductPrice()).sum();
		}
		order.setProductFee(productFee);
		order.setAmount(serviceFee + productFee);
		order.setStatus((byte)0);
		order = this.pmTaskProvider.createPmTaskOrder(order);
		Long orderId = order.getId();
		if(null != cmd.getOrderDetails()){
//			创建订单明细
			List<PmTaskOrderDetail> details = cmd.getOrderDetails().stream().map(r->{
				PmTaskOrderDetail bean = ConvertHelper.convert(r,PmTaskOrderDetail.class);
				bean.setNamespaceId(namespaceId);
				bean.setOwnerType(ownerType);
				bean.setOwnerId(ownerId);
				bean.setTaskId(taskId);
				bean.setOrderId(orderId);
				return bean;
			}).collect(Collectors.toList());
			this.pmTaskProvider.createOrderDetails(details);
		}

		this.FlowStepOrderDetailsOperate(taskId,user.getId(),user.getNickName(),FlowStepType.APPROVE_STEP.getCode());

	}

	@Override
	public void modifyOrderDetails(CreateOrderDetailsCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = cmd.getNamespaceId();
		String ownerType = cmd.getOwnerType();
		Long ownerId = cmd.getOwnerId();
		Long taskId = cmd.getTaskId();
		Long serviceFee = cmd.getServiceFee();
		Long productFee = 0L;
//		修改订单
		PmTaskOrder order = this.pmTaskProvider.findPmTaskOrderByTaskId(taskId);
		if(null != serviceFee)
			order.setServiceFee(serviceFee);
		if(null != cmd.getOrderDetails() && cmd.getOrderDetails().size() > 0){
			productFee = cmd.getOrderDetails().stream().mapToLong(r -> r.getProductAmount() * r.getProductPrice()).sum();
		}
		order.setProductFee(productFee);
		order.setAmount(serviceFee + productFee);
		order = this.pmTaskProvider.updatePmTaskOrder(order);
		Long orderId = order.getId();
		this.pmTaskProvider.deleteOrderDetailsByOrderId(orderId);
		if(null != cmd.getOrderDetails() && cmd.getOrderDetails().size() > 0){
//		创建订单明细
			List<PmTaskOrderDetail> details = cmd.getOrderDetails().stream().map(r->{
				PmTaskOrderDetail bean = ConvertHelper.convert(r,PmTaskOrderDetail.class);
				bean.setNamespaceId(namespaceId);
				bean.setOwnerType(ownerType);
				bean.setOwnerId(ownerId);
				bean.setTaskId(taskId);
				bean.setOrderId(orderId);
				return bean;
			}).collect(Collectors.toList());
			this.pmTaskProvider.createOrderDetails(details);
		}

		this.FlowStepOrderDetailsOperate(taskId,user.getId(),user.getNickName(),FlowStepType.NO_STEP.getCode());
	}

	@Override
	public PmTaskOrderDTO searchOrderDetailsByTaskId(GetOrderDetailsCommand cmd) {
		PmTask task = this.pmTaskProvider.findTaskById(cmd.getTaskId());
		PmTaskOrder order = this.pmTaskProvider.findPmTaskOrderByTaskId(cmd.getTaskId());
		PmTaskOrderDTO dto = new PmTaskOrderDTO();
		List<PmTaskOrderDetailDTO> products = new ArrayList<>();
		if(null != order)
			dto = ConvertHelper.convert(order, PmTaskOrderDTO.class);

		List<PmTaskOrderDetail> result = this.pmTaskProvider.findOrderDetailsByTaskId(null, null, null, cmd.getTaskId());
		if(null != result && result.size() > 0){
			products.addAll(result.stream().map(r -> ConvertHelper.convert(r,PmTaskOrderDetailDTO.class)).collect(Collectors.toList()));
		}
		dto.setProducts(products);
		if(null != task.getStatus() && (task.getStatus().equals(PmTaskFlowStatus.COMPLETED.getCode()) || task.getStatus().equals(PmTaskFlowStatus.CONFIRMED.getCode()))){
			dto.setIsConfirmed((byte)1);
		} else {
			dto.setIsConfirmed((byte)0);
		}
		return dto;
	}

	@Override
	public void syncOrderDetails() {
		Integer amount = generalFormValProvider.queryAmount("EhPmTasks",null);
		Integer pageSize = 10000;
		Long pageAnchor = 0L;
		Integer loopTime = amount/pageSize + 1;

		for (int i = 0 ; i < loopTime ; i++){
			List<GeneralFormVal> list = generalFormValProvider.queryGeneralFormVals("EhPmTasks",null,pageAnchor,pageSize);
            if(list.size() > 0)
			    pageAnchor = list.get(list.size() - 1).getId();
			Map<Long,List<GeneralFormVal>> taskMap = list.stream().collect(Collectors.groupingBy(GeneralFormVal::getSourceId));
			taskMap.forEach((taskId,task) ->{
				PmTaskOrder order = new PmTaskOrder();
				order.setTaskId(taskId);
				Long productFee = 0L;
				List<PmTaskOrderDetail> products = new ArrayList<>();
				for (GeneralFormVal item : task){
					if ("USER_NAME".equals(item.getFieldName())) {

					} else if ("USER_PHONE".equals(item.getFieldName())) {

					} else if ("USER_COMPANY".equals(item.getFieldName())) {

					} else if ("USER_ADDRESS".equals(item.getFieldName())) {

					} else if ("服务费".equals(item.getFieldName())) {
						BigDecimal serviceFee = BigDecimal.valueOf(Double.valueOf(getTextString(item.getFieldValue())));
						order.setServiceFee(serviceFee.movePointRight(2).longValue());
					} else if ("物品".equals(item.getFieldName())) {
						PostApprovalFormSubformValue subFormValue = JSON.parseObject(item.getFieldValue(), PostApprovalFormSubformValue.class);
						List<PostApprovalFormSubformItemValue> array = subFormValue.getForms();
						if (array.size()!=0) {
							products = new ArrayList<>();
							for (PostApprovalFormSubformItemValue itemValue : array) {
								PmTaskOrderDetail product = new PmTaskOrderDetail();
								List<PostApprovalFormItem> values = itemValue.getValues();
								String pname = getTextString(getFormItem(values, "物品名称").getFieldValue());
								String pprice = getTextString(getFormItem(values, "单价").getFieldValue());
								String pamount = getTextString(getFormItem(values, "数量").getFieldValue());
								if(StringUtils.isNotEmpty(pname) && StringUtils.isNotEmpty(pprice) && StringUtils.isNotEmpty(pamount)){
									product.setTaskId(taskId);
									product.setProductName(pname);
									product.setProductPrice(Long.valueOf(pamount) * 100);
									product.setProductAmount(Integer.valueOf(pamount));
									productFee += product.getProductPrice() * product.getProductAmount();
									products.add(product);
								} else {
									LOGGER.info("pmtaskorder useless data,data = {}",itemValue.toString());
								}
							}
						}

					} else if ("总计".equals(item.getFieldName())) {
						BigDecimal totalamount = BigDecimal.valueOf(Double.valueOf(getTextString(item.getFieldValue())));
						order.setAmount(totalamount.movePointRight(2).longValue());
					}
				}
				order.setProductFee(productFee);
				order = pmTaskProvider.createPmTaskOrder(order);
				Long orderId = order.getId();
				if(products.size() > 0){
					products = products.stream().map(r->{
						r.setOrderId(orderId);
						return r;
					}).collect(Collectors.toList());
					pmTaskProvider.createOrderDetails(products);
				}
			});
		}
	}

	@Override
	public void clearOrderDetails() {
		this.pmTaskProvider.clearOrderDetails();
	}

	@Override
	public List<ListBizPayeeAccountDTO> listPayeeAccounts(ListPayeeAccountsCommand cmd) {
		ListBusinessUsersCommand cmdPay = new ListBusinessUsersCommand();
		if(null == cmd.getOrganizationId()){
			LOGGER.error("Invalid OrganizationId, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid OrganizationId.");
		}
		// 给支付系统的bizUserId的形式：EhOrganizations1037001
		String userPrefix = "EhOrganizations";
		cmdPay.setBizUserId(userPrefix + cmd.getOrganizationId());
		List<String> tags = new ArrayList<>();
		tags.add("0");
		if(null == cmd.getCommunityId() || cmd.getCommunityId().equals(-1L)) {
			cmdPay.setTag1s(tags);
		} else {
			tags.add(String.valueOf(cmd.getCommunityId()));
			cmdPay.setTag1s(tags);
		}
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("List biz payee accounts(request), orgnizationId={}, tags={}, cmd={}", cmd.getOrganizationId(), tags, cmdPay);
		}
		List<PayUserDTO> payUserDTOs = payService.getPayUserList(cmdPay.getBizUserId(), cmdPay.getTag1s());
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("List biz payee accounts(response), orgnizationId={}, tags={}, response={}", cmd.getOrganizationId(), tags, GsonUtil.toJson(payUserDTOs));
		}
		List<ListBizPayeeAccountDTO> result = new ArrayList<ListBizPayeeAccountDTO>();
		if(payUserDTOs != null){
			for(PayUserDTO payUserDTO : payUserDTOs) {
				ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
				// 支付系统中的用户ID
				dto.setAccountId(payUserDTO.getId());
				// 用户向支付系统注册帐号时填写的帐号名称
				dto.setAccountName(payUserDTO.getRemark());
				dto.setAccountAliasName(payUserDTO.getUserAliasName());//企业名称（认证企业）
				// 帐号类型，1-个人帐号、2-企业帐号
				Integer userType = payUserDTO.getUserType();
				if(userType != null && userType.equals(2)) {
					dto.setAccountType(OwnerType.ORGANIZATION.getCode());
				} else {
					dto.setAccountType(OwnerType.USER.getCode());
				}
				// 企业账户：0未审核 1审核通过  ; 个人帐户：0 未绑定手机 1 绑定手机
				Integer registerStatus = payUserDTO.getRegisterStatus();
				if(registerStatus != null && registerStatus.intValue() == 1) {
					dto.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
				} else {
					dto.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
				}
				result.add(dto);
			}
		}
		return result;
	}

//	@Override
//	public PreOrderDTO payBills(CreatePmTaskOrderCommand cmd) {
//
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//        PmTask task = pmTaskProvider.findTaskById(cmd.getTaskId());
//        PmTaskOrder order = pmTaskProvider.findPmTaskOrderByTaskId(cmd.getTaskId());
//        FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(task.getId(),"EhPmTasks",20100L);
//        FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
//        flowCase = tree.getLeafNodes().get(0).getFlowCase();
//        PreOrderDTO preOrderDTO = null;
//
////      费用确认修改报修单状态
////		task.setStatus(PmTaskFlowStatus.COMPLETED.getCode());
////		task.setAmount(order.getAmount());
////		pmTaskProvider.updateTask(task);
////		pmTaskSearch.feedDoc(task);
//
//        Long taskCategoryId;
//        if(flowCase.getModuleType().equals(FlowModuleType.NO_MODULE.getCode())){
//            if(999983 == namespaceId){
//                taskCategoryId = 1L;
//            }else{
//                taskCategoryId = 6L;
//            }
//        }else{
//            taskCategoryId = 9L;
//        }
//        PmTaskConfig pmTaskConfig = pmTaskProvider.findPmTaskConfigbyOwnerId(namespaceId,task.getOwnerType(),task.getOwnerId(),taskCategoryId);
////		Integer feeModel = configProvider.getIntValue(namespaceId,"pmtask.feeModel." + taskCategoryId,0);
//        if(null == pmTaskConfig.getPaymentFlag() || pmTaskConfig.getPaymentFlag().equals((byte)0)){
//            FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
//            LOGGER.info("target:"+JSONObject.toJSONString(flowCase));
//            if(null == flowCase){
//                LOGGER.error("can not find flowcase PmTaskId={}", task.getId());
//                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                        "can not find flowcase");
//            }
//            stepDTO.setFlowCaseId(flowCase.getId());
//            stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
//            stepDTO.setFlowMainId(flowCase.getFlowMainId());
//            stepDTO.setFlowVersion(flowCase.getFlowVersion());
//            stepDTO.setStepCount(flowCase.getStepCount());
//            stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//            stepDTO.setEventType(FlowEventType.STEP_MODULE.getCode());
//            flowService.processAutoStep(stepDTO);
//        }else{
//
//			PayUserDTO payUser = null;
//            if(null == pmTaskConfig.getPaymentAccount() || 0 == pmTaskConfig.getPaymentAccount()){
//                LOGGER.error("暂未绑定收款账户,ownerId={}", task.getOwnerId());
//                throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_ACCOUNT_NO_FIND,
//                        "暂未绑定收款账户");
//            } else {
//				List<Long> payUserIds = new ArrayList<>();
//				payUserIds.add(pmTaskConfig.getPaymentAccount());
//				List<PayUserDTO> payUsers = payService.listPayUsersByIds(payUserIds);
//				if(null != payUsers && payUsers.size() > 0){
//					payUser = payUsers.get(0);
//					if(0 == payUser.getRegisterStatus().intValue()){
//						LOGGER.error("暂未绑定收款账户,ownerId={}", task.getOwnerId());
//						throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_ACCOUNT_NO_FIND,
//								"暂未绑定收款账户");
//					}
//				}
//			}
//            if(null != order.getBizOrderNum()){
//                preOrderDTO = ConvertHelper.convert(order,PreOrderDTO.class);
//                ListClientSupportPayMethodCommandResponse response = payService.listClientSupportPayMethod("NS" + namespaceId,
//                        cmd.getClientAppName());
//                List<com.everhomes.pay.order.PayMethodDTO> paymentMethods = response.getPaymentMethods();
//                if (paymentMethods != null)
//                    preOrderDTO.setPayMethod(paymentMethods.stream().map(r->{
//                        PayMethodDTO convert = ConvertHelper.convert(r, PayMethodDTO.class);
//                        convert.setExtendInfo(getPayMethodExtendInfo());
//                        return convert;
//                    }).collect(Collectors.toList()));
//                return preOrderDTO;
//            }
//            //1、检查买方（付款方）是否有会员，无则创建
//            User userById = userProvider.findUserById(UserContext.currentUserId());
//            UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(userById.getId(), namespaceId);
//            String userIdenify = null;
//            if(userIdentifier != null) {
//                userIdenify = userIdentifier.getIdentifierToken();
//            }
//            PayUserDTO payUserDTO = checkAndCreatePaymentUser(userById.getId().toString(), "NS" + namespaceId.toString(), userIdenify, null, null, null);
//            if(payUserDTO != null) {
//                cmd.setPayerId(payUserDTO.getId());
//            }else {
//                LOGGER.error("payerUserId no find, cmd={}", cmd);
//                throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
//                        "payerUserId no find");
//            }
//
//            //2、收款方是否有会员，无则报错
//            Long payeeUserId = pmTaskConfig.getPaymentAccount();
//            if(payeeUserId == null) {
//                LOGGER.error("payeeUserId no find, cmd={}", cmd);
//                throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
//                        "payeeUserId no find");
//            }
//
//            //3、组装报文，发起下单请求
//            Long amount = order.getAmount();
//            cmd.setOrderId(order.getId());
//            CreateOrderCommand cmdPay = this.CreateOrderCommand(cmd);
//            cmdPay.setPayeeUserId(payeeUserId);
//            cmdPay.setAmount(amount);
//            CreateOrderRestResponse response = payService.createPurchaseOrder(cmdPay);
//            if(!response.getErrorCode().equals(200)) {
//                LOGGER.error("create order fail");
//                throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_CREATE_FAIL,
//                        "create order fail");
//            }
//
//            //4、组装支付方式
//            preOrderDTO = orderCommandResponseToDto(response.getResponse());
//            preOrderDTO.setAmount(amount);
//            preOrderDTO.setOrderId(order.getId());
//
//            //5、保存订单信息
//            saveOrderRecord(order.getId(), response.getResponse());
//
//
//        }
//        //6、返回
//        return preOrderDTO;
//
//	}

	@Override
	public PreOrderDTO payBills(CreatePmTaskOrderCommand cmd) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		PmTask task = pmTaskProvider.findTaskById(cmd.getTaskId());
		PmTaskOrder order = pmTaskProvider.findPmTaskOrderByTaskId(cmd.getTaskId());
		order.setClientAppName(cmd.getClientAppName());
		FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(task.getId(),"EhPmTasks",20100L);
		FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
		flowCase = tree.getLeafNodes().get(0).getFlowCase();
		PreOrderDTO preOrderDTO = null;

		Long taskCategoryId;
		if(flowCase.getModuleType().equals(FlowModuleType.NO_MODULE.getCode())){
			if(999983 == namespaceId){
				taskCategoryId = 1L;
			}else{
				taskCategoryId = 6L;
			}
		}else{
			taskCategoryId = 9L;
		}
		PmTaskConfig pmTaskConfig = pmTaskProvider.findPmTaskConfigbyOwnerId(namespaceId,task.getOwnerType(),task.getOwnerId(),taskCategoryId);
		if(null == pmTaskConfig.getPaymentFlag() || pmTaskConfig.getPaymentFlag().equals((byte)0)){
			FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
			LOGGER.info("target:"+JSONObject.toJSONString(flowCase));
			if(null == flowCase){
				LOGGER.error("can not find flowcase PmTaskId={}", task.getId());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"can not find flowcase");
			}
			stepDTO.setFlowCaseId(flowCase.getId());
			stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
			stepDTO.setFlowMainId(flowCase.getFlowMainId());
			stepDTO.setFlowVersion(flowCase.getFlowVersion());
			stepDTO.setStepCount(flowCase.getStepCount());
			stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
			stepDTO.setEventType(FlowEventType.STEP_MODULE.getCode());
			flowService.processAutoStep(stepDTO);
		}else{

			PayUserDTO payUser = null;
			if(null == pmTaskConfig.getPaymentAccount() || 0 == pmTaskConfig.getPaymentAccount()){
				LOGGER.error("暂未绑定收款账户,ownerId={}", task.getOwnerId());
				throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_ACCOUNT_NO_FIND,
						"暂未绑定收款账户");
			} else {
				List<Long> payUserIds = new ArrayList<>();
				payUserIds.add(pmTaskConfig.getPaymentAccount());
				List<PayUserDTO> payUsers = payService.listPayUsersByIds(payUserIds);
				if(null != payUsers && payUsers.size() > 0){
					payUser = payUsers.get(0);
					if(0 == payUser.getRegisterStatus().intValue()){
						LOGGER.error("暂未绑定收款账户,ownerId={}", task.getOwnerId());
						throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_ACCOUNT_NO_FIND,
								"暂未绑定收款账户");
					}
				}
			}
			//1、检查买方（付款方）是否有会员，无则创建
			User userById = userProvider.findUserById(UserContext.currentUserId());
			UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(userById.getId(), namespaceId);
			String userIdenify = null;
			if(userIdentifier != null) {
				userIdenify = userIdentifier.getIdentifierToken();
			}
			order.setPayerType(BusinessPayerType.USER.getCode());
			order.setPayerId(String.valueOf(userById.getId()));

			//2、收款方是否有会员，无则报错
			Long payeeUserId = pmTaskConfig.getPaymentAccount();
			if(payeeUserId == null) {
				LOGGER.error("payeeUserId no find, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAYMENT_SERVICE_CONFIG_NO_FIND,
						"payeeUserId no find");
			}
			order.setPayeeType(PaymentAccountType.fromCode(pmTaskConfig.getPaymentAccountType()));
			order.setPayeeId(String.valueOf(payeeUserId));

			CreatePurchaseOrderCommand paycmd = preparePaymentBillOrder(task,order);

			//3、组装报文，发起下单请求
			PurchaseOrderCommandResponse response = sendCreatePreOrderRequest(paycmd);

			//5、保存订单信息
			afterBillOrderCreated(order,response);

			//4、组装支付方式
			preOrderDTO = populatePreOrderDto(response);

		}
		//6、返回
		return preOrderDTO;

	}

	protected CreatePurchaseOrderCommand preparePaymentBillOrder(PmTask task, PmTaskOrder order) {
		CreatePurchaseOrderCommand preOrderCommand = new CreatePurchaseOrderCommand();

		preOrderCommand.setAmount(order.getAmount());

		String accountCode = generateAccountCode(order.getNamespaceId());
		preOrderCommand.setAccountCode(accountCode);
		preOrderCommand.setClientAppName(order.getClientAppName());

		preOrderCommand.setBusinessOrderType(BusinessOrderType.PMTASK_CODE.getCode());
		// 移到统一订单系统完成
		// String BizOrderNum  = getOrderNum(orderId, OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
		// preOrderCommand.setBizOrderNum(BizOrderNum);

		preOrderCommand.setBusinessPayerType(order.getPayerType());
		preOrderCommand.setBusinessPayerId(order.getPayerId());
		String businessPayerParams = getBusinessPayerParams(order);
		preOrderCommand.setBusinessPayerParams(businessPayerParams);

		preOrderCommand.setPaymentPayeeType(order.getPayeeType());
		preOrderCommand.setPaymentPayeeId(Long.valueOf(order.getPayeeId()));

//		preOrderCommand.setExtendInfo(genPaymentExtendInfo(cmd, billGroup));
//		preOrderCommand.setPaymentParams(getPaymentParams(cmd, billGroup));
		preOrderCommand.setExpirationMillis(EXPIRE_TIME_15_MIN_IN_SEC);
		preOrderCommand.setCallbackUrl(getPayCallbackUrl());
//		preOrderCommand.setExtendInfo(cmd.getExtendInfo());
		preOrderCommand.setGoodsName(String.valueOf(OrderType.OrderTypeEnum.PMTASK_CODE.getMsg()));
		preOrderCommand.setGoodsDescription(null);
		preOrderCommand.setIndustryName(null);
		preOrderCommand.setIndustryCode(null);
		preOrderCommand.setSourceType(SourceType.MOBILE.getCode());
		preOrderCommand.setOrderRemark1(String.valueOf(OrderType.OrderTypeEnum.PMTASK_CODE.getMsg()));
		preOrderCommand.setOrderRemark2(String.valueOf(order.getId()));
		preOrderCommand.setOrderRemark3(String.valueOf(task.getOwnerId()));
		preOrderCommand.setOrderRemark4(null);
		preOrderCommand.setOrderRemark5(null);
		String systemId = configProvider.getValue(UserContext.getCurrentNamespaceId(), "gorder.system_id", "");
		preOrderCommand.setBusinessSystemId(Long.parseLong(systemId));

		return preOrderCommand;
	}

	private String generateAccountCode(Integer namespaceId) {
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("NS");
		strBuilder.append(namespaceId);

		return strBuilder.toString();
	}

	/**
	 * 获取付款方的额外参数信息，比如当个人帐号下单时，必须绑定一个手机号，此时手机号为额外信息；
	 * 如果是企业作为付款方，可能需要更多额外信息（目前未实现企业付款）；
	 * @return 取付款方的额外参数信息
	 */
	protected String getBusinessPayerParams(PmTaskOrder order) {
		OwnerType businessPayerType = OwnerType.fromCode(order.getPayerType());
		if(businessPayerType == OwnerType.ORGANIZATION) {
			// TODO: 未考虑企业帐号支付，先全部认为是个人帐号支付
		}
		businessPayerType = OwnerType.USER;

		// 如果参数中的付款方ID不正确，则使用当前用户ID
		String businessPayerIdStr = order.getPayerId();
		Long businessPayerId = UserContext.currentUserId();
		try {
			businessPayerId = Long.parseLong(businessPayerIdStr);
		} catch (Exception e) {
			// 不打印堆栈，因为businessPayerIdStr很可能没有
			LOGGER.error("Payer user id invalid in parameter, current user id will be used, orgPayerId={}, currentUserId={}", businessPayerIdStr, businessPayerId);
		}

		UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId, order.getNamespaceId());
		String buyerPhone = null;
		if(buyerIdentifier != null) {
			buyerPhone = buyerIdentifier.getIdentifierToken();
		}
		// 找不到手机号则默认一个
		if(buyerPhone == null || buyerPhone.trim().length() == 0) {
			buyerPhone = configProvider.getValue(UserContext.getCurrentNamespaceId(), "gorder.default.personal_bind_phone", "");
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("businessPayerPhone", buyerPhone);
		return StringHelper.toJsonString(map);
	}

	private String getPayCallbackUrl() {
		String configKey = "pay.v2.callback.url.pmtask";
		String backUrl = configProvider.getValue(UserContext.getCurrentNamespaceId(), configKey, "");
		if(backUrl == null || backUrl.trim().length() == 0) {
			LOGGER.error("Payment callback url empty, configKey={}", configKey);
			throw RuntimeErrorException.errorWith(PayServiceErrorCode.SCOPE, PayServiceErrorCode.ERROR_PAY_CALLBACK_URL_EMPTY,
					"Payment callback url empty");
		}

		if(!backUrl.toLowerCase().startsWith("http")) {
			String homeUrl = configProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
			backUrl = homeUrl + CONTEXT_PATH + backUrl;
		}

		return backUrl;
	}

	protected PurchaseOrderCommandResponse sendCreatePreOrderRequest(CreatePurchaseOrderCommand createOrderCommand) {
		CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createOrderCommand);
		if(!checkOrderRestResponseIsSuccess(createOrderResp)) {
			String scope = OrderErrorCode.SCOPE;
			int code = OrderErrorCode.ERROR_CREATE_ORDER_FAILED;
			String description = "Failed to create order";
			if(createOrderResp != null) {
				code = (createOrderResp.getErrorCode() == null) ? code : createOrderResp.getErrorCode()  ;
				scope = (createOrderResp.getErrorScope() == null) ? scope : createOrderResp.getErrorScope();
				description = (createOrderResp.getErrorDescription() == null) ? description : createOrderResp.getErrorDescription();
			}
			throw RuntimeErrorException.errorWith(scope, code, description);
		}

		PurchaseOrderCommandResponse orderCommandResponse = createOrderResp.getResponse();

		return orderCommandResponse;
	}

	/*
 * 由于从支付系统里回来的CreateOrderRestResponse有可能没有errorScope，故不能直接使用CreateOrderRestResponse.isSuccess()来判断，
   CreateOrderRestResponse.isSuccess()里会对errorScope进行比较
 */
	private boolean checkOrderRestResponseIsSuccess(CreatePurchaseOrderRestResponse response){
		if(response != null && response.getErrorCode() != null
				&& (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
			return true;
		return false;
	}

	protected void afterBillOrderCreated(PmTaskOrder order, PurchaseOrderCommandResponse orderResponse) {
		OrderCommandResponse payResponse = orderResponse.getPayResponse();
		PmTaskOrder record = this.pmTaskProvider.findPmTaskOrderById(order.getId());
		record.setPayOrderId(orderResponse.getOrderId());
		record.setBizOrderNum(payResponse.getBizOrderNum());
		record.setPayOrderId(payResponse.getOrderId());
		record.setOrderCommitNonce(payResponse.getOrderCommitNonce());
		record.setOrderCommitTimestamp(payResponse.getOrderCommitTimestamp());
		record.setOrderCommitToken(payResponse.getOrderCommitToken());
		record.setOrderCommitUrl(payResponse.getOrderCommitUrl());
		record.setPayInfo(payResponse.getPayInfo());
		this.pmTaskProvider.updatePmTaskOrder(record);
	}

	private PreOrderDTO populatePreOrderDto(PurchaseOrderCommandResponse orderResponse){
		OrderCommandResponse orderCommandResponse = orderResponse.getPayResponse();
		PreOrderDTO dto = ConvertHelper.convert(orderCommandResponse, PreOrderDTO.class);

		List<PayMethodDTO> payMethods = new ArrayList<>();//业务系统自己的支付方式格式
		List<com.everhomes.pay.order.PayMethodDTO> bizPayMethods = orderCommandResponse.getPaymentMethods();//支付系统传回来的支付方式
		String format = "{\"getOrderInfoUrl\":\"%s\"}";
		for(com.everhomes.pay.order.PayMethodDTO bizPayMethod : bizPayMethods) {
			PayMethodDTO payMethodDTO = new PayMethodDTO();//支付方式
			payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
			payMethodDTO.setExtendInfo(String.format(format, orderCommandResponse.getOrderPaymentStatusQueryUrl()));
			String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
			payMethodDTO.setPaymentLogo(paymentLogo);
			payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
			PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
			com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
			if(bizPaymentParamsDTO != null) {
				paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
			}
			payMethodDTO.setPaymentParams(paymentParamsDTO);
			payMethods.add(payMethodDTO);
		}
		dto.setPayMethod(payMethods);
		dto.setOrderId(orderResponse.getOrderId());//获取的是统一订单的id

		dto.setExpiredIntervalTime(orderCommandResponse.getExpirationMillis());
		if(orderResponse.getAmount() != null) {
			dto.setAmount(orderResponse.getAmount().longValue());
		}
		return dto;
	}

	public void payNotify(OrderPaymentNotificationCommand cmd) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("payNotify-command=" + GsonUtil.toJson(cmd));
		}
		if(cmd == null) {
			LOGGER.error("payNotify fail, cmd={}", cmd);
		}
		//检查订单是否存在
		PmTaskOrder paymentBillOrder = pmTaskProvider.findPmTaskOrderByBizOrderNum(cmd.getBizOrderNum());
		if(paymentBillOrder == null){
			LOGGER.error("can not find order record by BizOrderNum={}", cmd.getBizOrderNum());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"can not find order record");
		}

		GetPurchaseOrderCommand getPurchaseOrderCommand = new GetPurchaseOrderCommand();
		String systemId = configProvider.getValue(UserContext.getCurrentNamespaceId(), "gorder.system_id", "");
		getPurchaseOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
		String accountCode = generateAccountCode(UserContext.getCurrentNamespaceId());
		getPurchaseOrderCommand.setAccountCode(accountCode);
		getPurchaseOrderCommand.setBusinessOrderNumber(cmd.getBizOrderNum());

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("payNotify-GetPurchaseOrderCommand=" + GsonUtil.toJson(getPurchaseOrderCommand));
		}
		GetPurchaseOrderRestResponse response = orderService.getPurchaseOrder(getPurchaseOrderCommand);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("payNotify-getPurchaseOrder response=" + GsonUtil.toJson(response));
		}
		if(response == null || !response.getErrorCode().equals(200)) {
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.PAYMENT_ORDER_NOT_EXIST,
					"PayNotify getPurchaseOrder by bizOrderNum is error!");
		}
		PurchaseOrderDTO purchaseOrderDTO = response.getResponse();
		if(purchaseOrderDTO != null) {
			com.everhomes.pay.order.OrderType orderType = com.everhomes.pay.order.OrderType.fromCode(purchaseOrderDTO.getPaymentOrderType());
			if(orderType != null) {
				switch (orderType) {
					case PURCHACE:
						if(purchaseOrderDTO.getPaymentStatus() == PurchaseOrderPaymentStatus.PAID.getCode()){
							//支付成功
							paySuccess(purchaseOrderDTO);
						}
						break;
					default:
						LOGGER.error("unsupport orderType, orderType={}, cmd={}", orderType.getCode(), StringHelper.toJsonString(cmd));
				}
			}else {
				LOGGER.error("orderType is null, cmd={}", StringHelper.toJsonString(cmd));
			}
		}


	}

	public void paySuccess(PurchaseOrderDTO purchaseOrderDTO) {
		LOGGER.error("default payment success call back, purchaseOrderDTO={}", purchaseOrderDTO);

		String BizOrderNum = purchaseOrderDTO.getBusinessOrderNumber();
		Integer idx = BizOrderNum.indexOf(OrderType.OrderTypeEnum.PMTASK_CODE.getV2code());
		if(idx > -1){
//			Long orderId = Long.valueOf(BizOrderNum.substring(idx + 3));
			PmTaskOrder order = pmTaskProvider.findPmTaskOrderByBizOrderNum(BizOrderNum);
			order.setStatus((byte)1);
			pmTaskProvider.updatePmTaskOrder(order);

			FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
			FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(order.getTaskId(),"EhPmTasks",20100L);
			FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
			flowCase = tree.getLeafNodes().get(0).getFlowCase();
//			LOGGER.info("target:"+JSONObject.toJSONString(flowCase));
			if(null == flowCase){
				LOGGER.error("can not find flowcase PmTaskId={}", order.getTaskId());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"can not find flowcase");
			}
			stepDTO.setFlowCaseId(flowCase.getId());
			stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
			stepDTO.setFlowMainId(flowCase.getFlowMainId());
			stepDTO.setFlowVersion(flowCase.getFlowVersion());
			stepDTO.setStepCount(flowCase.getStepCount());
			stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
			stepDTO.setEventType(FlowEventType.STEP_MODULE.getCode());
			flowService.processAutoStep(stepDTO);
		}

	}

//	@Override
//	public void payNotify(OrderPaymentNotificationCommand cmd) {
//        if(LOGGER.isDebugEnabled()) {
//            LOGGER.debug("payNotify-command=" + GsonUtil.toJson(cmd));
//        }
//        //if(cmd == null || cmd.getPaymentErrorCode() != "200") {
//        if(cmd == null) {
//            LOGGER.error("payNotify fail, cmd={}", cmd);
//        }
//        //检查订单是否存在
////        PaymentOrderRecord orderRecord = payProvider.findOrderRecordByOrderNum(cmd.getBizOrderNum());
//		PmTaskOrder orderRecord = pmTaskProvider.findPmTaskOrderByBizOrderNum(cmd.getBizOrderNum());
//        if(orderRecord == null){
//            LOGGER.error("can not find order record by BizOrderNum={}", cmd.getBizOrderNum());
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "can not find order record");
//        }
//        //此处将orderId设置成业务系统的orderid，方便业务调用。原orderId为支付系统的orderid，业务不需要知道。
//        cmd.setOrderId(orderRecord.getId());
//        SrvOrderPaymentNotificationCommand srvCmd = ConvertHelper.convert(cmd, SrvOrderPaymentNotificationCommand.class);
//        com.everhomes.pay.order.OrderType orderType = com.everhomes.pay.order.OrderType.fromCode(cmd.getOrderType());
//        if(orderType != null) {
//            switch (orderType) {
//                case PURCHACE:
//                    if(cmd.getPaymentStatus()== OrderPaymentStatus.SUCCESS.getCode()){
//                        //支付成功
//						String BizOrderNum = cmd.getBizOrderNum();
//						Integer idx = BizOrderNum.indexOf(OrderType.OrderTypeEnum.PMTASK_CODE.getV2code());
//						if(idx > -1){
//							Long orderId = Long.valueOf(BizOrderNum.substring(idx + 3));
//							PmTaskOrder order = pmTaskProvider.findPmTaskOrderById(orderId);
//							order.setStatus((byte)1);
//							pmTaskProvider.updatePmTaskOrder(order);
//
//							FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
//							FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(order.getTaskId(),"EhPmTasks",20100L);
//							FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
//							flowCase = tree.getLeafNodes().get(0).getFlowCase();
//							LOGGER.info("target:"+JSONObject.toJSONString(flowCase));
//							if(null == flowCase){
//								LOGGER.error("can not find flowcase PmTaskId={}", order.getTaskId());
//								throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//										"can not find flowcase");
//							}
//							stepDTO.setFlowCaseId(flowCase.getId());
//							stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
//							stepDTO.setFlowMainId(flowCase.getFlowMainId());
//							stepDTO.setFlowVersion(flowCase.getFlowVersion());
//							stepDTO.setStepCount(flowCase.getStepCount());
//							stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
//							stepDTO.setEventType(FlowEventType.STEP_MODULE.getCode());
//							flowService.processAutoStep(stepDTO);
//						}
//                    }
////                    if(cmd.getPaymentStatus()==OrderPaymentStatus.FAILED.getCode()){
////                        //支付失败
////                        handler.payFail(srvCmd);
////                    }
//                    break;
////                case REFUND:
////                    if(cmd.getPaymentStatus()== OrderPaymentStatus.SUCCESS.getCode()){
////                        //退款成功
////                        handler.refundSuccess(srvCmd);
////                    }
////                    if(cmd.getPaymentStatus()==OrderPaymentStatus.FAILED.getCode()){
////                        //退款失败
////                        handler.refundFail(srvCmd);
////                    }
////                    break;
//                default:
//                    LOGGER.error("unsupport orderType, orderType={}, cmd={}", orderType.getCode(), StringHelper.toJsonString(cmd));
//            }
//        }else {
//            LOGGER.error("orderType is null, cmd={}", StringHelper.toJsonString(cmd));
//        }
//	}


	private void exportTaskStatByCategory(GetTaskStatCommand cmd, Sheet sheet){
	    List<PmTaskStatSubDTO> datalist = this.getStatByCategory(cmd);
        List<String> titles = new ArrayList<>();
		datalist.forEach(elem ->{
			if (!titles.contains(elem.getType())){
				titles.add(elem.getType());
			}
		});
		Map<Long,Map<String,Integer>> datamap = new HashMap<>();
		datalist.forEach(elem->{
            if(datamap.keySet().contains(elem.getOwnerId())) {
                Map<String, Integer> elemmap = datamap.get(elem.getOwnerId());
                elemmap.put(elem.getType(), elem.getTotal());
            } else {
                Map<String, Integer> elemmap = new HashMap<>();
                elemmap.put(elem.getType(),elem.getTotal());
                datamap.put(elem.getOwnerId(),elemmap);
            }
        });
		Row row = sheet.createRow(0);
		int columnnum = 0;
		int rownum = 1;
		List<String> tableHead = new LinkedList<>();
		tableHead.add("序号");
		tableHead.add("項目名稱");
		tableHead.addAll(titles);
		tableHead.add("合计");
		for(String th:tableHead){
			row.createCell(columnnum++).setCellValue(th);
		}
		for (Map.Entry<Long, Map<String, Integer>> elem : datamap.entrySet()) {
			Row temprow = sheet.createRow(rownum);
			temprow.createCell(0).setCellValue(rownum++);
			Community community = communityProvider.findCommunityById(elem.getKey());
			if(null != community)
				temprow.createCell(1).setCellValue(community.getName());
			else
				temprow.createCell(1).setCellValue("");
			int n = 2;
			int sum = 0;
			for (String title: titles) {
				int amount = 0;
				if(null != elem.getValue().get(title)) {
					amount = elem.getValue().get(title);
				}
				sum += amount;
				temprow.createCell(n++).setCellValue(amount);
			}
			temprow.createCell(n).setCellValue(sum);
		}
	}

	private void exportTaskStatByCreator(GetTaskStatCommand cmd,Sheet sheet){
		List<PmTaskStatDTO> datalist = this.getStatByCreator(cmd);

		int rownum = 0;
		Row row = sheet.createRow(rownum++);
		List<String> tableHead = new LinkedList<>();
		tableHead.add("序号");
		tableHead.add("项目名称");
		tableHead.add("用户发起");
		tableHead.add("员工代发");
		tableHead.add("合计");
		int colnum = 0;
		for(String th:tableHead){
			row.createCell(colnum++).setCellValue(th);
		}
		for (PmTaskStatDTO bean : datalist) {
			Row tempRow = sheet.createRow(rownum);
			tempRow.createCell(0).setCellValue(rownum++);
			tempRow.createCell(1).setCellValue(null == bean.getOwnerName() ? "" : bean.getOwnerName());
			tempRow.createCell(2).setCellValue(bean.getInit());
			tempRow.createCell(3).setCellValue(bean.getAgent());
			tempRow.createCell(4).setCellValue(bean.getTotal());
		}

	}
	private void exportTaskStatByStatus(GetTaskStatCommand cmd,Sheet sheet){
		List<PmTaskStatDTO> datalist = this.getStatByStatus(cmd);

		int rownum = 0;
		Row row = sheet.createRow(rownum++);
		List<String> tableHead = new LinkedList<>();
		tableHead.add("序号");
		tableHead.add("项目名称");
		tableHead.add("处理完成");
		tableHead.add("处理中");
		tableHead.add("已取消");
		tableHead.add("合计");
		int colnum = 0;
		for(String th:tableHead){
			row.createCell(colnum++).setCellValue(th);
		}
		for (PmTaskStatDTO bean : datalist) {
			Row tempRow = sheet.createRow(rownum);
			tempRow.createCell(0).setCellValue(rownum++);
			tempRow.createCell(1).setCellValue(null == bean.getOwnerName() ? "" : bean.getOwnerName());
			tempRow.createCell(2).setCellValue(bean.getComplete());
			tempRow.createCell(3).setCellValue(bean.getProcessing());
			tempRow.createCell(4).setCellValue(bean.getClose());
			tempRow.createCell(5).setCellValue(bean.getTotal());
		}

	}
	private void exportTaskStatByArea(GetTaskStatCommand cmd,Sheet sheet){
		List<PmTaskStatSubDTO> datalist = this.getStatByArea(cmd);

		int rownum = 0;
		int sum = 0;
		Row row = sheet.createRow(rownum++);
		List<String> tableHead = new LinkedList<>();
		tableHead.add("序号");
		tableHead.add("楼栋名称");
		tableHead.add("提单数量");
		int colnum = 0;
		for(String th:tableHead){
			row.createCell(colnum++).setCellValue(th);
		}
		for (PmTaskStatSubDTO bean : datalist) {
			Row tempRow = sheet.createRow(rownum);
			tempRow.createCell(0).setCellValue(rownum++);
			tempRow.createCell(1).setCellValue(null == bean.getType() ? "" : bean.getType());
			tempRow.createCell(2).setCellValue(bean.getTotal());
			sum += bean.getTotal();
		}
		Row lastRow	= sheet.createRow(rownum++);
		lastRow.createCell(1).setCellValue("合计");
		lastRow.createCell(2).setCellValue(sum);
	}

	private void exportTaskEval(GetTaskStatCommand cmd,Sheet sheet){
	    GetEvalStatCommand cmd1 = ConvertHelper.convert(cmd,GetEvalStatCommand.class);
	    cmd1.setBeginTime(cmd.getDateStart());
	    cmd1.setEndTime(cmd.getDateEnd());
	    cmd1.setOwnerType("PMTASK");
		if (9L == cmd.getAppId()) {
			cmd1.setModuleType("suggestion");
		} else {
			cmd1.setModuleType("any-module");
		}
	    cmd1.setProjectId(cmd.getOwnerId());
	    cmd1.setProjectType("EhCommunities");
	    List<PmTaskEvalStatDTO> datalist = this.getEvalStat(cmd1);

		int rownum = 0;
		Row row = sheet.createRow(rownum++);
		List<String> tableHead = new LinkedList<>();
		tableHead.add("序号");
		tableHead.add("评价项");
		tableHead.add("非常好(5分)");
		tableHead.add("很好(4分)");
		tableHead.add("一般(3分)");
		tableHead.add("很差(2分)");
		tableHead.add("非常差(1分)");
		tableHead.add("合计");
		tableHead.add("平均分");
		int colnum = 0;
		for(String th:tableHead){
			row.createCell(colnum++).setCellValue(th);
		}
		for (PmTaskEvalStatDTO bean : datalist) {
			Row tempRow = sheet.createRow(rownum);
			tempRow.createCell(0).setCellValue(rownum++);
			tempRow.createCell(1).setCellValue(bean.getEvalName());
			tempRow.createCell(2).setCellValue(bean.getAmount5());
			tempRow.createCell(3).setCellValue(bean.getAmount4());
			tempRow.createCell(4).setCellValue(bean.getAmount3());
			tempRow.createCell(5).setCellValue(bean.getAmount2());
			tempRow.createCell(6).setCellValue(bean.getAmount1());
			tempRow.createCell(7).setCellValue(bean.getTotalAmount());
			tempRow.createCell(8).setCellValue(bean.getEvalAvg());
		}
    }

	private List<PmTask> getSurveyData(GetTaskStatCommand cmd){
		List<Long> ownerIds = this.getOwnerIds(cmd);
//		模块权限校验
		if(cmd.getCurrentPMId()!=null && cmd.getOriginId()!=null){
			ownerIds.forEach(r -> userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2010020190L, cmd.getOriginId(), null,r));
		}
//		查询数据
//		List<Category> categories = categoryProvider.listTaskCategoriesByparentId(cmd.getNamespaceId(),cmd.getOwnerType(),ownerIds,cmd.getAppId());
//		List<Long> categoryIds = categories.stream().map(r -> r.getId()).collect(Collectors.toList());
//		List<PmTask> list = pmTaskProvider.listTaskByStat(cmd.getNamespaceId(),ownerIds,new Timestamp(cmd.getDateStart()),new Timestamp(cmd.getDateEnd()),categoryIds);

		SearchTasksCommand cmd1 = new SearchTasksCommand();
		cmd1.setNamespaceId(cmd.getNamespaceId());
		cmd1.setOwnerType(cmd.getOwnerType());
		cmd1.setOwnerId(cmd.getOwnerId());
		cmd1.setStartDate(cmd.getDateStart());
		cmd1.setEndDate(cmd.getDateEnd());
		cmd1.setTaskCategoryId(cmd.getAppId());
		cmd1.setAppId(cmd.getOriginId());
		cmd1.setCurrentPMId(cmd.getCurrentPMId());
		cmd1.setCurrentProjectId(cmd.getOwnerId());
		cmd1.setPageSize(99999);
		List<PmTaskDTO> list = this.searchTasks(cmd1).getRequests();
		return null != list ? list.stream().map(r -> ConvertHelper.convert(r,PmTask.class)).collect(Collectors.toList()) : new ArrayList<>();
	}

	/**
	 * 当ownerId为-1是查找用户所有项目
	 * @param cmd
	 * @return
	 */
	private List<Long> getOwnerIds(GetTaskStatCommand cmd){
		List<Long> ownerIds = new ArrayList<>();
		if(null == cmd.getOwnerId() || -1L == cmd.getOwnerId()){
			ListUserRelatedProjectByModuleCommand cmd1 = new ListUserRelatedProjectByModuleCommand();
			cmd1.setModuleId(20100L);
//			cmd1.setAppId(cmd.getAppId());
			cmd1.setOrganizationId(cmd.getCurrentPMId());
			List<ProjectDTO> dtos = serviceModuleService.listUserRelatedProjectByModuleId(cmd1);
			ownerIds.addAll(dtos.stream().map(ProjectDTO::getProjectId).collect(Collectors.toList()));
		} else {
			ownerIds.add(cmd.getOwnerId());
		}
		return ownerIds;
	}

	//检查买方（付款方）会员，无则创建
	private PayUserDTO checkAndCreatePaymentUser(String userId, String accountCode,String userIdenify, String tag1, String tag2, String tag3){
		PayUserDTO payUserDTO = new PayUserDTO();
		String payerid = OwnerType.USER.getCode()+userId;
		//根据tag查询支付帐号
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("getPayUserList payerid={}", payerid);
		}
		List<PayUserDTO> payUserDTOs = payService.getPayUserList(payerid);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("getPayUserList response={}", payUserDTOs);
		}
		if(payUserDTOs == null || payUserDTOs.size() == 0){
			//创建个人账号
			LOGGER.info("createPersonalPayUserIfAbsent payerid = {}, accountCode = {}, userIdenify={}",payerid,accountCode,userIdenify);
			payUserDTO = payService.createPersonalPayUserIfAbsent(payerid, accountCode);
			if(payUserDTO==null){
				throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_USER_ACCOUNT,
						"创建个人付款账户失败");
			}
			String s = payService.bandPhone(payUserDTO.getId(), userIdenify);//绑定手机号
		}else {
			payUserDTO = payUserDTOs.get(0);
		}
		return payUserDTO;
	}
	private CreateOrderCommand CreateOrderCommand(CreatePmTaskOrderCommand cmd){

		CreateOrderCommand createOrderCmd = new CreateOrderCommand();
		//业务系统中的订单号，请在整个业务系统中约定好唯一规则；
		String BizOrderNum  = getOrderNum(cmd.getOrderId(),OrderType.OrderTypeEnum.PMTASK_CODE.getPycode());
		LOGGER.info("BizOrderNum is = {} "+BizOrderNum);
		createOrderCmd.setBizOrderNum(BizOrderNum);
		createOrderCmd.setClientAppName(cmd.getClientAppName());
		createOrderCmd.setPayerUserId(cmd.getPayerId());//付款方ID
//		createOrderCmd.setPaymentParams(flattenMap);
		createOrderCmd.setPaymentType(cmd.getPaymentType());
//		设置回调地址
		String homeUrl = configProvider.getValue(0,"home.url", "");
		String backUri = configProvider.getValue(0,"pay.v2.callback.url.pmtask", "");
		String backUrl = homeUrl + CONTEXT_PATH + backUri;
		createOrderCmd.setBackUrl(backUrl);
//		createOrderCmd.setExtendInfo(cmd.getExtendInfo());
		createOrderCmd.setGoodsName("物业缴费");
		createOrderCmd.setGoodsDescription(null);
		createOrderCmd.setIndustryName(null);
		createOrderCmd.setIndustryCode(null);
		createOrderCmd.setSourceType(SourceType.MOBILE.getCode());
		createOrderCmd.setOrderRemark1(String.valueOf(OrderType.OrderTypeEnum.PMTASK_CODE.getPycode()));
		createOrderCmd.setOrderRemark2(String.valueOf(cmd.getOrderId()));
		createOrderCmd.setOrderRemark3(String.valueOf(cmd.getOwnerId()));
		createOrderCmd.setOrderRemark4(null);
		createOrderCmd.setOrderRemark5(null);
		if(UserContext.getCurrentNamespaceId() != null) {
			createOrderCmd.setAccountCode("NS" + UserContext.getCurrentNamespaceId().toString());
		}
		return createOrderCmd;
	}
	private String getOrderNum(Long orderId, String orderType) {
		String v2code = OrderType.OrderTypeEnum.getV2codeByPyCode(orderType);
		DecimalFormat df = new DecimalFormat("00000000000000000");
		String orderIdStr = df.format(orderId);
		if(orderIdStr!=null && orderIdStr.length()>17){
			orderIdStr = orderIdStr.substring(2);
		}
		return v2code+orderIdStr;
	}

	private PreOrderDTO orderCommandResponseToDto(OrderCommandResponse orderCommandResponse){
		PreOrderDTO dto = ConvertHelper.convert(orderCommandResponse, PreOrderDTO.class);
		List<PayMethodDTO> payMethods = new ArrayList<>();//业务系统自己的支付方式格式
		List<com.everhomes.pay.order.PayMethodDTO> bizPayMethods = orderCommandResponse.getPaymentMethods();//支付系统传回来的支付方式
		String format = "{\"getOrderInfoUrl\":\"%s\"}";
		for(com.everhomes.pay.order.PayMethodDTO bizPayMethod : bizPayMethods) {
			PayMethodDTO payMethodDTO = new PayMethodDTO();//支付方式
			payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
			payMethodDTO.setExtendInfo(String.format(format, orderCommandResponse.getOrderPaymentStatusQueryUrl()));
			String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
			payMethodDTO.setPaymentLogo(paymentLogo);
			payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
			PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
			com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
			if(bizPaymentParamsDTO != null) {
				paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
			}
			payMethodDTO.setPaymentParams(paymentParamsDTO);
			payMethods.add(payMethodDTO);
		}
		dto.setPayMethod(payMethods);
		dto.setExpiredIntervalTime(orderCommandResponse.getExpirationMillis());
		return dto;
	}

	private void saveOrderRecord(Long orderId, OrderCommandResponse orderCommandResponse) {
		PmTaskOrder record = this.pmTaskProvider.findPmTaskOrderById(orderId);
		record.setBizOrderNum(orderCommandResponse.getBizOrderNum());
		record.setPayOrderId(orderCommandResponse.getOrderId());
		record.setOrderCommitNonce(orderCommandResponse.getOrderCommitNonce());
		record.setOrderCommitTimestamp(orderCommandResponse.getOrderCommitTimestamp());
		record.setOrderCommitToken(orderCommandResponse.getOrderCommitToken());
		record.setOrderCommitUrl(orderCommandResponse.getOrderCommitUrl());
		record.setPayInfo(orderCommandResponse.getPayInfo());
		this.pmTaskProvider.updatePmTaskOrder(record);
	}

	private PostApprovalFormItem getFormItem(List<PostApprovalFormItem> values,String name){
		for (PostApprovalFormItem p:values)
			if (p.getFieldName().equals(name))
				return p;
		return null;
	}

	private String getTextString(String json){
		if (StringUtils.isEmpty(json))
			return "";
		return JSONObject.parseObject(json).getString("text");
	}

	private void FlowStepOrderDetailsOperate(Long taskId, Long operatorId, String operatorName, String stepType){
		FlowCase mainFlowCase = this.flowCaseProvider.findFlowCaseByReferId(taskId,"EhPmTasks",20100L);
		FlowCaseTree flowCaseTrees = flowService.getProcessingFlowCaseTree(mainFlowCase.getId());
		List<FlowCaseTree> flowCases = flowCaseTrees.getLeafNodes();
		FlowCase flowCase = flowCases.get(flowCases.size() - 1).getFlowCase();
//		LOGGER.info("nextStep:"+JSONObject.toJSONString(flowCase));
		FlowAutoStepDTO dto = new FlowAutoStepDTO();
		dto.setAutoStepType(stepType);
		dto.setEventType(FlowEventType.STEP_MODULE.getCode());

		dto.setFlowCaseId(flowCase.getId());
		dto.setFlowMainId(flowCase.getFlowMainId());
		dto.setFlowNodeId(flowCase.getCurrentNodeId());
		dto.setFlowVersion(flowCase.getFlowVersion());
		dto.setStepCount(flowCase.getStepCount());

		FlowEventLog log = new FlowEventLog();
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(flowCase.getFlowMainId());
		log.setFlowVersion(flowCase.getFlowVersion());
		log.setNamespaceId(flowCase.getNamespaceId());
		log.setFlowNodeId(flowCase.getCurrentNodeId());
		log.setFlowCaseId(flowCase.getId());
		log.setStepCount(flowCase.getStepCount());
		log.setSubjectId(0L);
		log.setParentId(0L);
		log.setFlowUserId(operatorId);
		log.setFlowUserName(operatorName);
		log.setLogType(FlowLogType.NODE_TRACKER.getCode());
		log.setButtonFiredStep(FlowStepType.NO_STEP.getCode());
		log.setTrackerApplier(1L); // 申请人可以看到此条log，为0则看不到
		log.setTrackerProcessor(1L);// 处理人可以看到此条log，为0则看不到

		PmTaskOrder order = this.pmTaskProvider.findPmTaskOrderByTaskId(taskId);
		List<PmTaskOrderDetail> products = this.pmTaskProvider.findOrderDetailsByTaskId(null,null,null,taskId);
		String content = "";
		if(null != order){
			content += "本次服务的费用清单如下，请进行确认\n";
			BigDecimal total = BigDecimal.valueOf(order.getAmount());
			content += "总计："+total.movePointLeft(2).toString()+"元\n";
			BigDecimal serviceFee = BigDecimal.valueOf(order.getServiceFee());
			content += "服务费："+serviceFee.movePointLeft(2).toString()+"元\n";
			BigDecimal productFee = BigDecimal.valueOf(order.getProductFee());
			content += "物品费："+ productFee.movePointLeft(2) +"元\n";
			if(null != products && products.size() > 0){
				content += "物品费详情：\n";
				for (PmTaskOrderDetail r : products) {
					BigDecimal price = BigDecimal.valueOf(r.getProductPrice());
					BigDecimal amount = BigDecimal.valueOf(r.getProductAmount());
					content += r.getProductName() + "：";
					content += price.multiply(amount).movePointLeft(2).toString() + "元";
					content += "（" + price.movePointLeft(2).toString() + "元*" + amount.intValue() + "）\n";
				}
			}
			content += "如对上述费用有疑义请附言说明";
		}else{
			content = "本次服务没有产生维修费";
		}

		log.setLogContent(content);
		List<FlowEventLog> logList = new ArrayList<>(1);
		logList.add(log);
		dto.setEventLogs(logList);

		flowService.processAutoStep(dto);
	}

	private boolean isEbeiCategory(Long categoryId){
		if(null != categoryId && categoryId.equals(EbeiPmTaskHandle.EBEI_TASK_CATEGORY)){
			return true;
		}
		return false;
	}

	private String getPayMethodExtendInfo(){
		String payV2HomeUrl = configProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.home.url", "");
		String getOrderInfoUri = configProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.orderPaymentStatusQueryUri", "");

		String format = "{\"getOrderInfoUrl\":\"%s\"}";
		return String.format(format, payV2HomeUrl+getOrderInfoUri);
	}

}
