// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: (选填)下一页锚点</li>
 * <li>visitorEnterpriseList: (必填)公司+办公地点列表，{@link com.everhomes.rest.visitorsys.BaseEnterpriseAndLocationDTO}</li>
 * </ul>
 */
public class ListCommunityOrganizationsResponse {
    private Long nextPageAnchor;
    @ItemType(BaseEnterpriseAndLocationDTO.class)
    private List<BaseEnterpriseAndLocationDTO> enterpriseAndLocationList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BaseEnterpriseAndLocationDTO> getEnterpriseAndLocationList() {
        return enterpriseAndLocationList;
    }

    public void setEnterpriseAndLocationList(List<BaseEnterpriseAndLocationDTO> enterpriseAndLocationList) {
        this.enterpriseAndLocationList = enterpriseAndLocationList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}