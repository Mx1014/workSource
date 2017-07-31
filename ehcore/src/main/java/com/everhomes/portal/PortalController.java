// @formatter:off
package com.everhomes.portal;

import com.everhomes.rest.portal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/portal")
public class PortalController extends ControllerBase {
	
	@Autowired
	private PortalService portalService;
	
	/**
	 * <p>1.模块应用列表</p>
	 * <b>URL: /portal/listServiceModuleApps</b>
	 */
	@RequestMapping("listServiceModuleApps")
	@RestReturn(ListServiceModuleAppsResponse.class)
	public RestResponse listServiceModuleApps(ListServiceModuleAppsCommand cmd){
		return new RestResponse(portalService.listServiceModuleApps(cmd));
	}

	/**
	 * <p>1.根据模块id查询模块应用列表</p>
	 * <b>URL: /portal/listServiceModuleAppsByModuleId</b>
	 */
	@RequestMapping("listServiceModuleAppsByModuleId")
	@RestReturn(ListServiceModuleAppsResponse.class)
	public RestResponse listServiceModuleAppsByModuleId(ListServiceModuleAppsCommand cmd){
		return new RestResponse(portalService.listServiceModuleApps(cmd));
	}

	/**
	 * <p>2.创建模块应用</p>
	 * <b>URL: /portal/createServiceModuleApp</b>
	 */
	@RequestMapping("createServiceModuleApp")
	@RestReturn(ServiceModuleAppDTO.class)
	public RestResponse createServiceModuleApp(CreateServiceModuleAppCommand cmd){
		return new RestResponse(portalService.createServiceModuleApp(cmd));
	}

	/**
	 * <p>批量创建模块应用</p>
	 * <b>URL: /portal/batchCreateServiceModuleApp</b>
	 */
	@RequestMapping("batchCreateServiceModuleApp")
	@RestReturn(value = ServiceModuleAppDTO.class, collection = true)
	public RestResponse batchCreateServiceModuleApp(BatchCreateServiceModuleAppCommand cmd){
		return new RestResponse(portalService.batchCreateServiceModuleApp(cmd));
	}

	/**
	 * <p>3.修改模块应用</p>
	 * <b>URL: /portal/updateServiceModuleApp</b>
	 */
	@RequestMapping("updateServiceModuleApp")
	@RestReturn(ServiceModuleAppDTO.class)
	public RestResponse updateServiceModuleApp(UpdateServiceModuleAppCommand cmd){
		return new RestResponse(portalService.updateServiceModuleApp(cmd));
	}

	/**
	 * <p>设置模块应用的instanceConfig</p>
	 * <b>URL: /portal/setServiceModuleAppInstanceConfig</b>
	 */
	@RequestMapping("setServiceModuleAppInstanceConfig")
	@RestReturn(String.class)
	public RestResponse setServiceModuleAppInstanceConfig(SetServiceModuleAppInstanceConfigCommand cmd){
		return new RestResponse();
	}

	/**
	 * <p>4.删除模块应用</p>
	 * <b>URL: /portal/deleteServiceModuleApp</b>
	 */
	@RequestMapping("deleteServiceModuleApp")
	@RestReturn(String.class)
	public RestResponse deleteServiceModuleApp(DeleteServiceModuleAppCommand cmd){
		portalService.deleteServiceModuleApp(cmd);
		return new RestResponse();
	}

	/**
	 * <p>5.门户layout模板列表</p>
	 * <b>URL: /portal/listPortalLayoutTemplates</b>
	 */
	@RequestMapping("listPortalLayoutTemplates")
	@RestReturn(PortalLayoutTemplateDTO.class)
	public RestResponse listPortalLayoutTemplates(){
		return new RestResponse(portalService.listPortalLayoutTemplates());
	}

	/**
	 * <p>5.门户layout列表</p>
	 * <b>URL: /portal/listPortalLayouts</b>
	 */
	@RequestMapping("listPortalLayouts")
	@RestReturn(ListPortalLayoutsResponse.class)
	public RestResponse listPortalLayouts(ListPortalLayoutsCommand cmd){
		return new RestResponse(portalService.listPortalLayouts(cmd));
	}

	/**
	 * <p>6.创建门户layout</p>
	 * <b>URL: /portal/createPortalLayout</b>
	 */
	@RequestMapping("createPortalLayout")
	@RestReturn(PortalLayoutDTO.class)
	public RestResponse createPortalLayout(CreatePortalLayoutCommand cmd){
		portalService.createPortalLayout(cmd);
		return new RestResponse();
	}

