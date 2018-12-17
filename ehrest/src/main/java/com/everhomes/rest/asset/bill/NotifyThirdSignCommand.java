//@formatter:off
package com.everhomes.rest.asset.bill;

import java.util.List;

/**
 *<ul>
 * <li>billIdList: 账单ID列表，左邻对这些账单置一个特殊error标记，以此标记判断这些账单数据下一次同步不再传输</li>
 */
public class NotifyThirdSignCommand {
	
	private List<Long> billIdList;

	public List<Long> getBillIdList() {
		return billIdList;
	}

	public void setBillIdList(List<Long> billIdList) {
		this.billIdList = billIdList;
	}
	
}
