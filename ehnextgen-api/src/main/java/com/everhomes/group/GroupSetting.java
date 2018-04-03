// @formatter:off
package com.everhomes.group;

import com.everhomes.server.schema.tables.pojos.EhGroupSettings;
import com.everhomes.util.StringHelper;

public class GroupSetting extends EhGroupSettings {

	private static final long serialVersionUID = 7473666647579865658L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}