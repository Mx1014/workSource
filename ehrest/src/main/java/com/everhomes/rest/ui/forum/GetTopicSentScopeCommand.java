// @formatter:off
package com.everhomes.rest.ui.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>scopeType: 范围类型，由于不只一个地方需要用到发送菜单列表，需要使用类型来标识，{@link com.everhomes.rest.ui.forum.PostSentScopeType}</li>
 * </ul>
 */
public class GetTopicSentScopeCommand {
    private String sceneToken;
    
    private String scopeType;

    public GetTopicSentScopeCommand() {
    }

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
