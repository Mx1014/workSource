// @formatter:off
package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRanges;
import com.everhomes.util.StringHelper;

public class OfficeCubicleRange extends EhOfficeCubicleRanges {
	
	private static final long serialVersionUID = 7910815455082596153L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}