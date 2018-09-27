package com.everhomes.rest.banner.targetdata;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>originId: 应用id</li>
 *     <li>name: 应用名称</li>
 *     <li>actionType: actionType</li>
 *     <li>actionData: actionData</li>
 * </ul>
 */
public class BannerAppTargetData {

    private Long moduleId;
    private Long originId;
    private String name;

    private Byte actionType;
    private String actionData;


    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
