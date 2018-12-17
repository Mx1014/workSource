package com.everhomes.investment;

import com.everhomes.archives.ArchivesDTSServiceImpl;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.filedownload.TaskHandler;
import org.mockito.internal.verification.Times;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Component
public class InitCustomerStatisticsHandle implements TaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitCustomerStatisticsHandle.class);


    @Autowired
    private InvitedCustomerProvider customerProvider;

    @Autowired
    private InvitedCustomerService customerService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        long startTime = System.currentTimeMillis();


        Long nextAnchor = 0L;
        int rountNum = 0;
        int rountSum = 0;
        Integer pageSize = 100;
        if(params.get("pageSize") != null)
            pageSize = Integer.valueOf(String.valueOf(params.get("pageSize")));
        LOGGER.info("init customer levelItemId to record start, pageSize : {} , now time: {}" , pageSize, startTime);


        while(nextAnchor != -1L){
            LOGGER.info("init customer levelItemId to record rountNum : {}, change Sum : {}, now" , rountNum++, rountSum);

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
