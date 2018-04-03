package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
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

    private OrganizationMemberBackGroundDTO backGround;

    private OrganizationMemberSocialSecurityDTO socialSecurity;

    @ItemType(OrganizationMemberContractsDTO.class)
    private List<OrganizationMemberContractsDTO> contracts;

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

    public OrganizationMemberBackGroundDTO getBackGround() {
        return backGround;
    }

    public void setBackGround(OrganizationMemberBackGroundDTO backGround) {
        this.backGround = backGround;
    }

    public OrganizationMemberSocialSecurityDTO getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(OrganizationMemberSocialSecurityDTO socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public List<OrganizationMemberContractsDTO> getContracts() {
        return contracts;
    }

    public void setContracts(List<OrganizationMemberContractsDTO> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
