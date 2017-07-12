// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 *<ul>
 *<li>activity:活动   参考{@link com.everhomes.rest.activity.ActivityDTO}</li>
 *<li>creatorFlag:创建人的标识</li>
 *<li>checkinQRUrl:二维码路径</li>
 *<li>roster:  参考{@link com.everhomes.rest.activity.ActivityMemberDTO}</li>
 *<li>nextAnchor: </li>
 *</ul>
 */
public class ActivityListResponse {
    private ActivityDTO activity;
    private Integer creatorFlag;
    private String checkinQRUrl;
    
    @ItemType(ActivityMemberDTO.class)
    private List<ActivityMemberDTO> roster;
    
    private Long nextAnchor;
    
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


    public String getCheckinQRUrl() {
        return checkinQRUrl;
    }

    public void setCheckinQRUrl(String checkinQRUrl) {
        this.checkinQRUrl = checkinQRUrl;
    }

    public List<ActivityMemberDTO> getRoster() {
        return roster;
    }

    public void setRoster(List<ActivityMemberDTO> roster) {
        this.roster = roster;
    }
    

    public Long getNextAnchor() {
        return nextAnchor;
    }

    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
