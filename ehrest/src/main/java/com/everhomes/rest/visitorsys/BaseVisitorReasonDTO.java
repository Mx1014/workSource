// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>visitorReasonId: (必填)事由idID</li>
 * <li>visitorReason: (必填)事由</li>
 * </ul>
 */
public class BaseVisitorReasonDTO {
    private Long visitorReasonId;
    private String visitorReason;

    public Long getVisitorReasonId() {
        return visitorReasonId;
    }

    public void setVisitorReasonId(Long visitorReasonId) {
        this.visitorReasonId = visitorReasonId;
    }

    public String getVisitorReason() {
        return visitorReason;
    }

    public void setVisitorReason(String visitorReason) {
        this.visitorReason = visitorReason;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
