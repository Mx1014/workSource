package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ns: 域空间Id</li>
 * </ul>
 */
public class CheckAuthCommand {

	private Integer ns;

	public Integer getNs() {
		return ns;
	}

	public void setNs(Integer ns) {
		this.ns = ns;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
