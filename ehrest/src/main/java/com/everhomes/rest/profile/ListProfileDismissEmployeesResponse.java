package com.everhomes.rest.profile;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset: 下一页页码</li>
 * <li>dismissEmployees: 离职员工信息，参考{@link com.everhomes.rest.profile.ProfileDismissEmployeeDTO}</li>
 * </ul>
 */
public class ListProfileDismissEmployeesResponse {

    private Integer nextPageOffset;

    @ItemType(ProfileDismissEmployeeDTO.class)
    private List<ProfileDismissEmployeeDTO> dismissEmployees;

    public ListProfileDismissEmployeesResponse() {
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
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
