package com.everhomes.search;

import com.everhomes.quality.QualityInspectionSamples;
import com.everhomes.rest.quality.*;

import java.util.List;

/**
 * Created by ying.xiong on 2017/6/9.
 */
public interface QualityInspectionSampleSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<QualityInspectionSamples> samples);
    void feedDoc(QualityInspectionSamples sample);
    void syncFromDb();
    ListSampleQualityInspectionResponse query(SearchSampleQualityInspectionCommand cmd);
    CountSampleTaskScoresResponse queryCount(CountSampleTaskScoresCommand cmd);
}
