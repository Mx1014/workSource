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
    //临时授权访客来访消息提示 add by liqingyan
    void sendMessageToAuthCreator(Long creatorId, Long visitorId, Long doorId);

    void remoteOpenDoor(Long doorId);

    DoorAuthDTO createDoorVisitorAuth(CreateDoorVisitorCommand cmd);

    ListDoorAccessQRKeyResponse updateAndQueryQR(AclinkUpdateLinglingStoreyCommand cmd);

    GetShortMessageResponse getShortMessages(GetShortMessageCommand cmd);

    AclinkLogListResponse createAclinkLog(AclinkLogCreateCommand cmd);

    AclinkQueryLogResponse queryLogs(AclinkQueryLogCommand cmd);

    //20180914 add by liqingyan
    CheckMobilePrivilegeResponse checkMobilePrivilege(CheckMobilePrivilegeCommand cmd);

//    void test();

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

    void exportTempAuthXls(SearchDoorAuthCommand cmd, HttpServletResponse httpResponse);

    //add by liqingyan
    void exportAclinkLogsXls(AclinkQueryLogCommand cmd, HttpServletResponse httpResponse);

    //add by liqingyan
    DoorStatisticResponse doorStatistic(DoorStatisticCommand cmd);

    DoorStatisticByTimeResponse doorStatisticByTime(DoorStatisticByTimeCommand cmd);

    TempStatisticByTimeResponse tempStatisticByTime(TempStatisticByTimeCommand cmd);

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

