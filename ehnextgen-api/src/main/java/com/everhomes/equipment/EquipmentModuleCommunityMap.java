package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentModuleCommunityMap;
import com.everhomes.util.StringHelper;

/**
 * Date: 2017/12/5 20 :20
 *
 * @author jerry.R
 */

public class EquipmentModuleCommunityMap  extends EhEquipmentModuleCommunityMap {
    private static final long serialVersionUID = -3393181671186124334L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
