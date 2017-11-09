package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;


/**
 * <ul>
 *     <li>tag: 标签</li>
 *     <li>forumEntryId: 论坛应用入口Id</li>
 * </ul>
 */
public class PostListActionData implements Serializable {
    private String tag;

    private Long forumEntryId;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getForumEntryId() {
        return forumEntryId;
    }

    public void setForumEntryId(Long forumEntryId) {
        this.forumEntryId = forumEntryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
