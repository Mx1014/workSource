package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

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
    private Integer namespaceId;
    
    Integer status;
    
    private Long pageAnchor;
    private Integer pageSize;
    private String keyword;
    private String enterpriseName;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
