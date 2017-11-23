// @formatter:off
package com.everhomes.rest.community.admin;

/**
 * <ul>
 *     <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 *     <li>pageSize: 每页的数量</li>
 *     <li>memberStatus: 参考{@link com.everhomes.rest.group.GroupMemberStatus}</li>
 *     <li>communityId: 小区id</li>
 *     <li>userInfoKeyword: 用户昵称、手机号</li>
 *     <li>communityKeyword: 小区名称</li>
 * </ul>
 */
public class CommunityAuthUserAddressCommand {

    private Long pageAnchor;
    private Integer pageSize;
    private Byte memberStatus;
    private Long communityId;

    private String userInfoKeyword;
    private String communityKeyword;

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
