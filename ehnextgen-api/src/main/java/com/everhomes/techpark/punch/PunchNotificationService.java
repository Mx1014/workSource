package com.everhomes.techpark.punch;

import com.everhomes.organization.OrganizationMemberDetails;

import java.util.Date;

public interface PunchNotificationService {
    /**
     * 考勤规则更新后全量初始化程序
     */
    void punchNotificationInitialize();

    /**
     * 打卡成功后增量更新
     */
    void setPunchNotificationInvalidBackground(OrganizationMemberDetails memberDetail, PunchLog punchLog, Long punchRuleId);

    /**
     * 审批单通过后增量更新
     */
    void setPunchNotificationInvalidBackground(PunchExceptionRequest request, Integer namespaceId, Date punchDate);

}
