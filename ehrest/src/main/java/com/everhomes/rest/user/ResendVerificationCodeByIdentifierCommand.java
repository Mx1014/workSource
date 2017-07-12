package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;
/**
 * 通过手机或者邮箱重新发送验证码
 * @author elians
 * <ul>
 * <li>identifier:用户手机号</li>
 * <li>namespaceId:名字空间ID</li>
 * </ul>
 */
public class ResendVerificationCodeByIdentifierCommand {
    @NotNull
    private String identifier;
    
    private Integer namespaceId;

    private Integer regionCode;

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

}
