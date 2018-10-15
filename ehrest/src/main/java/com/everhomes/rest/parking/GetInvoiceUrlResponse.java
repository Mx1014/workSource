package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>invoiceUrl : 发票url</li>
 * </ul>
 */
public class GetInvoiceUrlResponse {
    private String invoiceUrl;

    public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
