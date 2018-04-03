package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleCommunityMap;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/6/7.
 */
public class QualityInspectionSampleCommunityMap extends EhQualityInspectionSampleCommunityMap{
    private static final long serialVersionUID = -8873867728717256193L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
