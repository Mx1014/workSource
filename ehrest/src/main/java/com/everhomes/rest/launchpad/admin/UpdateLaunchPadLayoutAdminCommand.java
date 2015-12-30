// @formatter:off
package com.everhomes.rest.launchpad.admin;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 命名空间</li>
 * <li>name: 名称</li>
 * <li>layoutJson: 服务市场风格json</li>
 * <li>versionCode: 当前版本号</li>
 * <li>minVersionCode: 当前版本支持的最小版本号</li>
 * <li>status: 服务市场风格状态，参考{@link com.everhomes.rest.launchpad.LaunchPadLayoutStatus}</li>
 * </ul>
 */
public class UpdateLaunchPadLayoutAdminCommand {
    @NotNull
    private Long     id;
    private Integer  namespaceId;
    private String   name;
    private String   layoutJson;
    private Long     versionCode;
    private Long     minVersionCode;
    private Byte     status;

    public UpdateLaunchPadLayoutAdminCommand() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getLayoutJson() {
        return layoutJson;
    }

    public void setLayoutJson(String layoutJson) {
        this.layoutJson = layoutJson;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public Long getMinVersionCode() {
        return minVersionCode;
    }

    public void setMinVersionCode(Long minVersionCode) {
        this.minVersionCode = minVersionCode;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
