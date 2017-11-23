// @formatter:off
package com.everhomes.group;

import com.everhomes.server.schema.tables.pojos.EhGuildApplies;
import com.everhomes.util.StringHelper;

public class GuildApply extends EhGuildApplies {


	private static final long serialVersionUID = -7840307627571123030L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}