package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface OwnerDoorProvider {

    Long createOwnerDoor(OwnerDoor obj);

    void updateOwnerDoor(OwnerDoor obj);

    void deleteOwnerDoor(OwnerDoor obj);

    OwnerDoor getOwnerDoorById(Long id);

    List<OwnerDoor> queryOwnerDoors(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

}
