package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.AclinkConnectingCommand;
import com.everhomes.rest.aclink.AclinkCreateDoorAuthListCommand;
import com.everhomes.rest.aclink.AclinkDisconnectedCommand;
import com.everhomes.rest.aclink.AclinkFirmwareDTO;
import com.everhomes.rest.aclink.AclinkLogCreateCommand;
import com.everhomes.rest.aclink.AclinkLogDTO;
import com.everhomes.rest.aclink.AclinkLogListResponse;
import com.everhomes.rest.aclink.AclinkMgmtCommand;
import com.everhomes.rest.aclink.AclinkQueryLogCommand;
import com.everhomes.rest.aclink.AclinkQueryLogResponse;
import com.everhomes.rest.aclink.AclinkUpdateLinglingStoreyCommand;
import com.everhomes.rest.aclink.AclinkUpgradeCommand;
import com.everhomes.rest.aclink.AclinkUpgradeResponse;
import com.everhomes.rest.aclink.AclinkUserResponse;
import com.everhomes.rest.aclink.AclinkWebSocketMessage;
import com.everhomes.rest.aclink.CreateAclinkFirmwareCommand;
import com.everhomes.rest.aclink.CreateDoorAccessGroup;
import com.everhomes.rest.aclink.CreateDoorAccessLingLing;
import com.everhomes.rest.aclink.CreateDoorAuthByUser;
import com.everhomes.rest.aclink.CreateDoorAuthCommand;
import com.everhomes.rest.aclink.CreateDoorVisitorCommand;
import com.everhomes.rest.aclink.CreateLinglingVisitorCommand;
import com.everhomes.rest.aclink.CreateQRUserPermissionCommand;
import com.everhomes.rest.aclink.DeleteQRUserPermissionCommand;
import com.everhomes.rest.aclink.DoorAccessActivedCommand;
import com.everhomes.rest.aclink.DoorAccessActivingCommand;
import com.everhomes.rest.aclink.DoorAccessAdminUpdateCommand;
import com.everhomes.rest.aclink.DoorAccessCapapilityDTO;
import com.everhomes.rest.aclink.DoorAccessDTO;
import com.everhomes.rest.aclink.DoorAccessOwnerType;
import com.everhomes.rest.aclink.DoorAuthDTO;
import com.everhomes.rest.aclink.DoorMessage;
import com.everhomes.rest.aclink.DoorUserPermissionDTO;
import com.everhomes.rest.aclink.GetCurrentFirmwareCommand;
import com.everhomes.rest.aclink.GetDoorAccessCapapilityCommand;
import com.everhomes.rest.aclink.GetShortMessageCommand;
import com.everhomes.rest.aclink.GetShortMessageResponse;
import com.everhomes.rest.aclink.GetVisitorCommand;
import com.everhomes.rest.aclink.GetVisitorResponse;
import com.everhomes.rest.aclink.ListAclinkUserCommand;
import com.everhomes.rest.aclink.ListAesUserKeyByUserResponse;
import com.everhomes.rest.aclink.ListDoorAccessByOwnerIdCommand;
import com.everhomes.rest.aclink.ListDoorAccessGroupCommand;
import com.everhomes.rest.aclink.ListDoorAccessQRKeyResponse;
import com.everhomes.rest.aclink.ListDoorAccessResponse;
import com.everhomes.rest.aclink.ListDoorAuthCommand;
import com.everhomes.rest.aclink.ListDoorAuthResponse;
import com.everhomes.rest.aclink.ListQRUserPermissionCommand;
import com.everhomes.rest.aclink.ListQRUserPermissionResponse;
import com.everhomes.rest.aclink.QueryDoorAccessAdminCommand;
import com.everhomes.rest.aclink.QueryDoorMessageCommand;
import com.everhomes.rest.aclink.QueryDoorMessageResponse;
import com.everhomes.rest.aclink.SearchDoorAuthCommand;

public interface DoorAccessService {
    public void onDoorMessageTimeout(Long cmdId);

    List<DoorAccessDTO> listDoorAccessByOwnerId(CrossShardListingLocator locator, Long ownerId,
            DoorAccessOwnerType ownerType, int count);

    ListDoorAccessResponse listDoorAccessByOwnerId(ListDoorAccessByOwnerIdCommand cmd);

