// @formatter:off
package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>activityId:活动Id</li>
 *<li>anchor:锚点</li>
 *<li>pageSize:每页的个数<li>
 *<li>uuid:活动uuid</li>
 *</ul>
 */
public class ActivityListCommand {
    @NotNull
    private Long activityId;
    
    //activity unique id
    private String uuid;

    private Long anchor;
    
    private Integer pageSize;
    
    public ActivityListCommand() {
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    
    
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }
    

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
