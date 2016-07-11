package com.everhomes.promotion;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ScheduleTaskProvider {

    Long createScheduleTask(ScheduleTask obj);

    void updateScheduleTask(ScheduleTask obj);

    void deleteScheduleTask(ScheduleTask obj);

    ScheduleTask getScheduleTaskById(Long id);

    List<ScheduleTask> queryScheduleTasks(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    ScheduleTask getSchduleTaskByPromotionId(Long promotionId);

}
