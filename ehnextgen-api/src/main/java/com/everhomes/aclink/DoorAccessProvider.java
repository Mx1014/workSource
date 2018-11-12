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
    //门禁v3.0.6 左邻后台 add by liqingyan
    List<DoorAccessNewDTO> listDoorAccessEh(ListingLocator locator, int count,ListingQueryBuilderCallback queryBuilderCallback);

    DoorAccess findDoorAccessById(Long id);

    Long updateDoorAccessNew (DoorAccess obj);
	
	List<DoorAccessDTO> searchDoorAccessDTO(CrossShardListingLocator locator, QueryDoorAccessAdminCommand cmd);

	List<DoorAccessGroupRelDTO> listDoorGroupRel(CrossShardListingLocator locator, Integer count,
			ListDoorAccessGroupCommand cmd);
	//门禁v3.0.2 创建自定义表单
    Long createAclinkFormTitles(AclinkFormTitles form);

    List<AclinkFormTitlesDTO> searchAclinkFormTitles (ListingLocator locator,Integer count,ListingQueryBuilderCallback queryBuilderCallback);

    AclinkFormTitles findAclinkFormTitlesById (Long id);

    Long updateAclinkFormTitles(AclinkFormTitles form);

    Long createAclinkFormValues(AclinkFormValues value);

    List<AclinkFormValuesDTO> findAclinkFormValuesByAuthId (Long id);

    List<DoorsAndGroupsDTO> searchTempAuthPriority(ListTempAuthPriorityCommand cmd);

    AclinkFormValues findAclinkFormValuesById(Long id);

    AclinkFormValuesDTO updateAclinkFormValues(AclinkFormValues value);
    //门禁v3.0.2 添加企业管理授权
    Long createDoorManagement (AclinkManagement obj);

    List<AclinkManagementDTO> searchAclinkManagementByDoorId (Long doorId);

    List<AclinkManagementDTO> searchAclinkManagementByManager(Long managerId, Byte managerType);

    AclinkManagement findAclinkManagementById (Long id);

    Long updateAclinkManagement(AclinkManagement manager);
    //门禁v3.0.2 门禁分组 add by liqingyan
    AclinkGroup createDoorGroup(AclinkGroup group);

    AclinkGroup findAclinkGroupById(Long id);

    AclinkGroup updateDoorGroup(AclinkGroup group);


    //删除所有门禁组关系 add by liqingyan
    void deleteAllDoorGroupRel(Long id);

    List<DoorAccess> listNewDoorAccessByGroupId(Long groupId, int count);
//  旧方案不用
//    void createDoorGroupRel(Long groupId, Long doorId);

    AclinkGroupDoors createGroupDoors(AclinkGroupDoors door);

    AclinkGroupDoors updateGroupDoors(AclinkGroupDoors door);

    AclinkGroupDoors getGroupDoorsByDoorId(Long groupId, Long doorId);

    List<AclinkGroupDoors> getGroupDoorsByGroupId(Long groupId);

    List<AclinkGroupDoors> getGroupDoorsByOwnerId(Long ownerId,Byte ownerType);

    List<AclinkGroupDTO> listAclinkGroup (CrossShardListingLocator locator, Integer count,
                                                 ListDoorGroupCommand cmd);

}
