package com.everhomes.rest.decoration;

import java.math.BigDecimal;
import java.util.List;

public class ModifyFeeCommand {

    private Long requestId;
    private List<DecorationFeeDTO> decorationFee;
    private BigDecimal totalAmount;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
