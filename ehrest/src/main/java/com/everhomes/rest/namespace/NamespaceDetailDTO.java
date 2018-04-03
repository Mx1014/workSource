// @formatter:off
package com.everhomes.rest.namespace;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>resourceType: 域空间下所管理的小区类型，参考{@link com.everhomes.rest.namespace.NamespaceCommunityType}</li>
 * <li>pmMask : 蒙版信息</li>
 * <li>nameType : 名称显示类型</li>
 * </ul>
 */
public class NamespaceDetailDTO {
    private Long id;
    private Integer namespaceId;
    private String resourceType;
    private Byte authPopupConfig;
    @ItemType(MaskDTO.class)
    private List<MaskDTO> pmMasks;
    private Integer maskFlag;
    private Byte nameType;


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

    public Byte getAuthPopupConfig() {
        return authPopupConfig;
    }

    public void setAuthPopupConfig(Byte authPopupConfig) {
        this.authPopupConfig = authPopupConfig;
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

    public Integer getMaskFlag() {
        return maskFlag;
    }

    public void setMaskFlag(Integer maskFlag) {
        this.maskFlag = maskFlag;
    }

    public Byte getNameType() {
        return nameType;
    }

    public void setNameType(Byte nameType) {
        this.nameType = nameType;
    }
}
