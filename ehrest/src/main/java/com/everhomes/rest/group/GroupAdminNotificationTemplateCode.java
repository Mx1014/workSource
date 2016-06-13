// @formatter:off
package com.everhomes.rest.group;

public interface GroupAdminNotificationTemplateCode {
    static final String SCOPE = "group.admin.notification";
    
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
    static final int GROUP_ADMINROLE_INVOKE_FOR_APPLICANT = 20; // 某管理员被收回管理员权限，通知被收回权限的人
    static final int GROUP_ADMINROLE_INVOKE_FOR_OPERATOR = 19; // 某管理员被收回管理员权限，通知操作者
    static final int GROUP_ADMINROLE_INVOKE_FOR_OTHER = 21; // 某管理员被收回管理员权限，通知其它人
    static final int GROUP_ADMINROLE_FREE_INVITATION_FOR_APPLICANT = 22; // 邀请别人成为圈管理员（不需要同意），通知被邀请人
    static final int GROUP_ADMINROLE_FREE_INVITATION_FOR_OPERATOR = 23; // 邀请别人成为圈管理员（不需要同意），通知邀请人
    static final int GROUP_ADMINROLE_FREE_INVITATION_FOR_OTHER = 24; // 邀请别人成为圈管理员（不需要同意），通知其它人
}
