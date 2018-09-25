package com.everhomes.activity;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.activity.ActivityAttachmentDTO;
import com.everhomes.rest.activity.ActivityRosterStatus;
import org.jooq.Condition;
import org.jooq.Operator;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.user.UserGender;

public interface ActivityProivider {
    Activity findActivityById(Long id);

    void createActity(Activity activity);

    ActivityRoster cancelSignup(Activity activity, Long userId, Long familyId);

    void updateActivity(Activity activity);

    ActivityRoster checkIn(Activity activity, Long uid, Long familyId);

    void createActivityRoster(ActivityRoster createRoster);

    void updateRoster(ActivityRoster roster);

    ActivityRoster findRosterById(Long rosterId);

    ActivityRoster findRosterByUidAndActivityId(Long activityId, Long uid, Byte status);
    
    ActivityRoster findRosterByPhoneAndActivityId(Long activityId, String phone, Byte status);
    
    ActivityRoster findRosterByOrderNo(String orderNo);

    ActivityRoster findRosterByPayOrderId(Long payOrderId);

    List<ActivityRoster> listRosterPagination(CrossShardListingLocator locator, int count, Long activityId, boolean onlyConfirm);

    List<ActivityRoster> listRosters(Long activityId, ActivityRosterStatus status);
    
    /**
     * 按条件统计报名人数 add by yanjun 20170502
     * @param activityId
     * @param flagCondition
     * @return
     */
    Integer countActivityRosterByCondition(Long activityId, Condition flagCondition);

    Activity findSnapshotByPostId(Long postId);

    void deleteRoster(ActivityRoster createRoster);

    List<Activity> listActivities(CrossShardListingLocator locator, int count, Condition condition, Boolean orderByCreateTime, Byte needTemporary);
    
    Activity findActivityByUuid(String uuid);
    
    List<ActivityRoster> findRostersByUid(Long uid, CrossShardListingLocator locator, int count, Byte rosterStatus);
    
    List<Activity> listNewActivities(CrossShardListingLocator locator, int count, Timestamp lastViewedTime, Condition condition);

	List<Activity> listActivitiesForWarning(Integer namespaceId, Long categoryId, Timestamp queryStartTime, Timestamp queryEndTime);
	
	List<ActivityCategories> listActivityEntryCategories(Integer namespaceId, String ownerType, Long ownerId, Long parentId, CategoryAdminStatus status);

    ActivityCategories findActivityCategoriesById(Long id);
    
    ActivityCategories findActivityCategoriesByEntryId(Long entryId, Integer namespaceId);

    void createActivityAttachment(ActivityAttachment attachment);

    void updateActivityAttachment(ActivityAttachment attachment);

    ActivityAttachment findByActivityAttachmentId(Long id);

    void deleteActivityAttachment(Long id);

    List<ActivityAttachment> listActivityAttachments(CrossShardListingLocator locator, int count, Long activityId);

    boolean existActivityAttachments(Long activityId);

    void createActivityGoods(ActivityGoods goods);

    void updateActivityGoods(ActivityGoods goods);

    void deleteActivityGoods(Long id);

    ActivityGoods findActivityGoodsById(Long id);

    List<ActivityGoods> listActivityGoods(CrossShardListingLocator locator, int count, Long activityId);

	List<Activity> listActivityByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<Activity> listActivityByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	List<ActivityRoster> listActivitySignupByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize);

	List<ActivityRoster> listActivitySignupByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	List<ActivityCategories> listActivityCategory(Integer namespaceId, Long categoryId);

	List<ActivityRoster> listActivityRoster(Long activityId, Long pageAnchor, int pageSize);
	
	List<ActivityRoster> listActivityRoster(Long activityId, Long excludeUserId, Integer status, Integer cancelStatus, Integer offset, int pageSize);
	
	Integer countActivityRoster(Long activityId, Integer status);
	
	
	Integer countActivity(Integer namespaceId, Long categoryId, Long contentCategoryId, Timestamp startTime, Timestamp endTime, boolean isDelete);
	
	Integer countActivityRoster(Integer namespaceId, Long categoryId, Long contentCategoryId, Timestamp startTime, Timestamp endTime, UserGender userGender, boolean isCancel);
	
	List<Activity> statisticsActivity(Integer namespaceId, Long categoryId, Long contentCategoryId, Long startTime, Long endTime, String tag);
	
	/**
	 * 返回值object[]的格式如下：{Long, Integer} - {活动Id，报名人数}
	 * @return
	 */
	List<Object[]> statisticsRosterPay(List<Long> activityIds);
	
	/**
	 * 返回值object[]的格式如下：{String, Integer} - {标签名称，报名人数}
	 * @return
	 */
	List<Object[]> statisticsRosterTag(Integer namespaceId, Long categoryId, Long contentCategoryId);
	
	/**
	 * 返回值object[]的格式如下：{String, Integer} - {标签名称，报名活动数}
	 * @return
	 */
	List<Object[]> statisticsActivityTag(Integer namespaceId, Long categoryId, Long contentCategoryId);
	
	/**
	 * 返回值object[]的格式如下：{Long, String, Integer, Integer} - {机构Id， 机构名称， 报名人数，报名活动数}
	 * @return
	 */
	List<Object[]> statisticsOrganization(Integer namespaceId, Long categoryId, Long contentCategoryId);
	
	List<ActivityRoster> findExpireRostersByActivityId(Long activityId);
	
	List<Long> listActivityIds();

	/**
	 * 新建ActivityCategories
	 * @param activityCategory
	 */
	void createActivityCategories(ActivityCategories activityCategory);

	/**
	 * 更新ActivityCategories
	 * @param activityCategory
	 */
	void updateActivityCategories(ActivityCategories activityCategory);

	/**
	 * 获取当前域空间最大的EntryId
	 * @param namespaceId
	 * @return
	 */
	Long findActivityCategoriesMaxEntryId(Integer namespaceId);

	/**
	 * 删除ActivityCategories
	 * @param id
	 */
	void deleteActivityCategories(Long id);


	ActivityBizPayee getActivityPayee(Long categoryId, Integer namespaceId);

	void CreateActivityPayee(ActivityBizPayee activityBizPayee);

	void updateActivityPayee(ActivityBizPayee activityBizPayee);

	List<ActivityRoster> listActivityRosterByOrganizationId(Long organizationId, Integer namespaceId, Long pageAnchor, int pageSize);
	
//	void createActivityRosterError(ActivityRosterError rosterError);
//
//	List<ActivityRosterError> listActivityRosterErrorByJobId(Long jobId);

}
