// @formatter:off
package com.everhomes.group;

import java.util.List;

public interface GroupServiceProvider {
    void createGroup(Group group);
    void updateGroup(Group group);
    void deleteGroup(Group group);
    void deleteGroup(long id);
    Group findGroupById(long id);
    
    void createGroupMember(GroupMember groupMember);
    void updateGroupMember(GroupMember groupMember);
    void deleteGroupMember(GroupMember groupMember);
    void deleteGroupMemberById(long id);
    GroupMember findGroupMemberById(long id);
    GroupMember findGroupMemberByMemberInfo(long groupId, String memberType, long memberId);
    
    List<GroupMember> listGroupMembers(GroupMemberLocator locator, int count);
}
