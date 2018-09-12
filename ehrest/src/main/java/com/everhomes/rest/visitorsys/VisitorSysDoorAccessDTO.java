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
 *  <li>authRuleType : 授权规则种类，0 时长，1 次数</li>
 *  <li>maxDuration : 访客授权最长有效期</li>
 *  <li>maxCount : 访客授权最大次数</li>
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
    private Byte authRuleType;
    private Integer maxDuration;
    private Integer maxCount;
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

    public Byte getAuthRuleType() {
        return authRuleType;
    }

    public void setAuthRuleType(Byte authRuleType) {
        this.authRuleType = authRuleType;
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

    public Byte getDefaultDoorAccessFlag() {
        return defaultDoorAccessFlag;
    }

    public void setDefaultDoorAccessFlag(Byte defaultDoorAccessFlag) {
        this.defaultDoorAccessFlag = defaultDoorAccessFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
