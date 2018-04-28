// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.visitorsys.*;

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
}
