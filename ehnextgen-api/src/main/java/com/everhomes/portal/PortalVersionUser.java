// @formatter:off
package com.everhomes.portal;

import com.everhomes.server.schema.tables.pojos.EhPortalVersionUsers;
import com.everhomes.server.schema.tables.pojos.EhPortalVersions;
import com.everhomes.util.StringHelper;

public class PortalVersionUser extends EhPortalVersionUsers {


	private static final long serialVersionUID = 8174040975963884327L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}