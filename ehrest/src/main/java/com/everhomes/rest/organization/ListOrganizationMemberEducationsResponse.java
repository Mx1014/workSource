package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Ryan on 2017/5/19.
 * <ul>
 * <li>educations: 对应的教育信息，参考{@link com.everhomes.rest.organization.OrganizationMemberEducationsDTO}</li>
 * </ul>
 */
public class ListOrganizationMemberEducationsResponse {

    @ItemType(OrganizationMemberEducationsDTO.class)
    private List<OrganizationMemberEducationsDTO> educations;

    public ListOrganizationMemberEducationsResponse() {
    }

    public List<OrganizationMemberEducationsDTO> getEducations() {
        return educations;
    }

    public void setEducations(List<OrganizationMemberEducationsDTO> educations) {
        this.educations = educations;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
