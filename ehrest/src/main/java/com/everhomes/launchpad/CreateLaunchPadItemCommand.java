// @formatter:off
package com.everhomes.launchpad;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 命名空间</li>
 * <li>appId: 应用Id，参考{@link com.everhomes.app.AppConstants}</li>
 * <li>ItemScope: item可见范围列表， 参考{@link com.everhomes.launchpad.ItemScope}</li>
 * <li>itemName: 名称</li>
 * <li>itemLabel: 显示标签</li>
 * <li>itemGroup: item归属某一组，参考{@link com.everhomes.launchpad.ItemGroup}</li>
 * <li>actionName: 动作名称</li>
 * <li>actionIcon: 动作图标</li>
 * <li>actionUri: 动作uri</li>
 * </ul>
 */
public class CreateLaunchPadItemCommand {
    private Integer namespaceId;
    private Long    appId;
    @ItemType(ItemScope.class)
    private List<ItemScope> itemScopes;
    @NotNull
    private String  itemName;
    @NotNull
    private String  itemLabel;
    @NotNull
    private String  itemGroup;
    @NotNull
    private String  actionName;
    @NotNull
    private String  actionIcon;
    @NotNull
    private String  actionUri;

    public CreateLaunchPadItemCommand() {
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

    public List<ItemScope> getItemScopes() {
        return itemScopes;
    }

    public void setItemScopes(List<ItemScope> itemScopes) {
        this.itemScopes = itemScopes;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
