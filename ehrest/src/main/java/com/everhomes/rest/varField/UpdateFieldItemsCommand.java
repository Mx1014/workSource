package com.everhomes.rest.varField;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>fieldId: 所属字段系统id</li>
 *     <li>items: 字段选择项信息， 参考{@link ScopeFieldItemInfo}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class UpdateFieldItemsCommand {
    private Integer namespaceId;

    private Long communityId;

    private Long fieldId;

    @ItemType(ScopeFieldItemInfo.class)
    private List<ScopeFieldItemInfo> items;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
