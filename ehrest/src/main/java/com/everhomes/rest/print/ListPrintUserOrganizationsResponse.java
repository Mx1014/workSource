// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * <ul>
 * <li>organizationList : 用户所在企业（String）列表</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintUserOrganizationsResponse {
	@ItemType(String.class)
	private List<String> organizationList;
}
