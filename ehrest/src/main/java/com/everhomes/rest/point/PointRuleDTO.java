package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: 积分系统id</li>
 *     <li>categoryId: 积分模块id</li>
 *     <li>moduleId: 模块id</li>
 *     <li>displayName: 显示名称</li>
 *     <li>description: 描述</li>
 *     <li>arithmeticType: 算数计算类型 {@link com.everhomes.rest.point.PointArithmeticType}</li>
 *     <li>points: 积分数量</li>
 *     <li>limitType: 限制类型 {@link com.everhomes.rest.point.PointRuleLimitType}</li>
 *     <li>limitData: 限制data, e.g: {"times": 1}</li>
 *     <li>eventName: 事件名称</li>
 *     <li>status: status</li>
 * </ul>
 */
public class PointRuleDTO {

    private Long id;
    private Integer namespaceId;
    private Long systemId;
    private Long categoryId;
    private Long moduleId;
    private String displayName;
    private String description;
    private Byte arithmeticType;
    private Long points;
    private Byte limitType;
    private String limitData;
    private String eventName;
    private Byte status;
    private Timestamp createTime;
    private Long creatorUid;
    private Timestamp updateTime;
    private Long updateUid;

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

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getLimitType() {
        return limitType;
    }

    public void setLimitType(Byte limitType) {
        this.limitType = limitType;
    }

    public String getLimitData() {
        return limitData;
    }

    public void setLimitData(String limitData) {
        this.limitData = limitData;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Byte getArithmeticType() {
        return arithmeticType;
    }

    public void setArithmeticType(Byte arithmeticType) {
        this.arithmeticType = arithmeticType;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Long getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
