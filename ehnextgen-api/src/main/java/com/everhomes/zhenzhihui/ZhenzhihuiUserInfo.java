package com.everhomes.zhenzhihui;

import com.everhomes.server.schema.tables.pojos.EhZhenzhihuiUserInfo;
import com.everhomes.util.StringHelper;

public class ZhenzhihuiUserInfo extends EhZhenzhihuiUserInfo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7934829133906019437L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
