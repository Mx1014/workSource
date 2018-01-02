package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * <li>id：订单id</li>
 * <li>amount：修改后的价格</li>
 * </ul>
 */
public class ChangeRentalBillPayInfoCommand {
    private Long id;
    private Long amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
