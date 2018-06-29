// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>visitReasonId: (必填)事由idID</li>
 * <li>visitReason: (必填)事由</li>
 * </ul>
 */
public class BaseVisitorReasonDTO {
    private Long visitReasonId;
    private String visitReason;

    public Long getVisitReasonId() {
        return visitReasonId;
    }

    public void setVisitReasonId(Long visitReasonId) {
        this.visitReasonId = visitReasonId;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
