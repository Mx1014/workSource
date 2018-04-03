package com.everhomes.rest.customer;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>dtos: 参考{@link EnterpriseCustomerDTO}</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListNearbyEnterpriseCustomersCommandResponse {

    @ItemType(EnterpriseCustomerDTO.class)
    private List<EnterpriseCustomerDTO> dtos;

    private Long nextPageAnchor;

    public List<EnterpriseCustomerDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<EnterpriseCustomerDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
