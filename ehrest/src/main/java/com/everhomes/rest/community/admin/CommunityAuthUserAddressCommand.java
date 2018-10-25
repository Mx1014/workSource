// @formatter:off
package com.everhomes.rest.community.admin;

/**
 * <ul>
 *     <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 *     <li>pageSize: 每页的数量</li>
 *     <li>memberStatus: 参考{@link com.everhomes.rest.group.GroupMemberStatus}</li>
 *     <li>communityId: 小区id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>userInfoKeyword: 用户昵称</li>
 *     <li>identifierToken: 手机号</li>
 *     <li>communityKeyword: 小区名称</li>
 *     <li>currentOrgId: 当前用户的组织ID</li>
 *     <li>appId: appId</li>
 * </ul>
 */
public class CommunityAuthUserAddressCommand {

    private Long pageAnchor;
    private Integer pageSize;
    private Integer namespaceId;
    private Byte memberStatus;
    private Long communityId;

    private String userInfoKeyword;
    private String identifierToken;
    private String communityKeyword;
    private Long appId;
    private Long currentOrgId;

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

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getMemberStatus() {
        return memberStatus;
    }

    public String getUserInfoKeyword() {
        return userInfoKeyword;
    }

    public void setUserInfoKeyword(String userInfoKeyword) {
        this.userInfoKeyword = userInfoKeyword;
    }

    public String getCommunityKeyword() {
        return communityKeyword;
    }

    public void setCommunityKeyword(String communityKeyword) {
        this.communityKeyword = communityKeyword;
    }

    public void setMemberStatus(Byte memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}
