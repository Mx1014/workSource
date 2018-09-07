package com.everhomes.rest.user;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: 名字空间ID</li>
 *     <li>orgId: 企业ID</li>
 * </ul>
 */
public class GetTopAdministratorCommand {

    private Integer namespaceId ;

    @NotNull
    private Long orgId ;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
