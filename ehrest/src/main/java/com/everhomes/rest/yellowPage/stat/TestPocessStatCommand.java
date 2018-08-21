package com.everhomes.rest.yellowPage.stat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>dateString: YYYYMMDD 格式的字符串</li>
 * </ul>
 */
public class TestPocessStatCommand {
	private String dateString;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
}
