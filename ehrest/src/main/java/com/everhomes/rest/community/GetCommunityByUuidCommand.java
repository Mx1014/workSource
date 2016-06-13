// @formatter:off
package com.everhomes.rest.community;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>uuid: 小区唯一标识</li>
 * </ul>
 */
public class GetCommunityByUuidCommand {
    @NotNull
    private String uuid;
    
    public GetCommunityByUuidCommand() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
