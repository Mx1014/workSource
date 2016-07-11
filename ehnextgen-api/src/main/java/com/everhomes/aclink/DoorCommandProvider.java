package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface DoorCommandProvider {

    Long createDoorCommand(DoorCommand obj);

    void updateDoorCommand(DoorCommand obj);

    void deleteDoorCommand(DoorCommand obj);

    DoorCommand getDoorCommandById(Long id);

    List<DoorCommand> queryDoorCommandByDoorId(ListingLocator locator, Long refId, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    List<DoorCommand> queryDoorCommands(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    List<DoorCommand> queryValidDoorCommands(ListingLocator locator, Long doorId, int count);

    DoorCommand queryActiveDoorCommand(Long doorId);

}
