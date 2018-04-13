package com.everhomes.rest.acl;

/**
 * <ul>
 * <li>userId：用户Id</li>
 * <li>detailId：人员档案id</li>
 * <li>nickName: 用户名称</li>
 * <li>cotactName: 人员档案名称</li>
 * <li>isTopAdminFlag：是否是超级管理员</li>
 * <li>isSystemAdminFlag：是否是系统管理员</li>
 * </ul>
 */
public class GetPersonelInfoByTokenResponse {
    private Long userId;
    private Long detailId;
    private String nickName;
    private String cotactName;
    private Byte isTopAdminFlag;
    private Byte isSystemAdminFlag;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCotactName() {
        return cotactName;
    }

    public void setCotactName(String cotactName) {
        this.cotactName = cotactName;
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
