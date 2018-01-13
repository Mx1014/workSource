package com.everhomes.customer;

import com.everhomes.rest.common.SyncDataResponse;
import com.everhomes.rest.customer.SyncDataTaskStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public class SyncDataTaskServiceImpl implements SyncDataTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncDataTaskServiceImpl.class);

    @Autowired
    private SyncDataTaskProvider syncDataTaskProvider;
    @Override
    public SyncDataTask executeTask(ExecuteSyncTaskCallback callback, SyncDataTask task) {
        task.setStatus(SyncDataTaskStatus.CREATED.getCode());
        syncDataTaskProvider.createSyncDataTask(task);
        User user = UserContext.current().getUser();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    UserContext.setCurrentUser(user);
                    task.setStatus(SyncDataTaskStatus.EXECUTING.getCode());
                    syncDataTaskProvider.updateSyncDataTask(task);
                    SyncDataResponse response = callback.syncData();
                    task.setStatus(SyncDataTaskStatus.FINISH.getCode());
                    task.setResult(StringHelper.toJsonString(response));
                }catch (Exception e){
                    LOGGER.error("executor task error. error: {}", e);
                    task.setStatus(SyncDataTaskStatus.EXCEPTION.getCode());
                    task.setResult(e.toString());
                }finally {
                    syncDataTaskProvider.updateSyncDataTask(task);
                }

            }
        });
        return task;
    }

    @Override
    public SyncDataResponse getSyncDataResult(Long taskId) {
        return null;
    }
}
