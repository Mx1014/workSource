// @formatter:off
package com.everhomes.rest.qrcode;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>qrid: 二维码ID</li>
 *     <li>logoUri: 二维码中间的图片URI</li>
 *     <li>logoUrl: 二维码中间的图片URL</li>
 *     <li>description: 描述</li>
 *     <li>expireTime: 到期时间（从1970年1月1日0时0分到指定时间的毫秒数），如果没有则认为是永久二维码，否则则是临时二维码</li>
 *     <li>actionType: 操作类型，参考{@link com.everhomes.rest.launchpad.ActionType}</li>
 *     <li>actionData: 操作类型对应的参数，参考跳转相应文档</li>
 *     <li>status: 状态，参考{@link com.everhomes.rest.qrcode.QRCodeStatus}</li>
 *     <li>createTime: 创建时间</li>
 *     <li>url: 二维码URL</li>
 *     <li>routeUri: 路由</li>
 *     <li>handler: handler</li>
 *     <li>extra: extra</li>
 * </ul>
 */
public class QRCodeDTO {

    private String qrid;
    private String logoUri;
    private String logoUrl;
    private String description;
    private Timestamp expireTime;
    private Byte actionType;
    private String actionData;
    private Byte status;
    private Timestamp createTime;
    private String url;
    private String routeUri;
    private String handler;
    private String extra;

    public QRCodeDTO() {
    }

    public String getQrid() {
        return qrid;
    }

    public void setQrid(String qrid) {
        this.qrid = qrid;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getRouteUri() {
        return routeUri;
    }

    public void setRouteUri(String routeUri) {
        this.routeUri = routeUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
