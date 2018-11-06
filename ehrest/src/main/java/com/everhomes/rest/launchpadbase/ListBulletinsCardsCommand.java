// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.launchpadbase.groupinstanceconfig.OPPush;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>layoutId: layoutId</li>
 *     <li>instanceConfig: 从layout的group中获取，参考{@link OPPush}。其中itemGroup字段包含了组件信息，例如活动为OPPushActivity</li>
 *     <li>context: context {@link AppContext}</li>
 * </ul>
 */
public class ListBulletinsCardsCommand {

    private Long layoutId;

    private String instanceConfig;

    private AppContext context;

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
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
