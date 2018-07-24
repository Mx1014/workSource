// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.server.schema.tables.pojos.EhVisitorSysCoding;
import com.everhomes.util.StringHelper;

public class VisitorSysCoding extends EhVisitorSysCoding {
	
	private static final long serialVersionUID = -3877923154687682231L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}