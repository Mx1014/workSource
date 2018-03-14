//package com.everhomes.energy;
//
//import com.everhomes.rest.energy.SyncOfflineResponse;
//import com.everhomes.rest.energy.SyncOfflineTaskStatus;
//import com.everhomes.user.User;
//import com.everhomes.user.UserContext;
//import com.everhomes.util.ExecutorUtil;
//import com.everhomes.util.StringHelper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * Created by ying.xiong on 2018/1/6.
// */
//@Component
//public class SyncOfflineServiceImpl implements SyncOfflineService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(SyncOfflineServiceImpl.class);
//
//    @Autowired
//    private SyncOfflineProvider syncOfflineProvider;
//
//    @Override
//    public SyncOfflineTask executeTask(ExecuteSyncOfflineTaskCallback callback, SyncOfflineTask task) {
//        task.setStatus(SyncOfflineTaskStatus.CREATED.getCode());
//        syncOfflineProvider.createSyncOfflineTask(task);
//        User user = UserContext.current().getUser();
//        ExecutorUtil.submit(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    UserContext.setCurrentUser(user);
//                    task.setStatus(SyncOfflineTaskStatus.EXECUTING.getCode());
//                    syncOfflineProvider.updateSyncOfflineTask(task);
//                    SyncOfflineResponse response = callback.importFile();
//                    task.setStatus(SyncOfflineTaskStatus.FINISH.getCode());
//                    task.setResult(StringHelper.toJsonString(response));
//                }catch (Exception e){
//                    LOGGER.error("executor task error. error: {}", e);
//                    task.setStatus(SyncOfflineTaskStatus.EXCEPTION.getCode());
//                    task.setResult(e.toString());
//                }finally {
//                    syncOfflineProvider.updateSyncOfflineTask(task);
//                }
//
//            }
//        });
//        return task;
//    }
//
//    @Override
//    public SyncOfflineResponse getSyncOfflineTaskResult(Long taskId) {
//        return null;
//    }
//}
