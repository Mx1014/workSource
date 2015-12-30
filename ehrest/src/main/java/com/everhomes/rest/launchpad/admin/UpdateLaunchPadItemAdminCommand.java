// @formatter:off
package com.everhomes.rest.launchpad.admin;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: item的id</li>
 * <li>namespaceId: 命名空间</li>
 * <li>appId: 应用Id，参考{@link com.everhomes.rest.app.AppConstants}</li>
 * <li>scopeType: item可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * <li>defaultOrder: 默认顺序</li>
 * <li>applyPolicy: 应用策略{@link com.everhomes.rest.launchpad.ApplyPolicy}</li>
 * <li>itemName: 名称</li>
 * <li>itemLabel: 显示标签</li>
 * <li>itemGroup: item归属哪一个组，参考{@link com.everhomes.rest.launchpad.ItemGroup}</li>
 * <li>itemLocation: item的路径，如/home,/home/Bizs</li>
 * <li>itemWidth: 图标的宽</li>
 * <li>itemHeight: 图标的高</li>
 * <li>iconUri: 动作图标</li>
 * <li>actionType: item的动作类型，参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 * <li>actionData: 根据actionType不同的取值决定，json格式的字符串，如果item存在二级,如物业，则会有layoutName：PmLayout,itemLocation:/home/Pm</li>
 * <li>displayFlag: 是否显示，参考{@link com.everhomes.rest.launchpad.ItemDisplayFlag}</li>
 * <li>displayLayout: 图标尺寸 格式：1x2</li>
 * <li>tag：标签，默认不填，管理员可见item设相应的值，如物业（PM），居委（GANC），业委（GARC），派出所（GAPS）</li>
 * </ul>
 */
public class UpdateLaunchPadItemAdminCommand {
    private Long    id;
    private Integer namespaceId;
    private Long    appId;
    private Byte  scopeCode;
    private Long    scopeId;
    private Integer defaultOrder;
    private Byte    applyPolicy;
    @NotNull
    private String  itemName;
    @NotNull
    private String  itemLabel;
    @NotNull
    private String  itemGroup;
    @NotNull
    private String itemLocation;
    @NotNull
    private Integer  itemWidth;
    @NotNull
    private Integer  itemHeight;
    @NotNull
    private String  iconUri;
    @NotNull
    private Byte actionType;
    @NotNull
    private String actionData;
    
    private Byte displayFlag;
    
    private String displayLayout;
    
    private String tag;

    public UpdateLaunchPadItemAdminCommand() {
    }

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

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public Integer getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(Integer itemWidth) {
        this.itemWidth = itemWidth;
    }

    public Integer getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(Integer itemHeight) {
        this.itemHeight = itemHeight;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public Byte getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(Byte displayFlag) {
        this.displayFlag = displayFlag;
    }

    public String getDisplayLayout() {
        return displayLayout;
    }

    public void setDisplayLayout(String displayLayout) {
        this.displayLayout = displayLayout;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
