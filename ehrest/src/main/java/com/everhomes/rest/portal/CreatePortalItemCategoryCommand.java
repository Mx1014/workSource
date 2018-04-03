// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间</li>
 * <li>itemGroupId: item group id</li>
 * <li>name: 分类名称</li>
 * <li>iconUri: icon图片的uri</li>
 * <li>itemIds: itemId列表</li>
 * <li>scopes: item范围</li>
 * </ul>
 */
public class CreatePortalItemCategoryCommand {

	private Integer namespaceId;

	private Long itemGroupId;

	private String name;

	private String iconUri;

	@ItemType(Long.class)
	private List<Long> itemIds;

	@ItemType(PortalScope.class)
	private List<PortalScope> scopes;

	public CreatePortalItemCategoryCommand() {

	}

	public CreatePortalItemCategoryCommand(Integer namespaceId, String name, String iconUri, List<Long> itemIds, List<PortalScope> scopes) {
		super();
		this.namespaceId = namespaceId;
		this.name = name;
		this.iconUri = iconUri;
		this.itemIds = itemIds;
		this.scopes = scopes;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public List<Long> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
	}

	public List<PortalScope> getScopes() {
		return scopes;
	}

	public void setScopes(List<PortalScope> scopes) {
		this.scopes = scopes;
	}

	public Long getItemGroupId() {
		return itemGroupId;
	}

	public void setItemGroupId(Long itemGroupId) {
		this.itemGroupId = itemGroupId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
