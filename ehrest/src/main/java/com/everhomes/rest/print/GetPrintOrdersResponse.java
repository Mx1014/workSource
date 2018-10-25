// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>printOrders: 打印记录，参考{@link com.everhomes.rest.print.PrintOrderDTO}</li>
 * </ul>
 *
 */
public class GetPrintOrdersResponse {
	@ItemType(PrintOrderDTO.class)
	private PrintOrderDTO printOrder;
	
	public PrintOrderDTO getPrintOrder() {
		return printOrder;
	}

	public void setPrintOrder(PrintOrderDTO printOrder) {
		this.printOrder = printOrder;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
