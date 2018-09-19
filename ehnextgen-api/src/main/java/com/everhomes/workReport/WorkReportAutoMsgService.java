package com.everhomes.workReport;

import org.springframework.context.event.ContextRefreshedEvent;

public interface WorkReportAutoMsgService {
    
    void onApplicationEvent(ContextRefreshedEvent event);
}
