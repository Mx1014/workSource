package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: 小区id</li>
 *     <li>name: 小区名称</li>
 *     <li>organizations: 企业列表 {@link OrganizationDetailDTO}</li>
 * </ul>
 */
public class CommunityOrganizationTreeDTO {

    private Long id;
    private String name;
    @ItemType(OrganizationDetailDTO.class)
    private List<OrganizationDetailDTO> organizations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrganizationDetailDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationDetailDTO> organizations) {
        this.organizations = organizations;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
