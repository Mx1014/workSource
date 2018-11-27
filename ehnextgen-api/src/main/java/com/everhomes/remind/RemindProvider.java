package com.everhomes.remind;

import com.everhomes.rest.remind.SharingPersonDTO;
import com.everhomes.server.schema.tables.pojos.EhRemindCategoryDefaultShares;
import com.everhomes.server.schema.tables.pojos.EhRemindShares;

import java.sql.Timestamp;
import java.util.List;

public interface RemindProvider {

    RemindSetting getRemindSettingById(Integer id);

    List<RemindSetting> findRemindSettings();

    void evictRemindSettingsCache();

    boolean checkCategoryNameExist(CheckRemindCategoryNameExistRequest request);

    Integer getNextCategoryDefaultOrder(Integer namespaceId, String ownerType, Long ownerId, Long userId);

    void sortRemindCategories(Integer namespaceId, String ownerType, Long ownerId, Long userId, Integer currentOrder, Integer targetOrder);

    void sortReminds(Integer namespaceId, String ownerType, Long ownerId, Long userId, Integer currentOrder, Integer targetOrder);

    List<RemindCategory> findRemindCategories(Integer namespaceId, String ownerType, Long ownerId, Long userId);

    Long createRemindCategory(RemindCategory remindCategory);

    Long updateRemindCategory(RemindCategory remindCategory);

    void deleteRemindCategory(RemindCategory remindCategory);

    RemindCategory getRemindCategory(Integer namespaceId, String ownerType, Long ownerId, Long userId, Long categoryId);

    int countUserRemindCategories(Integer namespaceId, String ownerType, Long ownerId, Long userId);

    List<RemindCategoryDefaultShare> findShareMemberDetailsByCategoryId(Long categoryId);

    void batchCreateRemindCategoryDefaultShare(List<EhRemindCategoryDefaultShares> defaultShares);

    void deleteRemindCategoryDefaultSharesByCategoryId(Long remindCategoryId);

    void batchDeleteRemindCategoryDefaultShares(List<EhRemindCategoryDefaultShares> remindCategoryDefaultShares);

    Remind getRemindDetail(Integer namespaceId, String ownerType, Long ownerId, Long userId, Long remindId);

    boolean checkRemindShareToUser(Long memberDetailId, Long remindId, Long categoryId);

    Remind getSubscribeRemind(Integer namespaceId, String ownerType, Long ownerId, Long userId, Long subscribeRemindId);

    Long createRemind(Remind remind);

    Long updateRemind(Remind remind);

    void deleteRemind(Remind remind);

    Integer getNextRemindDefaultOrder(Integer namespaceId, String ownerType, Long ownerId, Long userId, Byte status);

    List<RemindShare> findShareMemberDetailsByRemindId(Long remindId);

    List<SharingPersonDTO> findSharingPersonsBySourceId(Integer namespaceId, String ownerType, Long ownerId, String sourceType, Long sourceId);

    void deleteRemindSharesByRemindId(Long remindId);

    void batchCreateRemindShare(List<EhRemindShares> remindShares);

    List<Remind> findRemindsByCategoryId(Long remindCategoryId);

    void deleteRemindsByCategoryId(Long remindCategoryId);

    List<Remind> findRemindsByTrackRemindIds(List<Long> trackReminIds);

    void deleteRemindsByTrackRemindId(List<Long> trackRemindIds);

    List<Remind> findSelfReminds(QuerySelfRemindsCondition condition);

    List<Remind> findShareReminds(QueryShareRemindsCondition request);

    Long createRemindDemoCreateLog(RemindDemoCreateLog remindDemoCreateLog);

    boolean checkRemindDemoHasCreated(Integer namespaceId, String ownerType, Long ownerId, Long userId);

    List<Remind> findUndoRemindsByRemindTime(Timestamp remindStartTime, Timestamp remindEndTime, int count);

	Remind getRemindById(Long trackRemindId);

	List<Remind> findUndoRemindsByRemindTimeByPage(Timestamp remindStartTime,
			Timestamp remindEndTime, int pageSize, int offset);

}
