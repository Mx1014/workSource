package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.sql.Date;

public class StatisticDataDTO {

    private Long id;
    private Integer namespaceId;
    private Date dateStr;
    private Long communityId;
    private Long newCustomerNum;
    private Long registeredCustomerNum;
    private Long lossCustomerNum;
    private Long historyCustomerNum;
    private Long deleteCustomerNum;
    private Long trackingNum;
    private Long customerConut;

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

    public Date getDateStr() {
        return dateStr;
    }

    public void setDateStr(Date dateStr) {
        this.dateStr = dateStr;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getNewCustomerNum() {
        return newCustomerNum;
    }

    public void setNewCustomerNum(Long newCustomerNum) {
        this.newCustomerNum = newCustomerNum;
    }

    public Long getRegisteredCustomerNum() {
        return registeredCustomerNum;
    }

    public void setRegisteredCustomerNum(Long registeredCustomerNum) {
        this.registeredCustomerNum = registeredCustomerNum;
    }

    public Long getLossCustomerNum() {
        return lossCustomerNum;
    }

    public void setLossCustomerNum(Long lossCustomerNum) {
        this.lossCustomerNum = lossCustomerNum;
    }

    public Long getHistoryCustomerNum() {
        return historyCustomerNum;
    }

    public void setHistoryCustomerNum(Long historyCustomerNum) {
        this.historyCustomerNum = historyCustomerNum;
    }

    public Long getDeleteCustomerNum() {
        return deleteCustomerNum;
    }

    public void setDeleteCustomerNum(Long deleteCustomerNum) {
        this.deleteCustomerNum = deleteCustomerNum;
    }

    public Long getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(Long trackingNum) {
        this.trackingNum = trackingNum;
    }

    public Long getCustomerConut() {
        return customerConut;
    }

    public void setCustomerConut(Long customerConut) {
        this.customerConut = customerConut;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
