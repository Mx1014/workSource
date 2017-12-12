// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhPortalLayouts;
import com.everhomes.util.StringHelper;

public class PortalLayout extends EhPortalLayouts {
	
	private static final long serialVersionUID = 4185035423963522020L;

	// 场景   add by xq.tian  2017/09/01
	private String sceneType;

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}