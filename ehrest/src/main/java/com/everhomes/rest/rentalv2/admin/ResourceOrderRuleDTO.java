package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerType: ownerType {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>sourceType: sourceType 默认规则：default_rule， 资源规则：resource_rule{@link RuleSourceType}</li>
 * <li>sourceId: 资源id，如果是默认规则，则不填</li>
 * <li>refundStrategy: 退款策略 {@link RentalOrderStrategy}</li>
 * <li>overtimeStrategy: 超时加收策略 {@link RentalOrderStrategy}</li>
 * <li>refundStrategies: 退款策略列表 {@link com.everhomes.rest.rentalv2.admin.RentalOrderRuleDTO}</li>
 * <li>overtimeStrategies: 加收策略列表 {@link com.everhomes.rest.rentalv2.admin.RentalOrderRuleDTO}</li>
 * </ul>
 */
public class ResourceOrderRuleDTO {
    private String ownerType;

    private Long ownerId;

    private Long resourceTypeId;

    private String resourceType;

    private String sourceType;

    private Long sourceId;

    private Byte refundStrategy;
    private Byte overtimeStrategy;

    @ItemType(RentalOrderRuleDTO.class)
    private List<RentalOrderRuleDTO> refundStrategies;
    @ItemType(RentalOrderRuleDTO.class)
    private List<RentalOrderRuleDTO> overtimeStrategies;
    private List<RentalRefundTipDTO> refundTips;

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

    public Byte getRefundStrategy() {
        return refundStrategy;
    }

    public void setRefundStrategy(Byte refundStrategy) {
        this.refundStrategy = refundStrategy;
    }

    public Byte getOvertimeStrategy() {
        return overtimeStrategy;
    }

    public void setOvertimeStrategy(Byte overtimeStrategy) {
        this.overtimeStrategy = overtimeStrategy;
    }

    public List<RentalOrderRuleDTO> getRefundStrategies() {
        return refundStrategies;
    }

    public void setRefundStrategies(List<RentalOrderRuleDTO> refundStrategies) {
        this.refundStrategies = refundStrategies;
    }

    public List<RentalOrderRuleDTO> getOvertimeStrategies() {
        return overtimeStrategies;
    }

    public void setOvertimeStrategies(List<RentalOrderRuleDTO> overtimeStrategies) {
        this.overtimeStrategies = overtimeStrategies;
    }

    public List<RentalRefundTipDTO> getRefundTips() {
        return refundTips;
    }

    public void setRefundTips(List<RentalRefundTipDTO> refundTips) {
        this.refundTips = refundTips;
    }


}
