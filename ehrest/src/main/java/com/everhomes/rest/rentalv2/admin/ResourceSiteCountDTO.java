package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;

import java.util.List;

/**
 * <ul>
 * <li>ownerType: ownerType {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>sourceType: sourceType 默认规则：default_rule， 资源规则：resource_rule{@link RuleSourceType}</li>
 * <li>sourceId: 资源id</li>
 * <li>	exclusiveFlag：    	是否为独占资源0否 1 是	</li>
 * <li>	autoAssign：       	是否动态分配 1是 0否	</li>
 * <li>	siteCounts：资源数量</li>
 * <li>	siteNumbers：资源编号列表 {String}</li>
 * </ul>
 */
public class ResourceSiteCountDTO {
    private String ownerType;

    private Long ownerId;

    private Long resourceTypeId;

    private String resourceType;

    private String sourceType;

    private Long sourceId;
    private java.lang.Byte       exclusiveFlag;
    private java.lang.Byte       autoAssign;
    private Double siteCounts;
    @ItemType(String.class)
    private List<String> siteNumbers;

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

    public Byte getExclusiveFlag() {
        return exclusiveFlag;
    }

    public void setExclusiveFlag(Byte exclusiveFlag) {
        this.exclusiveFlag = exclusiveFlag;
    }

    public Byte getAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(Byte autoAssign) {
        this.autoAssign = autoAssign;
    }

    public Double getSiteCounts() {
        return siteCounts;
    }

    public void setSiteCounts(Double siteCounts) {
        this.siteCounts = siteCounts;
    }

    public List<String> getSiteNumbers() {
        return siteNumbers;
    }

    public void setSiteNumbers(List<String> siteNumbers) {
        this.siteNumbers = siteNumbers;
    }
}
