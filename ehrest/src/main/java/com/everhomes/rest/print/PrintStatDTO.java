// @formatter:off
package com.everhomes.rest.print;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

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
	
	public PrintStatDTO() {
	}
	public PrintStatDTO(BigDecimal paid, BigDecimal unpaid, BigDecimal all) {
		this.paid = paid;
		this.unpaid = unpaid;
		this.all = all;
	}
	public BigDecimal getPaid() {
		return paid;
	}
	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}
	public BigDecimal getUnpaid() {
		return unpaid;
	}
	public void setUnpaid(BigDecimal unpaid) {
		this.unpaid = unpaid;
	}
	public BigDecimal getAll() {
		return all;
	}
	public void setAll(BigDecimal all) {
		this.all = all;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return StringHelper.toJsonString(this);
	}
}
