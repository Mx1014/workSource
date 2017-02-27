package com.everhomes.search;

import com.everhomes.group.GroupMember;
import com.everhomes.rest.family.GroupMemberQueryResult;
import com.everhomes.rest.family.SearchGroupMemberCommand;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */
public interface UserFamilySearcher {
    void deleteById(Long id);
    void bulkUpdate(List<GroupMember> members);
    void feedDoc(GroupMember member);
    void syncFromDb();
    GroupMemberQueryResult query(SearchGroupMemberCommand cmd);
}
