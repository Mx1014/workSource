package com.everhomes.enterprise;

import javax.validation.constraints.NotNull;

/**
 * <ul> 查询园区的企业
 * <li>communityId: 小区ID</li>
 * <li>status: 查询某个状态的企业</li>
 * </ul>
 * @author janson
 *
 */
public class ListEnterpriseByCommunityIdCommand {
    @NotNull
    Long communityId;
    
    Long status;
    
    private Long pageAnchor;
    private Integer pageSize;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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
}
