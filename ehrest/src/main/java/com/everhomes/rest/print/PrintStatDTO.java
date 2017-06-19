// @formatter:off
package com.everhomes.rest.print;

import java.math.BigDecimal;

/**
 * 
 * <ul>
 * <li>paid : 已付款总计</li>
 * <li>unpaid : 未付款总计</li>
 * <li>all : 全部总计</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class PrintStatDTO {
	private BigDecimal paid;
	private BigDecimal unpaid;
	private BigDecimal all;
}
