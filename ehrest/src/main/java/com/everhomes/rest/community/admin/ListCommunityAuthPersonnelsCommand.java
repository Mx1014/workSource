// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 *     <li>isSignedup: 是否左邻注册用户</li>
 *     <li>status: 状态:1-待认证 3-已同意 4-已拒绝</li>
 *     <li>pageOffset: pageOffset</li>
 *     <li>pageAnchor: 页码</li>
 *     <li>pageSize: 每页大小</li>
 *     <li>userInfoKeyword: 昵称姓名关键字</li>
 *     <li>identifierToken: 手机号</li>
 *     <li>orgNameKeyword: 公司名称关键字</li>
 *     <li>currentOrgId: 当前用户的组织ID</li>
 *     <li>appId: appID</li>
 *     <li>auditAuth: 审核权限,请参考{@link com.everhomes.rest.organization.AuditAuth}</li>
 * </ul>
 */
public class ListCommunityAuthPersonnelsCommand {
    @NotNull
    private Long communityId;
    private Byte isSignedup;
    private Byte status;
    private Integer pageOffset;
    private Long pageAnchor;
    private Integer pageSize;

    private String userInfoKeyword;
    private String identifierToken;
    private String orgNameKeyword;

    private Long currentOrgId;
    private Long appId;

    private Byte auditAuth;

    public Byte getAuditAuth() {
        return auditAuth;
    }

    public void setAuditAuth(Byte auditAuth) {
        this.auditAuth = auditAuth;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(Long currentOrgId) {
        this.currentOrgId = currentOrgId;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public Byte getIsSignedup() {
        return isSignedup;
    }

    public void setIsSignedup(Byte isSignedup) {
        this.isSignedup = isSignedup;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public String getUserInfoKeyword() {
        return userInfoKeyword;
    }

    public void setUserInfoKeyword(String userInfoKeyword) {
        this.userInfoKeyword = userInfoKeyword;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public String getOrgNameKeyword() {
        return orgNameKeyword;
    }

    public void setOrgNameKeyword(String orgNameKeyword) {
        this.orgNameKeyword = orgNameKeyword;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
