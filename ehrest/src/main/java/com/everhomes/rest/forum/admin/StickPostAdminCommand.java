// @formatter:off
package com.everhomes.rest.forum.admin;

import com.everhomes.rest.forum.StickFlag;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>postId: postId</li>
 *     <li>stickFlag: 置顶标志，0-否，1-是，参考{@link StickFlag}</li>
 * </ul>
 */
public class StickPostAdminCommand {
    private Long postId;
    private Byte stickFlag;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Byte getStickFlag() {
        return stickFlag;
    }

    public void setStickFlag(Byte stickFlag) {
        this.stickFlag = stickFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
