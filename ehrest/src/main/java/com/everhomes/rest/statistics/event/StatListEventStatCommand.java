// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>parentId: 门户的item的parentId</li>
 *     <li>identifier: 为了获取一个layout下的一个itemGroup的所有时间段的统计结果(LayoutName + groupName + itemGroup),顶部导航栏的identifier:TopNavigation, 底部导航栏的identifier:BottomNavigation</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 *     <li>startDate: 开始时间戳</li>
 *     <li>endDate: 结束时间戳</li>
 * </ul>
 */
public class StatListEventStatCommand {

    private Integer namespaceId;
    @NotNull
    private Long parentId;
    @NotNull
    private String identifier;//
    private String ownerType;
    private Long ownerId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
