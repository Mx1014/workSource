package com.everhomes.rest.rentalv2;

import com.everhomes.rest.order.CommonOrderDTO;

/**
 * @author sw on 2017/12/28.
 */
public class AddRentalOrderUsingInfoResponse {
    private CommonOrderDTO orderDTO;

    private String flowCaseUrl;

    public CommonOrderDTO getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(CommonOrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    public String getFlowCaseUrl() {
        return flowCaseUrl;
    }

    public void setFlowCaseUrl(String flowCaseUrl) {
        this.flowCaseUrl = flowCaseUrl;
    }
}
