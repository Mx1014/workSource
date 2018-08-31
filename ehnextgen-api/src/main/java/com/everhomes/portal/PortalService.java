// @formatter:off
package com.everhomes.portal;

import com.everhomes.rest.portal.*;
import com.everhomes.serviceModuleApp.ServiceModuleApp;

import java.util.List;

public interface PortalService {


	//todo
	ListServiceModuleAppsResponse listServiceModuleApps(ListServiceModuleAppsCommand cmd);

    PortalVersion findReleaseVersion(Integer namespaceId);

    ListServiceModuleAppsResponse listServiceModuleAppsWithConditon(ListServiceModuleAppsCommand cmd);

	ServiceModuleAppDTO createServiceModuleApp(CreateServiceModuleAppCommand cmd);

	List<ServiceModuleAppDTO> batchCreateServiceModuleApp(BatchCreateServiceModuleAppCommand cmd);

	ServiceModuleAppDTO updateServiceModuleApp(UpdateServiceModuleAppCommand cmd);


	void deleteServiceModuleApp(DeleteServiceModuleAppCommand cmd);


	ListPortalLayoutsResponse listPortalLayouts(ListPortalLayoutsCommand cmd);


    ServiceModuleAppDTO processServiceModuleAppDTO(ServiceModuleApp moduleApp);

    PortalLayoutDTO createPortalLayout(CreatePortalLayoutCommand cmd);


	PortalLayoutDTO updatePortalLayout(UpdatePortalLayoutCommand cmd);


    void deletePortalLayout(DeletePortalLayoutCommand cmd);


	ListPortalItemGroupsResponse listPortalItemGroups(ListPortalItemGroupsCommand cmd);


	PortalItemGroupDTO createPortalItemGroup(CreatePortalItemGroupCommand cmd);


	PortalItemGroupDTO updatePortalItemGroup(UpdatePortalItemGroupCommand cmd);


	void deletePortalItemGroup(DeletePortalItemGroupCommand cmd);


	ListPortalItemsResponse listPortalItems(ListPortalItemsCommand cmd);

	List<PortalItemDTO> listPortalItemsByItemGroupId(ListPortalItemsCommand cmd);

	PortalItemDTO createPortalItem(CreatePortalItemCommand cmd);


	PortalItemDTO updatePortalItem(UpdatePortalItemCommand cmd);


	void deletePortalItem(DeletePortalItemCommand cmd);

	void setPortalItemStatus(SetPortalItemStatusCommand cmd);

	void setPortalItemActionData(SetPortalItemActionDataCommand cmd);


	void reorderPortalItem(ReorderPortalItemCommand cmd);


	void reorderPortalItemGroup(ReorderPortalItemGroupCommand cmd);


	PortalItemDTO getPortalItemById(GetPortalItemByIdCommand cmd);

	PortalItemCategoryDTO getPortalItemCategoryById(GetPortalItemCategoryCommand cmd);

	ListPortalItemCategoriesResponse listPortalItemCategories(ListPortalItemCategoriesCommand cmd);


	PortalItemCategoryDTO createPortalItemCategory(CreatePortalItemCategoryCommand cmd);


	PortalItemCategoryDTO updatePortalItemCategory(UpdatePortalItemCategoryCommand cmd);


	void deletePortalItemCategory(DeletePortalItemCategoryCommand cmd);


	void reorderPortalItemCategory(ReorderPortalItemCategoryCommand cmd);


	void rankPortalItemCategory(RankPortalItemCategoryCommand cmd);


	PortalItemGroupDTO getPortalItemGroupById(GetPortalItemGroupByIdCommand cmd);


	ListPortalNavigationBarsResponse listPortalNavigationBars(ListPortalNavigationBarsCommand cmd);


	PortalNavigationBarDTO createPortalNavigationBar(CreatePortalNavigationBarCommand cmd);


	PortalNavigationBarDTO updatePortalNavigationBar(UpdatePortalNavigationBarCommand cmd);


	void deletePortalNavigationBar(DeletePortalNavigationBarCommand cmd);


	PortalPublishLogDTO publish(PublishCommand cmd);

	PortalPublishLogDTO getPortalPublishLog(GetPortalPublishLogCommand cmd);

	List<PortalLayoutTemplateDTO> listPortalLayoutTemplates();

	void setItemCategoryDefStyle(SetItemCategoryDefStyleCommand cmd);

	ListScopeResponse listScopes(ListScopeCommand cmd);

	ListScopeResponse searchScopes(ListScopeCommand cmd);

	PortalItemDTO getAllOrMoreItem(GetItemAllOrMoreCommand cmd);

	void syncLaunchPadData(SyncLaunchPadDataCommand cmd);

    PortalVersionDTO findPortalVersionById(Long versionId);

    PortalPublishHandler getPortalPublishHandler(Long moduleId);

    ListPortalVersionResponse listPortalVersions(ListPortalVersionCommand cmd);

	void updatePortalVersionUsers(UpdatePortalVersionUsersCommand cmd);

	ListPortalVersionUsersResponse listPortalVersionUsers(ListPortalVersionUsersCommand cmd);

    void revertVersion(RevertVersionCommand cmd);

	PortalLayoutDTO findIndexPortalLayout(FindIndexPortalLayoutCommand cmd);
}