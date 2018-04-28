package com.everhomes.customer;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.openapi.ZjSyncdataBackup;
import com.everhomes.openapi.ZjSyncdataBackupProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.SyncDataResponse;
import com.everhomes.rest.common.SyncDataResultLog;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2018/1/13.
 */
@Component
public class SyncDataTaskServiceImpl implements SyncDataTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncDataTaskServiceImpl.class);

    @Autowired
    private SyncDataTaskProvider syncDataTaskProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ZjSyncdataBackupProvider zjSyncdataBackupProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Override
    public SyncDataTask executeTask(ExecuteSyncTaskCallback callback, SyncDataTask task) {
        task.setStatus(SyncDataTaskStatus.EXECUTING.getCode());
        syncDataTaskProvider.createSyncDataTask(task);
        User user = UserContext.current().getUser();
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    LOGGER.debug("SyncDataTask RUN");
                    UserContext.setCurrentUser(user);
//                    SyncDataResponse response = callback.syncData();
                    callback.syncData();
                    task.setStatus(SyncDataTaskStatus.FINISH.getCode());
                    task.setResult("同步成功");
                }catch (Exception e){
                    LOGGER.error("executor task error. error: {}", e);
                    task.setStatus(SyncDataTaskStatus.EXCEPTION.getCode());
                    task.setResult(e.toString());
                }finally {
                    LOGGER.debug("SyncDataTask task: {}", StringHelper.toJsonString(task));
                    syncDataTaskProvider.updateSyncDataTask(task);
                }

            }
        });
        return task;
    }

    @Override
    public SyncDataResponse getSyncDataResult(Long taskId) {
        User user = UserContext.current().getUser();

        SyncDataResponse response = new SyncDataResponse();

        SyncDataTask task = syncDataTaskProvider.findSyncDataTaskById(taskId);

        if(null != task){
            if(SyncDataTaskStatus.FINISH == SyncDataTaskStatus.fromCode(task.getStatus())){
                response =  (SyncDataResponse)StringHelper.fromJsonString(task.getResult(), SyncDataResponse.class);
                List<SyncDataResultLog> logs =  response.getLogs();
                if (logs != null) {
                    for (SyncDataResultLog log : logs) {
                        log.setErrorDescription(localeStringService.getLocalizedString(log.getScope(), log.getCode().toString(), user.getLocale(), ""));
                    }
                }
            }
            response.setSyncStatus(task.getStatus());
        }

        return response;
    }

    @Override
    public String syncHasViewed(Long communityId, String syncType) {
        Integer notViewedCount = syncDataTaskProvider.countNotViewedSyncResult(communityId, syncType);
        if(notViewedCount == 0) {
            return String.valueOf(SyncResultViewedFlag.VIEWED.getCode());
        }
        return String.valueOf(SyncResultViewedFlag.NOT_VIEWED.getCode());
    }

    @Override
    public ListCommunitySyncResultResponse listCommunitySyncResult(Long communityId, String syncType, Integer pageSize, Long pageAnchor) {
        Community community = communityProvider.findCommunityById(communityId);
        if(community == null) {
            return null;
        }
        ListCommunitySyncResultResponse response = new ListCommunitySyncResultResponse();
        List<SyncDataTask> tasks = syncDataTaskProvider.listCommunitySyncResult(communityId, syncType, pageSize + 1, pageAnchor);
        if(tasks != null && tasks.size() > 0) {
            List<SyncDataResult> results = new ArrayList<>();
            for (SyncDataTask task : tasks) {
                SyncDataResult result = new SyncDataResult();
                result.setStartTime(task.getCreateTime());
                result.setEndTime(task.getUpdateTime());
                result.setStatus(task.getStatus());
                if(SyncDataTaskStatus.FINISH.equals(SyncDataTaskStatus.fromCode(task.getStatus()))
                        || SyncDataTaskStatus.EXCEPTION.equals(SyncDataTaskStatus.fromCode(task.getStatus()))) {
                    result.setEndTime(task.getUpdateTime());
                    result.setResult(task.getResult());
                } else if(SyncDataTaskStatus.EXECUTING.equals(SyncDataTaskStatus.fromCode(task.getStatus()))) {
                    result.setRateOfProgress(calculateProgress(community, syncType));
                }

                if(task.getCreatorUid() == null || task.getCreatorUid() == 0L) {
                    result.setManualFlag((byte)0);
                } else {
                    result.setManualFlag((byte)1);

                }
                results.add(result);

                task.setViewFlag(SyncResultViewedFlag.VIEWED.getCode());
                syncDataTaskProvider.updateSyncDataTask(task, false);
            }

            if(tasks.size() > pageSize) {
                results.remove(results.size() - 1);
                response.setNextPageAnchor(tasks.get(tasks.size() - 1).getId());
            }
            response.setResults(results);
        }
        return response;
    }

    private Double calculateProgress(Community community, String syncType) {
        Byte dataType = (byte) 0;
        if(SyncDataTaskType.CONTRACT.getCode().equals(syncType)) {
            dataType = DataType.CONTRACT.getCode();
        } else if(SyncDataTaskType.CUSTOMER.getCode().equals(syncType)) {
            dataType = DataType.ENTERPRISE.getCode();
        } else if(SyncDataTaskType.INDIVIDUAL.getCode().equals(syncType)) {
            dataType = DataType.INDIVIDUAL.getCode();
        }
        List<ZjSyncdataBackup> backups = zjSyncdataBackupProvider.listZjSyncdataBackupByParam(community.getNamespaceId(),
                community.getNamespaceCommunityToken(), dataType);
        if(backups != null && backups.size() > 0) {
            int i = 0;
            for (ZjSyncdataBackup backup : backups) {
                if(backup.getStatus() == CommonStatus.INACTIVE.getCode()) {
                    i++;
                }
            }
            if(i == 0) {
                return 0.5 - ((double)1 / ((double)backups.size()*2));
            }
            return 0.5 + ((double)i / ((double)backups.size()*2));
        }

        return 1.0;

    }
}
