package com.everhomes.rest.rentalv2;

import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>preOrderDTO：预支付订单（支付统一dto） </li>
 * <li>flowCaseUrl : payMode 为1线下支付的时候,需要url跳转工作流 </li>  
 * </ul>
 */
public class AddRentalBillItemV2Response {

	private String flowCaseUrl;

	private PreOrderDTO preOrderDTO;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getFlowCaseUrl() {
		return flowCaseUrl;
	}
	public void setFlowCaseUrl(String flowCaseUrl) {
		this.flowCaseUrl = flowCaseUrl;
	}

	public PreOrderDTO getPreOrderDTO() {
		return preOrderDTO;
	}

	public void setPreOrderDTO(PreOrderDTO preOrderDTO) {
		this.preOrderDTO = preOrderDTO;
	}
}
