package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.reserve.ReserveOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>resourceType: 资源类型  {@link com.everhomes.rest.reserve.ReserveResourceType}</li>
 * <li>resourceId: 具体资源id, 例如vip车位预约根据停车场做区分</li>
 * <li>refundStrategy: 退款策略 1:自定义 2：原价{@link ReserveOrderHandleRuleType}</li>
 * <li>overtimeStrategy: 加收策略 1:自定义 2：原价 {@link ReserveOrderHandleRuleType}</li>
 * <li>refundStrategies: 自定义策略列表 {@link ReserveOrderRuleDTO}</li>
 * <li>overtimeStrategies: 自定义策略列表 {@link ReserveOrderRuleDTO}</li>
 * </ul>
 */
public class ReserveOrderHandleRuleDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String resourceType;
    private Long resourceId;

    private Byte refundStrategy;
    private Byte overtimeStrategy;

    @ItemType(ReserveOrderRuleDTO.class)
    private List<ReserveOrderRuleDTO> refundStrategies;
    @ItemType(ReserveOrderRuleDTO.class)
    private List<ReserveOrderRuleDTO> overtimeStrategies;

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

    public List<ReserveOrderRuleDTO> getRefundStrategies() {
        return refundStrategies;
    }

    public void setRefundStrategies(List<ReserveOrderRuleDTO> refundStrategies) {
        this.refundStrategies = refundStrategies;
    }

    public List<ReserveOrderRuleDTO> getOvertimeStrategies() {
        return overtimeStrategies;
    }

    public void setOvertimeStrategies(List<ReserveOrderRuleDTO> overtimeStrategies) {
        this.overtimeStrategies = overtimeStrategies;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
