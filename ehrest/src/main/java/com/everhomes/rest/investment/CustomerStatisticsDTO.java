package com.everhomes.rest.investment;

import java.sql.Timestamp;

public class CustomerStatisticsDTO {

    private Long id;

    private Long communityId;

    private Timestamp countDate;

    private Long newCustomerCount;

    private Long entryCustomerCount;

    private Long loseCustomerCount;

    private Long historyCustomerCount;

    private Long trackingCount;

    private Long customerCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Timestamp getCountDate() {
        return countDate;
    }

    public void setCountDate(Timestamp countDate) {
        this.countDate = countDate;
    }

    public Long getNewCustomerCount() {
        return newCustomerCount;
    }

    public void setNewCustomerCount(Long newCustomerCount) {
        this.newCustomerCount = newCustomerCount;
    }

    public Long getEntryCustomerCount() {
        return entryCustomerCount;
    }

    public void setEntryCustomerCount(Long entryCustomerCount) {
        this.entryCustomerCount = entryCustomerCount;
    }

    public Long getLoseCustomerCount() {
        return loseCustomerCount;
    }

    public void setLoseCustomerCount(Long loseCustomerCount) {
        this.loseCustomerCount = loseCustomerCount;
    }

    public Long getHistoryCustomerCount() {
        return historyCustomerCount;
    }

    public void setHistoryCustomerCount(Long historyCustomerCount) {
        this.historyCustomerCount = historyCustomerCount;
    }

    public Long getTrackingCount() {
        return trackingCount;
    }

    public void setTrackingCount(Long trackingCount) {
        this.trackingCount = trackingCount;
    }

    public Long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }
}
