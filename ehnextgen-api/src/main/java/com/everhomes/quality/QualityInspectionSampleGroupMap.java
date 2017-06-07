package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleGroupMap;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/6/7.
 */
public class QualityInspectionSampleGroupMap extends EhQualityInspectionSampleGroupMap {
    private static final long serialVersionUID = 255243082301940209L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
