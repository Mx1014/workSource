// @formatter:off
package com.everhomes.rest.user.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>anchor: 下一页锚点</li>
 *     <li>pageSize: 每页数量</li>
 *     <li>status: 状态 {@link com.everhomes.rest.user.admin.UserAppealLogStatus}</li>
 * </ul>
 */
public class ListUserAppealLogsCommand {

    private Long anchor;
    private Integer pageSize;
    private Byte status;

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
