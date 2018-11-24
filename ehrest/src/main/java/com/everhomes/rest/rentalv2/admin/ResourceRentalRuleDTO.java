package com.everhomes.rest.rentalv2.admin;

import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: ownerType {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>sourceType: sourceType 默认规则：default_rule， 资源规则：resource_rule{@link RuleSourceType}</li>
 * <li>sourceId: 资源id，如果是默认规则，则不填</li>
 * <li>multiTimeInterval: 是否允许预约多个时段</li>
 * <li>rentalEndTimeFlag: 至少提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalStartTimeFlag: 最多提前预约时间标志 1：限制 0：不限制 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalEndTime: 至少提前预约时间</li>
 * <li>rentalStartTime: 最多提前预约时间</li>
 * <li>remarkFlag: 备注字段是否必填 0否 1是</li>
 * <li>remark: 备注显示文案</li>
 * </ul>
 */
public class ResourceRentalRuleDTO {
    private String ownerType;

    private Long ownerId;

    private Long resourceTypeId;

    private String resourceType;

    private String sourceType;

    private Long sourceId;

    private Byte multiTimeInterval;

    private Byte rentalEndTimeFlag;
    private Byte rentalStartTimeFlag;
    private Long rentalEndTime;
    private Long rentalStartTime;

    private Byte remarkFlag;
    private String remark;
    private Byte fileFlag;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Byte getMultiTimeInterval() {
        return multiTimeInterval;
    }

    public void setMultiTimeInterval(Byte multiTimeInterval) {
        this.multiTimeInterval = multiTimeInterval;
    }

    public Byte getRentalEndTimeFlag() {
        return rentalEndTimeFlag;
    }

    public void setRentalEndTimeFlag(Byte rentalEndTimeFlag) {
        this.rentalEndTimeFlag = rentalEndTimeFlag;
    }

    public Byte getRentalStartTimeFlag() {
        return rentalStartTimeFlag;
    }

    public void setRentalStartTimeFlag(Byte rentalStartTimeFlag) {
        this.rentalStartTimeFlag = rentalStartTimeFlag;
    }

    public Long getRentalEndTime() {
        return rentalEndTime;
    }

    public void setRentalEndTime(Long rentalEndTime) {
        this.rentalEndTime = rentalEndTime;
    }

    public Long getRentalStartTime() {
        return rentalStartTime;
    }

    public void setRentalStartTime(Long rentalStartTime) {
        this.rentalStartTime = rentalStartTime;
    }

    public Byte getRemarkFlag() {
        return remarkFlag;
    }

    public void setRemarkFlag(Byte remarkFlag) {
        this.remarkFlag = remarkFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getFileFlag() {
        return fileFlag;
    }

    public void setFileFlag(Byte fileFlag) {
        this.fileFlag = fileFlag;
    }
}
