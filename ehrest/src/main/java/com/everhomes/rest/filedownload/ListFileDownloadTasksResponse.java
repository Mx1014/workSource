// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>dtos: dtos {@link FileDownloadTaskDTO}</li>
 * </ul>
 */
public class ListFileDownloadTasksResponse {

    private Long nextPageAnchor;

    @ItemType(FileDownloadTaskDTO.class)
    private List<FileDownloadTaskDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<FileDownloadTaskDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FileDownloadTaskDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
