// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalVersions;
import com.everhomes.util.StringHelper;

public class PortalVersion extends EhPortalVersions {


	private static final long serialVersionUID = -6498610514200987662L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}