package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 所属客户id</li>
 *     <li>name: 商标名称</li>
 *     <li>registeDate: 注册日期 时间戳</li>
 *     <li>trademarkTypeItemId: 商标类型</li>
 *     <li>trademarkAmount: 商标数量</li>
 * </ul>
 * Created by ying.xiong on 2017/8/19.
 */
public class CreateCustomerTrademarkCommand {
    private Long customerId;
    private String name;
    private Long registeDate;
    private Long trademarkTypeItemId;
    private Integer trademarkAmount;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRegisteDate() {
        return registeDate;
    }

    public void setRegisteDate(Long registeDate) {
        this.registeDate = registeDate;
    }

    public Integer getTrademarkAmount() {
        return trademarkAmount;
    }

    public void setTrademarkAmount(Integer trademarkAmount) {
        this.trademarkAmount = trademarkAmount;
    }

    public Long getTrademarkTypeItemId() {
        return trademarkTypeItemId;
    }

    public void setTrademarkTypeItemId(Long trademarkTypeItemId) {
        this.trademarkTypeItemId = trademarkTypeItemId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
