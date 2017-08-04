package com.everhomes.launchpad;

import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.LaunchPadLayoutDTO;
import com.everhomes.rest.ui.user.SceneType;

import java.util.List;
import java.util.Map;

public interface LaunchPadProvider {
    void createLaunchPadItem(LaunchPadItem item);
    void updateLaunchPadItem(LaunchPadItem item);
    void deleteLaunchPadItem(LaunchPadItem item);
    void deleteLaunchPadItem(long id);
    void createLaunchPadItems(List<LaunchPadItem> items);
    LaunchPadItem findLaunchPadItemById(long id);
    List<LaunchPadItem> listLaunchPadItemsByScopeTypeAndScopeId(Byte scopeCode, long scopeId);
    void createLaunchPadLayout(LaunchPadLayout launchPadLayout);
    List<LaunchPadLayout> findLaunchPadItemsByVersionCode(Integer namespaceId, String sceneType, String name,long versionCode, ScopeType scopeType, Long scopeId);
    List<LaunchPadItem> findLaunchPadItemsByTagAndScope(Integer namespaceId, String sceneType, String itemLocation,String itemGroup, Byte scopeCode, long scopeId, List<String> tag);
    void updateLaunchPadLayout(LaunchPadLayout launchPadLayout);
    LaunchPadLayout findLaunchPadLayoutById(long id);
    List<LaunchPadItem> getLaunchPadItemsByKeyword(String keyword, int offset, int pageSize);
    List<LaunchPadItem> searchLaunchPadItemsByKeyword(Integer namespaceId, String sceneType, Map<Byte, Long> scopeMap, String keyword, int offset, int pageSize);
	List<LaunchPadLayoutDTO> listLaunchPadLayoutByKeyword(int pageSize, long offset, String keyword);
    List<LaunchPadItem> findLaunchPadItemByTargetAndScope(String targetType, long targetId,Byte scopeCode, long scopeId, Integer namesapceId);
    void deleteLaunchPadItemByTargetTypeAndTargetId(String targetType, long targetId);
    void deleteLaunchPadItemByScopeAndTargetId(Byte scopeCode,Long scopeId,String targetType, long targetId);
	List<LaunchPadItem> findLaunchPadItem(Integer nameSpaceId,String itemGroup, String itemName,Byte scopeCode, long scopeId);
	List<UserLaunchPadItem> findUserLaunchPadItemByUserId(Long userId, String sceneType, String ownerType, Long ownerId);
	void deleteUserLaunchPadItemById(Long id);
	void createUserLaunchPadItem(UserLaunchPadItem userItem);
	UserLaunchPadItem getUserLaunchPadItemByOwner(Long userId, String sceneType, String ownerType, Long ownerId, Long itemId);
	void updateUserLaunchPadItemById(UserLaunchPadItem userItem);
	LaunchPadItem findLaunchPadItemByTargetAndScopeAndSence(String targetType, long targetId,Byte scopeCode, long scopeId,Integer namesapceId, SceneType sceneType);
    List<ItemServiceCategry> listItemServiceCategries(Integer namespaceId, String sceneType);
    void createItemServiceCategry(ItemServiceCategry itemServiceCategry);

    List<LaunchPadItem> listLaunchPadItemsByNamespaceId(Integer namespaceId);
}
