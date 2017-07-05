// @formatter:off
package com.everhomes.portal;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
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

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PortalServiceImpl implements PortalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortalServiceImpl.class);


	@Autowired
	private PortalLayoutProvider portalLayoutProvider;

	@Autowired
	private PortalLayoutTemplateProvider portalLayoutTemplateProvider;

	@Autowired
	private DbProvider dbProvider;

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
	public ListPortalLayoutsResponse listPortalLayouts(ListPortalLayoutsCommand cmd) {
		List<PortalLayout> portalLayouts = portalLayoutProvider.listPortalLayout();
		return new ListPortalLayoutsResponse(portalLayouts.stream().map(r ->{
			return processPortalLayoutDTO(r);
		}).collect(Collectors.toList()));
	}

	private PortalLayoutDTO processPortalLayoutDTO(PortalLayout portalLayout){
		PortalLayoutDTO dto = ConvertHelper.convert(portalLayout, PortalLayoutDTO.class);
		return dto;
	}

	@Override
	public void createPortalLayout(CreatePortalLayoutCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		PortalLayout portalLayout = ConvertHelper.convert(cmd, PortalLayout.class);
		portalLayout.setCreatorUid(user.getId());
		portalLayout.setOperatorUid(user.getId());
		portalLayout.setNamespaceId(namespaceId);
		portalLayout.setStatus(PortalLayoutStatus.ACTIVE.getCode());

		this.dbProvider.execute((status) -> {
			portalLayoutProvider.createPortalLayout(portalLayout);
			if(null != cmd.getLayoutTemplateId()){
				PortalLayoutTemplate template = portalLayoutTemplateProvider.findPortalLayoutTemplateById(cmd.getLayoutTemplateId());
				if(null != template){
					//
				}
			}
			return null;
		});


	}

	@Override
	public void updatePortalLayout(UpdatePortalLayoutCommand cmd) {
		User user = UserContext.current().getUser();
		PortalLayout portalLayout = checkPortalLayout(cmd.getId());
		portalLayout.setName(cmd.getName());
		portalLayout.setDescription(cmd.getDescription());
		portalLayout.setOperatorUid(user.getId());
		portalLayoutProvider.updatePortalLayout(portalLayout);
	}

	@Override
	public void deletePortalLayout(DeletePortalLayoutCommand cmd) {
		PortalLayout portalLayout = checkPortalLayout(cmd.getId());
		portalLayout.setStatus(PortalLayoutStatus.INACTIVE.getCode());
		portalLayoutProvider.updatePortalLayout(portalLayout);
	}

	private PortalLayout checkPortalLayout(Long id){
		PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(id);
		if(null != portalLayout){
			LOGGER.error("Unable to find the portalLayout.id = {}", id);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the portalLayout.");
		}

		return portalLayout;
	}

	@Override
	public ListPortalItemGroupsResponse listPortalItemGroups(ListPortalItemGroupsCommand cmd) {
	
		return new ListPortalItemGroupsResponse();
	}

	@Override
	public void createPortalItemGroup(CreatePortalItemGroupCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		PortalItemGroup portalItemGroup = ConvertHelper.convert(cmd, PortalItemGroup.class);
	}

	@Override
	public void updatePortalItemGroup(UpdatePortalItemGroupCommand cmd) {
	

	}

	@Override
	public void deletePortalItemGroup(DeletePortalItemGroupCommand cmd) {
	

	}

	@Override
	public ListPortalItemsResponse listPortalItems(ListPortalItemsCommand cmd) {
	
		return new ListPortalItemsResponse();
	}

	@Override
	public void createPortalItem(CreatePortalItemCommand cmd) {
	

	}

	@Override
	public void updatePortalItem(UpdatePortalItemCommand cmd) {
	

	}

	@Override
	public void deletePortalItem(DeletePortalItemCommand cmd) {
	

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

}