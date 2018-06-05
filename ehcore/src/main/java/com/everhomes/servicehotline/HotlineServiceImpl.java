package com.everhomes.servicehotline;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.message.MessageProvider;
import com.everhomes.message.MessageRecord;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.message.MessageRecordSenderTag;
import com.everhomes.rest.message.MessageRecordStatus;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.servicehotline.AddHotlineCommand;
import com.everhomes.rest.servicehotline.ChatGroupDTO;
import com.everhomes.rest.servicehotline.ChatMessageType;
import com.everhomes.rest.servicehotline.ChatRecordDTO;
import com.everhomes.rest.servicehotline.DeleteHotlineCommand;
import com.everhomes.rest.servicehotline.GetChatGroupListCommand;
import com.everhomes.rest.servicehotline.GetChatGroupListResponse;
import com.everhomes.rest.servicehotline.GetChatRecordListCommand;
import com.everhomes.rest.servicehotline.GetChatRecordListResponse;
import com.everhomes.rest.servicehotline.GetHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListResponse;
import com.everhomes.rest.servicehotline.GetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.GetHotlineSubjectResponse;
import com.everhomes.rest.servicehotline.GetUserInfoByIdCommand;
import com.everhomes.rest.servicehotline.GetUserInfoByIdResponse;
import com.everhomes.rest.servicehotline.HotlineDTO;
import com.everhomes.rest.servicehotline.HotlineErrorCode;
import com.everhomes.rest.servicehotline.PrivilegeType;
import com.everhomes.rest.servicehotline.HotlineSubject;
import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.rest.servicehotline.ServiceType;
import com.everhomes.rest.servicehotline.SetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.UpdateHotlineCommand;
import com.everhomes.rest.servicehotline.UpdateHotlinesCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhMessageRecords;
import com.everhomes.server.schema.tables.EhUserIdentifiers;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.techpark.servicehotline.ServiceConfiguration;
import com.everhomes.techpark.servicehotline.ServiceConfigurationsProvider;
import com.everhomes.techpark.servicehotline.ServiceHotline;
import com.everhomes.techpark.servicehotline.ServiceHotlinesProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.TextUtils;

