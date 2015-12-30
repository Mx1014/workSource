package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>months: 起售月份</li>
 * </ul>
 *
 */
public class SetMinimumMonthsCommand {

	private String months;

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
