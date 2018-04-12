// @formatter:off
package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceApplicationRecords;
import com.everhomes.util.StringHelper;

public class ServiceAllianceApplicationRecord extends EhServiceAllianceApplicationRecords {
	
	private static final long serialVersionUID = 5073117646795182512L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}