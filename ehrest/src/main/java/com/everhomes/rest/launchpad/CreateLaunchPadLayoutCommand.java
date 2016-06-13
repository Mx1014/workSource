// @formatter:off
package com.everhomes.rest.launchpad;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 命名空间</li>
 * <li>name: 名称，取值为：ServiceMarketLayout（服务市场首页layout），PmLayout（物业首页layout）</li>
 * <li>layoutJson: 服务市场风格json</li>
 * <li>versionCode: 当前版本号</li>
 * <li>minVersionCode: 当前版本支持的最小版本号</li>
 * <li>status: 服务市场风格状态，参考{@link com.everhomes.rest.launchpad.LaunchPadLayoutStatus}</li>
 * </ul>
 */
public class CreateLaunchPadLayoutCommand {
    
    private Integer  namespaceId;
    private String   name;
    @NotNull
    private String   layoutJson;
    @NotNull
    private Long     versionCode;
    @NotNull
    private Long     minVersionCode;
    private Byte     status;

    public CreateLaunchPadLayoutCommand() {
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
