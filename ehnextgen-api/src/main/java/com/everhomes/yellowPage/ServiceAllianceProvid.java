// @formatter:off
package com.everhomes.yellowPage;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceProviders;
import com.everhomes.util.StringHelper;

public class ServiceAllianceProvid extends EhServiceAllianceProviders {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6666401058998073602L;
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}