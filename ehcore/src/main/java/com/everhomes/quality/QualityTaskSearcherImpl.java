package com.everhomes.quality;

import com.everhomes.rest.quality.ListQualityInspectionTasksResponse;
import com.everhomes.rest.quality.SearchQualityTasksCommand;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.QualityTaskSearcher;

import java.util.List;

/**
 * Created by ying.xiong on 2017/6/8.
 */
public class QualityTaskSearcherImpl extends AbstractElasticSearch implements QualityTaskSearcher {
    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void bulkUpdate(List<QualityInspectionTasks> tasks) {
        
    }

    @Override
    public void feedDoc(QualityInspectionTasks task) {

    }

    @Override
    public void syncFromDb() {

    }

    @Override
    public ListQualityInspectionTasksResponse query(SearchQualityTasksCommand cmd) {
        return null;
    }

    @Override
    public String getIndexType() {
        return null;
    }
}
