package com.everhomes.group;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.everhomes.sharding.ShardIterator;
import com.everhomes.util.StringHelper;

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
    
    public String getHashString() {
        StringBuffer sb = new StringBuffer();
        
        if(shardIterator != null) {
            List<String> servers = shardIterator.getShardServers();
            if(servers != null) {
                servers.stream().forEach((server)->sb.append(server));
            }
        }
        
        sb.append(groupId);
        if(this.anchor != null) {
            sb.append(this.anchor);
        }
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            return StringHelper.toHexString(md5Digest.digest(sb.toString().getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5 algorithm to initialize MD5HashFunctor");
        }
    }
}
