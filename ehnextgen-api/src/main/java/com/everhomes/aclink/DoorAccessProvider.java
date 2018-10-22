package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAccessGroupRelDTO;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.ListDoorAccessGroupCommand;
import com.everhomes.rest.aclink.QueryDoorAccessAdminCommand;

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

    List<DoorAccess> listAllDoorAccessLingling(Long ownerId, Byte ownerType, int count);

	List<DoorAccess> listDoorAccessByServerId(Long id, Integer count);
	//add by liqingyan
    List<ActiveDoorByNamespaceDTO> queryDoorAccessByNamespace(DoorStatisticEhCommand cmd);

    List<ActiveDoorByNamespaceDTO> queryDoorAccessByNamespaceNew(DoorStatisticEhCommand cmd);

    List<ActiveDoorByEquipmentDTO> queryDoorAccessByEquipment(DoorStatisticEhCommand cmd);

    List<ActiveDoorByFirmwareDTO> queryDoorAccessByFirmware(DoorStatisticEhCommand cmd);

    List<ActiveDoorByPlaceDTO> queryDoorAccessByPlace(DoorStatisticEhCommand cmd);

    List<ActiveDoorByPlaceDTO> queryDoorAccessByPlaceNew (DoorStatisticEhCommand cmd);
//add bu liqingyan
    List<DoorAccessNewDTO> listDoorAccessEh(ListingLocator locator, int count,ListingQueryBuilderCallback queryBuilderCallback);

    DoorAccess findDoorAccessById(Long id);

    Long updateDoorAccessNew (DoorAccess obj);
	
	List<DoorAccessDTO> searchDoorAccessDTO(CrossShardListingLocator locator, QueryDoorAccessAdminCommand cmd);

	List<DoorAccessGroupRelDTO> listDoorGroupRel(CrossShardListingLocator locator, Integer count,
			ListDoorAccessGroupCommand cmd);
}
