package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.DoorUserPermissionDTO;

public interface DoorUserPermissionProvider {

    Long createDoorUserPermission(DoorUserPermission obj);

    void updateDoorUserPermission(DoorUserPermission obj);

    void deleteDoorUserPermission(DoorUserPermission obj);

    DoorUserPermission getDoorUserPermissionById(Long id);

    List<DoorUserPermission> queryDoorUserPermissions(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    DoorUserPermission checkPermission(Integer namespaceId, Long userId, Long ownerId, Byte ownerType);

    List<DoorUserPermissionDTO> listDoorUserPermissions(Integer namespaceId, Long ownerId, Byte ownerType,
            ListingLocator locator, int count);

}
