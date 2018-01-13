package com.everhomes.customer;

import com.everhomes.rest.common.SyncDataResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public interface SyncDataTaskService {
    SyncDataTask executeTask(ExecuteSyncTaskCallback callback, SyncDataTask task);

    SyncDataResponse getSyncDataResult(Long taskId);
}
