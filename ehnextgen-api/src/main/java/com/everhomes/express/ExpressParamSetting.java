// @formatter:off
package com.everhomes.express;

import com.everhomes.server.schema.tables.pojos.EhExpressParamSettings;
import com.everhomes.util.StringHelper;

public class ExpressParamSetting extends EhExpressParamSettings {
	
	private static final long serialVersionUID = 7123971271965211600L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}