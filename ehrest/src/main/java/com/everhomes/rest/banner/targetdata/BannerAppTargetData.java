package com.everhomes.rest.banner.targetdata;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>appId: 应用id</li>
 *     <li>appName: 应用名称</li>
 *     <li>actionType: actionType</li>
 *     <li>actionData: actionData</li>
 * </ul>
 */
public class BannerAppTargetData {

    private Long appId;
    private String appName;

    private Byte actionType;
    private String actionData;

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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
