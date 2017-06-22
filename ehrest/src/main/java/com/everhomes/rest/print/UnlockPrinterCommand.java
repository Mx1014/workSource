// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>readerName : 打印机readerName</li>
 * </ul>
 *
 *  @author:dengs 2017年6月20日
 */
public class UnlockPrinterCommand {
	private String readerName;

	public String getReaderName() {
		return readerName;
	}

	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
