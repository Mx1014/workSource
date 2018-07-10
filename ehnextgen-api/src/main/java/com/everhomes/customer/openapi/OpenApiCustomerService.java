package com.everhomes.customer.openapi;


import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.openapi.DeleteEnterpriseCommand;
import com.everhomes.rest.customer.openapi.OpenApiUpdateCustomerCommand;

public interface OpenApiCustomerService {
    EnterpriseCustomerDTO createEnterpriseCustomer(OpenApiUpdateCustomerCommand cmd);

    EnterpriseCustomerDTO updateEnterpriseCustomer(OpenApiUpdateCustomerCommand cmd);

    EnterpriseCustomerDTO deleteEnterpriseCustomer(DeleteEnterpriseCommand cmd);

    void createEnterpriseAdmin(DeleteEnterpriseCommand cmd);

    void deleteEnterpriseAdmin(DeleteEnterpriseCommand cmd);
}
