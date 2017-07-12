package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>displayMode: 显示类型</li>
 * </ul>
 */
public class ServiceAllianceDisplayModeDTO {

    private Byte displayMode;

    public Byte getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(Byte displayMode) {
        this.displayMode = displayMode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
