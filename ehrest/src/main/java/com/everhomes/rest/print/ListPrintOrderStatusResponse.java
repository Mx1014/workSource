// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>orderStatusList : 订单状态集合，参考 {@link com.everhomes.rest.print.PrintOrderStatusType} </li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintOrderStatusResponse {
	@ItemType(Byte.class)
	private List<Byte> orderStatusList;
	
	public ListPrintOrderStatusResponse() {
	}
	
	public ListPrintOrderStatusResponse(List<Byte> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public List<Byte> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<Byte> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
