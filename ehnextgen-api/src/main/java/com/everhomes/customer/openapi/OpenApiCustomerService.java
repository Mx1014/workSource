package com.everhomes.customer.openapi;


import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListCommunitiesByCategoryCommand;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.openapi.CommunityResponse;
import com.everhomes.rest.customer.openapi.DeleteEnterpriseCommand;
import com.everhomes.rest.customer.openapi.ListBuildingResponse;
import com.everhomes.rest.customer.openapi.OpenApiUpdateCustomerCommand;
import com.everhomes.rest.organization.pm.PropFamilyDTO;

import java.util.List;

public interface OpenApiCustomerService {
    EnterpriseCustomerDTO createEnterpriseCustomer(OpenApiUpdateCustomerCommand cmd);

    EnterpriseCustomerDTO updateEnterpriseCustomer(OpenApiUpdateCustomerCommand cmd);

    EnterpriseCustomerDTO deleteEnterpriseCustomer(DeleteEnterpriseCommand cmd);

    void createEnterpriseAdmin(DeleteEnterpriseCommand cmd);

    void deleteEnterpriseAdmin(DeleteEnterpriseCommand cmd);

    List<PropFamilyDTO> listAddresses(Long buildingId);

    CommunityResponse listCommunitiesByCategory(ListCommunitiesByCategoryCommand cmd);

    ListBuildingResponse listBuildings(ListBuildingCommand cmd);
}
