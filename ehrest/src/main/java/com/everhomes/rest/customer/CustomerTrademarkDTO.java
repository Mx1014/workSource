package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 所属客户id</li>
 *     <li>name: 商标名称</li>
 *     <li>registeDate: 注册日期 时间戳</li>
 *     <li>trademarkTypeItemId: 商标类型</li>
 *     <li>trademarkTypeItemName: 名称</li>
 *     <li>trademarkAmount: 商标数量</li>
 * </ul>
 * Created by ying.xiong on 2017/8/19.
 */
public class CustomerTrademarkDTO {
    private Long id;
    private Long customerId;
    private String name;
    private Timestamp registeDate;
    private Long trademarkTypeItemId;
    private String trademarkTypeItemName;
    private Integer trademarkAmount;
    private Integer applications;
    private Integer patentApplications;
    private Integer authorizations;
    private Integer patentAuthorizations;

    public Integer getApplications() {
        return applications;
    }

    public void setApplications(Integer applications) {
        this.applications = applications;
    }

    public Integer getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Integer authorizations) {
        this.authorizations = authorizations;
    }

    public Integer getPatentApplications() {
        return patentApplications;
    }

    public void setPatentApplications(Integer patentApplications) {
        this.patentApplications = patentApplications;
    }

    public Integer getPatentAuthorizations() {
        return patentAuthorizations;
    }

    public void setPatentAuthorizations(Integer patentAuthorizations) {
        this.patentAuthorizations = patentAuthorizations;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getRegisteDate() {
        return registeDate;
    }

    public void setRegisteDate(Timestamp registeDate) {
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

    public String getTrademarkTypeItemName() {
        return trademarkTypeItemName;
    }

    public void setTrademarkTypeItemName(String trademarkTypeItemName) {
        this.trademarkTypeItemName = trademarkTypeItemName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
