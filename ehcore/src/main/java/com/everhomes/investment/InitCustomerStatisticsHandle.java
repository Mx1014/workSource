package com.everhomes.investment;

import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.filedownload.TaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class InitCustomerStatisticsHandle implements TaskHandler {

    @Autowired
    private InvitedCustomerProvider customerProvider;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {

        Long nextAnchor = 0L;
        if(params.get("nextAnchor") != null)
            nextAnchor = (Long) params.get("nextAnchor");

        Integer pageSize = 100;
        if(params.get("pageSize") != null)
            pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));

        Integer namespaceId = null;
        if(params.get("namespaceId") != null){
            namespaceId = Integer.valueOf(String.valueOf(params.get("namespaceId")));
        }

        while(nextAnchor != -1L){
            List<EnterpriseCustomer> customers = customerProvider.getInitCustomerStatus(namespaceId, pageSize, nextAnchor);
            if(customers != null && customers.size() > 0){
                if(customers.size() >= pageSize + 1){
                    nextAnchor = customers.get(customers.size() - 1).getId();
                    customers.remove(customers.size() - 1);
                }else{
                    nextAnchor = -1L;
                }
            }


        }
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
