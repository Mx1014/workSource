package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.ListDoorAuthLevelCommand;
import com.everhomes.rest.aclink.ListDoorAuthLevelResponse;

public interface DoorAuthLevelProvider {

    Long createDoorAuthLevel(DoorAuthLevel obj);

    void updateDoorAuthLevel(DoorAuthLevel obj);

    void deleteDoorAuthLevel(DoorAuthLevel obj);

    DoorAuthLevel getDoorAuthLevelById(Long id);

    List<DoorAuthLevel> queryDoorAuthLevels(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    ListDoorAuthLevelResponse findAuthLevels(ListDoorAuthLevelCommand cmd);

    List<DoorAuthLevel> findAuthLevels(Long levelId, Byte levelType);

    DoorAuthLevel findAuthLevel(Long levelId, Byte levelType, Long doorId);

    ListDoorAuthLevelResponse findAuthLevelsWithOrg(ListDoorAuthLevelCommand cmd);

    ListDoorAuthLevelResponse findAuthLevelsWithBuilding(ListDoorAuthLevelCommand cmd);

	void updateDoorAuthLevelBatch(List<DoorAuthLevel> ulevels);

}
