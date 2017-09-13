//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.util.Date;

/**
 *<ul>
 * <li>dimension:统计维度,1:账期;2:收费项目;3:项目</li>
 * <li>beginLimit:账期起始限制</li>
 * <li>endLimit:账期截止限制</li>
 * <li>ownerType:所属者type</li>
 * <li>ownerId:所属者id</li>
 *</ul>
 */
public class BillStaticsCommand {
    private Byte dimension;
    private String beginLimit;
    private String endLimit;
    private String ownerType;
    private Long ownerId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Byte getDimension() {
        return dimension;
    }

    public void setDimension(Byte dimension) {
        this.dimension = dimension;
    }

    public String getBeginLimit() {
        return beginLimit;
    }

    public void setBeginLimit(String beginLimit) {
        this.beginLimit = beginLimit;
    }

    public String getEndLimit() {
        return endLimit;
    }

    public void setEndLimit(String endLimit) {
        this.endLimit = endLimit;
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

    public BillStaticsCommand() {

    }
}
