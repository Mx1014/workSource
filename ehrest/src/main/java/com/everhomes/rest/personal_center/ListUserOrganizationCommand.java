// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>userId: 用户ID</li>
 *     <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class ListUserOrganizationCommand {
    private Long userId;
    private Integer namespaceId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
