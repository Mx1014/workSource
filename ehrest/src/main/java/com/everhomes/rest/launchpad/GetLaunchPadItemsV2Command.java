// @formatter:off
package com.everhomes.rest.launchpad;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>itemLocation: item的路径</li>
 * <li>itemGroup: 当前item归属哪个组</li>
 * <li>scene: 场景标识，参考{@link com.everhomes.rest.ui.SceneType}</li>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体ID</li>
 * </ul>
 */
public class GetLaunchPadItemsV2Command {
    
    @NotNull
    private String    itemLocation;
    @NotNull
    private String    itemGroup;
    
    private String scene;
    
    private String entityType;
    
    private Long entityId;

    public GetLaunchPadItemsV2Command() {
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
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
