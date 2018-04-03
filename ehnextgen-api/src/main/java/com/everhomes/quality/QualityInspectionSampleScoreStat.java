package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionSampleScoreStat;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/6/7.
 */
public class QualityInspectionSampleScoreStat extends EhQualityInspectionSampleScoreStat {
    private static final long serialVersionUID = 4084754900587384311L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
