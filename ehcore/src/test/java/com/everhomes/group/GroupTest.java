// @formatter:off
package com.everhomes.group;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.DbProvider;
import com.everhomes.junit.TestCaseBase;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.sharding.ShardingProvider;

public class GroupTest extends TestCaseBase {
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private GroupProvider groupProvider;

    @Ignore @Test
    public void testGroupCRUD() {
        Group group = new Group();
        group.setName("group 1");
        group.setCreatorUid(1L);
        this.groupProvider.createGroup(group);
        
        for(int i = 0; i < 100; i++) {
            GroupMember member = new GroupMember();
            member.setGroupId(group.getId());
            member.setMemberType("User");
            member.setMemberId((long)(i + 1));
            this.groupProvider.createGroupMember(member);
        }
    }
    
    @Ignore @Test
    public void testGroupMemberIteration() {
        
        CrossShardListingLocator locator = new CrossShardListingLocator(3);
        while(true) {
            List<GroupMember> members = this.groupProvider.listGroupMembers(locator, 20);
            
            for(GroupMember member: members) {
                System.out.println("Group member uid: " + member.getMemberId());
            }
            if(members.isEmpty())
                break;
        }
    }
}
