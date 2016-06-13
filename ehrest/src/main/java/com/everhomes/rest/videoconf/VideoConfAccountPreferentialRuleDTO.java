package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>limit: 消费满足的金额</li>
 *  <li>subtract: 减的金额</li>
 * </ul>
 */
public class VideoConfAccountPreferentialRuleDTO {

	private Double limit;
	
	private Double subtract;

	public Double getLimit() {
		return limit;
	}

	public void setLimit(Double limit) {
		this.limit = limit;
	}

	public Double getSubtract() {
		return subtract;
	}

	public void setSubtract(Double subtract) {
		this.subtract = subtract;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
