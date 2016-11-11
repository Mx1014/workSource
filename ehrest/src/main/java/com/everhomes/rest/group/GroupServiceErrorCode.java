// @formatter:off
package com.everhomes.rest.group;

public interface GroupServiceErrorCode {
    static final String SCOPE = "group";

    static final int ERROR_GROUP_NOT_FOUND = 10001; // 群聊不存在
    static final int ERROR_USER_ALREADY_IN_GROUP = 10002; // 用户已加入群聊
    static final int ERROR_USER_IN_JOINING_GROUP_PROCESS = 10003;
    static final int ERROR_GROUP_MEMBER_NOT_FOUND = 10004; // 群聊成员不存在
    static final int ERROR_GROUP_MEMBER_NOT_IN_ACCEPT_STATE = 10005; // 群聊成员状态异常
    static final int ERROR_GROUP_INVITED_USER_NOT_FOUND = 10006; // 被邀请的用户未加入群聊
    static final int ERROR_GROUP_INVALID_ROLE_STATUS = 10007; // 群聊成员状态异常
    static final int ERROR_GROUP_NOT_IN_ADMIN_ROLE = 10008;
    static final int ERROR_GROUP_CREATOR_LEAVE_NOT_ALLOW = 10009; // 群聊创建者无法退出
    static final int ERROR_GROUP_CREATOR_ADMIN_RESIGN_NOT_ALLOW = 10010; // 群聊创建者不能退出管理员角色
    static final int ERROR_GROUP_CREATOR_ADMIN_REVOKED_NOT_ALLOW = 10011;
    static final int ERROR_GROUP_OP_REQUEST_NOT_FOUND = 10012;
    static final int ERROR_GROUP_OP_REQUEST_NOT_IN_REQUESTING_STATE = 10013;
    static final int ERROR_GROUP_PHONE_USER_NOT_FOUND = 10014;
    static final int ERROR_GROUP_FAMILY_NOT_FOUND = 10015;
    static final int ERROR_GROUP_FAMILY_MEMBER_NOT_FOUND = 10016;
    static final int ERROR_GROUP_MEMBER_NOT_IN_APPROVAL_STATE = 10017; // 群聊成员状态异常
    static final int ERROR_USER_NO_IN_GROUP = 10018; // 用户未加入群聊
    static final int ERROR_GROUP_CREATOR_REVOKED_NOT_ALLOW = 10019; // 群聊创建者无法被踢出
    static final int ERROR_GROUP_CLUB_NOT_FOUND = 10020; // 俱乐部不存在
    static final int ERROR_USER_ALREADY_IN_GROUP_CLUB = 10021; // 用户已加入俱乐部
    static final int ERROR_GROUP_CLUB_MEMBER_NOT_FOUND = 10022; // 俱乐部成员不存在
    static final int ERROR_GROUP_CLUB_MEMBER_NOT_IN_ACCEPT_STATE = 10023; // 群聊成员状态异常
    static final int ERROR_GROUP_CLUB_INVITED_USER_NOT_FOUND = 10024; // 被邀请的用户未加入俱乐部
    static final int ERROR_GROUP_CLUB_CREATOR_LEAVE_NOT_ALLOW = 10025; // 俱乐部创建者无法退出
    static final int ERROR_GROUP_CLUB_CREATOR_ADMIN_RESIGN_NOT_ALLOW = 10026; // 俱乐部创建者不能退出管理员角色
    static final int ERROR_GROUP_CLUB_CREATOR_ADMIN_REVOKED_NOT_ALLOW = 10027; // 俱乐部创建者不能取消管理员角色
    static final int ERROR_GROUP_CLUB_INVALID_ROLE_STATUS = 10028; // 俱乐部成员状态异常
    static final int ERROR_USER_NO_IN_GROUP_CLUB = 10029; // 用户未加入俱乐部
    static final int ERROR_GROUP_MEMBER_IS_NOT_CREATOR = 10030; // 非创建者
    
    
    static final int ERROR_GROUP_CATEGORY_NAME_EXIST = 10031; // 分类已存在
    static final int ERROR_GROUP_BEYOND_BROADCAST_COUNT = 10032; // 今天广播发送次数已用完
    static final int ERROR_BROADCAST_TITLE_LENGTH = 10033; // 标题不能超过10个字
    static final int ERROR_BROADCAST_CONTENT_LENGTH = 10034; // 内容不能超过200个字
    static final int ERROR_GROUP_DESCRIPTION_LENGTH = 10035; // 不可小于10个字
}
