package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/12/18.
 */
public class SetReserveOrderStrategyCommand {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String resourceType;
    private Long resourceId;

    private Byte refundStrategy;
    private Byte overtimeStrategy;

    @ItemType(ReserveRuleStrategyDTO.class)
    private List<ReserveRuleStrategyDTO> refundStrategies;
    @ItemType(ReserveRuleStrategyDTO.class)
    private List<ReserveRuleStrategyDTO> overtimeStrategies;

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

    public List<ReserveRuleStrategyDTO> getRefundStrategies() {
        return refundStrategies;
    }

    public void setRefundStrategies(List<ReserveRuleStrategyDTO> refundStrategies) {
        this.refundStrategies = refundStrategies;
    }

    public List<ReserveRuleStrategyDTO> getOvertimeStrategies() {
        return overtimeStrategies;
    }

    public void setOvertimeStrategies(List<ReserveRuleStrategyDTO> overtimeStrategies) {
        this.overtimeStrategies = overtimeStrategies;
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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
