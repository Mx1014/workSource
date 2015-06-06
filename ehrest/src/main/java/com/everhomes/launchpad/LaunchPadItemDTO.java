// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: itemId</li>
 * <li>namespaceId: 命名空间</li>
 * <li>appId: 应用Id</li>
 * <li>scopeType: item可见范围类型 参考{@link com.everhomes.launchpad.LaunchPadScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * <li>itemName: 名称</li>
 * <li>itemLabel: 显示标签</li>
 * <li>itemGroup: item归属某一组</li>
 * <li>actionName: 动作名称</li>
 * <li>actionIcon: 动作图标</li>
 * <li>actionUri: 动作uri</li>
 * <li>defaultOrder: 默认顺序</li>
 * <li>applyPolicy: 应用策略{@link com.everhomes.launchpad.ApplyPolicy}</li>
 * <li>jsonObj: json字符串，存储附加信息</li>
 * </ul>
 */
public class LaunchPadItemDTO {

	private Long    id;
	private Integer namespaceId;
	private Long    appId;
	private String  scopeType;
	private Long    scopeId;
	private String  itemName;
	private String  itemLabel;
	private String  itemGroup;
	private String  actionName;
	private String  actionIcon;
	private String  actionUri;
	private Integer defaultOrder;
	private Byte    applyPolicy;
	private String  jsonObj;
    
	public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNamespaceId() {
        return namespaceId;
    }
    
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    public Long getAppId() {
        return appId;
    }
    
    public void setAppId(Long appId) {
        this.appId = appId;
    }
    
    public String getScopeType() {
        return scopeType;
    }
    
    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
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
    
    public String getItemGroup() {
        return itemGroup;
    }
    
    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }
    
    public String getActionName() {
        return actionName;
    }
    
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    
    public String getActionIcon() {
        return actionIcon;
    }
    
    public void setActionIcon(String actionIcon) {
        this.actionIcon = actionIcon;
    }
    
    public String getActionUri() {
        return actionUri;
    }
    
    public void setActionUri(String actionUri) {
        this.actionUri = actionUri;
    }
    
    public Integer getDefaultOrder() {
        return defaultOrder;
    }
    
    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }
    
    public Byte getApplyPolicy() {
        return applyPolicy;
    }
    
    public void setApplyPolicy(Byte applyPolicy) {
        this.applyPolicy = applyPolicy;
    }
    
    public String getJsonObj() {
        return jsonObj;
    }

    public void setJsonObj(String jsonObj) {
        this.jsonObj = jsonObj;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
