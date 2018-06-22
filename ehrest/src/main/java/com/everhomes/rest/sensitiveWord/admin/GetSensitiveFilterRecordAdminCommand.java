// @formatter:off
package com.everhomes.rest.sensitiveWord.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 日志记录ID</li>
 * </ul>
 */
public class GetSensitiveFilterRecordAdminCommand {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
