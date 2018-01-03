package com.everhomes.rest.share;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>postId: postId</li>
 * </ul>
 */
public class PostShareData {

    private Long postId;

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
