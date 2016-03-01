// @formatter:off
package com.everhomes.rest.launchpad;



import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: layout的名称</li>
 * <li>versionCode: 当前版本号</li>
 * <li>scene: 场景标识，参考{@link com.everhomes.rest.ui.SceneType}</li>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体ID</li>
 * </ul>
 */
public class GetLaunchPadLayoutByVersionCodeV2Command {
    
    private Long     versionCode;
    @NotNull
    private String   name;
    
    private String scene;
    
    private String entityType;
    
    private Long entityId;

    public GetLaunchPadLayoutByVersionCodeV2Command() {
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

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
