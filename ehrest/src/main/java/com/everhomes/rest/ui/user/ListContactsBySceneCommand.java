package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>isSignedup: 是否左邻注册用户 0-所有 1-注册用户</li>
 * <li>organizationId: 机构ID</li>
 * <li>isAdmin: 是否为管理员: 0-否 1-是</li>
 * </ul>
 */
public class ListContactsBySceneCommand {
    private String sceneToken;

    private Byte isSignedup;

    private Long organizationId;

    private Byte isAdmin;

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public Byte getIsSignedup() {
        return isSignedup;
    }

    public void setIsSignedup(Byte isSignedup) {
        this.isSignedup = isSignedup;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
