package com.everhomes.zhenzhihui;
import com.everhomes.server.schema.tables.pojos.EhZhenzhihuiEnterpriseInfo;
import com.everhomes.util.StringHelper;

public class ZhenzhihuiEnterpriseInfo extends EhZhenzhihuiEnterpriseInfo {
    /**
	 * 
	 */
	private static final long serialVersionUID = -586464779626161776L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
