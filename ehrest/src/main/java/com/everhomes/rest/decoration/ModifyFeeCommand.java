package com.everhomes.rest.decoration;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>requestId</li>
 * <li>decorationFee :List<DecorationFeeDTO> 参考{@link com.everhomes.rest.decoration.IllustrationType}</li>
 * <li>totalAmount</li>
 * </ul>
 */
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

    public List<DecorationFeeDTO> getDecorationFee() {
        return decorationFee;
    }

    public void setDecorationFee(List<DecorationFeeDTO> decorationFee) {
        this.decorationFee = decorationFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
