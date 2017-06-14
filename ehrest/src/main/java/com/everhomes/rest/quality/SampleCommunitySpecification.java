package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/6/14.
 */
public class SampleCommunitySpecification {
    private Long sampleId;
    private Long communityId;
    private Long specificationId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
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
