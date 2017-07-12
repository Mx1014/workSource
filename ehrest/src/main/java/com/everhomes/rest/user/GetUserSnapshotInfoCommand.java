package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>uid:用户ID</li>
 *     <li>uuid:用户唯一的标识</li>
 * </ul>
 */
public class GetUserSnapshotInfoCommand {
    private Long uid;
    private String uuid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
