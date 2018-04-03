// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.util.StringHelper;

import java.util.Map;

public class PortalItemGroup extends EhPortalItemGroups {
	
	private static final long serialVersionUID = 4333390250716261476L;

    // 场景   add by xq.tian  2017/09/01
    private String sceneType;

    private Map<String, String> instanceConfigMap;

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public Map<String, String> getInstanceConfigMap() {
        return instanceConfigMap;
    }

    public void setInstanceConfigMap(Map<String, String> instanceConfigMap) {
        this.instanceConfigMap = instanceConfigMap;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}