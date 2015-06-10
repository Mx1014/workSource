package com.everhomes.util;

import com.everhomes.poll.ProcessStatus;

public class StatusChecker {
    public static ProcessStatus getProcessStatus(Long startTime, Long endTime) {
        long current = DateHelper.currentGMTTime().getTime();
        if (startTime == null) {
            if (current <= endTime.longValue())
                return ProcessStatus.UNKNOWN;
            return ProcessStatus.END;
        }
        
        if (current <= endTime)
            return ProcessStatus.UNDERWAY;
        return ProcessStatus.END;
    }
}
