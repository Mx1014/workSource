package com.everhomes.rest.pmtask;

public interface PmTaskNotificationTemplateCode {
	
	String SCOPE = "pmtask.notification";
    
	String LOCALE = "zh_CN";
	
    int UNPROCESS_TASK_LOG = 1;
    
    int PROCESSING_TASK_LOG = 2;
    
    int PROCESSED_TASK_LOG = 3;
    
    int CLOSED_TASK_LOG = 4;
    
    int NOTIFY_TO_CREATOR = 5;
    
    int NOTIFY_TO_ASSIGNER = 6;
    
}
