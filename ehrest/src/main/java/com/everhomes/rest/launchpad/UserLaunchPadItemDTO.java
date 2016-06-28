// @formatter:off
package com.everhomes.rest.launchpad;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>itemId: item id</li>
 * <li>ownerType: 范围类型</li>
 * <li>ownerType: 范围ID</li>
 * <li>userId: 用户ID</li>
 * <li>defaultOrder: 默认顺序</li>
 * <li>applyPolicy: 应用策略{@link com.everhomes.rest.launchpad.ApplyPolicy}</li>
 * <li>displayFlag: 是否显示{@link com.everhomes.rest.launchpad.ItemDisplayFlag}</li>
 * <li>sceneType: 场景{@link com.everhomes.rest.ui.user.sceneType} </li>
 * </ul>
 */
public class UserLaunchPadItemDTO {
	private Long    itemId;
	
	private String  ownerType;
	
	private Long    ownerId;
	
	private Long    userId;
	
	private Byte    applyPolicy;
	
	private Byte    displayFlag;
	
	private Integer defaultOrder;
	
	private String  sceneType;
	
	
	
    public Long getItemId() {
		return itemId;
	}



	public void setItemId(Long itemId) {
		this.itemId = itemId;
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



	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public Byte getApplyPolicy() {
		return applyPolicy;
	}



	public void setApplyPolicy(Byte applyPolicy) {
		this.applyPolicy = applyPolicy;
	}



	public Byte getDisplayFlag() {
		return displayFlag;
	}



	public void setDisplayFlag(Byte displayFlag) {
		this.displayFlag = displayFlag;
	}



	public Integer getDefaultOrder() {
		return defaultOrder;
	}



	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
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
