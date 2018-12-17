// @formatter:off
package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubiclePayeeAccounts;
import com.everhomes.server.schema.tables.pojos.EhParkingBusinessPayeeAccounts;
import com.everhomes.util.StringHelper;

public class OfficeCubiclePayeeAccount extends EhOfficeCubiclePayeeAccounts {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7773626627268414774L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}