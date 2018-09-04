package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>feeName: 费项名称</li>
 * <li>feePrice: 单价</li>
 * <li>amount: 数量</li>
 * </ul>
 */
public class DecorationFeeDTO {

    private String feeName;
    private String feePrice;
    private String amount;

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFeePrice() {
        return feePrice;
    }

    public void setFeePrice(String feePrice) {
        this.feePrice = feePrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
