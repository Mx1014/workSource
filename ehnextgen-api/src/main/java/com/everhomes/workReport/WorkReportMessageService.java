package com.everhomes.workReport;

import com.everhomes.user.User;

public interface WorkReportMessageService {

    void workReportPostMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user);

    void workReportUpdateMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user);

    void workReportCommentMessage(User user, WorkReportVal reportVal, WorkReport report, Long parentCommentId);

    void workReportRxMessage();

    void workReportAuMessage();
}
