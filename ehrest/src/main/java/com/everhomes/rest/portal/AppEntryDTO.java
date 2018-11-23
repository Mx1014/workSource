// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entryId: 入口ID</li>
 *     <li>locationType: 入口分类,请参考{@link com.everhomes.rest.module.ServiceModuleLocationType}</li>
 *     <li>entryName: 入口名称</li>
 *     <li>iconUri: 入口图标uri</li>
 *     <li>iconUrl: 入口图标url</li>
 *     <li>sceneType: 1:管理端，2：用户端, 请参考{@link com.everhomes.rest.module.ServiceModuleSceneType}</li>
 *     <li>terminalType: 1：手机端，2：PC端  请参考{@link com.everhomes.rest.module.TerminalType}</li>
 * </ul>
 */
public class AppEntryDTO {
    private Long entryId;

    private Byte locationType;

    private String entryName;

    private String iconUri;

    private String iconUrl;

    private Byte sceneType;

    private Byte terminalType;

    public Byte getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Byte terminalType) {
        this.terminalType = terminalType;
    }

    public Byte getLocationType() {
        return locationType;
    }

    public void setLocationType(Byte locationType) {
        this.locationType = locationType;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Byte getSceneType() {
        return sceneType;
    }

    public void setSceneType(Byte sceneType) {
        this.sceneType = sceneType;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }


    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
