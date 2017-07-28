package com.everhomes.yellowPage;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.rest.user.*;
import com.everhomes.user.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.yellowPage.GetRequestInfoResponse;
import com.everhomes.rest.yellowPage.JumpType;
import com.everhomes.rest.yellowPage.ServiceAllianceRequestNotificationTemplateCode;
import com.everhomes.search.ApartmentRequestInfoSearcher;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.search.SettleRequestInfoSearcher;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceApartmentRequests;
import com.everhomes.util.ConvertHelper;
import com.mysql.jdbc.StringUtils;

@Component(CustomRequestHandler.CUSTOM_REQUEST_OBJ_RESOLVER_PREFIX + CustomRequestConstants.APARTMENT_REQUEST_CUSTOM)
public class ApartmentCustomRequestHandler implements CustomRequestHandler {

private static final Logger LOGGER=LoggerFactory.getLogger(ApartmentCustomRequestHandler.class);
	
	@Autowired
	private UserProvider userProvider;
	
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
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Override
	public void addCustomRequest(AddRequestCommand cmd) {
		ServiceAllianceApartmentRequests request = GsonUtil.fromJson(cmd.getRequestJson(), ServiceAllianceApartmentRequests.class);
		
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
		
		LOGGER.info("ApartmentCustomRequestHandler addCustomRequest request:" + request);
		yellowPageProvider.createApartmentRequests(request);
		ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
		requestInfo.setTemplateType(cmd.getTemplateType());
		requestInfo.setJumpType(JumpType.TEMPLATE.getCode());
		saRequestInfoSearcher.feedDoc(requestInfo);
		
		ServiceAllianceCategories category = yellowPageProvider.findCategoryById(request.getType());
		
		String creatorName = (request.getCreatorName() == null) ? "" : request.getCreatorName();
		String creatorMobile = (request.getCreatorMobile() == null) ? "" : request.getCreatorMobile();
		String categoryName = "";
		if(category != null) {
			categoryName = (category.getName() == null) ? "" : category.getName();
		}

		ServiceAlliances serviceOrg = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(), request.getOwnerType(), request.getOwnerId());

		//推送消息
		//给服务公司留的手机号推消息
		String scope = ServiceAllianceRequestNotificationTemplateCode.SCOPE;
		String locale = "zh_CN";

		Map<String, Object> notifyMap = new HashMap<String, Object>();
		notifyMap.put("categoryName", categoryName);
		notifyMap.put("creatorName", creatorName);
		notifyMap.put("creatorMobile", creatorMobile);
		notifyMap.put("note", changeRequestToHtml(request));
		notifyMap.put("serviceAllianceName", "");
		notifyMap.put("notemessage", getNote(request));
		Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
        String creatorOrganization = "";
		if(org != null) {
			creatorOrganization = org.getName();
		}
		notifyMap.put("creatorOrganization", creatorOrganization);
		
		String title = localeStringService.getLocalizedString(ServiceAllianceRequestNotificationTemplateCode.SCOPE, 
				ServiceAllianceRequestNotificationTemplateCode.AN_APPLICATION_FORM, UserContext.current().getUser().getLocale(), "");
		if(serviceOrg != null) {
			notifyMap.put("serviceOrgName", serviceOrg.getName());
			notifyMap.put("serviceAllianceName", serviceOrg.getName());
			title = serviceOrg.getName() + title;
		}
		notifyMap.put("title", title);
		//modify by dengs,20170425  更换模板，发送html邮件
//		int code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_NOTIFY_ORG;
		int code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_MAIL_ORG_ADMIN_IN_HTML;
		String notifyTextForOrg = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
		
