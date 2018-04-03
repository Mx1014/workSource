// @formatter:off
package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>: </li>
 * </ul>
 */
public class MaxMinPrice {
	private BigDecimal maxPrice;
	private BigDecimal minPrice;
	private BigDecimal maxOrgMemberPrice;
	private BigDecimal minOrgMemberPrice;
	private BigDecimal maxApprovingUserPrice;
	private BigDecimal minApprovingUserPrice;

	public MaxMinPrice() {
		super();
	}

	public MaxMinPrice(BigDecimal maxPrice, BigDecimal minPrice, BigDecimal maxOrgMemberPrice,
			BigDecimal minOrgMemberPrice, BigDecimal maxApprovingUserPrice, BigDecimal minApprovingUserPrice) {
		super();
		this.maxPrice = maxPrice;
		this.minPrice = minPrice;
		this.maxOrgMemberPrice = maxOrgMemberPrice;
		this.minOrgMemberPrice = minOrgMemberPrice;
		this.maxApprovingUserPrice = maxApprovingUserPrice;
		this.minApprovingUserPrice = minApprovingUserPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxOrgMemberPrice() {
		return maxOrgMemberPrice;
	}

	public void setMaxOrgMemberPrice(BigDecimal maxOrgMemberPrice) {
		this.maxOrgMemberPrice = maxOrgMemberPrice;
	}

	public BigDecimal getMinOrgMemberPrice() {
		return minOrgMemberPrice;
	}

	public void setMinOrgMemberPrice(BigDecimal minOrgMemberPrice) {
		this.minOrgMemberPrice = minOrgMemberPrice;
	}

	public BigDecimal getMaxApprovingUserPrice() {
		return maxApprovingUserPrice;
	}

	public void setMaxApprovingUserPrice(BigDecimal maxApprovingUserPrice) {
		this.maxApprovingUserPrice = maxApprovingUserPrice;
	}

	public BigDecimal getMinApprovingUserPrice() {
		return minApprovingUserPrice;
	}

	public void setMinApprovingUserPrice(BigDecimal minApprovingUserPrice) {
		this.minApprovingUserPrice = minApprovingUserPrice;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
