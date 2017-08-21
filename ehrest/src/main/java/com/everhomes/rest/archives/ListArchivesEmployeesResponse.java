package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页起始锚点</li>
 * <li>profilePersonnels: 成员信息，参考{@link ArchivesEmployeeDTO}</li>
 * </ul>
 */
public class ListArchivesEmployeesResponse {

    private Long nextPageAnchor;

    @ItemType(ArchivesEmployeeDTO.class)
    private List<ArchivesEmployeeDTO> profilePersonnels;

    public ListArchivesEmployeesResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ArchivesEmployeeDTO> getProfilePersonnels() {
        return profilePersonnels;
    }

    public void setProfilePersonnels(List<ArchivesEmployeeDTO> profilePersonnels) {
        this.profilePersonnels = profilePersonnels;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
