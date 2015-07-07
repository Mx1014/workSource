package com.everhomes.launchpad;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为post时点击item需要的参数
 * <li>forumId: 论坛id</li>
 * <li>postId: 帖子id</li>
 * </ul>
 */
public class LaunchPadPostActionData {
    //{"postId": 1,"fourmId":1}  
    private Long forumId;
    private Long postId;
    
    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
