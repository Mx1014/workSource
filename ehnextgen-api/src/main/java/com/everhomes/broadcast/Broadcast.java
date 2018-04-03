// @formatter:off
package com.everhomes.broadcast;

import com.everhomes.server.schema.tables.pojos.EhBroadcasts;
import com.everhomes.util.StringHelper;

public class Broadcast extends EhBroadcasts {

	private static final long serialVersionUID = -3347147589750460770L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}