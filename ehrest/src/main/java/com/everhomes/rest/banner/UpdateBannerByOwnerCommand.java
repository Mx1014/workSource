// @formatter:off
package com.everhomes.rest.banner;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner id</li>
 * <li>ownerType: 归属者类型 {@link com.everhomes.rest.banner.BannerOwnerType}</li>
 * <li>ownerId: 归属者id</li>
 * <li>scopeCode: banner可见范围类型</li>
 * <li>scopeId：banner可见范围的具体id</li>
 * <li>posterPath: 图片路径</li>
 * <li>actionType: 动作类型，参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 * <li>actionData: 根据actionType不同的取值决定，json格式的字符串，跳圈，或直接进入帖子等等</li>
 * <li>status: banner的状态{@link com.everhomes.rest.banner.BannerStatus}</li>
 * <li>defaultOrder: banner排序</li>
 * <li>sceneTypes: 场景类型列表 {@link com.everhomes.rest.ui.user.SceneType}</li>
 * </ul>
 */
public class UpdateBannerByOwnerCommand {

    @NotNull
    private Long    id;
    private String  ownerType;
	private Long    ownerId;
    private Byte    scopeCode;
    private Long    scopeId;
    private String  posterPath;
    private Byte    actionType;
    private String  actionData;
    private Byte    status;
    private Integer defaultOrder;
    @ItemType(SceneType.class)
    private List<SceneType> sceneTypes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SceneType> getSceneTypes() {
		return sceneTypes;
	}

	public void setSceneTypes(List<SceneType> sceneTypes) {
		this.sceneTypes = sceneTypes;
	}

	public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Byte getScopeCode() {
        return scopeCode;
    }

    public void setScopeCode(Byte scopeCode) {
        this.scopeCode = scopeCode;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public Byte getStatus() {
        return status;
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

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
