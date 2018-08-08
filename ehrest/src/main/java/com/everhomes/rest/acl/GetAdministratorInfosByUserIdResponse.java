package com.everhomes.rest.acl;

/**
 * <ul>
 * <li>isTopAdminFlag：是否是超级管理员</li>
 * <li>isSystemAdminFlag：是否是系统管理员</li>
 * <li>topAdminName: 超级管理员名字</li>
 * <li>topAdminToken: 超级管理员手机号</li>
 * </ul>
 */
public class GetAdministratorInfosByUserIdResponse {
    private Byte isTopAdminFlag;
    private Byte isSystemAdminFlag;
    private String topAdminName;
    private String topAdminToken;


    public String getTopAdminName() {
        return topAdminName;
    }

    public void setTopAdminName(String topAdminName) {
        this.topAdminName = topAdminName;
    }

    public String getTopAdminToken() {
        return topAdminToken;
    }

    public void setTopAdminToken(String topAdminToken) {
        this.topAdminToken = topAdminToken;
    }

    public Byte getIsTopAdminFlag() {
        return isTopAdminFlag;
    }

    public void setIsTopAdminFlag(Byte isTopAdminFlag) {
        this.isTopAdminFlag = isTopAdminFlag;
    }

    public Byte getIsSystemAdminFlag() {
        return isSystemAdminFlag;
    }

    public void setIsSystemAdminFlag(Byte isSystemAdminFlag) {
        this.isSystemAdminFlag = isSystemAdminFlag;
    }
}
