// @formatter:off
package com.everhomes.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class ListMemberInStatusCommand {
    @NotNull
    private Long groupId;
    
    @NotNull
    private Byte status;

    private Long pageAnchor;
    
    private Integer pageSize;
    
    public ListMemberInStatusCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
