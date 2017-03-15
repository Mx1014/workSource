// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>status: 小区状态,参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 * </ul>
 */
public class ListCommunitesByStatusCommand {
    private Long pageAnchor;
    
    private Integer pageSize;
    
    private Byte status;
    
    public ListCommunitesByStatusCommand() {
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
