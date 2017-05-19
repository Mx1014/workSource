package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by Ryan on 2017/5/19.
 * <ul>
 *     <li>memberId: 员工编号</li>
 *     <li>salaryCardNumber: 工资卡号</li>
 *     <li>socialSecurityNumber: 公积金卡号</li>
 *     <li>providentFundNumber: 社保卡号</li>
 * </ul>
 */
public class OrganizationMemberNumbersDTO {

    private Long memberId;

    private String salaryCardNumber;

    private String socialSecurityNumber;

    private String providentFundNumber;

    public OrganizationMemberNumbersDTO() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getSalaryCardNumber() {
        return salaryCardNumber;
    }

    public void setSalaryCardNumber(String salaryCardNumber) {
        this.salaryCardNumber = salaryCardNumber;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getProvidentFundNumber() {
        return providentFundNumber;
    }

    public void setProvidentFundNumber(String providentFundNumber) {
        this.providentFundNumber = providentFundNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
