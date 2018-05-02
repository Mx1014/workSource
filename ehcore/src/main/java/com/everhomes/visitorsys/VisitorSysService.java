// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.CreateNewsResponse;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.ui.*;
import org.springframework.web.context.request.async.DeferredResult;

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

    void confirmVisitor(GetBookedVisitorByIdCommand cmd);

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

    ListDoorGuardsResponse listDoorGuards(BaseVisitorsysCommand cmd);

    GetInvitationLetterForWebResponse getInvitationLetterForWeb(GetInvitationLetterForWebCommand cmd);

    GetPairingCodeResponse getPairingCode(GetPairingCodeCommand cmd);

    DeferredResult<RestResponse> confirmPairingCode(ConfirmPairingCodeCommand cmd);

    GetConfigurationResponse getUIConfiguration(BaseVisitorsysUICommand cmd);

    CreateOrUpdateVisitorUIResponse createOrUpdateUIVisitor(CreateOrUpdateVisitorUICommand cmd);

    ListOfficeLocationsResponse listUIOfficeLocations(BaseVisitorsysUICommand cmd);

    ListOfficeLocationsResponse listUICommunityOrganizations(BaseVisitorsysUICommand cmd);

    ListVisitReasonsResponse listUIVisitReasons(BaseVisitorsysUICommand cmd);

    void sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd);

    void confirmVerificationCode(ConfirmVerificationCodeCommand cmd);
}
