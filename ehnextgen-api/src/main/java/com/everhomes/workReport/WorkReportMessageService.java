package com.everhomes.workReport;

import com.everhomes.rest.workReport.ReportMsgSettingDTO;
import com.everhomes.rest.workReport.ReportValiditySettingDTO;
import com.everhomes.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface WorkReportMessageService {

    void workReportPostMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user);

    void workReportUpdateMessage(WorkReport report, WorkReportVal reportVal, Long receiverId, User user);

    void workReportCommentMessage(User user, WorkReportVal reportVal, WorkReport report, Long parentCommentId);

    void workReportRxMessage();

    void workReportAuMessage();

    void createWorkReportAuMessage();

    WorkReportScopeMsg createWorkReportScopeMsg(WorkReport report, ReportMsgSettingDTO auMsgSetting, ReportValiditySettingDTO validity, Timestamp reportTime);
}
