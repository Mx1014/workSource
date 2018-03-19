package com.everhomes.rest.launchpadbase;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: 应用名称</li>
 *     <li>iconUrl: iconUrl</li>
 *     <li>router: router</li>
 *     <li>instanceConfig: 参数json</li>
 * </ul>
 */
public class AppDTO {

    private String name;
    private String iconUrl;
    private String router;
    private String instanceConfig;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
