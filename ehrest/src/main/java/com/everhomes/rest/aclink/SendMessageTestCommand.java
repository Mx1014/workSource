// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType：所属组织类型 0小区 1企业 {@link DoorAccessOwnerType}</li>
 * <li>ownerId:所属组织Id</li>
 * </ul>
 *
 */
public class SendMessageTestCommand {

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