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
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.launchpad.ItemDisplayFlag;
import com.everhomes.rest.portal.*;
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
		return new ListPortalItemGroupsResponse(portalItemGroups.stream().map(r ->{
			return processPortalItemGroupDTO(r);
		}).collect(Collectors.toList()));
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
					return o1.getDefaultOrder() - o2.getDefaultOrder();
				}
			});
		}

		response.setPortalItems(dtos);

		return response;
	}

	@Override
	public PortalItemDTO createPortalItem(CreatePortalItemCommand cmd) {
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getItemGroupId());
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
		portalItem.setItemLocation("/" + portalItem.getGroupName());
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
		List<PortalItemCategory> portalItemCategorys = portalItemCategoryProvider.listPortalItemCategory(namespaceId);
		List<PortalItemCategoryDTO> portalItemCategories = portalItemCategorys.stream().map(r ->{
			PortalItemCategoryDTO dto = processPortalItemCategoryDTO(r);
			List<PortalItem> portalItems = portalItemProvider.listPortalItem(r.getId());
			dto.setItems(portalItems.stream().map(i ->{
				return processPortalItemDTO(i);
			}).collect(Collectors.toList()));

			List<PortalContentScope> portalContentScopes = portalContentScopeProvider.listPortalContentScope(EntityType.PORTAL_ITEM_CATEGORY.getCode(), r.getId());
			dto.setScopes(processListPortalContentScopeDTO(portalContentScopes));
			return dto;
		}).collect(Collectors.toList());

		return new ListPortalItemCategoriesResponse(portalItemCategories);
	}

	@Override
	public PortalItemCategoryDTO createPortalItemCategory(CreatePortalItemCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		PortalItemCategory portalItemCategory = ConvertHelper.convert(cmd, PortalItemCategory.class);
		portalItemCategory.setStatus(PortalItemCategoryStatus.ACTIVE.getCode());
		portalItemCategory.setOperatorUid(user.getId());
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
		return dto;
	}

	private PortalItem getItemAllOrMore(Integer namespaceId, AllOrMoreType type){
		List<PortalItem> portalItems = portalItemProvider.listPortalItem(null, namespaceId, PortalItemActionType.ALLORMORE.getCode());
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
		portalItem.setActionData(cmd.getInstanceConfig());
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
				if(null != portalItemCategoryRank.getItemIds() && portalItemCategoryRank.getItemIds().size() != 0){
					for (Long itemId: portalItemCategoryRank.getItemIds()) {
						PortalItem portalItem = checkPortalItem(itemId);
						portalItem.setOperatorUid(user.getId());
						portalItem.setItemCategoryId(portalItemCategory.getId());
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
	

	}


	public static void main(String[] args) {
//		PortalItemGroupJson[] jsons = (PortalItemGroupJson[])StringHelper.fromJsonString("[{\"label\":\"应用\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Navigator\",\"style\":\"Metro\",\"instanceConfig\":{\"margin\":20,\"padding\":16,\"backgroundColor\":\"#ffffff\",\"titleFlag\":0,\"title\":\"标题\",\"titleUri\":\"cs://\"},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"横幅广告\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Banners\",\"style\":\"Default\",\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"公告\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Bulletins\",\"style\":\"Default\",\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"运营模块\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"OPPush\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"titleFlag\":0,\"title\":\"标题\",\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"无时间轴\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"News_Flash\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"时间轴\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"News\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"timeWidgetStyle\":\"date\",\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"分页签\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Tabs\",\"style\":\"Pure_text\",\"defaultOrder\":0,\"description\":\"描述\"}]", PortalItemGroupJson[].class);
//		for (PortalItemGroupJson json: jsons) {
			System.out.println(GeoHashUtils.encode(113.952532, 22.550182));
//		}
	}
}