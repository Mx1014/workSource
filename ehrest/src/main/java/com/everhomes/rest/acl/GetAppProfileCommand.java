package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>originId: 应用originId</li>
 * </ul>
 */
public class GetAppProfileCommand {
    private Long originId;

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
