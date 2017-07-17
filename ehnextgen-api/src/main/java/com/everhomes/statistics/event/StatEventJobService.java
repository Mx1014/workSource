package com.everhomes.statistics.event;

import java.time.LocalDate;

/**
 * Created by xq.tian on 2017/7/31.
 */
public interface StatEventJobService {

    void executeTask(LocalDate statDate);
}
