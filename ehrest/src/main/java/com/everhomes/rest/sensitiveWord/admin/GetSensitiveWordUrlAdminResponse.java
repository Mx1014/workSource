// @formatter:off
package com.everhomes.rest.sensitiveWord.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>url: 敏感词文本URL</li>
 * </ul>
 */
public class GetSensitiveWordUrlAdminResponse {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
