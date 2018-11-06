package com.everhomes.rest.forum;

public interface ForumServiceErrorCode {
    static final String SCOPE = "forum";
    
    static final int ERROR_FORUM_NOT_FOUND = 10001;
    static final int ERROR_FORUM_VISIBLE_REGION_NOT_FOUND = 10002;
    static final int ERROR_FORUM_COMMUNITY_NOT_FOUND = 10003;
    static final int ERROR_FORUM_TOPIC_NOT_FOUND = 10004;
    static final int ERROR_FORUM_ORGANIZATION_NOT_FOUND = 10004;
    static final int ERROR_FORUM_ORGANIZATION_COMMUNITY_NOT_FOUND = 10005;
    static final int ERROR_FORUM_TOPIC_DELETED = 10006;
    static final int ERROR_INVALID_PARAMETER = 10010;
    

    static final int ERROR_FORUM_TOPIC_NOT_ASSIGNED = 10011;
    static final int ERROR_FORUM_TOPIC_ALREADY_ASSIGNED = 10012;

    static final int ERROR_BLACK_LIST_USER_CREATE_TOPIC = 10021;// 对不起,您已被禁止发帖

    static final int ERROR_ANNOUNCEMENT_DELETED = 10007;
}
