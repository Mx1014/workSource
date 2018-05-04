// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysVisitors;
import com.everhomes.util.StringHelper;

public class VisitorSysVisitor extends EhVisitorSysVisitors {
	
	private static final long serialVersionUID = -1090547867158368768L;
	private Long enterpriseId;
	private String enterpriseName;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}