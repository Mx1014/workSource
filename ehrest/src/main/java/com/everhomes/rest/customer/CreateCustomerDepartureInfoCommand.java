package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 *     <li>reviewTime：评审时间</li>
 *     <li>hatchMonths：孵化时间(X月)</li>
 *     <li>departureNatureId：离场性质</li>
 *     <li>departureDirectionId：离场去向</li>
 *     <li>remark：备注</li>
 * </ul>
 * Created by ying.xiong on 2017/12/6.
 */
public class CreateCustomerDepartureInfoCommand {
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private Long reviewTime;
    private Integer hatchMonths;
    private Long departureNatureId;
    private Long departureDirectionId;
    private String remark;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Long getDepartureDirectionId() {
        return departureDirectionId;
    }

    public void setDepartureDirectionId(Long departureDirectionId) {
        this.departureDirectionId = departureDirectionId;
    }

    public Long getDepartureNatureId() {
        return departureNatureId;
    }

    public void setDepartureNatureId(Long departureNatureId) {
        this.departureNatureId = departureNatureId;
    }

    public Integer getHatchMonths() {
        return hatchMonths;
    }

    public void setHatchMonths(Integer hatchMonths) {
        this.hatchMonths = hatchMonths;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Long reviewTime) {
        this.reviewTime = reviewTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
