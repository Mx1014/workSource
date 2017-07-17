// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>parentId: parentId</li>
 *     <li>startDate: startDate</li>
 *     <li>endDate: endDate</li>
 * </ul>
 */
public class ListEventPortalStatCommand {

    private Integer namespaceId;

    private Byte statType;// 统计类型， 导航栏，门户，工具栏
    private Long parentId;

    @NotNull
    private Long startDate;
    @NotNull
    private Long endDate;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getStatType() {
        return statType;
    }

    public void setStatType(Byte statType) {
        this.statType = statType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
