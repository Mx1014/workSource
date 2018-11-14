package com.everhomes.rest.investmentAd;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id: 招商广告id</li>
 * </ul>
 */
public class GetInvestmentAdCommand {
	
	@NotNull
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
