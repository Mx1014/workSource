// @formatter:off
package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>location: 跳转URL</li>
 * </ul>
 */
public class CreateZhenZhiHuiUserInfoResponse {
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
