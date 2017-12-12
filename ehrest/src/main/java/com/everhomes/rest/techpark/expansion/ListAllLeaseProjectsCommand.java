package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/10/18.
 */
public class ListAllLeaseProjectsCommand {
    private Integer namespaceId;

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
