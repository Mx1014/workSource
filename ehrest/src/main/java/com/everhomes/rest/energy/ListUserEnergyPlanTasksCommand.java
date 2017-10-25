package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>meterType: 表计类型 参考{@link com.everhomes.rest.energy.EnergyMeterType}</li>
 *     <li>apartmentFloor: 楼层</li>
 *     <li>status: 状态 参考{@link com.everhomes.rest.energy.EnergyMeterTaskStatus}</li>
 *     <li>ownerType: 所有者类型 如：PM</li>
 *     <li>ownerId: 管理机构id</li>
 *     <li>targetType: 关联类型 如 community</li>
 *     <li>targetId: 关联id communityId</li>
 *     <li>pageAnchor: 下页锚点</li>
 *     <li>pageSize: 每页数量</li>
 * </ul>
 * Created by ying.xiong on 2017/10/20.
 */
public class ListUserEnergyPlanTasksCommand {
    private Byte meterType;
    private String apartmentFloor;
    private Byte status;
    private String targetType;
    private Long targetId;
    private Long ownerId;
    private String ownerType;

    private Long pageAnchor;
    private Integer pageSize;

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

    public String getApartmentFloor() {
        return apartmentFloor;
    }

    public void setApartmentFloor(String apartmentFloor) {
        this.apartmentFloor = apartmentFloor;
    }

    public Byte getMeterType() {
        return meterType;
    }

    public void setMeterType(Byte meterType) {
        this.meterType = meterType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
