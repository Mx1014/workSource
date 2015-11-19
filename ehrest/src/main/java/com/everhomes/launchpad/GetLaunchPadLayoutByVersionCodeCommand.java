// @formatter:off
package com.everhomes.launchpad;



import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: layout的名称</li>
 * <li>versionCode: 当前版本号</li>
 * <li>siteUri: 链接</li>
 * </ul>
 */
public class GetLaunchPadLayoutByVersionCodeCommand {
    
    private Long     versionCode;
    @NotNull
    private String   name;
    @NotNull
    private Integer namespaceId;

    public GetLaunchPadLayoutByVersionCodeCommand() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
