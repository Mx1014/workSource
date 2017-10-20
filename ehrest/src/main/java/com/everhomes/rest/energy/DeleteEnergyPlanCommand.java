package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>planId: 计划id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>ownerType: 所有者类型 如：PM</li>
 *     <li>ownerId: 管理机构id</li>
 *     <li>targetType: 关联类型 如 community</li>
 *     <li>targetId: 关联id communityId</li>
 * </ul>
 * Created by ying.xiong on 2017/10/20.
 */
public class DeleteEnergyPlanCommand {
    private Long planId;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
