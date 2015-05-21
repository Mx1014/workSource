package com.everhomes.user;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *uid:用户ID
 */
public class GetUserSnapshotInfoCommand {
    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
