// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id:id</li>
 *     <li>appOriginId: 应用originId</li>
 *     <li>appName: 应用名称</li>
 *     <li>appEntry: 应用入口</li>
 *     <li>appEntryCategory: 应用入口分类</li>
 *     <li>visibleFlag: 可见性，0：不可见，1：可见</li>
 *     <li>sortNum: 排序</li>
 *     <li>sceneType: 1：管理端，2：用户端</li>
 * </ul>
 */
public class WorkPlatformAppDTO {

    private Long id;

    private Long appOriginId;

    private String appName;

    private String appEntry;

    private String appEntryCategory;

    private Byte visibleFlag;

    private Integer sortNum;

    private Byte sceneType;

    public Byte getSceneType() {
        return sceneType;
    }

    public void setSceneType(Byte sceneType) {
        this.sceneType = sceneType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppOriginId() {
        return appOriginId;
    }

    public void setAppOriginId(Long appOriginId) {
        this.appOriginId = appOriginId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppEntry() {
        return appEntry;
    }

    public void setAppEntry(String appEntry) {
        this.appEntry = appEntry;
    }

    public String getAppEntryCategory() {
        return appEntryCategory;
    }

    public void setAppEntryCategory(String appEntryCategory) {
        this.appEntryCategory = appEntryCategory;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
