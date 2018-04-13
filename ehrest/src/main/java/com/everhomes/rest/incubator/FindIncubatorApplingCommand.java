package com.everhomes.rest.incubator;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>rootId: rootId</li>
 * </ul>
 */
public class FindIncubatorApplingCommand {


	private Long rootId;

	public Long getRootId() {
		return rootId;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
