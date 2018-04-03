//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/10.
 */

/**
 *<ul>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者id</li>
 * <li>namespaceId:域空间</li>
 *</ul>
 */
public class ListAutoNoticeConfigCommand {
    private String ownerType;
    private Long ownerId;
    private Integer namespaceId;

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
