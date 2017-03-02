package com.everhomes.docking;

/**
 * Created by Administrator on 2017/3/2.
 */
public interface DockingMappingProvider {
    void createDockingMapping(DockingMapping dockingMapping);

    DockingMapping findDockingMappingByScopeAndValue(Integer namespaceId, String scope, String value);

    DockingMapping findDockingMappingByScopeAndMappingValue(Integer namespaceId, String scope, String value);

    DockingMapping findDockingMappingById(Long id);

    void deleteDockingMapping(DockingMapping dockingMapping);

    void updateDockingMapping(DockingMapping dockingMapping);
}
