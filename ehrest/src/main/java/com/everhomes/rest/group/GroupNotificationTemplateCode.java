// @formatter:off
package com.everhomes.rest.group;

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
    static final int GROUP_AUTH_JOIN_INVITATION_REQ_FOR_APPLICANT = 14; // 邀请别人加入圈（需要同意），通知邀请人
    static final int GROUP_AUTH_JOIN_INVITATION_REQ_FOR_OPERATOR = 15; // 邀请别人加入圈（需要同意），通知被邀请人
    static final int GROUP_AUTH_JOIN_INVITATION_REQ_FOR_OTHER = 16; // 邀请别人加入圈（需要同意），通知管理员
    static final int GROUP_JOIN_INVITATION_ACCEPT_FOR_APPLICANT = 17; // 被邀请人同意加入圈，通知邀请人
    static final int GROUP_JOIN_INVITATION_ACCEPT_FOR_OPERATOR = 18; // 被邀请人同意加入圈，通知被邀请人
    static final int GROUP_JOIN_INVITATION_ACCEPT_FOR_OTHER = 19; // 被邀请人同意加入圈，通知其它人
    static final int GROUP_JOIN_INVITATION_REJECT_FOR_APPLICANT = 20; // 被邀请人拒绝加入圈，通知邀请人
    static final int GROUP_JOIN_INVITATION_REJECT_FOR_OPERATOR = 21; // 被邀请人拒绝加入圈，通知被邀请人
    static final int GROUP_JOIN_INVITATION_REJECT_FOR_OTHER = 22; // 被邀请人拒绝加入圈，通知管理员
    static final int GROUP_MEMBER_LEAVE_FOR_OPERATOR = 23; // 圈里的人主动退出圈，通知退出人
    static final int GROUP_MEMBER_LEAVE_FOR_OTHER = 24; // 圈里的人主动退出圈，通知其它人
    static final int GROUP_MEMBER_INVOKE_FOR_APPLICANT = 25; // 圈里的人被踢出圈，通知退出人
    static final int GROUP_MEMBER_INVOKE_FOR_OPERATOR = 26; // 圈里的人被踢出圈，通知操作者
    static final int GROUP_MEMBER_INVOKE_FOR_OTHER = 27; // 圈里的人被踢出圈，通知其它人
    
    static final int GROUP_MEMBER_PUBLIC_APPLICANT = 28; //您已订阅兴趣圈“瑞地自由度”
    static final int GROUP_MEMBER_PUBLIC_MEMBER_CHANGE = 29; //兴趣圈‘瑞地自由度’人数有变化
    static final int GROUP_MEMBER_DELETED_ADMIN = 30;   //彭海星已删除圈“瑞地自由度”
    static final int GROUP_MEMBER_DELETED_OPERATOR = 31;    //您已删除圈“瑞地自由度”
    static final int GROUP_MEMBER_DELETE_MEMBER = 32;   //您已取消订阅兴趣圈“瑞地自由度”
    
    static final int GROUP_RECOMMEND = 33;   //兴趣圈推荐
    
    static final int GROUP_MEMBER_DELETED_CLUB_ADMIN = 34;  
    static final int GROUP_MEMBER_DELETED_CLUB_OPERATOR = 35;
}
