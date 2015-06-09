// @formatter:off
package com.everhomes.group;

public interface GroupNotificationTemplateCode {
    static final String SCOPE = "group.notification";
    
    static final int GROUP_FREE_JOIN_REQ_FOR_APPLICANT = 1; // 有人申请加入圈（圈不需要审批），通知申请者成功加入圈
    static final int GROUP_FREE_JOIN_REQ_FOR_OTHER = 2; // 有人申请加入圈（圈不需要审批），通知圈管理员有新成员加入
    static final int GROUP_AUTH_JOIN_REQ_FOR_APPLICANT = 3; // 有人申请加入圈（圈需要审批），通知申请者等待审核
    static final int GROUP_AUTH_JOIN_REQ_FOR_OPERATOR = 4; // 有人申请加入圈（圈需要审批），通知圈管理员审核
    static final int GROUP_AUTH_JOIN_APPROVE_FOR_APPLICANT = 5; // 有人申请加入圈（圈需要审批），圈管理员审核通过，通知申请者
    static final int GROUP_AUTH_JOIN_APPROVE_FOR_OPERATOR = 6; // 有人申请加入圈（圈需要审批），圈管理员审核通过，通知审核人
    static final int GROUP_AUTH_JOIN_APPROVE_FOR_OTHER = 7; // 有人申请加入圈（圈需要审批），圈管理员审核通过，通知其它人
    static final int GROUP_AUTH_JOIN_REJECT_FOR_APPLICANT = 8; // 有人申请加入圈（圈需要审批），圈管理员拒绝，通知申请者
    static final int GROUP_AUTH_JOIN_REJECT_FOR_OPERATOR = 9; // 有人申请加入圈（圈需要审批），圈管理员拒绝，通知审核人
    static final int GROUP_AUTH_JOIN_REJECT_FOR_OTHER = 10; // 有人申请加入圈（圈需要审批），圈管理员拒绝，通知其它管理员
    static final int GROUP_FREE_JOIN_INVITATION_REQ_FOR_APPLICANT = 11; // 邀请别人加入圈（不需要同意），通知被邀请人
    static final int GROUP_FREE_JOIN_INVITATION_REQ_FOR_OPERATOR = 12; // 邀请别人加入圈（不需要同意），通知操作者
    static final int GROUP_FREE_JOIN_INVITATION_REQ_FOR_OTHER = 13; // 邀请别人加入圈（不需要同意），通知其它人
    static final int GROUP_AUTH_JOIN_INVITATION_REQ_FOR_APPLICANT = 11; // 邀请别人加入圈（需要同意），通知邀请人
    static final int GROUP_AUTH_JOIN_INVITATION_REQ_FOR_OPERATOR = 12; // 邀请别人加入圈（需要同意），通知被邀请人
    static final int GROUP_AUTH_JOIN_INVITATION_REQ_FOR_OTHER = 13; // 邀请别人加入圈（需要同意），通知管理员
    static final int GROUP_JOIN_INVITATION_ACCEPT_FOR_APPLICANT = 14; // 被邀请人同意加入圈，通知邀请人
    static final int GROUP_JOIN_INVITATION_ACCEPT_FOR_OPERATOR = 15; // 被邀请人同意加入圈，通知被邀请人
    static final int GROUP_JOIN_INVITATION_ACCEPT_FOR_OTHER = 16; // 被邀请人同意加入圈，通知其它人
    static final int GROUP_JOIN_INVITATION_REJECT_FOR_APPLICANT = 17; // 被邀请人拒绝加入圈，通知邀请人
    static final int GROUP_JOIN_INVITATION_REJECT_FOR_OPERATOR = 18; // 被邀请人拒绝加入圈，通知被邀请人
    static final int GROUP_JOIN_INVITATION_REJECT_FOR_OTHER = 19; // 被邀请人拒绝加入圈，通知管理员
    static final int GROUP_MEMBER_LEAVE_FOR_OPERATOR = 20; // 圈里的人主动退出圈，通知退出人
    static final int GROUP_MEMBER_LEAVE_FOR_OTHER = 21; // 圈里的人主动退出圈，通知其它人
    static final int GROUP_MEMBER_INVOKE_FOR_APPLICANT = 22; // 圈里的人被踢出圈，通知退出人
    static final int GROUP_MEMBER_INVOKE_FOR_OPERATOR = 23; // 圈里的人被踢出圈，通知操作者
    static final int GROUP_MEMBER_INVOKE_FOR_OTHER = 24; // 圈里的人被踢出圈，通知其它人
    
