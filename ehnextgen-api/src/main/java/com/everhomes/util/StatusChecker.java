package com.everhomes.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.activity.Activity;
import com.everhomes.poll.ActivityProcessStatus;
import com.everhomes.poll.ProcessStatus;

public class StatusChecker {
	private static final Logger LOGGER = LoggerFactory.getLogger(StatusChecker.class);
    public static ProcessStatus getProcessStatus(Long startTime, Long endTime) {
        long current = DateHelper.currentGMTTime().getTime();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Check activity process status, current=" + current + ", startTime=" + startTime + ", endTime=" + endTime);
        }
        
        if (startTime == null) {
            if (current <= endTime.longValue())
                return ProcessStatus.UNKNOWN;
            return ProcessStatus.END;
        }
        
        if (current <= endTime && current >= startTime)
            return ProcessStatus.UNDERWAY;
        
        else if(current < startTime)
        	return ProcessStatus.NOTSTART;
        
        return ProcessStatus.END;
    }
    
    public static ActivityProcessStatus getActivityProcessStatus(Activity activity) {
    	long current = DateHelper.currentGMTTime().getTime();
    	Long startTime = activity.getStartTimeMs();
    	Long endTime = activity.getEndTimeMs();
    	Long signupEndtime = activity.getSignupEndTime()==null?startTime:activity.getSignupEndTime().getTime();
		if (signupEndtime != null && current < signupEndtime.longValue()) {
			return ActivityProcessStatus.SINGING_UP;
		}
		
		if (startTime == null) {
            if (current <= endTime.longValue())
                return ActivityProcessStatus.UNKNOWN;
            return ActivityProcessStatus.END;
        }
        
        if (current <= endTime && current >= startTime)
            return ActivityProcessStatus.UNDERWAY;
        
        else if(current < startTime)
        	return ActivityProcessStatus.NOTSTART;
        
        return ActivityProcessStatus.END;
	}
}
