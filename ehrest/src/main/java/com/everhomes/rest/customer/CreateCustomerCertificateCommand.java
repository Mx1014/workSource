package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 所属客户id</li>
 *     <li>name: 证书名称</li>
 *     <li>certificateNumber: 证书编号</li>
 *     <li>registeDate: 获得日期 时间戳</li>
 * </ul>
 * Created by ying.xiong on 2017/8/30.
 */
public class CreateCustomerCertificateCommand {
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String name;
    private String certificateNumber;
    private Long registeDate;

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getRegisteDate() {
        return registeDate;
    }

    public void setRegisteDate(Long registeDate) {
        this.registeDate = registeDate;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
