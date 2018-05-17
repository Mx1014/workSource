package com.everhomes.rest.decoration;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>requestId</li>
 * <li>refoundAmount</li>
 * <li>refoundComment</li>
 * </ul>
 */
public class ModifyRefoundAmountCommand {

    private Long requestId;
    private BigDecimal refoundAmount;
    private String refoundComment;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public BigDecimal getRefoundAmount() {
        return refoundAmount;
    }

    public void setRefoundAmount(BigDecimal refoundAmount) {
        this.refoundAmount = refoundAmount;
    }

    public String getRefoundComment() {
        return refoundComment;
    }

    public void setRefoundComment(String refoundComment) {
        this.refoundComment = refoundComment;
    }
}
