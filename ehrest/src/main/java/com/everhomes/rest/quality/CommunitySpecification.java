package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/6/26.
 */
public class CommunitySpecification {

    private Long communityId;

    private Long specificationId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(Long specificationId) {
        this.specificationId = specificationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
