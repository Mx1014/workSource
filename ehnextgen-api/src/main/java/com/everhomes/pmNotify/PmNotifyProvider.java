package com.everhomes.pmNotify;

import java.util.List;

/**
 * Created by ying.xiong on 2017/9/12.
 */
public interface PmNotifyProvider {
    void createPmNotifyConfigurations(PmNotifyConfigurations configuration);
    void updatePmNotifyConfigurations(PmNotifyConfigurations configuration);
    List<PmNotifyConfigurations> listScopePmNotifyConfigurations(String ownerType, Byte scopeType, Long scopeId);
    PmNotifyConfigurations findScopePmNotifyConfiguration(Long id, String ownerType,Byte scopeType, Long scopeId);
}
