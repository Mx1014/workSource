// @formatter:off
package com.everhomes.launchpad;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>versionCode: 当前版本号</li>
 * </ul>
 */
public class GetLaunchPadLayoutByVersionCodeCommand {
    
    @NotNull
    private Long     versionCode;

    public GetLaunchPadLayoutByVersionCodeCommand() {
    }
    
    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
