package com.everhomes.search;

import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.rest.customer.EasySearchEnterpriseCustomersCommand;
import com.everhomes.rest.customer.EasySearchEnterpriseCustomersDTO;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerResponse;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/17.
 */
public interface EnterpriseCustomerSearcher {
    void deleteById(Long id);

    void bulkUpdate(List<EnterpriseCustomer> customers);

    void feedDoc(EnterpriseCustomer customer);

    void syncFromDb();

    SearchEnterpriseCustomerResponse queryEnterpriseCustomersForOpenAPI(SearchEnterpriseCustomerCommand cmd);

    SearchEnterpriseCustomerResponse queryEnterpriseCustomers(SearchEnterpriseCustomerCommand cmd, Boolean isAdmin);

    List<EasySearchEnterpriseCustomersDTO> easyQueryEnterpriseCustomers(EasySearchEnterpriseCustomersCommand cmd);

    List<EasySearchEnterpriseCustomersDTO> listEnterpriseCustomers(EasySearchEnterpriseCustomersCommand cmd);

    SearchEnterpriseCustomerResponse queryEnterpriseCustomersById(SearchEnterpriseCustomerCommand cmd);
}
