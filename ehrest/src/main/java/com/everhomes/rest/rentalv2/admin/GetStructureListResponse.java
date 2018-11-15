package com.everhomes.rest.rentalv2.admin;

import com.everhomes.rest.rentalv2.SiteItemDTO;
import com.everhomes.rest.rentalv2.SiteStructureDTO;

import java.util.List;

public class GetStructureListResponse {
    private List<SiteStructureDTO> siteStructures;
    private Long nextPageAnchor;

    public List<SiteStructureDTO> getSiteStructures() {
        return siteStructures;
    }

    public void setSiteStructures(List<SiteStructureDTO> siteStructures) {
        this.siteStructures = siteStructures;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
