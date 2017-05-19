package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Ryan on 2017/5/19.
 * <ul>
 * <li>workExps: 对应的工作经历，参考{@link com.everhomes.rest.organization.OrganizationMemberWorkExpsDTO}</li>
 * </ul>
 */
public class ListOrganizationMemberWorkExpsResponse {

    @ItemType(OrganizationMemberWorkExpsDTO.class)
    private List<OrganizationMemberWorkExpsDTO> workExps;

    public ListOrganizationMemberWorkExpsResponse() {
    }

    public List<OrganizationMemberWorkExpsDTO> getWorkExps() {
        return workExps;
    }

    public void setWorkExps(List<OrganizationMemberWorkExpsDTO> workExps) {
        this.workExps = workExps;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
