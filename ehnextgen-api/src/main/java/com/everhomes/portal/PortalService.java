// @formatter:off
package com.everhomes.portal;

import com.everhomes.rest.portal.*;

public interface PortalService {


	public ListServiceModuleAppsResponse listServiceModuleApps(ListServiceModuleAppsCommand cmd);


	public void createServiceModuleApp(CreateServiceModuleAppCommand cmd);


	public void updateServiceModuleApp(UpdateServiceModuleAppCommand cmd);


	public void deleteServiceModuleApp(DeleteServiceModuleAppCommand cmd);


	public ListPortalLayoutsResponse listPortalLayouts(ListPortalLayoutsCommand cmd);


	public PortalLayout createPortalLayout(CreatePortalLayoutCommand cmd);


	public PortalLayout updatePortalLayout(UpdatePortalLayoutCommand cmd);


	public void deletePortalLayout(DeletePortalLayoutCommand cmd);


	public ListPortalItemGroupsResponse listPortalItemGroups(ListPortalItemGroupsCommand cmd);


	public PortalItemGroup createPortalItemGroup(CreatePortalItemGroupCommand cmd);


	public PortalItemGroup updatePortalItemGroup(UpdatePortalItemGroupCommand cmd);


	public void deletePortalItemGroup(DeletePortalItemGroupCommand cmd);


	public ListPortalItemsResponse listPortalItems(ListPortalItemsCommand cmd);


	public PortalItem createPortalItem(CreatePortalItemCommand cmd);


	public PortalItem updatePortalItem(UpdatePortalItemCommand cmd);


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

}