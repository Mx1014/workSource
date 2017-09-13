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
 * <li>isSearch: 是否支持筛选</li>
 * <li>deleteFlag: 删除符号 0 未删除 1 删除</li>
 * <li>tags: 标签可选项</li>
 * </ul>
 */
public class GetNewsTagResponse {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long parentId;
    private String value;
    private String isSearch;
    @ItemType(NewsTagDTO.class)
    private List<NewsTagDTO> tags;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(String isSearch) {
        this.isSearch = isSearch;
    }

    public List<NewsTagDTO> getTags() {
        return tags;
    }

    public void setTags(List<NewsTagDTO> tags) {
        this.tags = tags;
    }
}
