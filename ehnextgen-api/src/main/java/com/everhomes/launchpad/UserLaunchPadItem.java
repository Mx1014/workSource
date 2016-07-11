package com.everhomes.launchpad;

import com.everhomes.server.schema.tables.pojos.EhUserLaunchPadItems;
import com.everhomes.util.StringHelper;

public class UserLaunchPadItem extends EhUserLaunchPadItems{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
