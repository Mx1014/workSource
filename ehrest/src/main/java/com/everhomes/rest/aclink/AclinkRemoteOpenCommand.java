package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>authId: 门禁授权 ID</li>
 * </ul>
 * @author janson
 *
 */
public class AclinkRemoteOpenCommand {
    private Long authId;

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }
   
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
