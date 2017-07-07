// @formatter:off
package com.everhomes.portal;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.launchpad.ItemDisplayFlag;
import com.everhomes.rest.portal.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
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

	@Override
	public ListServiceModuleAppsResponse listServiceModuleApps(ListServiceModuleAppsCommand cmd) {
		return new ListServiceModuleAppsResponse();
	}

	@Override
	public void createServiceModuleApp(CreateServiceModuleAppCommand cmd) {
	

	}

	@Override
	public void updateServiceModuleApp(UpdateServiceModuleAppCommand cmd) {
	

	}


	@Override
	public void deleteServiceModuleApp(DeleteServiceModuleAppCommand cmd) {
	

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
	
		return new ListPortalItemsResponse();
	}

	@Override
	public PortalItemDTO createPortalItem(CreatePortalItemCommand cmd) {
		PortalItemGroup portalItemGroup = checkPortalItemGroup(cmd.getItemGroupId());
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId(portalItemGroup.getNamespaceId());
		PortalItem portalItem = ConvertHelper.convert(cmd, PortalItem.class);
		portalItem.setNamespaceId(namespaceId);
		portalItem.setStatus(PortalItemGroupStatus.ACTIVE.getCode());
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
		portalItem.setOperatorUid(user.getId());
		return processPortalItemDTO(portalItem);
	}

	@Override
	public void deletePortalItem(DeletePortalItemCommand cmd) {
	

	}

	private PortalItemDTO processPortalItemDTO(PortalItem portalItem){
		return ConvertHelper.convert(portalItem, PortalItemDTO.class);
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


	@Override
	public void reorderPortalItem(ReorderPortalItemCommand cmd) {
	

	}

	@Override
	public void reorderPortalItemGroup(ReorderPortalItemGroupCommand cmd) {
	

	}

	@Override
	public GetPortalItemByIdResponse getPortalItemById(GetPortalItemByIdCommand cmd) {
	
		return new GetPortalItemByIdResponse();
	}

	@Override
	public ListPortalItemCategoriesResponse listPortalItemCategories(ListPortalItemCategoriesCommand cmd) {
	
		return new ListPortalItemCategoriesResponse();
	}

	@Override
	public void createPortalItemCategory(CreatePortalItemCategoryCommand cmd) {
	

	}

	@Override
	public void updatePortalItemCategory(UpdatePortalItemCategoryCommand cmd) {
	

	}

	@Override
	public void deletePortalItemCategory(DeletePortalItemCategoryCommand cmd) {
	

	}

	@Override
	public void reorderPortalItemCategory(ReorderPortalItemCategoryCommand cmd) {
	

	}

	@Override
	public void rankPortalItemCategory(RankPortalItemCategoryCommand cmd) {
	

	}

	@Override
	public GetPortalItemGroupByIdResponse getPortalItemGroupById(GetPortalItemGroupByIdCommand cmd) {
	
		return new GetPortalItemGroupByIdResponse();
	}

	@Override
	public ListPortalNavigationBarsResponse listPortalNavigationBars(ListPortalNavigationBarsCommand cmd) {
	
		return new ListPortalNavigationBarsResponse();
	}

	@Override
	public void createPortalNavigationBar(CreatePortalNavigationBarCommand cmd) {
	

	}

	@Override
	public void updatePortalNavigationBar(UpdatePortalNavigationBarCommand cmd) {
	

	}

	@Override
	public void deletePortalNavigationBar(DeletePortalNavigationBarCommand cmd) {
	

	}

	@Override
	public void publish(PublishCommand cmd) {
	

	}


	public static void main(String[] args) {
		PortalItemGroupJson[] jsons = (PortalItemGroupJson[])StringHelper.fromJsonString("[{\"label\":\"应用\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Navigator\",\"style\":\"Metro\",\"instanceConfig\":{\"margin\":20,\"padding\":16,\"backgroundColor\":\"#ffffff\",\"titleFlag\":0,\"title\":\"标题\",\"titleUri\":\"cs://\"},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"横幅广告\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Banners\",\"style\":\"Default\",\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"公告\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Bulletins\",\"style\":\"Default\",\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"运营模块\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"OPPush\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"titleFlag\":0,\"title\":\"标题\",\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"无时间轴\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"News_Flash\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"时间轴\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"News\",\"style\":\"Default\",\"instanceConfig\":{\"newsSize\":20,\"timeWidgetStyle\":\"date\",\"moduleAppId\":1},\"defaultOrder\":0,\"description\":\"描述\"},{\"label\":\"分页签\", \"separatorFlag\":\"1\", \"separatorHeight\":\"12\",\"widget\":\"Tabs\",\"style\":\"Pure_text\",\"defaultOrder\":0,\"description\":\"描述\"}]", PortalItemGroupJson[].class);
		for (PortalItemGroupJson json: jsons) {
			System.out.println(StringHelper.toJsonString(json.getInstanceConfig()));
		}
	}
}