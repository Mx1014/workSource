//@formatter:off
package com.everhomes.rest.supplier;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>ownerType：所属者类型，通常为园区清醒，园区情形下：EhCommunities</li>
 * <li>ownerId：所属者id，园区情况下为园区id</li>
 * <li>namespaceId：域空间id</li>
 * <li>supplierName:供应商名称，非必填</li>
 * <li>contactName：联系人名称，非必填</li>
 * <li>pageAnchor：锚点，对应返回response中的nextPageAnchor，第一次请求时没有不传即可</li>
 * <li>pageSize：页大小</li>
 * <li>communityId：园区id</li>
 *</ul>
 */
public class ListSuppliersCommand {
    private String ownerType;
    private Long ownerId;
    private Integer namespaceId;
    private String supplierName;
    private String contactName;
    private Long pageAnchor;
    private Integer pageSize;
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
