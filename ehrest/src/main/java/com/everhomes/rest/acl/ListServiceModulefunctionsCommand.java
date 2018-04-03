package com.everhomes.rest.acl;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>organizationId: 公司id</li>
 *     <li>moduleId: 模块id</li>
 * </ul>
 * Created by ying.xiong on 2017/12/4.
 */
public class ListServiceModulefunctionsCommand {
    private Integer namespaceId;

    private Long communityId;

    private Long organizationId;
    @NotNull
    private Long moduleId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
}
