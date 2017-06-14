package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/6/14.
 */
public class SampleCommunitySpecification {
    private Long ownerId;

    private String ownerType;

    private Integer namespaceId;

    private Long sampleId;

    private Long communityId;

    private Long specificationId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

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
