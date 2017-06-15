package com.everhomes.search;

import com.everhomes.quality.QualityInspectionTasks;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.quality.ListQualityInspectionTasksResponse;
import com.everhomes.rest.quality.SearchQualityTasksCommand;

import java.util.List;

/**
 * Created by ying.xiong on 2017/6/8.
 */
public interface QualityTaskSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<QualityInspectionTasks> tasks);
    void feedDoc(QualityInspectionTasks task);
    void syncFromDb();
    List<Long> query(SearchQualityTasksCommand cmd);
}
