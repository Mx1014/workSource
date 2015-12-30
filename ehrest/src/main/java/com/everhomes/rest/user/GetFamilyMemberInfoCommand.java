package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians 
 * uid:用户Id
 * uuid:用户唯一的标识
 */
public class GetFamilyMemberInfoCommand {
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
