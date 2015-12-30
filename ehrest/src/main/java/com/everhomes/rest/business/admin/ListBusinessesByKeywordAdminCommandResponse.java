package com.everhomes.rest.business.admin;


import java.util.List;




import com.everhomes.rest.business.BusinessDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>requests: 商家列表，参考{@link com.everhomes.rest.business.admin.BusinessAdminDTO}</li>
 * <li>nextPageOffset: 下一页页码</li>
 * </ul>
 */

public class ListBusinessesByKeywordAdminCommandResponse{
    @ItemType(BusinessAdminDTO.class)
    private List<BusinessAdminDTO> requests;
    private Integer nextPageOffset;

    public List<BusinessAdminDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<BusinessAdminDTO> requests) {
        this.requests = requests;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
