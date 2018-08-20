package com.everhomes.workReport;

import com.everhomes.user.User;

public interface WorkReportMessageService {

    void postWorkReportMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user);
}
