// @formatter:off
package com.everhomes.print;

import com.everhomes.server.schema.tables.pojos.EhSiyinPrintSettings;
import com.everhomes.util.StringHelper;

public class SiyinPrintSetting extends EhSiyinPrintSettings {
	
	private static final long serialVersionUID = -4926733952839971851L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}