// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>expressCompanyId: 快递公司id</li>
 * <li>billNo: 快递单号</li>
 * </ul>
 */
public class GetExpressLogisticsDetailCommand {

	private Long expressCompanyId;

	private String billNo;

	public GetExpressLogisticsDetailCommand() {

	}

	public GetExpressLogisticsDetailCommand(Long expressCompanyId, String billNo) {
		super();
		this.expressCompanyId = expressCompanyId;
		this.billNo = billNo;
	}

	public Long getExpressCompanyId() {
		return expressCompanyId;
	}

	public void setExpressCompanyId(Long expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
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
