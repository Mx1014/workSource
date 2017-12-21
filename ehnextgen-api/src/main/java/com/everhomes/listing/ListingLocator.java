package com.everhomes.listing;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

public class ListingLocator implements Serializable {
    private static final long serialVersionUID = -4503068736007287833L;

    private Long anchor;
    private long entityId;
    
    public ListingLocator() {
    }
    
    public ListingLocator(long entityId) {
        this.entityId = entityId;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
