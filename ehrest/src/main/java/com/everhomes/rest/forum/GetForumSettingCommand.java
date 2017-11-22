// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>forumId: forumId</li>
 *     <li>entryId: entryId</li>
 * </ul>
 */
public class GetForumSettingCommand {

    private Integer namespaceId;
    private Long forumId;
    private Long entryId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
