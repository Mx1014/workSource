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

    @Autowired
    private InvitedCustomerService customerService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {

        Long nextAnchor = 0L;

        Integer pageSize = 100;
        if(params.get("pageSize") != null)
            pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));


        while(nextAnchor != -1L){
            List<EnterpriseCustomer> customers = customerProvider.getInitCustomerStatus(pageSize, nextAnchor);
            if(customers != null && customers.size() > 0){
                if(customers.size() >= pageSize + 1){
                    nextAnchor = customers.get(customers.size() - 1).getId();
                    customers.remove(customers.size() - 1);
                }else{
                    nextAnchor = -1L;
                }
            }
            if(customers != null && customers.size() > 0) {
                customers.forEach(r -> customerService.recordCustomerLevelChange(null, r.getLevelItemId(), r.getNamespaceId(), r.getCommunityId(), r.getId(), null));
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
