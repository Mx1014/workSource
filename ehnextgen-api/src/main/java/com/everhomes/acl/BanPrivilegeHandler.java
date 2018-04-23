package com.everhomes.acl;

import java.util.List;

public interface BanPrivilegeHandler {
    String BAN_PRIVILEGE_OBJECT_PREFIX = "BanPrivilegeHandlerImpl";

    List<Long> listBanPrivilegesByModuleIdAndAppId(Integer namespaceId, Long module, Long appId);
}
