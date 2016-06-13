package com.everhomes.rest.local;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>platformType:平台，2-IOS，1-Android,3-WindowsPhone</li>
 *<li>currVersionCode:当前版本号</li>
 *</ul>
 */
public class AppVersionCommand {
    private Integer platformType;
    private String currVersionCode;

    public Integer getPlatformType() {
        return platformType;
    }

    public void setPlatformType(Integer platformType) {
        this.platformType = platformType;
    }

    public String getCurrVersionCode() {
        return currVersionCode;
    }

    public void setCurrVersionCode(String currVersionCode) {
        this.currVersionCode = currVersionCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
