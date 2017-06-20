// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>identifierToken : url后跟的唯一标识</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class LogonPrintCommand {
	private String identifierToken;

	public String getIdentifierToken() {
		return identifierToken;
	}

	public void setIdentifierToken(String identifierToken) {
		this.identifierToken = identifierToken;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
