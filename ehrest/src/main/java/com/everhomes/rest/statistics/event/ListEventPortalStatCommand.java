// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>statType: 统计类型{@link com.everhomes.rest.statistics.event.StatEventPortalStatType}</li>
 *     <li>parentId: 根据parentId获取下面的门户项目</li>
 *     <li>startDate: 开始时间戳</li>
 *     <li>endDate: 结束时间戳</li>
 * </ul>
 */
public class ListEventPortalStatCommand {

    private Integer namespaceId;

    @NotNull
    private Byte statType;// 统计类型：门户
    @NotNull
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
