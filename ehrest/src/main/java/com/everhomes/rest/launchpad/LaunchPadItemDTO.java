// @formatter:off
package com.everhomes.rest.launchpad;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 命名空间</li>
 * <li>appId: 应用Id</li>
 * <li>scopeType: item可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * <li>itemLocation: item 的路径</li>
 * <li>itemGroup: 当前item归属的组，参考{@link com.everhomes.rest.launchpad.ItemGroup}</li>
 * <li>itemName: 名称</li>
 * <li>itemLabel: 显示标签</li>
 * <li>iconUri: 图标uri</li>
 * <li>iconUrl: 图标url</li>
 * <li>selectedIconUri: 选中图标uri</li>
 * <li>selectedIconUrl: 选中图标url</li>
 * <li>itemWidth: 图标的宽</li>
 * <li>itemHeight: 图标的高</li>
 * <li>actionType: 动作类型，参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 * <li>actionData: 动作所需要的参数，由actionType决定</li>
 * <li>defaultOrder: 默认顺序</li>
 * <li>applyPolicy: 应用策略{@link com.everhomes.rest.launchpad.ApplyPolicy}</li>
 * <li>minVersion: item 最小版本号</li>
 * <li>displayFlag: 是否显示{@link com.everhomes.rest.launchpad.ItemDisplayFlag}</li>
 * <li>displayLayout: 图标尺寸 格式：1x2</li>
 * <li>bgcolor: item的背景颜色</li>
 * <li>scaleType: 图标是否需要裁剪0-不需要，1-需要</li>
 * <li>deleteFlag: 是否可删除,0-不可删除,1-可删除,详情{@link com.everhomes.rest.launchpad.DeleteFlagType}</li>
 * <li>editFlag: 是否可编辑,0-不可编辑,1-可编辑 详情{@link com.everhomes.rest.launchpad.EditFlagType}</li>
 * <li>aliasIconUri: 图标别名uri。现在用于搜索结果页面，原有iconUri有圆形、方形等，展现风格不一致。应对这样的场景增加aliasIconUri，存储圆形默认图片。搜索功能模块当它不为空时用它替换iconUri， 返回客户端的是统一风格的iconUri。 add by yanjun 20170420</li>
 * <li>aliasIconUrl:  图标别名url。逻辑同aliasIconUri</li>
 * <li>searchTypeId: 内容类型id</li>
 * <li>searchTypeName: 内容类型名称</li>
 * <li>contentType: 内容类型</li>
 * </ul>
 */
public class LaunchPadItemDTO {
    private Long    id;
    private Integer namespaceId;
    private Long    appId;
    private String  scopeType;
    private Long    scopeId;
    private String  itemLocation;
    private String  itemGroup;
    private String  itemName;
    private String  itemLabel;
    private String  iconUri;
    private String  iconUrl;
    private String  selectedIconUri;
    private String  selectedIconUrl;
    private Integer itemWidth;
    private Integer itemHeight;
    private Byte    actionType;
    private String  actionData;
    private Integer defaultOrder;
    private Byte    applyPolicy;
    private Long    minVersion;
    private Byte    displayFlag;
    private String  displayLayout;
    private Integer    bgcolor;
    private Byte    scaleType;
    private Byte deleteFlag;
    private Byte editFlag;
    private Integer moreOrder;
    private String  aliasIconUri;
    private String  aliasIconUrl;
    
    private Long searchTypeId;
	private String searchTypeName;
	private String contentType;

    public String getSelectedIconUri() {
		return selectedIconUri;
	}

	public void setSelectedIconUri(String selectedIconUri) {
		this.selectedIconUri = selectedIconUri;
	}

	public String getSelectedIconUrl() {
		return selectedIconUrl;
	}

	public void setSelectedIconUrl(String selectedIconUrl) {
		this.selectedIconUrl = selectedIconUrl;
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

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public Long getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(Long minVersion) {
        this.minVersion = minVersion;
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

    public Integer getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(Integer bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Byte getScaleType() {
        return scaleType;
    }

    public void setScaleType(Byte scaleType) {
        this.scaleType = scaleType;
    }
    
    public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

    public Integer getMoreOrder() {
        return moreOrder;
    }

    public void setMoreOrder(Integer moreOrder) {
        this.moreOrder = moreOrder;
    }

	public Byte getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(Byte editFlag) {
		this.editFlag = editFlag;
	}
	public String getAliasIconUri() {
		return aliasIconUri;
	}

	public void setAliasIconUri(String aliasIconUri) {
		this.aliasIconUri = aliasIconUri;
	}

	public String getAliasIconUrl() {
		return aliasIconUrl;
	}

	public void setAliasIconUrl(String aliasIconUrl) {
		this.aliasIconUrl = aliasIconUrl;
	}
	
	public Long getSearchTypeId() {
		return searchTypeId;
	}

	public void setSearchTypeId(Long searchTypeId) {
		this.searchTypeId = searchTypeId;
	}

	public String getSearchTypeName() {
		return searchTypeName;
	}

	public void setSearchTypeName(String searchTypeName) {
		this.searchTypeName = searchTypeName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this,new String[]{"id","scopeType","scopeId","applyPolicy"} );
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj,new String[]{"id","scopeType","scopeId","applyPolicy"});
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
