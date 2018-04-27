package com.everhomes.servicehotline;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.servicehotline.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
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
import com.everhomes.message.MessageRecord;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.message.MessageRecordSenderTag;
import com.everhomes.rest.message.MessageRecordStatus;
import com.everhomes.rest.messaging.ImageBody;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhMessageRecords;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.techpark.servicehotline.ServiceConfiguration;
import com.everhomes.techpark.servicehotline.ServiceConfigurationsProvider;
import com.everhomes.techpark.servicehotline.ServiceHotline;
import com.everhomes.techpark.servicehotline.ServiceHotlinesProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

import freemarker.template.utility.StringUtil;

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
	
	@Autowired
	private CommunityProvider communityProvider;		
		
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
		
		//设置分页
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<HotlineDTO> hotlines = getHotlineList(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getPageSize(),
				locator, cmd.getServiceType());
		
		//构造返回
		GetHotlineListResponse resp = new GetHotlineListResponse();
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
	 * 根据条件获取会话列表。 相同的两个人的会话定义为一个会话。
	 * 例： 客服A与用户A的有100条聊天记录，归为 “客服A-用户A”一个会话
	 * 客服A与用户B的有1条聊天记录，归为 “客服A-用户B”一个会话 
	 * 客服A与用户C的无聊天记录，不属于会话。
	 * 注意：锚点的意义
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
		
		// 设置锚点
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		
		//获取所有客服id
		locator.setEntityId(cmd.getServicerId() == null ? 0 : cmd.getServicerId());
		List<HotlineDTO> hotlines = getHotlineList(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), pageSize,
				locator, ServiceType.ZHUANSHU_SERVICE.getCode());
		if (CollectionUtils.isEmpty(hotlines)) {
			throw RuntimeErrorException.errorWith(HotlineErrorCode.SCOPE,
					HotlineErrorCode.ERROR_HOTLINE_NOT_FOUND, "hotline list not found");
		}

		//转成客服id列表
		List<Long> servicerIds = new ArrayList<>(10);
		hotlines.stream().forEach(r -> {
			servicerIds.add(r.getUserId());
		});
		
		//获取所有用户
		String keyword =  cmd.getKeyword();
		List<TargetDTO> customerDtoList = findUserByTokenOrNickName(keyword, namespaceId);
			
		//转成用户id列表
		List<Long> customerIds = new ArrayList<>(10);
		if (!CollectionUtils.isEmpty(customerDtoList)) {
			customerDtoList.stream().forEach(r->{ customerIds.add(r.getTargetId());});
		}

		// 获取消息
		locator.setEntityId(0);//将之前的设置清零
		List<ChatGroupDTO> dtoList = getChatGroupList(namespaceId, pageSize, locator, servicerIds, customerIds);

		// 设置返回数据
		GetChatGroupListResponse rsp = new GetChatGroupListResponse();
		rsp.setNextPageAnchor(locator.getAnchor());
		rsp.setChatGroupList(dtoList);

		return rsp;
	}

	/*
	 * @see
	 * com.everhomes.techpark.servicehotline.HotlineService#getChatRecordList(
	 * com.everhomes.rest.servicehotline.GetChatRecordListCommand)
	 * 根据客服id与用户id查询聊天记录
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
		
		// 获取客服和用户
		String servicerName = "servicer-"+cmd.getServicerId();
		String customerName = "customer-"+cmd.getCustomerId();
		
		// 获取聊天记录
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<ChatRecordDTO> chatRecordList = getChatRecordList(pageSize, locator, cmd.getServicerId(),
				cmd.getCustomerId(), servicerName,  customerName);

		//设置返回数据
		GetChatRecordListResponse rsp = new GetChatRecordListResponse();
		rsp.setNextPageAnchor(locator.getAnchor());
		rsp.setChatRecordList(chatRecordList);
		return rsp;
	}

	/**
	 * private方法。用于查询会话列表逻辑
	 */
	private List<ChatGroupDTO> getChatGroupList(Integer namespaceId, Integer pageSize, ListingLocator locator, List<Long> servicerIds,
			List<Long> customerIds) {
		
		//获取会话列表
		List<MessageRecord> msgRecList = messageProvider.listMessageRecords(pageSize, locator, (l,q)->{
			return buildChatGroupQuery(l,q,pageSize,servicerIds, customerIds);
		});
		
		ChatGroupDTO dto = new ChatGroupDTO();
		for (MessageRecord msg : msgRecList) {
//			if () {
//				
//			}
			TargetDTO servicer = userProvider.findUserByToken("19107797118", namespaceId); // 晁倩2
			TargetDTO customer = userProvider.findUserByToken("12000003208", namespaceId);// 科技园官方帐号
			dto.setServicer(servicer);
			dto.setCustomer(customer);
		}

		List<ChatGroupDTO> dtoList = new ArrayList<>(pageSize);
		dtoList.add(dto);

		return dtoList;
	}

	/**
	 * private方法。用于查询某个会话的聊天记录
	 */
	private List<ChatRecordDTO> getChatRecordList(Integer pageSize, ListingLocator locator, Long servicerId,
			Long customerId, String servicerName, String customerName) {

		// 获取会话列表
		List<MessageRecord> msgRecList = messageProvider.listMessageRecords(pageSize, locator, (l, q) -> {
			return buildChatRecordQuery(l, q, pageSize, servicerId, customerId);
		});

		ChatRecordDTO dto = null;
		List<ChatRecordDTO> chatRecordDtos = new ArrayList<>(pageSize);
		for (MessageRecord msg : msgRecList) {
			dto = new ChatRecordDTO();
			if (msg.getSenderUid().equals(servicerId)) {
				dto.setSenderName(servicerName);
			} else {
				dto.setSenderName(customerName);
			}

			// 根据类型获取不同格式的内容
			setChatMessage(dto, msg);
			dto.setSendTime(msg.getCreateTime());
			chatRecordDtos.add(dto);
		}

		return chatRecordDtos;
	}
	
	
	/**
	 * 根据客服id与用户id导出聊天记录
	 */
	public void exportChatRecordList(GetChatRecordListCommand cmd) {
		
	}
	
	/**
	 * 根据条件导出相应聊天记录
	 */
	public void exportMultiChatRecordList(GetChatGroupListCommand cmd) {
		
	}
	
	
	/**
	 * 聊天记录的sql逻辑。只要发送了，就算是聊天记录。
	 * 即
	 * status = 'CORE_HANDLE'
	 * sender_tag = 'ROUTE MESSAGE'
	 * 注：index_id相同的表示同一条消息。且数字越大消息越新
	 * @param locator 分页器
	 * @param query 查询handler
	 * @param servicerId 客服id
	 * @param customerId 用户id
	 * @return
	 */
	private SelectQuery<? extends Record> buildChatRecordQuery(ListingLocator locator,
			SelectQuery<? extends Record> query, Integer pageSize, Long servicerId, Long customerId) {

		final String TYPE_USER = "user";

		// 对当前表取别名
		EhMessageRecords records = Tables.EH_MESSAGE_RECORDS;

		// 条件1 客服发送的
		Condition sendByServicer = records.SENDER_UID.eq(servicerId)
				.and(records.DST_CHANNEL_TYPE.eq(TYPE_USER).and(records.DST_CHANNEL_TOKEN.eq("" + customerId)));

		// 条件2 用户发送的
		Condition sendByCustomer = records.SENDER_UID.eq(customerId)
				.and(records.DST_CHANNEL_TYPE.eq(TYPE_USER).and(records.DST_CHANNEL_TOKEN.eq("" + servicerId)));

		// 条件3 固定条件: status = 'CORE_HANDLE', sender_tag = 'ROUTE MESSAGE'
		Condition fixedCond = records.STATUS.eq(MessageRecordStatus.CORE_HANDLE.getCode())
				.and(records.SENDER_TAG.eq(MessageRecordSenderTag.ROUTE_MESSAGE.getCode()));

		// 添加条件
		query.addConditions(sendByServicer.or(sendByCustomer));
		query.addConditions(fixedCond);

		// 做分组，排序，分页
		query.addGroupBy(records.INDEX_ID);
		query.addOrderBy(records.INDEX_ID.desc());

		return query;
	}
	
	
	/**
	 * 获取会话列表的sql逻辑
	 * 即
	 * status = 'CORE_HANDLE'
	 * sender_tag = 'ROUTE MESSAGE'
	 * 注：这种消息可能有多条，index_id为唯一标识
	 * @param locator 分页器
	 * @param query 查询handler
	 * @param servicerId 客服id
	 * @param customerId 用户id
	 * @return
	 */
	private SelectQuery<? extends Record> buildChatGroupQuery(ListingLocator locator,
			SelectQuery<? extends Record> query, Integer pageSize, List<Long> servicerIds, List<Long> customerIds) {
		
		// 必须有客服id做筛选条件
		if (null == servicerIds || servicerIds.isEmpty()) {
			throw RuntimeErrorException.errorWith(HotlineErrorCode.SCOPE,
					HotlineErrorCode.ERROR_HOTLINE_SERVICER_KEY_INVALID, "servicer's id as searching key not exist");
		}
		
		final String TYPE_USER = "user";
		
		// 对当前表取别名
		EhMessageRecords records = Tables.EH_MESSAGE_RECORDS;
		
		// 条件1 客服发送的
		Condition sendByServicer = records.SENDER_UID.in(servicerIds);
		
		// 条件2 用户发送的
		Condition sendByCustomer = records.DST_CHANNEL_TYPE.eq(TYPE_USER).
				and(records.DST_CHANNEL_TOKEN.in(CollectionUtils.collect(servicerIds,(i)->{return i+"";})));
		
		if (null != customerIds && !customerIds.isEmpty()) {
			sendByServicer = sendByServicer.and(
					records.DST_CHANNEL_TYPE.eq(TYPE_USER).
						and(records.DST_CHANNEL_TOKEN.in(CollectionUtils.collect(customerIds,(i)->{return i+"";}))));
			sendByCustomer = sendByCustomer.and(records.SENDER_UID.in(customerIds));
		}		
				
		// 条件3 固定条件: status = 'CORE_HANDLE', sender_tag = 'ROUTE MESSAGE'
		Condition fixedCond = records.STATUS.eq(MessageRecordStatus.CORE_HANDLE.getCode())
				.and(records.SENDER_TAG.eq(MessageRecordSenderTag.ROUTE_MESSAGE.getCode()));

		// 添加条件
		query.addConditions(sendByServicer.or(sendByCustomer));
		query.addConditions(fixedCond);
		
		// 做分组，排序，分页
		query.addGroupBy(
				DSL.decode()
				.when(records.SENDER_UID.gt(records.DST_CHANNEL_TOKEN.cast(Long.class)) ,  
						   DSL.concat(records.DST_CHANNEL_TOKEN.toString(), "_", records.SENDER_UID.toString()))
				.otherwise(DSL.concat(records.SENDER_UID.toString(), "_", records.DST_CHANNEL_TOKEN.toString())));
		
		query.addOrderBy(records.INDEX_ID.desc());

		return query;
	}
	
	
	
	/**
	 * 设置聊天记录（ChatRecordDTO）的message，messageType字段
	 * @param chatRecord 需要设置的聊天记录
	 * @param msgRecord 数据库中的消息记录
	 */
	private final void setChatMessage(ChatRecordDTO chatRecord, MessageRecord msgRecord) {
				
		// 1.IMAGE body类型为ImageBody，这里不做解析，直接使用url字段
		if (MessageBodyType.IMAGE == MessageBodyType.fromCode(msgRecord.getBodyType())) {
			/*
			body格式
		  	{
			    "fileSize":16541,
			    "filename":"9f2f070828381fx63.jpg",
			    "format":"image/jpeg",
			    "url":"http://xxxx:5000/image/axxxRQ?token=6Gxxl",
			    "uri":"cs://1/image/aW1xxxxUQ",
			    "height":405,
			    "width":616
			}
			  */
			JSONObject json = JSONObject.parseObject(msgRecord.getBody());
			chatRecord.setMessage(json.getString("url"));
			chatRecord.setMessageType(ChatMessageType.IMAGE.getCode());
			return;
		}
		
		
		// 2. AUDIO类型，方法同IMAGE,语音内容暂不需要显示在前端
		if (MessageBodyType.AUDIO == MessageBodyType.fromCode(msgRecord.getBodyType()))  {
			
			/*
			body格式
			{
			    "duration":"2",
			    "fileSize":0,
			    "filename":"1524571892706.m4a",
			    "format":"audio/m4a",
			    "uri":"cs://1/audio/aW1xxxxUQ",
			    "url":"http://xxxx:5000/audio/axxxRQ?token=6Gxxl"	
			}
			*/
//			JSONObject json = JSONObject.parseObject(msgRecord.getBody());
//			chatRecord.setMessage(json.getString("url"));
			chatRecord.setMessageType(ChatMessageType.AUDIO.getCode());
			return ;
		}
		
		//其他都默认为text类型
		chatRecord.setMessageType(ChatMessageType.TEXT.getCode());
		chatRecord.setMessage(msgRecord.getBody());
	}
    
	
	private List<HotlineDTO> getHotlineList(Integer namespaceId, String ownerType, Long ownerId, Integer pageSize,
			ListingLocator locator, Byte serviceType) {
		
		int maxPageSize = null == pageSize ? Integer.MAX_VALUE - 1 : pageSize;

		// 获取相应热线
		List<ServiceHotline> serviceHotlines = serviceHotlinesProvider.queryServiceHotlines(locator, maxPageSize,
				(l, query) -> {
					query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_TYPE.eq(ownerType));
					query.addConditions(Tables.EH_SERVICE_HOTLINES.OWNER_ID.eq(ownerId));
					query.addConditions(Tables.EH_SERVICE_HOTLINES.NAMESPACE_ID.eq(namespaceId));
					query.addConditions(Tables.EH_SERVICE_HOTLINES.SERVICE_TYPE.eq(serviceType.intValue()));

					if (0 != l.getEntityId()) {
						query.addConditions(Tables.EH_SERVICE_HOTLINES.ID.eq(l.getEntityId()));
					}

					return query;
				});
		
		//未找到返回
		List<HotlineDTO> hotlines = new ArrayList<HotlineDTO>(16);
		if (CollectionUtils.isEmpty(serviceHotlines)) {
			return hotlines;
		}

		//找到后进行转换
		serviceHotlines.forEach(r -> {
			HotlineDTO dto = ConvertHelper.convert(r, HotlineDTO.class);
			if (null != r.getUserId()) {
				User user = this.userProvider.findUserById(r.getUserId());
				if (null != user)
					dto.setAvatar(populateUserAvatar(user, user.getAvatar()));
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(),
						IdentifierType.MOBILE.getCode());
				dto.setPhone(userIdentifier.getIdentifierToken());
			}
			hotlines.add(dto);
		});

		return hotlines;
	}
	
	/**
	 * 根据传入的手机号或用户名获取用户列表
	 * @param keyword 必填
	 * @param namespaceId 必填
	 * @return
	 */
	private List<TargetDTO> findUserByTokenOrNickName(String keyword, int namespaceId) {
		
		if (StringUtils.isBlank(keyword)) {
			return null;
		}
		
		// 1.先对手机号进行搜索
		List<TargetDTO> targetList = new ArrayList<>(10);
		TargetDTO target = null;
		if (HotlineUtils.isPhoneNumber(keyword)) {
			target = userProvider.findUserByToken(keyword, namespaceId);
			if (null != target) {
				targetList.add(target);
				return targetList;
			}
		}
		
		// 2.如果不是手机号，或者手机号未找到。都需要通过nickName搜索。
		// 因为有人可能把昵称设置成数字号码了
		target = userProvider.findUserByTokenAndName(null, keyword);
		targetList.add(target);
		return targetList;
	}
	
//	
//	private TargetDTO getHotlineInfo() {
//		
//	}
	
	
}
