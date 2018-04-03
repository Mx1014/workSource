package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>salaryCardNumber: 工资卡号</li>
 * <li>socialSecurityNumber: 公积金卡号</li>
 * <li>providentFundNumber: 社保卡号</li>
 * <li>insurances: 保险信息，参考{@link OrganizationMemberInsurancesDTO}</li>
 * </ul>
 */
public class OrganizationMemberSocialSecurityDTO {

    private String salaryCardNumber;

    private String socialSecurityNumber;

    private String providentFundNumber;

    @ItemType(OrganizationMemberInsurancesDTO.class)
    private List<OrganizationMemberInsurancesDTO> insurances;

    public OrganizationMemberSocialSecurityDTO() {
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

    public List<OrganizationMemberInsurancesDTO> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<OrganizationMemberInsurancesDTO> insurances) {
        this.insurances = insurances;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