    static final int GROUP_ADMINROLE_REQ_FOR_APPLICANT = 1; // 有人申请成为圈管理员，通知申请人
    static final int GROUP_ADMINROLE_REQ_FOR_OPERATOR = 2; // 有人申请成为圈管理员，通知审核人
    static final int GROUP_ADMINROLE_APPROVE_FOR_APPLICANT = 3; // 申请成为圈管理员的请求被审核通过，通知申请人
    static final int GROUP_ADMINROLE_APPROVE_FOR_OPERATOR = 4; // 申请成为圈管理员的请求被审核通过，通知审核人
    static final int GROUP_ADMINROLE_APPROVE_FOR_OTHER = 5; // 申请成为圈管理员的请求被审核通过，通知其它管理员
    static final int GROUP_ADMINROLE_REJECT_FOR_APPLICANT = 6; // 申请成为圈管理员的请求被拒绝通过，通知申请人
    static final int GROUP_ADMINROLE_REJECT_FOR_OPERATOR = 7; // 申请成为圈管理员的请求被拒绝通过，通知审核人
    static final int GROUP_ADMINROLE_REJECT_FOR_OTHER = 8; // 申请成为圈管理员的请求被拒绝通过，通知其它管理员
    static final int GROUP_ADMINROLE_INVITATION_REQ_FOR_APPLICANT = 9; // 邀请别人成为圈管理员，通知邀请人
    static final int GROUP_ADMINROLE_INVITATION_REQ_FOR_OPERATOR = 10; // 邀请别人成为圈管理员，通知被邀请人
    static final int GROUP_ADMINROLE_INVITATION_ACCEPT_FOR_APPLICANT = 11; // 邀请别人成为圈管理员，被邀请人同意时，通知被邀请人
    static final int GROUP_ADMINROLE_INVITATION_ACCEPT_FOR_OPERATOR = 12; // 邀请别人成为圈管理员，被邀请人同意时，通知邀请人
    static final int GROUP_ADMINROLE_INVITATION_ACCEPT_FOR_OTHER = 13; // 邀请别人成为圈管理员，被邀请人同意时，通知其它人
    static final int GROUP_ADMINROLE_INVITATION_REJECT_FOR_APPLICANT = 14; // 邀请别人成为圈管理员，被邀请人拒绝时，通知被邀请人
    static final int GROUP_ADMINROLE_INVITATION_REJECT_FOR_OPERATOR = 15; // 邀请别人成为圈管理员，被邀请人拒绝时，通知邀请人
    static final int GROUP_ADMINROLE_INVITATION_REJECT_FOR_OTHER = 16; // 邀请别人成为圈管理员，被邀请人拒绝时，通知其它人
    static final int GROUP_ADMINROLE_LEAVE_FOR_OPERATOR = 17; // 圈管理员主动辞去管理员身份，通知操作人
    static final int GROUP_ADMINROLE_LEAVE_FOR_OTHER = 18; // 圈管理员主动辞去管理员身份，通知其它管理员
    static final int GROUP_ADMINROLE_INVOKE_FOR_APPLICANT = 19; // 某管理员被收回管理员权限，通知操作者
    static final int GROUP_ADMINROLE_INVOKE_FOR_OPERATOR = 20; // 某管理员被收回管理员权限，通知被收回权限的人
    static final int GROUP_ADMINROLE_INVOKE_FOR_OTHER = 21; // 某管理员被收回管理员权限，通知其它人


}
