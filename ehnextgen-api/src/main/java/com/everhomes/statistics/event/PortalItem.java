// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.launchpad.LaunchPadItem;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.util.StringHelper;

public class PortalItem extends EhPortalItems {
	
	private static final long serialVersionUID = 9120998246747101737L;

    private LaunchPadItem launchPadItem;

    public LaunchPadItem getLaunchPadItem() {
        return launchPadItem;
    }

    public void setLaunchPadItem(LaunchPadItem launchPadItem) {
        this.launchPadItem = launchPadItem;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}