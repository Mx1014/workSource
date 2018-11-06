// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.GetFormCommand;
import com.everhomes.rest.visitorsys.ui.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/27 15:18
 */
public interface VisitorSysService {
    ListBookedVisitorsResponse listBookedVisitors(ListBookedVisitorsCommand cmd);

    GetBookedVisitorByIdResponse getBookedVisitorById(GetBookedVisitorByIdCommand cmd);

    ListOfficeLocationsResponse listOfficeLocations(ListOfficeLocationsCommand cmd);

    ListCommunityOrganizationsResponse listCommunityOrganizations(ListCommunityOrganizationsCommand cmd);

    ListVisitReasonsResponse listVisitReasons(BaseVisitorsysCommand cmd);

    GetBookedVisitorByIdResponse createOrUpdateVisitor(CreateOrUpdateVisitorCommand cmd);

    void sendVisitorSMS(GetBookedVisitorByIdCommand cmd);

    void deleteVisitor(GetBookedVisitorByIdCommand cmd);

    void confirmVisitor(CreateOrUpdateVisitorCommand cmd);

    GetStatisticsResponse getStatistics(GetStatisticsCommand cmd);

    AddDeviceResponse addDevice(AddDeviceCommand cmd);

    ListDevicesResponse listDevices(BaseVisitorsysCommand cmd);

    void deleteDevice(DeleteDeviceCommand cmd);

    BaseOfficeLocationDTO createOrUpdateOfficeLocation(CreateOrUpdateOfficeLocationCommand cmd);

    void deleteOfficeLocation(DeleteOfficeLocationCommand cmd);

    GetConfigurationResponse getConfiguration(GetConfigurationCommand cmd);

    GetConfigurationResponse updateConfiguration(UpdateConfigurationCommand cmd);

    ListBlackListsResponse listBlackLists(ListBlackListsCommand cmd);

    void deleteBlackList(DeleteBlackListCommand cmd);

    CreateOrUpdateBlackListResponse createOrUpdateBlackList(CreateOrUpdateBlackListCommand cmd);

    ListDoorGuardsResponse listDoorGuards(ListDoorGuardsCommand cmd);

    GetInvitationLetterForWebResponse getInvitationLetterForWeb(GetInvitationLetterForWebCommand cmd);

    GetPairingCodeResponse getPairingCode(GetPairingCodeCommand cmd);

    void confirmPairingCode(ConfirmPairingCodeCommand cmd);

    GetConfigurationResponse getUIConfiguration(BaseVisitorsysUICommand cmd);

    CreateOrUpdateVisitorUIResponse createOrUpdateUIVisitor(CreateOrUpdateVisitorUICommand cmd);

    ListUIOfficeLocationsResponse listUIOfficeLocations(ListUIOfficeLocationsCommand cmd);

    ListUICommunityOrganizationsResponse listUICommunityOrganizations(ListUICommunityOrganizationsCommand cmd);

    ListUIVisitReasonsResponse listUIVisitReasons(BaseVisitorsysUICommand cmd);

    void sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd);

    ListBookedVisitorsResponse confirmVerificationCode(ConfirmVerificationCodeCommand cmd);

    GetHomePageConfigurationResponse getHomePageConfiguration();

    GetFormResponse getUIForm(GetUIFormCommand cmd);

    GetFormResponse getForm(GetFormCommand cmd);

    GetFormForWebResponse getFormForWeb(GetFormForWebCommand cmd);

    void rejectVisitor(CreateOrUpdateVisitorCommand cmd);

    void syncVisitor(BaseVisitorsysCommand cmd);

    void deleteVisitorAppoint(GetBookedVisitorByIdCommand cmd);

    GetBookedVisitorByIdResponse getUIBookedVisitorById(GetUIBookedVisitorByIdCommand cmd);

    void exportBookedVisitors(ListBookedVisitorsCommand cmd,HttpServletResponse resp);

    GetConfigurationResponse getConfigurationForWeb(GetConfigurationForWebCommand cmd);

    void transferQrcode(TransferQrcodeCommand qrcode, HttpServletResponse resp);

    GetBookedVisitorByIdResponse createOrUpdateVisitorForWeb(CreateOrUpdateVisitorCommand cmd);

    ListBookedVisitorsResponse listBookedVisitorsForWeb(ListBookedVisitorsCommand cmd);

    GetBookedVisitorByIdResponse getBookedVisitorByIdForWeb(GetBookedVisitorByIdCommand cmd);

    ListVisitReasonsResponse listVisitReasonsForWeb(BaseVisitorsysCommand cmd);

    void deleteVisitorAppointForWeb(GetBookedVisitorByIdCommand cmd);

    ListOfficeLocationsResponse listOfficeLocationsForWeb(ListOfficeLocationsCommand cmd);

    ListCommunityOrganizationsResponse listCommunityOrganizationsForWeb(ListCommunityOrganizationsCommand cmd);

    ListBookedVisitorsResponse confirmVerificationCodeForWeb(ConfirmVerificationCodeForWebCommand cmd);

    void sendSMSVerificationCodeForWeb(SendSMSVerificationCodeForWebCommand cmd);

    GetUploadFileTokenResponse getUploadFileToken(GetUIUploadFileTokenCommand cmd);

    GetUploadFileTokenResponse getUploadFileTokenForWeb(GetUploadFileTokenCommand cmd);

    void checkBlackList(CheckBlackListCommand cmd);

    void checkBlackListForWeb(CheckBlackListForWebCommand cmd);

    ListBookedVisitorsResponse listBookedVisitorsForManage(ListBookedVisitorsCommand cmd);

    GetBookedVisitorByIdResponse getBookedVisitorByIdForManage(GetBookedVisitorByIdCommand cmd);

    ListVisitReasonsResponse listVisitReasonsForManage(BaseVisitorsysCommand cmd);

    GetBookedVisitorByIdResponse createOrUpdateVisitorForManage(CreateOrUpdateVisitorCommand cmd);

    void confirmVisitorForManage(CreateOrUpdateVisitorCommand cmd);

    void rejectVisitorForManage(CreateOrUpdateVisitorCommand cmd);

    void updateMessageReceiverForManage(UpdateMessageReceiverCommand cmd);

    GetMessageReceiverForManageResponse getMessageReceiverForManage(BaseVisitorsysCommand cmd);

    OpenApiListOrganizationsResponse openApiListOrganizations(OpenApiListOrganizationsCommand cmd);

    OpenApiCreateVisitorResponse openApiCreateVisitor(OpenApiCreateVisitorCommand cmd);

    IdentifierCardDTO getIDCardInfo();

    List<VisitorSysDoorAccessDTO> listDoorAccess(BaseVisitorsysCommand cmd);

    void createDoorAccess(CreateOrUpdateDoorAccessCommand cmd);

    void deleteDoorAccess(DeleteDoorAccessCommand cmd);

    void setDefaultAccess(CreateOrUpdateDoorAccessCommand cmd);

    void removeInvalidTime();

    GetConfigurationResponse getConfigurationForManage(GetConfigurationCommand cmd);

    List<VisitorSysDoorAccessDTO> listDoorAccessForManage(BaseVisitorsysCommand cmd);
}
