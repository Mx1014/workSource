// @formatter:off
package com.everhomes.user;

import java.util.List;

import com.everhomes.aclink.AclinkUser;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.ListAclinkUserCommand;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.user.InvitationRoster;
import com.everhomes.rest.user.UserInvitationsDTO;

public interface UserProvider {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
    void deleteUser(long id);
    User findUserById(long id);
    User findUserByAccountName(String accountName);
    List<User> queryUsers(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);
    
    List<UserIdentifier> listUserIdentifiersOfUser(long userId);

    void createIdentifier(UserIdentifier userIdentifier);
    void updateIdentifier(UserIdentifier userIdentifier);
    void deleteIdentifier(UserIdentifier userIdentifier);
    void deleteIdentifier(long id);
    UserIdentifier findIdentifierById(long id);
    List<UserIdentifier> findClaimingIdentifierByToken(String identifierToken);
    List<UserIdentifier> findClaimedIdentifiersByToken(String identifierToken);
    UserIdentifier findClaimedIdentifierByToken(String identifierToken);
    UserIdentifier findClaimedIdentifierByToken(Integer namespaceId, String identifierToken);
    UserIdentifier findClaimedIdentifierByOwnerAndType(long ownerId, byte identifierType);
    
    void createUserGroup(UserGroup userGroup);
    void updateUserGroup(UserGroup userGroup);
    void deleteUserGroup(UserGroup userGroup);
    void deleteUserGroup(long ownerId, long groupId);
    UserGroup findUserGroupByOwnerAndGroup(long ownerId, long groupId);
    List<UserGroup> listUserGroups(long uid, String groupDiscriminator);
    
    void createUserLike(UserLike userLike);
    void updateUserLike(UserLike userLike);
    void deleteUserLike(UserLike userLike);    void deleteUserLikeById(long id);
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
	List<User> listUserByKeyword(String keyword,Integer namespaceId, CrossShardListingLocator locator, int pageSize);
	User findUserByNamespace(Integer namespaceId, String namespaceUserToken);
	void createUserCommunity(UserCommunity userCommunity);
	UserCommunity findUserCommunityByOwnerAndCommunity(long ownerUid, long communityId);
	List<User> findUserByNamespaceId(Integer namespaceId, CrossShardListingLocator locator, int pageSize);
	List<User> listUserByKeywords(String keyword);
	
	int countUserByNamespaceId(Integer namespaceId, Boolean isAuth);
	List<User> listUserByNickNameOrIdentifier(String keyword);
	List<UserIdentifier> listUserIdentifierByIdentifier(String identifier);
	List<User> listUserByIds(Integer namespaceId, List<Long> userIds);
	
	/**
	 * Added by Janson
	 * @param namespaceId
	 * @param organizationId
	 * @param buildingId
	 * @param isAuth
	 * @param keyword
	 * @param locator
	 * @param pageSize
	 * @return
	 */
    List<AclinkUser> searchDoorUsers(ListAclinkUserCommand cmd, CrossShardListingLocator locator, int pageSize);
	List<User> listMatchedUser(String nickName);
}
