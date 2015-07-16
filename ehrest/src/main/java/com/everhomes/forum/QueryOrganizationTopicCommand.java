// @formatter:off
package com.everhomes.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizaitonId: 机构ID</li>
 * <li>contentCategory: 内容类型</li>
 * <li>actionCategory: 动作类型，对应以前的serviceType</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class QueryOrganizationTopicCommand {
    private Long organizaitonId;
    private Long contentCategory;
    private Long actionCategory;
    private Long pageAnchor;
    private Integer pageSize;
    
    public QueryOrganizationTopicCommand() {
    }

    public Long getOrganizaitonId() {
        return organizaitonId;
    }

    public void setOrganizaitonId(Long organizaitonId) {
        this.organizaitonId = organizaitonId;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
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
