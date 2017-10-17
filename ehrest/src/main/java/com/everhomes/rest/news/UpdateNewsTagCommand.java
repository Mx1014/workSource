package com.everhomes.rest.news;

import com.everhomes.discover.ItemType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属ID</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>value: 标签名</li>
 * <li>parentId: 父标签id</li>
 * <li>isSearch: 是否支持筛选 0否 1是</li>
 * <li>deleteFlag: 删除标志 0未删除 1删除</li>
 * <li>tags: 标签可选项</li>
 * </ul>
 */
public class UpdateNewsTagCommand {

    private Long id;
    private Integer namespaceId;
    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    private String value;
    private Long parentId;
    private Byte isSearch;
    private Byte deleteFlag;
    @ItemType(NewsTagDTO.class)
    private List<NewsTagDTO> tags;

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

    public List<NewsTagDTO> getTags() {
        return tags;
    }

    public void setTags(List<NewsTagDTO> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
