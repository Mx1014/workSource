package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId：所属客户id</li>
 *     <li>area：区域</li>
 *     <li>address：地址</li>
 *     <li>addressName：楼栋门牌名</li>
 *     <li>addressId：楼栋门牌id</li>
 *     <li>areaSize：面积</li>
 *     <li>contractStartDate：合同开始日期</li>
 *     <li>contractEndDate：合同结束日期</li>
 *     <li>contractEndMonth：合同结束月份</li>
 *     <li>remark：备注/企业评级</li>
 * </ul>
 * Created by ying.xiong on 2017/12/6.
 */
public class CustomerEntryInfoDTO {

    private Long id;
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String area;
    private String address;
    private String addressName;
    private Long addressId;
    private BigDecimal areaSize;
    private Timestamp contractStartDate;
    private Timestamp contractEndDate;
    private Integer contractEndMonth;
    private String remark;

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public BigDecimal getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(BigDecimal areaSize) {
        this.areaSize = areaSize;
    }

    public Timestamp getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Timestamp contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Timestamp getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Timestamp contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Integer getContractEndMonth() {
        return contractEndMonth;
    }

    public void setContractEndMonth(Integer contractEndMonth) {
        this.contractEndMonth = contractEndMonth;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
