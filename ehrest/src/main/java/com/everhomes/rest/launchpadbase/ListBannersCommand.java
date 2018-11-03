// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>instanceConfig: instanceConfig</li>
 *     <li>context: 上下文信息context {@link AppContext}</li>
 *     <li>categoryId: 应用入口ID</li>
 * </ul>
 */
public class ListBannersCommand {

    private String instanceConfig;
    private AppContext context;
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