	/**
	 * <p>7.修改门户layout</p>
	 * <b>URL: /portal/updatePortalLayout</b>
	 */
	@RequestMapping("updatePortalLayout")
	@RestReturn(PortalLayoutDTO.class)
	public RestResponse updatePortalLayout(UpdatePortalLayoutCommand cmd){
		portalService.updatePortalLayout(cmd);
		return new RestResponse();
	}

	/**
	 * <p>8.删除门户layout</p>
	 * <b>URL: /portal/deletePortalLayout</b>
	 */
	@RequestMapping("deletePortalLayout")
	@RestReturn(String.class)
	public RestResponse deletePortalLayout(DeletePortalLayoutCommand cmd){
		portalService.deletePortalLayout(cmd);
		return new RestResponse();
	}

	/**
	 * <p>9.门户itemGroup列表</p>
	 * <b>URL: /portal/listPortalItemGroups</b>
	 */
	@RequestMapping("listPortalItemGroups")
	@RestReturn(ListPortalItemGroupsResponse.class)
	public RestResponse listPortalItemGroups(ListPortalItemGroupsCommand cmd){
		return new RestResponse(portalService.listPortalItemGroups(cmd));
	}

	/**
	 * <p>10.创建门户itemGroup</p>
	 * <b>URL: /portal/createPortalItemGroup</b>
	 */
	@RequestMapping("createPortalItemGroup")
	@RestReturn(PortalItemGroupDTO.class)
	public RestResponse createPortalItemGroup(CreatePortalItemGroupCommand cmd){
		return new RestResponse(portalService.createPortalItemGroup(cmd));
	}

	/**
	 * <p>11.修改门户itemGroup</p>
	 * <b>URL: /portal/updatePortalItemGroup</b>
	 */
	@RequestMapping("updatePortalItemGroup")
	@RestReturn(PortalItemGroupDTO.class)
	public RestResponse updatePortalItemGroup(UpdatePortalItemGroupCommand cmd){
		return new RestResponse(portalService.updatePortalItemGroup(cmd));
	}

	/**
	 * <p>12.删除门户itemGroup</p>
	 * <b>URL: /portal/deletePortalItemGroup</b>
	 */
	@RequestMapping("deletePortalItemGroup")
	@RestReturn(String.class)
	public RestResponse deletePortalItemGroup(DeletePortalItemGroupCommand cmd){
		portalService.deletePortalItemGroup(cmd);
		return new RestResponse();
	}

	/**
	 * <p>13.门户itemGroup里面的item列表</p>
	 * <b>URL: /portal/listPortalItems</b>
	 */
	@RequestMapping("listPortalItems")
	@RestReturn(ListPortalItemsResponse.class)
	public RestResponse listPortalItems(ListPortalItemsCommand cmd){
		return new RestResponse(portalService.listPortalItems(cmd));
	}

	/**
	 * <p>14.创建门户item</p>
	 * <b>URL: /portal/createPortalItem</b>
	 */
	@RequestMapping("createPortalItem")
	@RestReturn(PortalItemDTO.class)
	public RestResponse createPortalItem(CreatePortalItemCommand cmd){
		return new RestResponse(portalService.createPortalItem(cmd));
	}

	/**
	 * <p>15.修改门户item</p>
	 * <b>URL: /portal/updatePortalItem</b>
	 */
	@RequestMapping("updatePortalItem")
	@RestReturn(PortalItemDTO.class)
	public RestResponse updatePortalItem(UpdatePortalItemCommand cmd){
		return new RestResponse(portalService.updatePortalItem(cmd));
	}

	/**
	 * <p>16.删除门户item</p>
	 * <b>URL: /portal/deletePortalItem</b>
	 */
	@RequestMapping("deletePortalItem")
	@RestReturn(String.class)
	public RestResponse deletePortalItem(DeletePortalItemCommand cmd){
		portalService.deletePortalItem(cmd);
		return new RestResponse();
	}

	/**
	 * <p>设置item的状态</p>
	 * <b>URL: /portal/setPortalItemStatus</b>
	 */
	@RequestMapping("setPortalItemStatus")
	@RestReturn(String.class)
	public RestResponse setPortalItemStatus(SetPortalItemStatusCommand cmd){
		portalService.setPortalItemStatus(cmd);
		return new RestResponse();
	}

	/**
	 * <p>设置item的instanceConfig</p>
	 * <b>URL: /portal/setPortalItemActionData</b>
	 */
	@RequestMapping("setPortalItemActionData")
	@RestReturn(String.class)
	public RestResponse setPortalItemActionData(SetPortalItemActionDataCommand cmd){
		portalService.setPortalItemActionData(cmd);
		return new RestResponse();
	}

