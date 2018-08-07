// @formatter:off
package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>fileSuffix: 后缀名</li>
 * </ul>
 */
public class GetFileIconCommand {

	private String fileSuffix;

	public GetFileIconCommand() {

	}

	public GetFileIconCommand(String fileSuffix) {
		super();
		this.fileSuffix = fileSuffix;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
