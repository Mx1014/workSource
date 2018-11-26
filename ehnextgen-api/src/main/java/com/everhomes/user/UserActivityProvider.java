package com.everhomes.user;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.user.UserFavoriteDTO;
import org.jooq.Condition;

import java.util.List;
import java.util.Set;

public interface UserActivityProvider {
    List<User> listUsers(List<Long> uids);

    List<UserContact> listContactByUid(Long uid, ListingLocator locator, int count);

    List<UserContact> listContacts(Long uid);

    List<UserInvitation> listInvitationByUid(Long uid, CrossShardListingLocator locator, int count);

    List<UserInvitationRoster> listInvitationRoster(Set<Long> invitationIds);

    void addContacts(List<UserContact> contacts,Long uid);

    void updateContact(List<UserContact> contacts,Long uid);

    void addInstalledApp(List<UserInstalledApp> insApps,Long uid);

    void addBehavior(UserBehavior behavior,Long uid);

    void addActivity(UserActivity activity,Long uid);

    void addLocation(UserLocation location,Long uid);
    
    List<UserLocation> findLocation(Long uid);

    List<UserContact> listRetainUserContactByUid(Long uid);

    List<UserIdentifier> listUserIdentifiers(List<String> indentifierTokens);

    void addUserProfile(UserProfile userProfile);

    List<UserProfile> findProfileByUid(Long uid);

    List<User> listUnAuthUsersByProfileCommunityId(Integer namespaceId, Long communityId, Long anchor, int pagesize,  Byte CommunityType, Byte userSourceType, String keywords);

    List<User> listUnAuthUsersByProfileCommunityId(Integer namespaceId, Long communityId, Long anchor, int pagesize, Byte communityType, Byte userSourceType, String keywords, Long startTime, Long endTime);

    void updateUserProfile(UserProfile userProfile);

    void deleteProfile(UserProfile userProfile);

    UserProfile findUserProfileBySpecialKey(Long uid, String key);

    void updateUserProfile(Long uid, String key, String... content);
    
    void updateUserCurrentEntityProfile(Long uid, String key, Long entityId, Long timestemp, Integer namespaceId);

    void addFeedback(Feedback feedback,Long uid);
    
    List<Feedback> ListFeedbacks(CrossShardListingLocator locator, Integer namespaceId, Byte targetType, Byte status, int pageSize);

    List<Feedback> ListFeedbacksByNamespaceId(Integer namespaceId);

    void updateFeedback(Feedback feedback);
    
    void updateOtherFeedback(Long targetId, Long feedbackId, Byte verifyType, Byte handleType);
    
    Feedback findFeedbackById(Long id);

    List<UserFavoriteDTO> findFavorite(Long uid);
    
    List<UserFavoriteDTO> findFavorite(Long uid, String targetType, Long targetId);
    
    List<UserFavoriteDTO> findFavorite(Long uid, String targetType, CrossShardListingLocator locator, int count);

    void addUserFavorite(UserFavorite userFavorite);

    void addPostedTopic(Long ownerUid, String targetType, Long postId);

    List<UserPost> listPostedTopics(Long uid);
    List<UserPost> listPostedTopics(Long uid, String targetType, CrossShardListingLocator locator, int count);
    void updateProfileIfNotExist(Long uid, String key, Integer val);
    
    void deleteFavorite(Long uid,Long targetId,String type);

    void addUserServiceAddress(UserServiceAddress serviceAddress);

    List<UserServiceAddress> findUserRelateServiceAddresses(long uid);
    
    void addUserShop(Long uid);
    
    void deleteShop(Long uid);

    void deleteUserServieAddress(Long addressId,Long uid);

    int deletePostedTopic(Long ownerId, Long postId);
    
    void updateViewedActivityProfileIfNotExist(Long uid, String key, Long lastViewedTime, List<Long> ids);
    
    List<SearchTypes> listByNamespaceId(Integer namespaceId);
	SearchTypes findByContentAndNamespaceId(Integer namespaceId, String contentType);
	
	void createSearchTypes(SearchTypes searchType);
	void deleteSearchTypes(Long id);
    RequestTemplates getCustomRequestTemplate(String templateType);
    RequestTemplates getCustomRequestTemplate(Long id);
    List<RequestTemplatesNamespaceMapping> getRequestTemplatesNamespaceMappings(Integer namespaceId);
    List<RequestTemplates> listCustomRequestTemplates();
    List<RequestAttachments> listRequestAttachments(String ownerType, Long ownerId);
    void createRequestAttachments(RequestAttachments attachment);

	List<StatActiveUser> listActiveStats(Long beginDate, Long endDate, Integer namespaceId, CrossShardListingLocator locator, int i);

	StatActiveUser findStatActiveUserByDate(java.sql.Date date, Integer namespaceId);

	void createStatActiveUser(StatActiveUser stat);

    List<UserActivity> listUserActivetys(ListingLocator locator, Integer count, ListingQueryBuilderCallback callback);

    List<UserActivity> listUserActivetys(Long userId, Integer pageSize);

    UserActivity findLastUserActivity(Long uid);

    List<User> listNotInUserActivityUsers(Integer namespaceId);

    void addActivities(List<UserActivity> activityList);

    void deleteUserActivity(UserActivity activity);

    List<VipPriority> listVipPriorityByNamespaceId(Integer namespaceId);
}
