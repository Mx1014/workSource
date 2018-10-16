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
    private Byte payMode;

    public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
