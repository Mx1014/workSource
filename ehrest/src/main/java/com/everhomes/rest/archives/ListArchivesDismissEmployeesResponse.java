package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset: 下一页页码</li>
 * <li>dismissEmployees: 离职员工信息，参考{@link ArchivesDismissEmployeeDTO}</li>
 * </ul>
 */
public class ListArchivesDismissEmployeesResponse {

    private Integer nextPageAnchor;

    @ItemType(ArchivesDismissEmployeeDTO.class)
    private List<ArchivesDismissEmployeeDTO> dismissEmployees;

    public ListArchivesDismissEmployeesResponse() {
    }

    public Integer getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Integer nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ArchivesDismissEmployeeDTO> getDismissEmployees() {
        return dismissEmployees;
    }

    public void setDismissEmployees(List<ArchivesDismissEmployeeDTO> dismissEmployees) {
        this.dismissEmployees = dismissEmployees;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
