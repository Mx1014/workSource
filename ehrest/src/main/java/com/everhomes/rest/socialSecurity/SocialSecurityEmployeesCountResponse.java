package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

public class SocialSecurityEmployeesCountResponse {

    private Integer socialSecurity;

    private Integer accumulationFund;

    private Integer growth;

    private Integer reduce;

    private Integer checkIn;

    private Integer dismiss;

    public SocialSecurityEmployeesCountResponse() {
    }

    public Integer getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(Integer socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public Integer getAccumulationFund() {
        return accumulationFund;
    }

    public void setAccumulationFund(Integer accumulationFund) {
        this.accumulationFund = accumulationFund;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Integer getReduce() {
        return reduce;
    }

    public void setReduce(Integer reduce) {
        this.reduce = reduce;
    }

    public Integer getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Integer checkIn) {
        this.checkIn = checkIn;
    }

    public Integer getDismiss() {
        return dismiss;
    }

    public void setDismiss(Integer dismiss) {
        this.dismiss = dismiss;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
