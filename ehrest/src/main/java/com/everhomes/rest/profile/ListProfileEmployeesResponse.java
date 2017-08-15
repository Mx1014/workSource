package com.everhomes.rest.profile;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页起始锚点</li>
 * <li>profilePersonnels: 成员信息，参考{@link ProfileEmployeeDTO}</li>
 * </ul>
 */
public class ListProfileEmployeesResponse {

    private Long nextPageAnchor;

    @ItemType(ProfileEmployeeDTO.class)
    private List<ProfileEmployeeDTO> profilePersonnels;

    public ListProfileEmployeesResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ProfileEmployeeDTO> getProfilePersonnels() {
        return profilePersonnels;
    }

    public void setProfilePersonnels(List<ProfileEmployeeDTO> profilePersonnels) {
        this.profilePersonnels = profilePersonnels;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
