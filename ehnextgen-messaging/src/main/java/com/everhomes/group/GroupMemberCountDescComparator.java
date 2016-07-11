package com.everhomes.group;

import java.util.Comparator;

public class GroupMemberCountDescComparator implements Comparator<Group> {
    @Override
    public int compare(Group g1, Group g2) {
        Long memberCount1 = g1.getMemberCount();
        Long memberCount2 = g2.getMemberCount();
        
        if(memberCount1 == null) {
            if(memberCount2 == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if(memberCount2 == null) {
                return -1;
            } else {
                if(memberCount1.longValue() > memberCount2.longValue()) {
                    return -1;
                } else {
                    if(memberCount1.longValue() == memberCount2.longValue()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            }
        }
    }
    
}
