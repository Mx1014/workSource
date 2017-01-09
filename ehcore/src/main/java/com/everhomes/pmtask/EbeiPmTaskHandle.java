package com.everhomes.pmtask;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.pmtask.ebei.EbeiPmTaskDTO;
import com.everhomes.pmtask.ebei.EbeiResponseEntity;
import com.everhomes.pmtask.ebei.EbeiServiceType;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.pmtask.AttachmentDescriptor;
import com.everhomes.rest.pmtask.CancelTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListAllTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.PmTaskAddressType;
import com.everhomes.rest.pmtask.PmTaskAttachmentType;
import com.everhomes.rest.pmtask.PmTaskDTO;
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

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.EBEI)
public class EbeiPmTaskHandle implements PmTaskHandle{
	
	public static final String CATEGORY_SEPARATOR = "/";
	
	private static final String LIST_SERVICE_TYPE = "/rest/crmFeedBackInfoJoin/serviceTypeList";
	private static final String CREATE_TASK = "/rest/crmFeedBackInfoJoin/uploadFeedBackOrder";
	private static final String LIST_TASK = "/rest/crmFeedBackInfoJoin/feedBackOrderList";
	private static final String GET_TASK_DETAIL = "/rest/crmFeedBackInfoJoin/feedBackOrderDetail";
	private static final String CANCEL_TASK = "/rest/crmFeedBackInfoJoin/cancelOrder";
	private static final String EVALUATE = "/rest/crmFeedBackInfoJoin/evaluateFeedBack";
	private static final String GET_TOKEN = "/rest/ebeiInfo/sysQueryToken";

	
    SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	private String projectId = "240111044331055940";
    
	private static final Logger LOGGER = LoggerFactory.getLogger(EbeiPmTaskHandle.class);

	CloseableHttpClient httpclient = null;
	
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
	
	@PostConstruct
	public void init() {
		httpclient = HttpClients.createDefault();
	}

	private List<CategoryDTO> listServiceType(String projectId) {
		JSONObject param = new JSONObject();
		param.put("projectId", projectId);
		
		String json = postToEbei(param, LIST_SERVICE_TYPE, null);
		
		EbeiResponseEntity<EbeiServiceType> entity = JSONObject.parseObject(json, new TypeReference<EbeiResponseEntity<EbeiServiceType>>(){});
		
		if(entity.isSuccess()) {
			EbeiServiceType type = entity.getData();
			List<EbeiServiceType> types = type.getItems();
			
			List<CategoryDTO> result = types.stream().map(c -> {
				return convertCategory(c);
				
			}).collect(Collectors.toList());
			
			return result;
		}
		
		return null;
	}
	
	private CategoryDTO convertCategory(EbeiServiceType ebeiServiceType) {
		
		CategoryDTO dto = new CategoryDTO();
		dto.setId(Long.valueOf(ebeiServiceType.getServiceId()));
		String parentId = ebeiServiceType.getParentId();
		dto.setParentId("".equals(parentId)?0:Long.valueOf(parentId));
		dto.setName(ebeiServiceType.getServiceName());
		
		List<EbeiServiceType> types = ebeiServiceType.getItems();
		if(null != types) {
			List<CategoryDTO> childrens = types.stream().map(r -> {
				return convertCategory(r);
			}).collect(Collectors.toList());
			dto.setChildrens(childrens);
		}
		
		return dto;
	}
	
