package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>memberId: 员工编号</li>
 * <li>memberBasicDTO: 员工基本信息 参考{@link com.everhomes.rest.organization.OrganizationMemberBasicDTO}</li>
 * <li>memberEducationsResponse: 员工教育信息 参考{@link com.everhomes.rest.organization.OrganizationMemberEducationsDTO}</li>
 * <li>memberWorkExperiencesResponse: 员工工作经历 参考{@link com.everhomes.rest.organization.OrganizationMemberWorkExperiencesDTO}</li>
 * <li>memberInsurancesResponse: 员工保险信息 参考{@link com.everhomes.rest.organization.OrganizationMemberInsurancesDTO}</li>
 * <li>memberContractsResponse: 员工合同信息 参考{@link com.everhomes.rest.organization.OrganizationMemberContractsDTO}</li>
 * </ul>
 */
public class PersonnelsDetailsV2Response {

    private Long memberId;

    private OrganizationMemberBasicDTO memberBasicDTO;

    private ListOrganizationMemberEducationsResponse memberEducationsResponse;

    private ListOrganizationMemberWorkExperiencesResponse memberWorkExperiencesResponse;

    private ListOrganizationMemberInsurancesResponse memberInsurancesResponse;

    private ListOrganizationMemberContractsResponse memberContractsResponse;

    public PersonnelsDetailsV2Response() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public OrganizationMemberBasicDTO getMemberBasicDTO() {
        return memberBasicDTO;
    }

    public void setMemberBasicDTO(OrganizationMemberBasicDTO memberBasicDTO) {
        this.memberBasicDTO = memberBasicDTO;
    }

    public ListOrganizationMemberEducationsResponse getMemberEducationsResponse() {
        return memberEducationsResponse;
    }

    public void setMemberEducationsResponse(ListOrganizationMemberEducationsResponse memberEducationsResponse) {
        this.memberEducationsResponse = memberEducationsResponse;
    }

    public ListOrganizationMemberWorkExperiencesResponse getMemberWorkExperiencesResponse() {
        return memberWorkExperiencesResponse;
    }

    public void setMemberWorkExperiencesResponse(ListOrganizationMemberWorkExperiencesResponse memberWorkExperiencesResponse) {
        this.memberWorkExperiencesResponse = memberWorkExperiencesResponse;
    }

    public ListOrganizationMemberInsurancesResponse getMemberInsurancesResponse() {
        return memberInsurancesResponse;
    }

    public void setMemberInsurancesResponse(ListOrganizationMemberInsurancesResponse memberInsurancesResponse) {
        this.memberInsurancesResponse = memberInsurancesResponse;
    }

    public ListOrganizationMemberContractsResponse getMemberContractsResponse() {
        return memberContractsResponse;
    }

    public void setMemberContractsResponse(ListOrganizationMemberContractsResponse memberContractsResponse) {
        this.memberContractsResponse = memberContractsResponse;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
