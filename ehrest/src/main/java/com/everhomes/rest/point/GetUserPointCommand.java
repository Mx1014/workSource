package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>uid:用户ID</li>
 *<li>anchor:页码</li>
 */
public class GetUserPointCommand {
    private Long uid;
    private Long anchor;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
