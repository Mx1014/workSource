package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.DoorAccessOwnerType;

public interface DoorAccessProvider {

    List<DoorAccess> queryDoorAccesss(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    DoorAccess getDoorAccessById(Long id);

    void deleteDoorAccess(DoorAccess obj);

    void updateDoorAccess(DoorAccess obj);

    Long createDoorAccess(DoorAccess obj);

    DoorAccess queryDoorAccessByHardwareId(String hardware);

    List<DoorAccess> listDoorAccessByOwnerId(CrossShardListingLocator locator, Long ownerId,
            DoorAccessOwnerType ownerType, int count);

    DoorAccess queryDoorAccessByUuid(String uuid);

    List<DoorAccess> listDoorAccessByGroupId(Long groupId, int count);

}
