package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>planId: 计划id</li>
 *  <li>groupId: 业务组id</li>
 *  <li>groupName: 业务组组名</li>
 *  <li>positionId: 通用岗位id</li>
 * </ul>
 * Created by ying.xiong on 2017/10/19.
 */
public class EnergyPlanGroupDTO {
    private Long id;

    private Long groupId;

    private String groupName;

    private Long planId;

    private Long positionId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
