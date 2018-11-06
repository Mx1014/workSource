package com.everhomes.rest.varField;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>fieldIds: 所属字段系统id列表</li>
 *     <li>items: 字段选择项信息， 参考{@link ScopeFieldItemInfo}</li>
 *     <li>categoryId: 合同类型categoryId，用于多入口</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class UpdateFieldItemsCommand {
    private Integer namespaceId;

    private Long communityId;

    @ItemType(Long.class)
    private List<Long> fieldIds;

    @ItemType(ScopeFieldItemInfo.class)
    private List<ScopeFieldItemInfo> items;

	private Long categoryId;

    private Long ownerId;

    private String ownerType;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    
    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<Long> getFieldIds() {
        return fieldIds;
    }

    public void setFieldIds(List<Long> fieldIds) {
        this.fieldIds = fieldIds;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<ScopeFieldItemInfo> getItems() {
        return items;
    }

    public void setItems(List<ScopeFieldItemInfo> items) {
        this.items = items;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
