// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: (选填)下一页锚点</li>
 * <li>visitorEnterpriseList: (必填)公司列表，{@link com.everhomes.rest.visitorsys.BaseVisitorEnterpriseDTO}</li>
 * </ul>
 */
public class ListCommunityOrganizationsResponse {
    private Long nextPageAnchor;
    @ItemType(BaseVisitorEnterpriseDTO.class)
    private List<BaseVisitorEnterpriseDTO> visitorEnterpriseList;

    public ListCommunityOrganizationsResponse(Long nextPageAnchor, List<BaseVisitorEnterpriseDTO> visitorEnterpriseList) {
        this.nextPageAnchor = nextPageAnchor;
        this.visitorEnterpriseList = visitorEnterpriseList;
    }

    public ListCommunityOrganizationsResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BaseVisitorEnterpriseDTO> getVisitorEnterpriseList() {
        return visitorEnterpriseList;
    }

    public void setVisitorEnterpriseList(List<BaseVisitorEnterpriseDTO> visitorEnterpriseList) {
        this.visitorEnterpriseList = visitorEnterpriseList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}