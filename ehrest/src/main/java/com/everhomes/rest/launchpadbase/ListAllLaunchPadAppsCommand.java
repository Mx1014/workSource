// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.launchpad.Widget;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>layoutId: layoutId</li>
 *     <li>widget: 组件 参考{@link Widget}</li>
 *     <li>instanceConfig: instanceConfig</li>
 *     <li>context: 上下文信息context {@link AppContext}</li>
 * </ul>
 */
public class ListAllLaunchPadAppsCommand {

    private Long layoutId;
    private String widget;
    private String instanceConfig;
    private AppContext context;

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    public AppContext getContext() {
        return context;
    }

    public void setContext(AppContext context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
