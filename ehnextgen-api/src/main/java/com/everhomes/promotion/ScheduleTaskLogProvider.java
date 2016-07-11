package com.everhomes.promotion;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ScheduleTaskLogProvider {

    Long createScheduleTaskLog(ScheduleTaskLog obj);

    void updateScheduleTaskLog(ScheduleTaskLog obj);

    void deleteScheduleTaskLog(ScheduleTaskLog obj);

    ScheduleTaskLog getScheduleTaskLogById(Long id);

    List<ScheduleTaskLog> queryScheduleTaskLogs(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    ScheduleTaskLog findScheduleTaskLogByUser(Long promotionId, Long userId);

}
