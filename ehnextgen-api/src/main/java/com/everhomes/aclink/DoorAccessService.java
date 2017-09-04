package com.everhomes.aclink;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

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

    ListDoorAuthResponse createAllDoorAuthList(AclinkCreateAllDoorAuthListCommand cmd);

    ListDoorAuthLogResponse listDoorAuthLogs(ListDoorAuthLogCommand cmd);

    DoorAuthStatisticsDTO qryDoorAuthStatistics(QryDoorAuthStatisticsCommand cmd);

    void exportAclinkUsersXls(ListAclinkUserCommand cmd, HttpServletResponse response);

    String checkAllDoorAuthList();

	public void remoteOpenDoor(String hardwareId);

	AclinkGetServerKeyResponse getServerKey(AclinkGetServerKeyCommand cmd);

	public QueryDoorMessageResponse syncTimerMessage(AclinkSyncTimerCommand cmd);

	void joinCompanyAutoAuth(Integer namespaceId, Long orgId, Long userId);

    void exportVisitorDoorAuth(ExportDoorAuthCommand cmd, HttpServletResponse httpResponse);

    DoorAuthLevelDTO createDoorAuthLevel(CreateDoorAuthLevelCommand cmd);

    void aliTest(HttpServletRequest request);

    String aliTest2(HttpServletRequest request);

    ListDoorAuthLevelResponse listDoorAuthLevel(ListDoorAuthLevelCommand cmd);
    
    void deleteDoorAuthLevel(Long id);
    
}
