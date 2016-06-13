package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>limit: 消费满足的金额</li>
 *  <li>subtract: 减的金额</li>
 * </ul>
 */
public class SetVideoConfAccountPreferentialRuleCommand {

	private String limit;
	
	private String subtract;

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getSubtract() {
		return subtract;
	}

	public void setSubtract(String subtract) {
		this.subtract = subtract;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
