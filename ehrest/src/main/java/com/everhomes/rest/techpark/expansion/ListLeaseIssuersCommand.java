package com.everhomes.rest.techpark.expansion;

/**
 * Created by Administrator on 2017/3/15.
 */
public class ListLeaseIssuersCommand {
    private Long communityId;
    private String keyword;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
