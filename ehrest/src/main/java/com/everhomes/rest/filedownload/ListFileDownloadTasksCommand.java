// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 *     <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListFileDownloadTasksCommand {

    private String keyword;

    private Long startTime;

    private Long endTime;

    private Long pageAnchor;

    private Integer pageSize;

    public ListFileDownloadTasksCommand() {
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
