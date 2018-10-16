package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id : 主键</li>
 *  <li>namespaceId : 域空间Id</li>
 *  <li>ownerType : 所属类型</li>
 *  <li>ownerId : 所属Id</li>
 *  <li>doorAccessId : 门禁Id</li>
 *  <li>doorAccessName : 门禁名称</li>
 *  <li>defaultAuthDurationType : 默认访客授权有效期种类,0 天数，1 小时数</li>
 *  <li>defaultAuthDuration : 默认访客授权有效期</li>
 *  <li>defaultEnableAuthCount : 默认访客授权次数开关 0 关 1 开</li>
 *  <li>defaultAuthCount : 默认访客授权次数</li>
 *  <li>maxDuration : 访客授权最长有效期（门禁系统）</li>
 *  <li>maxCount : 访客授权最大次数（门禁系统）</li>
 *  <li>enableAmount:门禁是否允许授权按次开门，1是0否（门禁系统）</li>
 *  <li>enableDuration:门禁是否允许授权按时间开门，1是0否（门禁系统）</li>
 *  <li>defaultDoorAccessFlag : 默认门禁组 0 非默认 1 默认</li>
 * </ul>
 */

public class VisitorSysDoorAccessDTO {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long doorAccessId;
    private String doorAccessName;
    private Byte defaultAuthDurationType;
    private Integer defaultAuthDuration;
    private Byte defaultEnableAuthCount;
    private Integer defaultAuthCount;
//  ---------从门禁获取数据---------
    private Integer maxDuration;
    private Integer maxCount;
    private Byte enableAmount;
    private Byte enableDuration;
//  ------------------------------
    private Byte defaultDoorAccessFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getDoorAccessId() {
        return doorAccessId;
    }

    public void setDoorAccessId(Long doorAccessId) {
        this.doorAccessId = doorAccessId;
    }

    public String getDoorAccessName() {
        return doorAccessName;
    }

    public void setDoorAccessName(String doorAccessName) {
        this.doorAccessName = doorAccessName;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }


    public Byte getEnableAmount() {
        return enableAmount;
    }

    public void setEnableAmount(Byte enableAmount) {
        this.enableAmount = enableAmount;
    }

    public Byte getEnableDuration() {
        return enableDuration;
    }

    public void setEnableDuration(Byte enableDuration) {
        this.enableDuration = enableDuration;
    }

    public Byte getDefaultDoorAccessFlag() {
        return defaultDoorAccessFlag;
    }

    public void setDefaultDoorAccessFlag(Byte defaultDoorAccessFlag) {
        this.defaultDoorAccessFlag = defaultDoorAccessFlag;
    }

    public Byte getDefaultAuthDurationType() {
        return defaultAuthDurationType;
    }

    public void setDefaultAuthDurationType(Byte defaultAuthDurationType) {
        this.defaultAuthDurationType = defaultAuthDurationType;
    }

    public Integer getDefaultAuthDuration() {
        return defaultAuthDuration;
    }

    public void setDefaultAuthDuration(Integer defaultAuthDuration) {
        this.defaultAuthDuration = defaultAuthDuration;
    }

    public Byte getDefaultEnableAuthCount() {
        return defaultEnableAuthCount;
    }

    public void setDefaultEnableAuthCount(Byte defaultEnableAuthCount) {
        this.defaultEnableAuthCount = defaultEnableAuthCount;
    }

    public Integer getDefaultAuthCount() {
        return defaultAuthCount;
    }

    public void setDefaultAuthCount(Integer defaultAuthCount) {
        this.defaultAuthCount = defaultAuthCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
