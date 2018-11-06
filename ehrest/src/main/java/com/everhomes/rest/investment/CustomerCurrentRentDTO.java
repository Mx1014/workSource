package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>address: 当前地址</li>
 *     <li>rentPrice: 当前租金</li>
 *     <li>rentPriceUnit: 当前租金单位</li>
 *     <li>rentArea: 当前面积</li>
 *     <li>contractIntentionDate: 当前合同结束日</li>
 *     <li>status: status</li>
 *     <li>customerId: customerId</li>
 *     <li>createTime: createTime</li>
 *     <li>createBy: createBy</li>
 *     <li>operatorTime: operatorTime</li>
 *     <li>operatorBy: operatorBy</li>
 * </ul>
 */
public class CustomerCurrentRentDTO {

    private Long id;
    private Long customerId;
    private String address;
    private BigDecimal rentPrice;
    private Byte rentPriceUnit;
    private BigDecimal rentArea;
    private Long contractIntentionDate;
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(BigDecimal rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Byte getRentPriceUnit() {
        return rentPriceUnit;
    }

    public void setRentPriceUnit(Byte rentPriceUnit) {
        this.rentPriceUnit = rentPriceUnit;
    }

    public BigDecimal getRentArea() {
        return rentArea;
    }

    public void setRentArea(BigDecimal rentArea) {
        this.rentArea = rentArea;
    }

    public Long getContractIntentionDate() {
        return contractIntentionDate;
    }

    public void setContractIntentionDate(Long contractIntentionDate) {
        this.contractIntentionDate = contractIntentionDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
