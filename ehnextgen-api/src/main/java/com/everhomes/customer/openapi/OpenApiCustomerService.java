package com.everhomes.customer.openapi;


import com.everhomes.rest.customer.CreateEnterpriseCustomerCommand;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.UpdateEnterpriseCustomerCommand;

public interface OpenApiCustomerService {
    EnterpriseCustomerDTO createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd);
    EnterpriseCustomerDTO updateEnterpriseCustomer(UpdateEnterpriseCustomerCommand cmd);
}