//    String faceTest();

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

    public int invalidVistorAuth(Long DoorId, String phone, Byte groupType);

    public ListDoorAccessQRKeyResponse listBusAccessQRKey();

    public void updateAccessType(Long doorId, byte doorType);

    public ListZLDoorAccessResponse listDoorAccessMacByApp();

    public GetZLAesUserKeyResponse getAppAesUserKey(GetZLAesUserKeyCommand cmd);

    public void createVisitorBatch(CreateVisitorBatchCommand cmd);

    public CreateZLVisitorQRKeyResponse createZLVisitorQRKey(CreateZLVisitorQRKeyCommand cmd);

    public ListDoorAccessLiteResponse listDoorAccessByOwnerIdLite(QueryDoorAccessAdminCommand cmd);

    public DoorAccessDTO getDoorAccessById(GetDoorAccessByIdCommand cmd);

    public void deleteAuthByOwner(DeleteAuthByOwnerCommand cmd);

    //门禁v3.0.6 add by liqingyan
    Long deleteDoorAccessEh(Long doorAccessId);

    void changeDoorName(ChangeDoorNameCommand cmd);

    ChangeUpdateFirmwareResponse changeUpdateFirmware(ChangeUpdateFirmwareCommand cmd);

    FirmwareNewDTO addFirmware(AddFirmwareCommand cmd);

    FirmwareNewDTO deleteFirmware(DeleteFirmwareCommand cmd);

    ListFirmwarePackageResponse listFirmwarePackage(ListFirmwarePackageCommand cmd);

    FirmwarePackageDTO uploadFirmwarePackage(UploadFirmwarePackageCommand cmd);

    FirmwarePackageDTO deleteFirmwarePackage(DeleteFirmwarePackageCommand cmd);

    void uploadWifi(UploadFirmwarePackageCommand cmd);

    void downloadBluetooth(DownloadBluetoothCommand cmd);

    void downloadWifi(DownloadBluetoothCommand cmd);

    void deleteBluetooth(DeleteBluetoothCommand cmd);

    void deleteWifi(DeleteBluetoothCommand cmd);

    ListDoorAccessEhResponse listDoorAccessEh(ListDoorAccessEhCommand cmd);

    ListDoorTypeResponse listDoorType(ListDoorTypeCommand cmd);

    ListFirmwareResponse listFirmware(ListFirmwareCommand cmd);

    DoorStatisticEhResponse doorStatisticEh(DoorStatisticEhCommand cmd);

    //门禁v3.0.2 创建临时授权 add by liqingyan
    DoorAuthDTO createTempAuth(CreateTempAuthCommand cmd);

    ListDoorAuthResponse listTempAuth(SearchDoorAuthCommand cmd);

    //门禁v3.0.1
    public ListFormalAuthResponse listFormalAuth(ListFormalAuthCommand cmd);

    public void updateAuthBatch(UpdateAuthBatchCommand cmd);

    public void createFormalAuthBatch(CreateFormalAuthBatchCommand cmd);

    public ListUserAuthResponse listUserKeys(ListAesUserKeyByUserCommand cmd);

    public GetUserKeyInfoRespnose getUserKeyInfo(GetUserKeyInfoCommand cmd);

    public ListAccessGroupRelResponse listDoorGroupRel(ListDoorAccessGroupCommand cmd);

    //门禁v3.0.2 添加门禁自定义字段 add by liqingyan
    void createTempAuthCustomField(CreateTempAuthCustomFieldCommand cmd);

    ListTempAuthCustomFieldResponse listTempAuthCustomField(ListTempAuthCustomFieldCommand cmd);

    AclinkFormTitlesDTO changeTempAuthCustomField(ChangeTempAuthCustomFieldCommand cmd);

    BatchCreateVisitorsResponse batchCreateVisitors(BatchCreateVisitorsCommand cmd);

    void invalidVistorAuths(InvalidVistorAuthsCommand cmd);

    OpenQueryLogResponse openQueryLogs(OpenQueryLogCommand cmd);
    //门禁v3.0.2 临时授权优先门禁 add by liqingyan
    void createTempAuthPriority (CreateTempAuthPriorityCommand cmd);

    ListTempAuthPriorityResponse listTempAuthPriority (ListTempAuthPriorityCommand cmd);

    DeleteTempAuthPriorityResponse deleteTempAuthPriority(DeleteTempAuthPriorityCommand cmd);

    void createTempAuthDefaultRule(CreateTempAuthDefaultRuleCommand cmd);

    ListTempAuthDefaultRuleResponse listTempAuthDefaultRule (ListTempAuthDefaultRuleCommand cmd);
    /**
	 * 常规授权,授权/取消权限,园区下的所有门禁,单个用户/企业下的所有用户,1成功,0失败 
	 */
	public UpdateFormalAuthByCommunityResponse updateFormalAuthByCommunity(UpdateFormalAuthByCommunityCommand cmd);

	public String getVisitorUrlById(GetVisitorCommand cmd);
    //门禁v3.0.2 添加管理授权企业 add by liqingyan
    void addDoorManagement(AddDoorManagementCommand cmd);

    ListDoorManagementResponse listDoorManagement(ListDoorManagementCommand cmd);

    void deleteDoorManagement(DeleteDoorManagementCommand cmd);

    //门禁v3.0.2 创建门禁分组 add by liqingyan
    AclinkGroup createDoorGroup(CreateDoorAccessGroupCommand cmd);

    void updateDoorGroup(UpdateDoorAccessGroupCommand cmd);

    ListDoorGroupResponse listDoorGroupNew(ListDoorGroupCommand cmd);

    ListGroupDoorsResponse listGroupDoors(ListGroupDoorsCommand cmd);

    void deleteDoorGroupRel (DeleteDoorGroupRelCommand cmd);
    //列出可加入门禁组门禁
    ListSelectDoorsResponse listSelectDoors(ListSelectDoorsCommand cmd);
    //列出所有管理门禁(组)
    ListSelectDoorsAndGroupsResponse listSelectDoorsAndGroups (ListSelectDoorsAndGroupsCommand cmd);
    //查询门禁关联服务器、摄像头
    SearchDoorServerResponse searchDoorServer (SearchDoorServerCommand cmd);
    //服务热线
    QueryServiceHotlineResponse queryServiceHotline (QueryServiceHotlineCommand cmd);

    void updateServiceHotline (UpdateServiceHotlineCommand cmd);
    //访客来访提示
    VisitorComingNoticeResponse visitorComingNotice (VisitorComingNoticeCommand cmd);
}
