package com.everhomes.rest.news;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属ID</li>
 * <li>parentId: 父标签id</li>
 * <li>value: 值</li>
 * <li>isDefault: 是否是默认选项</li>
 * <li>deleteFlag: 删除符号 0 未删除 1 删除</li>
 * <li>isSearch: 是否支持筛选</li>
 * <li>childTags: 可选标签</li>
 * <li>defaultOrder: 顺序</li>
 * </ul>
 */
public class NewsTagDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long parentId;
    private String value;
    private Byte isSearch;
    private Byte isDefault;
    private Byte deleteFlag;
    private Long defaultOrder;
    @ItemType(NewsTagDTO.class)
    private List<NewsTagDTO> childTags;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Long defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Byte getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(Byte isSearch) {
        this.isSearch = isSearch;
    }

    public List<NewsTagDTO> getChildTags() {
        return childTags;
    }

    public void setChildTags(List<NewsTagDTO> childTags) {
        this.childTags = childTags;
    }
}
