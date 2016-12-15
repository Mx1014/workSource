package com.everhomes.yellowPage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.equipment.EquipmentNotificationTemplateCode;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.user.AddRequestCommand;
import com.everhomes.rest.user.FieldContentType;
import com.everhomes.rest.user.FieldType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.rest.videoconf.ConfServiceErrorCode;
import com.everhomes.rest.yellowPage.ServiceAllianceRequestNotificationTemplateCode;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.user.CustomRequestConstants;
import com.everhomes.user.CustomRequestHandler;
import com.everhomes.user.RequestAttachments;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;

@Component(CustomRequestHandler.CUSTOM_REQUEST_OBJ_RESOLVER_PREFIX + CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM)
public class ServiceAllianceCustomRequestHandler implements CustomRequestHandler {

	private static final Logger LOGGER=LoggerFactory.getLogger(ServiceAllianceCustomRequestHandler.class);
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private UserActivityProvider userActivityProvider;
	
	@Autowired
	private YellowPageProvider yellowPageProvider;
	
	@Autowired
	private MessagingService messagingService;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private ServiceAllianceRequestInfoSearcher saRequestInfoSearcher;
			
	@Override
	public void addCustomRequest(AddRequestCommand cmd) {
		LOGGER.info("ServiceAllianceCustomRequestHandler addCustomRequest cmd:" + cmd);
		
		ServiceAllianceRequests request = GsonUtil.fromJson(cmd.getRequestJson(), ServiceAllianceRequests.class);
		request.setNamespaceId(UserContext.getCurrentNamespaceId());

		request.setOwnerType(cmd.getOwnerType());
		request.setOwnerId(cmd.getOwnerId());
		request.setType(cmd.getType());
		request.setCategoryId(cmd.getCategoryId());
		request.setCreatorOrganizationId(cmd.getCreatorOrganizationId());
		request.setServiceAllianceId(cmd.getServiceAllianceId());
	  
		User user = UserContext.current().getUser();
		request.setCreatorUid(user.getId());
		request.setCreatorName(user.getNickName());
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		if(identifier != null)
			request.setCreatorMobile(identifier.getIdentifierToken());
		  
		LOGGER.info("ServiceAllianceCustomRequestHandler addCustomRequest request:" + request);
		yellowPageProvider.createServiceAllianceRequests(request);
		ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
		requestInfo.setTemplateType(cmd.getTemplateType());
		saRequestInfoSearcher.feedDoc(requestInfo);
		
		if(cmd.getAttachments() != null && cmd.getAttachments().size() > 0) {
			List<RequestAttachments> attachments = cmd.getAttachments().stream().map(attachment -> {
				RequestAttachments att = ConvertHelper.convert(attachment, RequestAttachments.class);
				att.setOwnerType(CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM);
				att.setOwnerId(request.getId());
				att.setCreatorUid(user.getId());
				
				userActivityProvider.createRequestAttachments(att);
				return att;
			}).collect(Collectors.toList());
			request.setAttachments(attachments);
		}
		
		LOGGER.info("ServiceAllianceCustomRequestHandler addCustomRequest request:" + request);
		
		ServiceAllianceCategories category = yellowPageProvider.findCategoryById(request.getType());
		String creatorName = (request.getCreatorName() == null) ? "" : request.getCreatorName();
		String creatorMobile = (request.getCreatorMobile() == null) ? "" : request.getCreatorMobile();
		String categoryName = (category.getName() == null) ? "" : category.getName();
		//推送消息
		//给服务公司留的手机号推消息
		String scope = ServiceAllianceRequestNotificationTemplateCode.SCOPE;
		String locale = "zh_CN";
		
		Map<String, Object> notifyMap = new HashMap<String, Object>();
		notifyMap.put("categoryName", categoryName);
		notifyMap.put("creatorName", creatorName);
		notifyMap.put("creatorMobile", creatorMobile);
		notifyMap.put("note", getNote(request));
		Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
		
		String creatorOrganization = "";
		if(org != null) {
			creatorOrganization = org.getName();
		}
		notifyMap.put("creatorOrganization", creatorOrganization);
			
		int code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_NOTIFY_ORG;
		String notifyTextForOrg = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
		
		ServiceAlliances serviceOrg = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(), request.getOwnerType(), request.getOwnerId());
		if(serviceOrg != null) {
			UserIdentifier orgContact = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), serviceOrg.getContactMobile());
			if(orgContact != null) {
				sendMessageToUser(orgContact.getOwnerUid(), notifyTextForOrg);
			}
			
			sendEmail(serviceOrg.getEmail(), category.getName(), notifyTextForOrg);
			
		}
		
		//发消息给服务联盟机构管理员
		code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_NOTIFY_ADMIN;
		String notifyTextForAdmin = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<ServiceAllianceNotifyTargets> targets = yellowPageProvider.listNotifyTargets(request.getOwnerType(), request.getOwnerId(), ContactType.MOBILE.getCode(), 
				request.getType(), locator, Integer.MAX_VALUE);
		if(targets != null && targets.size() > 0) {
			for(ServiceAllianceNotifyTargets target : targets) {
				if(target.getStatus().byteValue() == 1) {
					UserIdentifier contact = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), target.getContactToken());
					if(contact != null)
						sendMessageToUser(contact.getOwnerUid(), notifyTextForAdmin);
				}
				
			}
		}
		
		//发邮件给服务联盟机构管理员
		List<ServiceAllianceNotifyTargets> emails = yellowPageProvider.listNotifyTargets(request.getOwnerType(), request.getOwnerId(), ContactType.EMAIL.getCode(), 
				request.getType(), locator, Integer.MAX_VALUE);
		if(emails != null && emails.size() > 0) {
			for(ServiceAllianceNotifyTargets email : emails) {
				if(email.getStatus().byteValue() == 1) {
					sendEmail(email.getContactToken(), category.getName(), notifyTextForAdmin);
				}
			}
		}
	}
	
	private String getNote(ServiceAllianceRequests request) {
		
		String name = (request.getName() == null) ? "" : request.getName();
		String mobile = (request.getMobile() == null) ? "" : request.getMobile();
		String organizationName = (request.getOrganizationName() == null) ? "" : request.getOrganizationName();
		String cityName = (request.getCityName() == null) ? "" : request.getCityName();
		String industry = (request.getIndustry() == null) ? "" : request.getIndustry();
		String projectDesc = (request.getProjectDesc() == null) ? "" : request.getProjectDesc();
		String financingStage = (request.getFinancingStage() == null) ? "" : request.getFinancingStage();
		BigDecimal financingAmount = (request.getFinancingAmount() == null) ? new BigDecimal(0) : request.getFinancingAmount();
		Double transferShares = (request.getTransferShares() == null) ? 0.0 : request.getTransferShares();
		
		String note = "姓名:" + name + "\n" + "手机号:" + mobile + "\n" + "企业名称:" + organizationName
				 + "\n" + "企业城市:" + cityName + "\n" + "企业行业:" + industry + "\n"
				 + "项目描述:" + projectDesc + "\n" + "融资阶段:" + financingStage + "\n" + "融资金额（万元）:"
				 + financingAmount + "\n" + "出让股份 %:" + transferShares + "\n";
		return note;
	}

	@Override
	public List<RequestFieldDTO> getCustomRequestInfo(Long id) {

		ServiceAllianceRequests request = yellowPageProvider.findServiceAllianceRequests(id);
//		ServiceAllianceRequestsFields fields = ConvertHelper.convert(request, ServiceAllianceRequestsFields.class);
		List<RequestFieldDTO> fieldList = new ArrayList<RequestFieldDTO>();
		if(request != null) {
			fieldList = toFieldDTOList(request);
		}
		
		return fieldList;
	}
	
	private void sendEmail(String emailAddress, String categoryName, String content) {
		if(!StringUtils.isNullOrEmpty(emailAddress)) {
			String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
	        MailHandler handler = PlatformContext.getComponent(handlerName);
	        
	        String scope = ServiceAllianceRequestNotificationTemplateCode.SCOPE;
			String locale = "zh_CN";
			int code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_MAIL_SUBJECT;
			Map<String, Object> notifyMap = new HashMap<String, Object>();
			notifyMap.put("categoryName", categoryName);
			String subject = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
			
	        handler.sendMail(UserContext.getCurrentNamespaceId(), null,emailAddress, subject, content);
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
	
	//硬转，纯体力
	private List<RequestFieldDTO> toFieldDTOList(ServiceAllianceRequests fields) {
		List<RequestFieldDTO> list = new ArrayList<RequestFieldDTO>();
		RequestFieldDTO dto = new RequestFieldDTO();
		dto.setFieldType(FieldType.STRING.getCode());
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldValue(fields.getName());
		dto.setFieldName("姓名");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldType(FieldType.STRING.getCode());
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldValue(fields.getMobile());
		dto.setFieldName("手机号");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldType(FieldType.STRING.getCode());
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldValue(fields.getOrganizationName());
		dto.setFieldName("企业名称");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldType(FieldType.STRING.getCode());
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldValue(fields.getCityName());
		dto.setFieldName("企业城市");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldType(FieldType.STRING.getCode());
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldValue(fields.getIndustry());
		dto.setFieldName("企业行业");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldType(FieldType.STRING.getCode());
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldValue(fields.getProjectDesc());
		dto.setFieldName("项目描述");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldType(FieldType.STRING.getCode());
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldValue(fields.getFinancingStage());
		dto.setFieldName("融资阶段");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldType(FieldType.DECIMAL.getCode());
		if(fields.getFinancingAmount() != null) {
			dto.setFieldValue(fields.getFinancingAmount().toString());
		}
		dto.setFieldName("融资金额（万元）");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldContentType(FieldContentType.TEXT.getCode());

		dto.setFieldType(FieldType.NUMBER.getCode());
		if(fields.getTransferShares() != null) {
			dto.setFieldValue(fields.getTransferShares().toString());
		}
		dto.setFieldName("出让股份 %");
		list.add(dto);
		
		dto = new RequestFieldDTO();
		dto.setFieldContentType(FieldContentType.TEXT.getCode());
		
		dto.setFieldType(FieldType.DATETIME.getCode());
		if(fields.getCreateTime() != null) {
			dto.setFieldValue(fields.getCreateTime().toString());
		}
		dto.setFieldName("提交时间");
		list.add(dto);
		
		List<RequestAttachments> attachments =  userActivityProvider.listRequestAttachments(CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM, fields.getId());
		if(attachments != null && attachments.size() > 0) {
			for(RequestAttachments attachment : attachments) {
				dto = new RequestFieldDTO();
				dto.setFieldType(FieldType.STRING.getCode());
				dto.setFieldContentType(FieldContentType.TEXT.getCode());
				
				dto.setFieldContentType(attachment.getContentType());
				dto.setFieldType(FieldType.BLOB.getCode());
				dto.setFieldName(attachment.getTargetFieldName());
				dto.setFieldValue(attachment.getContentUri());
				list.add(dto);
			}
			                        
		}
		
		return list;
	}

}
