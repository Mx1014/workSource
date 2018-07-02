package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>contactToken：手机号</li>
 * <li>organizationId：公司id</li>
 * </ul>
 */
public class GetPersonelInfoByTokenCommand {
    private String contactToken;
    private Long organizationId;
    private Integer namespaceId;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
