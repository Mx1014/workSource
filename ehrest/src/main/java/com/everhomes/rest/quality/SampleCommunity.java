package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>sampleId: 例行检查id</li>
 *     <li>communityId: 小区id</li>
 *     <li>communityName: 小区名</li>
 * </ul>
 * Created by ying.xiong on 2017/6/8.
 */
public class SampleCommunity {

    private Long sampleId;

    private Long communityId;

    private String communityName;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