    DoorAuthDTO createDoorAuth(CreateDoorAuthCommand cmd);

    ListDoorAccessResponse searchDoorAccessByAdmin(QueryDoorAccessAdminCommand cmd);

    AclinkUserResponse listAclinkUsers(ListAclinkUserCommand cmd);

    DoorMessage activatingDoorAccess(DoorAccessActivingCommand cmd);

    QueryDoorMessageResponse activateDoorAccess(DoorAccessActivedCommand cmd);

    QueryDoorMessageResponse queryDoorMessageByDoorId(QueryDoorMessageCommand cmd);

    Long deleteDoorAuth(Long doorAuthId);

    Long deleteDoorAccess(Long doorAccessId);

    ListAesUserKeyByUserResponse listAesUserKeyByUser();

    List<AesUserKey> listAesUserKeyByUserId(Long userId);

    DoorAuthDTO createDoorAuth(CreateDoorAuthByUser cmd2);

    ListAesUserKeyByUserResponse listAdminAesUserKeyByUser();

    ListDoorAuthResponse queryDoorAuthByApproveId(ListDoorAuthCommand cmd);

    ListDoorAuthResponse searchDoorAuth(SearchDoorAuthCommand cmd);

    void updateDoorAccess(DoorAccessAdminUpdateCommand cmd);

    ListAesUserKeyByUserResponse listAdminAesUserKeyByUserAuth();

    DoorAccess onDoorAccessConnecting(AclinkConnectingCommand cmd);

    void onDoorAcessDisconnected(AclinkDisconnectedCommand cmd);

    AclinkWebSocketMessage syncWebSocketMessages(AclinkWebSocketMessage resp);

    AclinkFirmwareDTO createAclinkFirmware(CreateAclinkFirmwareCommand cmd);

    DoorAccessDTO getDoorAccessDetail(String hardware);

    AclinkUpgradeResponse upgradeFirmware(AclinkUpgradeCommand cmd);

    String upgradeVerify(AclinkUpgradeCommand cmd);

    ListDoorAccessQRKeyResponse listDoorAccessQRKey();

    DoorAccessCapapilityDTO getDoorAccessCapapility(GetDoorAccessCapapilityCommand cmd);

    DoorAccessDTO createDoorAccessGroup(CreateDoorAccessGroup cmd);

    DoorAccessDTO createDoorAccessLingLing(CreateDoorAccessLingLing cmd);

    GetVisitorResponse getVisitor(GetVisitorCommand cmd);

    DoorMessage queryWifiMgmtMessage(AclinkMgmtCommand cmd);

    ListDoorAccessResponse listDoorAccessGroup(ListDoorAccessGroupCommand cmd);

    AclinkFirmwareDTO getCurrentFirmware(GetCurrentFirmwareCommand cmd);

    ListDoorAuthResponse createDoorAuthList(AclinkCreateDoorAuthListCommand cmd);

    ListDoorAuthResponse searchVisitorDoorAuth(SearchDoorAuthCommand cmd);

    void sendMessageToUser(Long uid, Long doorId, Byte doorType);

    void remoteOpenDoor(Long doorId);

    DoorAuthDTO createDoorVisitorAuth(CreateDoorVisitorCommand cmd);

    ListDoorAccessQRKeyResponse updateAndQueryQR(AclinkUpdateLinglingStoreyCommand cmd);

    GetShortMessageResponse getShortMessages(GetShortMessageCommand cmd);

    AclinkLogListResponse createAclinkLog(AclinkLogCreateCommand cmd);

    AclinkQueryLogResponse queryLogs(AclinkQueryLogCommand cmd);

    void test();

    DoorAuth getLinglingDoorAuthByUuid(String uuid);

    GetVisitorResponse getVisitorPhone(GetVisitorCommand cmd);

    GetVisitorResponse checkVisitor(GetVisitorCommand cmd);

    DoorUserPermissionDTO createQRUserPermission(CreateQRUserPermissionCommand cmd);

    DoorUserPermissionDTO deleteQRUserPermission(DeleteQRUserPermissionCommand cmd);

    ListQRUserPermissionResponse listQRUserPermissions(ListQRUserPermissionCommand cmd);

	void deleteAuthWhenLeaveFromOrg(Integer namespaceId, Long orgId, Long userId);
}
