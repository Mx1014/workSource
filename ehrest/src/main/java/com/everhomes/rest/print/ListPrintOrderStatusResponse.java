// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * <ul>
 * <li>orderStatusList : 订单状态集合，参考 {@link com.everhomes.rest.print.PrintOrderStatusType} </li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintOrderStatusResponse {
	@ItemType(PrintOrderStatusType.class)
	private List<PrintOrderStatusType> orderStatusList;
}
