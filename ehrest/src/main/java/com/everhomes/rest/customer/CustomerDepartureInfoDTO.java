package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 *     <li>reviewTime：评审时间</li>
 *     <li>hatchMonths：孵化时间(X月)</li>
 *     <li>departureNatureId：离场性质id</li>
 *     <li>departureNatureName：离场性质名称</li>
 *     <li>departureDirectionId：离场去向id</li>
 *     <li>departureDirectionName：离场去向名称</li>
 *     <li>remark：备注</li>
 * </ul>
 * Created by ying.xiong on 2017/12/6.
 */
public class CustomerDepartureInfoDTO {
    private Long id;
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private Timestamp reviewTime;
    private Integer hatchMonths;
    private Long departureNatureId;
    private String departureNatureName;
    private Long departureDirectionId;
    private String departureDirectionName;
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

    public String getDepartureDirectionName() {
        return departureDirectionName;
    }

    public void setDepartureDirectionName(String departureDirectionName) {
        this.departureDirectionName = departureDirectionName;
    }

    public Long getDepartureNatureId() {
        return departureNatureId;
    }

    public void setDepartureNatureId(Long departureNatureId) {
        this.departureNatureId = departureNatureId;
    }

    public String getDepartureNatureName() {
        return departureNatureName;
    }

    public void setDepartureNatureName(String departureNatureName) {
        this.departureNatureName = departureNatureName;
    }

    public Integer getHatchMonths() {
        return hatchMonths;
    }

    public void setHatchMonths(Integer hatchMonths) {
        this.hatchMonths = hatchMonths;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Timestamp getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Timestamp reviewTime) {
        this.reviewTime = reviewTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
