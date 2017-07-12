package com.everhomes.rest.aclink;


import javax.validation.constraints.NotNull;

public class AclinkCreateAllDoorAuthListCommand {

    @NotNull
    private Long     doorId;

    @NotNull
    private Byte     authType;

    private Byte rightOpen;

    private Byte rightVisitor;

    private Byte rightRemote;

    private Integer namespaceId;

    private String keyword;

    private Long organizationId;

    private Byte isAuth;

    private Byte isOpenAuth;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Byte getAuthType() {
        return authType;
    }

    public void setAuthType(Byte authType) {
        this.authType = authType;
    }

    public Byte getRightOpen() {
        return rightOpen;
    }

    public void setRightOpen(Byte rightOpen) {
        this.rightOpen = rightOpen;
    }

    public Byte getRightVisitor() {
        return rightVisitor;
    }

    public void setRightVisitor(Byte rightVisitor) {
        this.rightVisitor = rightVisitor;
    }

    public Byte getRightRemote() {
        return rightRemote;
    }

    public void setRightRemote(Byte rightRemote) {
        this.rightRemote = rightRemote;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Byte isAuth) {
        this.isAuth = isAuth;
    }

    public Byte getIsOpenAuth() {
        return isOpenAuth;
    }

    public void setIsOpenAuth(Byte isOpenAuth) {
        this.isOpenAuth = isOpenAuth;
    }
}
