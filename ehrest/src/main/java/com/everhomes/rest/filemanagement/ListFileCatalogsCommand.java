package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 场景id</li>
 * <li>ownerType: 场景类型</li>
 * <li>keywords: 搜索关键词
 * <li>pageAnchor: 锚点值</li>
 * <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListFileCatalogsCommand {

    private Long ownerId;

    private String ownerType;

    private String keywords;

    private Long pageAnchor;

    private Integer pageSize;

    public ListFileCatalogsCommand() {
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
