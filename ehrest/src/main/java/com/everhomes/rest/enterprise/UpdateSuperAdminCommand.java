package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entries: 手机号</li>
 *     <li>organizationId: 组织id</li>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>isSigned: 是否已经注册标志</li>
 *
 * </ul>
 */
public class UpdateSuperAdminCommand {
    private String entries;
    private Long organizationId;
    private Integer namespaceId;
    private Byte isSigned;
    private Byte isJoined;
    private String contactor;

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
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

    public Byte getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(Byte isSigned) {
        this.isSigned = isSigned;
    }

    public Byte getIsJoined() {
        return isJoined;
    }

    public void setIsJoined(Byte isJoined) {
        this.isJoined = isJoined;
    }

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
