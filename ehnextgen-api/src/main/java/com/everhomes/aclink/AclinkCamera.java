// @formatter:off
package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkCameras;
import com.everhomes.util.StringHelper;

public class AclinkCamera extends EhAclinkCameras {
	/**
	 * 
	 */
	private static final long serialVersionUID = -789463393006389356L;

	public AclinkCamera() {
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
