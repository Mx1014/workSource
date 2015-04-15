package com.everhomes.acl;

public interface PrivilegeConstants {
    // System defined privileges
    public static final long All = 1L;
    public static final long Visible = 2L;
    public static final long Read = 3L; 
    public static final long Create = 4L;
    public static final long Write = 5L;
    public static final long Delete = 6L;

    // privileges defined by Forum module
    public static final long ForumNewTopic = 100L;
    public static final long ForumDeleteTopic = 101L;
    public static final long ForumNewReply = 102L;
    public static final long ForumDeleteReply = 103L;
}
