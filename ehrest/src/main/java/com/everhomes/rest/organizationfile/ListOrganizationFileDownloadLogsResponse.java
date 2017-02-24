// @formatter:off
package com.everhomes.rest.organizationfile;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下页锚点</li>
 *     <li>list: 下载记录列表{@link com.everhomes.rest.organizationfile.OrganizationFileDownloadLogsDTO}</li>
 * </ul>
 */
public class ListOrganizationFileDownloadLogsResponse {

    private Long nextPageAnchor;
    @ItemType(OrganizationFileDownloadLogsDTO.class)
    private List<OrganizationFileDownloadLogsDTO> list = new ArrayList<>();

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<OrganizationFileDownloadLogsDTO> getList() {
        return list;
    }

    public void setList(List<OrganizationFileDownloadLogsDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
