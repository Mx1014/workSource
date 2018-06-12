// @formatter:off
package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkIpads;
import com.everhomes.util.StringHelper;

public class AclinkIpad extends EhAclinkIpads {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4929951626833161662L;

	public AclinkIpad() {
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
