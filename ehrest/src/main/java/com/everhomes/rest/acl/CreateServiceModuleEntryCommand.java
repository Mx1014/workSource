package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>moduleId: moduleId</li>
 *     <li>entryName: entryName</li>
 *     <li>iconUri: iconUri</li>
 *     <li>terminalType: 终端类型 {@link com.everhomes.rest.module.TerminalType}</li>
 *     <li>locationType: 位置类型 {@link com.everhomes.rest.module.ServiceModuleLocationType}</li>
 *     <li>sceneType: 场景类型 {@link com.everhomes.rest.module.ServiceModuleSceneType}</li>
 *     <li>appCategoryId: appCategoryId</li>
 * </ul>
 */
public class CreateServiceModuleEntryCommand {

    private Long moduleId;
    private String entryName;
    private String iconUri;
    private Byte terminalType;
    private Byte locationType;
    private Byte sceneType;
    private Long appCategoryId;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }


    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
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

    public Long getAppCategoryId() {
        return appCategoryId;
    }

    public void setAppCategoryId(Long appCategoryId) {
        this.appCategoryId = appCategoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
