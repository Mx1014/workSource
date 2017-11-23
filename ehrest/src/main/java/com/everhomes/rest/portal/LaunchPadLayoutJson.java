// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.launchpad.LaunchPadLayoutGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>versionCode: 版本号2015072810</li>
 * <li>versionName: 版本名称如3.0.0</li>
 * <li>layoutName: layout名字</li>
 * <li>displayName: 显示名字，如服务市场首页</li>
 * <li>groups: 该layout的组件列表，参考{@link LaunchPadLayoutGroupDTO}</li>
 * </ul>
 */
public class LaunchPadLayoutJson {

    private String  versionCode;
    private String  versionName;
    private String   layoutName;
    private String   displayName;
    @ItemType(LaunchPadLayoutGroup.class)
    private List<LaunchPadLayoutGroup>  groups;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
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

    public List<LaunchPadLayoutGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<LaunchPadLayoutGroup> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
