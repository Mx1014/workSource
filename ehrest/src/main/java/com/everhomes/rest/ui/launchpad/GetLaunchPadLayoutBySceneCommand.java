// @formatter:off
package com.everhomes.rest.ui.launchpad;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: layout的名称</li>
 * <li>versionCode: 当前版本号</li>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * </ul>
 */
public class GetLaunchPadLayoutBySceneCommand {
    
    private Long     versionCode;
    @NotNull
    private String   name;

    private String sceneToken;

    public GetLaunchPadLayoutBySceneCommand() {
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

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