	public String postToEbei(JSONObject param, String method, Map<String, String> headers) {

		String url = "http://120.24.88.192:13902/ebeitest/";
		HttpPost httpPost = new HttpPost(url + method);
		CloseableHttpResponse response = null;
		
		String json = null;
		
		try {
			StringEntity stringEntity = new StringEntity(param.toString(), "utf8");
			httpPost.setEntity(stringEntity);
//			httpPost.addHeader("EBEI_TOKEN", "");
//			httpPost.addHeader("HTMIMI_USERID", "");
			
			response = httpclient.execute(httpPost);
			
			int status = response.getStatusLine().getStatusCode();
			if(status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
					json = EntityUtils.toString(entity, "utf8");
				}
			}
			
		} catch (IOException e) {
			LOGGER.error("Pmtask request error, param={}", param, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Pmtask request error.");
		}finally {
            try {
				response.close();
			} catch (IOException e) {
				LOGGER.error("Pmtask close instream, response error, param={}", param, e);
			}
        }
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Data from Ebei, json={}", json);
		
		return json;
	}

	
	@PreDestroy
	public void destroy() {
		if(null != httpclient) {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("Pmtask close httpclient, response error, httpclient={}", httpclient, e);
			}
		}
	}

	private void createTask(PmTask task) {
		
		JSONObject param = new JSONObject();
		
		param.put("userId", "12");
		param.put("address", "测试项目东三区凤凰阁第1栋1单元1楼0001");
		param.put("buildingId", "53327");
		param.put("linkName", "一碑测试");
		param.put("linkTel", "18011001100");
		param.put("serviceId", "");
		param.put("type", "1");
		param.put("remarks", "业主报修水管坏了");
		param.put("projectId", "240111044331055940");
		param.put("anonymous", "0");
		param.put("fileAddrs", "");
		param.put("buildingType", "0");
		
		postToEbei(param, CREATE_TASK, null);
	}
	
	private Boolean cancelTask(PmTask task) {
		
		JSONObject param = new JSONObject();
		
		param.put("orderId", task.getId());
		
		postToEbei(param, CANCEL_TASK, null);
		
		return false;
	}
	
	private Boolean evaluateTask(PmTask task) {
		
		JSONObject param = new JSONObject();
		
		param.put("userId", task.getId());
		param.put("recordId", task.getId());
		param.put("serviceAttitude", task.getId());
		param.put("serviceEfficiency", task.getId());
		param.put("serviceQuality", task.getId());
		param.put("remark", task.getId());
		param.put("fileAddrs", task.getId());
		
		postToEbei(param, EVALUATE, null);
		
		return false;
	}
	
	private EbeiPmTaskDTO getTaskDetail(PmTask task) {
		
		JSONObject param = new JSONObject();
		
		param.put("orderId", task.getId());
		
		String json = postToEbei(param, EVALUATE, null);
		
		EbeiResponseEntity<EbeiPmTaskDTO> entity = JSONObject.parseObject(json, new TypeReference<EbeiResponseEntity<EbeiPmTaskDTO>>(){});
		
		if(entity.isSuccess())
			return entity.getData();
		
		return null;
	}
	
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
	public void evaluateTask(EvaluateTaskCommand cmd) {
		checkOwnerIdAndOwnerType(cmd.getOwnerType(), cmd.getOwnerId());
		checkId(cmd.getId());

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
	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
		// TODO Auto-generated method stub
		
		PmTask task = pmTaskProvider.findTaskById(cmd.getId());
		EbeiPmTaskDTO ebeiPmTaskDTO = getTaskDetail(task);
		
		return null;
	}

	@Override
	public ListTaskCategoriesResponse listTaskCategories(ListTaskCategoriesCommand cmd) {
		
		ListTaskCategoriesResponse response = new ListTaskCategoriesResponse();
		
		List<CategoryDTO> childrens = listServiceType(projectId);
		
		if(null == cmd.getParentId()) {
			CategoryDTO dto = createCategoryDTO();
			dto.setChildrens(childrens);
			
			response.setRequests(Collections.singletonList(dto));
		}else {
			response.setRequests(childrens);

		}
		
		return response;
	}

	@Override
	public List<CategoryDTO> listAllTaskCategories(ListAllTaskCategoriesCommand cmd) {
		
		List<CategoryDTO> childrens = listServiceType(projectId);
		CategoryDTO dto = createCategoryDTO();
		dto.setChildrens(childrens);
		
		return Collections.singletonList(dto);
	}
	
	private CategoryDTO createCategoryDTO() {
		CategoryDTO dto = new CategoryDTO();
		dto.setId(0L);
		dto.setName("物业报修");
		dto.setParentId(0L);
		
		return dto;
	}
}
