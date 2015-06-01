package com.everhomes.forum;

public interface ForumServiceErrorCode {
    static final String SCOPE = "forum";
    
    static final int ERROR_FORUM_NOT_EXIST = 10001;
    static final int ERROR_FORUM_VISIBLE_REGION_NOT_EXIST = 10002;
    static final int ERROR_FORUM_COMMUNITY_NOT_EXIST = 10003;
}
