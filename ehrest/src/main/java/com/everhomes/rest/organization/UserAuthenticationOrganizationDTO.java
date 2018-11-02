// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id:id</li>
 *     <li>organizationId: 企业ID</li>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>communityId: 项目ID</li>
 *     <li>authFlag: 是否授权，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class UserAuthenticationOrganizationDTO {

    private Long id;

    private Long organizationId;

    private Integer namespaceId;

    private Long communityId;

    private Byte authFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getAuthFlag() {
        return authFlag;
    }

    public void setAuthFlag(Byte authFlag) {
        this.authFlag = authFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
