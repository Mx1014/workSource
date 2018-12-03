package com.everhomes.rest.customer.openapi;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/7/13 17 :15
 */

public class EnterpriseDTO {
    private Long id;

    private Long communityId;

    private String communityName;

    private String name;

    private String trackingName;

    private String corpNatureItemName;

    private String hotline;

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

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrackingName() {
        return trackingName;
    }

    public void setTrackingName(String trackingName) {
        this.trackingName = trackingName;
    }

    public String getCorpNatureItemName() {
        return corpNatureItemName;
    }

    public void setCorpNatureItemName(String corpNatureItemName) {
        this.corpNatureItemName = corpNatureItemName;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
