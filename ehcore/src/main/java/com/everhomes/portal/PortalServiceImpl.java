// @formatter:off
package com.everhomes.portal;

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
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.MoreActionData;
import com.everhomes.rest.common.NavigationActionData;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.portal.*;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.widget.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

	@Override
	public ListServiceModuleAppsResponse listServiceModuleApps(ListServiceModuleAppsCommand cmd) {
		List<ServiceModuleApp> moduleApps = serviceModuleAppProvider.listServiceModuleApp(cmd.getNamespaceId());
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
	public ServiceModuleAppDTO updateServiceModuleApp(UpdateServiceModuleAppCommand cmd) {
		ServiceModuleApp moduleApp = checkServiceModuleApp(cmd.getId());
		moduleApp.setOperatorUid(UserContext.current().getUser().getId());
		moduleApp.setName(cmd.getName());
		moduleApp.setModuleId(cmd.getModuleId());
		if(null != cmd.getModuleId()){
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
		List<PortalLayout> portalLayouts = portalLayoutProvider.listPortalLayout(namespaceId);
		return new ListPortalLayoutsResponse(portalLayouts.stream().map(r ->{
			return processPortalLayoutDTO(r);
		}).collect(Collectors.toList()));
	}

	private ServiceModuleAppDTO processServiceModuleAppDTO(ServiceModuleApp moduleApp){
		ServiceModuleAppDTO dto = ConvertHelper.convert(moduleApp, ServiceModuleAppDTO.class);
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
			portalLayoutProvider.createPortalLayout(portalLayout);
			if(null != cmd.getLayoutTemplateId()){
				PortalLayoutTemplate template = portalLayoutTemplateProvider.findPortalLayoutTemplateById(cmd.getLayoutTemplateId());
				if(null != template && !StringUtils.isEmpty(template.getTemplateJson())){
					List<PortalItemGroup> groups = new ArrayList<>();
					PortalItemGroupJson[] jsonObjs = (PortalItemGroupJson[])StringHelper.fromJsonString(template.getTemplateJson(), PortalItemGroupJson[].class);
					for (PortalItemGroupJson jsonObj: jsonObjs) {
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
		return dto;
	}

	@Override
	public ListPortalItemsResponse listPortalItems(ListPortalItemsCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<PortalItem> portalItems = portalItemProvider.listPortalItem(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_PORTAL_ITEMS.ITEM_GROUP_ID.eq(cmd.getItemGroupId()));
				if(null != PortalScopeType.fromCode(cmd.getScopeType())){
					query.addConditions(Tables.EH_PORTAL_CONTENT_SCOPES.SCOPE_TYPE.eq(cmd.getScopeType()));
				}
				return query;
			}
		});
		ListPortalItemsResponse response = new ListPortalItemsResponse();
		response.setNextPageAnchor(locator.getAnchor());
		List<PortalItemDTO> dtos = portalItems.stream().map(r ->{
			return processPortalItemDTO(r);
		}).collect(Collectors.toList());

		if(dtos.size() > 0){
			//按order 排序
			Collections.sort(dtos, new Comparator<PortalItemDTO>() {
				@Override
				public int compare(PortalItemDTO o1, PortalItemDTO o2) {
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
		}

		response.setPortalItems(dtos);

		return response;
	}

	@Override
	public PortalItemDTO createPortalItem(CreatePortalItemCommand cmd) {
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getItemGroupId());
		PortalLayout portalLayout = checkPortalLayout(portalItemGroup.getLayoutId());
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(portalItemGroup.getNamespaceId());
		PortalItem portalItem = ConvertHelper.convert(cmd, PortalItem.class);
		portalItem.setNamespaceId(namespaceId);
		if(null == PortalItemGroupStatus.fromCode(cmd.getStatus())){
			portalItem.setStatus(PortalItemGroupStatus.ACTIVE.getCode());
		}
		portalItem.setCreatorUid(user.getId());
		portalItem.setOperatorUid(user.getId());
		portalItem.setDisplayFlag(ItemDisplayFlag.DISPLAY.getCode());

		portalItem.setGroupName(portalItemGroup.getName());
		portalItem.setItemLocation(getItemLocation(portalLayout.getName()));
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
		if(null == PortalItemGroupStatus.fromCode(cmd.getStatus())){
			portalItem.setStatus(PortalItemGroupStatus.ACTIVE.getCode());
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

		if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ALLORMORE){
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
			if(AllOrMoreType.ALL == AllOrMoreType.fromCode(actionData.getType())){
				dto.setContentName(configurationProvider.getValue(ConfigConstants.PORTAL_ITEM_ALL_TITLE, "全部"));
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
			dto.setContentName(actionData.getUrl());
		}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.ZUOLINURL){
			UrlActionData actionData = (UrlActionData)StringHelper.fromJsonString(portalItem.getActionData(), UrlActionData.class);
			dto.setContentName(actionData.getUrl());
		}

		List<PortalContentScope> portalContentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
		dto.setScopes(processListPortalContentScopeDTO(portalContentScopes));

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
	public ListPortalItemCategoriesResponse listPortalItemCategories(ListPortalItemCategoriesCommand cmd) {
		Integer namespaceId= UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<PortalItemCategory> portalItemCategories = portalItemCategoryProvider.listPortalItemCategory(namespaceId);
		List<PortalItemCategoryDTO> dtos = portalItemCategories.stream().map(r ->{
			PortalItemCategoryDTO dto = processPortalItemCategoryDTO(r);
			List<PortalItem> portalItems = portalItemProvider.listPortalItemByCategoryId(r.getId());
			dto.setItems(portalItems.stream().map(i ->{
				return processPortalItemDTO(i);
			}).collect(Collectors.toList()));
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
		PortalItemCategory portalItemCategory = ConvertHelper.convert(cmd, PortalItemCategory.class);
		portalItemCategory.setStatus(PortalItemCategoryStatus.ACTIVE.getCode());
		portalItemCategory.setOperatorUid(user.getId());
		portalItemCategory.setCreatorUid(user.getId());
		portalItemCategory.setNamespaceId(namespaceId);
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
		portalItemCategory.setName(cmd.getName());
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
		PortalItemCategory portalItemCategory = checkPortalItemCategory(cmd.getId());
		portalItemCategory.setOperatorUid(UserContext.current().getUser().getId());
		portalItemCategory.setStatus(PortalItemCategoryStatus.INACTIVE.getCode());
		this.dbProvider.execute((status) -> {
			portalItemCategoryProvider.updatePortalItemCategory(portalItemCategory);
			portalContentScopeProvider.deletePortalContentScopes(EntityType.PORTAL_ITEM_CATEGORY.getCode(), portalItemCategory.getId());
			return null;
		});

	}

	private PortalItemCategoryDTO processPortalItemCategoryDTO(PortalItemCategory portalItemCategory){
		PortalItemCategoryDTO dto = ConvertHelper.convert(portalItemCategory, PortalItemCategoryDTO.class);
		PortalItem portalItem = getItemAllOrMore(portalItemCategory.getNamespaceId(), AllOrMoreType.ALL);
		if(null != portalItem){
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
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

	private PortalItem getItemAllOrMore(Integer namespaceId, AllOrMoreType type){
		List<PortalItem> portalItems = portalItemProvider.listPortalItem(null, namespaceId, PortalItemActionType.ALLORMORE.getCode(), null);
		for (PortalItem portalItem: portalItems) {
			AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
			if(null != actionData && AllOrMoreType.fromCode(actionData.getType()) == type){
				return portalItem;
			}
		}
		return null;
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
		AllOrMoreActionData actionData = (AllOrMoreActionData)StringHelper.fromJsonString(portalItem.getActionData(), AllOrMoreActionData.class);
		actionData.setAlign(cmd.getAlign());
		actionData.setDefUri(cmd.getDefUri());
		portalItem.setActionData(StringHelper.toJsonString(actionData));
		portalItemProvider.updatePortalItem(portalItem);
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
				PortalItemCategory portalItemCategory = portalItemCategoryProvider.findPortalItemCategoryById(portalItemCategoryRank.getItemCategoryId());
				if(null != portalItemCategoryRank.getItems() && portalItemCategoryRank.getItems().size() > 0){
					for (PortalItemReorder item: portalItemCategoryRank.getItems()) {
						PortalItem portalItem = checkPortalItem(item.getItemId());
						portalItem.setOperatorUid(user.getId());
						portalItem.setItemCategoryId(portalItemCategory.getId());
						if(null != ItemDisplayFlag.fromCode(item.getDisplayFlag()))
							portalItem.setDisplayFlag(item.getDisplayFlag());
						portalItem.setMoreOrder(item.getMoreOrder());
						if(null != portalItem.getDefaultOrder())
							portalItem.setDefaultOrder(item.getDefaultOrder());
						portalItemProvider.updatePortalItem(portalItem);
					}
				}
			}
			return null;
		});

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

		return dto;
	}

	@Override
	public void publish(PublishCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<PortalLayout> layouts = portalLayoutProvider.listPortalLayout(cmd.getNamespaceId());

		this.dbProvider.execute((status) -> {
			//发布item分类
			publishItemCategory(namespaceId);

			for (PortalLayout layout: layouts) {
				//发布layout
				publishLayout(layout);
			}
			return null;
		});
	}

	private void publishLayout(PortalLayout layout){

		User user = UserContext.current().getUser();

		List<PortalLaunchPadMapping> portalLaunchPadMappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_LAYOUT.getCode(), layout.getId(), null);

		Long versionCode = 0L;

		LaunchPadLayoutJson layoutJson = new LaunchPadLayoutJson();
		layoutJson.setVersionCode(versionCode.toString());
		layoutJson.setDisplayName(layout.getLabel());
		layoutJson.setLayoutName(layout.getName());
		List<LaunchPadLayoutGroupDTO> groups = new ArrayList<>();
		List<PortalItemGroup> itemGroups = portalItemGroupProvider.listPortalItemGroup(layout.getId());
		for (PortalItemGroup itemGroup: itemGroups) {
			LaunchPadLayoutGroupDTO group = ConvertHelper.convert(itemGroup, LaunchPadLayoutGroupDTO.class);
			group.setGroupName(itemGroup.getLabel());
			ItemGroupInstanceConfig instanceConfig = (ItemGroupInstanceConfig)StringHelper.fromJsonString(itemGroup.getInstanceConfig(), ItemGroupInstanceConfig.class);
			group.setColumnCount(instanceConfig.getColumnCount());
			if(TitleFlag.TRUE == TitleFlag.fromCode(instanceConfig.getTitleFlag())){
				group.setTitle(instanceConfig.getTitle());
				if(!StringUtils.isEmpty(instanceConfig.getTitleUri())){
					String url = contentServerService.parserUri(instanceConfig.getTitleUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
					group.setIconUrl(url);
				}
			}

			if(Widget.fromCode(group.getWidget()) == Widget.NAVIGATOR){
				NavigatorInstanceConfig config = new NavigatorInstanceConfig();
				config.setBackgroundColor(instanceConfig.getBackgroundColor());
				if(Style.fromCode(group.getStyle()) == Style.GALLERY){
					config.setCssStyleFlag(CssStyleFlagType.YES.getCode());
				}
				config.setPaddingBottom(instanceConfig.getPadding());
				config.setPaddingLeft(instanceConfig.getPadding());
				config.setPaddingRight(instanceConfig.getPadding());
				config.setPaddingTop(instanceConfig.getPadding());
				config.setColumnSpacing(instanceConfig.getMargin());
				config.setLineSpacing(instanceConfig.getMargin());
				config.setItemGroup(itemGroup.getName());
				group.setInstanceConfig(StringHelper.toJsonString(config));
			}else if(Widget.fromCode(group.getWidget()) == Widget.BANNERS){
				BannersInstanceConfig config = new BannersInstanceConfig();
				config.setItemGroup(itemGroup.getName());
				group.setInstanceConfig(StringHelper.toJsonString(config));
			}else if(Widget.fromCode(group.getWidget()) == Widget.NEWS){
				ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(instanceConfig.getModuleAppId());
				if(null != moduleApp){
					NewsInstanceConfig config = (NewsInstanceConfig)StringHelper.fromJsonString(moduleApp.getInstanceConfig(), NewsInstanceConfig.class);
					config.setItemGroup(itemGroup.getName());
					config.setTimeWidgetStyle(instanceConfig.getTimeWidgetStyle());
					group.setInstanceConfig(StringHelper.toJsonString(config));
				}
			}else if(Widget.fromCode(group.getWidget()) == Widget.NEWS_FLASH){
				ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(instanceConfig.getModuleAppId());
				if(null != moduleApp){
					NewsFlashInstanceConfig config = (NewsFlashInstanceConfig)StringHelper.fromJsonString(moduleApp.getInstanceConfig(), NewsFlashInstanceConfig.class);
					config.setItemGroup(itemGroup.getName());
					config.setTimeWidgetStyle(instanceConfig.getTimeWidgetStyle());
					config.setNewsSize(instanceConfig.getNewsSize());
					group.setInstanceConfig(StringHelper.toJsonString(config));
				}
			}else if(Widget.fromCode(group.getWidget()) == Widget.BULLETINS){
				BulletinsInstanceConfig config = new BulletinsInstanceConfig();
				config.setItemGroup(itemGroup.getName());
				config.setRowCount(instanceConfig.getRowCount());
				group.setInstanceConfig(StringHelper.toJsonString(config));
			}else if(Widget.fromCode(group.getWidget()) == Widget.OPPUSH){
				OPPushInstanceConfig config = new OPPushInstanceConfig();
				config.setItemGroup(itemGroup.getName());
				config.setNewsSize(instanceConfig.getNewsSize());
//				config.setDescriptionHeight();
//				config.setSubjectHeight();
				//应用入口的应用不明
				ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(instanceConfig.getModuleAppId());
				if(null != moduleApp){

				}
				group.setInstanceConfig(StringHelper.toJsonString(config));
			}else if(Widget.fromCode(group.getWidget()) == Widget.TAB){
				TabInstanceConfig config = new TabInstanceConfig();
				config.setItemGroup(itemGroup.getName());
				group.setInstanceConfig(StringHelper.toJsonString(config));
			}
			groups.add(group);

			if(Widget.fromCode(group.getWidget()) == Widget.NAVIGATOR){
				// 发布item
				publishItem(itemGroup);
			}
		}
		layoutJson.setGroups(groups);
		String json = StringHelper.toJsonString(layoutJson);
		LaunchPadLayout launchPadLayout = new LaunchPadLayout();
		if(portalLaunchPadMappings.size() > 0){
			for (PortalLaunchPadMapping mapping: portalLaunchPadMappings) {
				launchPadLayout = launchPadProvider.findLaunchPadLayoutById(mapping.getLaunchPadContentId());
				launchPadLayout.setVersionCode(versionCode);
				launchPadLayout.setLayoutJson(json);
				launchPadProvider.updateLaunchPadLayout(launchPadLayout);
			}
		}else{
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

	public void publishItem(PortalItemGroup itemGroup){
		User user = UserContext.current().getUser();
		List<PortalItem> portalItems = portalItemProvider.listPortalItemByGroupId(itemGroup.getId());
		Map<Long, Long> categoryIdMap = getItemCategoryMap(itemGroup.getNamespaceId());
		for (PortalItem portalItem: portalItems) {
			if(PortalItemStatus.ACTIVE == PortalItemStatus.fromCode(portalItem.getStatus())){

				List<PortalLaunchPadMapping> mappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_ITEM.getCode(), portalItem.getId(), null);

				if(null != mappings && mappings.size() > 0){
					for (PortalLaunchPadMapping mapping: mappings) {
						launchPadProvider.deleteLaunchPadItem(mapping.getLaunchPadContentId());
						portalLaunchPadMappingProvider.deletePortalLaunchPadMapping(mapping.getId());
					}
				}
				List<PortalContentScope> contentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM.getCode(), portalItem.getId());
				for (PortalContentScope scope: contentScopes) {
					LaunchPadItem item = ConvertHelper.convert(portalItem, LaunchPadItem.class);
					if(PortalScopeType.RESIDENTIAL == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.RESIDENTIAL.getCode());
						item.setScopeId(scope.getScopeId());
						item.setSceneType(SceneType.DEFAULT.getCode());
					}else if(PortalScopeType.COMMERCIAL == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.COMMUNITY.getCode());
						item.setScopeId(scope.getScopeId());
						item.setSceneType(SceneType.PARK_TOURIST.getCode());
					}else if(PortalScopeType.PM == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.PM.getCode());
						item.setScopeId(scope.getScopeId());
						item.setSceneType(SceneType.PM_ADMIN.getCode());
					}else if(PortalScopeType.ORGANIZATION == PortalScopeType.fromCode(scope.getScopeType())){
						item.setScopeCode(ScopeType.ORGANIZATION.getCode());
						item.setScopeId(scope.getScopeId());
						item.setSceneType(SceneType.PARK_TOURIST.getCode());
					}

					if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.LAYOUT){
						item.setActionType(ActionType.NAVIGATION.getCode());
						LayoutActionData data = (LayoutActionData)StringHelper.fromJsonString(portalItem.getActionData(), LayoutActionData.class);
						PortalLayout layout = portalLayoutProvider.findPortalLayoutById(data.getLayoutId());
						if(null != layout){
							NavigationActionData actionData = new NavigationActionData();
							actionData.setItemLocation(getItemLocation(layout.getName()));
							actionData.setLayoutName(layout.getName());
							actionData.setTitle(layout.getLabel());
							item.setActionData(StringHelper.toJsonString(actionData));
						}else{
							LOGGER.error("Unable to find the portal layout.id = {}, actionData = {}", data.getLayoutId(), portalItem.getActionData());
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
									"Unable to find the portal layout.");
						}

					}else if(PortalItemActionType.fromCode(portalItem.getActionType()) == PortalItemActionType.MODULEAPP){
						ModuleAppActionData data = (ModuleAppActionData)StringHelper.fromJsonString(portalItem.getActionData(), ModuleAppActionData.class);
						ServiceModuleApp moduleApp = serviceModuleAppProvider.findServiceModuleAppById(data.getModuleAppId());
						if(null != moduleApp){
							item.setActionType(moduleApp.getActionType());
							item.setActionData(moduleApp.getInstanceConfig());
						}
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
					}

					item.setAppId(AppConstants.APPID_DEFAULT);
					item.setApplyPolicy(ApplyPolicy.OVERRIDE.getCode());
					item.setMinVersion(1L);
					item.setItemGroup(portalItem.getGroupName());
					item.setItemLabel(portalItem.getLabel());
					item.setItemName(portalItem.getName());
					item.setDeleteFlag(DeleteFlagType.YES.getCode());
					item.setScaleType(ScaleType.TAILOR.getCode());
					item.setServiceCategryId(categoryIdMap.get(portalItem.getItemCategoryId()));
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


	public void publishItemCategory(Integer namespaceId){
		User user = UserContext.current().getUser();
		PortalItem allItem = getItemAllOrMore(namespaceId, AllOrMoreType.ALL);
		AllOrMoreActionData actionData = null;
		if(null != allItem)actionData = (AllOrMoreActionData)StringHelper.fromJsonString(allItem.getActionData(), AllOrMoreActionData.class);
		List<PortalItemCategory> categorys = portalItemCategoryProvider.listPortalItemCategory(namespaceId);
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
				}else if(PortalScopeType.COMMERCIAL == PortalScopeType.fromCode(scope.getScopeType())){
					itemCategory.setSceneType(SceneType.PARK_TOURIST.getCode());
				}else if(PortalScopeType.PM == PortalScopeType.fromCode(scope.getScopeType())){
					itemCategory.setSceneType(SceneType.PM_ADMIN.getCode());
				}else if(PortalScopeType.ORGANIZATION == PortalScopeType.fromCode(scope.getScopeType())){
					itemCategory.setSceneType(SceneType.PARK_TOURIST.getCode());
				}
				itemCategory.setScopeType(scope.getScopeType());
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

	private Map<Long, Long> getItemCategoryMap(Integer namespaceId){
		Map<Long, Long> categoryMap = new HashMap<>();
		List<PortalLaunchPadMapping> mappings = portalLaunchPadMappingProvider.listPortalLaunchPadMapping(EntityType.PORTAL_ITEM_CATEGORY.getCode(), null, null);
		for (PortalLaunchPadMapping  mapping: mappings) {
			categoryMap.put(mapping.getPortalContentId(), mapping.getLaunchPadContentId());
		}
		return categoryMap;
	}

	private String getItemLocation(String layoutName){
		return "/" + layoutName;
	}

	public static void main(String[] args) {
//		PortalItemGroupJson[] jsons = (PortalItemGroupJson[])StringHelper.fromJsonString("[{\"label\":\"应用\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Navigator\",\"style\":\"Metro\",\"instanceConfig\":{\"margin\":20,\"padding\":16,\"backgroundColor\":\"#ffffff\",\"titleFlag\":0,\"title\":\"标题\",\"titleUri\":\"cs://\"},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"横幅广告\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Banners\",\"style\":\"Default\",\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"公告\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Bulletins\",\"style\":\"Default\",\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"运营模块\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"OPPush\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"titleFlag\":0,\"title\":\"标题\",\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"无时间轴\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"News_Flash\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"时间轴\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"News\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"timeWidgetStyle\":\"date\",\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"分页签\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Tabs\",\"style\":\"Pure_text\",\"defaultOrder\":0,\"description\":\"描述\"}]", PortalItemGroupJson[].class);
//		for (PortalItemGroupJson json: jsons) {
			System.out.println(GeoHashUtils.encode(113.952532, 22.550182));
//		}
	}
}