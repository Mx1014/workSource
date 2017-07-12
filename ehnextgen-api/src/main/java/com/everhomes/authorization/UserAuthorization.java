// @formatter:off
package com.everhomes.authorization;

import com.everhomes.server.schema.tables.pojos.EhUserAuthorizations;
import com.everhomes.util.StringHelper;

public class UserAuthorization extends EhUserAuthorizations{
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
