// @formatter:off
package com.everhomes.rest.banner;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属者类型 {@link com.everhomes.rest.banner.BannerOwnerType}</li>
 * <li>ownerId: 归属者id</li>
 * <li>name: 名称</li>
 * <li>scope: 可见范围，参考 {@link com.everhomes.rest.banner.BannerScope}</li>
 * <li>bannerLocation: banner所在路径，如/home，/home/Pm</li>
 * <li>bannerGroup: banner所在的组，默认 Default,参考{@link com.everhomes.rest.launchpad.ItemGroup}</li>
 * <li>posterPath: 图片路径</li>
 * <li>actionType: 动作类型,参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 * <li>actionData: 根据actionType不同的取值决定,json格式的字符串,跳转,或直接进入帖子等等</li>
 * <li>status: banner的开启关闭状态 {@link com.everhomes.rest.banner.BannerStatus}</li>
 * <li>defaultOrder: banner排序</li>
 * <li>sceneTypes: 场景类型列表 {@link com.everhomes.rest.ui.user.SceneType}</li>
 * </ul>
 */
public class CreateBannerByOwnerCommand {
	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
    @NotNull
    private BannerScope scope;
    private String   name;
    private String   bannerLocation;
    private String   bannerGroup;
    private String   posterPath;
    private Byte     actionType;
    private String   actionData;
    private Byte     status;
    private Integer  defaultOrder;
    @ItemType(String.class)
    private List<String> sceneTypes;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPosterPath() {
        return posterPath;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getBannerLocation() {
        return bannerLocation;
    }
    public void setBannerLocation(String bannerLocation) {
        this.bannerLocation = bannerLocation;
    }
    public String getBannerGroup() {
        return bannerGroup;
    }
    public void setBannerGroup(String bannerGroup) {
        this.bannerGroup = bannerGroup;
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
	public BannerScope getScope() {
		return scope;
	}
	public void setScope(BannerScope scope) {
		this.scope = scope;
	}
	public List<String> getSceneTypes() {
		return sceneTypes;
	}
	public void setSceneTypes(List<String> sceneTypes) {
		this.sceneTypes = sceneTypes;
	}
	public Integer getDefaultOrder() {
		return defaultOrder;
	}
	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
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
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
