package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/12/18.
 */
public class SetReserveDiscountStrategyCommand {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String resourceType;
    private Long resourceId;

    @ItemType(ReserveDiscountStrategyDTO.class)
    private List<ReserveDiscountStrategyDTO> strategyDTOS;

    public List<ReserveDiscountStrategyDTO> getStrategyDTOS() {
        return strategyDTOS;
    }

    public void setStrategyDTOS(List<ReserveDiscountStrategyDTO> strategyDTOS) {
        this.strategyDTOS = strategyDTOS;
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
