package com.everhomes.rest.acl;

import com.everhomes.rest.module.ServiceModuleLocationType;
import com.everhomes.rest.module.TerminalType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entryName: 应用入口名称</li>
 *     <li>terminalType: 终端类型 {@link com.everhomes.rest.module.TerminalType}</li>
 *     <li>locationType: 位置类型 {@link com.everhomes.rest.module.ServiceModuleLocationType}</li>
 *     <li>sceneType: 形态类型 {@link com.everhomes.rest.module.ServiceModuleSceneType}</li>
 * </ul>
 */
public class AppEntryInfoDTO {
    private String entryName;
    private Byte terminalType;
    private Byte locationType;
    private Byte sceneType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

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

    public Byte getSceneType() {
        return sceneType;
    }

    public void setSceneType(Byte sceneType) {
        this.sceneType = sceneType;
    }
}
