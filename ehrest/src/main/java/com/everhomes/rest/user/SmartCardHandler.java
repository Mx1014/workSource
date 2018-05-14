package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: 独一无二的回调处理名字</li>
 * <li>moduleId: 业务模块 ID</li>
 * <li>appId: 业务应用 ID</li>
 * <li>data: 业务自己的数据</li>
 * </ul>
 * @author janson
 *
 */
public class SmartCardHandler {
    private String name;
    private Long moduleId;
    private Long appId;
    private String data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
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
