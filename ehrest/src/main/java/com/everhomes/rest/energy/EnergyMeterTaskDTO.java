package com.everhomes.rest.energy;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>ownerType: 所有者类型 如：PM</li>
 *     <li>ownerId: 管理机构id</li>
 *     <li>targetType: 关联类型 如 community</li>
 *     <li>targetId: 关联id communityId</li>
 *     <li>planId: 计划id</li>
 *     <li>meterId: 表id</li>
 *     <li>meterNumber: 表记号码</li>
 *     <li>meterType: 表记类型</li>
 *     <li>meterName: 表记名称</li>
 *     <li>apartmentFloor: 楼层</li>
 *     <li>address: 楼栋门牌</li>
 *     <li>executiveStartTime: 任务开始时间</li>
 *     <li>executiveExpireTime: 任务结束时间</li>
 *     <li>lastTaskReading: 上次任务读表数</li>
 *     <li>reading: 表读数</li>
 *     <li>status: 状态</li>
 *     <li>defaultOrder: 顺序</li>
 *     <li>createTime: 创建时间</li>
 *     <li>operatorUid: 操作人</li>
 *     <li>updateTime: 操作时间</li>
 * </ul>
 * Created by ying.xiong on 2017/10/23.
 */
public class EnergyMeterTaskDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;
    private Long planId;
    private Long meterId;
    private Byte meterType;
    private String meterNumber;
    private String meterName;
    private String apartmentFloor;
    private String address;
    private Timestamp executiveStartTime;
    private Timestamp executiveExpireTime;
    private BigDecimal lastTaskReading;
    private BigDecimal reading;
    private Byte status;
    private Integer defaultOrder;
    private Timestamp createTime;
    private Long operatorUid;
    private Timestamp updateTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApartmentFloor() {
        return apartmentFloor;
    }

    public void setApartmentFloor(String apartmentFloor) {
        this.apartmentFloor = apartmentFloor;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Timestamp getExecutiveExpireTime() {
        return executiveExpireTime;
    }

    public void setExecutiveExpireTime(Timestamp executiveExpireTime) {
        this.executiveExpireTime = executiveExpireTime;
    }

    public Timestamp getExecutiveStartTime() {
        return executiveStartTime;
    }

    public void setExecutiveStartTime(Timestamp executiveStartTime) {
        this.executiveStartTime = executiveStartTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLastTaskReading() {
        return lastTaskReading;
    }

    public void setLastTaskReading(BigDecimal lastTaskReading) {
        this.lastTaskReading = lastTaskReading;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public Byte getMeterType() {
        return meterType;
    }

    public void setMeterType(Byte meterType) {
        this.meterType = meterType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
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

    public BigDecimal getReading() {
        return reading;
    }

    public void setReading(BigDecimal reading) {
        this.reading = reading;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
