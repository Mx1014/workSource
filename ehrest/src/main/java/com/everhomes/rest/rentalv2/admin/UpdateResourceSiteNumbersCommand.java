package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <li>multiUnit: 是否允许预约多个场所</li>
 * <li>	autoAssign：       	是否动态分配 1是 0否	</li>
 * <li>	siteCounts：资源数量</li>
 * <li>	siteNumbers：资源编号列表 {String}</li>
 */
public class UpdateResourceSiteNumbersCommand {
    private Long rentalSiteId;
    private String resourceType;
    private Byte multiUnit;
    private java.lang.Byte       autoAssign;
    private Double siteCounts;
    @ItemType(SiteNumberDTO.class)
    private List<SiteNumberDTO> siteNumbers;

    public Long getRentalSiteId() {
        return rentalSiteId;
    }

    public void setRentalSiteId(Long rentalSiteId) {
        this.rentalSiteId = rentalSiteId;
    }

    public Byte getMultiUnit() {
        return multiUnit;
    }

    public void setMultiUnit(Byte multiUnit) {
        this.multiUnit = multiUnit;
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

    public List<SiteNumberDTO> getSiteNumbers() {
        return siteNumbers;
    }

    public void setSiteNumbers(List<SiteNumberDTO> siteNumbers) {
        this.siteNumbers = siteNumbers;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
