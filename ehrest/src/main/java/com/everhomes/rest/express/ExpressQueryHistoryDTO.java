// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>expressCompany: 快递公司</li>
 * <li>billNo: 快递单号</li>
 * </ul>
 */
public class ExpressQueryHistoryDTO {
	private String expressCompany;
	private String billNo;

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
