// @formatter:off
package com.everhomes.activity;

import com.everhomes.server.schema.tables.pojos.EhWarningSettings;
import com.everhomes.util.StringHelper;

public class WarningSetting extends EhWarningSettings {

	private static final long serialVersionUID = 5915268144874896464L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}