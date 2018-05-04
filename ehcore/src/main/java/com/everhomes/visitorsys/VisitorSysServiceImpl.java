// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.rest.visitorsys.ui.*;
import com.everhomes.search.VisitorsysSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/27 15:18
 */
@Component
public class VisitorSysServiceImpl implements VisitorSysService{
    @Autowired
    public VisitorSysVisitorProvider visitorSysVisitorProvider;
    @Autowired
    public ConfigurationProvider configurationProvider;
    @Autowired
    public CommunityProvider communityProvider;
    @Autowired
    public OrganizationProvider organizationProvider;
    @Autowired
    public VisitorsysSearcher visitorsysSearcher;

    @Override
    public ListBookedVisitorsResponse listBookedVisitors(ListBookedVisitorsCommand cmd) {
        checkOwnerType(cmd.getOwnerType());

        Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        Long pageAnchor = cmd.getPageAnchor() == null ? 0 : cmd.getPageAnchor();
        ListBookedVisitorParams params = ConvertHelper.convert(cmd, ListBookedVisitorParams.class);
        params.setPageSize(pageSize);
        params.setPageAnchor(pageAnchor);

        return visitorsysSearcher.searchVisitors(params);
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
    public ListUIOfficeLocationsResponse listUIOfficeLocations(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public ListUICommunityOrganizationsResponse listUICommunityOrganizations(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public ListUIVisitReasonsResponse listUIVisitReasons(BaseVisitorsysUICommand cmd) {
        return null;
    }

    @Override
    public void sendSMSVerificationCode(SendSMSVerificationCodeCommand cmd) {

    }

    @Override
    public void confirmVerificationCode(ConfirmVerificationCodeCommand cmd) {

    }

    @Override
    public GetHomePageConfigurationResponse getHomePageConfiguration() {
        return null;
    }

    @Override
    public GetEnterpriseFormResponse getEnterpriseForm(GetEnterpriseFormCommand cmd) {
        return null;
    }

    @Override
    public GetEnterpriseFormForWebResponse getEnterpriseFormForWeb(GetEnterpriseFormForWebCommand cmd) {
        return null;
    }

    private void checkOwner(String ownerType, Long ownerId) {
        VisitorsysOwnerType visitorsysOwnerType = checkOwnerType(ownerType);
        switch (visitorsysOwnerType){
            case COMMUNITY:
                Community community = communityProvider.findCommunityById(ownerId);
                if(community==null){
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "unknown ownerId "+ownerId);
                }
                break;
            case ENTERPRISE:
                Organization organization = organizationProvider.findOrganizationById(ownerId);
                if(organization==null){
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "unknown ownerId "+ownerId);
                }
                break;
        }

    }

    private VisitorsysOwnerType checkOwnerType(String ownerType) {
        VisitorsysOwnerType visitorsysOwnerType = VisitorsysOwnerType.fromCode(ownerType);
        if(visitorsysOwnerType==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "unknown ownerType "+ownerType);
        }
        return visitorsysOwnerType;
    }
}
