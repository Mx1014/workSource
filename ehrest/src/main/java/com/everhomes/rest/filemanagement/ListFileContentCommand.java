package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>catalogId: 目录id</li>
 * <li>contentId: 内容id</li>
 * <li>keywords: 关键词</li>
 * </ul>
 */
public class ListFileContentCommand {

    private Long catalogId;

    private Long contentId;

    private String keywords;

    private Integer pageOffset;

    private Integer pageSize;

    public ListFileContentCommand() {
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
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
