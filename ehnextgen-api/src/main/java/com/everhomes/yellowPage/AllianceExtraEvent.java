// @formatter:off
package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhAllianceExtraEvents;
import com.everhomes.util.StringHelper;

public class AllianceExtraEvent extends EhAllianceExtraEvents {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1742972186600335777L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}