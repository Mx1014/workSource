// @formatter:off
package com.everhomes.print;

import com.everhomes.server.schema.tables.pojos.EhSiyinUserPrinterMappings;
import com.everhomes.util.StringHelper;

public class SiyinUserPrinterMapping extends EhSiyinUserPrinterMappings {
	
	private static final long serialVersionUID = -5634059019267282087L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}