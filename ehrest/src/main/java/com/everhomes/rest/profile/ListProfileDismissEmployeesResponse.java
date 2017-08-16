package com.everhomes.rest.profile;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页起始锚点</li>
 * <li>dismissEmployees: 离职员工信息，参考{@link com.everhomes.rest.profile.ProfileDismissEmployeeDTO}</li>
 * </ul>
 */
public class ListProfileDismissEmployeesResponse {

    private Long nextPageAnchor;

    @ItemType(ProfileDismissEmployeeDTO.class)
    private List<ProfileDismissEmployeeDTO> dismissEmployees;

    public ListProfileDismissEmployeesResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ProfileDismissEmployeeDTO> getDismissEmployees() {
        return dismissEmployees;
    }

    public void setDismissEmployees(List<ProfileDismissEmployeeDTO> dismissEmployees) {
        this.dismissEmployees = dismissEmployees;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
