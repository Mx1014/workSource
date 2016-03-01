package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scene: 场景标识，参考{@link com.everhomes.rest.ui.SceneType}</li>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体ID</li>
 * <li>pageAnchor: 页面开始anchor</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListContactsV2Command {
    private String scene;
    
    private String entityType;
    
    private Long entityId;
    
    private Long pageAnchor;
    
    private Integer pageSize;

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

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
