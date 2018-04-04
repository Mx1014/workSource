package com.everhomes.banner;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>actionType: actionType</li>
 *     <li>actionData: actionData</li>
 * </ul>
 */
public class BannerTargetHandleResult {

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
