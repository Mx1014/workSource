package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billId:账单id</li>
 *</ul>
 */
public class ListUploadCertificatesCommand {
	
	private Long billId;

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
