package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionModleCommunityMap;
import com.everhomes.util.StringHelper;

/**
 * Date: 2017/12/11 16 :57
 *
 * @author jerry.R
 */

public class QualityInspectionModleCommunityMap extends EhQualityInspectionModleCommunityMap {

    private static final long serialVersionUID = -2402582732512747491L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
