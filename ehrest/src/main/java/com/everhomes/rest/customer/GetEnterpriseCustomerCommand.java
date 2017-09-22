package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 客户id</li>
 *     <li>communityId: 客户所属园区id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/15.
 */
public class GetEnterpriseCustomerCommand {
    private Long id;

    private Long communityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
