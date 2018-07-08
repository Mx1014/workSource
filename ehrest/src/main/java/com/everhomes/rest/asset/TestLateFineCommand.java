//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by ycx
 * @date 下午3:30:35
 */

/**
 *<ul>
 * <li>date: 你想要的测试滞纳金的未来日期，如：2019-07-08</li>
 *</ul>
 */
public class TestLateFineCommand {
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
    
}
