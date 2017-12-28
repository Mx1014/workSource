package com.everhomes.rest.rentalv2;

import com.everhomes.rest.order.PreOrderDTO;

/**
 * @author sw on 2017/12/28.
 */
public class AddRentalOrderUsingInfoV2Response {
    private PreOrderDTO preOrderDTO;
    private String flowCaseUrl;

    public PreOrderDTO getPreOrderDTO() {
        return preOrderDTO;
    }

    public void setPreOrderDTO(PreOrderDTO preOrderDTO) {
        this.preOrderDTO = preOrderDTO;
    }

    public String getFlowCaseUrl() {
        return flowCaseUrl;
    }

    public void setFlowCaseUrl(String flowCaseUrl) {
        this.flowCaseUrl = flowCaseUrl;
    }
}