	/**
	 * <p>设置item分类的默认样式</p>
	 * <b>URL: /portal/setItemCategoryDefStyle</b>
	 */
	@RequestMapping("setItemCategoryDefStyle")
	@RestReturn(String.class)
	public RestResponse setItemCategoryDefStyle(SetItemCategoryDefStyleCommand cmd){
		portalService.setItemCategoryDefStyle(cmd);
		return new RestResponse();
	}

	/**
	 * <p>17.重新排序item</p>
	 * <b>URL: /portal/reorderPortalItem</b>
	 */
	@RequestMapping("reorderPortalItem")
	@RestReturn(String.class)
	public RestResponse reorderPortalItem(ReorderPortalItemCommand cmd){
		portalService.reorderPortalItem(cmd);
		return new RestResponse();
	}

	/**
	 * <p>18.重新排序itemGroup</p>
	 * <b>URL: /portal/reorderPortalItemGroup</b>
	 */
	@RequestMapping("reorderPortalItemGroup")
	@RestReturn(String.class)
	public RestResponse reorderPortalItemGroup(ReorderPortalItemGroupCommand cmd){
		portalService.reorderPortalItemGroup(cmd);
		return new RestResponse();
	}

	/**
	 * <p>19.删除门户item</p>
	 * <b>URL: /portal/getPortalItemById</b>
	 */
	@RequestMapping("getPortalItemById")
	@RestReturn(GetPortalItemByIdResponse.class)
	public RestResponse getPortalItemById(GetPortalItemByIdCommand cmd){
		return new RestResponse(portalService.getPortalItemById(cmd));
	}

	/**
	 * <p>19.查询item group下的item</p>
	 * <b>URL: /portal/getPortalItemsByItemGroupId</b>
	 */
	@RequestMapping("getPortalItemsByItemGroupId")
	@RestReturn(value = PortalItemDTO.class, collection = true)
	public RestResponse getPortalItemsByItemGroupId(ListPortalItemsCommand cmd){
		return new RestResponse(portalService.listPortalItemsByItemGroupId(cmd));
	}

	/**
	 * <p>20.门户item分类列表</p>
	 * <b>URL: /portal/listPortalItemCategories</b>
	 */
	@RequestMapping("listPortalItemCategories")
	@RestReturn(ListPortalItemCategoriesResponse.class)
	public RestResponse listPortalItemCategories(ListPortalItemCategoriesCommand cmd){
		return new RestResponse(portalService.listPortalItemCategories(cmd));
	}

	/**
	 * <p>获取门户item分类</p>
	 * <b>URL: /portal/getPortalItemCategory</b>
	 */
	@RequestMapping("getPortalItemCategory")
	@RestReturn(PortalItemCategoryDTO.class)
	public RestResponse getPortalItemCategory(GetPortalItemCategoryCommand cmd){
		return new RestResponse(portalService.getPortalItemCategoryById(cmd));
	}


	/**
	 * <p>21.创建item分类</p>
	 * <b>URL: /portal/createPortalItemCategory</b>
	 */
	@RequestMapping("createPortalItemCategory")
	@RestReturn(PortalItemCategoryDTO.class)
	public RestResponse createPortalItemCategory(CreatePortalItemCategoryCommand cmd){
		return new RestResponse(portalService.createPortalItemCategory(cmd));
	}

	/**
	 * <p>22.修改item分类</p>
	 * <b>URL: /portal/updatePortalItemCategory</b>
	 */
	@RequestMapping("updatePortalItemCategory")
	@RestReturn(PortalItemCategoryDTO.class)
	public RestResponse updatePortalItemCategory(UpdatePortalItemCategoryCommand cmd){
		return new RestResponse(portalService.updatePortalItemCategory(cmd));
	}

	/**
	 * <p>23.删除item分类</p>
	 * <b>URL: /portal/deletePortalItemCategory</b>
	 */
	@RequestMapping("deletePortalItemCategory")
	@RestReturn(String.class)
	public RestResponse deletePortalItemCategory(DeletePortalItemCategoryCommand cmd){
		portalService.deletePortalItemCategory(cmd);
		return new RestResponse();
	}

	/**
	 * <p>24.重新排序item分类</p>
	 * <b>URL: /portal/reorderPortalItemCategory</b>
	 */
	@RequestMapping("reorderPortalItemCategory")
	@RestReturn(String.class)
	public RestResponse reorderPortalItemCategory(ReorderPortalItemCategoryCommand cmd){
		portalService.reorderPortalItemCategory(cmd);
		return new RestResponse();
	}

