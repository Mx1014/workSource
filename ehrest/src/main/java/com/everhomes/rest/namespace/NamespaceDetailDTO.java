// @formatter:off
package com.everhomes.rest.namespace;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>resourceType: 域空间下所管理的小区类型，参考{@link com.everhomes.rest.namespace.NamespaceCommunityType}</li>
 * <li>pmMask : pm_admin场景</li>
 * <li>parkMask ：park_tourist场景</li>
 * </ul>
 */
public class NamespaceDetailDTO {
    private Long id;
    private Integer namespaceId;
    private String resourceType;
    private List<MaskDTO> pmMasks;
    private List<MaskDTO> parkMasks;


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

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<MaskDTO> getPmMasks() {
        return pmMasks;
    }

    public void setPmMasks(List<MaskDTO> pmMasks) {
        this.pmMasks = pmMasks;
    }

    public List<MaskDTO> getParkMasks() {
        return parkMasks;
    }

    public void setParkMasks(List<MaskDTO> parkMasks) {
        this.parkMasks = parkMasks;
    }
}
