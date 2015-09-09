package com.everhomes.forum;

import java.util.Comparator;

public class ForumPostComparator implements Comparator<Post> {
    @Override
    public int compare(Post post1, Post post2) {
        // if post1 is null then should put post2 ahead of post1
        if(post1 == null) {
            return 1;
        } else {
            if(post2 == null) {
                return -1;
            } else {
                long seq1 = post1.getModifySeq();
                long seq2 = post2.getModifySeq();
                if(seq1 == seq2) {
                    return 0;
                } else {
                    if(seq1 > seq2) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        }
    }

}
