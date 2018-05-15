package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>moduleId: 业务模块 ID</li>
 * <li>appOriginId: 业务应用 ID，因为 appId 被拿来干别的意思了，这个字段与服务广场的保持一致 </li>
 * <li>data: 业务自己的数据</li>
 * </ul>
 * @author janson
 *
 */
public class SmartCardHandler {
    private Long moduleId;
    private Long appOriginId;
    private String data;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAppOriginId() {
        return appOriginId;
    }

    public void setAppOriginId(Long appOriginId) {
        this.appOriginId = appOriginId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
