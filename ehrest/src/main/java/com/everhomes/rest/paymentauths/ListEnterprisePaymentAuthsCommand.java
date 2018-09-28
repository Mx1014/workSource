package com.everhomes.rest.paymentauths;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>orgnazitionId: 公司ID</li>
 * </ul>
 */
public class ListEnterprisePaymentAuthsCommand {
	private Long orgnazitionId;

	public Long getOrgnazitionId() {
		return orgnazitionId;
	}

	public void setOrgnazitionId(Long orgnazitionId) {
		this.orgnazitionId = orgnazitionId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
