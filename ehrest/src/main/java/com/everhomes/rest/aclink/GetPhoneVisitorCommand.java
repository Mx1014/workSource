package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class GetPhoneVisitorCommand {
    Integer namespaceId;
    String phvid;

    public String getPhvid() {
        return phvid;
    }

    public void setPhvid(String phvid) {
        this.phvid = phvid;
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
