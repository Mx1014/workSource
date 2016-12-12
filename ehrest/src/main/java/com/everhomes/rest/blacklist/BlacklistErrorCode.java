package com.everhomes.rest.blacklist;

/**
 * Created by sfyan on 2016/12/12.
 */
public interface BlacklistErrorCode {

    String SCOPE = "blacklist";

    int ERROR_USERBLACKLIST_EXISTS = 600010;

    int ERROR_USER_NOT_EXISTS = 600020;

    int ERROR_FORBIDDEN_PERMISSIONS = 600000;

}
