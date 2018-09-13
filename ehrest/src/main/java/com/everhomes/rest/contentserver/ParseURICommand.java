package com.everhomes.rest.contentserver;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul> 上传控件的会话 ID，用于处理后续的一系列动作
 * <li>uploadId: 上传会话 ID</li>
 * </ul>
 */
public class ParseURICommand {

    @NotNull
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
