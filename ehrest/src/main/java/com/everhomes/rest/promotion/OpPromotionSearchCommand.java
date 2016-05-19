package com.everhomes.rest.promotion;

/**
 * <ul>
 * <li>keyword: title/description/id</li>
 * </ul>
 * @author janson
 *
 */
public class OpPromotionSearchCommand {
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
}
