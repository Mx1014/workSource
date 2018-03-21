package com.everhomes.rest.launchpadbase;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>versionCode: versionCode</li>
 *     <li>layoutName: layoutName</li>
 *     <li>displayName: displayName</li>
 *     <li>groups: groups  参考 {@link ItemGroupDTO}</li>
 * </ul>
 */
public class LayoutJsonDTO {

    private String versionCode;
    private String layoutName;
    private String displayName;
    private List<ItemGroupDTO> groups;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<ItemGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<ItemGroupDTO> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
