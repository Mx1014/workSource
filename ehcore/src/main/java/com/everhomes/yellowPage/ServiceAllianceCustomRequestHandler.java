package com.everhomes.yellowPage;

import java.lang.reflect.Field;
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
import com.everhomes.organization.OrganizationMember;
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
import com.everhomes.rest.user.FieldDTO;
import com.everhomes.rest.user.FieldType;
import com.everhomes.rest.user.GetCustomRequestTemplateCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.rest.user.RequestTemplateDTO;
import com.everhomes.rest.videoconf.ConfServiceErrorCode;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.rest.yellowPage.JumpType;
import com.everhomes.rest.yellowPage.ServiceAllianceRequestNotificationTemplateCode;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceApartmentRequests;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceRequests;
import com.everhomes.user.CustomRequestConstants;
import com.everhomes.user.CustomRequestHandler;
import com.everhomes.user.RequestAttachments;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserActivityService;
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

	@Autowired
	private UserActivityService userActivityService;
			
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
		request.setTemplateType(cmd.getTemplateType());
	  
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
		requestInfo.setJumpType(JumpType.TEMPLATE.getCode());
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

		ServiceAlliances serviceOrg = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(), request.getOwnerType(), request.getOwnerId());

		//推送消息
		//给服务公司留的手机号推消息
		String scope = ServiceAllianceRequestNotificationTemplateCode.SCOPE;
		String locale = "zh_CN";
		
		Map<String, Object> notifyMap = new HashMap<String, Object>();
		notifyMap.put("categoryName", categoryName);
		notifyMap.put("creatorName", creatorName);
		notifyMap.put("creatorMobile", creatorMobile);
		notifyMap.put("note", getNote(request));
		notifyMap.put("serviceAllianceName", serviceOrg.getName());

		Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
		
		String creatorOrganization = "";
		if(org != null) {
			creatorOrganization = org.getName();
		}
		notifyMap.put("creatorOrganization", creatorOrganization);
			
		int code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_NOTIFY_ORG;
		String notifyTextForOrg = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
		
		if(serviceOrg != null) {
//			UserIdentifier orgContact = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), serviceOrg.getContactMobile());
//			if(orgContact != null) {
//				sendMessageToUser(orgContact.getOwnerUid(), notifyTextForOrg);
//			}
			
			OrganizationMember member = organizationProvider.findOrganizationMemberById(serviceOrg.getContactMemid());
			if(member != null) {
				sendMessageToUser(member.getTargetId(), notifyTextForOrg);
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
		
		List<RequestFieldDTO> fieldList = toFieldDTOList(request);
		if(fieldList != null && fieldList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for(RequestFieldDTO field : fieldList) {
				String fieldValue = (field.getFieldValue() == null) ? "" : field.getFieldValue();
				sb.append(field.getFieldName() + ":" + fieldValue + "\n");
			}
			
			return sb.toString();
		}
		
//		String name = (request.getName() == null) ? "" : request.getName();
//		String mobile = (request.getMobile() == null) ? "" : request.getMobile();
//		String organizationName = (request.getOrganizationName() == null) ? "" : request.getOrganizationName();
//		String cityName = (request.getCityName() == null) ? "" : request.getCityName();
//		String industry = (request.getIndustry() == null) ? "" : request.getIndustry();
//		String projectDesc = (request.getProjectDesc() == null) ? "" : request.getProjectDesc();
//		String financingStage = (request.getFinancingStage() == null) ? "" : request.getFinancingStage();
//		BigDecimal financingAmount = (request.getFinancingAmount() == null) ? new BigDecimal(0) : request.getFinancingAmount();
//		Double transferShares = (request.getTransferShares() == null) ? 0.0 : request.getTransferShares();
//		
//		String note = "姓名:" + name + "\n" + "手机号:" + mobile + "\n" + "企业名称:" + organizationName
//				 + "\n" + "企业城市:" + cityName + "\n" + "企业行业:" + industry + "\n"
//				 + "项目描述:" + projectDesc + "\n" + "融资阶段:" + financingStage + "\n" + "融资金额（万元）:"
//				 + financingAmount + "\n" + "出让股份 %:" + transferShares + "\n";
		return null;
	}

	@Override
	public GetRequestInfoResponse getCustomRequestInfo(Long id) {

		ServiceAllianceRequests request = yellowPageProvider.findServiceAllianceRequests(id);
		
//		ServiceAllianceRequestsFields fields = ConvertHelper.convert(request, ServiceAllianceRequestsFields.class);
		List<RequestFieldDTO> fieldList = new ArrayList<RequestFieldDTO>();
		if(request != null) {
			fieldList = toFieldDTOList(request);
		}
		
		GetRequestInfoResponse response = new GetRequestInfoResponse();
		response.setDtos(fieldList);
		
		if(request != null) {
			response.setCreateTime(request.getCreateTime());
		}
		

		return response;
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
	private List<RequestFieldDTO> toFieldDTOList(ServiceAllianceRequests field) {
		
		GetCustomRequestTemplateCommand command = new GetCustomRequestTemplateCommand();
		command.setTemplateType(field.getTemplateType());
		RequestTemplateDTO template = userActivityService.getCustomRequestTemplate(command);

		List<RequestFieldDTO> list = new ArrayList<RequestFieldDTO>();
		if(template != null && template.getDtos() != null && template.getDtos().size() > 0) {
			List<RequestAttachments> attachments =  userActivityProvider.listRequestAttachments(CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM, field.getId());
			EhServiceAllianceRequests request = ConvertHelper.convert(field, EhServiceAllianceRequests.class);
			Field[] fields = request.getClass().getDeclaredFields();
			for (FieldDTO fieldDTO : template.getDtos()) {
				RequestFieldDTO dto = new RequestFieldDTO();
				dto.setFieldType(fieldDTO.getFieldType());
				dto.setFieldContentType(fieldDTO.getFieldContentType());
				dto.setFieldName(fieldDTO.getFieldDisplayName());

				for (Field requestField : fields) {
					requestField.setAccessible(true);  
					// 表示为private类型
					if (requestField.getModifiers() == 2) {
						
						if(dto.getFieldType().equals(FieldType.BLOB.getCode())) {
							if(attachments != null && attachments.size() > 0) {
								for(RequestAttachments attachment : attachments) {
									if(attachment.getTargetFieldName().equals(dto.getFieldName())){
										dto.setFieldContentType(attachment.getContentType());
										dto.setFieldName(attachment.getTargetFieldName());
										dto.setFieldValue(attachment.getContentUri());
									}
								}
							}
							
						} else {
							if(requestField.getName().equals(fieldDTO.getFieldName())){
								// 字段值
								try {
									if(requestField.get(request) != null)
										dto.setFieldValue(requestField.get(request).toString());
									
									break;
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}

				list.add(dto);
			}
		}
		
		return list;
	}

}
