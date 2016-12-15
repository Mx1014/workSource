package com.everhomes.activity;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Operator;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.category.CategoryAdminStatus;

public interface ActivityProivider {
    Activity findActivityById(Long id);

    void createActity(Activity activity);

    ActivityRoster cancelSignup(Activity activity, Long userId, Long familyId);

    void updateActivity(Activity activity);

    ActivityRoster checkIn(Activity activity, Long uid, Long familyId);

    void createActivityRoster(ActivityRoster createRoster);

    void updateRoster(ActivityRoster roster);

    ActivityRoster findRosterById(Long rosterId);

    ActivityRoster findRosterByUidAndActivityId(Long activityId, Long uid);

    List<ActivityRoster> listRosterPagination(CrossShardListingLocator locator, int count, Long activityId);

    List<ActivityRoster> listRosters(Long activityId);

    Activity findSnapshotByPostId(Long postId);

    void deleteRoster(ActivityRoster createRoster);

    List<Activity> listActivities(CrossShardListingLocator locator, int count, Condition condition);
    
    Activity findActivityByUuid(String uuid);
    
    List<ActivityRoster> findRostersByUid(Long uid, CrossShardListingLocator locator, int count);
    
    List<Activity> listNewActivities(CrossShardListingLocator locator, int count, Timestamp lastViewedTime, Condition condition);

	List<Activity> listActivitiesForWarning(Integer namespaceId, Timestamp queryStartTime, Timestamp queryEndTime);
	
	List<ActivityCategories> listActivityEntryCategories(Integer namespaceId, String ownerType, Long ownerId, Long parentId, CategoryAdminStatus status);

    ActivityCategories findActivityCategoriesById(Long id);

	List<Activity> listActivityByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

	List<Activity> listActivityByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

	List<ActivityRoster> listActivitySignupByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize);

	List<ActivityRoster> listActivitySignupByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);
}
