// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.ui.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/27 15:18
 */
public class VisitorSysServiceImpl implements VisitorSysService{
    @Override
    public ListBookedVisitorsResponse listBookedVisitors(ListBookedVisitorsCommand cmd) {
        return null;
    }

    @Override
    public GetBookedVisitorByIdResponse getBookedVisitorById(GetBookedVisitorByIdCommand cmd) {
        return null;
    }

    @Override
    public ListOfficeLocationsResponse listOfficeLocations(ListOfficeLocationsCommand cmd) {
        return null;
    }

    @Override
    public ListCommunityOrganizationsResponse listCommunityOrganizations(ListCommunityOrganizationsCommand cmd) {
        return null;
    }

    @Override
    public ListVisitReasonsResponse listVisitReasons(BaseVisitorsysCommand cmd) {
        return null;
    }

    @Override
    public GetBookedVisitorByIdResponse createOrUpdateVisitor(CreateOrUpdateVisitorCommand cmd) {
        return null;
    }

    @Override
    public void sendVisitorSMS(GetBookedVisitorByIdCommand cmd) {

    }

    @Override
    public void deleteVisitor(GetBookedVisitorByIdCommand cmd) {

    }

    @Override
    public void confirmVisitor(GetBookedVisitorByIdCommand cmd) {

    }

    @Override
    public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
        return null;
    }

    @Override
    public AddDeviceResponse addDevice(AddDeviceCommand cmd) {
        return null;
    }

    @Override
    public ListDevicesResponse listDevices(BaseVisitorsysCommand cmd) {
        return null;
    }

    @Override
    public void deleteDevice(DeleteDeviceCommand cmd) {

    }

    @Override
    public BaseOfficeLocationDTO createOrUpdateOfficeLocation(CreateOrUpdateOfficeLocationCommand cmd) {
        return null;
    }

    @Override
    public void deleteOfficeLocation(DeleteOfficeLocationCommand cmd) {

    }

    @Override
    public GetConfigurationResponse getConfiguration(BaseVisitorsysCommand cmd) {
        return null;
    }

    @Override
    public GetConfigurationResponse updateConfiguration(UpdateConfigurationCommand cmd) {
        return null;
    }

    @Override
    public ListBlackListsResponse listBlackLists(ListBlackListsCommand cmd) {
        return null;
    }

    @Override
    public void deleteBlackList(DeleteBlackListCommand cmd) {

    }

    @Override
    public CreateOrUpdateBlackListResponse createOrUpdateBlackList(CreateOrUpdateBlackListCommand cmd) {
        return null;
    }

    @Override
    public ListDoorGuardsResponse listDoorGuards(BaseVisitorsysCommand cmd) {
        return null;
    }

    @Override
    public GetInvitationLetterForWebResponse getInvitationLetterForWeb(GetInvitationLetterForWebCommand cmd) {
        return null;
    }

    @Override
    public GetPairingCodeResponse getPairingCode(GetPairingCodeCommand cmd) {
        return null;
    }

    @Override
    public DeferredResult<RestResponse> confirmPairingCode(ConfirmPairingCodeCommand cmd) {
        return null;
    }

    @Override
    public GetConfigurationResponse getUIConfiguration(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public CreateOrUpdateVisitorUIResponse createOrUpdateUIVisitor(CreateOrUpdateVisitorUICommand cmd) {
        return null;
    }

    @Override
    public ListOfficeLocationsResponse listUIOfficeLocations(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public ListOfficeLocationsResponse listUICommunityOrganizations(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public ListVisitReasonsResponse listUIVisitReasons(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public void sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd) {

    }

    @Override
    public void confirmVerificationCode(ConfirmVerificationCodeCommand cmd) {

    }
}
