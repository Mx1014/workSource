// @formatter:off
package com.everhomes.group;

import com.everhomes.server.schema.tables.pojos.EhIndustryTypes;
import com.everhomes.util.StringHelper;

public class IndustryType extends EhIndustryTypes {

	private static final long serialVersionUID = 8445163262224738735L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}