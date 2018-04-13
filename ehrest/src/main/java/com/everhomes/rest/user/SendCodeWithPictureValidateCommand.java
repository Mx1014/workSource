package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>identifier: 用户手机号</li>
 *     <li>namespaceId: 名字空间ID</li>
 *     <li>pictureCode: 图片验证码</li>
 *     <li>regionCode: regionCode</li>
 * </ul>
 */
public class SendCodeWithPictureValidateCommand {
    @NotNull
    private String identifier;

    private Integer namespaceId;

    private String pictureCode;

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

    public String getPictureCode() {
        return pictureCode;
    }

    public void setPictureCode(String pictureCode) {
        this.pictureCode = pictureCode;
    }
}
