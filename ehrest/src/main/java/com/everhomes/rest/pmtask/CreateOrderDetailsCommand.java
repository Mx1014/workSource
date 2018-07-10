package com.everhomes.rest.pmtask;

import java.util.List;


/**
 * <ul>
 * <li>orderDetails: 订单明细 {@link com.everhomes.rest.pmtask.PmTaskOrderDetailDTO}</li>
 * </ul>
 */
public class CreateOrderDetailsCommand {

    List<PmTaskOrderDetailDTO> orderDetails;

    public List<PmTaskOrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<PmTaskOrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
