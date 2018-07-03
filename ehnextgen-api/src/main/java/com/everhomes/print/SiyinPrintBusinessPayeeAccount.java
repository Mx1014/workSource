// @formatter:off
package com.everhomes.print;

import com.everhomes.server.schema.tables.pojos.EhSiyinPrintBusinessPayeeAccounts;
import com.everhomes.util.StringHelper;

public class SiyinPrintBusinessPayeeAccount extends EhSiyinPrintBusinessPayeeAccounts {
	
	private static final long serialVersionUID = -4294049926055160084L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}