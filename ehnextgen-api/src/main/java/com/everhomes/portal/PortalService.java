// @formatter:off
package com.everhomes.portal;

import com.everhomes.rest.portal.*;

import java.util.List;

public interface PortalService {


	public ListServiceModuleAppsResponse listServiceModuleApps(ListServiceModuleAppsCommand cmd);


	public void createServiceModuleApp(CreateServiceModuleAppCommand cmd);


	public void updateServiceModuleApp(UpdateServiceModuleAppCommand cmd);


	public void deleteServiceModuleApp(DeleteServiceModuleAppCommand cmd);


	public ListPortalLayoutsResponse listPortalLayouts(ListPortalLayoutsCommand cmd);


	public PortalLayoutDTO createPortalLayout(CreatePortalLayoutCommand cmd);


	public PortalLayoutDTO updatePortalLayout(UpdatePortalLayoutCommand cmd);


	public void deletePortalLayout(DeletePortalLayoutCommand cmd);


	public ListPortalItemGroupsResponse listPortalItemGroups(ListPortalItemGroupsCommand cmd);


	public PortalItemGroupDTO createPortalItemGroup(CreatePortalItemGroupCommand cmd);


	public PortalItemGroupDTO updatePortalItemGroup(UpdatePortalItemGroupCommand cmd);


	public void deletePortalItemGroup(DeletePortalItemGroupCommand cmd);


	public ListPortalItemsResponse listPortalItems(ListPortalItemsCommand cmd);


	public PortalItemDTO createPortalItem(CreatePortalItemCommand cmd);


	public PortalItemDTO updatePortalItem(UpdatePortalItemCommand cmd);


	public void deletePortalItem(DeletePortalItemCommand cmd);


	public void reorderPortalItem(ReorderPortalItemCommand cmd);


	public void reorderPortalItemGroup(ReorderPortalItemGroupCommand cmd);


	public GetPortalItemByIdResponse getPortalItemById(GetPortalItemByIdCommand cmd);


	public ListPortalItemCategoriesResponse listPortalItemCategories(ListPortalItemCategoriesCommand cmd);


	public void createPortalItemCategory(CreatePortalItemCategoryCommand cmd);


	public void updatePortalItemCategory(UpdatePortalItemCategoryCommand cmd);


	public void deletePortalItemCategory(DeletePortalItemCategoryCommand cmd);


	public void reorderPortalItemCategory(ReorderPortalItemCategoryCommand cmd);


	public void rankPortalItemCategory(RankPortalItemCategoryCommand cmd);


	public GetPortalItemGroupByIdResponse getPortalItemGroupById(GetPortalItemGroupByIdCommand cmd);


	public ListPortalNavigationBarsResponse listPortalNavigationBars(ListPortalNavigationBarsCommand cmd);


	public void createPortalNavigationBar(CreatePortalNavigationBarCommand cmd);


	public void updatePortalNavigationBar(UpdatePortalNavigationBarCommand cmd);


	public void deletePortalNavigationBar(DeletePortalNavigationBarCommand cmd);


	public void publish(PublishCommand cmd);

	List<PortalLayoutTemplateDTO> listPortalLayoutTemplates();

}