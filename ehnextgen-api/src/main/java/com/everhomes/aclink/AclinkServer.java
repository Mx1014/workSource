// @formatter:off
package com.everhomes.aclink;

import com.everhomes.server.schema.tables.pojos.EhAclinkServers;
import com.everhomes.util.StringHelper;

public class AclinkServer extends EhAclinkServers {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1030908388740582073L;
	
	public String getUuidNum(){
		return this.getUuid().replace(":","");
	}

	public AclinkServer() {
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
