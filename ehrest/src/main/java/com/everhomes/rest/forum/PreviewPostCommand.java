// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>postId: 帖子ID[必填]</li>
 * </ul>
 */
public class PreviewPostCommand {

    @NotNull
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