@Component
public class HotlineServiceImpl implements HotlineService {
	private static final Logger LOGGER = LoggerFactory.getLogger(HotlineServiceImpl.class);
	
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
	private TaskService taskService; //用于文件下载中心
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Override
	public GetHotlineSubjectResponse getHotlineSubject(GetHotlineSubjectCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		List<ServiceConfiguration> sConfigurations = serviceConfigurationsProvider.queryServiceConfigurations(null, 1,
				new ListingQueryBuilderCallback() {

					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE.eq(cmd.getOwnerType()));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID.eq(cmd.getOwnerId()));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME.eq(HOTLINE_SCOPE));
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
			if (ServiceType.ZHUANSHU_SERVICE.getCode()
					.byteValue() == (ServiceType.ZHUANSHU_SERVICE.getCode().byteValue()
							& Byte.valueOf(configuration.getValue()))) {
				HotlineSubject service1 = new HotlineSubject();
				service1.setLayoutType(LayoutType.ZHUANSHU_SERVICE.getCode());
				service1.setServiceType(ServiceType.ZHUANSHU_SERVICE.getCode());
				service1.setTitle(configuration.getDisplayName());
				response.getSubjects().add(service1);
			}
			// 新增的类型在后面加,仿照专属客服
		}
		setShowSubjects(cmd, response);
		return response;
	}

	private void setShowSubjects(GetHotlineSubjectCommand cmd, GetHotlineSubjectResponse response) {
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		List<ServiceConfiguration> sConfigurations = serviceConfigurationsProvider.queryServiceConfigurations(null, 1,
				new ListingQueryBuilderCallback() {

					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE.eq(cmd.getOwnerType()));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID.eq(cmd.getOwnerId()));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
						query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME.eq(HOTLINE_NOTSHOW_SCOPE));
						return query;
					}
				});
		Set<String> set = new HashSet<>();
		if (sConfigurations != null && sConfigurations.size() > 0)
			sConfigurations.stream().forEach(r -> {
				set.add(r.getDisplayName());
			});
		response.setShowSubjecs(new ArrayList<>());
		response.getSubjects().stream().forEach(r -> {
			if (!set.contains(r.getTitle()))
				response.getShowSubjecs().add(r.getTitle());
		});
	}

	@Override
	public GetHotlineListResponse getHotlineList(GetHotlineListCommand cmd) {
		return getHotlineList(cmd, false);
	}
	
	/**
	 * 后台获取热线/客服列表
	 * @param cmd
	 * @return
	 */
	@Override
	public GetHotlineListResponse getHotlineListAdmin(GetHotlineListCommand cmd) {
		return getHotlineList(cmd, true);
	}
	
	private GetHotlineListResponse getHotlineList(GetHotlineListCommand cmd, boolean needCheckPrivilege) {

		//需要鉴权时
		if (needCheckPrivilege) {
			if (ServiceType.SERVICE_HOTLINE.getCode().equals(cmd.getServiceType())) {
				checkPrivilege(PrivilegeType.PUBLIC_HOTLINE, cmd.getCurrentPMId(), cmd.getAppId(),
						cmd.getCurrentProjectId());
			} else {
				checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICER_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(),
						cmd.getCurrentProjectId());
			}
		}

		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();

		// 设置分页
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<HotlineDTO> hotlines = getActiveHotlineList(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getPageSize(), locator, null, cmd.getServiceType());

		// 构造返回
		GetHotlineListResponse resp = new GetHotlineListResponse();
		resp.setHotlines(hotlines);
		return resp;
	}
	

	private String populateUserAvatar(User user, String avatarUri) {
		if (avatarUri == null || avatarUri.trim().length() == 0) {
			avatarUri = userService.getUserAvatarUriByGender(user.getId(), user.getNamespaceId(), user.getGender());
		}
		try {
			String url = contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), user.getId());
			return url;
		} catch (Exception e) {
			// LOGGER.error("Failed to parse avatar uri, userId=" + user.getId()
			// + ", avatar=" + avatarUri);
		}
		return null;
	}

	@Override
	public void addHotline(AddHotlineCommand cmd) {
		
		if (ServiceType.SERVICE_HOTLINE.getCode().equals(cmd.getServiceType())) {
			checkPrivilege(PrivilegeType.PUBLIC_HOTLINE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		} else {
			checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICER_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		}

		
		ServiceHotline hotline = ConvertHelper.convert(cmd, ServiceHotline.class);
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		
		//检查参数

		// 查询是否有重复的
		ServiceHotline tmp = getSingleHotlineIfExist(hotline);
		if (null != tmp) {
			// 热线直接报错
			if (ServiceType.SERVICE_HOTLINE.getCode().equals(hotline.getServiceType().byteValue())) {
				throwErrorCode(HotlineErrorCode.ERROR_DUPLICATE_PHONE);
				return;
			}

			// 如果激活状态下有相同的手机号，也需要报错
			if (HotlineStatus.ACTIVE.getCode() == tmp.getStatus()) {
				throwErrorCode(HotlineErrorCode.ERROR_DUPLICATE_PHONE);
				return;
			}
			// 存在有相同userId的客服时，对该记录进行更新
			//这里不退出，在下面进行判断更新还是添加记录
			
		}

		hotline.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		hotline.setCreatorUid(UserContext.current().getUser().getId());
		hotline.setNamespaceId(namespaceId);
		hotline.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		hotline.setStatus(HotlineStatus.ACTIVE.getCode());
		
		if (null != tmp) {//已删除的专属客服重新被添加时，进入此分支
			hotline.setId(tmp.getId());
			serviceHotlinesProvider.updateServiceHotline(hotline);
			return;
		}
		
		serviceHotlinesProvider.createServiceHotline(hotline);
		return;

	}

	@Override
	public void deleteHotline(DeleteHotlineCommand cmd) {
		
		if (null == cmd) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter id can not be null or hotline can not found");
		}
		
		ServiceHotline hotline = serviceHotlinesProvider.getServiceHotlineById(cmd.getId());
		if( null == hotline)  {
			throwErrorCode(HotlineErrorCode.ERROR_HOTLINE_UPDATING_NOT_EXIST);
		}
		
		//检查权限
		if (ServiceType.SERVICE_HOTLINE.getCode().equals(hotline.getServiceType().byteValue())) {
			checkPrivilege(PrivilegeType.PUBLIC_HOTLINE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		} else {
			checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICER_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		}
		
		// 专属客服将状态标识为改为0即可
		if (ServiceType.ZHUANSHU_SERVICE.getCode().equals(hotline.getServiceType().byteValue())) {
			hotline.setStatus(HotlineStatus.DELETED.getCode());
			serviceHotlinesProvider.updateServiceHotline(hotline);
			return;
		}
		
		//其他直接删除
		serviceHotlinesProvider.deleteServiceHotline(hotline);
	}

	@Override
	public void updateHotline(UpdateHotlineCommand cmd) {
		
		if (null == cmd) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter");
		}
		
		ServiceHotline record = serviceHotlinesProvider.getServiceHotlineById(cmd.getId());
		if (null == record) {
			throwErrorCode(HotlineErrorCode.ERROR_HOTLINE_UPDATING_NOT_EXIST);
		}
		
		//检查权限
		if (ServiceType.SERVICE_HOTLINE.getCode().equals(record.getServiceType().byteValue())) {
			checkPrivilege(PrivilegeType.PUBLIC_HOTLINE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		} else {
			checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICER_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		}
		
		
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
		
		ServiceHotline hotline = ConvertHelper.convert(cmd, ServiceHotline.class);
		
		// 查询是否有重复的
		List<ServiceHotline> tmp = getMultiHotlineIfExist(hotline);
		
		//排除自己
		tmp = tmp.stream().filter(p -> !p.getId().equals(cmd.getId())).collect(Collectors.toList());
		
		// 仍有记录说明有重复的，需报错
		if (null != tmp && tmp.size() > 0) {
			throwErrorCode(HotlineErrorCode.ERROR_DUPLICATE_PHONE);
		}

		hotline.setNamespaceId(namespaceId);
		hotline.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
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
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
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
					.queryServiceConfigurations(null, 1, new ListingQueryBuilderCallback() {

						@Override
						public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
								SelectQuery<? extends Record> query) {
							query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_TYPE.eq(cmd.getOwnerType()));
							query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.OWNER_ID.eq(cmd.getOwnerId()));
							query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
							query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.NAME.eq(HOTLINE_SCOPE));
							query.addConditions(Tables.EH_SERVICE_CONFIGURATIONS.VALUE.eq(cmd.getServiceType() + ""));
							return query;
						}
					}).get(0);
			serviceConfigurationsProvider.deleteServiceConfiguration(sConfiguration);
			break;

		}
	}

	@Override
	public void updateHotlineOrder(UpdateHotlinesCommand cmd) {
		
		//检查权限
		if (ServiceType.SERVICE_HOTLINE.getCode().equals(cmd.getServiceType().byteValue())) {
			checkPrivilege(PrivilegeType.PUBLIC_HOTLINE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		} else {
			checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICER_MANAGE, cmd.getCurrentPMId(), cmd.getAppId(), cmd.getCurrentProjectId());
		}
		
		if (cmd.getHotlines() == null)
			return;
		this.dbProvider.execute((TransactionStatus status) -> {
			for (UpdateHotlineCommand cmd1 : cmd.getHotlines()) {
				ServiceHotline hotline = this.serviceHotlinesProvider.getServiceHotlineById(cmd1.getId());
				if (null == hotline)
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid paramter   hotline can not found id = " + cmd1.getId());
				hotline.setDefaultOrder(cmd1.getDefaultOrder());
				hotline.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				this.serviceHotlinesProvider.updateServiceHotline(hotline);
			}
			return null;
		});
	}

	@Override
	public GetUserInfoByIdResponse getUserInfoById(GetUserInfoByIdCommand cmd) {
		User queryUser = userProvider.findUserById(cmd.getId());
		if (queryUser == null) {
			return null;
		}
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(queryUser.getId());
		List<String> phones = identifiers.stream().filter((r) -> {
			return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE;
		}).map(r -> r.getIdentifierToken()).collect(Collectors.toList());
		GetUserInfoByIdResponse info = ConvertHelper.convert(queryUser, GetUserInfoByIdResponse.class);
		info.setPhones(phones);

		if (cmd.getOrgId() != null) {
			OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(cmd.getId(),
					cmd.getOrgId());
			if (member != null)
				info.setContractName(member.getContactName());
		}
		return info;
	}

	/*
	 * 根据条件获取会话列表。 相同的两个人的会话定义为一个会话。 
	 * 例： 
	 * 客服A与用户A的有100条聊天记录，归为 “客服A-用户A”一个会话
	 * 客服A与用户B的有1条聊天记录，归为 “客服A-用户B”一个会话 
	 * 客服A与用户C的无聊天记录，不属于会话。 
	 * 注意：锚点的意义——下次查找时起始id
	 */
	@Override
	public GetChatGroupListResponse getChatGroupList(GetChatGroupListCommand cmd) {

		// 检查权限
		checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICE_CHAT_RECORD, cmd.getCurrentPMId(), cmd.getAppId(),
				cmd.getCurrentProjectId());

		// 获取namespaceId
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();

		// 获取页码数据
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		// 设置锚点
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		// 获取所有客服id
		List<TargetDTO> servicerDtos = null;
		if (cmd.getServicerId() == null) {
			// 未传客服id时，认为是查询全部客服，包括被删除的客服
			servicerDtos = getMultiExclusiveServicer(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), true);
		} else {
			// 传了客服id，认为是指定客服
			TargetDTO servicer = getSingleExclusiveServicer(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getServicerId());
			if (null != servicer) {
				servicerDtos = new ArrayList<>(1);
				servicerDtos.add(servicer);
			}
		}

		// 未找到相应客服，直接返回
		if (CollectionUtils.isEmpty(servicerDtos)) {
			return new GetChatGroupListResponse();
		}

		// 根据keywork获得用户
		List<TargetDTO> customerDtos = null;
		if (!StringUtils.isBlank(cmd.getKeyword())) {
			customerDtos = findUserByTokenOrNickName(cmd.getKeyword(), namespaceId);
			if (CollectionUtils.isEmpty(customerDtos)) {
				return new GetChatGroupListResponse();
			}
		}
		

		// 获取消息
		List<ChatGroupDTO> dtoList = getChatGroupList(namespaceId, pageSize, locator, servicerDtos, customerDtos);

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
		checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICE_CHAT_RECORD, cmd.getCurrentPMId(), cmd.getAppId(),
				cmd.getCurrentProjectId());
		
		// 获取namespaceId

		// 判空
		if (null == cmd.getServicerId()) {
			throwErrorCode(HotlineErrorCode.ERROR_HOTLINE_SERVICER_KEY_INVALID);
		}

		if (null == cmd.getCustomerId()) {
			throwErrorCode(HotlineErrorCode.ERROR_HOTLINE_CUSTOMER_KEY_INVALID);
		}

		// 获取页码数据
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		// 获取客服和用户
		TargetDTO servicerDto = getSingleExclusiveServicer(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getServicerId());
		if (null == servicerDto) {
			return new GetChatRecordListResponse();
		}

		TargetDTO customerDto = findUserNameIdentifierById(cmd.getCustomerId());
		if (null == customerDto) {
			return new GetChatRecordListResponse();
		}

		// 获取聊天记录
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<ChatRecordDTO> chatRecordList = getChatRecordList(pageSize, locator, cmd.getNamespaceId(), servicerDto, customerDto);

		// 设置返回数据
		GetChatRecordListResponse rsp = new GetChatRecordListResponse();
		rsp.setNextPageAnchor(locator.getAnchor());
		rsp.setChatRecordList(chatRecordList);
		return rsp;
	}

	/**
	 * private方法。用于查询会话列表逻辑
	 */
	private List<ChatGroupDTO> getChatGroupList(Integer namespaceId, Integer pageSize, ListingLocator locator,
			List<TargetDTO> servicerDtos, List<TargetDTO> customerDtos) {

		// 1.必须有客服做筛选条件
		if (CollectionUtils.isEmpty(servicerDtos)) {
			throwErrorCode(HotlineErrorCode.ERROR_HOTLINE_SERVICER_KEY_INVALID);
		}

		// 2.1将客服转成id列表
		List<Long> servicerIds = new ArrayList<>(10);
		if (!CollectionUtils.isEmpty(servicerDtos)) {
			servicerDtos.forEach(r -> {
				servicerIds.add(r.getTargetId());
			});
		}

		// 2.2将用户转成id列表
		List<Long> customerIds = new ArrayList<>(10);
		if (!CollectionUtils.isEmpty(customerDtos)) {
			customerDtos.forEach(r -> {
				customerIds.add(r.getTargetId());
			});
		}

		// 3.获取会话列表
		List<MessageRecord> msgRecList = messageProvider.listMessageRecords(pageSize, locator, (l, q) -> {
			return buildChatGroupQuery(l, q, pageSize, servicerIds, customerIds);
		});

		List<ChatGroupDTO> dtoList = new ArrayList<>(10);
		ChatGroupDTO chatGroup = null;
		for (MessageRecord msg : msgRecList) {
			chatGroup = new ChatGroupDTO();
			setChatGroup(chatGroup, msg, servicerDtos, customerDtos);
			dtoList.add(chatGroup);
		}

		return dtoList;
	}

	/**
	 * 获取聊天记录的逻辑处理
	 */
	private List<ChatRecordDTO> getChatRecordList(Integer inputPageSize, ListingLocator locator, Integer namespaceId, TargetDTO servicerDto,
			TargetDTO customerDto) {

		// 判空
		if (null == servicerDto || null == customerDto || null == servicerDto.getTargetId()
				|| null == customerDto.getTargetId()) {
			return null;
		}

		// 获取客服发送列表
		Long anchor = locator.getAnchor();
		int pageSize = null == inputPageSize ? 0 : inputPageSize;
		List<MessageRecord> servicerList = messageProvider.listMessageRecords(pageSize + 1, locator, (l, q) -> {
			return buildChatRecordQuery(l, q, pageSize + 1, namespaceId, servicerDto.getTargetId(), customerDto.getTargetId());
		});
		
		// 获取用户发送列表
		List<MessageRecord> customerList = null;
		locator.setAnchor(anchor);
		if (!servicerDto.getTargetId().equals(customerDto.getTargetId())) {
			customerList = messageProvider.listMessageRecords(pageSize + 1, locator, (l, q) -> {
				return buildChatRecordQuery(l, q, pageSize + 1, namespaceId, customerDto.getTargetId(), servicerDto.getTargetId());
			});
		}
		
		// 合并记录
		List<MessageRecord> totalMsgList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(servicerList)) {
			totalMsgList.addAll(servicerList);
		}
		
		if (!CollectionUtils.isEmpty(customerList)) {
			totalMsgList.addAll(customerList);
		}
		
		// 判空
		if (CollectionUtils.isEmpty(totalMsgList)) {
			return null;
		}
		
		
		// 以index_id倒叙排
		Collections.sort(totalMsgList, (cmpA, cmpB) -> {
			return (int) (cmpB.getIndexId() - cmpA.getIndexId());
		});
		
		
		// 设置Anchor
		int finalMsgSize = totalMsgList.size();
        if (pageSize > 0 && totalMsgList.size() > pageSize) {
            locator.setAnchor(totalMsgList.get(pageSize).getId());
            finalMsgSize = pageSize;
        } else {
            locator.setAnchor(null);
        }
        
        
		ChatRecordDTO dto = null;
		List<ChatRecordDTO> chatRecordDtos = new ArrayList<>(50);
		for (int i = 0; i < finalMsgSize; i++) {
			dto = new ChatRecordDTO();

			// 填充查询结果
			setChatRecord(dto, totalMsgList.get(i), servicerDto, customerDto);
			chatRecordDtos.add(dto);
		}

		return chatRecordDtos;
	}

	/**
	 * 根据客服id与用户id导出聊天记录
	 */
	public void exportChatRecordList(GetChatRecordListCommand cmd, HttpServletResponse httpResponse) {
		
		checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICE_CHAT_RECORD, cmd.getCurrentPMId(), cmd.getAppId(),
				cmd.getCurrentProjectId());
		
		//获取记录
		if (null == cmd.getServicerId()) {
			throwErrorCode(HotlineErrorCode.ERROR_HOTLINE_SERVICER_KEY_INVALID);
		}

		if (null == cmd.getCustomerId()) {
			throwErrorCode(HotlineErrorCode.ERROR_HOTLINE_CUSTOMER_KEY_INVALID);
		}

		// 获取页码数据
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		// 获取客服和用户
		TargetDTO servicerDto = getSingleExclusiveServicer(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getServicerId());
		if (null == servicerDto) {
			return;
		}

		TargetDTO customerDto = findUserNameIdentifierById(cmd.getCustomerId());
		if (null == customerDto) {
			return;
		}

		// 获取聊天记录
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<ChatRecordDTO> chatRecordList = getChatRecordList(pageSize, locator, cmd.getNamespaceId(), servicerDto, customerDto);
		if (CollectionUtils.isEmpty(chatRecordList)) {
			return;
		}
		
		// 获取拼接内容
		List<String> dataList = createSingleChatGroupCotent(chatRecordList, servicerDto, customerDto);
		
		String fileName = servicerDto.getTargetName()+"与"+customerDto.getTargetName()+"（"+customerDto.getUserIdentifier()+"）的消息记录.txt";
		
		// 输出文档
		exportTxt(httpResponse, dataList, fileName);
		
		return;
	}

	/**
	 * 根据条件导出相应聊天记录
	 */
	public void exportMultiChatRecordList(GetChatGroupListCommand cmd, HttpServletResponse httpResponse) {

		// 检查权限
		checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICE_CHAT_RECORD, cmd.getCurrentPMId(), cmd.getAppId(),
				cmd.getCurrentProjectId());

		// 获取namespaceId
		Integer namespaceId = cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();

		// 获取页码数据
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		// 设置锚点
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		// 获取所有客服id
		List<TargetDTO> servicerDtos = null;
		if (cmd.getServicerId() == null) {
			// 未传客服id时，认为是查询全部客服，包括被删除的客服
			servicerDtos = getMultiExclusiveServicer(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(), true);
		} else {
			// 传了客服id，认为是指定客服
			TargetDTO servicer = getSingleExclusiveServicer(namespaceId, cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getServicerId());
			if (null != servicer) {
				servicerDtos = new ArrayList<>(1);
				servicerDtos.add(servicer);
			}
		}

		// 未找到相应客服，直接返回
		if (CollectionUtils.isEmpty(servicerDtos)) {
			return ;
		}

		// 根据keywork获得用户
		List<TargetDTO> customerDtos = null;
		if (!StringUtils.isBlank(cmd.getKeyword())) {
			customerDtos = findUserByTokenOrNickName(cmd.getKeyword(), namespaceId);
			if (CollectionUtils.isEmpty(customerDtos)) {
				return ;
			}
		}

		// 获取消息
		List<ChatGroupDTO> dtoList = getChatGroupList(namespaceId, pageSize, locator, servicerDtos, customerDtos);
		if (CollectionUtils.isEmpty(dtoList)) {
			return ;
		}
		
		List<String> dataList = new ArrayList<String>(1000);
		for (ChatGroupDTO group : dtoList) {
			// 获取聊天记录
			List<ChatRecordDTO> chatRecordList = getChatRecordList(pageSize, new ListingLocator(), namespaceId, group.getServicer(), group.getCustomer());
			if (CollectionUtils.isEmpty(chatRecordList)) {
				continue;
			}
			
			// 获取拼接内容
			List<String> tmpList = createSingleChatGroupCotent(chatRecordList, group.getServicer(),  group.getCustomer());
			if (CollectionUtils.isEmpty(chatRecordList)) {
				continue;
			}
			
			dataList.addAll(tmpList);
		}
		
		
		//获取文件名
		Community community = communityProvider.findCommunityById(cmd.getOwnerId());
		String communityName = null == community ? "" : community.getName();
		String fileName = communityName+"-消息记录.txt";
		
		//导出
		exportTxt(httpResponse, dataList, fileName);
		return ;
	}

	/**
	 * 聊天记录的sql逻辑。只要发送了，就算是聊天记录。 即 status = 'CORE_HANDLE' sender_tag = 'ROUTE
	 * MESSAGE' 注：index_id相同的表示同一条消息。且数字越大消息越新
	 * 
	 * @param locator
	 *            分页器
	 * @param query
	 *            查询handler
	 * @param servicerId
	 *            客服id
	 * @param customerId
	 *            用户id
	 * @return
	 */
	private SelectQuery<? extends Record> buildChatRecordQuery(ListingLocator locator,
			SelectQuery<? extends Record> query,  Integer pageSize, Integer namespaceId, Long senderUserId, Long receiverUserId) {

		final String TYPE_USER = "user";

		// 对当前表取别名
		EhMessageRecords records = Tables.EH_MESSAGE_RECORDS;
		
		// 增加域空间条件
		if (null != namespaceId) {
			query.addConditions(records.NAMESPACE_ID.eq(namespaceId));
		}

		// 构建发送者和接收者条件
		Condition sendCon = records.SENDER_UID.eq(senderUserId)
				.and(records.DST_CHANNEL_TYPE.eq(TYPE_USER).and(records.DST_CHANNEL_TOKEN.eq("" + receiverUserId)));


		// 固定条件: status = 'CORE_HANDLE', sender_tag = 'ROUTE MESSAGE'
		Condition fixedCond = records.STATUS.eq(MessageRecordStatus.CORE_HANDLE.getCode())
				.and(records.SENDER_TAG.eq(MessageRecordSenderTag.ROUTE_MESSAGE.getCode()));

		// 添加条件
		query.addConditions(sendCon);
		query.addConditions(fixedCond);

		// 做分组，排序，分页
		query.addGroupBy(records.INDEX_ID);
		query.addOrderBy(records.INDEX_ID.desc());

		return query;
	}

	/**
	 * 获取会话列表的sql逻辑 即 status = 'CORE_HANDLE' sender_tag = 'ROUTE MESSAGE'
	 * 注：这种消息可能有多条，index_id为唯一标识
	 * 
	 * @param locator
	 *            分页器
	 * @param query
	 *            查询handler
	 * @param servicerId
	 *            客服id
	 * @param customerId
	 *            用户id
	 * @return
	 */
	private SelectQuery<? extends Record> buildChatGroupQuery(ListingLocator locator,
			SelectQuery<? extends Record> query, Integer pageSize, List<Long> servicerIds, List<Long> customerIds) {

		final String TYPE_USER = "user";

		// 对当前表取别名
		EhMessageRecords records = Tables.EH_MESSAGE_RECORDS;

		// 客服发送
		Condition sendByServicer = records.SENDER_UID.in(servicerIds);

		// 客服接收
		Condition sendByCustomer = records.DST_CHANNEL_TYPE.eq(TYPE_USER)
				.and(records.DST_CHANNEL_TOKEN.in(CollectionUtils.collect(servicerIds, (i) -> {
					return i + "";
				})));

		if (null != customerIds && !customerIds.isEmpty()) {
			// 增加
			sendByServicer = sendByServicer.and(records.DST_CHANNEL_TYPE.eq(TYPE_USER)
					.and(records.DST_CHANNEL_TOKEN.in(CollectionUtils.collect(customerIds, (i) -> {
						return i + "";
					}))));
			sendByCustomer = sendByCustomer.and(records.SENDER_UID.in(customerIds));
		} else {
			// 去除系统用户 坑啊!
			sendByCustomer = sendByCustomer.and(records.SENDER_UID.greaterThan(User.MAX_SYSTEM_USER_ID));
		}

		// 条件3 固定条件: status = 'CORE_HANDLE', sender_tag = 'ROUTE MESSAGE'
		Condition fixedCond = records.STATUS.eq(MessageRecordStatus.CORE_HANDLE.getCode())
				.and(records.SENDER_TAG.eq(MessageRecordSenderTag.ROUTE_MESSAGE.getCode()));

		// 添加条件
		query.addConditions(sendByServicer.or(sendByCustomer));
		query.addConditions(fixedCond);

		// 做分组，排序，分页
		query.addGroupBy(DSL.decode()
				.when(records.SENDER_UID.gt(records.DST_CHANNEL_TOKEN.cast(Long.class)),
						DSL.concat(records.DST_CHANNEL_TOKEN, DSL.field("'_'", String.class), records.SENDER_UID))
				.otherwise(DSL.concat(records.SENDER_UID, DSL.field("'_'", String.class), records.DST_CHANNEL_TOKEN)));

		query.addOrderBy(records.INDEX_ID.desc());

		return query;
	}
	
	
	/**
	 * 获取有效的客服/热线列表
	 */
	private List<HotlineDTO> getActiveHotlineList(Integer namespaceId, String ownerType, Long ownerId, Integer pageSize,
			ListingLocator locator, Long servicerUid, Byte serviceType) {
		return getHotlineList(namespaceId, ownerType, ownerId, pageSize, locator, servicerUid, null, serviceType,
				HotlineStatus.ACTIVE.getCode());
	}
	
	/**
	 * 获取表里存在的客服/热线列表，包括已删除的
	 */
	private List<HotlineDTO> getExistHotlineList(Integer namespaceId, String ownerType, Long ownerId, Integer pageSize,
			ListingLocator locator, Long servicerUid, Byte serviceType) {
		return getHotlineList(namespaceId, ownerType, ownerId, pageSize, locator, servicerUid, null, serviceType,
				null);
	}
	

	/**
	 * 获取热线/客服列表的实现方法
	 * @return
	 */
	private List<HotlineDTO> getHotlineList(Integer namespaceId, String ownerType, Long ownerId, Integer pageSize,
			ListingLocator locator, Long userId, String contact, Byte serviceType, Byte status) {

		// 获取相应热线
		List<ServiceHotline> serviceHotlines = serviceHotlinesProvider.queryServiceHotlines(namespaceId, ownerType,
				ownerId, pageSize, locator, userId, contact, serviceType, status);

		// 未找到返回
		if (CollectionUtils.isEmpty(serviceHotlines)) {
			return null;
		}

		// 找到后进行转换
		List<HotlineDTO> hotlines = new ArrayList<HotlineDTO>(16);
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
	 * 
	 * @param keyword
	 *            必填
	 * @param namespaceId
	 *            必填
	 * @return
	 */
	private List<TargetDTO> findUserByTokenOrNickName(String inputKeyword, int namespaceId) {

		if (StringUtils.isBlank(inputKeyword)) {
			return null;
		}
		
		String keyword = inputKeyword.trim();

		// 1.只要是数字，就先进行手机号搜索
		if (HotlineUtils.isNumber(keyword)) {
			TargetDTO target = userProvider.findUserByToken(keyword, namespaceId);
			if (null != target) {
				List<TargetDTO> targetList = new ArrayList<TargetDTO>(1);
				targetList.add(target);
				return targetList;
			}
		}

		// 2.如果不是手机号，或者手机号未找到。都需要通过nickName搜索。
		// 因为有人可能把昵称设置成数字号码了
		return findUserByNickName(keyword, namespaceId);
	}
	
	
	

	/**
	 * 获取多个专属客服 以下参数均为必传
	 * 
	 * @param namespaceId
	 * @param ownerType
	 * @param ownerId
	 * @param servicerId
	 * @return
	 */
	private List<TargetDTO> getMultiExclusiveServicer(Integer namespaceId, String ownerType, Long ownerId,
			boolean isIncludeDeleted) {
		return getExclusiveServicer(namespaceId, ownerType, ownerId, null, ServiceType.ZHUANSHU_SERVICE.getCode(), false,
				isIncludeDeleted);
	}

	/**
	 * 获取单个专属客服 以下参数均为必传
	 * 
	 * @param namespaceId
	 * @param ownerType
	 * @param ownerId
	 * @param servicerId
	 * @return
	 */
	private TargetDTO getSingleExclusiveServicer(Integer namespaceId, String ownerType, Long ownerId, Long servicerId) {

		if (null == servicerId) {
			throwErrorCode(HotlineErrorCode.ERROR_HOTLINE_SERVICER_KEY_INVALID);
		}

		List<TargetDTO> dtos = getExclusiveServicer(namespaceId, ownerType, ownerId, servicerId,
				ServiceType.ZHUANSHU_SERVICE.getCode(), true, true);
		if (CollectionUtils.isEmpty(dtos)) {
			return null;
		}

		return dtos.get(0);
	}

	/**
	 * 根据条件获取专属客服的
	 * 
	 * @param namespaceId
	 * @param ownerType
	 * @param ownerId
	 * @param serviceUid 客服用户userId
	 * @param isSingleResult
	 * @return
	 */
	private List<TargetDTO> getExclusiveServicer(Integer namespaceId, String ownerType, Long ownerId, Long serviceUid,
			Byte serviceType, boolean isSingleResult, boolean isIncludeDeleted) {

		// 设置分页条件
		ListingLocator locator = new ListingLocator();

		// 查询单个是pageSize为1
		Integer pageSize = isSingleResult ? 1 : null;

		// 获取结果
		List<HotlineDTO> servicers = null;
		if (isIncludeDeleted) {
			servicers = getExistHotlineList(namespaceId, ownerType, ownerId, pageSize, locator, serviceUid,  serviceType);
		} else {
			servicers = getActiveHotlineList(namespaceId, ownerType, ownerId, pageSize, locator, serviceUid, serviceType);
		}
		
		if (CollectionUtils.isEmpty(servicers)) {
			return null;
		}

		// 转成客服target列表
		List<TargetDTO> dtos = new ArrayList<>(10);
		servicers.stream().forEach(r -> {
			TargetDTO dto = new TargetDTO();
			dto.setTargetId(r.getUserId());
			dto.setTargetType("eh_user");
			dto.setTargetName(r.getName());
			dto.setUserIdentifier(r.getContact());
			dtos.add(dto);
		});

		return dtos;
	}

	/**
	 * 设置聊天记录ChatRecordDTO字段
	 * 
	 * @param chatRecord
	 *            需要设置的聊天记录
	 * @param msgRecord
	 *            数据库中的消息记录
	 */
	private final void setChatRecord(ChatRecordDTO chatRecord, MessageRecord msgRecord, TargetDTO servicerDto,
			TargetDTO customerDto) {

		// 1.设置发送者名字，客服名或用户名
		if (msgRecord.getSenderUid().equals(servicerDto.getTargetId())) {
			chatRecord.setSenderName(servicerDto.getTargetName());
			chatRecord.setIsServicer(HotlineFlag.YES.getCode());
		} else {
			chatRecord.setSenderName(customerDto.getTargetName());
			chatRecord.setIsServicer(HotlineFlag.NO.getCode());
		}

		// 2.设置发送时间
		chatRecord.setSendTime(msgRecord.getCreateTime());

		// 3.设置消息内容和消息类型
		// 3.1 IMAGE body类型为ImageBody，这里不做解析，直接使用url字段
		if (MessageBodyType.IMAGE == MessageBodyType.fromCode(msgRecord.getBodyType())) {
			/*
			 * body格式 { "fileSize":16541, "filename":"9f2f070828381fx63.jpg",
			 * "format":"image/jpeg",
			 * "url":"http://xxxx:5000/image/axxxRQ?token=6Gxxl",
			 * "uri":"cs://1/image/aW1xxxxUQ", "height":405, "width":616 }
			 */
			JSONObject json = JSONObject.parseObject(msgRecord.getBody());
			chatRecord.setMessage(json.getString("url"));
			chatRecord.setMessageType(ChatMessageType.IMAGE.getCode());
			return;
		}

		// 3.2 AUDIO类型，方法同IMAGE,但语音内容不需要显示，置为null
		if (MessageBodyType.AUDIO == MessageBodyType.fromCode(msgRecord.getBodyType())) {

			/*
			 * body格式 { "duration":"2", "fileSize":0,
			 * "filename":"1524571892706.m4a", "format":"audio/m4a",
			 * "uri":"cs://1/audio/aW1xxxxUQ",
			 * "url":"http://xxxx:5000/audio/axxxRQ?token=6Gxxl" }
			 */
			// JSONObject json = JSONObject.parseObject(msgRecord.getBody());
			// chatRecord.setMessage(json.getString("url"));
			chatRecord.setMessageType(ChatMessageType.AUDIO.getCode());
			return;
		}

		// 3.3 其他都默认为text类型
		chatRecord.setMessageType(ChatMessageType.TEXT.getCode());
		chatRecord.setMessage(msgRecord.getBody());
	}

	/*
	 * 设置会话（ChatGroupDTO）的servicer，customer字段
	 * 
	 * @param chatGroup 需要设置的会话
	 * 
	 * @param msgRecord 数据库中的消息记录
	 */
	private final void setChatGroup(ChatGroupDTO chatGroup, MessageRecord msgRecord, List<TargetDTO> servicerDtos,
			List<TargetDTO> customerDtos) {

		// 1.获取发送方和接收方id
		Long senderUid = msgRecord.getSenderUid();
		Long acceptUid = Long.parseLong(msgRecord.getDstChannelToken());


		// 2.未指定客户
		Long customerId = null;
		for (int i = 0; i < servicerDtos.size(); i++) {
			if (senderUid.equals(servicerDtos.get(i).getTargetId())) {
				chatGroup.setServicer(servicerDtos.get(i));
				customerId = acceptUid;
				break;
			}

			if (acceptUid.equals(servicerDtos.get(i).getTargetId())) {
				chatGroup.setServicer(servicerDtos.get(i));
				customerId = senderUid;
				break;
			}
		}
		
		// 3.做异常判断，通常不会到这一步
		if (null == customerId) {
			return;
		}

		// 4.根据入参获得客户
		if (null != customerDtos) {
			for (TargetDTO dto : customerDtos) {
				if (customerId.equals(dto.getTargetId())) {
					chatGroup.setCustomer(dto);
					return;
				}
			}
		}
		
		// 5.如果无入参，则直接从数据库获取
		TargetDTO targetDto = findUserNameIdentifierById(customerId);
		chatGroup.setCustomer(targetDto);
		return;
	}

	/**
	 * 根据用户id获取到姓名和电话号码
	 * 姓名从eh_users的nick_name获取。
	 * 号码从eh_user_identifiers中identifier_token获取
	 * @param userId
	 * @return
	 */
	private TargetDTO findUserNameIdentifierById(Long userId) {
		
		if (null == userId) {
			return null;
		}

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhUserIdentifiers token = Tables.EH_USER_IDENTIFIERS.as("token");
		EhUsers user = Tables.EH_USERS.as("user");

		TargetDTO dto = new TargetDTO();
		context.select(token.IDENTIFIER_TOKEN, user.ID, user.NICK_NAME).from(user).leftOuterJoin(token)
				.on(token.OWNER_UID.eq(user.ID)).where(user.ID.eq(userId)).fetchOne().map(r -> {
					dto.setTargetName(r.getValue(user.NICK_NAME));
					dto.setTargetType("eh_user");
					dto.setTargetId(r.getValue(user.ID));
					dto.setUserIdentifier(r.getValue(token.IDENTIFIER_TOKEN));
					return null;
				});

		return dto;
	}
	
	/**
	 * 根据用户id获取到姓名和电话号码
	 * 姓名从eh_users的nick_name获取。
	 * 号码从eh_user_identifiers中identifier_token获取
	 * @param userId
	 * @return
	 */
	private List<TargetDTO> findUserByNickName(String nickName, Integer namespaceId) {
		
		if (StringUtils.isBlank(nickName) || null == namespaceId) {
			return null;
		}

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhUserIdentifiers token = Tables.EH_USER_IDENTIFIERS.as("token");
		EhUsers user = Tables.EH_USERS.as("user");

		List<TargetDTO> dtoList = new ArrayList<>(10);
		context.select(token.IDENTIFIER_TOKEN, user.ID, user.NICK_NAME).from(user).leftOuterJoin(token)
				.on(token.OWNER_UID.eq(user.ID))
				.where(user.NICK_NAME.eq(nickName).and(user.NAMESPACE_ID.eq(namespaceId))).fetch().map(r -> {
					TargetDTO dto = new TargetDTO();
					dto.setTargetName(r.getValue(user.NICK_NAME));
					dto.setTargetType("eh_user");
					dto.setTargetId(r.getValue(user.ID));
					dto.setUserIdentifier(r.getValue(token.IDENTIFIER_TOKEN));
					dtoList.add(dto);
					return null;
				});

		return dtoList;
	}

	

	private final void throwErrorCode(HotlineErrorCode hotlineErrorCode) {
		throw RuntimeErrorException.errorWith(HotlineErrorCode.SCOPE, hotlineErrorCode.getCode(),
				hotlineErrorCode.getInfo());
	}
	
	/**
	 * 检查是否有相同条件的记录，并返回单条符合条件记录
	 */
	private List<ServiceHotline> getMultiHotlineIfExist(ServiceHotline conditionObject) {
		return getHotlineIfExist(conditionObject, null);

	}
	
	/**
	 * 检查是否有相同条件的记录，并返回单条符合条件记录
	 */
	private ServiceHotline getSingleHotlineIfExist(ServiceHotline conditionObject) {

		List<ServiceHotline> hotlines = getHotlineIfExist(conditionObject, 1);

		return CollectionUtils.isEmpty(hotlines) ? null : hotlines.get(0);

	}

	/**
	 * 检查是否有相同条件的记录，并返回入参限制的条数的记录
	 */
	private List<ServiceHotline> getHotlineIfExist(ServiceHotline conditionObject, Integer getCount) {

		if (null == conditionObject.getServiceType()) {
			return null;
		}

		Byte serviceType = conditionObject.getServiceType().byteValue();

		// 返回一条数据即可
		List<ServiceHotline> hotlines = null;

		// 热线和专属客服都不能有重复的号码（contact字段），所以先查询正常状态下是否有重复号码
		hotlines = serviceHotlinesProvider.queryServiceHotlines(conditionObject.getNamespaceId(),
				conditionObject.getOwnerType(), conditionObject.getOwnerId(), getCount, null, null,
				conditionObject.getContact(), serviceType, HotlineStatus.ACTIVE.getCode());

		// 如果非空，返回查询出来的数据
		if (!CollectionUtils.isEmpty(hotlines)) {
			return hotlines;
		}

		// 热线查询到此就结束了
		if (ServiceType.SERVICE_HOTLINE.getCode().equals(serviceType)) {
			return null;
		}
		

		// 专属客服还需查看被删除数据是否有相同userId，注：这里获取一条即可。如果有多条，也是数据库出错了
		hotlines = serviceHotlinesProvider.queryServiceHotlines(conditionObject.getNamespaceId(),
				conditionObject.getOwnerType(), conditionObject.getOwnerId(), getCount, null,
				conditionObject.getUserId(), null, serviceType, null);

		return hotlines;
	}
	
	
	/**
	 * 校验当前请求是否符合权限
	 * @param privilegeType
	 * @param currentOrgId
	 * @param appId
	 * @param checkOrgId
	 * @param checkCommunityId
	 */
	private void checkPrivilege(PrivilegeType privilegeType, Long currentOrgId, Long appId,
			Long checkCommunityId) {
		if (configProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), currentOrgId,
					privilegeType.getCode(), appId, null, checkCommunityId);
		}
	}
	
	
	/**
	 * @param chatDtos
	 * @return
	 */
	private List<String> createSingleChatGroupCotent(List<ChatRecordDTO> chatDtos, TargetDTO servicerDto, TargetDTO customerDto) {
		
		/*
		 * 	====================================
			客服名称:客服A
			====================================
			用户姓名:刘亦菲   联系电话：15306070607
			====================================
						
			2018-02-27 14:41:38 客服A（这是客服名称）
			Hello there!
			
			2018-02-27 14:45:32 刘亦菲（这是用户姓名）
			[图片]
			
		 * */
		List<String> content = new ArrayList<>(1000);
		content.add("=======================================");
		content.add("客服名称:"+servicerDto.getTargetName());
		content.add("=======================================");
		content.add("用户名称:"+customerDto.getTargetName()+"   联系电话:"+customerDto.getUserIdentifier());
		content.add("=======================================");
		content.add("\n");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (ChatRecordDTO dto : chatDtos) {
			content.add(sdf.format(dto.getSendTime()) + " " + dto.getSenderName());

			if (ChatMessageType.IMAGE.getCode().equals(dto.getMessageType())) {
				content.add("[图片]");
			}
			else if (ChatMessageType.AUDIO.getCode().equals(dto.getMessageType())) {
				content.add("[语音]");
			}
			else {
				//这里做表情过滤功能
				String filterMsg = HotlineUtils.filterEmoji(dto.getMessage(), "[表情]"); 
				content.add(filterMsg);
			}
			content.add("\n");
		}
		
		return content;
	}
	
	private void exportTxt(HttpServletResponse httpResponse, List<String> dataList, String fileName) {

		try {
			httpResponse.setContentType("multipart/form-data");
			httpResponse.setHeader("Content-Disposition",
					"attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
			TextUtils.exportTxtByOutputStream(httpResponse.getOutputStream(), dataList, null);
		} catch (IOException e) {
			LOGGER.error("Export Txt =" + e.getMessage());
			return;
		}
	}

}
