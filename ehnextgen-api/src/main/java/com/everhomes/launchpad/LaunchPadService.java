package com.everhomes.launchpad;

import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.launchpad.UpdateUserAppsCommand;
import com.everhomes.rest.launchpad.admin.*;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.ui.launchpad.*;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


public interface LaunchPadService {

    //void createLaunchPadItem(CreateLaunchPatItemCommand cmd);
    void userDefinedLaunchPad(UserDefinedLaunchPadCommand cmd);

    String refreshActionData(String actionData);

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
    LaunchPadLayoutDTO getLaunchPadBaseLayout(GetLaunchPadLayoutCommand cmd);
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

    List<ItemServiceCategryDTO> listItemServiceCategries();

    /**
     * 编辑服务广场的item
     * @param cmd
     */
    void editLaunchPadItemByScene(EditLaunchPadItemBySceneCommand cmd);
    
    /**
     * 查询应用
     * @param cmd
     * @return
     */
    SearchContentsBySceneReponse searchLaunchPadItemByScene(SearchContentsBySceneCommand cmd);

    List<IndexDTO> listIndexDtos(Integer namespaceId, Long userId);

    ListBannersResponse listBanners(ListBannersCommand cmd);

    ListOPPushCardsResponse listOPPushCards(ListOPPushCardsCommand cmd);

    OPPushHandler getOPPushHandler(Long moduleId);

    BulletinsHandler getBulletinsHandler(Long moduleId);

    String getSceneTokenByCommunityId(Long communityId);

    ListBulletinsCardsResponse listBulletinsCards(ListBulletinsCardsCommand cmd);

    ListAllAppsResponse listAllApps(ListAllAppsCommand cmd);

    void updateUserApps(UpdateUserAppsCommand cmd);
}
