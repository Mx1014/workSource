package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailId: 员工编号</li>
 * <li>basic: 员工基本信息 参考{@link OrganizationMemberBasicDTO}</li>
 * <li>educations: 员工教育信息 参考{@link com.everhomes.rest.organization.OrganizationMemberEducationsDTO}</li>
 * <li>workExperiences: 员工工作经历 参考{@link com.everhomes.rest.organization.OrganizationMemberWorkExperiencesDTO}</li>
 * <li>insurances: 员工保险信息 参考{@link com.everhomes.rest.organization.OrganizationMemberInsurancesDTO}</li>
 * <li>contracts: 员工合同信息 参考{@link com.everhomes.rest.organization.OrganizationMemberContractsDTO}</li>
 * </ul>
 */
public class PersonnelsDetailsV2Response {

    private Long detailId;

    private OrganizationMemberBasicDTO basic;

    private ListOrganizationMemberEducationsResponse educations;

    private ListOrganizationMemberWorkExperiencesResponse workExperiences;

    private ListOrganizationMemberInsurancesResponse insurances;

    private ListOrganizationMemberContractsResponse contracts;


    public PersonnelsDetailsV2Response() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public OrganizationMemberBasicDTO getBasic() {
        return basic;
    }

    public void setBasic(OrganizationMemberBasicDTO basic) {
        this.basic = basic;
    }

    public ListOrganizationMemberEducationsResponse getEducation() {
        return educations;
    }

    public void setEducation(ListOrganizationMemberEducationsResponse educations) {
        this.educations = educations;
    }

    public ListOrganizationMemberWorkExperiencesResponse getWorkExperience() {
        return workExperiences;
    }

    public void setWorkExperience(ListOrganizationMemberWorkExperiencesResponse workExperiences) {
        this.workExperiences = workExperiences;
    }

    public ListOrganizationMemberInsurancesResponse getInsurance() {
        return insurances;
    }

    public void setInsurance(ListOrganizationMemberInsurancesResponse insurances) {
        this.insurances = insurances;
    }

    public ListOrganizationMemberContractsResponse getContract() {
        return contracts;
    }

    public void setContract(ListOrganizationMemberContractsResponse contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
