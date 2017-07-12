package com.everhomes.group;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

/**
 * Created by xq.tian on 2017/7/11.
 */
public interface GroupMemberLogProvider {

    GroupMemberLog findGroupMemberLogByGroupMemberId(Long groupMemberId);

    void createGroupMemberLog(GroupMemberLog groupMemberLog);

    List<GroupMemberLog> queryGroupMemberLog(String userInfoKeyword, String communityKeyword, List<Long> communityIds, Byte status, CrossShardListingLocator locator, int pageSize);
}
