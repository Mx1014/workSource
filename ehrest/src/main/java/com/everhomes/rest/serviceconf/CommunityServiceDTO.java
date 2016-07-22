package com.everhomes.rest.serviceconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>scopeCode: service可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * <li>itemName: 每页条数</li>
 * <li>itemLabel: 分页瞄</li>
 * <li>iconUri: 图标路径</li>
 * <li>actionType: 动作类型，参考{@link com.everhomes.rest.serviceconf.ActionType}</li>
 * <li>actionData: 动作所需要的参数，由actionType决定</li>
 * </ul>
 */
public class CommunityServiceDTO {
	private Long    id;
	private Byte    scopeCode;
	private Long    scopeId;
	private String  itemName;
	private String  itemLabel;
	private String  iconUri;
	private String actionType;
	private String  actionData;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemLabel() {
		return itemLabel;
	}
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}
	public String getIconUri() {
		return iconUri;
	}
	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
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
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
}
