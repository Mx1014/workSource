package com.everhomes.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ActivityListResponse {
    private ActivityDTO activity;
    private Integer creatorFlag;
    private String signupQRUrl;
    
    @ItemType(ActivityMemberDTO.class)
    private List<ActivityMemberDTO> roster;
    
    public ActivityListResponse() {
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    public Integer getCreatorFlag() {
        return creatorFlag;
    }

    public void setCreatorFlag(Integer creatorFlag) {
        this.creatorFlag = creatorFlag;
    }

    public String getSignupQRUrl() {
        return signupQRUrl;
    }

    public void setSignupQRUrl(String signupQRUrl) {
        this.signupQRUrl = signupQRUrl;
    }

    public List<ActivityMemberDTO> getRoster() {
        return roster;
    }

    public void setRoster(List<ActivityMemberDTO> roster) {
        this.roster = roster;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
