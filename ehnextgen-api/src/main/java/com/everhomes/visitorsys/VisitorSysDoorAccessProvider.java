package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysDoorAccessProvider {

    VisitorSysDoorAccess createVisitorSysDoorAccess(VisitorSysDoorAccess bean);
    VisitorSysDoorAccess updateVisitorSysDoorAccess(VisitorSysDoorAccess bean);
    void deleteVisitorSysDoorAccesss(Long id);
    VisitorSysDoorAccess findVisitorSysDoorAccess(Long id);
    List<VisitorSysDoorAccess> listVisitorSysDoorAccessByOwner(Integer namespaceId, String ownerType, Long ownerId);
    List<VisitorSysDoorAccess> listVisitorSysDoorAccess(Integer namespaceId, String ownerType, Long ownerId,Long doorAccessId);
}
