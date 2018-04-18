package com.everhomes.rest.acl.admin;

import com.everhomes.rest.organization.OrganizationContactDTO;

import java.util.List;

/**
 * <ul>
 * <li>List<OrganizationContactDTO>: 超级管理员列表</li>
 * </ul>
 */
public class ListOrganizationContactDTOResponse {

    private List<OrganizationContactDTO> organizationContactDTOList;

    public List<OrganizationContactDTO> getOrganizationContactDTOList() {
        return organizationContactDTOList;
    }

    public void setOrganizationContactDTOList(List<OrganizationContactDTO> organizationContactDTOList) {
        this.organizationContactDTOList = organizationContactDTOList;
    }
}
