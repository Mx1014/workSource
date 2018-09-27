package com.everhomes.banner;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>appId: appId</li>
 *     <li>actionType: actionType</li>
 *     <li>actionData: actionData</li>
 *     <li>appName: appName</li>
 * </ul>
 */
public class BannerTargetHandleResult {

    private Long appId;
    private Byte actionType;
    private String actionData;
    private String appName;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
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
