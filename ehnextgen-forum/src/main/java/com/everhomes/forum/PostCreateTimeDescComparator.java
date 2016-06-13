package com.everhomes.forum;

import java.util.Comparator;

public class PostCreateTimeDescComparator implements Comparator<Post> {

    @Override
    public int compare(Post post1, Post post2) {
        long createTime1 = post1.getCreateTime().getTime();
        long createTime2 = post1.getCreateTime().getTime();
        
        if(createTime2 > createTime1) {
            return 1;
        } else {
            if(createTime2 == createTime1) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}
