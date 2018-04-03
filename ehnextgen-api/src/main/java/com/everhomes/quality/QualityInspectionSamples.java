package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSamples;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/6/7.
 */
public class QualityInspectionSamples extends EhQualityInspectionSamples {
    private static final long serialVersionUID = -591076922694663238L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
