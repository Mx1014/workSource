package com.everhomes.rest.investment;

import java.sql.Date;
import java.sql.Timestamp;

public class CustomerStatisticsDTO {

    private Long id;
    private Integer namespaceId;
    private Date dateStr;
    private Long communityId;
    private String communityName;
    private Integer communityNum;
    private Integer newCustomerNum;
    private Integer registeredCustomerNum;
    private Integer lossCustomerNum;
    private Integer historyCustomerNum;
    private Integer deleteCustomerNum;
    private Integer trackingNum;
    private Integer customerCount;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Integer getCommunityNum() {
        return communityNum;
    }

    public void setCommunityNum(Integer communityNum) {
        this.communityNum = communityNum;
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

    public Integer getNewCustomerNum() {
        return newCustomerNum;
    }

    public void setNewCustomerNum(Integer newCustomerNum) {
        this.newCustomerNum = newCustomerNum;
    }

    public Integer getRegisteredCustomerNum() {
        return registeredCustomerNum;
    }

    public void setRegisteredCustomerNum(Integer registeredCustomerNum) {
        this.registeredCustomerNum = registeredCustomerNum;
    }

    public Integer getLossCustomerNum() {
        return lossCustomerNum;
    }

    public void setLossCustomerNum(Integer lossCustomerNum) {
        this.lossCustomerNum = lossCustomerNum;
    }

    public Integer getHistoryCustomerNum() {
        return historyCustomerNum;
    }

    public void setHistoryCustomerNum(Integer historyCustomerNum) {
        this.historyCustomerNum = historyCustomerNum;
    }

    public Integer getDeleteCustomerNum() {
        return deleteCustomerNum;
    }

    public void setDeleteCustomerNum(Integer deleteCustomerNum) {
        this.deleteCustomerNum = deleteCustomerNum;
    }

    public Integer getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(Integer trackingNum) {
        this.trackingNum = trackingNum;
    }

    public Integer getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }
}
