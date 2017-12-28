package com.everhomes.rest.rentalv2;

import com.everhomes.rest.order.PreOrderDTO;

/**
 * <ul>
 * <li>preOrderDTO: preOrderDTO {@link com.everhomes.rest.order.PreOrderDTO}</li>
 * <li>flowCaseUrl: flowCaseUrl</li>
 * </ul>
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
