// @formatter:off
package com.everhomes.serviceModuleApp;

import java.util.List;

public interface WorkPlatformAppProvider {

    WorkPlatformApp getWorkPlatformApp(Long appOriginId, Long scopeId);

    void deleteWorkPlatformApp(WorkPlatformApp workPlatformApp);

    void createWorkPlatformApp(WorkPlatformApp workPlatformApp);

    void updateWorkPlatformApp(WorkPlatformApp workPlatformApp);

    List<WorkPlatformApp> listWorkPlatformApp(Long appOriginId, Long scopeId, Integer sortNum);

    List<WorkPlatformApp> listWorkPlatformApp(Long appOriginId, Long scopeId);
    Integer getMaxSort(Long appOriginId, Long scopeId);
}
