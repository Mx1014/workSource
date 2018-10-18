//@formatter:off
package com.everhomes.asset;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhAssetDooraccessLogs;
/**
 * Created by djm
 */

public class AssetDooraccessLog extends EhAssetDooraccessLogs {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5272927229744806162L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
