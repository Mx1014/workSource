package com.everhomes.group;

import com.everhomes.sharding.ShardIterator;

public class GroupMemberLocator {
    private ShardIterator shardIterator;
    private long groupId;
    private Long anchor;
    
    public GroupMemberLocator(long groupId) {
        this.groupId = groupId;
    }

    public ShardIterator getShardIterator() {
        return shardIterator;
    }
    
    public void setShardIterator(ShardIterator shardIterator) {
        this.shardIterator = shardIterator;
    }
    
    public long getGroupId() {
        return groupId;
    }
    
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    
    public Long getAnchor() {
        return anchor;
    }
    
    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }
}
