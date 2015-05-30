// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.util.StringHelper;

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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
