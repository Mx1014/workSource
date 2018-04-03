// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>forumId: forumId</li>
 * </ul>
 */
public class ListForumCategoryCommand {

    private Long forumId;

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
