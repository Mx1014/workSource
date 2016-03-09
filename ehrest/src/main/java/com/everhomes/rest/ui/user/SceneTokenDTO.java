// @formatter:off
package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scene: 场景标识，参考com.everhomes.rest.ui.SceneType </li>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体ID </li>
 * </ul>
 */
public class SceneTokenDTO {
    private String scene;
    private String entityType;
    private Long entityId;

    
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
