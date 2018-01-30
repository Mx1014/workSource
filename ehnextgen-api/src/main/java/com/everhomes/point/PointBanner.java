// @formatter:off
package com.everhomes.point;

import com.everhomes.server.schema.tables.pojos.EhPointBanners;
import com.everhomes.util.StringHelper;

public class PointBanner extends EhPointBanners {
	
	private static final long serialVersionUID = -6522402641443290418L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}