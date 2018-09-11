package com.everhomes.pmNotify;

import java.util.List;

/**
 * Created by ying.xiong on 2017/9/12.
 */
public interface PmNotifyProvider {
    void createPmNotifyConfigurations(PmNotifyConfigurations configuration);
    void updatePmNotifyConfigurations(PmNotifyConfigurations configuration);
    List<PmNotifyConfigurations> listScopePmNotifyConfigurations(String ownerType, Byte scopeType, Long scopeId,Long targetId,String targetType);
    PmNotifyConfigurations findScopePmNotifyConfiguration(Long id, String ownerType,Byte scopeType, Long scopeId);

    void createPmNotifyRecord(PmNotifyRecord record);
    PmNotifyRecord findRecordById(Long id);
    boolean updateIfUnsend(Long id);
    List<PmNotifyRecord> listUnsendRecords();

    void createPmNotifyLog(PmNotifyLog log);

    void invalidateNotifyRecord(List<Long> recordIds);
}
