// @formatter:off
package com.everhomes.rest.ui.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>filterType: 组件过滤器类型，在layout中的instanceConfig中定义，不使用location是因为相同过滤器的组件可以放置到任意地方、location并不方便于作判断，{@link com.everhomes.rest.ui.forum.PostFilterType}</li>
 * </ul>
 */
public class GetTopicQueryFilterCommand {
    private String sceneToken;
    
    private String filterType;

    public GetTopicQueryFilterCommand() {
    }

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
