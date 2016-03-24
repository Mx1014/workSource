// @formatter:off
package com.everhomes.rest.launchpad;



import javax.validation.constraints.NotNull;

import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: layout的名称</li>
 * <li>versionCode: 当前版本号</li>
 * <li>siteUri: 链接</li>
 * <li>namespaceId: 域空间</li>
 * <li>sceneType: 场景类型，{@link com.everhomes.rest.ui.user.SceneType}</li>
 * </ul>
 */
public class GetLaunchPadLayoutByVersionCodeCommand {
    
    private Long     versionCode;
    @NotNull
    private String   name;
    @NotNull
    private Integer namespaceId;
    
    private String sceneType;

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

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }
    
    public String getCurrentSceneType() {
        return (sceneType == null) ? SceneType.DEFAULT.getCode() : sceneType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
