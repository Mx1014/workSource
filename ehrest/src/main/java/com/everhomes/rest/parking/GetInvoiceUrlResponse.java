package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>invoiceDetailUrl : 发票详情url</li>
 * <li>invoiceOpenUrl：申请开票Url</li>
 * </ul>
 */
public class GetInvoiceUrlResponse {
    private String invoiceDetailUrl;
    private String invoiceOpenUrl;

	public String getInvoiceDetailUrl() {
		return invoiceDetailUrl;
	}


	public void setInvoiceDetailUrl(String invoiceDetailUrl) {
		this.invoiceDetailUrl = invoiceDetailUrl;
	}


	public String getInvoiceOpenUrl() {
		return invoiceOpenUrl;
	}


	public void setInvoiceOpenUrl(String invoiceOpenUrl) {
		this.invoiceOpenUrl = invoiceOpenUrl;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
