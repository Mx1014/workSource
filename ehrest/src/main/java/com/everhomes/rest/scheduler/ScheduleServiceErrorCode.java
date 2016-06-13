package com.everhomes.rest.scheduler;

public interface ScheduleServiceErrorCode {
    static final String SCOPE = "schedule";
    
    static final int ERROR_SCHEDULE_JOB_FAILED = 10001;
    static final int ERROR_CHECK_JOB_FAILED = 10002;
    static final int ERROR_UNSCHEDULE_JOB_FAILED = 10003;
}