		//modify by dengs 20170425  邮件附件生成
		List<File> attementList = createAttachmentList(title, notifyMap, request);
		List<String> stringAttementList = new ArrayList<String>();
		attementList.stream().forEach(file->{stringAttementList.add(file.getAbsolutePath());});
		if(serviceOrg != null) {
//			UserIdentifier orgContact = userProvider.findClaimedIdentifierByToken(UserContext.getCurrentNamespaceId(), serviceOrg.getContactMobile());
//			if(orgContact != null) {
//				sendMessageToUser(orgContact.getOwnerUid(), notifyTextForOrg);
//			}
			OrganizationMember member = organizationProvider.findOrganizationMemberById(serviceOrg.getContactMemid());
			if(member != null) {
				code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_NOTIFY_ORG;
				String notifyText = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
				sendMessageToUser(member.getTargetId(), notifyText);
			}
			sendEmail(serviceOrg.getEmail(), title, notifyTextForOrg,stringAttementList);
			
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
					sendEmail(email.getContactToken(), title, notifyTextForAdmin,stringAttementList);
				}
			}
		}
		//删除生成的pdf文件，附件
		attementList.stream().forEach(file->{file.delete();});
	}
	

	private String getNote(ServiceAllianceApartmentRequests request) {
		
		List<RequestFieldDTO> fieldList = toFieldDTOList(request);
		if(fieldList != null && fieldList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for(RequestFieldDTO field : fieldList) {
				String fieldValue = (field.getFieldValue() == null) ? "" : field.getFieldValue();
				String fieldName = field.getFieldName()==null?"":field.getFieldName();
				sb.append(" ").append(fieldName.trim()).append("：").append(fieldValue).append("\n");
			}
			
			return sb.toString();
		}
		
//		String name = (request.getName() == null) ? "" : request.getName();
//		String mobile = (request.getMobile() == null) ? "" : request.getMobile();
//		String organizationName = (request.getOrganizationName() == null) ? "" : request.getOrganizationName();
//		Double areaSize = (request.getAreaSize() == null) ? 0.0 : request.getAreaSize();
//		
//		String note = "姓名:" + name + "\n" + "手机号:" + mobile + "\n" + "公司:" + organizationName + "\n" + "面积需求:" + areaSize + "\n";
		return null;
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
	

	@Override
	public GetRequestInfoResponse getCustomRequestInfo(Long id) {
		ServiceAllianceApartmentRequests request = yellowPageProvider.findApartmentRequests(id);
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



	
	public List<RequestFieldDTO> toFieldDTOList(Object requestObject) {
		ServiceAllianceApartmentRequests field = (ServiceAllianceApartmentRequests)requestObject;
		GetCustomRequestTemplateCommand command = new GetCustomRequestTemplateCommand();
		command.setTemplateType(field.getTemplateType());
		RequestTemplateDTO template = userActivityService.getCustomRequestTemplate(command);

		List<RequestFieldDTO> list = new ArrayList<RequestFieldDTO>();
		if(template != null && template.getDtos() != null && template.getDtos().size() > 0) {
			EhServiceAllianceApartmentRequests request = ConvertHelper.convert(field, EhServiceAllianceApartmentRequests.class);
			Field[] fields = request.getClass().getDeclaredFields();
			for (FieldDTO fieldDTO : template.getDtos()) {
				RequestFieldDTO dto = new RequestFieldDTO();
				dto.setFieldType(fieldDTO.getFieldType());
				dto.setFieldContentType(fieldDTO.getFieldContentType());

				for (Field requestField : fields) {
					requestField.setAccessible(true);  
					// 表示为private类型
					if (requestField.getModifiers() == 2) {
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

				dto.setFieldName(fieldDTO.getFieldDisplayName());
				list.add(dto);
			}
		}

//		RequestFieldDTO dto = new RequestFieldDTO();
//		dto.setFieldType(FieldType.STRING.getCode());
//		dto.setFieldContentType(FieldContentType.TEXT.getCode());
//
//		dto.setFieldValue(fields.getName());
//		dto.setFieldName("姓名");
//		list.add(dto);
//
//		dto = new RequestFieldDTO();
//		dto.setFieldType(FieldType.STRING.getCode());
//		dto.setFieldContentType(FieldContentType.TEXT.getCode());
//
//		dto.setFieldValue(fields.getMobile());
//		dto.setFieldName("手机号");
//		list.add(dto);
//
//		dto = new RequestFieldDTO();
//		dto.setFieldType(FieldType.STRING.getCode());
//		dto.setFieldContentType(FieldContentType.TEXT.getCode());
//
//		dto.setFieldValue(fields.getOrganizationName());
//		dto.setFieldName("公司");
//
//		dto = new RequestFieldDTO();
//		dto.setFieldType(FieldType.NUMBER.getCode());
//		dto.setFieldContentType(FieldContentType.TEXT.getCode());
//
//		if(fields.getAreaSize() != null) {
//			dto.setFieldValue(fields.getAreaSize().toString());
//		}
//		dto.setFieldName("面积需求");
//		list.add(dto);
		
		return list;
	}


	@Override
	public String getFixedContent(Object notifyMap, String defaultValue) {
		String scope = ServiceAllianceRequestNotificationTemplateCode.SCOPE;
		int code = ServiceAllianceRequestNotificationTemplateCode.REQUEST_MAIL_TO_PDF;
		return localeTemplateService.getLocaleTemplateString(scope, code, UserContext.current().getUser().getLocale(), notifyMap, "");
	
	}
	
	@Override
	public String parseUri(String uri){
		return this.contentServerService.parserUri(uri, EntityType.USER.getCode(), UserContext.current().getUser().getId());
	}

}
