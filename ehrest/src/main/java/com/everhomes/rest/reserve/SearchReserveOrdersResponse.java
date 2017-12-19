package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/12/18.
 */
public class SearchReserveOrdersResponse {
    private Long nextPageAnchor;

    private List<ReserveOrderDTO> orderDTOS;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ReserveOrderDTO> getOrderDTOS() {
        return orderDTOS;
    }

    public void setOrderDTOS(List<ReserveOrderDTO> orderDTOS) {
        this.orderDTOS = orderDTOS;
    }
}
