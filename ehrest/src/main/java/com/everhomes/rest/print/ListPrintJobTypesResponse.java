// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * <ul>
 * <li>jobTypeList : 任务类型列表，参考 {@link com.everhomes.rest.print.PrintJobTypeType} </li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintJobTypesResponse {
	@ItemType(PrintJobTypeType.class)
	private List<PrintJobTypeType> jobTypeList;
}
