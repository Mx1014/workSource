package com.everhomes.rest.acl.admin;

import com.everhomes.rest.organization.OrganizationContactDTO;

import java.util.List;

/**
 * <ul>
 * <li>dtos: 系统管理员列表(标准版)</li>
 * <li>nextPageAnchor: 下一个锚点</li>
 * </ul>
 */
public class ListOrganizationContectDTOResponse {

    private List<OrganizationContactDTO> dtos;

    private Long nextPageAnchor;

    public List<OrganizationContactDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<OrganizationContactDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
