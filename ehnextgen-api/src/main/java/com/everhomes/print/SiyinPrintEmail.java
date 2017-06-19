// @formatter:off
package com.everhomes.print;

import com.everhomes.server.schema.tables.pojos.EhSiyinPrintEmails;
import com.everhomes.util.StringHelper;

public class SiyinPrintEmail extends EhSiyinPrintEmails {
	
	private static final long serialVersionUID = -3846146120831876121L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}