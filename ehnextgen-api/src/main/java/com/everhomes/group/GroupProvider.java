// @formatter:off
package com.everhomes.group;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.group.GroupDiscriminator;

public interface GroupProvider {
    void createGroup(Group group);
    void updateGroup(Group group);
    void deleteGroup(Group group);
    void deleteGroup(long id);
    Group findGroupById(long id);
    Group findGroupByUuid(String uuid);
    List<Group> findGroupByCreatorId(long creatorId);
    
    List<Group> queryPulbicGroups(int maxCount, ListingQueryBuilderCallback callback);
    List<Group> queryGroups(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback callback);
    /**
     * <p>遍历符合条件的group。</p>
     * <p>分批从数据库中读取group信息，每批数量为<code>count</code>，可以指定<code>discriminator</code>进行过滤，
     * 对取出的每个group信息，都会调用<code>callback</code>进行处理，调用者可通过定义callback对取出的信息进一步处理。</p>
     * @param count 每批读取的数量，必须大于0；
     * @param discriminator group的过滤条件，参考{@link com.everhomes.group.GroupDiscriminator}，
     * 			如果不指定，则遍历不区分<code>discriminator</code>进行遍历；
     * @param callback 回调处理
     */
    void iterateGroups(int count, GroupDiscriminator discriminator, IterateGroupCallback callback);
    
    void createGroupMember(GroupMember groupMember);
    void updateGroupMember(GroupMember groupMember);
    void deleteGroupMember(GroupMember groupMember);
    void deleteGroupMemberById(long id);
    GroupMember findGroupMemberById(long id);
    GroupMember findGroupMemberByUuid(String uuid);
    GroupMember findGroupMemberByMemberInfo(long groupId, String memberType, long memberId);
    List<GroupMember> findGroupMemberByGroupId(long groupId);
    
    List<GroupMember> listGroupMembers(ListingLocator locator, int count);
    List<GroupMember> queryGroupMembers(ListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    void iterateGroupMembers(int count, long groupId, ListingQueryBuilderCallback queryBuilderCallback, 
        IterateGroupMemberCallback callback);
    
    void createGroupVisibleScope(GroupVisibilityScope scope);
    void updateGroupVisibleScope(GroupVisibilityScope scope);
    void deleteGroupVisibleScope(GroupVisibilityScope scope);
    void deleteGroupVisibleScopeById(long id);
    GroupVisibilityScope findGroupVisibleScopeById(long id);
    
    List<GroupVisibilityScope> listGroupVisibilityScopes(long groupId);
    
    void createGroupOpRequest(GroupOpRequest request);
    void updateGroupOpRequest(GroupOpRequest request);
    void deleteGroupOpRequest(GroupOpRequest request);
    void deleteGroupOpRequestById(long id);
    GroupOpRequest findGroupOpRequestById(long id);
    GroupOpRequest findGroupOpRequestByRequestor(long groupId, long requestorUid);
    
    List<GroupOpRequest> queryGroupOpRequests(ListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    /**
     * <p>应用于没有<code>groupId</code>而需要使用MapReduce遍历所有分区查找符合条件的GroupOpRequest的场合。</p>
     * @param locator 包含本页开始的锚点
     * @param count 本页的数量
     * @param queryBuilderCallback 查询条件
     * @return GroupOpRequest对象列表
     */
    List<GroupOpRequest> queryGroupOpRequestsByMapReduce(ListingLocator locator, int count, 
        ListingQueryBuilderCallback queryBuilderCallback);
    
    List<GroupMember> listGroupMemberByGroupIds(List<Long> groupIds, ListingLocator locator, Integer pageSize,  ListingQueryBuilderCallback queryBuilderCallback);
    
    List<Group> listGroupByCommunityId(Long communityId, ListingQueryBuilderCallback queryBuilderCallback);
	GroupMember findGroupMemberTopOne(Long groupId);
    GroupMemberCaches listGroupMessageMembers(Integer namespaceId, ListingLocator locator, int pageSize);
    void evictGroupMessageMembers(Integer namespaceId, ListingLocator locator, int pageSize);

    List<GroupMember> listPublicGroupMembersByStatus(Long groupId, String keyword, Byte status, Long from, int pageSize,
                                                     boolean includeCreator, Long creatorId);

    List<GroupMember> listPublicGroupMembersByStatus(Long groupId, Byte status, Long from, int pageSize, boolean includeCreator, Long creatorId);

    List<GroupMember> searchPublicGroupMembersByStatus(Long groupId, String keyword, Byte status, Long from, int pageSize);
	GroupMemberLog findGroupMemberLogByGroupMemberId(Long groupMemberId);
	void createGroupMemberLog(GroupMemberLog groupMemberLog);
}
