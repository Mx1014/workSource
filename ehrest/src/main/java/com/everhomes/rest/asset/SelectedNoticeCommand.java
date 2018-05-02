//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billIdAndTypes:账单id客户类型的集合</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 * <li>organizationId:公司id,权限用</li>
 *</ul>
 */
public class SelectedNoticeCommand {
    @ItemType(Long.class)
    private List<BillIdAndType> billIdAndTypes;
    private String ownerType;
    private Long ownerId;
    private Long organizationId;
    private Long billGroupId;

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<BillIdAndType> getBillIdAndTypes() {
        return billIdAndTypes;
    }

    public void setBillIdAndTypes(List<BillIdAndType> billIdAndTypes) {
        this.billIdAndTypes = billIdAndTypes;
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
}
