package com.everhomes.customer;

import com.everhomes.rest.common.SyncDataResponse;
import com.everhomes.rest.customer.ListCommunitySyncResultResponse;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public interface SyncDataTaskService {
    SyncDataTask executeTask(ExecuteSyncTaskCallback callback, SyncDataTask task);

    SyncDataResponse getSyncDataResult(Long taskId);

    ListCommunitySyncResultResponse listCommunitySyncResult(Long communityId, String syncType, Integer pageSize, Long pageAnchor);

    String syncHasViewed(Long communityId, String syncType);
    // add for multi thread sync data
    Boolean requireSyncDataLock(String lockKey);

    void releaseSyncLock(String lockKey);
}
