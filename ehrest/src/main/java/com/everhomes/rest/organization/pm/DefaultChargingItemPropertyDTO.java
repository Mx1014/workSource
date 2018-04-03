package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>defaultChargingItemId: 默认计价条款id</li>
 *     <li>propertyType: 资源类型 0: community; 1: building; 2: apartment</li>
 *     <li>propertyId: 资源id</li>
 *     <li>propertyName: 资源名</li>
 * </ul>
 * Created by ying.xiong on 2017/10/27.
 */
public class DefaultChargingItemPropertyDTO {
    private Long id;
    private Integer namespaceId;
    private Long defaultChargingItemId;
    private Byte propertyType;
    private Long propertyId;
    private String propertyName;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Long getDefaultChargingItemId() {
        return defaultChargingItemId;
    }

    public void setDefaultChargingItemId(Long defaultChargingItemId) {
        this.defaultChargingItemId = defaultChargingItemId;
    }

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

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Byte getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Byte propertyType) {
        this.propertyType = propertyType;
    }
}
