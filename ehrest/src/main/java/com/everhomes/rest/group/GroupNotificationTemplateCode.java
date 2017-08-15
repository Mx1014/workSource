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
    
    
    static final int GROUP_MEMBER_TRANSFER_CREATOR_TO_OTHERS = 36;  //${newCreator}已成为“${groupName}”的创建者
    static final int GROUP_MEMBER_TRANSFER_CREATOR_TO_NEW_CREATOR = 37;  //你已成为“${groupName}”的创建者
    static final int GROUP_MEMBER_TO_CREATOR_WHEN_APPROVAL = 38;  //你创建“俱乐部A”的申请已通过
    static final int GROUP_MEMBER_TO_CREATOR_WHEN_NO_APPROVAL = 39;  //你已成功创建“${groupName}”
    static final int GROUP_MEMBER_TO_CREATOR_WHEN_NEED_APPROVAL = 40;  //你提交了创建“${groupName}”的申请，需要人工审核，请耐心等候
    static final int GROUP_MEMBER_TO_CREATOR_WHEN_REJECTED = 41;  //你创建“${groupName}”的申请被拒绝
    static final int GROUP_MEMBER_TO_ALL_WHEN_DELETE = 42;  //你加入的“${groupName}”已解散
    static final int GROUP_NOT_ALLOW_TO_CREATE_GROUP = 43;  //不允许创建${clubPlaceholderName}
    static final int GROUP_MEMBER_TO_ADMIN_WHEN_REQUEST_TO_JOIN = 44;  //${userName}申请加入“${groupName}”，理由：${reason}
    static final int GROUP_MEMBER_APPROVE_REQUEST_TO_JOIN = 45;  //你加入“${groupName}”的申请已通过
    static final int GROUP_MEMBER_REJECT_REQUEST_TO_JOIN = 46;  //你加入“${groupName}”的申请被拒绝


    static final int GROUP_INVITE_USERS_TO_JOIN = 50;  //你邀请${userNameList}加入了群聊
    static final int GROUP_OTHER_INVITE_USERS_TO_JOIN = 51;  //${inviterName}邀请${userNameList}加入了群聊
    static final int GROUP_BE_INVITE_TO_JOIN = 52;  //${inviterName}邀请你加入了群聊
    static final int GROUP_MEMBER_LEAVE = 53;  //${userName}已退出群聊
    static final int GROUP_RENAME = 54;  //群聊名称已修改为“${groupName}”
    static final int GROUP_REMOVE_MEMBERS = 55;  //你将${userNameList}移出了群聊
    static final int GROUP_BE_REMOVE = 56;  //你被${userName}移出了群聊
    static final int GROUP_DELETE = 57;  //你加入的群聊“${groupName}”已解散

}
