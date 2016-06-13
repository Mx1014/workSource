// @formatter:off
package com.everhomes.rest.forum.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forumId: 论坛ID</li>
 * <li>commentId: 评论ID</li>
 * </ul>
 */
public class DeleteCommentAdminCommand {
    private Long forumId;
    private Long commentId;
    
    public DeleteCommentAdminCommand() {
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
