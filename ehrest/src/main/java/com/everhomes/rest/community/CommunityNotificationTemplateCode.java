// @formatter:off
package com.everhomes.rest.community;

public interface CommunityNotificationTemplateCode {
    static final String SCOPE = "community.notification";
    
    static final int COMMUNITY_ADD_FOR_APPLICANT = 1; // 有人填加小区（小区地址不存在），通知申请人等待审核
    static final int COMMUNITY_ADD_APPROVE_FOR_APPLICANT = 2; // 管理员审批通过，通知申请人
    static final int COMMUNITY_ADD_REJECT_FOR_APPLICANT = 3; // 管理员拒绝通过，通知申请人
}
