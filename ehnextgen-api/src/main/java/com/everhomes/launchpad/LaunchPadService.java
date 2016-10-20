package com.everhomes.launchpad;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.launchpad.admin.CreateLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.CreateLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.DeleteLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.DeleteLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.GetLaunchPadItemsByKeywordAdminCommand;
import com.everhomes.rest.launchpad.admin.GetLaunchPadItemsByKeywordAdminCommandResponse;
import com.everhomes.rest.launchpad.admin.ListLaunchPadLayoutAdminCommand;
import com.everhomes.rest.launchpad.admin.UpdateLaunchPadItemAdminCommand;
import com.everhomes.rest.launchpad.admin.UpdateLaunchPadLayoutAdminCommand;
import com.everhomes.rest.ui.launchpad.AddLaunchPadItemBySceneCommand;
import com.everhomes.rest.ui.launchpad.CancelFavoriteBusinessBySceneCommand;
import com.everhomes.rest.ui.launchpad.DeleteLaunchPadItemBySceneCommand;
import com.everhomes.rest.ui.launchpad.FavoriteBusinessesBySceneCommand;
import com.everhomes.rest.ui.launchpad.GetLaunchPadItemsBySceneCommand;
import com.everhomes.rest.ui.launchpad.GetLaunchPadLayoutBySceneCommand;
import com.everhomes.rest.ui.launchpad.ReorderLaunchPadItemBySceneCommand;


public interface LaunchPadService {

    //void createLaunchPadItem(CreateLaunchPatItemCommand cmd);
    void userDefinedLaunchPad(UserDefinedLaunchPadCommand cmd);
    void createLaunchPadItem(CreateLaunchPadItemAdminCommand cmd);
    void deleteLaunchPadItem(DeleteLaunchPadItemAdminCommand cmd);
    List<LaunchPadLayoutDTO> getLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd, ScopeType scopeType, Long scopeId);
    LaunchPadLayoutDTO getLastLaunchPadLayoutByVersionCode(GetLaunchPadLayoutByVersionCodeCommand cmd, ScopeType scopeType, Long scopeId);
    LaunchPadLayoutDTO getLastLaunchPadLayoutByScene(@Valid GetLaunchPadLayoutBySceneCommand cmd);
    void createLaunchPadLayout(CreateLaunchPadLayoutAdminCommand cmd);
    GetLaunchPadItemsCommandResponse getLaunchPadItems(GetLaunchPadItemsCommand cmd, HttpServletRequest request);
    GetLaunchPadItemsCommandResponse getLaunchPadItems(GetLaunchPadItemsByOrgCommand cmd, HttpServletRequest request);
    GetLaunchPadItemsCommandResponse getLaunchPadItemsByScene(GetLaunchPadItemsBySceneCommand cmd, HttpServletRequest request);
    void deleteLaunchPadLayout(DeleteLaunchPadLayoutAdminCommand cmd);
    void updateLaunchPadLayout(UpdateLaunchPadLayoutAdminCommand cmd);
    LaunchPadLayoutDTO getLaunchPadLayout(GetLaunchPadLayoutCommand cmd);
    LaunchPadItemDTO getLaunchPadItemById(GetLaunchPadItemByIdCommand cmd);
    void updateLaunchPadItem(UpdateLaunchPadItemAdminCommand cmd);
    GetLaunchPadItemsByKeywordAdminCommandResponse getLaunchPadItemsByKeyword(GetLaunchPadItemsByKeywordAdminCommand cmd);
	ListLaunchPadLayoutCommandResponse listLaunchPadLayoutByKeyword(ListLaunchPadLayoutAdminCommand cmd);
	void deleteLaunchPadById(DeleteLaunchPadByIdCommand cmd);
	
	void favoriteBusinessesByScene(FavoriteBusinessesBySceneCommand cmd);
	void cancelFavoriteBusinessByScene(CancelFavoriteBusinessBySceneCommand cmd);
	GetLaunchPadItemsCommandResponse getMoreItemsByScene(GetLaunchPadItemsBySceneCommand cmd, HttpServletRequest request);
	GetLaunchPadItemsCommandResponse getMoreItems(GetLaunchPadItemsCommand cmd, HttpServletRequest request);
	void reorderLaunchPadItemByScene(ReorderLaunchPadItemBySceneCommand cmd, ItemDisplayFlag itemDisplayFlag);
	UserLaunchPadItemDTO deleteLaunchPadItemByScene(DeleteLaunchPadItemBySceneCommand cmd);
	UserLaunchPadItemDTO addLaunchPadItemByScene(AddLaunchPadItemBySceneCommand cmd);

    /**
     * 获取全部类型的item
     * @param cmd
     * @param request
     * @return
     */
    List<CategryItemDTO> getAllCategryItemsByScene(GetLaunchPadItemsBySceneCommand cmd, HttpServletRequest request);

    /**
     * 根据园区获取全部类型的item
     * @param cmd
     * @param request
     * @return
     */
    List<CategryItemDTO> getAllCategryItems(GetLaunchPadItemsCommand cmd, HttpServletRequest request);

    /**
     * 根据机构获取全部类型的item
     * @param cmd
     * @param request
     * @return
     */
    List<CategryItemDTO> getAllCategryItems(GetLaunchPadItemsByOrgCommand cmd, HttpServletRequest request);
}
