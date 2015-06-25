// @formatter:off
package com.everhomes.launchpad;



import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>versionCode: 当前版本号</li>
 * </ul>
 */
public class GetLaunchPadLayoutByVersionCodeCommand {
    
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
