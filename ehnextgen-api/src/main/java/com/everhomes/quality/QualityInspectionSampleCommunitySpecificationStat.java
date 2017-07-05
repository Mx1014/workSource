package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleCommunitySpecificationStat;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/6/7.
 */
public class QualityInspectionSampleCommunitySpecificationStat extends EhQualityInspectionSampleCommunitySpecificationStat {
    private static final long serialVersionUID = 4646975895286814563L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
