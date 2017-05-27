package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailId: 员工编号</li>
 * <li>memberBasicDTO: 员工基本信息 参考{@link OrganizationMemberBasicDTO}</li>
 * <li>memberEducationsResponse: 员工教育信息 参考{@link com.everhomes.rest.organization.OrganizationMemberEducationsDTO}</li>
 * <li>memberWorkExperiencesResponse: 员工工作经历 参考{@link com.everhomes.rest.organization.OrganizationMemberWorkExperiencesDTO}</li>
 * <li>memberInsurancesResponse: 员工保险信息 参考{@link com.everhomes.rest.organization.OrganizationMemberInsurancesDTO}</li>
 * <li>memberContractsResponse: 员工合同信息 参考{@link com.everhomes.rest.organization.OrganizationMemberContractsDTO}</li>
 * </ul>
 */
public class PersonnelsDetailsV2Response {

    private Long detailId;

    private OrganizationMemberBasicDTO basic;

    private List<OrganizationMemberEducationsDTO> education;

    private List<OrganizationMemberWorkExperiencesDTO> workExperience;

    private List<OrganizationMemberInsurancesDTO> insurance;

    private List<OrganizationMemberContractsDTO> contract;

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

    public List<OrganizationMemberEducationsDTO> getEducation() {
        return education;
    }

    public void setEducation(List<OrganizationMemberEducationsDTO> education) {
        this.education = education;
    }

    public List<OrganizationMemberWorkExperiencesDTO> getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(List<OrganizationMemberWorkExperiencesDTO> workExperience) {
        this.workExperience = workExperience;
    }

    public List<OrganizationMemberInsurancesDTO> getInsurance() {
        return insurance;
    }

    public void setInsurance(List<OrganizationMemberInsurancesDTO> insurance) {
        this.insurance = insurance;
    }

    public List<OrganizationMemberContractsDTO> getContract() {
        return contract;
    }

    public void setContract(List<OrganizationMemberContractsDTO> contract) {
        this.contract = contract;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
