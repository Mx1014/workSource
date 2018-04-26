// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.launchpad.Widget;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>instanceConfig: instanceConfig</li>
 *     <li>context: 上下文信息context {@link ContextDTO}</li>
 * </ul>
 */
public class ListBannersCommand {

    private String instanceConfig;
    private ContextDTO context;

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
