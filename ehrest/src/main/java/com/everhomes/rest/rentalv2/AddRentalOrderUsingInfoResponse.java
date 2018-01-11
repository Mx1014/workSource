package com.everhomes.rest.rentalv2;

import com.everhomes.rest.order.CommonOrderDTO;

/**
 * <ul>
 * <li>orderDTO: orderDTO {@link com.everhomes.rest.order.CommonOrderDTO}</li>
 * <li>flowCaseUrl: flowCaseUrl</li>
 * </ul>
 */
public class AddRentalOrderUsingInfoResponse {
    private CommonOrderDTO orderDTO;

    private String flowCaseUrl;

    private Long billId;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

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
