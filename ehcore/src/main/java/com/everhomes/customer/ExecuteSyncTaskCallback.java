package com.everhomes.customer;

import com.everhomes.rest.common.SyncDataResponse;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public interface ExecuteSyncTaskCallback<T> {
    SyncDataResponse<T> syncData();
}
