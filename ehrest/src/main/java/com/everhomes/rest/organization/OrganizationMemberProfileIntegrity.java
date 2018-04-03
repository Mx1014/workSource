package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>basicIntegrity: 基本信息完整度</li>
 * <li>backEndIntegrity: 背景信息完整度</li>
 * <li>socialSecurityIntegrity: 社保信息完整度</li>
 * <li>contractIntegrity: 合同信息完整度</li>
 * <li>profileIntegrity: 档案完整度</li>
 * </ul>
 */
public class OrganizationMemberProfileIntegrity {

    private Integer basicIntegrity;

    private Integer backEndIntegrity;

    private Integer socialSecurityIntegrity;

    private Integer contractIntegrity;

    private Integer profileIntegrity;

    public OrganizationMemberProfileIntegrity() {
    }

    public OrganizationMemberProfileIntegrity(Integer basicIntegrity, Integer backEndIntegrity, Integer socialSecurityIntegrity, Integer contractIntegrity) {
        this.basicIntegrity = basicIntegrity;
        this.backEndIntegrity = backEndIntegrity;
        this.socialSecurityIntegrity = socialSecurityIntegrity;
        this.contractIntegrity = contractIntegrity;
    }

    public Integer getBasicIntegrity() {
        return basicIntegrity;
    }

    public void setBasicIntegrity(Integer basicIntegrity) {
        this.basicIntegrity = basicIntegrity;
    }

    public Integer getBackEndIntegrity() {
        return backEndIntegrity;
    }

    public void setBackEndIntegrity(Integer backEndIntegrity) {
        this.backEndIntegrity = backEndIntegrity;
    }

    public Integer getSocialSecurityIntegrity() {
        return socialSecurityIntegrity;
    }

    public void setSocialSecurityIntegrity(Integer socialSecurityIntegrity) {
        this.socialSecurityIntegrity = socialSecurityIntegrity;
    }

    public Integer getContractIntegrity() {
        return contractIntegrity;
    }

    public void setContractIntegrity(Integer contractIntegrity) {
        this.contractIntegrity = contractIntegrity;
    }

    public Integer getProfileIntegrity() {
        return profileIntegrity;
    }

    public void setProfileIntegrity(Integer profileIntegrity) {
        this.profileIntegrity = profileIntegrity;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
