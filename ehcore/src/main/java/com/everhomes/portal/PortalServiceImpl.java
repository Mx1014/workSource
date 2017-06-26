// @formatter:off
package com.everhomes.portal;

import com.everhomes.rest.portal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PortalServiceImpl implements PortalService {

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
	
		return new ListPortalLayoutsResponse();
	}

	@Override
	public void createPortalLayout(CreatePortalLayoutCommand cmd) {
	

	}

	@Override
	public void updatePortalLayout(UpdatePortalLayoutCommand cmd) {
	

	}

	@Override
	public void deletePortalLayout(DeletePortalLayoutCommand cmd) {
	

	}

	@Override
	public ListPortalItemGroupsResponse listPortalItemGroups(ListPortalItemGroupsCommand cmd) {
	
		return new ListPortalItemGroupsResponse();
	}

	@Override
	public void createPortalItemGroup(CreatePortalItemGroupCommand cmd) {
	

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