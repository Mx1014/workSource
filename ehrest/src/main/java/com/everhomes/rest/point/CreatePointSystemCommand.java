package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>displayName: 系统名称</li>
 *     <li>pointName: 积分名称</li>
 * </ul>
 */
public class CreatePointSystemCommand {

    @NotNull
    private String displayName;
    @NotNull
    private String pointName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
