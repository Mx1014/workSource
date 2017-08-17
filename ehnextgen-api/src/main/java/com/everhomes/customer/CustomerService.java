package com.everhomes.customer;

import com.everhomes.rest.customer.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ying.xiong on 2017/8/15.
 */
public interface CustomerService {
    void createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd);
    void updateEnterpriseCustomer(UpdateEnterpriseCustomerCommand cmd);
    void deleteEnterpriseCustomer(DeleteEnterpriseCustomerCommand cmd);
    SearchEnterpriseCustomerResponse searchEnterpriseCustomer(SearchEnterpriseCustomerCommand cmd);
    ImportFileTaskDTO importEnterpriseCustomerData(ImportEnterpriseCustomerDataCommand cmd, MultipartFile[] files);
    EnterpriseCustomerDTO getEnterpriseCustomer(GetEnterpriseCustomerCommand cmd);
}
