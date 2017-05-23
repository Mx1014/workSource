package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>workExps: 对应的工作经历，参考{@link OrganizationMemberWorkExperiencesDTO}</li>
 * </ul>
 */
public class ListOrganizationMemberWorkExperiencesResponse {

    @ItemType(OrganizationMemberWorkExperiencesDTO.class)
    private List<OrganizationMemberWorkExperiencesDTO> workExps;

    public ListOrganizationMemberWorkExperiencesResponse() {
    }

    public List<OrganizationMemberWorkExperiencesDTO> getWorkExps() {
        return workExps;
    }

    public void setWorkExps(List<OrganizationMemberWorkExperiencesDTO> workExps) {
        this.workExps = workExps;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
