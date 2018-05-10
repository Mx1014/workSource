// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.ui.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletResponse;

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

    GetConfigurationResponse getConfiguration(BaseVisitorsysCommand cmd);

    GetConfigurationResponse updateConfiguration(UpdateConfigurationCommand cmd);

    ListBlackListsResponse listBlackLists(ListBlackListsCommand cmd);

    void deleteBlackList(DeleteBlackListCommand cmd);

    CreateOrUpdateBlackListResponse createOrUpdateBlackList(CreateOrUpdateBlackListCommand cmd);

    ListDoorGuardsResponse listDoorGuards(ListDoorGuardsCommand cmd);

    GetInvitationLetterForWebResponse getInvitationLetterForWeb(GetInvitationLetterForWebCommand cmd);

    GetPairingCodeResponse getPairingCode(GetPairingCodeCommand cmd);

    DeferredResult<RestResponse> confirmPairingCode(ConfirmPairingCodeCommand cmd);

    GetConfigurationResponse getUIConfiguration(BaseVisitorsysUICommand cmd);

    CreateOrUpdateVisitorUIResponse createOrUpdateUIVisitor(CreateOrUpdateVisitorUICommand cmd);

    ListUIOfficeLocationsResponse listUIOfficeLocations(BaseVisitorsysUICommand cmd);

    ListUICommunityOrganizationsResponse listUICommunityOrganizations(BaseVisitorsysUICommand cmd);

    ListUIVisitReasonsResponse listUIVisitReasons(BaseVisitorsysUICommand cmd);

    void sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd);

    void confirmVerificationCode(ConfirmVerificationCodeCommand cmd);

    GetHomePageConfigurationResponse getHomePageConfiguration();

    GetEnterpriseFormResponse getEnterpriseForm(GetEnterpriseFormCommand cmd);

    GetEnterpriseFormForWebResponse getEnterpriseFormForWeb(GetEnterpriseFormForWebCommand cmd);

    void rejectVisitor(GetBookedVisitorByIdCommand cmd);

    void syncVisitor(BaseVisitorsysCommand cmd);

    void deleteVisitorAppoint(GetBookedVisitorByIdCommand cmd);

    GetBookedVisitorByIdResponse getUIBookedVisitorById(GetUIBookedVisitorByIdCommand cmd);

    void exportBookedVisitors(ListBookedVisitorsCommand cmd,HttpServletResponse resp);
}
