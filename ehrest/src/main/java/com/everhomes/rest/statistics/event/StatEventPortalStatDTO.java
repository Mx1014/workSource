// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>parentId: 该门户项的parentId</li>
 *     <li>identifier: 标识符</li>
 *     <li>displayName: 显示名称</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 *     <li>widget: widget {@link com.everhomes.rest.launchpad.Widget}</li>
 *     <li>contentType: 只有widget是OPPush时才有这个值</li>
 *     <li>sceneType: 场景类型 {@link com.everhomes.rest.ui.user.SceneType}</li>
 * </ul>
 */
public class StatEventPortalStatDTO {

    private Long id;
    private Long parentId;
    private String identifier;
    private String displayName;
    private String ownerType;
    private Long ownerId;
    private String widget;
    private String contentType;
    private String sceneType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
