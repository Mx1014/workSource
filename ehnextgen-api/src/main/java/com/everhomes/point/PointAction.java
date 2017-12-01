// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointActions;
import com.everhomes.util.StringHelper;

public class PointAction extends EhPointActions {
	
	private static final long serialVersionUID = 1123914970485522651L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}