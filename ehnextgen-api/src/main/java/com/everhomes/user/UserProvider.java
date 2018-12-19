// @formatter:off
package com.everhomes.user;

import com.everhomes.aclink.AclinkUser;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.Organization;
import com.everhomes.rest.aclink.ListAclinkUserCommand;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.user.InvitationRoster;
import com.everhomes.rest.user.UserDTO;
import com.everhomes.rest.user.UserInvitationsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface UserProvider {
    void createUser(User user);
    //统一用户同步数据使用
    void createUserFromUnite(User user);
    void updateUser(User user);
    void updateUserFromUnite(User user);
    void deleteUser(User user);
    void deleteUser(long id);
    void deleteUserAndUserIdentifiers(Integer namespaceId, List<String> namespaceUserTokens, String namespaceUserType);
    User findUserById(long id);
    User findUserByNamespaceUserTokenAndType(String token, String type);
    User findUserByAccountName(String accountName);
    List<User> queryUsers(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
    List<User> listUsers(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
    List<UserIdentifier> listUserIdentifiersOfUser(long userId);
    UserIdentifier findUserIdentifiersOfUser(long userId, Integer namespaceId);

    void createIdentifier(UserIdentifier userIdentifier);
    //统一用户同步数据使用
    void createIdentifierFromUnite(UserIdentifier userIdentifier);
    void updateIdentifier(UserIdentifier userIdentifier);
    void updateIdentifierFromUnite(UserIdentifier userIdentifier);
    void updateIdentifierByUid(UserIdentifier userIdentifier);
    void deleteIdentifier(UserIdentifier userIdentifier);
    void deleteIdentifier(long id);
    void deleteIdentifier(Integer namespaceId, List<Long> uIds);
    UserIdentifier findIdentifierById(long id);
    List<UserIdentifier> findClaimingIdentifierByToken(String identifierToken);
    List<UserIdentifier> findClaimedIdentifiersByToken(String identifierToken);
    List<UserIdentifier> listClaimedIdentifiersByTokens(Integer namespaceId,List<String> identifiers);

    UserIdentifier findClaimedIdentifierByToken(String identifierToken);

    List<User> listAppAndWeiXinAndAlipayUserByCreateTime(Integer namespaceId, LocalDateTime start, LocalDateTime end, List<Long> excludeUIDs);
    /**根据域空间id和注册的手机号来查询对应的注册信息**/
    UserIdentifier findClaimedIdentifierByToken(Integer namespaceId, String identifierToken);
    UserIdentifier findClaimedIdentifierByOwnerAndType(long ownerId, byte identifierType);
    UserIdentifier findClaimingIdentifierByToken(Integer namespaceId, String identifierToken);
    UserIdentifier findIdentifierByOwnerAndTypeAndClaimStatus(long ownerUid, byte identifierType, byte claimStatus);

    void createUserGroup(UserGroup userGroup);
    void updateUserGroup(UserGroup userGroup);
    void deleteUserGroup(UserGroup userGroup);
    void deleteUserGroup(long ownerId, long groupId);
    UserGroup findUserGroupByOwnerAndGroup(long ownerId, long groupId);
    List<UserGroup> listUserGroups(long uid, String groupDiscriminator);
    
    void createUserLike(UserLike userLike);
    void updateUserLike(UserLike userLike);
    void deleteUserLike(UserLike userLike);
    void deleteUserLikeById(long id);
    UserLike findUserLikeById(long id);
    UserLike findUserLike(long uid, String targetType, long targetId);
    List<UserLike> listUserLikes(long uid, String targetType);
    
    
    UserIdentifier findIdentifierByVerifyCode(String code,String identifyToken);
    
    void createInvitation(UserInvitation invitations);
    
    void updateInvitation(UserInvitation invitations);
    
    UserInvitationsDTO findUserInvitationByCode(String inviteCode);
    
    void createUserInvitationRoster(UserInvitationRoster roster,Long uid);
    
    User findUserByUuid(String uuid);
    
    void cleanupZombies();
    List<InvitationRoster> listInvitationRostor(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
	List<User> listUserByKeyword(String keyword, Integer namespaceId, CrossShardListingLocator locator, int pageSize);
	List<User> listUserByKeyword(Integer isAuth, Byte gender, Long organizationId, String keyword,Byte executiveFlag, Integer namespaceId, CrossShardListingLocator locator, int pageSize);
	User findUserByNamespace(Integer namespaceId, String namespaceUserToken);
	void createUserCommunity(UserCommunity userCommunity);
	UserCommunity findUserCommunityByOwnerAndCommunity(long ownerUid, long communityId);
	List<User> findUserByNamespaceId(Integer namespaceId, CrossShardListingLocator locator, int pageSize);
	List<User> listUserByKeywords(String keyword);

    int countUserByNamespaceIdAndGender(Integer namespaceId, Byte gender);

    int countUserByNamespaceId(Integer namespaceId, Boolean isAuth);
	List<User> listUserByNickNameOrIdentifier(String keyword);
	List<UserIdentifier> listUserIdentifierByIdentifier(String identifier);
	List<User> listUserByIds(Integer namespaceId, List<Long> userIds);

    List<User> searchUserByIdentifier(String identifier, Integer namespaceId, int pageSize);

    /**
	 * Added by Janson
	 * @param locator
	 * @param pageSize
	 * @return
	 */
    List<AclinkUser> searchDoorUsers(ListAclinkUserCommand cmd, CrossShardListingLocator locator, int pageSize);
    
    /**
     * 有些用户来源于第三方（比如华为、微信等），这些用户没有密码等，是使用namespace_id、namespace_user_token、namespace_user_type来区分
     * @param namespaceId 域空间
     * @param userType 域空间下用户类型
     * @param userToken 第三方系统的用户标识
     * @return
     */
    List<User> findThirdparkUserByTokenAndType(Integer namespaceId, String userType, String userToken);
    List<User> listUserByNamespace(String keyword, Integer namespaceId, CrossShardListingLocator locator, int pageSize);
	List<User> listUserByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, Integer pageSize);
	List<User> listUserByUpdateTime(Integer namespaceId, Long timestamp, Integer pageSize);
	Organization findAnyUserRelatedOrganization(Long id, Integer namespaceId);

    List<User> listUserByNickName(String keyword);
    List<UserGroup> listUserActiveGroups(long uid, String groupDiscriminator);

    /**
     * 查询用户免打扰设置
     * @param ownerType
     * @param ownerId
     * @param targetType
     * @param targetId
     * @return
     */
    UserNotificationSetting findUserNotificationSetting(String ownerType, Long ownerId, String targetType, Long targetId);

    /**
     * 修改免打扰设置
     * @param setting
     */
    void updateUserNotificationSetting(UserNotificationSetting setting);

    /**
     * 创建免打扰记录
     * @param setting
     * @return  返回该记录的id
     */
    long createUserNotificationSetting(UserNotificationSetting setting);


    List<TargetDTO> findUesrIdByNameAndAddressId(String targetName, List<Long> ids, String tel);

    TargetDTO findUserByTokenAndName(String tel, String targetName);
	
	TargetDTO findUserByToken(String tel,Integer namespaceId);
	
	TargetDTO findUserTargetById(Long userId) ;

    /**
     * 查询非当前userId的正常用户数据
     * @param namespaceId
     * @param identifierToken
     * @param userId
     * @return
     */
    UserIdentifier findClaimedIdentifierByTokenAndNotUserId(Integer namespaceId, String identifierToken, Long userId);

    int countUserByNamespaceIdAndNamespaceUserType(Integer namespaceId, String namespaceUserType);

    /**
     * 用于测试缓存使用是否正常，不要用于业务使用 by lqs 20171019
     */
    String checkCacheStatus();
    
    /**
     * 用于测试缓存使用是否正常，不要用于业务使用 by lqs 20171019
     */
    void updateCacheStatus();

    List<Long> listUsersByNamespaceUserInfo(Integer namespaceId, List<String> namespaceUserTokens, String namespaceUserType);
	
	String findMobileByUid(Long contactId);

    /**
     * 根据UserId来查询用户信息
     * @param userId
     * @return
     */
    UserDTO findUserInfoByUserId(Long userId);

    /**
     * 根据手机号查询用户信息
     * @param namespaceId
     * @param identifierTokens
     * @return
     */
    List<UserDTO> listUserInfoByIdentifierToken(Integer namespaceId, List<String> identifierTokens);
    /**
     * 查询该手机号是否已经进行注册
     * @param contactToken
     * @return
     */
    UserIdentifier getUserByToken(String contactToken,Integer namespaceId);

    /**
     * 根据手机号和域空间来查询eh_user_identifiers表中的数据
     * @param identifierToken
     * @param namespaceId
     * @return
     */
    UserIdentifier findClaimedIdentifierByTokenAndNamespaceId(String identifierToken,Integer namespaceId);

    /**
     * 根据ownerUid和namespaceId来查询手机号
     * @param ownerUid
     * @param namespaceId
     * @return
     */
    String findContactTokenByOwnerUidAndNamespaceId(Long ownerUid , Integer namespaceId);
    String findUserTokenOfUser(Long userId);


    Integer countAppAndWeiXinAndAlipayUserByCreateTime(Integer namespaceId, LocalDateTime start, LocalDateTime end, List<Long> excludeUIDs);

    String getNickNameByUid(Long creatorUid);

}
