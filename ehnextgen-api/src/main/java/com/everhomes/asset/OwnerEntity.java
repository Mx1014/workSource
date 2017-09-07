//@formatter:off
package com.everhomes.asset;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2017/8/17.
 */

public class OwnerEntity {
    private String ownerType;
    private Long ownerId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OwnerEntity)) return false;

        OwnerEntity that = (OwnerEntity) o;

        if (!getOwnerType().equals(that.getOwnerType())) return false;
        return getOwnerId().equals(that.getOwnerId());
    }

    @Override
    public int hashCode() {
        int result = getOwnerType().hashCode();
        result = 31 * result + getOwnerId().hashCode();
        return result;
    }
}
