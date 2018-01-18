package com.everhomes.energy;

import com.everhomes.rest.energy.SyncOfflineResponse;

/**
 * Created by ying.xiong on 2018/1/6.
 */
public interface ExecuteSyncOfflineTaskCallback<T> {
    SyncOfflineResponse<T> importFile();
}
