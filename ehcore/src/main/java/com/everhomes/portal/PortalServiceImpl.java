// @formatter:off
package com.everhomes.portal;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.launchpad.ItemServiceCategry;
import com.everhomes.launchpad.LaunchPadItem;
import com.everhomes.launchpad.LaunchPadLayout;
import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.MoreActionData;
import com.everhomes.rest.common.NavigationActionData;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.portal.*;
import com.everhomes.rest.portal.LaunchPadLayoutJson;
import com.everhomes.rest.search.OrganizationQueryResult;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.widget.*;
import com.everhomes.rest.widget.NewsInstanceConfig;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PortalServiceImpl implements PortalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortalServiceImpl.class);


	@Autowired
	private PortalLayoutProvider portalLayoutProvider;

	@Autowired
	private PortalItemGroupProvider portalItemGroupProvider;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private PortalItemProvider portalItemProvider;

	@Autowired
	private PortalContentScopeProvider portalContentScopeProvider;

	@Autowired
	private PortalLayoutTemplateProvider portalLayoutTemplateProvider;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;

	@Autowired
	private PortalItemCategoryProvider portalItemCategoryProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private PortalNavigationBarProvider portalNavigationBarProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private LaunchPadProvider launchPadProvider;

	@Autowired
	private PortalLaunchPadMappingProvider portalLaunchPadMappingProvider;

	@Autowired
	private PortalPublishLogProvider portalPublishLogProvider;

	@Autowired
	private CommunitySearcher communitySearcher;

	@Autowired
	private OrganizationSearcher organizationSearcher;

	@Override
	public ListServiceModuleAppsResponse listServiceModuleApps(ListServiceModuleAppsCommand cmd) {
		List<ServiceModuleApp> moduleApps = serviceModuleAppProvider.listServiceModuleApp(cmd.getNamespaceId(), cmd.getModuleId());
		return new ListServiceModuleAppsResponse(moduleApps.stream().map(r ->{
			return processServiceModuleAppDTO(r);
		}).collect(Collectors.toList()));
	}

	@Override
	public ServiceModuleAppDTO createServiceModuleApp(CreateServiceModuleAppCommand cmd) {
		ServiceModule serviceModule = checkServiceModule(cmd.getModuleId());
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		ServiceModuleApp moduleApp = ConvertHelper.convert(cmd, ServiceModuleApp.class);
		if(StringUtils.isEmpty(cmd.getInstanceConfig()) && null != serviceModule){
			moduleApp.setInstanceConfig(serviceModule.getInstanceConfig());
		}
		moduleApp.setNamespaceId(namespaceId);
		moduleApp.setStatus(ServiceModuleAppStatus.ACTIVE.getCode());
		moduleApp.setCreatorUid(UserContext.current().getUser().getId());
		moduleApp.setOperatorUid(moduleApp.getCreatorUid());
		moduleApp.setActionType(serviceModule.getActionType());
		serviceModuleAppProvider.createServiceModuleApp(moduleApp);
		return processServiceModuleAppDTO(moduleApp);
	}

	@Override
	public List<ServiceModuleAppDTO> batchCreateServiceModuleApp(BatchCreateServiceModuleAppCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		if(null == cmd.getModuleApps() || cmd.getModuleApps().size() == 0){
			LOGGER.error("params moduleApps is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"params moduleApps is null.");
		}

		List<ServiceModuleApp> serviceModuleApps = new ArrayList<>();

		for (CreateServiceModuleApp createModuleApp: cmd.getModuleApps()) {
			ServiceModuleApp moduleApp = ConvertHelper.convert(createModuleApp, ServiceModuleApp.class);
			ServiceModule serviceModule = checkServiceModule(createModuleApp.getModuleId());
			moduleApp.setInstanceConfig(serviceModule.getInstanceConfig());
			moduleApp.setStatus(ServiceModuleAppStatus.ACTIVE.getCode());
			moduleApp.setCreatorUid(UserContext.current().getUser().getId());
			moduleApp.setOperatorUid(moduleApp.getCreatorUid());
			moduleApp.setActionType(serviceModule.getActionType());
			moduleApp.setNamespaceId(namespaceId);
			serviceModuleApps.add(moduleApp);
		}
		serviceModuleAppProvider.createServiceModuleApps(serviceModuleApps);
		return serviceModuleApps.stream().map(r ->{
			return processServiceModuleAppDTO(r);
		}).collect(Collectors.toList());
	}

	@Override
	public ServiceModuleAppDTO updateServiceModuleApp(UpdateServiceModuleAppCommand cmd) {
		ServiceModuleApp moduleApp = checkServiceModuleApp(cmd.getId());
		moduleApp.setOperatorUid(UserContext.current().getUser().getId());
		moduleApp.setName(cmd.getName());
		if(null != cmd.getModuleId()){
			moduleApp.setModuleId(cmd.getModuleId());
			ServiceModule serviceModule = checkServiceModule(cmd.getModuleId());
			if(StringUtils.isEmpty(cmd.getInstanceConfig()) && null != serviceModule){
				cmd.setInstanceConfig(serviceModule.getInstanceConfig());
			}
		}
		moduleApp.setInstanceConfig(cmd.getInstanceConfig());
		serviceModuleAppProvider.updateServiceModuleApp(moduleApp);
		return processServiceModuleAppDTO(moduleApp);
	}


	@Override
	public void deleteServiceModuleApp(DeleteServiceModuleAppCommand cmd) {
		ServiceModuleApp moduleApp = checkServiceModuleApp(cmd.getId());
		moduleApp.setOperatorUid(UserContext.current().getUser().getId());
		moduleApp.setStatus(ServiceModuleAppStatus.INACTIVE.getCode());
		serviceModuleAppProvider.updateServiceModuleApp(moduleApp);
	}

	private ServiceModuleApp checkServiceModuleApp(Long id){
		ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(id);
		if(null == serviceModuleApp){
			LOGGER.error("Unable to find the serviceModuleApp.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the serviceModuleApp.");
		}
		return serviceModuleApp;
	}

	private ServiceModule checkServiceModule(Long id){
		ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(id);
		if(null == serviceModule){
			LOGGER.error("Unable to find the serviceModule.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the serviceModule.");
		}
		return serviceModule;
	}

	@Override
	public List<PortalLayoutTemplateDTO> listPortalLayoutTemplates() {
		List<PortalLayoutTemplate> portalLayoutTemplates = portalLayoutTemplateProvider.listPortalLayoutTemplate();
		return portalLayoutTemplates.stream().map(r ->{
			return ConvertHelper.convert(r, PortalLayoutTemplateDTO.class);
		}).collect(Collectors.toList());
	}

	@Override
	public ListPortalLayoutsResponse listPortalLayouts(ListPortalLayoutsCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<PortalLayout> portalLayouts = portalLayoutProvider.listPortalLayout(namespaceId, null);
		return new ListPortalLayoutsResponse(portalLayouts.stream().map(r ->{
			return processPortalLayoutDTO(r);
		}).collect(Collectors.toList()));
	}

	private ServiceModuleAppDTO processServiceModuleAppDTO(ServiceModuleApp moduleApp){
		ServiceModuleAppDTO dto = ConvertHelper.convert(moduleApp, ServiceModuleAppDTO.class);
		if(null != moduleApp.getModuleId() && moduleApp.getModuleId() != 0){
			ServiceModule serviceModule = checkServiceModule(moduleApp.getModuleId());
			dto.setModuleName(serviceModule.getName());

			PortalPublishHandler handler = getPortalPublishHandler(moduleApp.getModuleId());
			if(null != handler){
				dto.setInstanceConfig(handler.processInstanceConfig(dto.getInstanceConfig()));
			}
		}
		return dto;
	}

	private PortalLayoutDTO processPortalLayoutDTO(PortalLayout portalLayout){
		PortalLayoutDTO dto = ConvertHelper.convert(portalLayout, PortalLayoutDTO.class);
		return dto;
	}

	@Override
	public PortalLayoutDTO createPortalLayout(CreatePortalLayoutCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		PortalLayout portalLayout = ConvertHelper.convert(cmd, PortalLayout.class);
		portalLayout.setCreatorUid(user.getId());
		portalLayout.setOperatorUid(user.getId());
		portalLayout.setNamespaceId(namespaceId);
		portalLayout.setStatus(PortalLayoutStatus.ACTIVE.getCode());

		this.dbProvider.execute((status) -> {
			if(null != cmd.getLayoutTemplateId()){
				PortalLayoutTemplate template = portalLayoutTemplateProvider.findPortalLayoutTemplateById(cmd.getLayoutTemplateId());
				if(null != template && !StringUtils.isEmpty(template.getTemplateJson())){
					List<PortalItemGroup> groups = new ArrayList<>();
					PortalLayoutJson layoutJson = (PortalLayoutJson)StringHelper.fromJsonString(template.getTemplateJson(), PortalLayoutJson.class);
					portalLayout.setName(layoutJson.getLayoutName());
					portalLayout.setLocation(layoutJson.getLocation());
					portalLayoutProvider.createPortalLayout(portalLayout);
					if(null != layoutJson.getGroups()){
						for (PortalItemGroupJson jsonObj: layoutJson.getGroups()) {
							PortalItemGroup portalItemGroup = ConvertHelper.convert(jsonObj, PortalItemGroup.class);
							String instanceConfig = StringHelper.toJsonString(jsonObj.getInstanceConfig());
							if(!StringUtils.isEmpty(instanceConfig) && !"null".equals(instanceConfig)){
								portalItemGroup.setInstanceConfig(instanceConfig);
							}
							portalItemGroup.setStatus(PortalItemGroupStatus.ACTIVE.getCode());
							portalItemGroup.setLayoutId(portalLayout.getId());
							portalItemGroup.setCreatorUid(user.getId());
							portalItemGroup.setOperatorUid(user.getId());
							groups.add(portalItemGroup);
						}
						portalItemGroupProvider.createPortalItemGroups(groups);
					}

				}
			}else{
				portalLayoutProvider.createPortalLayout(portalLayout);
			}

			return null;
		});
		return processPortalLayoutDTO(portalLayout);
	}

	@Override
	public PortalLayoutDTO updatePortalLayout(UpdatePortalLayoutCommand cmd) {
		User user = UserContext.current().getUser();
		PortalLayout portalLayout = checkPortalLayout(cmd.getId());
		portalLayout.setLabel(cmd.getLabel());
		portalLayout.setDescription(cmd.getDescription());
		portalLayout.setOperatorUid(user.getId());
		portalLayoutProvider.updatePortalLayout(portalLayout);
		return processPortalLayoutDTO(portalLayout);
	}

	@Override
	public void deletePortalLayout(DeletePortalLayoutCommand cmd) {
		User user = UserContext.current().getUser();
		PortalLayout portalLayout = checkPortalLayout(cmd.getId());
		portalLayout.setStatus(PortalLayoutStatus.INACTIVE.getCode());
		portalLayout.setOperatorUid(user.getId());
		portalLayoutProvider.updatePortalLayout(portalLayout);
	}

	private PortalLayout checkPortalLayout(Long id){
		PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(id);
		if(null == portalLayout){
			LOGGER.error("Unable to find the portalLayout.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalLayout.");
		}

		return portalLayout;
	}

	@Override
	public ListPortalItemGroupsResponse listPortalItemGroups(ListPortalItemGroupsCommand cmd) {
		List<PortalItemGroup> portalItemGroups = portalItemGroupProvider.listPortalItemGroup(cmd.getLayoutId());

		List<PortalItemGroupDTO> dtos = portalItemGroups.stream().map(r ->{
			return processPortalItemGroupDTO(r);
		}).collect(Collectors.toList());

		Collections.sort(dtos, new Comparator<PortalItemGroupDTO>() {
			@Override
			public int compare(PortalItemGroupDTO o1, PortalItemGroupDTO o2) {
				Integer order1 = 0;
				if(null != o1.getDefaultOrder()){
					order1 = o1.getDefaultOrder();
				}

				Integer order2 = 0;
				if(null != o2.getDefaultOrder()){
					order2 = o2.getDefaultOrder();
				}
				return order1 - order2;
			}
		});
		return new ListPortalItemGroupsResponse(dtos);
	}

	@Override
	public PortalItemGroupDTO createPortalItemGroup(CreatePortalItemGroupCommand cmd) {
		User user = UserContext.current().getUser();
		PortalLayout portalLayout = checkPortalLayout(cmd.getLayoutId());
		PortalItemGroup portalItemGroup = ConvertHelper.convert(cmd, PortalItemGroup.class);
		Integer namespaceId = UserContext.getCurrentNamespaceId(portalLayout.getNamespaceId());
		portalItemGroup.setNamespaceId(namespaceId);
		portalItemGroup.setLayoutId(portalLayout.getId());
		portalItemGroup.setStatus(PortalItemGroupStatus.ACTIVE.getCode());
		portalItemGroup.setCreatorUid(user.getId());
		portalItemGroup.setOperatorUid(user.getId());
		portalItemGroupProvider.createPortalItemGroup(portalItemGroup);
		return processPortalItemGroupDTO(portalItemGroup);
	}

	@Override
	public PortalItemGroupDTO updatePortalItemGroup(UpdatePortalItemGroupCommand cmd) {
		User user = UserContext.current().getUser();
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getId());
		portalItemGroup.setLabel(cmd.getLabel());
		portalItemGroup.setSeparatorFlag(cmd.getSeparatorFlag());
		portalItemGroup.setSeparatorHeight(cmd.getSeparatorHeight());
		portalItemGroup.setWidget(cmd.getWidget());
		portalItemGroup.setStyle(cmd.getStyle());
		portalItemGroup.setInstanceConfig(cmd.getInstanceConfig());
		portalItemGroup.setOperatorUid(user.getId());
		portalItemGroup.setDescription(cmd.getDescription());
		portalItemGroup.setContentType(cmd.getContentType());
		portalItemGroupProvider.updatePortalItemGroup(portalItemGroup);
		return processPortalItemGroupDTO(portalItemGroup);
	}

	@Override
	public void deletePortalItemGroup(DeletePortalItemGroupCommand cmd) {
		User user = UserContext.current().getUser();
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getId());
		portalItemGroup.setStatus(PortalItemGroupStatus.INACTIVE.getCode());
		portalItemGroup.setOperatorUid(user.getId());
		portalItemGroupProvider.updatePortalItemGroup(portalItemGroup);
	}

	private PortalItemGroup checkPortalItemGroup(Long id){
		PortalItemGroup portalItemGroup = portalItemGroupProvider.findPortalItemGroupById(id);
		if(null == portalItemGroup){
			LOGGER.error("Unable to find the portalItemGroup.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalItemGroup.");
		}

		return portalItemGroup;
	}

	private PortalItemGroupDTO processPortalItemGroupDTO(PortalItemGroup portalItemGroup){
		PortalItemGroupDTO dto = ConvertHelper.convert(portalItemGroup, PortalItemGroupDTO.class);
		dto.setCreateTime(portalItemGroup.getCreateTime().getTime());
		dto.setUpdateTime(portalItemGroup.getUpdateTime().getTime());
		ItemGroupInstanceConfig config = (ItemGroupInstanceConfig)StringHelper.fromJsonString(portalItemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);
		if(null != config){
			if(!StringUtils.isEmpty(config.getTitleUri())){
				String url = contentServerService.parserUri(config.getTitleUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
				config.setTitleUrl(url);
				dto.setInstanceConfig(StringHelper.toJsonString(config));
			}
		}
		return dto;
	}

	@Override
	public ListPortalItemsResponse listPortalItems(ListPortalItemsCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<PortalItem> portalItems = portalItemProvider.listPortalItems(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				Condition cond = Tables.EH_PORTAL_ITEMS.ITEM_GROUP_ID.eq(cmd.getItemGroupId());
				if(null != PortalScopeType.fromCode(cmd.getScopeType())){
					Condition cond1 = Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_TYPE.eq(cmd.getScopeType());
					Condition cond2 = Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_ID.eq(cmd.getScopeId());

					//如果是选择具体场景的实体 则要合上具体场景的 全部条件
					if(0 != cmd.getScopeId()){
						cond2 = cond2.or(Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_ID.eq(0L));
					}
					cond1 = cond1.and(cond2);

					//如果选择的场景是管理公司或者普通公司，则需要合上园区场景条件
					if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.PM || PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.ORGANIZATION){
						Condition cond3 = Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_TYPE.eq(PortalScopeType.COMMERCIAL.getCode());

						//合上园区场景的 全部条件
						Condition cond4 = Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_ID.eq(0L);

						//合上公司所入住的园区场景具体实体条件
						if(0 != cmd.getScopeId()){
							OrganizationCommunityRequest request = organizationProvider.getOrganizationCommunityRequestByOrganizationId(cmd.getScopeId());
							if(null != request){
								cond4 = cond4.or(Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_ID.eq(request.getCommunityId()));
							}
						}
						cond3 = cond3.and(cond4);
						cond1 = cond1.or(cond3);
					}
					cond = cond.and(cond1);
				}
				query.addConditions(cond);
				return query;
			}
		});
		ListPortalItemsResponse response = new ListPortalItemsResponse();
		response.setNextPageAnchor(locator.getAnchor());
		List<PortalItemDTO> dtos = portalItems.stream().map(r ->{
			return processPortalItemDTO(r);
		}).collect(Collectors.toList());
		response.setPortalItems(dtos);

		return response;
	}


	@Override
	public List<PortalItemDTO> listPortalItemsByItemGroupId(ListPortalItemsCommand cmd) {
		List<PortalItem> portalItems = portalItemProvider.listPortalItemByGroupId(cmd.getItemGroupId());
		return portalItems.stream().map(r ->{
			return processPortalItemDTO(r);
		}).collect(Collectors.toList());
	}


	@Override
	public PortalItemDTO createPortalItem(CreatePortalItemCommand cmd) {
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getItemGroupId());
		PortalLayout portalLayout = checkPortalLayout(portalItemGroup.getLayoutId());
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(portalItemGroup.getNamespaceId());
		PortalItem portalItem = ConvertHelper.convert(cmd, PortalItem.class);
		portalItem.setNamespaceId(namespaceId);
		if(null == PortalItemStatus.fromCode(cmd.getStatus())){
			portalItem.setStatus(PortalItemStatus.ACTIVE.getCode());
		}
		if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
			portalItem.setDefaultOrder(10000);
		}
		portalItem.setCreatorUid(user.getId());
		portalItem.setOperatorUid(user.getId());
		portalItem.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());

		portalItem.setGroupName(portalItemGroup.getName());
		portalItem.setItemLocation(portalLayout.getLocation());
		this.dbProvider.execute((status) -> {
			portalItemProvider.createPortalItem(portalItem);
			if(null != cmd.getScopes() && cmd.getScopes().size() > 0){
				createContentScopes(EntityType.PORTAL_ITEM.getCode(), portalItem.getId(), cmd.getScopes());
			}
			return null;
		});

		return processPortalItemDTO(portalItem);
	}

	@Override
	public PortalItemDTO updatePortalItem(UpdatePortalItemCommand cmd) {
		User user = UserContext.current().getUser();
		PortalItem portalItem = checkPortalItem(cmd.getId());
		if(null == PortalItemStatus.fromCode(cmd.getStatus())){
			portalItem.setStatus(PortalItemStatus.ACTIVE.getCode());
		}else{
			portalItem.setStatus(cmd.getStatus());
		}
		portalItem.setOperatorUid(user.getId());
		portalItem.setLabel(cmd.getLabel());
		portalItem.setStatus(cmd.getStatus());
		portalItem.setActionType(cmd.getActionType());
		portalItem.setActionData(cmd.getActionData());
		portalItem.setDescription(cmd.getDescription());
		portalItem.setIconUri(cmd.getIconUri());
		portalItem.setBgcolor(cmd.getBgcolor());
		portalItem.setSelectedIconUri(cmd.getSelectedIconUri());
		this.dbProvider.execute((status) -> {
			portalItemProvider.updatePortalItem(portalItem);
			if(null != cmd.getScopes() && cmd.getScopes().size() > 0){
				portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
				createContentScopes(EntityType.PORTAL_ITEM.getCode(), portalItem.getId(), cmd.getScopes());
			}
			return null;
		});

		return processPortalItemDTO(portalItem);
	}

	@Override
	public void deletePortalItem(DeletePortalItemCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		portalItem.setOperatorUid(UserContext.current().getUser().getId());
		portalItem.setStatus(PortalItemGroupStatus.INACTIVE.getCode());
		this.dbProvider.execute((status) -> {
			portalItemProvider.updatePortalItem(portalItem);
			portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
			return null;
		});
	}

	@Override
	public void setPortalItemStatus(SetPortalItemStatusCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		portalItem.setOperatorUid(UserContext.current().getUser().getId());
		portalItem.setStatus(cmd.getStatus());
		portalItemProvider.updatePortalItem(portalItem);
	}

	private PortalItemDTO processPortalItemDTO(PortalItem portalItem){
		PortalItemDTO dto = ConvertHelper.convert(portalItem, PortalItemDTO.class);
		dto.setCreateTime(portalItem.getCreateTime().getTime());
		dto.setUpdateTime(portalItem.getUpdateTime().getTime());

		if(!StringUtils.isEmpty(portalItem.getIconUri())){
			String url = contentServerService.parserUri(portalItem.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setIconUrl(url);
		}

		if(!StringUtils.isEmpty(portalItem.getSelectedIconUri())){
			String url = contentServerService.parserUri(portalItem.getSelectedIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setSelectedIconUrl(url);
		}

		if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
			if(AllOrMoreType.ALL == AllOrMoreType.fromCode(actionData.getType())){
				dto.setContentName(configurationProvider.getValue(ConfigConstants.PORTAL_ITEM_ALL_TITLE, "全部"));
				if(!StringUtils.isEmpty(actionData.getDefUri())){
					String url = contentServerService.parserUri(actionData.getDefUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
					actionData.setDefUrl(url);
				}
				dto.setActionData(StringHelper.toJsonString(actionData));
			}else if(AllOrMoreType.MORE == AllOrMoreType.fromCode(actionData.getType())){
				dto.setContentName(configurationProvider.getValue(ConfigConstants.PORTAL_ITEM_MORE_TITLE, "更多"));
			}
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.LAYOUT){
			LayoutActionData actionData = (LayoutActionData)StringHelper.fromJsonString(portalItem.getActionData(), LayoutActionData.class);
			PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(actionData.getLayoutId());
			if(null != portalLayout){
				dto.setContentName(portalLayout.getLabel());
			}
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.MODULEAPP){
			ModuleAppActionData actionData = (ModuleAppActionData)StringHelper.fromJsonString(portalItem.getActionData(), ModuleAppActionData.class);
			ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(actionData.getModuleAppId());
			if(null != serviceModuleApp){
//				String moduleAppTitle = configurationProvider.getValue(ConfigConstants.PORTAL_MODULE_APP_TITLE, "应用");
				dto.setContentName(serviceModuleApp.getName());
			}
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.THIRDURL){
			UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(portalItem.getActionData(), UrlActionData.class);
			if(null != actionData)
				dto.setContentName(actionData.getUrl());
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ZUOLINURL){
			UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(portalItem.getActionData(), UrlActionData.class);
			if(null != actionData)
				dto.setContentName(actionData.getUrl());
		}

		List<PortalContentScope> portalContentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
		dto.setScopes(processListPortalContentScopeDTO(portalContentScopes));

		User user = userProvider.findUserById(portalItem.getOperatorUid());
		if(null != user){
			dto.setOperatorUName(user.getNickName());
		}

		return dto;
	}

	private PortalItem checkPortalItem(Long id){
		PortalItem portalItem = portalItemProvider.findPortalItemById(id);
		if(null == portalItem){
			LOGGER.error("Unable to find the portalItem.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalItem.");
		}

		return portalItem;
	}

	private void createContentScopes(String contentType, Long contentId, List<PortalScope> portalScopes){
		User user = UserContext.current().getUser();
		List<PortalContentScope> scopes = new ArrayList<>();
		for (PortalScope scope: portalScopes) {
			if(null != scope.getScopeIds()){
				for (Long scopeId: scope.getScopeIds()) {
					PortalContentScope contentScope = new PortalContentScope();
					contentScope.setContentType(contentType);
					contentScope.setContentId(contentId);
					contentScope.setOperatorUid(user.getId());
					contentScope.setCreatorUid(user.getId());
					contentScope.setScopeType(scope.getScopeType());
					contentScope.setScopeId(scopeId);
					scopes.add(contentScope);
				}
			}
		}
		portalContentScopeProvider.createPortalContentScopes(scopes);
	}

	private List<PortalContentScopeDTO> processListPortalContentScopeDTO(List<PortalContentScope> portalContentScopes){
		Map<String, List<PortalScopeDTO>> portalScopeMap = new HashMap<>();
		for (PortalContentScope portalContentScope: portalContentScopes) {
			if(null == portalScopeMap.get(portalContentScope.getScopeType())){
				List<PortalScopeDTO> scopes = new ArrayList<>();
				scopes.add(processPortalScopeDTO(portalContentScope));
				portalScopeMap.put(portalContentScope.getScopeType(), scopes);
			}else{
				portalScopeMap.get(portalContentScope.getScopeType()).add(processPortalScopeDTO(portalContentScope));
			}
		}

		List<PortalContentScopeDTO> dtos = new ArrayList<>();
		for (Map.Entry<String, List<PortalScopeDTO>> entry : portalScopeMap.entrySet()) {
			PortalContentScopeDTO dto = new PortalContentScopeDTO();
			dto.setScopeType(entry.getKey());
			dto.setScopes(entry.getValue());
			dtos.add(dto);
		}
		return dtos;
	}

	private PortalScopeDTO processPortalScopeDTO(PortalContentScope portalContentScope){
		PortalScopeDTO dto = ConvertHelper.convert(portalContentScope, PortalScopeDTO.class);
		if(PortalScopeType.fromCode(portalContentScope.getScopeType()) == PortalScopeType.PM || PortalScopeType.fromCode(portalContentScope.getScopeType()) == PortalScopeType.ORGANIZATION){
			Organization organization = organizationProvider.findOrganizationById(portalContentScope.getScopeId());
			if(null != organization){
				dto.setScopeName(organization.getName());
			}
		}else if(PortalScopeType.fromCode(portalContentScope.getScopeType()) == PortalScopeType.COMMERCIAL || PortalScopeType.fromCode(portalContentScope.getScopeType()) == PortalScopeType.RESIDENTIAL){
			Community community = communityProvider.findCommunityById(portalContentScope.getScopeId());
			if(null != community){
				dto.setScopeName(community.getName());
			}
		}

		if(portalContentScope.getScopeId() == 0L){
			dto.setScopeName("全部");
		}
		return dto;
	}

	@Override
	public void reorderPortalItem(ReorderPortalItemCommand cmd) {
		if(null == cmd.getReorders() && cmd.getReorders().size() == 0){
			LOGGER.error("Params reorders is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Params reorders is null.");
		}

		User user = UserContext.current().getUser();
		this.dbProvider.execute((status) -> {
			for (PortalItemReorder portalItemReorder : cmd.getReorders()) {
				PortalItem portalItem = checkPortalItem(portalItemReorder.getItemId());
				portalItem.setOperatorUid(user.getId());
				portalItem.setDefaultOrder(portalItemReorder.getDefaultOrder());
				if(null != ItemDisplayFlag.fromCode(portalItemReorder.getDisplayFlag()))
					portalItem.setDisplayFlag(portalItemReorder.getDisplayFlag());
				portalItemProvider.updatePortalItem(portalItem);
			}
			return null;
		});
	}

	@Override
	public void reorderPortalItemGroup(ReorderPortalItemGroupCommand cmd) {
		if(null == cmd.getReorders() && cmd.getReorders().size() == 0){
			LOGGER.error("Params reorders is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Params reorders is null.");
		}

		User user = UserContext.current().getUser();
		this.dbProvider.execute((status) -> {
			for (PortalItemGroupReorder portalItemGroupReorder : cmd.getReorders()) {
				PortalItemGroup portalItemGroup = checkPortalItemGroup(portalItemGroupReorder.getItemGroupId());
				portalItemGroup.setOperatorUid(user.getId());
				portalItemGroup.setDefaultOrder(portalItemGroupReorder.getDefaultOrder());
				portalItemGroupProvider.updatePortalItemGroup(portalItemGroup);
			}
			return null;
		});
	}

	@Override
	public PortalItemDTO getPortalItemById(GetPortalItemByIdCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		return processPortalItemDTO(portalItem);
	}

	@Override
	public PortalItemCategoryDTO getPortalItemCategoryById(GetPortalItemCategoryCommand cmd) {
		PortalItemCategory portalItemCategory = checkPortalItemCategory(cmd.getId());
		PortalItemCategoryDTO dto = processPortalItemCategoryDTO(portalItemCategory);
		List<PortalItem> portalItems = portalItemProvider.listPortalItemByCategoryId(portalItemCategory.getId());

		List<PortalItemDTO> items = portalItems.stream().map(i ->{
			return processPortalItemDTO(i);
		}).collect(Collectors.toList());

		dto.setItems(items);
		return dto;
	}


	@Override
	public ListPortalItemCategoriesResponse listPortalItemCategories(ListPortalItemCategoriesCommand cmd) {
		Integer namespaceId= UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		checkPortalItemGroup(cmd.getItemGroupId());
		List<PortalItemCategory> portalItemCategories = portalItemCategoryProvider.listPortalItemCategory(namespaceId, cmd.getItemGroupId());
		PortalItemCategory category = new PortalItemCategory();
		category.setId(0L);
		category.setName("未分组");
		portalItemCategories.add(category);
		List<PortalItemCategoryDTO> dtos = portalItemCategories.stream().map(r ->{
			PortalItemCategoryDTO dto = processPortalItemCategoryDTO(r);
			List<PortalItem> portalItems = portalItemProvider.listPortalItems(r.getId(), cmd.getItemGroupId());

			List<PortalItemDTO> items = portalItems.stream().map(i ->{
				return processPortalItemDTO(i);
			}).collect(Collectors.toList());

			Collections.sort(items, new Comparator<PortalItemDTO>() {
				@Override
				public int compare(PortalItemDTO o1, PortalItemDTO o2) {
					Integer order1 = 0;
					if(null != o1.getMoreOrder()) order1 = o1.getMoreOrder();

					Integer order2 = 0;
					if(null != o2.getMoreOrder()) order2 = o2.getMoreOrder();
					return order1 - order2;
				}
			});
			dto.setItems(items);
			return dto;
		}).collect(Collectors.toList());
		Collections.sort(dtos, new Comparator<PortalItemCategoryDTO>() {
			@Override
			public int compare(PortalItemCategoryDTO o1, PortalItemCategoryDTO o2) {
				Integer order1 = 0;
				if(null != o1.getDefaultOrder()) order1 = o1.getDefaultOrder();

				Integer order2 = 0;
				if(null != o2.getDefaultOrder()) order2 = o2.getDefaultOrder();
				return order1 - order2;
			}
		});
		return new ListPortalItemCategoriesResponse(dtos);
	}

	@Override
	public PortalItemCategoryDTO createPortalItemCategory(CreatePortalItemCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		checkPortalItemGroup(cmd.getItemGroupId());
		PortalItemCategory portalItemCategory = ConvertHelper.convert(cmd, PortalItemCategory.class);
		portalItemCategory.setStatus(PortalItemCategoryStatus.ACTIVE.getCode());
		portalItemCategory.setLabel(cmd.getName());
		portalItemCategory.setName(null);
		portalItemCategory.setOperatorUid(user.getId());
		portalItemCategory.setCreatorUid(user.getId());
		portalItemCategory.setNamespaceId(namespaceId);
		portalItemCategory.setItemGroupId(cmd.getItemGroupId());
		this.dbProvider.execute((status) -> {
			portalItemCategoryProvider.createPortalItemCategory(portalItemCategory);
			if(null != cmd.getScopes() && cmd.getScopes().size() > 0){
				createContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId(), cmd.getScopes());
			}
			return null;
		});
		return processPortalItemCategoryDTO(portalItemCategory);
	}

	@Override
	public PortalItemCategoryDTO updatePortalItemCategory(UpdatePortalItemCategoryCommand cmd) {
		PortalItemCategory portalItemCategory = checkPortalItemCategory(cmd.getId());
		portalItemCategory.setLabel(cmd.getName());
		portalItemCategory.setOperatorUid(UserContext.current().getUser().getId());
		portalItemCategory.setIconUri(cmd.getIconUri());
		this.dbProvider.execute((status) -> {
			portalItemCategoryProvider.updatePortalItemCategory(portalItemCategory);
			if(null != cmd.getScopes() && cmd.getScopes().size() > 0){
				portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId());
				createContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId(), cmd.getScopes());
			}
			return null;
		});

		return processPortalItemCategoryDTO(portalItemCategory);
	}

	@Override
	public void deletePortalItemCategory(DeletePortalItemCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		PortalItemCategory portalItemCategory = checkPortalItemCategory(cmd.getId());
		checkPortalItemCategory(cmd.getMoveItemCategoryId());
		portalItemCategory.setOperatorUid(user.getId());
		portalItemCategory.setStatus(PortalItemCategoryStatus.INACTIVE.getCode());

		this.dbProvider.execute((status) -> {
			List<PortalItem> portalItems = portalItemProvider.listPortalItemByCategoryId(portalItemCategory.getId());
			List<PortalItemReorder> itemReorders = new ArrayList<>();
			for (PortalItem portalItem: portalItems) {
				PortalItemReorder reorder = new PortalItemReorder();
				reorder.setItemId(portalItem.getId());
				itemReorders.add(reorder);
			}
			PortalItemCategoryRank rank = new PortalItemCategoryRank();
			rank.setItemCategoryId(cmd.getMoveItemCategoryId());
			rank.setItems(itemReorders);
			//把要删除的分类移动到对应的分类
			setPortalItemCategory(rank, user);
			portalItemCategoryProvider.updatePortalItemCategory(portalItemCategory);
			portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId());
			return null;
		});
	}

	private PortalItemCategoryDTO processPortalItemCategoryDTO(PortalItemCategory portalItemCategory){
		PortalItemCategoryDTO dto = ConvertHelper.convert(portalItemCategory, PortalItemCategoryDTO.class);
		dto.setName(portalItemCategory.getLabel());
		List<PortalItem> portalItems = getItemAllOrMore(portalItemCategory.getNamespaceId(),portalItemCategory.getItemGroupId(), AllOrMoreType.ALL);
		if(portalItems.size() > 0){
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItems.get(0).getActionData(), AllOrMoreActionData.class);
			if(null == AlignType.fromCode(portalItemCategory.getAlign())){
				dto.setAlign(actionData.getAlign());
			}

			if(StringUtils.isEmpty(portalItemCategory.getIconUri())){
				portalItemCategory.setIconUri(actionData.getDefUri());
			}
		}

		if(!StringUtils.isEmpty(portalItemCategory.getIconUri())){
			String url = contentServerService.parserUri(portalItemCategory.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setIconUrl(url);
		}

		List<PortalContentScope> portalContentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId());
		dto.setScopes(processListPortalContentScopeDTO(portalContentScopes));

		return dto;
	}

	@Override
	public PortalItemDTO getAllOrMoreItem(GetItemAllOrMoreCommand cmd){
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		checkPortalItemGroup(cmd.getItemGroupId());
		List<PortalItem> items = getItemAllOrMore(namespaceId, cmd.getItemGroupId(), AllOrMoreType.fromCode(cmd.getMoreOrAllType()));
		if(items.size() > 0){
			return processPortalItemDTO(items.get(0));
		}
		return null;
	}

	private List<PortalItem> getItemAllOrMore(Integer namespaceId,Long itemGroupId, AllOrMoreType type){
		List<PortalItem> items = new ArrayList<>();
		List<PortalItem> portalItems = portalItemProvider.listPortalItems(null, namespaceId, PortalItemActionType.ALLORMORE.getCode(), itemGroupId, PortalItemStatus.INACTIVE.getCode());
		for (PortalItem portalItem: portalItems) {
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
			if(null != actionData && AllOrMoreType.fromCode(actionData.getType()) == type){
				items.add(portalItem);
			}
		}
		return items;
	}

	private PortalItemCategory checkPortalItemCategory(Long id){
		PortalItemCategory portalItemCategory = portalItemCategoryProvider.findPortalItemCategoryById(id);
		if(null == portalItemCategory){
			LOGGER.error("Unable to find the portalItemCategory.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalItemCategory.");
		}

		return portalItemCategory;
	}

	@Override
	public void reorderPortalItemCategory(ReorderPortalItemCategoryCommand cmd) {
		if(null == cmd.getReorders() && cmd.getReorders().size() == 0){
			LOGGER.error("Params reorders is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Params reorders is null.");
		}

		User user = UserContext.current().getUser();
		this.dbProvider.execute((status) -> {
			for (PortalItemCategoryReorder portalItemGroupReorder : cmd.getReorders()) {
				PortalItemCategory portalItemCategory = portalItemCategoryProvider.findPortalItemCategoryById(portalItemGroupReorder.getItemCategoryId());
				portalItemCategory.setOperatorUid(user.getId());
				portalItemCategory.setDefaultOrder(portalItemGroupReorder.getDefaultOrder());
				portalItemCategoryProvider.updatePortalItemCategory(portalItemCategory);
			}
			return null;
		});

	}

	@Override
	public void setPortalItemActionData(SetPortalItemActionDataCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		portalItem.setOperatorUid(UserContext.current().getUser().getId());
		portalItem.setActionData(cmd.getActionData());
		portalItemProvider.updatePortalItem(portalItem);
	}

	@Override
	public void setItemCategoryDefStyle(SetItemCategoryDefStyleCommand cmd) {
		PortalItem portalItem = checkPortalItem(cmd.getId());
		portalItem.setOperatorUid(UserContext.current().getUser().getId());
		if(!StringUtils.isEmpty(portalItem.getActionData())){
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
			actionData.setAlign(cmd.getAlign());
			actionData.setDefUri(cmd.getDefUri());
			portalItem.setActionData(StringHelper.toJsonString(actionData));
			portalItemProvider.updatePortalItem(portalItem);
		}
	}

	@Override
	public void rankPortalItemCategory(RankPortalItemCategoryCommand cmd) {
		if(null == cmd.getRanks() && cmd.getRanks().size() == 0){
			LOGGER.error("Params ranks is null.cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Params ranks is null.");
		}
		User user = UserContext.current().getUser();
		this.dbProvider.execute((status) -> {
			for (PortalItemCategoryRank portalItemCategoryRank : cmd.getRanks()) {
				setPortalItemCategory(portalItemCategoryRank, user);
			}
			return null;
		});

	}

	private void setPortalItemCategory(PortalItemCategoryRank rank, User user){
		PortalItemCategory portalItemCategory = checkPortalItemCategory(rank.getItemCategoryId());
		if(null != rank.getItems() && rank.getItems().size() > 0){
			for (PortalItemReorder item: rank.getItems()) {
				PortalItem portalItem = checkPortalItem(item.getItemId());
				//更多全部不进行分类
				if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
					continue;
				}
				portalItem.setOperatorUid(user.getId());
				portalItem.setItemCategoryId(portalItemCategory.getId());
				if(null != ItemDisplayFlag.fromCode(item.getDisplayFlag()))
					portalItem.setDisplayFlag(item.getDisplayFlag());
				if(null != item.getMoreOrder())
					portalItem.setMoreOrder(item.getMoreOrder());
				if(null != portalItem.getDefaultOrder())
					portalItem.setDefaultOrder(item.getDefaultOrder());
				portalItemProvider.updatePortalItem(portalItem);
			}
		}
	}


	@Override
	public PortalItemGroupDTO getPortalItemGroupById(GetPortalItemGroupByIdCommand cmd) {
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getId());
		return processPortalItemGroupDTO(portalItemGroup);
	}

	@Override
	public ListPortalNavigationBarsResponse listPortalNavigationBars(ListPortalNavigationBarsCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<PortalNavigationBar> portalNavigationBars = portalNavigationBarProvider.listPortalNavigationBar(namespaceId);
		return new ListPortalNavigationBarsResponse(portalNavigationBars.stream().map(r ->{
			return processPortalNavigationBarDTO(r);
		}).collect(Collectors.toList()));
	}

	@Override
	public PortalNavigationBarDTO createPortalNavigationBar(CreatePortalNavigationBarCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		PortalNavigationBar portalNavigationBar = ConvertHelper.convert(cmd, PortalNavigationBar.class);
		portalNavigationBar.setOperatorUid(user.getId());
		portalNavigationBar.setCreatorUid(user.getId());
		portalNavigationBar.setNamespaceId(namespaceId);
		portalNavigationBar.setStatus(PortalNavigationBarStatus.ACTIVE.getCode());
		portalNavigationBarProvider.createPortalNavigationBar(portalNavigationBar);
		return processPortalNavigationBarDTO(portalNavigationBar);
	}

	@Override
	public PortalNavigationBarDTO updatePortalNavigationBar(UpdatePortalNavigationBarCommand cmd) {
		PortalNavigationBar portalNavigationBar = checkPortalNavigationBar(cmd.getId());
		portalNavigationBar.setLabel(cmd.getLabel());
		portalNavigationBar.setDescription(cmd.getDescription());
		portalNavigationBar.setTargetType(cmd.getTargetType());
		portalNavigationBar.setTargetId(cmd.getTargetId());
		portalNavigationBar.setIconUri(cmd.getIconUri());
		portalNavigationBar.setSelectedIconUri(cmd.getSelectedIconUri());
		portalNavigationBar.setOperatorUid(UserContext.current().getUser().getId());
		portalNavigationBarProvider.updatePortalNavigationBar(portalNavigationBar);
		return processPortalNavigationBarDTO(portalNavigationBar);
	}

	@Override
	public void deletePortalNavigationBar(DeletePortalNavigationBarCommand cmd) {
		PortalNavigationBar portalNavigationBar = checkPortalNavigationBar(cmd.getId());
		portalNavigationBar.setOperatorUid(UserContext.current().getUser().getId());
		portalNavigationBar.setStatus(PortalNavigationBarStatus.INACTIVE.getCode());
		portalNavigationBarProvider.updatePortalNavigationBar(portalNavigationBar);
	}

	private PortalNavigationBar checkPortalNavigationBar(Long id){
		PortalNavigationBar portalNavigationBar = portalNavigationBarProvider.findPortalNavigationBarById(id);
		if(null == portalNavigationBar){
			LOGGER.error("Unable to find the portalNavigationBar.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalNavigationBar.");
		}

		return portalNavigationBar;
	}

	private PortalNavigationBarDTO processPortalNavigationBarDTO(PortalNavigationBar portalNavigationBar){
		PortalNavigationBarDTO dto = ConvertHelper.convert(portalNavigationBar, PortalNavigationBarDTO.class);
		dto.setCreateTime(portalNavigationBar.getCreateTime().getTime());
		dto.setUpdateTime(portalNavigationBar.getUpdateTime().getTime());
		User operator = userProvider.findUserById(portalNavigationBar.getOperatorUid());
		if(null != operator) dto.setOperatorUName(operator.getNickName());

		if(EntityType.fromCode(portalNavigationBar.getTargetType()) == EntityType.PORTAL_LAYOUT){
			PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(portalNavigationBar.getTargetId());
			if(null != portalLayout){
				String layoutTitle = configurationProvider.getValue(ConfigConstants.PORTAL_LAYOUT_TITLE, "门户");
				dto.setContentName(layoutTitle + "-" + portalLayout.getLabel());
			}
		}else if(EntityType.fromCode(portalNavigationBar.getTargetType()) == EntityType.SERVICE_MODULE_APP){
			ServiceModuleApp serviceModuleApp = serviceModuleAppProvider.findServiceModuleAppById(portalNavigationBar.getTargetId());
			if(null != serviceModuleApp){
				String moduleAppTitle = configurationProvider.getValue(ConfigConstants.PORTAL_MODULE_APP_TITLE, "应用");
				dto.setContentName(moduleAppTitle + "-" + serviceModuleApp.getName());
			}
		}
		if(!StringUtils.isEmpty(portalNavigationBar.getIconUri())){
			String url = contentServerService.parserUri(portalNavigationBar.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setIconUrl(url);
		}
		if(!StringUtils.isEmpty(portalNavigationBar.getSelectedIconUri())){
			String url = contentServerService.parserUri(portalNavigationBar.getSelectedIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
			dto.setSelectedIconUrl(url);
		}
		return dto;
	}

	@Override
	public ListScopeResponse listScopes(ListScopeCommand cmd){
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		ListScopeResponse response = new ListScopeResponse();
		List<ScopeDTO> dtos = new ArrayList<>();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getAnchor());
		OrganizationType oType = null;
		if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.PM){
			oType = OrganizationType.PM;
		}else if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.ORGANIZATION){
			oType = OrganizationType.ENTERPRISE;
		}
		if(null != oType){
			List<String> groupTypes = new ArrayList<>();
			groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
			List<Organization> organizations = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, cmd.getKeywords(), oType.getCode(), locator, pageSize);
			for (Organization organization: organizations) {
				dtos.add(ConvertHelper.convert(organization, ScopeDTO.class));
			}
			response.setDtos(dtos);
			response.setNextPageAnchor(locator.getAnchor());
			return response;
		}

		CommunityType cType = null;
		if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.COMMERCIAL){
			cType = CommunityType.COMMERCIAL;
		}else if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.RESIDENTIAL){
			cType = CommunityType.RESIDENTIAL;
		}
		CommunityType communityType = cType;
		if(null != communityType){
			List<Community> communities = communityProvider.listCommunities(namespaceId, locator, pageSize, new ListingQueryBuilderCallback() {
				@Override
				public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
					query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(communityType.getCode()));
					if(!StringUtils.isEmpty(cmd.getKeywords())){
						query.addConditions(Tables.EH_COMMUNITIES.NAME.like(cmd.getKeywords() + "%").or(Tables.EH_COMMUNITIES.ALIAS_NAME.like(cmd.getKeywords() + "%")));

					}
					return query;
				}
			});
			for (Community community: communities) {
				dtos.add(ConvertHelper.convert(community, ScopeDTO.class));
			}
		}
		response.setDtos(dtos);
		response.setNextPageAnchor(locator.getAnchor());
		return response;
	}


	@Override
	public ListScopeResponse searchScopes(ListScopeCommand cmd){

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		if(null == cmd.getAnchor()){
			cmd.setAnchor(0L);
		}
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		ListScopeResponse response = new ListScopeResponse();
		List<ScopeDTO> dtos = new ArrayList<>();
		OrganizationType oType = null;
		if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.PM){
			oType = OrganizationType.PM;
		}else if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.ORGANIZATION){
			oType = OrganizationType.ENTERPRISE;
		}
		if(null != oType){
			SearchOrganizationCommand command = new SearchOrganizationCommand();
			command.setNamespaceId(namespaceId);
			command.setOrganizationType(oType.getCode());
			command.setPageAnchor(cmd.getAnchor());
			command.setPageSize(pageSize);
			command.setKeyword(cmd.getKeywords());
			OrganizationQueryResult result = organizationSearcher.queryOrganization(command);
			response.setDtos(result.getDtos().stream().map(r ->{
				return ConvertHelper.convert(r, ScopeDTO.class);
			}).collect(Collectors.toList()));
			response.setNextPageAnchor(result.getPageAnchor());
			return response;
		}

		CommunityType cType = null;
		if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.COMMERCIAL){
			cType = CommunityType.COMMERCIAL;
		}else if(PortalScopeType.fromCode(cmd.getScopeType()) == PortalScopeType.RESIDENTIAL){
			cType = CommunityType.RESIDENTIAL;
		}
		CommunityType communityType = cType;
		if(null != communityType){
			List<CommunityDoc> communities = communitySearcher.searchDocs(cmd.getKeywords(), cType.getCode(), null, null, cmd.getAnchor().intValue(), cmd.getPageSize());
			for (CommunityDoc communityDoc: communities) {
				dtos.add(ConvertHelper.convert(communityDoc, ScopeDTO.class));
			}
		}
		response.setDtos(dtos);
		response.setNextPageAnchor(cmd.getAnchor() + 1);
		return response;
	}

	@Override
	public PortalPublishLogDTO publish(PublishCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<PortalLayout> layouts = portalLayoutProvider.listPortalLayout(cmd.getNamespaceId(), null);
		PortalPublishLog portalPublishLog = new PortalPublishLog();
		portalPublishLog.setNamespaceId(namespaceId);
		portalPublishLog.setStatus(PortalPublishLogStatus.PUBLISHING.getCode());
		portalPublishLog.setCreatorUid(user.getId());
		portalPublishLog.setOperatorUid(user.getId());
		portalPublishLogProvider.createPortalPublishLog(portalPublishLog);

		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				try{
					UserContext.setCurrentUser(user);
					dbProvider.execute((status) -> {

						//发布item分类
						publishItemCategory(namespaceId);

						for (PortalLayout layout: layouts) {
							//发布layout
							publishLayout(layout);
						}

						portalPublishLog.setStatus(PortalPublishLogStatus.SUCCESS.getCode());
						portalPublishLogProvider.updatePortalPublishLog(portalPublishLog);
						return null;
					});
				}catch (Exception e){
					portalPublishLog.setStatus(PortalPublishLogStatus.FAILING.getCode());
					portalPublishLogProvider.updatePortalPublishLog(portalPublishLog);
					LOGGER.error("publish error:", e);
				}finally {
					LOGGER.debug("publish end...");
				}

			}
		});
		return ConvertHelper.convert(portalPublishLog, PortalPublishLogDTO.class);
	}

	public PortalPublishLogDTO getPortalPublishLog(GetPortalPublishLogCommand cmd){
		return ConvertHelper.convert(portalPublishLogProvider.findPortalPublishLogById(cmd.getId()), PortalPublishLogDTO.class);
	}


	private void publishLayout(PortalLayout layout){

		User user = UserContext.current().getUser();

		List<PortalLaunchPadMapping> portalLaunchPadMappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_LAYOUT.getCode(), layout.getId(), null);

		String now = DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH);
		Long versionCode = Long.valueOf(now + "01");
		LaunchPadLayoutJson layoutJson = new LaunchPadLayoutJson();
		layoutJson.setDisplayName(layout.getLabel());
		layoutJson.setLayoutName(layout.getName());
		List<LaunchPadLayoutGroup> groups = new ArrayList<>();
		List<PortalItemGroup> itemGroups = portalItemGroupProvider.listPortalItemGroup(layout.getId());
		for (PortalItemGroup itemGroup: itemGroups) {
			LaunchPadLayoutGroup group = ConvertHelper.convert(itemGroup, LaunchPadLayoutGroup.class);
			group.setGroupName(itemGroup.getLabel());
			if(null != itemGroup.getSeparatorFlag()){
				group.setSeparatorFlag(itemGroup.getSeparatorFlag().intValue());
			}

			if(null != itemGroup.getSeparatorHeight()){
				group.setSeparatorHeight(itemGroup.getSeparatorHeight().doubleValue());
			}

			ItemGroupInstanceConfig instanceConfig = new ItemGroupInstanceConfig();
			if(!StringUtils.isEmpty(itemGroup.getInstanceConfig())){
				instanceConfig = (ItemGroupInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);
				group.setColumnCount(instanceConfig.getColumnCount());
			}
			if(TitleFlag.TRUE == TitleFlag.fromCode(instanceConfig.getTitleFlag())){
				group.setTitle(instanceConfig.getTitle());
				if(!StringUtils.isEmpty(instanceConfig.getTitleUri())){
					String url = contentServerService.parserUri(instanceConfig.getTitleUri(), EntityType.USER.getCode(), user.getId());
					group.setIconUrl(url);
				}
			}

			if(Widget.fromCode(group.getWidget()) == Widget.NAVIGATOR){
				NavigatorInstanceConfig config = new NavigatorInstanceConfig();
				config.setBackgroundColor(instanceConfig.getBackgroundColor());
				if(Style.fromCode(group.getStyle()) == Style.GALLERY){
					config.setCssStyleFlag(CssStyleFlagType.YES.getCode());
				}
				if(null == instanceConfig.getPadding()){
					instanceConfig.setPadding(1);
				}

				if(null == instanceConfig.getMargin()){
					instanceConfig.setMargin(1);
				}

				if(null == instanceConfig.getBackgroundColor()){
					instanceConfig.setBackgroundColor("#FFFFFF");
				}
				config.setPaddingBottom(instanceConfig.getPadding());
				config.setPaddingLeft(instanceConfig.getPadding());
				config.setPaddingRight(instanceConfig.getPadding());
				config.setPaddingTop(instanceConfig.getPadding());
				config.setColumnSpacing(instanceConfig.getMargin());
				config.setLineSpacing(instanceConfig.getMargin());
				config.setItemGroup(itemGroup.getName());
				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.BANNERS){
				BannersInstanceConfig config = new BannersInstanceConfig();
				config.setItemGroup(itemGroup.getName());
				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.NEWS){
				String instanceConf = setItemModuleAppActionData(itemGroup.getLabel(), instanceConfig.getModuleAppId());
				if(null != instanceConf){
					NewsInstanceConfig config = (NewsInstanceConfig)StringHelper.fromJsonString(instanceConf, NewsInstanceConfig.class);
					config.setItemGroup(itemGroup.getName());
					config.setTimeWidgetStyle(instanceConfig.getTimeWidgetStyle());
					group.setInstanceConfig(config);
				}
			}else if(Widget.fromCode(group.getWidget()) == Widget.NEWS_FLASH){
				String instanceConf = setItemModuleAppActionData(itemGroup.getLabel(), instanceConfig.getModuleAppId());
				if(null != instanceConf){
					NewsFlashInstanceConfig config = (NewsFlashInstanceConfig)StringHelper.fromJsonString(instanceConf, NewsFlashInstanceConfig.class);
					config.setItemGroup(itemGroup.getName());
					config.setTimeWidgetStyle(instanceConfig.getTimeWidgetStyle());
					config.setNewsSize(instanceConfig.getNewsSize());
					group.setInstanceConfig(config);
				}

			}else if(Widget.fromCode(group.getWidget()) == Widget.BULLETINS){
				BulletinsInstanceConfig config = new BulletinsInstanceConfig();
				config.setItemGroup(itemGroup.getName());
				config.setRowCount(instanceConfig.getRowCount());
				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.OPPUSH){
				OPPushInstanceConfig config = new OPPushInstanceConfig();
				config.setItemGroup(itemGroup.getName());
				config.setNewsSize(instanceConfig.getNewsSize());
//				config.setDescriptionHeight();
//				config.setSubjectHeight();
				if(EntityType.fromCode(itemGroup.getContentType()) == EntityType.ACTIVITY){
					itemGroup.setName("OPPushActivity");
				}
				if(EntityType.fromCode(itemGroup.getContentType()) == EntityType.SERVICE_ALLIANCE){
				}
				if(EntityType.fromCode(itemGroup.getContentType()) == EntityType.BIZ){
					itemGroup.setName("OPPushBiz");
				}
				publishOPPushItem(itemGroup, layout.getLocation());
				config.setItemGroup(itemGroup.getName());
//				group.setInstanceConfig(StringHelper.toJsonString(config));
				group.setInstanceConfig(config);
			}else if(Widget.fromCode(group.getWidget()) == Widget.TAB){
				TabInstanceConfig config = new TabInstanceConfig();
				publishTabItem(itemGroup);
				config.setItemGroup(itemGroup.getName());
				group.setInstanceConfig(config);
			}
			groups.add(group);

			if(Widget.fromCode(group.getWidget()) == Widget.NAVIGATOR){
				// 发布item
				publishItem(itemGroup);
			}
		}
		layoutJson.setGroups(groups);
		LaunchPadLayout launchPadLayout = new LaunchPadLayout();
		if(portalLaunchPadMappings.size() > 0){
			for (PortalLaunchPadMapping mapping: portalLaunchPadMappings) {
				launchPadLayout = launchPadProvider.findLaunchPadLayoutById(mapping.getLaunchPadContentId());
				if(null != launchPadLayout){
					if(launchPadLayout.getVersionCode().toString().indexOf(now) != -1){
						versionCode = launchPadLayout.getVersionCode() + 1;
					}
					launchPadLayout.setVersionCode(versionCode);
					layoutJson.setVersionCode(versionCode.toString());
					String json = StringHelper.toJsonString(layoutJson);
					launchPadLayout.setLayoutJson(json);
					launchPadProvider.updateLaunchPadLayout(launchPadLayout);

				}
			}
		}else{
			layoutJson.setVersionCode(versionCode.toString());
			String json = StringHelper.toJsonString(layoutJson);
			launchPadLayout.setNamespaceId(layout.getNamespaceId());
			launchPadLayout.setName(layout.getName());
			launchPadLayout.setVersionCode(versionCode);
			launchPadLayout.setMinVersionCode(0L);
			launchPadLayout.setStatus(LaunchPadLayoutStatus.ACTIVE.getCode());
			launchPadLayout.setScopeCode((byte)0);
			launchPadLayout.setApplyPolicy((byte)0);
			launchPadLayout.setScopeId(0L);
			launchPadLayout.setLayoutJson(json);
			for (SceneType sceneType: SceneType.values()) {
				if(sceneType == SceneType.DEFAULT ||
						sceneType == SceneType.PARK_TOURIST ||
						sceneType == SceneType.PM_ADMIN){
					launchPadLayout.setSceneType(sceneType.getCode());
					launchPadProvider.createLaunchPadLayout(launchPadLayout);
					PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
					mapping.setContentType(EntityType.PORTAL_LAYOUT.getCode());
					mapping.setPortalContentId(layout.getId());
					mapping.setCreatorUid(user.getId());
					mapping.setLaunchPadContentId(launchPadLayout.getId());
					portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
				}
			}
		}
	}


	private void publishTabItem(PortalItemGroup itemGroup){
		List<PortalItem> portalItems = portalItemProvider.listPortalItemByGroupId(itemGroup.getId());
		for (PortalItem portalItem: portalItems) {
			LaunchPadItem item = ConvertHelper.convert(portalItem, LaunchPadItem.class);
			if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.LAYOUT){
				setItemLayoutActionData(item, portalItem.getActionData());
			}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.MODULEAPP){
				setItemModuleAppActionData(item, portalItem.getActionData());
			}

			item.setAppId(AppConstants.APPID_DEFAULT);
			item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
			item.setMinVersion(1L);
			item.setItemGroup(portalItem.getGroupName());
			item.setItemLabel(portalItem.getLabel());
			item.setItemName(portalItem.getName());
			item.setDeleteFlag(DeleteFlagType.YES.getCode());
			item.setScaleType(ScaleType.TAILOR.getCode());

			for (SceneType sceneType: SceneType.values()) {
				if(sceneType == SceneType.PARK_TOURIST ||
						sceneType == SceneType.PM_ADMIN){
					item.setSceneType(sceneType.getCode());
					launchPadProvider.createLaunchPadItem(item);
				}
			}
		}
	}

	private void publishOPPushItem(PortalItemGroup itemGroup, String location){
		List<LaunchPadItem> items = launchPadProvider.findLaunchPadItem(itemGroup.getNamespaceId(), itemGroup.getName(), location);
		for (LaunchPadItem item: items) {
			launchPadProvider.deleteLaunchPadItem(item.getId());
		}
		PortalLayout layout = portalLayoutProvider.findPortalLayoutById(itemGroup.getLayoutId());
		LaunchPadItem item = new LaunchPadItem();
		ItemGroupInstanceConfig instanceConfig = (ItemGroupInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);
		item.setNamespaceId(itemGroup.getNamespaceId());
		item.setAppId(AppConstants.APPID_DEFAULT);
		item.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
		item.setMinVersion(1L);
		item.setItemGroup(itemGroup.getName());
		item.setItemLocation(location);
		item.setItemLabel(instanceConfig.getTitle());
		item.setItemName(instanceConfig.getTitle());
		item.setDeleteFlag(DeleteFlagType.YES.getCode());
		item.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());
		item.setScaleType(ScaleType.TAILOR.getCode());
		item.setScopeCode(ScopeType.ALL.getCode());
		item.setScopeId(0L);
		if(null != layout){
			item.setItemLocation(layout.getLocation());
		}
		if(EntityType.fromCode(itemGroup.getContentType()) == EntityType.BIZ){
			item.setActionType(ActionType.OFFICIAL_URL.getCode());
			UrlActionData data = new UrlActionData();
			data.setUrl(instanceConfig.getBizUrl());
			item.setActionData(StringHelper.toJsonString(data));
		}else{
			ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(instanceConfig.getModuleAppId());
			if(null != moduleApp){
				item.setActionType(moduleApp.getActionType());
				item.setActionData(moduleApp.getInstanceConfig());
			}
			setItemModuleAppActionData(item, instanceConfig.getModuleAppId());
		}
		for (SceneType sceneType: SceneType.values()) {
			if(sceneType == SceneType.PARK_TOURIST ||
					sceneType == SceneType.PM_ADMIN){
				item.setSceneType(sceneType.getCode());
				launchPadProvider.createLaunchPadItem(item);
			}
		}
	}

	private void publishItem(PortalItemGroup itemGroup){
		User user = UserContext.current().getUser();
		List<PortalItem> portalItems = portalItemProvider.listPortalItemByGroupId(itemGroup.getId(), null);
		Map<Long, String> categoryIdMap = getItemCategoryMap(itemGroup.getNamespaceId());
		for (PortalItem portalItem: portalItems) {
			List<PortalLaunchPadMapping> mappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_ITEM.getCode(), portalItem.getId(), null);

			if(null != mappings && mappings.size() > 0){
				for (PortalLaunchPadMapping mapping: mappings) {
					launchPadProvider.deleteLaunchPadItem(mapping.getLaunchPadContentId());
					portalLaunchPadMappingProvider.deletePortalLaunchPadMapping(mapping.getId());
				}
			}

			if(PortalItemStatus.ACTIVE == PortalItemStatus.fromCode(portalItem.getStatus())){
				List<PortalContentScope> contentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
				for (PortalContentScope scope: contentScopes) {
					LaunchPadItem item = ConvertHelper.convert(portalItem, LaunchPadItem.class);
					item.setAppId(AppConstants.APPID_DEFAULT);
					item.setApplyPolicy(ApplyPolicy.DEFAULT.getCode());
					item.setMinVersion(1L);
					item.setItemGroup(portalItem.getGroupName());
					item.setItemLabel(portalItem.getLabel());
					item.setItemName(portalItem.getName());
					item.setDeleteFlag(DeleteFlagType.YES.getCode());
					item.setScaleType(ScaleType.TAILOR.getCode());

					//更多全部不进行分类
					if(PortalItemActionType.fromCode(portalItem.getActionType()) != PortalItemActionType.ALLORMORE){
						item.setCategryName(categoryIdMap.get(portalItem.getItemCategoryId()));
					}

					if(PortalScopeType.RESIDENTIAL == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.RESIDENTIAL.getCode());
						item.setSceneType(SceneType.DEFAULT.getCode());
					}else if(PortalScopeType.COMMERCIAL == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.COMMUNITY.getCode());
						item.setSceneType(SceneType.PARK_TOURIST.getCode());
					}else if(PortalScopeType.PM == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.PM.getCode());
						item.setSceneType(SceneType.PM_ADMIN.getCode());
					}else if(PortalScopeType.ORGANIZATION == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.ORGANIZATION.getCode());
						item.setSceneType(SceneType.PARK_TOURIST.getCode());
					}
					item.setScopeId(scope.getScopeId());
					if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.LAYOUT){
						setItemLayoutActionData(item, portalItem.getActionData());
					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.MODULEAPP){
						setItemModuleAppActionData(item, portalItem.getActionData());
					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ZUOLINURL){
						item.setActionType(ActionType.OFFICIAL_URL.getCode());
						item.setActionData(portalItem.getActionData());
					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.THIRDURL){
						item.setActionType(ActionType.THIRDPART_URL.getCode());
						item.setActionData(portalItem.getActionData());
					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
						AllOrMoreActionData data = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
						if(AllOrMoreType.fromCode(data.getType()) == AllOrMoreType.ALL){
							item.setActionType(ActionType.ALL_BUTTON.getCode());
						}else{
							item.setActionType(ActionType.MORE_BUTTON.getCode());
						}
						MoreActionData actionData = new MoreActionData();
						actionData.setItemLocation(portalItem.getItemLocation());
						actionData.setItemGroup(portalItem.getGroupName());
						item.setActionData(StringHelper.toJsonString(actionData));
						item.setDeleteFlag(DeleteFlagType.NO.getCode());
					}
					launchPadProvider.createLaunchPadItem(item);

					PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
					mapping.setContentType(EntityType.PORTAL_ITEM.getCode());
					mapping.setPortalContentId(portalItem.getId());
					mapping.setLaunchPadContentId(item.getId());
					mapping.setCreatorUid(user.getId());
					portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
				}
			}
		}
	}

	private void setItemLayoutActionData(LaunchPadItem item, String actionData){
		item.setActionType(ActionType.NAVIGATION.getCode());
		LayoutActionData data = (LayoutActionData)StringHelper.fromJsonString(actionData, LayoutActionData.class);
		PortalLayout layout = portalLayoutProvider.findPortalLayoutById(data.getLayoutId());
		if(null != layout){
			NavigationActionData navigationActionData = new NavigationActionData();
			navigationActionData.setItemLocation(layout.getLocation());
			navigationActionData.setLayoutName(layout.getName());
			navigationActionData.setTitle(layout.getLabel());
			item.setActionData(StringHelper.toJsonString(navigationActionData));
		}else{
			LOGGER.error("Unable to find the portal layout.id = {}, actionData = {}", data.getLayoutId(), actionData);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portal layout.");
		}
	}

	private void setItemModuleAppActionData(LaunchPadItem item, String actionData){
		ModuleAppActionData data = (ModuleAppActionData)StringHelper.fromJsonString(actionData, ModuleAppActionData.class);
		setItemModuleAppActionData(item, data.getModuleAppId());
	}

	private void setItemModuleAppActionData(LaunchPadItem item, Long moduleAppId){
		ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(moduleAppId);
		if(null != moduleApp){
			PortalPublishHandler handler = getPortalPublishHandler(moduleApp.getModuleId());
			item.setActionType(moduleApp.getActionType());
			if(null != handler){
				String instanceConfig = handler.publish(moduleApp.getNamespaceId(), moduleApp.getInstanceConfig(), item.getItemLabel());
				moduleApp.setInstanceConfig(instanceConfig);
				serviceModuleAppProvider.updateServiceModuleApp(moduleApp);
				item.setActionData(handler.getItemActionData(moduleApp.getNamespaceId(), instanceConfig));
			}else{
				item.setActionData(moduleApp.getInstanceConfig());
			}
		}
	}

	private String setItemModuleAppActionData(String name, Long moduleAppId){
		ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(moduleAppId);
		if(null != moduleApp){
			PortalPublishHandler handler = getPortalPublishHandler(moduleApp.getModuleId());
			if(null != handler){
				String instanceConfig = handler.publish(moduleApp.getNamespaceId(), moduleApp.getInstanceConfig(), name);
				moduleApp.setInstanceConfig(instanceConfig);
				serviceModuleAppProvider.updateServiceModuleApp(moduleApp);
				return instanceConfig;
			}
		}
		return null;
	}

	public void publishItemCategory(Integer namespaceId){
		User user = UserContext.current().getUser();
		List<PortalItem> allItems = getItemAllOrMore(namespaceId, null, AllOrMoreType.ALL);
		for (PortalItem item: allItems) {
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(item.getActionData(), AllOrMoreActionData.class);
			List<PortalItemCategory> categorys = portalItemCategoryProvider.listPortalItemCategory(namespaceId, item.getItemGroupId());
			for (PortalItemCategory category: categorys) {
				List<PortalContentScope> contentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM_CATEGORY.getCode(), category.getId());
				List<PortalLaunchPadMapping> mappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_ITEM_CATEGORY.getCode(), category.getId(), null);
				if(null != mappings && mappings.size() > 0){
					for (PortalLaunchPadMapping mapping: mappings) {
						launchPadProvider.deleteItemServiceCategryById(mapping.getLaunchPadContentId());
						portalLaunchPadMappingProvider.deletePortalLaunchPadMapping(mapping.getId());
					}
				}
				for (PortalContentScope scope: contentScopes) {
					ItemServiceCategry itemCategory = ConvertHelper.convert(category, ItemServiceCategry.class);
					if(PortalScopeType.RESIDENTIAL == PortalScopeType.fromCode(scope.getScopeType())){
						itemCategory.setSceneType(SceneType.DEFAULT.getCode());
						itemCategory.setScopeCode(ScopeType.RESIDENTIAL.getCode());
					}else if(PortalScopeType.COMMERCIAL == PortalScopeType.fromCode(scope.getScopeType())){
						itemCategory.setSceneType(SceneType.PARK_TOURIST.getCode());
						itemCategory.setScopeCode(ScopeType.COMMUNITY.getCode());
					}else if(PortalScopeType.PM == PortalScopeType.fromCode(scope.getScopeType())){
						itemCategory.setSceneType(SceneType.PM_ADMIN.getCode());
						itemCategory.setScopeCode(ScopeType.PM.getCode());
					}else if(PortalScopeType.ORGANIZATION == PortalScopeType.fromCode(scope.getScopeType())){
						itemCategory.setSceneType(SceneType.PARK_TOURIST.getCode());
						itemCategory.setScopeCode(ScopeType.ORGANIZATION.getCode());
					}
					itemCategory.setScopeId(scope.getScopeId());
					itemCategory.setStatus(ItemServiceCategryStatus.ACTIVE.getCode());
					itemCategory.setCreatorUid(user.getId());

					if(StringUtils.isEmpty(category.getIconUri()) && null != actionData){
						itemCategory.setIconUri(actionData.getDefUri());
					}

					if(AlignType.CENTER == AlignType.fromCode(category.getAlign()))
						itemCategory.setAlign(ItemServiceCategryAlign.CENTER.getCode());
					else if(AlignType.LEFT == AlignType.fromCode(category.getAlign()))
						itemCategory.setAlign(ItemServiceCategryAlign.LEFT.getCode());

					if(null == ItemServiceCategryAlign.fromCode(itemCategory.getAlign())  && null != actionData){
						if(AlignType.CENTER == AlignType.fromCode(actionData.getAlign()))
							itemCategory.setAlign(ItemServiceCategryAlign.CENTER.getCode());
						else if(AlignType.LEFT == AlignType.fromCode(actionData.getAlign()))
							itemCategory.setAlign(ItemServiceCategryAlign.LEFT.getCode());
					}
					itemCategory.setOrder(category.getDefaultOrder());
					itemCategory.setItemLocation(item.getItemLocation());
					itemCategory.setItemGroup(item.getGroupName());
					launchPadProvider.createItemServiceCategry(itemCategory);

					PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
					mapping.setContentType(EntityType.PORTAL_ITEM_CATEGORY.getCode());
					mapping.setPortalContentId(category.getId());
					mapping.setLaunchPadContentId(itemCategory.getId());
					mapping.setCreatorUid(user.getId());
					portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
				}
			}
		}
	}

	private Map<Long, String> getItemCategoryMap(Integer namespaceId){
		Map<Long, String> categoryMap = new HashMap<>();
		List<PortalItemCategory> categories = portalItemCategoryProvider.listPortalItemCategory(namespaceId, null);
		for (PortalItemCategory  category: categories) {
			categoryMap.put(category.getId(), category.getName());
		}
		return categoryMap;
	}


	@Override
	public void syncLaunchPadData(SyncLaunchPadDataCommand cmd){

		if(StringUtils.isEmpty(cmd.getLocation())){
			cmd.setLocation("/home");
		}

		if(StringUtils.isEmpty(cmd.getName())){
			cmd.setName("ServiceMarketLayout");
		}

		dbProvider.execute((status) -> {
			syncLayout(cmd.getNamespaceId(), cmd.getLocation(), cmd.getName());
			return null;
		});
	}


	private PortalLayout syncLayout(Integer namespaceId, String location, String name){
		User user = UserContext.current().getUser();
		List<LaunchPadLayout> padLayouts = launchPadProvider.getLaunchPadLayouts(name, namespaceId);
		PortalLayout layout = null;
		if(padLayouts.size() == 0){
			LOGGER.error("Unable to find the lunch pad layout. namespaceId = {}, location = {}, layoutName = {}", namespaceId, location, name);
			layout = new PortalLayout();
			layout.setId(0L);
		}

		for (LaunchPadLayout padLayout: padLayouts) {
			layout = portalLayoutProvider.getPortalLayout(padLayout.getNamespaceId(), name);
			if(null == layout){
				layout = ConvertHelper.convert(padLayout, PortalLayout.class);
				LaunchPadLayoutJson layoutJson = (LaunchPadLayoutJson)StringHelper.fromJsonString(padLayout.getLayoutJson(), LaunchPadLayoutJson.class);
				layout.setLabel(layoutJson.getDisplayName());
				layout.setLocation(location);
				layout.setOperatorUid(user.getId());
				layout.setCreatorUid(user.getId());
				portalLayoutProvider.createPortalLayout(layout);
				List<LaunchPadLayoutGroup> padLayoutGroups = layoutJson.getGroups();
				for (LaunchPadLayoutGroup padLayoutGroup: padLayoutGroups) {
					PortalItemGroup itemGroup = ConvertHelper.convert(padLayoutGroup, PortalItemGroup.class);
					itemGroup.setNamespaceId(layout.getNamespaceId());
					itemGroup.setLayoutId(layout.getId());
					itemGroup.setLabel(padLayoutGroup.getGroupName());
					itemGroup.setStatus(layout.getStatus());
					itemGroup.setCreatorUid(user.getId());
					itemGroup.setOperatorUid(user.getId());
					if(null != padLayoutGroup.getSeparatorFlag()){
						itemGroup.setSeparatorFlag(padLayoutGroup.getSeparatorFlag().byteValue());
					}

					if(null != padLayoutGroup.getSeparatorHeight()){
						itemGroup.setSeparatorHeight(new BigDecimal(padLayoutGroup.getSeparatorHeight()));
					}
					if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.NAVIGATOR){
						NavigatorInstanceConfig instanceConfig = (NavigatorInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), NavigatorInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						if(Style.fromCode(padLayoutGroup.getStyle()) == Style.GALLERY){
							if(StringUtils.isEmpty(padLayoutGroup.getTitle()) || StringUtils.isEmpty(padLayoutGroup.getIconUrl())){
								config.setTitleFlag(TitleFlag.TRUE.getCode());
								config.setTitle(padLayoutGroup.getTitle());
								config.setTitleUri(padLayoutGroup.getIconUrl());
							}
							config.setPadding(instanceConfig.getPaddingTop());
							config.setMargin(instanceConfig.getLineSpacing());
						}
						config.setColumnCount(padLayoutGroup.getColumnCount());
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
						if(name.equals("ServiceMarketLayout"))
							syncItemCategory(itemGroup.getNamespaceId(),itemGroup.getId());
						syncItem(itemGroup.getNamespaceId(), location, itemGroup.getName(), itemGroup.getId());
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.BANNERS){
						BannersInstanceConfig instanceConfig = (BannersInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), BannersInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.BULLETINS){
						BulletinsInstanceConfig instanceConfig = (BulletinsInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), BulletinsInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.OPPUSH){
						OPPushInstanceConfig instanceConfig = (OPPushInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), OPPushInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						List<LaunchPadItem> padItems = launchPadProvider.listLaunchPadItemsByItemGroup(padLayout.getNamespaceId(), "/home", instanceConfig.getItemGroup());
						if(padItems.size() > 0){
							config.setTitleFlag(TitleFlag.TRUE.getCode());
							config.setTitle(padItems.get(0).getItemLabel());

							Long moduleId = null;
							if(OPPushWidgetStyle.LIST_VIEW == OPPushWidgetStyle.fromCode(padLayoutGroup.getStyle())){
								moduleId = 10600L;
								itemGroup.setContentType(EntityType.ACTIVITY.getCode());
							}else if(OPPushWidgetStyle.LARGE_IMAGE_LIST_VIEW == OPPushWidgetStyle.fromCode(padLayoutGroup.getStyle())){
								moduleId = 40500L;
								itemGroup.setContentType(EntityType.SERVICE_ALLIANCE.getCode());
							}else{
								UrlActionData urlData = (UrlActionData)StringHelper.fromJsonString(padItems.get(0).getActionData(), UrlActionData.class);
								config.setBizUrl(urlData.getUrl());
								itemGroup.setContentType(EntityType.BIZ.getCode());
							}

							if(null != moduleId){
								LaunchPadItem padItem = padItems.get(0);
								ServiceModuleApp moduleApp = syncServiceModuleApp(itemGroup.getNamespaceId(), padItem.getActionData(), padItem.getActionType(), padItem.getItemLabel());
								config.setModuleAppId(moduleApp.getId());
							}
							itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						}

						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.TAB){
						TabInstanceConfig instanceConfig = (TabInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), TabInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						portalItemGroupProvider.createPortalItemGroup(itemGroup);

						syncItem(itemGroup.getNamespaceId(), location, itemGroup.getName(), itemGroup.getId());

					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.NEWS){
						Long moduleId = 10800L;
						ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(moduleId);
						NewsInstanceConfig instanceConfig = (NewsInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), NewsInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						ServiceModuleApp moduleApp = syncServiceModuleApp(itemGroup.getNamespaceId(), StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), serviceModule.getActionType(), itemGroup.getLabel());
						config.setModuleAppId(moduleApp.getId());
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}else if(Widget.fromCode(padLayoutGroup.getWidget()) == Widget.NEWS_FLASH){
						NewsFlashInstanceConfig instanceConfig = (NewsFlashInstanceConfig)StringHelper.fromJsonString(StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), NewsFlashInstanceConfig.class);
						itemGroup.setName(instanceConfig.getItemGroup());
						ItemGroupInstanceConfig config = ConvertHelper.convert(instanceConfig, ItemGroupInstanceConfig.class);
						Long moduleId = 10800L;
						ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(moduleId);
						ServiceModuleApp moduleApp = syncServiceModuleApp(itemGroup.getNamespaceId(), StringHelper.toJsonString(padLayoutGroup.getInstanceConfig()), serviceModule.getActionType(), itemGroup.getLabel());
						config.setModuleAppId(moduleApp.getId());
						itemGroup.setInstanceConfig(StringHelper.toJsonString(config));
						portalItemGroupProvider.createPortalItemGroup(itemGroup);
					}
				}
			}

			PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
			mapping.setLaunchPadContentId(padLayout.getId());
			mapping.setPortalContentId(layout.getId());
			mapping.setContentType(EntityType.PORTAL_LAYOUT.getCode());
			mapping.setCreatorUid(user.getId());
			portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
		}
		return layout;
	}

	private void syncItemCategory(Integer namespaceId, Long itemGroupId){
		User user = UserContext.current().getUser();
		List<ItemServiceCategry> categories = launchPadProvider.listItemServiceCategries(namespaceId);
		for (ItemServiceCategry category: categories) {
			PortalItemCategory portalItemCategory = portalItemCategoryProvider.getPortalItemCategoryByName(namespaceId, itemGroupId, category.getName());
			if(null == portalItemCategory){
				portalItemCategory = ConvertHelper.convert(category, PortalItemCategory.class);
				portalItemCategory.setItemGroupId(itemGroupId);
				portalItemCategory.setDefaultOrder(category.getOrder());
				portalItemCategory.setStatus(PortalItemCategoryStatus.ACTIVE.getCode());

				// 添加item 分类
				portalItemCategoryProvider.createPortalItemCategory(portalItemCategory);
			}
			syncContentScope(user, namespaceId, EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId(), ScopeType.ALL.getCode(), 0L, category.getSceneType());

			PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
			mapping.setLaunchPadContentId(category.getId());
			mapping.setPortalContentId(portalItemCategory.getId());
			mapping.setContentType(EntityType.PORTAL_ITEM_CATEGORY.getCode());
			mapping.setCreatorUid(user.getId());
			portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);
		}
	}


	private void syncItem(Integer namespaceId, String location, String itemGroupName, Long itemGroupId){
		User user = UserContext.current().getUser();
		List<LaunchPadItem> padItems = launchPadProvider.listLaunchPadItemsByItemGroup(namespaceId, location, itemGroupName);
		for (LaunchPadItem padItem: padItems) {
			if(ScopeType.USER == ScopeType.fromCode(padItem.getScopeCode())){
				continue;
			}
			PortalItem item = portalItemProvider.getPortalItemByGroupNameAndName(namespaceId, location, itemGroupName, padItem.getItemName(), itemGroupId);
			if(null == item){
				item = ConvertHelper.convert(padItem, PortalItem.class);
				item.setItemGroupId(itemGroupId);
				item.setLabel(padItem.getItemLabel());
				item.setName(padItem.getItemName());
				item.setGroupName(padItem.getItemGroup());
				item.setStatus(PortalItemStatus.ACTIVE.getCode());
				item.setCreatorUid(user.getId());
				item.setOperatorUid(user.getId());
				PortalItemCategory category = portalItemCategoryProvider.getPortalItemCategoryByName(namespaceId, itemGroupId, padItem.getCategryName());
				if(null != category){
					item.setItemCategoryId(category.getId());
				}
				if(ActionType.NONE == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.NONE.getCode());
				}else if(ActionType.MORE_BUTTON == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.ALLORMORE.getCode());
					AllOrMoreActionData actionData = new AllOrMoreActionData();
					actionData.setType(AllOrMoreType.MORE.getCode());
					item.setActionData(StringHelper.toJsonString(actionData));
				}else if(ActionType.ALL_BUTTON == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.ALLORMORE.getCode());
					AllOrMoreActionData actionData = new AllOrMoreActionData();
					actionData.setType(AllOrMoreType.ALL.getCode());
					item.setActionData(StringHelper.toJsonString(actionData));
				}else if(ActionType.OFFICIAL_URL == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.ZUOLINURL.getCode());
					if(!StringUtils.isEmpty(padItem.getActionData())){
						UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(padItem.getActionData(), UrlActionData.class);
						item.setActionData(StringHelper.toJsonString(actionData));
					}

				}else if(ActionType.THIRDPART_URL == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.THIRDURL.getCode());
					if(!StringUtils.isEmpty(padItem.getActionData())){
						UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(padItem.getActionData(), UrlActionData.class);
						item.setActionData(StringHelper.toJsonString(actionData));
					}
				}else if(ActionType.NAVIGATION  == ActionType.fromCode(padItem.getActionType())){
					item.setActionType(PortalItemActionType.LAYOUT.getCode());
					if(!StringUtils.isEmpty(padItem.getActionData())){
						NavigationActionData data = (NavigationActionData)StringHelper.fromJsonString(padItem.getActionData(), NavigationActionData.class);
						PortalLayout layout = syncLayout(item.getNamespaceId(), data.getItemLocation(), data.getLayoutName());
						LayoutActionData actionData = new LayoutActionData();
						actionData.setLayoutId(layout.getId());
						item.setActionData(StringHelper.toJsonString(actionData));
					}
				}else{
					item.setActionType(PortalItemActionType.MODULEAPP.getCode());
					ModuleAppActionData actionData = new ModuleAppActionData();
					ServiceModuleApp moduleApp= syncServiceModuleApp(padItem.getNamespaceId(), padItem.getActionData(), padItem.getActionType(), padItem.getItemLabel());
					actionData.setModuleAppId(moduleApp.getId());
					item.setActionData(StringHelper.toJsonString(actionData));
				}

				//添加item到数据库
				portalItemProvider.createPortalItem(item);
			}

			syncContentScope(user, namespaceId, EntityType.PORTAL_ITEM.getCode(), item.getId(), padItem.getScopeCode(), padItem.getScopeId(), padItem.getSceneType());

			PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
			mapping.setLaunchPadContentId(padItem.getId());
			mapping.setPortalContentId(item.getId());
			mapping.setContentType(EntityType.PORTAL_ITEM.getCode());
			mapping.setCreatorUid(user.getId());
			portalLaunchPadMappingProvider.createPortalLaunchPadMapping(mapping);

		}
	}

	private ServiceModuleApp syncServiceModuleApp(Integer namespaceId, String actionData, Byte actionType, String itemLabel){
		User user = UserContext.current().getUser();
		ServiceModuleApp moduleApp = new ServiceModuleApp();
		moduleApp.setInstanceConfig(actionData);
		moduleApp.setActionType(actionType);
		moduleApp.setName(itemLabel);
		moduleApp.setNamespaceId(namespaceId);
		moduleApp.setStatus(ServiceModuleAppStatus.ACTIVE.getCode());
		moduleApp.setCreatorUid(user.getId());
		moduleApp.setOperatorUid(user.getId());
		List<ServiceModule> serviceModules = serviceModuleProvider.listServiceModule(actionType);
		if(serviceModules.size() == 0 || ActionType.OFFLINE_WEBAPP  == ActionType.fromCode(actionType)
				|| ActionType.ROUTER  == ActionType.fromCode(actionType)){

		}else{
			ServiceModule serviceModule = serviceModules.get(0);
			moduleApp.setModuleId(serviceModule.getId());
			if(StringUtils.isEmpty(itemLabel)){
				moduleApp.setName(serviceModule.getName());
			}
			if(MultipleFlag.fromCode(serviceModule.getMultipleFlag()) == MultipleFlag.YES){
				PortalPublishHandler handler = getPortalPublishHandler(moduleApp.getModuleId());
				if(null != handler){
					String instanceConfig = handler.getAppInstanceConfig(namespaceId, actionData);
					moduleApp.setInstanceConfig(instanceConfig);
				}
			}
		}
		serviceModuleAppProvider.createServiceModuleApp(moduleApp);
		return moduleApp;
	}

	private PortalContentScope syncContentScope(User user, Integer namespaceId, String contentType, Long contentId, Byte scopeType, Long scopeId, String sceneType){
		PortalContentScope scope = new PortalContentScope();
		scope.setContentType(contentType);
		scope.setContentId(contentId);
		scope.setNamespaceId(namespaceId);
		scope.setCreatorUid(user.getId());
		scope.setOperatorUid(user.getId());
		if(ScopeType.ALL == ScopeType.fromCode(scopeType)){
			if(SceneType.PM_ADMIN == SceneType.fromCode(sceneType)){
				scope.setScopeType(PortalScopeType.PM.getCode());
				scope.setScopeId(0L);
			}else if(SceneType.PARK_TOURIST == SceneType.fromCode(sceneType)){
				scope.setScopeType(PortalScopeType.COMMERCIAL.getCode());
				scope.setScopeId(0L);
			}else if(SceneType.DEFAULT == SceneType.fromCode(sceneType)){
				scope.setScopeType(PortalScopeType.RESIDENTIAL.getCode());
				scope.setScopeId(0L);
			}else{
				LOGGER.debug("data error. sceneType = " + sceneType);
				return null;
			}

		}else if(ScopeType.COMMUNITY == ScopeType.fromCode(scopeType)){
			Community community = communityProvider.findCommunityById(scopeId);
			scope.setScopeType(PortalScopeType.COMMERCIAL.getCode());
			if(null != community){
				if(CommunityType.RESIDENTIAL == CommunityType.fromCode(community.getCommunityType())){
					scope.setScopeType(PortalScopeType.RESIDENTIAL.getCode());
				}
			}
			scope.setScopeId(scopeId);
		}else if(ScopeType.ORGANIZATION == ScopeType.fromCode(scopeType)){
			Organization organization = organizationProvider.findOrganizationById(scopeId);
			scope.setScopeType(PortalScopeType.ORGANIZATION.getCode());
			if(null != organization){
				if(OrganizationType.PM == OrganizationType.fromCode(organization.getOrganizationType())){
					scope.setScopeType(PortalScopeType.PM.getCode());
				}
			}
			scope.setScopeId(scopeId);
		}else{
			LOGGER.debug("data error. scopeType = " + scopeType);
			return null;
		}

		//添加item的scope到数据库
		portalContentScopeProvider.createPortalContentScope(scope);
		return scope;
	}

	private PortalPublishHandler getPortalPublishHandler(Long moduleId) {
		PortalPublishHandler handler = null;

		if(moduleId != null && moduleId.longValue() > 0) {
			String handlerPrefix = PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX;
			handler = PlatformContext.getComponent(handlerPrefix + moduleId);
		}

		return handler;
	}



	public static void main(String[] args) {
//		PortalItemGroupJson[] jsons = (PortalItemGroupJson[])StringHelper.fromJsonString("[{\"label\":\"应用\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Navigator\",\"style\":\"Metro\",\"instanceConfig\":{\"margin\":20,\"padding\":16,\"backgroundColor\":\"#ffffff\",\"titleFlag\":0,\"title\":\"标题\",\"titleUri\":\"cs://\"},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"横幅广告\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Banners\",\"style\":\"Default\",\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"公告\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Bulletins\",\"style\":\"Default\",\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"运营模块\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"OPPush\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"titleFlag\":0,\"title\":\"标题\",\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"无时间轴\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"News_Flash\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"时间轴\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"News\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"timeWidgetStyle\":\"date\",\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"分页签\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Tabs\",\"style\":\"Pure_text\",\"defaultOrder\":0,\"description\":\"描述\"}]", PortalItemGroupJson[].class);
//		for (PortalItemGroupJson json: jsons) {
		Byte b = 1;
		System.out.println("int：" +b.intValue());
//		}
		BigDecimal bd = new BigDecimal(1);
		System.out.println("Double：" +bd.doubleValue());

	}
}