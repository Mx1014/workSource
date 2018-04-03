package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>members：机构成员信息，参考{@link com.everhomes.rest.organization.OrganizationMemberV2DTO}</li>
 * </ul>
 */
public class ListPersonnelsV2CommandResponse{

    private Integer nextPageOffset;

    private Long nextPageAnchor;

    @ItemType(OrganizationMemberV2DTO.class)
    private List<OrganizationMemberV2DTO> members;

    public ListPersonnelsV2CommandResponse() {
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<OrganizationMemberV2DTO> getMembers() {
        return members;
    }

    public void setMembers(List<OrganizationMemberV2DTO> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
