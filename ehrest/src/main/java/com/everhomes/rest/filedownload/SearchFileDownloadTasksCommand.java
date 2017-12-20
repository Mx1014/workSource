// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>keyword: keyword</li>
 *     <li>startTime: 开始时间毫秒数</li>
 *     <li>endTime: 结束时间毫秒数</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchFileDownloadTasksCommand {

    private String keyword;

    private Long startTime;

    private Long endTime;

    private Long pageAnchor;

    private Integer pageSize;

    public SearchFileDownloadTasksCommand() {
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
