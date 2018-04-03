// @formatter:off
package com.everhomes.print;

import com.everhomes.server.schema.tables.pojos.EhSiyinPrintRecords;
import com.everhomes.util.StringHelper;

public class SiyinPrintRecord extends EhSiyinPrintRecords {
	
	private static final long serialVersionUID = -3346743592153325824L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}