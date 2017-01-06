package com.everhomes.pmtask;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.pmtask.AttachmentDescriptor;
import com.everhomes.rest.pmtask.PmTaskAddressType;
import com.everhomes.rest.pmtask.PmTaskAttachmentType;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.pmtask.PmTaskNotificationTemplateCode;
import com.everhomes.rest.pmtask.PmTaskOperateType;
import com.everhomes.rest.pmtask.PmTaskSourceType;
import com.everhomes.rest.pmtask.PmTaskStatus;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
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
	
}
