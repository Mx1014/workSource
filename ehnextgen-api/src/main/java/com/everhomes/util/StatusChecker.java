package com.everhomes.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
