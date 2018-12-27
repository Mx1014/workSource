package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationOwnerDTO;

import java.util.List;

/**
 * <ul>
 *  <li>nextPageAnchor: 下一页锚点</li>
 *  <li>owners: 参考{@link com.everhomes.rest.organization.OrganizationOwnerDTO}</li>
 * </ul>
 */
public class ListOrganizationOwnersResponse {
    private Long nextPageAnchor;
    @ItemType(OrganizationOwnerDTO.class)
    private List<OrganizationOwnerDTO> owners;

    private Long totalNum;

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<OrganizationOwnerDTO> getOwners() {
        return owners;
    }

    public void setOwners(List<OrganizationOwnerDTO> owners) {
        this.owners = owners;
    }
}
