// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>currentOrgId: 当前所在机构</li>
 * <li>ownerType: 项目范围</li>
 * <li>ownerId: 项目id</li>
 * <li>forumId: 论坛ID</li>
 * <li>commentId: 评论ID</li>
 * </ul>
 */
public class DeleteCommentCommand {
    private Long currentOrgId;
    private String ownerType;
    private Long ownerId;
    private Long forumId;
    private Long commentId;
    
    public DeleteCommentCommand() {
    }

    public Long getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(Long currentOrgId) {
        this.currentOrgId = currentOrgId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
