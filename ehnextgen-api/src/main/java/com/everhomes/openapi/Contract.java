// @formatter:off
package com.everhomes.openapi;

import com.everhomes.server.schema.tables.pojos.EhContracts;
import com.everhomes.util.StringHelper;

public class Contract extends EhContracts {

	private static final long serialVersionUID = 7451788240342013832L;

	private Long orgId;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}