// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>layoutId: layoutId</li>
 *     <li>itemGroup: 当前item归属哪个组，来源layout里的itemGroup</li>
 *     <li>context: 上下文信息context {@link ContextDTO}</li>
 * </ul>
 */
public class ListLaunchPadAppsCommand {

    private Long layoutId;
    private String itemGroup;
    private ContextDTO context;

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
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
