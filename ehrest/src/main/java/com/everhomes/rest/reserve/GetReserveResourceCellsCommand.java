package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.reserve.ReserveOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>resourceType: 资源类型  {@link com.everhomes.rest.reserve.ReserveResourceType}</li>
 * <li>resourceId: 具体资源id, 例如vip车位预约根据停车场做区分</li>
 * <li>timeType: 时间类型 {@link com.everhomes.rest.reserve.ReserveTimeType}</li>
 * </ul>
 */
public class GetReserveResourceCellsCommand {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String resourceType;
    private Long resourceId;
    private Byte timeType;

    public Byte getTimeType() {
        return timeType;
    }

    public void setTimeType(Byte timeType) {
        this.timeType = timeType;
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
}
