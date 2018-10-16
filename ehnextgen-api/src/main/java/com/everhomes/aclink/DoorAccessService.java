package com.everhomes.aclink;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.*;
import com.everhomes.user.User;

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

    ListAesUserKeyByUserResponse listAesUserKeyByUser(ListAesUserKeyByUserCommand cmd);

    List<AesUserKey> listAesUserKeyByUserId(Long userId);

    DoorAuthDTO createDoorAuth(CreateDoorAuthByUser cmd2);

    ListAesUserKeyByUserResponse listAdminAesUserKeyByUser();

    ListDoorAuthResponse queryDoorAuthByApproveId(ListDoorAuthCommand cmd);

    ListDoorAuthResponse searchDoorAuth(SearchDoorAuthCommand cmd);

    void updateDoorAccess(DoorAccessAdminUpdateCommand cmd);

    ListAesUserKeyByUserResponse listAdminAesUserKeyByUserAuth(ListAdminAesUserKeyCommand cmd);

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

    String aliTest2(HttpServletRequest request);

    ListDoorAuthLevelResponse listDoorAuthLevel(ListDoorAuthLevelCommand cmd);
    
    void deleteDoorAuthLevel(Long id);
    
    String faceTest();

    //要不要生成门禁二维码信息，如果是 web 端，则需要直接生成给 web
    ListDoorAccessQRKeyResponse listDoorAccessQRKeyAndGenerateQR(
    		DoorAccessDriverType driverType, boolean generate);

    void doAlipayRedirect(HttpServletRequest request,
            HttpServletResponse response);

    GetVisitorResponse getAlipayQR(HttpServletRequest r);

	public void excuteMessage(AclinkWebSocketMessage cmd);

    DoorAccessGroupResp listDoorAccessByUser(ListDoorAccessByUserCommand cmd);


	public QueryDoorAccessByServerResponse listDoorAccessByServerId(QueryDoorAccessByServerCommand cmd);

	public ListDoorAccessByGroupIdResponse listDoorAccessByGroupId(ListDoorAccessByGroupIdCommand cmd);

	public ListFacialRecognitionKeyByUserResponse listFacialAesUserKeyByUser(ListFacialRecognitionKeyByUserCommand cmd);

	public AesUserKey getAesUserKey(User user, DoorAuth doorAuth);

	public DoorAuthDTO createLocalVisitorAuth(CreateLocalVistorCommand cmd);

	public int invalidVistorAuth(Long DoorId, String phone);

	public ListDoorAccessQRKeyResponse listBusAccessQRKey();

	public void updateAccessType(Long doorId, byte doorType);

	public void deleteAuthByOwner(DeleteAuthByOwnerCommand cmd);
    //add by liqingyan
    Long deleteDoorAccessEh(Long doorAccessId);

    void changeDoorName(ChangeDoorNameCommand cmd);

    void addDoorManagement(AddDoorManagementCommand cmd);

    void deleteDoorManagement (AddDoorManagementCommand cmd);

    void changeUpdateFirmware (ChangeUpdateFirmwareCommand cmd);

    void addFirmware (AddFirmwareCommand cmd);

    void deleteFirmware (DeleteFirmwareCommand cmd);

    ListFirmwarePackageResponse listFirmwarePackage (ListFirmwarePackageCommand cmd);
    //add by liqingyan
    FirmwarePackageDTO uploadFirmwarePackage(uploadFirmwarePackageCommand cmd);

    void uploadWifi(uploadFirmwarePackageCommand cmd);

    void downloadBluetooth(DownloadBluetoothCommand cmd);

    void downloadWifi(DownloadBluetoothCommand cmd);

    void deleteBluetooth (DeleteBluetoothCommand cmd);

    void deleteWifi (DeleteBluetoothCommand cmd);

    ListDoorAccessEhResponse listDoorAccessEh(ListDoorAccessEhCommand cmd);

    ListDoorTypeResponse listDoorType  (ListDoorTypeCommand cmd);

    ListFirmwareResponse listFirmware (ListFirmwareCommand cmd);
    //add by liqingyan
    DoorStatisticEhResponse doorStatisticEh (DoorStatisticEhCommand cmd);
}
