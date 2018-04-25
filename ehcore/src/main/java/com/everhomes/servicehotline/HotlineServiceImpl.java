package com.everhomes.servicehotline;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.servicehotline.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.*;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.message.MessageProvider;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.techpark.servicehotline.ServiceConfiguration;
import com.everhomes.techpark.servicehotline.ServiceConfigurationsProvider;
import com.everhomes.techpark.servicehotline.ServiceHotline;
import com.everhomes.techpark.servicehotline.ServiceHotlinesProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class HotlineServiceImpl implements HotlineService {
	public static final String HOTLINE_SCOPE = "hotline";
	public static final String HOTLINE_NOTSHOW_SCOPE = "hotline-notshow";

	@Autowired
	private ServiceConfigurationsProvider serviceConfigurationsProvider;

	@Autowired
	private ServiceHotlinesProvider serviceHotlinesProvider;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
		@Autowired
	private ConfigurationProvider configProvider;
		
	@Autowired
	private MessageProvider messageProvider;		
		
	@Override
	public GetHotlineSubjectResponse getHotlineSubject(
			GetHotlineSubjectCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		List<ServiceConfiguration> sConfigurations = serviceConfigurationsProvider
				.queryServiceConfigurations(null, 1,
						new ListingQueryBuilderCallback() {

							@Override
							public SelectQuery<? extends Record> buildCondition(
									ListingLocator locator,
									SelectQuery<? extends Record> query) {
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE
										.eq(cmd.getOwnerType()));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID
										.eq(cmd.getOwnerId()));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID
										.eq(namespaceId));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME
										.eq(HOTLINE_SCOPE));
								return query;
							}
						});
		GetHotlineSubjectResponse response = new GetHotlineSubjectResponse();
		HotlineSubject baseHotline = new HotlineSubject();
		baseHotline.setLayoutType(LayoutType.SERVICE_HOTLINE.getCode());
		baseHotline.setServiceType(ServiceType.SERVICE_HOTLINE.getCode());
		baseHotline.setTitle("服务热线");
		response.setSubjects(new ArrayList<HotlineSubject>());
		response.getSubjects().add(baseHotline);
		for (ServiceConfiguration configuration : sConfigurations) {
			if (ServiceType.ZHUANSHU_SERVICE.getCode().byteValue() == (ServiceType.ZHUANSHU_SERVICE
					.getCode().byteValue() & Byte.valueOf(configuration
					.getValue()))) {
				HotlineSubject service1 = new HotlineSubject();
				service1.setLayoutType(LayoutType.ZHUANSHU_SERVICE.getCode());
				service1.setServiceType(ServiceType.ZHUANSHU_SERVICE.getCode());
				service1.setTitle(configuration.getDisplayName());
				response.getSubjects().add(service1);
			}
			// 新增的类型在后面加,仿照专属客服
		}
		setShowSubjects(cmd,response);
		return response;
	}

	private void setShowSubjects(GetHotlineSubjectCommand cmd ,GetHotlineSubjectResponse response){
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		List<ServiceConfiguration> sConfigurations = serviceConfigurationsProvider
				.queryServiceConfigurations(null, 1,
						new ListingQueryBuilderCallback() {

							@Override
							public SelectQuery<? extends Record> buildCondition(
									ListingLocator locator,
									SelectQuery<? extends Record> query) {
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE
										.eq(cmd.getOwnerType()));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID
										.eq(cmd.getOwnerId()));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID
										.eq(namespaceId));
								query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME
										.eq(HOTLINE_NOTSHOW_SCOPE));
								return query;
							}
						});
		Set<String> set = new HashSet<>();
		if (sConfigurations!=null && sConfigurations.size()>0)
			sConfigurations.stream().forEach(r->{
				set.add(r.getDisplayName());
			});
		response.setShowSubjecs(new ArrayList<>());
		response.getSubjects().stream().forEach(r->{
			if (!set.contains(r.getTitle()))
				response.getShowSubjecs().add(r.getTitle());
		});
	}

	@Override
	public GetHotlineListResponse getHotlineList(GetHotlineListCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4030040300L, cmd.getAppId(), null,cmd.getCurrentProjectId());//订单记录权限
		}
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		GetHotlineListResponse resp = new GetHotlineListResponse();
		List<HotlineDTO> hotlines = new ArrayList<HotlineDTO>();
		this.serviceHotlinesProvider.queryServiceHotlines(null,
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {

					@Override
					public SelectQuery<? extends Record> buildCondition(
							ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_TYPE
								.eq(cmd.getOwnerType()));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_ID
								.eq(cmd.getOwnerId()));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.NAMESPACE_ID
								.eq(namespaceId));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.SERVICE_TYPE
								.eq(cmd.getServiceType().intValue()));
						return query;
					}
				}).forEach(r -> {
			HotlineDTO dto = ConvertHelper.convert(r, HotlineDTO.class);
			if(null != r.getUserId()){
				User user = this.userProvider.findUserById(r.getUserId());
				if (null != user)
					dto.setAvatar(populateUserAvatar ( user,user.getAvatar()));
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
				dto.setPhone(userIdentifier.getIdentifierToken());
			}
			hotlines.add(dto);
		});
		resp.setHotlines(hotlines);
		return resp;
	}
	private String populateUserAvatar( User user,String avatarUri) {
		if(avatarUri == null || avatarUri.trim().length() == 0) {
			avatarUri = userService.getUserAvatarUriByGender(user.getId(), user.getNamespaceId(), user.getGender());
		} 
		try{
			String url=contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), user.getId());
			return url;
		}catch(Exception e){
			//LOGGER.error("Failed to parse avatar uri, userId=" + user.getId() + ", avatar=" + avatarUri);
		}
		return null;
	}
	@Override
	public void addHotline(AddHotlineCommand cmd) {
		ServiceHotline hotline = ConvertHelper.convert(cmd,
				ServiceHotline.class);
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		//查询是否有重复的
		List<ServiceHotline> tmp = this.serviceHotlinesProvider.queryServiceHotlines(null,
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {

					@Override
					public SelectQuery<? extends Record> buildCondition(
							ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_TYPE
								.eq(cmd.getOwnerType()));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_ID
								.eq(cmd.getOwnerId()));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.NAMESPACE_ID
								.eq(namespaceId));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.SERVICE_TYPE
								.eq(cmd.getServiceType().intValue()));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.CONTACT
								.eq(cmd.getContact()));
						return query;
					}
				});
		if (tmp!=null && tmp.size()>0)
			throw RuntimeErrorException
					.errorWith(HotlineErrorCode.SCOPE,
							HotlineErrorCode.ERROR_DUPLICATE_PHONE,
							"hotline already exists");

		hotline.setCreateTime(new Timestamp( DateHelper.currentGMTTime().getTime()));
		hotline.setCreatorUid(UserContext.current().getUser().getId());
		hotline.setNamespaceId(namespaceId);
		hotline.setUpdateTime(new Timestamp( DateHelper.currentGMTTime().getTime()));
		this.serviceHotlinesProvider.createServiceHotline(hotline);

	}

	@Override
	public void deleteHotline(DeleteHotlineCommand cmd) {

		if (null == cmd
				|| null == this.serviceHotlinesProvider
						.getServiceHotlineById(cmd.getId())) {
			throw RuntimeErrorException
					.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid paramter id can not be null or hotline can not found");
		}
		ServiceHotline hotline = ConvertHelper.convert(cmd,
				ServiceHotline.class);
		this.serviceHotlinesProvider.deleteServiceHotline(hotline);
	}

	@Override
	public void updateHotline(UpdateHotlineCommand cmd) {
		if (null == cmd
				|| null == this.serviceHotlinesProvider
						.getServiceHotlineById(cmd.getId())) {
			throw RuntimeErrorException
					.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid paramter id can not be null or hotline can not found");
		}
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		//查询是否有重复的
		List<ServiceHotline> tmp = this.serviceHotlinesProvider.queryServiceHotlines(null,
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {

					@Override
					public SelectQuery<? extends Record> buildCondition(
							ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_TYPE
								.eq(cmd.getOwnerType()));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_ID
								.eq(cmd.getOwnerId()));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.NAMESPACE_ID
								.eq(namespaceId));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.SERVICE_TYPE
								.eq(cmd.getServiceType().intValue()));
						query.addConditions(Tables.EH_SERVICE_HOTLINES.CONTACT
								.eq(cmd.getContact()));
						return query;
					}
				});
		tmp = tmp.stream().filter(p->!p.getId().equals(cmd.getId())).collect(Collectors.toList());//排除自己
		if (tmp!=null && tmp.size()>0)
			throw RuntimeErrorException
					.errorWith(HotlineErrorCode.SCOPE,
							HotlineErrorCode.ERROR_DUPLICATE_PHONE,
							"hotline already exists");

		ServiceHotline hotline = ConvertHelper.convert(cmd,
				ServiceHotline.class);
		hotline.setNamespaceId(namespaceId);
		hotline.setUpdateTime(new Timestamp( DateHelper.currentGMTTime().getTime()));
		this.serviceHotlinesProvider.updateServiceHotline(hotline);
	}

	@Override
	public void updateHotlines(UpdateHotlinesCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			for (UpdateHotlineCommand cmd1 : cmd.getHotlines()) {
				this.updateHotline(cmd1);
			}
			return null;
		});
	}

	@Override
	public void setHotlineSubject(SetHotlineSubjectCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		switch (NormalFlag.fromCode(cmd.getSwitchFlag())) {
		case NEED:
			ServiceConfiguration obj = new ServiceConfiguration();
			obj.setOwnerType(cmd.getOwnerType());
			obj.setOwnerId(cmd.getOwnerId());
			obj.setNamespaceId(namespaceId);
			obj.setName(HOTLINE_SCOPE);
			obj.setValue(cmd.getServiceType() + "");
			obj.setDisplayName(cmd.getDisplayName());
			serviceConfigurationsProvider.createServiceConfiguration(obj);
			break;
		case NONEED:
			ServiceConfiguration sConfiguration = serviceConfigurationsProvider
					.queryServiceConfigurations(null, 1,
							new ListingQueryBuilderCallback() {

								@Override
								public SelectQuery<? extends Record> buildCondition(
										ListingLocator locator,
										SelectQuery<? extends Record> query) {
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE
											.eq(cmd.getOwnerType()));
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID
											.eq(cmd.getOwnerId()));
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID
											.eq(namespaceId));
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME
											.eq(HOTLINE_SCOPE));
									query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.VALUE
											.eq(cmd.getServiceType() + ""));
									return query;
								}
							}).get(0);
			serviceConfigurationsProvider
					.deleteServiceConfiguration(sConfiguration);
			break;

		}
	}

	@Override
	public void updateHotlineOrder(UpdateHotlinesCommand cmd) {
		if(cmd.getHotlines() ==null )
			return  ;
		this.dbProvider.execute((TransactionStatus status) -> {
			for (UpdateHotlineCommand cmd1 : cmd.getHotlines()) {
				ServiceHotline hotline = this.serviceHotlinesProvider.getServiceHotlineById(cmd1.getId());
				if(null == hotline)
					throw RuntimeErrorException
					.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid paramter   hotline can not found id = "+cmd1.getId());
				hotline.setDefaultOrder(cmd1.getDefaultOrder());
				hotline.setUpdateTime(new Timestamp( DateHelper.currentGMTTime().getTime()));
				this.serviceHotlinesProvider.updateServiceHotline(hotline);
			}
			return null;
		}); 
	}

	@Override
	public GetUserInfoByIdResponse getUserInfoById(GetUserInfoByIdCommand cmd) {
		User queryUser = userProvider.findUserById(cmd.getId());
		if(queryUser == null){
			return null;
		}
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(queryUser.getId());
		List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
				.map(r->r.getIdentifierToken()).collect(Collectors.toList());
		GetUserInfoByIdResponse info=ConvertHelper.convert(queryUser, GetUserInfoByIdResponse.class);
		info.setPhones(phones);

		if (cmd.getOrgId()!=null) {
			OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(cmd.getId(), cmd.getOrgId());
			if (member!=null)
				info.setContractName(member.getContactName());
		}
		return info;
	}
	
	
	
	/*
	 * @see
	 * com.everhomes.techpark.servicehotline.HotlineService#listChatGroup(com.
	 * everhomes.rest.servicehotline.ListChatGroupCommand)
	 * 
	 * 根据条件获取会话列表。 相同的两个人的会话定义为一个会话。 例： 客服A与用户A的有100条聊天记录，归为 “客服A-用户A”一个会话
	 * 客服A与用户B的有1条聊天记录，归为 “客服A-用户B”一个会话 客服A与用户C的无聊天记录，不属于会话。
	 * 
	 */
	@Override
	public GetChatGroupListResponse getChatGroupList(GetChatGroupListCommand cmd) {

		// 检查权限
		// if(configProvider.getBooleanValue("privilege.community.checkflag",
		// true)){
		// userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(),
		// cmd.getCurrentPMId(), 4030040300L, cmd.getAppId(),
		// null,cmd.getCurrentProjectId());//订单记录权限
		// }

		// 获取namespaceId
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();

		// 获取页码数据
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();

		ListingLocator locator = new ListingLocator();
		locator.setAnchor(pageAnchor);
		// messageProvider.listMessageRecords(pageSize, locator, (a,b)->{});

		// 模拟数据
		List<ChatGroupDTO> dtoList = getChatGroupList(namespaceId, pageSize, pageAnchor, cmd.getServicerId(),
				cmd.getKeyword());

		// if (list.size() > pageSize) {
		// nextPageAnchor += 1;
		// list.remove(list.size() - 1);
		// } else {
		// nextPageAnchor = null;
		// }

		GetChatGroupListResponse rsp = new GetChatGroupListResponse();
		rsp.setNextPageAnchor(pageAnchor + 1);
		rsp.setChatGroupList(dtoList);

		return rsp;
	}

	/*
	 * @see
	 * com.everhomes.techpark.servicehotline.HotlineService#getChatRecordList(
	 * com.everhomes.rest.servicehotline.GetChatRecordListCommand)
	 * 查询客服id与用户id的聊天记录
	 * 
	 */
	public GetChatRecordListResponse getChatRecordList(GetChatRecordListCommand cmd) {
		// 检查权限
		// if(configProvider.getBooleanValue("privilege.community.checkflag",
		// true)){
		// userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(),
		// cmd.getCurrentPMId(), 4030040300L, cmd.getAppId(),
		// null,cmd.getCurrentProjectId());//订单记录权限
		// }

		// 获取页码数据
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();
		List<ChatRecordDTO> chatRecordList = getChatRecordList(pageSize, pageAnchor, cmd.getServicerId(),
				cmd.getCustomerId());

		GetChatRecordListResponse rsp = new GetChatRecordListResponse();
		rsp.setNextPageAnchor(pageAnchor + 1);
		rsp.setChatRecordList(chatRecordList);
		return rsp;
	}

	/**
	 * private方法。用于查询会话列表逻辑
	 */
	private List<ChatGroupDTO> getChatGroupList(Integer namespaceId, Integer pageSize, Long pageAnchor, Long servicerId,
			String keyword) {

		ChatGroupDTO dto = new ChatGroupDTO();
		TargetDTO servicer = userProvider.findUserByToken("19107797118", namespaceId); // 晁倩2
		TargetDTO customer = userProvider.findUserByToken("13816368413", namespaceId);// 科技园官方帐号

		dto.setServicer(servicer);
		dto.setCustomer(customer);

		List<ChatGroupDTO> dtoList = new ArrayList<>(pageSize);
		dtoList.add(dto);

		return dtoList;
	}

	/**
	 * private方法。用于查询某个会话的聊天记录
	 */
	private List<ChatRecordDTO> getChatRecordList(Integer pageSize, Long pageAnchor, Long servicerId, Long customerId) {

		List<ChatRecordDTO> chatRecordDtos = new ArrayList<>(pageSize);
		ChatRecordDTO dto = null;
		long nowTime = System.currentTimeMillis();
		for (int i = 0; i < pageSize; i++, nowTime++) {

			String senderName = (i % 2 == 0) ? "晁倩2" : "科技园官方帐号";
			dto = new ChatRecordDTO();
			dto.setSenderName(senderName);
			dto.setMessageType(ChatMessageType.TEXT.getCode());
			Timestamp now = new Timestamp(nowTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(now);
			dto.setMessage("msg" + i + " nowTime:" + time);
			dto.setSendTime(now);

			chatRecordDtos.add(dto);
		}

		return chatRecordDtos;
	}

}
