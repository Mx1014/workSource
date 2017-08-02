// @formatter:off
package com.everhomes.print;

import com.everhomes.server.schema.tables.pojos.EhSiyinPrintPrinters;
import com.everhomes.util.StringHelper;

public class SiyinPrintPrinter extends EhSiyinPrintPrinters {
	
	private static final long serialVersionUID = 6598662573716247014L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}