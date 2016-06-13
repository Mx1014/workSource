// @formatter:off
package com.everhomes.rest.qrcode;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>logoUri: 二维码中间的图片URI</li>
 * <li>description: 描述</li>
 * <li>expireSeconds: 到期秒数（从当前时间算起多少秒到期），如果没有则认为是永久二维码，否则则是临时二维码</li>
 * <li>actionType: 操作类型，参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 * <li>actionData: 操作类型对应的参数，参考跳转相应文档</li>
 * </ul>
 */
public class NewQRCodeCommand {
    private String logoUri;
    private String description;
    private Long expireSeconds;
    private Byte actionType;
    private String actionData;
    
    public NewQRCodeCommand() {
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Long expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
