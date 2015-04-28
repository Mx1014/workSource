package com.everhomes.acl;

public interface RoleConstants {
    // system defined roles
    public static final long Anonymous = 1L;
    public static final long SystemAdmin = 2L;
    public static final long AuthenticatedUser = 3L;
    public static final long ResourceCreator = 4L;
    public static final long ResourceAdmin = 5L;
    public static final long ResourceOperator = 6L;
    public static final long ResourceUser = 7L;
    public static final long SystemExtension = 8L;
}
