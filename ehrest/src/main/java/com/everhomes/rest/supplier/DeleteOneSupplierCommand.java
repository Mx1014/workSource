//@formatter:off
package com.everhomes.rest.supplier;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>id:供应商的标识id</li>
 * <li>communityId:园区id</li>
 *</ul>
 */
public class DeleteOneSupplierCommand {
    private Long id;
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
