// @formatter:off
package com.everhomes.serviceModuleApp;

import java.util.List;

public interface WorkPlatformAppProvider {

    WorkPlatformApp getWorkPlatformApp(Long appOriginId, Long scopeId, Long  entryId);

    void deleteWorkPlatformApp(WorkPlatformApp workPlatformApp);

    void createWorkPlatformApp(WorkPlatformApp workPlatformApp);

    void updateWorkPlatformApp(WorkPlatformApp workPlatformApp);

    List<WorkPlatformApp> listWorkPlatformApp(Long scopeId, Integer sortNum);

    List<WorkPlatformApp> listWorkPlatformApp(Long appOriginId, Long scopeId);

    List<WorkPlatformApp> listWorkPlatformAppByScopeId(Long scopeId);
    Integer getMaxSort(Long scopeId);

    WorkPlatformApp findWorkPlatformById(Long id);
}