	/**
	 * <p>25.调整item的分类</p>
	 * <b>URL: /portal/rankPortalItemCategory</b>
	 */
	@RequestMapping("rankPortalItemCategory")
	@RestReturn(String.class)
	public RestResponse rankPortalItemCategory(RankPortalItemCategoryCommand cmd){
		portalService.rankPortalItemCategory(cmd);
		return new RestResponse();
	}

	/**
	 * <p>26.查询门户itemGroup</p>
	 * <b>URL: /portal/getPortalItemGroupById</b>
	 */
	@RequestMapping("getPortalItemGroupById")
	@RestReturn(GetPortalItemGroupByIdResponse.class)
	public RestResponse getPortalItemGroupById(GetPortalItemGroupByIdCommand cmd){
		return new RestResponse(portalService.getPortalItemGroupById(cmd));
	}

	/**
	 * <p>27.门户导航栏列表</p>
	 * <b>URL: /portal/listPortalNavigationBars</b>
	 */
	@RequestMapping("listPortalNavigationBars")
	@RestReturn(ListPortalNavigationBarsResponse.class)
	public RestResponse listPortalNavigationBars(ListPortalNavigationBarsCommand cmd){
		return new RestResponse(portalService.listPortalNavigationBars(cmd));
	}

	/**
	 * <p>28.创建门户导航栏</p>
	 * <b>URL: /portal/createPortalNavigationBar</b>
	 */
	@RequestMapping("createPortalNavigationBar")
	@RestReturn(String.class)
	public RestResponse createPortalNavigationBar(CreatePortalNavigationBarCommand cmd){
		return new RestResponse(portalService.createPortalNavigationBar(cmd));
	}

	/**
	 * <p>29.修改门户导航栏</p>
	 * <b>URL: /portal/updatePortalNavigationBar</b>
	 */
	@RequestMapping("updatePortalNavigationBar")
	@RestReturn(PortalNavigationBarDTO.class)
	public RestResponse updatePortalNavigationBar(UpdatePortalNavigationBarCommand cmd){
		return new RestResponse(portalService.updatePortalNavigationBar(cmd));
	}

	/**
	 * <p>30.删除门户导航栏</p>
	 * <b>URL: /portal/deletePortalNavigationBar</b>
	 */
	@RequestMapping("deletePortalNavigationBar")
	@RestReturn(PortalNavigationBarDTO.class)
	public RestResponse deletePortalNavigationBar(DeletePortalNavigationBarCommand cmd){
		portalService.deletePortalNavigationBar(cmd);
		return new RestResponse();
	}

	/**
	 * <p>31.发布</p>
	 * <b>URL: /portal/publish</b>
	 */
	@RequestMapping("publish")
	@RestReturn(PortalPublishLogDTO.class)
	public RestResponse publish(PublishCommand cmd){
		return new RestResponse(portalService.publish(cmd));
	}

	/**
	 * <p>获取发布状态</p>
	 * <b>URL: /portal/getPortalPublishLog</b>
	 */
	@RequestMapping("getPortalPublishLog")
	@RestReturn(PortalPublishLogDTO.class)
	public RestResponse getPortalPublishLog(GetPortalPublishLogCommand cmd){
		return new RestResponse(portalService.getPortalPublishLog(cmd));
	}

	/**
	 * <p>根据场景获取 物业公司普通公司园区小区列表</p>
	 * <b>URL: /portal/listScopes</b>
	 */
	@RequestMapping("listScopes")
	@RestReturn(ListScopeResponse.class)
	public RestResponse listScopes(ListScopeCommand cmd){
		return new RestResponse(portalService.listScopes(cmd));
	}

	/**
	 * <p>根据场景 搜索物业公司普通公司园区小区列表</p>
	 * <b>URL: /portal/searchScopes</b>
	 */
	@RequestMapping("searchScopes")
	@RestReturn(ListScopeResponse.class)
	public RestResponse searchScopes(ListScopeCommand cmd){
		return new RestResponse(portalService.searchScopes(cmd));
	}

	/**
	 * <p>获取全部或者更多item</p>
	 * <b>URL: /portal/getAllOrMoreItem</b>
	 */
	@RequestMapping("getAllOrMoreItem")
	@RestReturn(PortalItemDTO.class)
	public RestResponse getAllOrMoreItem(GetItemAllOrMoreCommand cmd){
		return new RestResponse(portalService.getAllOrMoreItem(cmd));
	}
}