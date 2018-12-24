package com.everhomes.launchpad;

import java.util.List;
import java.util.Map;

import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.LaunchPadLayoutDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.util.Tuple;
import org.jooq.Condition;

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
    List<LaunchPadItem> findLaunchPadItemsByTagAndScope(Integer namespaceId, String sceneType, String itemLocation,String itemGroup,Byte scopeCode,long scopeId,List<String> tags, String categryName);
    void updateLaunchPadLayout(LaunchPadLayout launchPadLayout);

    void deleteLaunchPadLayout(Long id);

    LaunchPadLayout findLaunchPadLayoutById(long id);
    List<LaunchPadItem> getLaunchPadItemsByKeyword(String keyword, int offset, int pageSize);
    List<LaunchPadItem> searchLaunchPadItemsByKeyword(Integer namespaceId, String sceneType, Map<Byte, Long> scopeMap, Map<Byte, Long> defalutScopeMap, String keyword, int offset, int pageSize);
	List<LaunchPadLayoutDTO> listLaunchPadLayoutByKeyword(int pageSize, long offset, String keyword);

    void deleteLaunchPadLayout(Integer namespaceId, String name, Byte publishType);

    List<LaunchPadItem> findLaunchPadItemByTargetAndScope(String targetType, long targetId, Byte scopeCode, long scopeId, Integer namesapceId);
    void deleteLaunchPadItemByTargetTypeAndTargetId(String targetType, long targetId);
    void deleteLaunchPadItemByScopeAndTargetId(Byte scopeCode,Long scopeId,String targetType, long targetId);
	List<LaunchPadItem> findLaunchPadItem(Integer nameSpaceId,String itemGroup, String itemName,Byte scopeCode, long scopeId);
	List<UserLaunchPadItem> findUserLaunchPadItemByUserId(Long userId, String sceneType, String ownerType, Long ownerId);
	void deleteUserLaunchPadItemById(Long id);
	void createUserLaunchPadItem(UserLaunchPadItem userItem);
	UserLaunchPadItem getUserLaunchPadItemByOwner(Long userId, String sceneType, String ownerType, Long ownerId, String itemName);
	void updateUserLaunchPadItemById(UserLaunchPadItem userItem);
	LaunchPadItem findLaunchPadItemByTargetAndScopeAndSence(String targetType, long targetId,Byte scopeCode, long scopeId,Integer namesapceId, SceneType sceneType);
    List<ItemServiceCategry> listItemServiceCategries(Integer namespaceId, String itemLocation, String itemGroup, ListingQueryBuilderCallback callback);

    List<ItemServiceCategry> listItemServiceCategryByGroupId(Long groupId, List<Tuple<Byte, Long>> scopes);

    void createItemServiceCategry(ItemServiceCategry itemServiceCategry);

    List<LaunchPadItem> listLaunchPadItemsByNamespaceId(Integer namespaceId);
    void deleteItemServiceCategryById(Long id);
    List<LaunchPadItem> listLaunchPadItemsByItemGroup(Integer namespaceId, String itemLocation,String itemGroup);
    List<LaunchPadLayout> getLaunchPadLayouts(String name, Integer namespaceId);
    List<LaunchPadItem> listLaunchPadItemsByScopeType(Integer namespaceId, String itemLocation,String itemGroup, Byte applyPolicy, ListingQueryBuilderCallback queryBuilderCallback);
    List<ItemServiceCategry> listItemServiceCategries(Integer namespaceId, String itemLocation, String itemGroup);
	LaunchPadItem searchLaunchPadItemsByItemName(Integer namespaceId, String sceneType, String itemName);

    List<LaunchPadItem> findLaunchPadItem(Integer namespaceId,String itemGroup,String location);

    List<LaunchPadItem> findLaunchPadItem(Integer namespaceId,String itemGroup, String location, String itemName, Byte scopeCode, Long scopeId);

    Condition getPreviewPortalVersionCondition(String tableName);

    void deletePreviewVersionItems(Integer namespaceId);

    void deletePreviewVersionLayouts(Integer namespaceId);

    void deletePreviewVersionCategories(Integer namespaceId);

    List<LaunchPadItem> listLaunchPadItemsByGroupId(Long groupId, List<Tuple<Byte, Long>> scopes, String categoryName, Byte displayFlag, Byte moreOrderFlag);

    void deleteUserLaunchPadItemByUserId(Long userId, Long groupId, String ownerType, Long ownerId);

    List<UserLaunchPadItem> listUserLaunchPadItemByUserId(Long userId, Long groupId, String ownerType, Long ownerId);

    List<LaunchPadItem> listLaunchPadItemsByIds(List<Long> itemIds);
}
