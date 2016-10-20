package com.everhomes.group;

import java.io.Serializable;
import java.util.List;

import com.everhomes.util.StringHelper;

public class GroupMemberCaches implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5351485129060006204L;
    
    private List<GroupMember> members;
    private long tick;

    public List<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMember> members) {
        this.members = members;
    }

    public int getSize() {
        if(this.members == null) {
            return 0;
        }
        
        return this.members.size();
    }


    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
