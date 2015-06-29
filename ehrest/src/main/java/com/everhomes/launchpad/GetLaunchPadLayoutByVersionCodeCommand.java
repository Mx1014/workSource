// @formatter:off
package com.everhomes.launchpad;



import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: layout的名称</li>
 * <li>versionCode: 当前版本号</li>
 * </ul>
 */
public class GetLaunchPadLayoutByVersionCodeCommand {
    
    private Long     versionCode;
    private String   name;

    public GetLaunchPadLayoutByVersionCodeCommand() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
