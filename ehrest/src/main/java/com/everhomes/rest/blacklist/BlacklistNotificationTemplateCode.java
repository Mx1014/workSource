package com.everhomes.rest.blacklist;

/**
 * Created by sfyan on 2016/12/23.
 */
public interface BlacklistNotificationTemplateCode {

    String SCOPE = "blacklist.notification";

    int JOIN_USER_BLACKLIST = 1;  //加入黑名单

    int RELIEVE_USER_BLACKLIST = 1;  // 解除黑名单

}
