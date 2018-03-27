// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>layoutId: layoutId</li>
 *     <li>itemGroup: 当前item归属哪个组，来源layout里的itemGroup</li>
 *     <li>content: 上下文信息content {@link com.everhomes.rest.launchpadbase.ContentDTO}</li>
 * </ul>
 */
public class ListLaunchPadAppsCommand {

    private Long layoutId;
    private String itemGroup;
    private ContentDTO content;

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

    public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
