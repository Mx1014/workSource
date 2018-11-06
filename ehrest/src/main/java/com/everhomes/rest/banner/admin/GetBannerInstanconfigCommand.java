// @formatter:off
package com.everhomes.rest.banner.admin;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>originId: 应用originId</li>
 * </ul>
 */
public class GetBannerInstanconfigCommand {

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
