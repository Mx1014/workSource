// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>dtos: dtos {@link com.everhomes.rest.filedownload.FileDownloadJobDTO}</li>
 * </ul>
 */
public class ListFileDownloadJobsResponse {

    private Long nextPageAnchor;

    @ItemType(FileDownloadJobDTO.class)
    private List<FileDownloadJobDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<FileDownloadJobDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FileDownloadJobDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
