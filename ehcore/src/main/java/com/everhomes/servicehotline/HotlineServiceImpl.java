package com.everhomes.servicehotline;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.chatrecord.AbstractChatRecordService;
import com.everhomes.chatrecord.ChatRecordService;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.message.MessageProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.common.PrivilegeType;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.servicehotline.AddHotlineCommand;
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
import com.everhomes.rest.servicehotline.HotlineSubject;
import com.everhomes.rest.servicehotline.LayoutType;
import com.everhomes.rest.servicehotline.ServiceType;
import com.everhomes.rest.servicehotline.SetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.UpdateHotlineCommand;
import com.everhomes.rest.servicehotline.UpdateHotlinesCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.Tables;
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

@Component
public class HotlineServiceImpl extends AbstractChatRecordService implements HotlineService {
	
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

//	@Autowired
//	private TaskService taskService; //用于文件下载中心
	
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

		hotline.setNamespaceId(namespaceId);
		hotline.setStatus(HotlineStatus.ACTIVE.getCode());
		
		if (null != tmp) {//已删除的专属客服重新被添加时，进入此分支
			hotline.setId(tmp.getId());
			hotline.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			hotline.setCreatorUid(UserContext.current().getUser().getId());			
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
		
		
		record.setContact(cmd.getContact());
		record.setName(cmd.getName());
		record.setUserId(cmd.getUserId());
		record.setDescription(cmd.getDescription());
		// 查询是否有重复的
		List<ServiceHotline> tmp = getMultiHotlineIfExist(record);
		
		//排除自己
		if (!CollectionUtils.isEmpty(tmp)) {
			tmp = tmp.stream().filter(p -> !p.getId().equals(cmd.getId())).collect(Collectors.toList());
		}
		
		// 仍有记录说明有重复的，需报错
		if (null != tmp && tmp.size() > 0) {
			throwErrorCode(HotlineErrorCode.ERROR_DUPLICATE_PHONE);
		}

		this.serviceHotlinesProvider.updateServiceHotline(record);
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
			OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(cmd.getId(),
					cmd.getOrgId());
			if (member != null)
				info.setContractName(member.getContactName());
		}
		return info;
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
	 * 获取多个专属客服 以下参数均为必传
	 * 
	 * @param namespaceId
	 * @param ownerType
	 * @param ownerId
	 * @param servicerId
	 * @return
	 */
	public List<TargetDTO> getMultiExclusiveServicer(Integer namespaceId, String ownerType, Long ownerId) {
		return getExclusiveServicer(namespaceId, ownerType, ownerId, null, ServiceType.ZHUANSHU_SERVICE.getCode(), false,
				true);
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
	public TargetDTO getSingleExclusiveServicer(Integer namespaceId, String ownerType, Long ownerId, Long servicerId) {

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

	// 获取客服列表需要子类实现如下方法
	@Override
	protected TargetDTO getSingleServicer(Integer namespaceId, String ownerType, Long ownerId, Long servicerId, String extraParam) {
		return getSingleExclusiveServicer(namespaceId, ownerType, ownerId, servicerId);
	}

	@Override
	protected List<TargetDTO> getAllServicers(Integer namespaceId, String ownerType, Long ownerId, String extraParam) {
		return getMultiExclusiveServicer(namespaceId, ownerType, ownerId);
	}

	@Override
	protected void checkOnlineServicePrivilege(Long currentOrgId, Long appId, Long checkCommunityId) {
		checkPrivilege(PrivilegeType.EXCLUSIVE_SERVICE_CHAT_RECORD, currentOrgId, appId,
				checkCommunityId);
		
	}
}
