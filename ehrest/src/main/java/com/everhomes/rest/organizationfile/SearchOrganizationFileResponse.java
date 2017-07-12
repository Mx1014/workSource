// @formatter:off
package com.everhomes.rest.organizationfile;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下页锚点</li>
 *     <li>list: 文件列表 {@link com.everhomes.rest.organizationfile.OrganizationFileDTO}</li>
 * </ul>
 */
public class SearchOrganizationFileResponse {

    private Long nextPageAnchor;
    @ItemType(OrganizationFileDTO.class)
    private List<OrganizationFileDTO> list = new ArrayList<>();

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<OrganizationFileDTO> getList() {
        return list;
    }

    public void setList(List<OrganizationFileDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
