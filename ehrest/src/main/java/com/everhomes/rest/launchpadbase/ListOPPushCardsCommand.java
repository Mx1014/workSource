// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.rest.launchpadbase.groupinstanceconfig.OPPush;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>content: content {@link com.everhomes.rest.launchpadbase.ContentDTO}</li>
 *     <li>layoutId: layoutId</li>
 *     <li>instanceConfig: 从layout的group中获取，参考{@link OPPush}。其中itemGroup字段包含了组件信息，例如活动为OPPushActivity</li>
 * </ul>
 */
public class ListOPPushCardsCommand {

    private Long layoutId;

    private String instanceConfig;

    private ContentDTO content;

    public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
