package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页起始锚点</li>
 * <li>archivesEmployees: archivesEmployees {@link com.everhomes.rest.archives.ArchivesEmployeeDTO}</li>
 * <li>formOriginId: 表单Id (当Id为0时表示该公司未创建自己的表单)</li>
 * </ul>
 */
public class ListArchivesEmployeesResponse {

    private Long nextPageAnchor;

    @ItemType(ArchivesEmployeeDTO.class)
    private List<ArchivesEmployeeDTO> archivesEmployees;

    private Long formOriginId;

    public ListArchivesEmployeesResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ArchivesEmployeeDTO> getArchivesEmployees() {
        return archivesEmployees;
    }

    public void setArchivesEmployees(List<ArchivesEmployeeDTO> archivesEmployees) {
        this.archivesEmployees = archivesEmployees;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
